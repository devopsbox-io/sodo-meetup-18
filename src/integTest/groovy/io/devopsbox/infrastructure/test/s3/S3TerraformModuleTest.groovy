package io.devopsbox.infrastructure.test.s3

import io.devopsbox.infrastructure.test.common.terraform.TerraformIntegrationTest
import org.apache.commons.codec.digest.DigestUtils
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetBucketEncryptionRequest
import software.amazon.awssdk.services.s3.model.ListBucketsRequest
import software.amazon.awssdk.services.s3.model.ServerSideEncryption
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
        def constructProps = new S3ConstructProps(
                "acme",
                environmentName(),
                "tfzbigniew",
                "pictures"
        )

        when:
        deployTerraformModule("terraform/s3", constructProps)

        then:
        def bucketName = "acme-" + environmentName() + "-tfzbigniew-pictures"
        checkBucketExists(bucketName)
        isBucketEncrypted(bucketName)

        cleanup:
        destroyTerraformModule("terraform/s3", constructProps)
    }

    def "should create s3 bucket with long name"() {
        given:
        def constructProps = new S3ConstructProps(
                "acme",
                environmentName(),
                "tfzbigniew",
                "pictures12345678901234567890123456789012345678901234567890"
        )

        when:
        deployTerraformModule("terraform/s3", constructProps)

        then:
        def bucketName = DigestUtils.sha256Hex(
                "acme-" + environmentName() + "-tfzbigniew-pictures12345678901234567890123456789012345678901234567890"
        ).substring(0, 63)
        checkBucketExists(bucketName)

        cleanup:
        destroyTerraformModule("terraform/s3", constructProps)
    }

    void checkBucketExists(String name) {
        def listBucketsResponse = s3Client.listBuckets(ListBucketsRequest.builder()
                .build())

        assert listBucketsResponse.buckets().any { name.equals(it.name()) }
    }

    void isBucketEncrypted(String bucketName) {
        def getBucketEncryptionResponse = s3Client.getBucketEncryption(GetBucketEncryptionRequest.builder()
                .bucket(bucketName)
                .build())

        assert getBucketEncryptionResponse.serverSideEncryptionConfiguration().rules().any {
            it.applyServerSideEncryptionByDefault().sseAlgorithm() == ServerSideEncryption.AWS_KMS
        }
    }
}
