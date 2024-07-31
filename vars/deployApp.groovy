def call() {
                    sh """ 
                    rm -rf cluster_dir
                    git clone -b App_Kube_cluster https://github.com/amgad96/EKS_Full_React_App.git cluster_dir
                    cd cluster_dir
                    kubectl apply -f DB-PersistentVol.yaml
                    kubectl apply -f MongoDB-DS.yaml
                    sed -i 's/\\${IMAGE_TAG_back}/v${version}/g' Backend-DS.yaml
                    kubectl apply -f Backend-DS.yaml
                    sed -i 's/\\${IMAGE_TAG_front}/v${version}/g' Frontend-DS.yaml
                    kubectl apply -f Frontend-DS.yaml
                    cd ..
                    rm -rf cluster_dir
                    """
}
