pipeline {
    agent any
    
    environment {
        APP_NAME = 'blogapp'
        DOCKER_IMAGE = 'blogapp:latest'
        // Get the host IP for Selenium tests to access the app
        HOST_IP = sh(script: "hostname -I | awk '{print \$1}'", returnStdout: true).trim()
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
                echo '🚀 Deploying application with Docker Compose...'
                sh '''
                    docker-compose down --remove-orphans || true
                    docker-compose up -d
                '''
            }
        }
        
        stage('Health Check') {
            steps {
                echo '🏥 Waiting for application to be ready...'
                sh '''
                    echo "Waiting for containers to start..."
                    sleep 20
                    
                    echo "Checking application health..."
                    for i in 1 2 3 4 5 6; do
                        if curl -f -s http://localhost:3000/ > /dev/null; then
                            echo "✅ Application is healthy and ready!"
                            exit 0
                        fi
                        echo "Attempt $i: Application not ready yet, waiting..."
                        sleep 10
                    done
                    
                    echo "❌ Application failed to start"
                    docker-compose logs
                    exit 1
                '''
            }
        }
        
        stage('Run Selenium Tests') {
            steps {
                echo '🧪 Running Selenium Tests in Docker container...'
                sh '''
                    echo "========================================="
                    echo "🧪 PART-II: Selenium Automated Tests"
                    echo "========================================="
                    echo "Using markhobson/maven-chrome Docker image"
                    echo "Testing against: http://${HOST_IP}:3000"
                    echo "========================================="
                    
                    # Run Selenium tests using markhobson/maven-chrome image
                    # This image contains Chrome, ChromeDriver, Maven, and JDK
                    docker run --rm \
                        --network host \
                        -v "${WORKSPACE}/selenium-tests:/app" \
                        -w /app \
                        markhobson/maven-chrome:jdk-11 \
                        mvn clean test -DbaseUrl=http://${HOST_IP}:3000
                    
                    echo "========================================="
                    echo "✅ Selenium Tests Completed!"
                    echo "========================================="
                '''
            }
            post {
                always {
                    // Archive test results
                    junit allowEmptyResults: true, testResults: 'selenium-tests/target/surefire-reports/*.xml'
                }
            }
        }
    }
    
    post {
        success {
            echo '''
            =========================================
            ✅ PIPELINE COMPLETED SUCCESSFULLY!
            =========================================
            ✓ Code checked out from GitHub
            ✓ Docker image built
            ✓ Application deployed
            ✓ Health check passed
            ✓ Selenium tests executed
            =========================================
            '''
            emailext (
                subject: "✅ SUCCESS: BlogApp Pipeline #${BUILD_NUMBER}",
                body: """
                <h2>✅ Pipeline Completed Successfully!</h2>
                <p><b>Project:</b> ${JOB_NAME}</p>
                <p><b>Build Number:</b> ${BUILD_NUMBER}</p>
                <p><b>Build URL:</b> <a href="${BUILD_URL}">${BUILD_URL}</a></p>
                <hr>
                <h3>Summary:</h3>
                <ul>
                    <li>✓ Code checked out from GitHub</li>
                    <li>✓ Docker image built</li>
                    <li>✓ Application deployed</li>
                    <li>✓ Health check passed</li>
                    <li>✓ All 16 Selenium tests passed</li>
                </ul>
                <hr>
                <p><i>This is an automated email from Jenkins CI/CD Pipeline</i></p>
                """,
                to: 'omermuhammadi03@gmail.com',
                mimeType: 'text/html'
            )
        }
        failure {
            echo '''
            =========================================
            ❌ PIPELINE FAILED!
            =========================================
            '''
            sh 'docker-compose logs || true'
            emailext (
                subject: "❌ FAILED: BlogApp Pipeline #${BUILD_NUMBER}",
                body: """
                <h2>❌ Pipeline Failed!</h2>
                <p><b>Project:</b> ${JOB_NAME}</p>
                <p><b>Build Number:</b> ${BUILD_NUMBER}</p>
                <p><b>Build URL:</b> <a href="${BUILD_URL}">${BUILD_URL}</a></p>
                <hr>
                <p>Please check the <a href="${BUILD_URL}console">console output</a> for details.</p>
                <hr>
                <p><i>This is an automated email from Jenkins CI/CD Pipeline</i></p>
                """,
                to: 'omermuhammadi03@gmail.com',
                mimeType: 'text/html'
            )
        }
        always {
            echo '🧹 Cleaning up...'
            sh '''
                # Keep containers running for demo purposes
                # Uncomment below to stop after pipeline:
                # docker-compose down || true
                echo "Containers are still running at http://localhost:3000"
            '''
        }
    }
}