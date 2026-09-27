pipeline {
    agent any

    tools {
        maven 'maven-3.9'
    }

    environment {
        DOCKER_IMAGE = 'junkkari/temperature-converter:latest'
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'
        PATH = "C:\\Users\\kaspe\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin;${env.PATH}"
        DOCKER_HOST = 'tcp://localhost:2375'
        DOCKER_API_VERSION = '1.44'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Code Coverage') {
            steps {
                bat 'mvn jacoco:report'
            }
        }

        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Publish Coverage Report') {
            steps {
                jacoco()
            }
        }

        stage('Build Docker Image') {
            steps {
                bat "docker build -t ${env.DOCKER_IMAGE} ."
            }
        }

        stage('Run Docker Image (Local Verification)') {
            steps {
                bat "docker run --rm ${env.DOCKER_IMAGE}"
            }
        }

        stage('Deploy to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${env.DOCKER_CREDENTIALS_ID}",
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS')]) {
                    bat "docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}"
                    bat "docker push ${env.DOCKER_IMAGE}"
                }
            }
        }
    }
}