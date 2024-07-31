resource "aws_default_route_table" "kube_route" {
  default_route_table_id = var.kube_vpc.default_route_table_id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = var.kube_igw.id
  }

  tags = {
    Name = "kube_route_table"
  }
}
resource "aws_subnet" "kubeSub" {
  vpc_id     = var.kube_vpc.id
  cidr_block = var.sub_cidrblock
  map_public_ip_on_launch  = true
  availability_zone  = var.sub_avail_zone
  tags = {
    Name = "EKSSub"
  }
}