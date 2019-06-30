#!/bin/bash

minikube delete
minikube start \
  --memory 8192 --cpus 10 \
  --extra-config=controller-manager.horizontal-pod-autoscaler-upscale-delay=10s  \
  --extra-config=controller-manager.horizontal-pod-autoscaler-downscale-delay=2m \
  --extra-config=controller-manager.horizontal-pod-autoscaler-sync-period=10s