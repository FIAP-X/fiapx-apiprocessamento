terraform {
  backend "s3" {
    bucket = "fiapx-bucket-statefile"
    key    = "apiprocessamento/terraform.tfstate"
    region = "us-east-1"
  }
}