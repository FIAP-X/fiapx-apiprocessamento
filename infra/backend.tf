terraform {
  backend "s3" {
    bucket = "fiapx-statefile-bucket"
    key    = "apiprocessamento/terraform.tfstate"
    region = "us-east-1"
  }
}