#!/bin/bash

### set docker env
eval $(minikube docker-env)

### build the repository
mvn clean install

### build the docker images on minikube
cd user-service
docker build -t user-service .
cd ..

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
kubectl delete configmap user-service || true
kubectl delete -f user-service/user-service-deployment.yaml || true

kubectl create -f user-service/user-config.yaml
kubectl create -f user-service/user-service-deployment.yaml

# Check that the pods are running
kubectl get pods
