pipeline {
    agent any
    
    environment {
        APP_NAME = 'blogapp'
        DOCKER_IMAGE = 'blogapp:latest'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo '📥 Checking out code from GitHub...'
                checkout scm
            }
        }
        
        stage('Build Docker Image') {
            steps {
                echo '🐳 Building Docker image...'
                sh 'docker build -t ${DOCKER_IMAGE} .'
            }
        }
        
        stage('Deploy Application') {
            steps {
                echo '🚀 Deploying application...'
                sh '''
                    docker-compose down || true
                    docker-compose up -d
                '''
            }
        }
        
        stage('Health Check') {
            steps {
                echo '🏥 Waiting for application to start...'
                sh '''
                    sleep 15
                    for i in 1 2 3 4 5; do
                        if curl -f -s http://localhost:3000/ > /dev/null; then
                            echo "✅ Application is healthy"
                            exit 0
                        fi
                        echo "Attempt $i: Waiting..."
                        sleep 5
                    done
                    echo "❌ Health check failed"
                    exit 1
                '''
            }
        }
        
        // TODO: Add Selenium Test Stage (Part-II)
        
    }
    
    post {
        success {
            echo '✅ Pipeline completed successfully!'
        }
        failure {
            echo '❌ Pipeline failed!'
            sh 'docker-compose logs || true'
        }
    }
}