This project consists of a Node.js backend, a React frontend, and MongoDB. It uses Amazon EKS (Elastic Kubernetes Service) to create and manage the Kubernetes cluster. The project diagram file is named "EKS_Full_React_APP.jpg".

Project Setup Instructions:-
Follow these steps to set up the environment for this project.

Step 1: Run Installation Scripts
  Run the following scripts on your main server to install necessary tools:
    jenkins_installation.sh: Sets up the server as a Jenkins server.
    terraform_installation.sh: Prepares the environment to create an EKS cluster using Terraform (Infrastructure as Code).
    nodejs_installation.sh: Installs Node.js for testing and building the application artifact
    docker_installation.sh: Allows the Jenkins server to use Docker commands, such as building and pushing Docker images.

Step 2: Provide AWS Credentials
  Set the AWS credentials to enable Terraform to create the EKS cluster:
    export AWS_ACCESS_KEY_ID="anaccesskey"
    export AWS_SECRET_ACCESS_KEY="asecretkey"

Step 3: Create EKS Cluster with Terraform
  Navigate to the EKSTERRAFORM directory and apply the Terraform configuration:
    cd EKSTERRAFORM
    terraform apply -auto-approve

Step 4: Install Jenkins Plugins
  Install the following Jenkins plugins:
    Multibranch Scan Webhook Trigger
    Pipeline: GitHub Groovy Libraries

Step 5: Install AWS CLI and kubectl
  You will need the AWS CLI and kubectl to manage your EKS cluster. Follow these steps to install them on Ubuntu 24:
  Install AWS CLI
    -Update the package list and install prerequisites:
        sudo apt update
        sudo apt install -y curl unzip
    -Download the AWS CLI version 2:
        curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
    -Unzip the downloaded package:
        unzip awscliv2.zip
    -Run the AWS CLI installer:
        sudo ./aws/install
    -Verify the installation:
        aws --version
  Configure AWS CLI
    -Configure AWS CLI with your credentials:
        aws configure
      You will be prompted to enter your AWS Access Key ID, Secret Access Key, region, and output format.

    -Install and Configure kubectl for EKS
     Install kubectl:
        curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
        sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
    -Update kubeconfig for EKS:
        aws eks --region <region> update-kubeconfig --name <cluster_name>
      Replace <region> with your AWS region and <cluster_name> with the name of your EKS cluster.
    -Verify kubectl configuration:
        kubectl get nodes
      You should see a list of nodes in your EKS cluster.

Step 6: Jenkins Configuration:
  Create a Secret File Credential:
    In your Jenkins server, create a Secret file credential to allow Jenkins to access the EKS cluster and run application YAML files.
      The secret file should contain the following content:
        AWS_ACCESS_KEY_ID="anaccesskey"
        AWS_SECRET_ACCESS_KEY="asecretkey"

  Create a Username and Password Credential:
    In your Jenkins server, create a Username and Password credential to allow Jenkins to push images to Docker Hub.
      Note: The password will be a Personal Access Token generated in Docker Hub.

Step 7: Configure GitHub Webhook:
Add a webhook in your GitHub repository to connect to your Jenkins server.
You will need to add the IP address of your Jenkins server in the webhook configuration. As in "webhook.png".

Step 8: Configure Jenkins Shared library:
  You will need to add the repo url and branch name in the jenkins shared library configuration. As in "Shared_library_config.jpg".

    
