@Library('Kube_cluster') _ // Load the shared library
pipeline {
    agent any 
        environment {
        AWS_REGION = 'us-east-1' // Replace with your AWS region
        EKS_CLUSTER_NAME = 'prod_eks_cluster' // Replace with your EKS cluster name
    }
    stages {
        stage('Access_AWS') {
            steps {
                    withCredentials([usernamePassword(credentialsId: 'AWS_CREDENTIALS', usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY')]) {
                    sh """
                    # Configure AWS CLI
                    aws configure set aws_access_key_id ${AWS_ACCESS_KEY_ID}
                    aws configure set aws_secret_access_key ${AWS_SECRET_ACCESS_KEY}
                    aws configure set default.region ${AWS_REGION}
                    
                    # Update kubeconfig for EKS
                    aws eks --region ${AWS_REGION} update-kubeconfig --name ${EKS_CLUSTER_NAME}
                    """
                }
                }
            }
        stage('Apply cluster yaml files') {
            steps {
                script {
                    deployApp()
                    echo "Yaml files Applied"
                    }
                }
            }
        }
    post {
        always {
            cleanWs() // Clean the workspace after the build
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
