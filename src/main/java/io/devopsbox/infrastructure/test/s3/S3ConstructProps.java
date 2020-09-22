package io.devopsbox.infrastructure.test.s3;

import io.devopsbox.infrastructure.test.ConstructProps;

public class S3ConstructProps extends ConstructProps {
    private final String bucketPurpose;

    public S3ConstructProps(String companyName, String envName, String appName, String bucketPurpose) {
        super(companyName, envName, appName);
        this.bucketPurpose = bucketPurpose;
    }

    public String getBucketPurpose() {
        return bucketPurpose;
    }
}
