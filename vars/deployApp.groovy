def call() {
        /* environment {
        AWS_REGION = 'us-east-1' // Replace with your AWS region
        EKS_CLUSTER_NAME = 'prod_eks_cluster' // Replace with your EKS cluster name
        }*/
        withCredentials([usernamePassword(credentialsId: 'AWS_CREDENTIALS', usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY')]) {
                    sh """
                    # Configure AWS CLI
                    aws configure set aws_access_key_id ${AWS_ACCESS_KEY_ID}
                    aws configure set aws_secret_access_key ${AWS_SECRET_ACCESS_KEY}
                    aws configure set default.region us-east-1
                    
                    # Update kubeconfig for EKS
                    #aws eks --region ${AWS_REGION} update-kubeconfig --name ${EKS_CLUSTER_NAME}
                    
                    aws eks --region us-east-1 update-kubeconfig --name prod_eks_cluster
                    
                    # Apply yaml files
                    rm -rf cluster_dir
                    git clone -b App_Kube_cluster https://github.com/amgad96/EKS_Full_React_App.git cluster_dir
                    cd cluster_dir
                    sudo kubectl apply -f DB-PersistentVol.yaml
                    sudo kubectl apply -f MongoDB-DS.yaml
                    sudo kubectl apply -f Backend-DS.yaml
                    sudo kubectl apply -f Frontend-DS.yaml
                    cd ..
                    rm -rf cluster_dir
                    """
    }
    /*stage('Clone Repository and Apply Kubernetes Manifests') {
            steps {
                git branch: 'App_Kube_cluster', url: 'https://github.com/amgad96/Full_React_App.git'
                
                sh """
                kubectl apply -f cluster_dir/DB-PersistentVol.yaml
                kubectl apply -f cluster_dir/MongoDB-DS.yaml
                kubectl apply -f cluster_dir/Backend-DS.yaml
                kubectl apply -f cluster_dir/Frontend-DS.yaml
                """
            }*/
}
