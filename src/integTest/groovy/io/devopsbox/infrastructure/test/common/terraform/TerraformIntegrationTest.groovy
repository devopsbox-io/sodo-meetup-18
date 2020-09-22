package io.devopsbox.infrastructure.test.common.terraform

import io.devopsbox.infrastructure.test.ConstructProps
import spock.lang.Shared
import spock.lang.Specification

abstract class TerraformIntegrationTest extends Specification {

    @Shared
    File tmpDir
    @Shared
    Terraform terraform
    @Shared
    TerraformVariablesMarshaller terraformVariablesMarshaller

    def setupSpec() {
        tmpDir = File.createTempDir()
        terraform = new Terraform(awsRegion())
        terraformVariablesMarshaller = new TerraformVariablesMarshaller()
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

    protected void deployTerraformModule(String moduleDirPath, ConstructProps terraformVariables) {
        runCdkConstruct(moduleDirPath, terraformVariables, "apply")
    }

    protected void destroyTerraformModule(String moduleDirPath, ConstructProps terraformVariables) {
        if ("true".equals(System.getProperty("cleanup.after.tests"))) {
            runCdkConstruct(moduleDirPath, terraformVariables, "destroy")
        }
    }

    private void runCdkConstruct(String moduleDirPath, ConstructProps terraformVariables, String terraformCommand) {
        def moduleVariablesFile = File.createTempFile("variables-", ".tfvars.json", tmpDir)
        terraformVariablesMarshaller.marshall(moduleVariablesFile, terraformVariables)

        terraform.run(tmpDir.absolutePath,
                moduleDirPath,
                moduleVariablesFile.absolutePath,
                terraformCommand)
    }
}
