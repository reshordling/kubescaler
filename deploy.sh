#!/bin/bash

### set docker env
eval $(minikube docker-env)

### build the repository
./mvnw clean install

### build the docker images on minikube
docker build -t kubescaler/base base/
docker build -t kubescaler/main main/
docker build -t kubescaler/stream stream/

# Bind access
kubectl create clusterrolebinding default-admin --clusterrole cluster-admin --serviceaccount=default:default


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

kubectl create -f main/k8s/service.yaml
kubectl create -f main/k8s/deployment.yaml
kubectl create -f main/k8s/hpa.yaml

kubectl create -f stream/k8s/service.yaml
kubectl create -f stream/k8s/deployment.yaml
kubectl create -f stream/k8s/hpa.yaml

kubectl create -f ingress/gateway.yaml

# Check that the pods are running
kubectl get pods -w
