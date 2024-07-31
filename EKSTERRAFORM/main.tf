terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}
provider "aws" {
  region = "us-east-1"
}
resource "aws_vpc" "KubeVPC" {
  cidr_block = "172.15.0.0/16"
   tags = {
    Name = "EKSVPC"
   }
}
resource "aws_internet_gateway" "kube_cluster_gw" {
  vpc_id = aws_vpc.KubeVPC.id
  tags = {
    Name = "EKSigw"
  }
}
module pubsubnet {
  source = "./pubsubnet"
  kube_vpc  = aws_vpc.KubeVPC
  kube_igw = aws_internet_gateway.kube_cluster_gw
  sub_cidrblock = "172.15.0.0/24"
  sub_avail_zone = "us-east-1a"
}
module pubsubnet2 {
  source = "./pubsubnet"
  kube_vpc  = aws_vpc.KubeVPC
  kube_igw = aws_internet_gateway.kube_cluster_gw
  sub_cidrblock = "172.15.1.0/24"
  sub_avail_zone = "us-east-1b"
}

/*module "network" {
    source = "./network"
    
    ws_name = var.ws_name
    region = var.region
    vpc_name = var.vpc_name
    vpc_cidr_block = var.vpc_cidr_block
    pub_sub-1_cidr_block = var.pub_sub-1_cidr_block
    pub_sub-2_cidr_block = var.pub_sub-2_cidr_block
    priv_sub-1_cidr_block = var.priv_sub-1_cidr_block
    priv_sub-2_cidr_block = var.priv_sub-2_cidr_block
}*/

module "eks" {
    source = "./eks"

    ws_name = var.ws_name
    eks_sub_1 = module.pubsubnet.pubsubnet_id
    eks_sub_2 = module.pubsubnet2.pubsubnet_id

    nodes_desired_size = var.nodes_desired_size
    nodes_max_size = var.nodes_max_size
    nodes_min_size = var.nodes_min_size

    sg_cidr = aws_vpc.KubeVPC.cidr_block
    vpc_id = aws_vpc.KubeVPC.id

}