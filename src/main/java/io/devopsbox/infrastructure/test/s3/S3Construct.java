package io.devopsbox.infrastructure.test.s3;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.RemovalPolicy;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketEncryption;
import software.amazon.awscdk.services.s3.BucketProps;

public class S3Construct extends Construct {
    public S3Construct(Construct scope, String id, S3ConstructProps props) {
        super(scope, id);

        String bucketName = props.getCompanyName() + "-" + props.getEnvName() + "-" + props.getAppName() + "-" + props.getBucketPurpose();
        new Bucket(this, bucketName, BucketProps.builder()
                .removalPolicy(RemovalPolicy.DESTROY)
                .encryption(BucketEncryption.KMS_MANAGED)
                .bucketName(bucketName)
                .build());
    }
}
