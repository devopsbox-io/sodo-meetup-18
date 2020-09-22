package io.devopsbox.infrastructure.test.s3;

import org.apache.commons.codec.digest.DigestUtils;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.RemovalPolicy;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketEncryption;
import software.amazon.awscdk.services.s3.BucketProps;

public class S3Construct extends Construct {

    public static final int BUCKET_NAME_MAX_LENGTH = 63;

    public S3Construct(Construct scope, String id, S3ConstructProps props) {
        super(scope, id);

        String bucketName = bucketName(props);
        new Bucket(this, bucketName, BucketProps.builder()
                .removalPolicy(RemovalPolicy.DESTROY)
                .encryption(BucketEncryption.KMS_MANAGED)
                .bucketName(bucketName)
                .build());
    }

    public static String bucketName(S3ConstructProps props) {
        String bucketName = props.getCompanyName() + "-" + props.getEnvName() + "-" + props.getAppName() + "-" + props.getBucketPurpose();
        if(bucketName.length() > BUCKET_NAME_MAX_LENGTH) {
            bucketName = DigestUtils.sha256Hex(bucketName).substring(0, BUCKET_NAME_MAX_LENGTH);
        }
        return bucketName;
    }
}
