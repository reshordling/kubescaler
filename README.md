# kubescaler

<img width="1204" alt="Web-client" src="https://user-images.githubusercontent.com/17854317/60402280-1ae31480-9b96-11e9-81aa-5300831f0656.png">

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