#!/bin/bash

### set docker env
eval $(minikube docker-env)

### build the repository
mvn clean install

### build the docker images on minikube
docker build -t kubescaler/user user/
docker build -t kubescaler/base base/

# Bind access
kubectl create clusterrolebinding default-admin --clusterrole cluster-admin --serviceaccount=default:default

#### secret and mongodb
#kubectl delete -f travel-agency-service/secret.yaml
#kubectl delete -f travel-agency-service/mongo-deployment.yaml
#
#kubectl create -f travel-agency-service/secret.yaml
#kubectl create -f travel-agency-service/mongo-deployment.yaml
#
#### travel-agency-service
#kubectl delete -f travel-agency-service/travel-agency-deployment.yaml
#kubectl create -f travel-agency-service/travel-agency-deployment.yaml
#
#
### user-service
#kubectl delete configmap user-service || true
#kubectl delete -f user-service/user-service-deployment.yaml || true


# CPU based autoscaling, for custom metrics use prometheus
# see https://learnk8s.io/blog/scaling-spring-boot-microservices/
kubectl create -f monitoring/metrics-server
kubectl create -f monitoring/namespaces.yaml
kubectl create -f monitoring/prometheus
kubectl create -f monitoring/custom-metrics-api

kubectl create -f cassandra/service.yaml
kubectl create -f cassandra/stateful-set.yaml

kubectl create -f redis/service.yaml
kubectl create -f redis/deployment.yaml

kubectl create -f base/k8s/service.yaml
kubectl create -f base/k8s/deployment.yaml

#kubectl create -f user/k8s/config.yaml
#kubectl create -f user/k8s/service.yaml
#kubectl create -f user/k8s/deployment.yaml
#kubectl create -f user/k8s/hpa.yaml

kubectl create -f ingress/gateway.yaml

# Check that the pods are running
kubectl get pods
