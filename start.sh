#!/bin/bash

# Stop execution on any error
set -e

cd openmrs-module-bahmniapps
echo "🔧 Building bahmni-web-drc..."
docker build -t bahmni-web-drc:latest -f package/docker/Dockerfile .
cd - > /dev/null  # Return to previous directory quietly


cd default-config
echo "🔧 Building bahmni-config-drc..."
docker build -t bahmni-config-drc:latest -f package/docker/Dockerfile .
cd - > /dev/null

cd bahmni-proxy
echo "🔧 Building bahmni-proxy-drc..."
docker build -t bahmni-proxy-drc:latest -f Dockerfile .


cd - > /dev/null
echo "🚀 Running docker-compose..."
docker compose up -d