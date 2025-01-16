terraform {
  backend "s3" {
    bucket = "fiapx-bucket-statefile"
    key    = "infra/terraform.tfstate"
    region = "us-east-1"
  }
}