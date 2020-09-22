package io.devopsbox.infrastructure.test.common.cdk

import io.devopsbox.infrastructure.test.ConstructProps
import software.amazon.awscdk.core.Construct
import spock.lang.Shared
import spock.lang.Specification

abstract class CdkIntegrationTest extends Specification {

    @Shared
    File tmpDir
    @Shared
    Cdk cdk
    @Shared
    PropsFileSerializer propsFileSerializer

    def setupSpec() {
        tmpDir = File.createTempDir()
        cdk = new Cdk(awsRegion())
        propsFileSerializer = new PropsFileSerializer()
    }

    void cleanupSpec() {
        tmpDir.deleteDir()
    }

    def environmentName() {
        return System.getProperty("user.name")
    }

    def awsRegion() {
        return "eu-west-1"
    }

    protected <T extends Construct> void deployCdkConstruct(String cdkStackId, Class<T> constructClass, ConstructProps constructProps) {
        runCdkConstruct(cdkStackId, constructClass, constructProps, "deploy")
    }

    protected <T extends Construct> void destroyCdkConstruct(String cdkStackId, Class<T> constructClass, ConstructProps constructProps) {
        if ("true".equals(System.getProperty("cleanup.after.tests"))) {
            runCdkConstruct(cdkStackId, constructClass, constructProps, "destroy")
        }
    }

    private <T extends Construct> void runCdkConstruct(String cdkStackId, Class<T> constructClass, ConstructProps constructProps, String cdkCommand) {
        def constructPropsFile = File.createTempFile("props-", ".ser", tmpDir)
        propsFileSerializer.serialize(constructPropsFile, constructProps)

        cdk.run(tmpDir.absolutePath, [
                (CdkIntegrationTestMain.ENV_VAR_CDK_STACK_ID)            : cdkStackId,
                (CdkIntegrationTestMain.ENV_VAR_CDK_CONSTRUCT_CLASS_NAME): constructClass.name,
                (CdkIntegrationTestMain.ENV_VAR_CDK_CONSTRUCT_PROPS_FILE): constructPropsFile.absolutePath,
        ], cdkCommand)
    }
}
