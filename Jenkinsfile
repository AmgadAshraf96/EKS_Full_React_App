@Library('Kube_cluster') _ // Load the shared library
pipeline {
    agent any
//This command to check  
    environment {
        DOCKER_CREDENTIALS_ID = 'Amgad-Docker-Cred'
        AWS_REGION = 'us-east-1' // Replace with your AWS region
        EKS_CLUSTER_NAME = 'DEVOPS_eks_cluster' // Replace with your EKS cluster name
    }
    stages {           
      stage('Build Frontend') {
            steps {
                    script {
                        sh 'npm install'
                        sh 'npm run build'
                            }
                        }
                    }

        stage('Test Frontend') {
            steps {
                    script {
                        sh 'npm install'
                        sh 'npm test'
                    }
                }
            }
               

        stage('Build frontend docker Image') {
            steps {
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        script {
                            def version = sh(script: "jq -r .version package.json", returnStdout: true).trim()
                            sh """
                            echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin
                            docker build -t amgadashraf/ffrontend:"v${version}" .
                            docker push amgadashraf/ffrontend:"v${version}"
                            docker logout
                                """
                            /*
                              ## we could use these command to build and push the same version of package.json
                              docker build -t amgadashraf/ffrontend:"v${version}" .
                              docker push amgadashraf/ffrontend:"v${version}"
                              docker build -t amgadashraf/ffrontend:latest .
                              docker push amgadashraf/ffrontend:latest
                            */
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
                script{
                    // export the version from the package.json
                    def version = sh(script: "jq -r .version package.json", returnStdout: true).trim()
                    sh  """
                        kubectl patch configmap frontend-config --patch "{\\"data\\": {\\"IMAGE_TAG\\": \\"v${version}\\"}}"
                        """
                    // apply app yaml files
                    deployApp()
                    echo 'Frontend Deployed'
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

