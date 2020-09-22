package io.devopsbox.infrastructure.test.s3

import spock.lang.Specification
import spock.lang.Unroll

class S3ConstructTest extends Specification {

    @Unroll
    def "#testCase s3 bucket name should be no more than 63 characters"() {
        given:
        def props = new S3ConstructProps(
                "acme",
                "dev",
                "zbigniew",
                bucketPurpose,
        )

        when:
        def bucketName = S3Construct.bucketName(props)

        then:
        bucketName.length() <= 63

        where:
        testCase | bucketPurpose
        "short"  | "pictures"
        "long"   | "pictures12345678901234567890123456789012345678901234567890"
    }
}
