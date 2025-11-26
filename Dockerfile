# Use Node.js 20 as base image
FROM node:20-alpine

# Set working directory
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies (include devDependencies for sequelize-cli)
RUN npm install

# Copy application files
COPY . .

# Make entrypoint script executable
RUN chmod +x docker-entrypoint.sh

# Expose port
EXPOSE 3000

# Set environment variable
ENV NODE_ENV=production

# Use entrypoint script
ENTRYPOINT ["./docker-entrypoint.sh"]
