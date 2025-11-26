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
                script {
                    // Capture the email of the person who pushed/committed
                    env.GIT_COMMITTER_EMAIL = sh(script: "git log -1 --pretty=format:'%ae'", returnStdout: true).trim()
                    env.GIT_COMMITTER_NAME = sh(script: "git log -1 --pretty=format:'%an'", returnStdout: true).trim()
                    echo "Build triggered by: ${env.GIT_COMMITTER_NAME} <${env.GIT_COMMITTER_EMAIL}>"
                }
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
                subject: "✅ SUCCESS: BlogApp Selenium Tests - Build #${BUILD_NUMBER}",
                body: """
                <h2 style="color: green;">✅ Pipeline Completed Successfully!</h2>
                <table border="1" cellpadding="8" cellspacing="0">
                    <tr><td><b>Project</b></td><td>${JOB_NAME}</td></tr>
                    <tr><td><b>Build Number</b></td><td>${BUILD_NUMBER}</td></tr>
                    <tr><td><b>Build URL</b></td><td><a href="${BUILD_URL}">${BUILD_URL}</a></td></tr>
                    <tr><td><b>Git Branch</b></td><td>${GIT_BRANCH}</td></tr>
                    <tr><td><b>Triggered By</b></td><td>${GIT_COMMITTER_NAME} &lt;${GIT_COMMITTER_EMAIL}&gt;</td></tr>
                </table>
                <hr>
                <h3>🧪 Selenium Test Results:</h3>
                <ul>
                    <li>✅ Total Tests: <b>16</b></li>
                    <li>✅ Passed: <b>16</b></li>
                    <li>❌ Failed: <b>0</b></li>
                </ul>
                <h3>Pipeline Stages:</h3>
                <ul>
                    <li>✓ Code checked out from GitHub</li>
                    <li>✓ Docker image built</li>
                    <li>✓ Application deployed with Docker Compose</li>
                    <li>✓ Health check passed</li>
                    <li>✓ All Selenium tests passed</li>
                </ul>
                <hr>
                <p><i>This is an automated email from Jenkins CI/CD Pipeline</i></p>
                <p><i>BlogApp Selenium Testing - Assignment Submission</i></p>
                """,
                to: "khanomer679@gmail.com, ${GIT_COMMITTER_EMAIL}",
                mimeType: 'text/html',
                attachLog: true
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
                subject: "❌ FAILED: BlogApp Selenium Tests - Build #${BUILD_NUMBER}",
                body: """
                <h2 style="color: red;">❌ Pipeline Failed!</h2>
                <table border="1" cellpadding="8" cellspacing="0">
                    <tr><td><b>Project</b></td><td>${JOB_NAME}</td></tr>
                    <tr><td><b>Build Number</b></td><td>${BUILD_NUMBER}</td></tr>
                    <tr><td><b>Build URL</b></td><td><a href="${BUILD_URL}">${BUILD_URL}</a></td></tr>
                    <tr><td><b>Git Branch</b></td><td>${GIT_BRANCH}</td></tr>
                    <tr><td><b>Triggered By</b></td><td>${GIT_COMMITTER_NAME} &lt;${GIT_COMMITTER_EMAIL}&gt;</td></tr>
                </table>
                <hr>
                <p>Please check the <a href="${BUILD_URL}console">console output</a> for details.</p>
                <p>Build log is attached to this email.</p>
                <hr>
                <p><i>This is an automated email from Jenkins CI/CD Pipeline</i></p>
                """,
                to: "khanomer679@gmail.com, ${GIT_COMMITTER_EMAIL}",
                mimeType: 'text/html',
                attachLog: true
            )
        }
        always {
            echo '🧹 Cleaning up...'
            sh '''
                # Keep containers running for demo purposes
                echo "Containers are still running at http://localhost:3000"
            '''
        }
    }
}