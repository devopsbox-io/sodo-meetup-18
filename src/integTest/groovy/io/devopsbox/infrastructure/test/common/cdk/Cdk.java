package io.devopsbox.infrastructure.test.common.cdk;

import io.devopsbox.infrastructure.test.common.ProcessRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cdk {
    private static final Logger log = LoggerFactory.getLogger(Cdk.class);
    private final ProcessRunner processRunner;

    private final String awsRegion;

    public Cdk(String awsRegion) {
        this.awsRegion = awsRegion;
        this.processRunner = new ProcessRunner();
    }

    public void run(String tmpDir, Map<String, String> environmentVariables, String cdkCommand) {
        Map<String, String> cdkEnvironmentVariables = new HashMap<>(environmentVariables);
        cdkEnvironmentVariables.put("AWS_REGION", awsRegion);

        List<String> commandList = new ArrayList<>();

//        dockerWrapper(tmpDir, environmentVariables, commandList);

        commandList.add("cdk");
        commandList.add(cdkCommand);
        commandList.add("--verbose");
        commandList.add("--trace");
        commandList.add("--force");

        String cdkApp = "./gradlew startCdkTest";
        if (isDebugEnabled()) {
            cdkApp = cdkApp + " --debug-jvm";
        }
        commandList.add("--app");
        commandList.add(cdkApp);
        commandList.add("--require-approval");
        commandList.add("\"never\"");

        log.debug("Starting cdk process {}", commandList);

        int returnCode = processRunner.run(
                cdkEnvironmentVariables,
                commandList.toArray(new String[0])
        );
        if (returnCode != 0) {
            throw new RuntimeException("Cdk process failed!");
        }

    }

    private void dockerWrapper(String tmpDir, Map<String, String> environmentVariables, List<String> commandList) {
        commandList.add("docker");
        commandList.add("run");
        commandList.add("--rm");
        commandList.add("--network");
        commandList.add("host");
        commandList.add("-v");
        commandList.add(System.getProperty("user.dir") + ":/app");
        commandList.add("-v");
        commandList.add("/etc/passwd:/etc/passwd:ro");
        commandList.add("-v");
        commandList.add(tmpDir + ":" + tmpDir + ":ro");

        environmentVariables.forEach((k, v) -> {
            commandList.add("-e");
            commandList.add(k + "=" + v);
        });

        String homeDir = System.getProperty("user.home");
        String gradleConfigDir = homeDir + "/.gradle";
        commandList.add("-v");
        commandList.add(gradleConfigDir + ":" + gradleConfigDir);

        String awsConfigDir = homeDir + "/.aws";
        commandList.add("-v");
        commandList.add(awsConfigDir + ":" + awsConfigDir + ":ro");
        commandList.add("-u");
        commandList.add(getUid() + ":" + getGid());
        commandList.add("iac-test");
    }

    private boolean isDebugEnabled() {
        String cdkDebugEnabled = System.getenv("CDK_DEBUG_ENABLED");
        return "true".equals(cdkDebugEnabled);
    }

    private String getUid() {
        return processRunner.runWithOutput("id", "-u");
    }

    private String getGid() {
        return processRunner.runWithOutput("id", "-g");
    }
}
