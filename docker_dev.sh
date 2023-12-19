#!/bin/bash

docker compose --env-file .env -f docker/compose.dev.yaml --project-directory docker up
