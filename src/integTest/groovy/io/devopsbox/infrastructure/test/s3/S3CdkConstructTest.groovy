package io.devopsbox.infrastructure.test.s3

import io.devopsbox.infrastructure.test.common.cdk.CdkIntegrationTest
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ListBucketsRequest
import spock.lang.Shared

class S3CdkConstructTest extends CdkIntegrationTest {

    @Shared
    S3Client s3Client

    def setupSpec() {
        s3Client = S3Client.builder()
                .region(Region.of(awsRegion()))
                .build()
    }

    def "should create s3 bucket"() {
        given:
        def stackId = environmentName() + "S3BucketConstructTest"
        def constructProps = new S3ConstructProps(
                "acme",
                environmentName(),
                "cdkzbigniew",
                "pictures"
        )

        when:
        deployCdkConstruct(stackId, S3Construct, constructProps)

        then:
        def bucketName = "acme-" + environmentName() + "-cdkzbigniew-pictures"
        checkBucketExists(bucketName)

        cleanup:
        destroyCdkConstruct(stackId, S3Construct, constructProps)
    }

    void checkBucketExists(String name) {
        def listBucketsResponse = s3Client.listBuckets(ListBucketsRequest.builder()
                .build())

        assert listBucketsResponse.buckets().any { name.equals(it.name()) }
    }
}
