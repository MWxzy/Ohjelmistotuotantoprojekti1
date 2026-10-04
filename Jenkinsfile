pipeline {
    agent any

    tools {
        maven 'maven-3.9'
    }

    environment {
        DOCKER_IMAGE = 'junkkari/temperature-converter-gui:latest'
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'
        PATH = "C:\\Users\\kaspe\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin;${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Build') {
            steps { bat 'mvn clean package' }
        }

        stage('Test') {
            steps { bat 'mvn test' }
        }

        stage('Code Coverage') {
            steps { bat 'mvn jacoco:report' }
        }

        stage('Publish Test Results') {
            steps { junit '**/target/surefire-reports/*.xml' }
        }

        stage('Publish Coverage Report') {
            steps { jacoco() }
        }

        stage('Build Docker Image') {
            steps {
                // --no-cache ensures the multi-stage build reruns inside Docker
                bat "docker build --no-cache -t ${env.DOCKER_IMAGE} ."
            }
        }

        stage('Verify Image') {
            steps {
                bat "docker run --rm --entrypoint ls ${env.DOCKER_IMAGE} -la /app"
            }
        }

        stage('Deploy to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${env.DOCKER_CREDENTIALS_ID}",
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS')]) {
                    bat "docker login -u ${env.DOCKER_USER} -p ${env.DOCKER_PASS}"
                    bat "docker push ${env.DOCKER_IMAGE}"
                }
            }
        }
    }
}