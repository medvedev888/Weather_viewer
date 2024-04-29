#!/bin/bash

# Set the environment variables
export JAKARTA_PERSISTENCE_JDBC_URL="jdbc:postgresql://localhost:5432/weather_viewer"
export JAKARTA_PERSISTENCE_JDBC_USER="vladislavmedvedev"
export JAKARTA_PERSISTENCE_JDBC_PASSWORD=123

# Optionally, you can also set other environment variables required by your application

# Print a message indicating that the environment variables have been set
echo "Environment variables set:"
echo "JAKARTA_PERSISTENCE_JDBC_URL=${JAKARTA_PERSISTENCE_JDBC_URL}"
echo "JAKARTA_PERSISTENCE_JDBC_USER=${JAKARTA_PERSISTENCE_JDBC_USER}"
echo "JAKARTA_PERSISTENCE_JDBC_PASSWORD=${JAKARTA_PERSISTENCE_JDBC_PASSWORD}"