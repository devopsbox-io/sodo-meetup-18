package io.devopsbox.infrastructure.test.s3

import io.devopsbox.infrastructure.test.common.terraform.TerraformIntegrationTest
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ListBucketsRequest
import spock.lang.Shared

class S3TerraformModuleTest extends TerraformIntegrationTest {

    @Shared
    S3Client s3Client

    def setupSpec() {
        s3Client = S3Client.builder()
                .region(Region.of(awsRegion()))
                .build()
    }

    def "should create s3 bucket"() {
        given:
        def bucketName = "sodo-meetup-18-tftest91"
        def constructProps = new S3ConstructProps(
                bucketName,
        )

        when:
        deployTerraformModule("terraform/s3", constructProps)

        then:
        checkBucketExists(bucketName)

        cleanup:
        destroyTerraformModule("terraform/s3", constructProps)
    }

    void checkBucketExists(String name) {
        def listBucketsResponse = s3Client.listBuckets(ListBucketsRequest.builder()
                .build())

        assert listBucketsResponse.buckets().any { name.equals(it.name()) }
    }
}
