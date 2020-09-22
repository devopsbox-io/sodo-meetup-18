terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
    }
  }
}

locals {
  requested_bucket_name = "${var.companyName}-${var.envName}-${var.appName}-${var.bucketPurpose}"
  bucket_name = length(local.requested_bucket_name) > 63 ? substr(sha256(local.requested_bucket_name), 0, 63) : local.requested_bucket_name
}

resource "aws_s3_bucket" "bucket" {
  bucket = local.bucket_name
  server_side_encryption_configuration {
    rule {
      apply_server_side_encryption_by_default {
        sse_algorithm = "aws:kms"
      }
    }
  }
}
