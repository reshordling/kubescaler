# kubescaler

<img width="1204" alt="Web-client" src="https://user-images.githubusercontent.com/17854317/60402280-1ae31480-9b96-11e9-81aa-5300831f0656.png">

## Architecture

### Services

User[id] and Profile[id, users[id1, .., idN]] are tables in Cassandra DB. There is a secondary index on Profile.users field. Data is kept in persisted store

Base service (spring boot + java 12) implements CRUD for Cassandra and retrieves all profiles of the user from Cassandra . API exported as REST.

Redis in-memory stores information about locks (launched user-profile combination) and the list of launched profiles for the specified user. No information about hosts is stored. Records will expire in 1 min if not refreshed

Main service (scaled on CPU) communicates with Base service via REST (feign) and Redis. Main service provides helpers to create/drop/show random testdata (profiles with users, some users are bound to multiple profiles). It also exposes the list of launched profiles for the specified user - Main service asks the list of all user's profiles from Base service and then verifies if the user-profile pairs present in Redis in launched profiles. This functionality is the main service provided by Main service

Stream service (scaled on the nr of launched profiles on this instance) communicates with Base service via REST and Redis. It asks Base service if user and profile are bound. If they are bound, it tries to obtain expiring Lock in Redis using RedLock algorithm (will work if we need also scale Redis and use multiple instances in the future). If lock obtained, the service writes user-launched profile key to Redis (expires in 1 min, the information is stored only in 1 Redis instance, so if we decide to distribute Redis as well, we need to use Redlock approach) and refreshes the lock and the keypair until the connection is closed. It also increases/decreases the number of active connections (metrics used for scaling). The refresh is run asynchronously by scheduler. On error/disconnect the information is removed from the scheduled refresh. After obtaining lock Stream service starts sending random data to the client using WebFlux SSE (mostly used for streaming). If the user's channel is not wide enough, we allow skipping some messages (see onBackpressureDrop in WebFlux) on condition that user does not close the connection. This is required in order not to buffer too much data in Streaming service.

Streaming service also exports some static html/JS to implement the test agent. This should be moved to NGinx at some point in the future.

### Infrastructure

Kubernetes uses Ingress as entrypoint and Gateway. Inside live scalable services. Every service can have multiple instances (called 'pods' == Docker containers) that are accessed via Kubernetes load balancer (eg. accessing 'main-service.default.svc.cluster.local' goes to its load balancer that automatically chooses which 'main' pod responds). Kubernetes HPA scales up and down pods ('main' scaled based on CPU, 'stream' scaled based on custom metrics 'connections') - scaling means that HPA creates new 'pods' of desired services and registers them in load balancers. Pod startup is ordered (e.g 'main' service can be run only if there is working 'base' service and 'redis'), ordering is implemented using 'wait for' approach (busybox Docker containers in service startup). Also ordering (and autoscaling) is driven by health and readiness probes in services (see corresponding endpoints in services). Eg. if "readiness' probe is not responding, then HPA kills the pod and runs a new one. Custom metrics used by HPA are stored in Prometheus - it is also a service deployed to Kubernetes. Java services are containerised in adoptopenjdk12 alpine-healdess containers. They use spring-boot 2. Streams use Reactive WebFlux.

## Usual requests

### User list
http://****.us-west-2.compute.amazonaws.com/main/users

### Profile list with user IDs
http://****.us-west-2.compute.amazonaws.com/main/profiles

### Helper
List of users that have at least 2 profiles (only on the container that generated testdata)

http://****.us-west-2.compute.amazonaws.com/main/suggested

### Web client
HTTP Client + JS that connects to WebFlux SSE endpoint (load-balanced, scalable), load ~ 2 Mb/s. You can evict SSE stream elements more often (see org.springframework.boot.kubescaler.stream.repository.ReactiveMessageRepository). Adblock + Privacy badger + Decentraleyes pass the traffic in (tested on FF).

