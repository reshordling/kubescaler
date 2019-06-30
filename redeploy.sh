#!/bin/bash

eval $(minikube docker-env)
./mvnw clean install
docker build -t kubescaler/stream stream/

# does it really work?
#kubectl set image deployment.v1.apps/stream-deployment stream=kubescaler/stream
kubectl delete -f stream/k8s/deployment.yaml
kubectl create -f stream/k8s/deployment.yaml
kubectl get pods -w
