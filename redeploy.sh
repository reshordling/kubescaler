#!/bin/bash

eval $(minikube docker-env)
mvn clean install
docker build -t kubescaler/main main/

# does it really work?
kubectl set image deployment.v1.apps/main-deployment main=kubescaler/main
#kubectl delete -f user/k8s/deployment.yaml
#kubectl create -f user/k8s/deployment.yaml
kubectl get pods
