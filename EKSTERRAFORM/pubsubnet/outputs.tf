output pubsubnet_id {
  value = aws_subnet.kubeSub.id
}
output pubsubnet_cidrblock {
  value = aws_subnet.kubeSub.cidr_block
}
/*output "subnets" {
  value = aws_subnet.kubeSub[*].id
}*/