#!/bin/bash

# Stop execution on any error
set -e

echo "🛑 Stopping running containers..."
docker compose stop

echo "🧹 Bringing containers down and cleaning up..."
docker compose down

echo "✅ All done!"