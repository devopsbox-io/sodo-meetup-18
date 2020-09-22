package io.devopsbox.infrastructure.test.common.terraform

import groovy.json.JsonOutput
import io.devopsbox.infrastructure.test.ConstructProps

class TerraformVariablesMarshaller {

    void marshall(File file, ConstructProps constructProps) {
        def json = JsonOutput.toJson(constructProps)
        file.write(json)
    }
}
