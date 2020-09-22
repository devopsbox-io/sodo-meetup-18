# This is an example cdk/tf project with spock tests

**Look at this repository branches to see all the steps.**

## Requirements
* AWS CDK version 1.62.0
* JDK 11
* Terraform (tested with 0.13.3)
* AWS credentials set as a default profile in your ~/.aws/credentials

## Running tests
You can run tests from your IDE or using:
```shell script
./gradlew check
```
Run:
```shell script
./gradlew check -Dcleanup.after.tests=true
```
if you want to clean AWS resources after tests. You should probably use it in CI/CD.

## Running the stack
You can run the stack using:
```shell script
cdk deploy
```

Use:
```shell script
cdk destroy
```
when you want to destroy AWS resources created by cdk stack.
