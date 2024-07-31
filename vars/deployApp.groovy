def call() {
                    sh """ 
                    rm -rf cluster_dir
                    git clone -b App_Kube_cluster https://github.com/amgad96/EKS_Full_React_App.git cluster_dir
                    cd cluster_dir
                    kubectl apply -f DB-PersistentVol.yaml
                    kubectl apply -f MongoDB-DS.yaml
                    kubectl apply -f Backend-DS.yaml
                    kubectl apply -f Frontend-DS.yaml
                    cd ..
                    rm -rf cluster_dir
                    """
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
