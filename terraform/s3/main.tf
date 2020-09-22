terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
    }
  }
}

resource "aws_s3_bucket" "bucket" {
  bucket = "${var.companyName}-${var.envName}-${var.appName}-${var.bucketPurpose}"
}
