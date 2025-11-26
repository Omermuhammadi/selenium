#!/bin/sh

echo "Waiting for PostgreSQL to be ready..."
sleep 5

echo "Running database migrations..."
npx sequelize-cli db:migrate

echo "Starting the application..."
npm start
