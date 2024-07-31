@Library('Kube_cluster') _ // Load the shared library
pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'Amgad-Docker-Cred'
        AWS_REGION = 'us-east-1' // Replace with your AWS region
        EKS_CLUSTER_NAME = 'DEVOPS_eks_cluster' // Replace with your EKS cluster name
    } 

    stages {
       stage('Installing Backend dependencies') {
                    steps {
                        script {
                            sh '''
                                npm update
                                npm install --save-dev jest supertest
                                npm install express
                                npm install --save-dev @babel/cli @babel/core @babel/preset-env
                                '''
                            }
                        }
                    }
                
      stage('Build Backend') {
            steps {
                    script {
                        sh 'npm run build'
                    }
                }
            }

        stage('Test Backend') {
            steps {
                    script {
                        sh 'npm test'
                    }
                }
            }

        stage('Build backend docker Image') {
            steps {
                // Extract version from package.json
                
                withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                script {
                    def version = sh(script: "jq -r .version package.json", returnStdout: true).trim()
                    sh """
                    echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin
                    docker build -t amgadashraf/fbackend:latest .
                    docker push amgadashraf/fbackend:latest 
                    docker logout
                        """
                    /* we could use use this command to use the same version of package.json
                       docker build -t amgadashraf/fbackend:"v${version}" .
                       docker push amgadashraf/fbackend:"v${version}" */
                        }
                }
            }
        }
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
        stage('Deploy') {
            steps {
                    // Add your deployment steps here
                    deployApp()
                    echo ' Backend Deployed ' 
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
