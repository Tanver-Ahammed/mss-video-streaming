#!/bin/bash

# Get the current working directory
current_dir=$(pwd)

# Define a cleanup function to kill both processes
cleanup() {
    echo "Terminating frontend and backend..."
    kill "$backend_pid" "$frontend_pid" 2>/dev/null
    exit
}

# Trap SIGINT (Ctrl+C) and call the cleanup function
trap cleanup SIGINT

# Navigate to the backend directory and start the backend process
cd "$current_dir"/video-streaming-backend || exit

pwd

mvn clean install

cd target || exit

pwd

java -jar video-streaming-backend-0.0.1-SNAPSHOT.jar &  # Run backend in the background
backend_pid=$!  # Store the backend PID

# Navigate to the frontend directory and start the frontend process
cd ../../video-stream-front-end-app || exit

npm install

npm run dev &  # Run frontend in the background
frontend_pid=$!  # Store the frontend PID

# Wait for both processes to finish (or be killed)
wait "$backend_pid" "$frontend_pid"
