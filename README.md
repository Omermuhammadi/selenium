# BlogApp - Selenium Testing & Jenkins CI/CD Assignment

A simple blog application built with Node.js, Express, and PostgreSQL for demonstrating automated Selenium testing with Jenkins CI/CD pipeline.

## Features
- User registration and authentication
- Create and read blog posts
- Comment system
- Session management
- 
- Responsive Bootstrap UI

## Technology Stack
- **Backend**: Node.js, Express.js
- **Database**: PostgreSQL
- **Frontend**: EJS templating, Bootstrap 5
- **Containerization**: Docker, Docker Compose
- **CI/CD**: Jenkins
- **Testing**: Selenium WebDriver (Java + TestNG + Headless Chrome)
- **Deployment**: AWS EC2

## Quick Start with Docker

### Prerequisites
- Docker & Docker Compose installed
- Git

### Local Development
```bash
# Clone the repository
git clone https://github.com/Omermuhammadi/blogapp-docker-cicd.git
cd blogApp

# Start the application
docker-compose up -d

# Access the application at http://localhost:3000
```

## Project Structure
```
blogApp/
├── app.js                 # Main Express application
├── package.json           # Node.js dependencies
├── Dockerfile             # App container configuration
├── docker-compose.yml     # Multi-container orchestration
├── docker-entrypoint.sh   # Container startup script
├── Jenkinsfile            # CI/CD pipeline definition
├── config/                # Database configuration
├── models/                # Sequelize ORM models
├── migrations/            # Database migrations
├── views/                 # EJS templates
└── selenium-tests/        # Selenium test cases (Part-I)
    ├── pom.xml            # Maven configuration
    └── src/test/java/     # Java test classes
        └── com/blogapp/tests/
            ├── BaseTest.java
            ├── RegistrationTest.java
            ├── LoginTest.java
            ├── HomePageTest.java
            └── BlogPostTest.java
```

## Assignment Components

### Part-I: Selenium Test Cases (16 Tests)
| Test Class | Tests | Description |
|------------|-------|-------------|
| RegistrationTest | 1-4 | User registration functionality |
| LoginTest | 5-8 | User login/authentication |
| HomePageTest | 9-13 | Home page and navigation |
| BlogPostTest | 14-16 | Blog post functionality |

**Running Tests Locally (requires Java 11 & Maven):**
```bash
cd selenium-tests
mvn test -DbaseUrl=http://localhost:3000
```

### Part-II: Jenkins CI/CD Pipeline
- GitHub webhook trigger on push
- Containerized test execution with headless Chrome
- Email notifications with test results

## Jenkins Pipeline Stages
1. **Checkout** - Clone code from GitHub
2. **Build & Deploy** - Build Docker images and start containers
3. **Health Check** - Verify application is running
4. **Selenium Tests** - Run automated tests in Docker container
5. **Notify** - Email test results to committer

## Author
Omer Muhammadi

## License
MIT
