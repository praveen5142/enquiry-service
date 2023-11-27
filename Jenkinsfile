pipeline {
    tools {
        maven 'Maven3'
    }
    agent any
    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://praveen5142@bitbucket.org/papadevelopers/core-service.git']]])
            }
        }
        stage('Build Jar') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Docker Image Build') {
            steps {
                sh 'docker build -t 641926501088.dkr.ecr.us-east-1.amazonaws.com/core-service:1.0 .'
            }
        }
        stage('Push Docker Image to ECR') {
            steps {
                withAWS(credentials: '<AWS_CREDENTIALS_ID>', region: '<AWS_REGION>') {
                    sh 'aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 641926501088.dkr.ecr.us-east-1.amazonaws.com'
                    sh 'docker push 641926501088.dkr.ecr.us-east-1.amazonaws.com/core-service:1.0'
                }
            }
        }
        stage('Integrate Jenkins with EKS Cluster and Deploy App') {
            steps {
                withAWS(credentials: '<AWS_CREDENTIALS_ID>', region: 'us-east-1') {
                  script {
                    sh ('aws eks update-kubeconfig --name dev --region us-east-1')
                    sh "kubectl apply -f kube/deployment.yaml"
                    sh "kubectl apply -f kube/service.yaml"
                }
                }
        }
    }
    }
}