http://****.us-west-2.compute.amazonaws.com/main/USER_ID/PROFILE_ID

Opening the same user-profile pair locks it for concurrent requests (on the same machine or from another IP - does not matter). Disconnect (Also browser refresh) removes the lock in 1 min. Opening more than 5 connections makes the stream-service so scale up.

URL examples

http://****.us-west-2.compute.amazonaws.com/index/8a377bc6-c2e7-42db-bf80-36301e21f25e/3a3314a5-2ca3-4c75-9bbd-89fb6152f138

http://****.us-west-2.compute.amazonaws.com/index/8a377bc6-c2e7-42db-bf80-36301e21f25e/5c24dd1f-f3d3-40b7-a6f7-c09f4649279a

http://****.us-west-2.compute.amazonaws.com/index/8a377bc6-c2e7-42db-bf80-36301e21f25e/ddf2b289-0fb5-44bd-b340-0cca47f264b6

http://****.us-west-2.compute.amazonaws.com/index/8a377bc6-c2e7-42db-bf80-36301e21f25e/d19bd37e-c610-4cc4-890b-079120c11031


http://****.us-west-2.compute.amazonaws.com/index/a4c9302e-026a-4fc1-93c4-6422bea1760f/3a3314a5-2ca3-4c75-9bbd-89fb6152f138

http://****.us-west-2.compute.amazonaws.com/index/a4c9302e-026a-4fc1-93c4-6422bea1760f/5c24dd1f-f3d3-40b7-a6f7-c09f4649279a

http://****.us-west-2.compute.amazonaws.com/index/a4c9302e-026a-4fc1-93c4-6422bea1760f/ddf2b289-0fb5-44bd-b340-0cca47f264b6

http://****.us-west-2.compute.amazonaws.com/index/a4c9302e-026a-4fc1-93c4-6422bea1760f/d19bd37e-c610-4cc4-890b-079120c11031

### Currently active profiles for USER_ID
http://****.us-west-2.compute.amazonaws.com/main/login/USER_ID

eg.

http://****.us-west-2.compute.amazonaws.com/main/login/8a377bc6-c2e7-42db-bf80-36301e21f25e

http://****.us-west-2.compute.amazonaws.com/main/login/a4c9302e-026a-4fc1-93c4-6422bea1760f

## Minikube commands

```
ssh -i ext.public ubuntu@****.us-west-2.compute.amazonaws.com
sudo -i

# interactive list of containers (ctrl+C to exit)
kubectl get pods -w
```
Produces:
```
root@ip-172-31-45-201:~# kubectl get pods -w
NAME                                READY   STATUS    RESTARTS   AGE
base-deployment-9bbb48b69-fbblk     1/1     Running   0          91m
cassandra-0                         1/1     Running   0          91m
main-deployment-96556bc8d-krmqs     1/1     Running   0          91m
redis-668947bb88-ftfq6              1/1     Running   0          91m
stream-deployment-7668bf575-vn6bv   1/1     Running   0          91m
```

```
# non-interactive list of scalable services
kubectl get hpa
```
```
root@ip-172-31-45-201:~# kubectl get hpa
NAME         REFERENCE                      TARGETS   MINPODS   MAXPODS   REPLICAS   AGE
main-hpa     Deployment/main-deployment     0%/80%    1         3         1          92m
stream-hpa   Deployment/stream-deployment   0/5       1         5         1          92m
```
`main` autoscales if average CPU usage on a single instance is on average >= 80% CPU

`stream` autoscales if there are more than 5 active streaming connections on a single instance

### Dashboard
```
root@ip-172-31-45-201:~# minikube dashboard --url
Verifying dashboard health ...
Launching proxy ...
Verifying proxy health ...
http://127.0.0.1:42327/api/v1/namespaces/kube-system/services/http:kubernetes-dashboard:/proxy/
```
There are only 22 and 80 TCP ports available. This is why if one wants to use the dashboard, it should be done via SSH pipeline (port forwarding)