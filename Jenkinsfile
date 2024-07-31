@Library('Kube_cluster') _ // Load the shared library
pipeline {
    agent any 
        environment {
        AWS_REGION = 'us-east-1' // Replace with your AWS region
        EKS_CLUSTER_NAME = 'DEVOPS_eks_cluster' // Replace with your EKS cluster name
    }
    stages {
        stage('Access_AWS') {                
            steps {
                withCredentials([file(credentialsId: 'AWS_CREDENTIALS_F', variable: 'AWS_CREDENTIALS_FILE')]) {
                    sh '''
                        # Configure AWS CLI
                        export AWS_SHARED_CREDENTIALS_FILE=${AWS_CREDENTIALS_FILE}
                        aws eks --region ${AWS_REGION} update-kubeconfig --name ${EKS_CLUSTER_NAME}
                        '''
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
