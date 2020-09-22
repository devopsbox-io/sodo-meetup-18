package io.devopsbox.infrastructure.test.s3;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.RemovalPolicy;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketProps;

public class S3Construct extends Construct {
    public S3Construct(Construct scope, String id, S3ConstructProps props) {
        super(scope, id);

        new Bucket(this, props.getBucketName(), BucketProps.builder()
                .removalPolicy(RemovalPolicy.DESTROY)
                .bucketName(props.getBucketName())
                .build());
    }
}
