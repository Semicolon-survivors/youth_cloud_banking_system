pipeline {
    agent any

    environment {
        ACCOUNT_IMAGE = "nathiiiworld7/account-service"
        TRANSACTION_IMAGE = "nathiiiworld7/transaction-service"
        TAG = "latest"
    }

    stages {

        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        // ===== Account Service =====
        stage('Build Account Service') {
            steps {
                echo "Building Account Service..."
                dir('account-service') {
                    sh 'mvn clean package || true'
                }
            }
        }

        stage('Docker Build - Account Service') {
            steps {
                sh 'docker build -t $ACCOUNT_IMAGE:$TAG ./account-service'
            }
        }

        // ===== Transaction Service =====
        stage('Build Transaction Service') {
            steps {
                echo "Building Transaction Service..."
                dir('transaction-service') {
                    sh 'mvn clean package || true'
                }
            }
        }

        stage('Docker Build - Transaction Service') {
            steps {
                sh 'docker build -t $TRANSACTION_IMAGE:$TAG ./transaction-service'
            }
        }

        // ===== Push Images =====
        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: nathiiiworld7,
                        passwordVariable: Nkosinathi1$
                )]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh 'docker push $ACCOUNT_IMAGE:$TAG'
                    sh 'docker push $TRANSACTION_IMAGE:$TAG'
                }
            }
        }
    }

    post {
        success {
            echo 'Both microservices built and pushed successfully!'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
