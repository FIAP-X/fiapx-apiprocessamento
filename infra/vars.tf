variable "aws_region" {
  description = "Região da AWS"
  type        = string
  default     = "us-east-1"
}

variable "lab_role" {
  description = "ARN da role IAM"
  type        = string
}

variable "ecr_image" {
  description = "Imagem no ECR"
  type        = string
}

variable "subnet_ids" {
  description = "Lista de IDs das subnets"
  type        = list(string)
}

variable "vpc_id" {
  description = "VPC ID"
  type        = string
}

variable "ecs_cluster_arn" {
  description = "ID do cluster ECS"
  type        = string
}

variable "aws_access_key_id" {
  description = "Chave de acesso da AWS"
  type        = string
  sensitive   = true
}

variable "aws_secret_access_key" {
  description = "Chave AWS"
  type        = string
  sensitive   = true
}