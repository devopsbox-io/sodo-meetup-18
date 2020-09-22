package io.devopsbox.infrastructure.test.s3;

import io.devopsbox.infrastructure.test.ConstructProps;

public class S3ConstructProps extends ConstructProps {
    private final String bucketName;

    public S3ConstructProps(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
