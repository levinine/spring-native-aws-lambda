### What is Spring Native?

Spring Native provides **beta** support for compiling Spring Boot
applications to native executables with GraalVM, providing a
new way to deploy Spring Boot applications that then run
extremely efficiently.

JVM shines in terms of short build time, maturity and performance.
Native is optimized for low memory footprint and low startup time.

### AWS Lambda using Spring Cloud function built by Spring native 
This mini project contains:
- A Spring boot project consisting of two Spring Cloud functions.
  - Function `generateFunnyName` generates a funny name based on the input and saves it in a DynamoDB table
  - Function `getFunnyNameById` reads the funny name from the same DynamoDB table
- Since this Lambda contains two functions, user is able to pick which one will be executed by providing the name of the function as a value of the `spring.cloud.function.definition` header.


### How to build the  project
**Make sure you have at least 6 GB+ memory for Docker to use for the build process.**
**Make sure you have GraalVM 22.2.0 or latter installed.**

To build the project, execute:

```
$> mvn -Pnative clean package
```

This will create the native-lambda executable inside the /target folder which can be executed just by running it by 
```
$> cd target
$> ./native-lambda
```

File named **native-lambda-native.zip** represents the zipped directory to be deployed on the Lambda.



### How to deploy to Lambda

Preconditions:
- DynamoDB table named: `names` is created and the Lambda has R/W access to it
- Navigate to AWS Lambda dashboard and create a new function using the `Author from scratch`. Name it anyway you want.
- In "Runtime" go to `Custm runtime` and select `Provide your own bootstrap on Amazon Linux 2`.
- Architecture should be `x84_64`
- Create the Lambda
- When it's created, scroll down to the section `Edit runtime settings` and click `Edit`
- Set the Handler to `org.springframework.cloud.function.adapter.aws.FunctionInvoker`
- Click `Save`
- Open the `Configuration` tab
- Open the `Environment variables` tab
  - Add variable `DEFAULT_HANDLER` with value `functionRouter`
  - Add variable `LOGGING_LEVEL_org_springframework_cloud` with value `DEBUG`
- Open the `Test` tab
- Upload generated `native-lambda-native.zip` from the target folder or upload it from S3.
  - NB: Lambda runs on x84_64 Linux. In case you want to upload a locally built native image from your local machine it needs to conform to the Lambda's OS.
  - If you are running on Windows, the native image will have to be built inside a Docker container using image docker.io/springci/spring-native:java17-0.11.x
  - If you are running on Intel Mac, the native image will have to be built inside a Docker image docker.io/springci/spring-native:java17-0.11.x
  - If you are running on ARM Mac, the native image will have to be built through AWS CodeBuild using image docker.io/springci/spring-native:java17-0.11.x

### How to Test

- Finally test the Lambda by providing JSON-style string value via 'Test' tab. Choose the `apigateway-aws-proxy` template.
- To create an entity in DynamoDB, append the following to the Event JSON and click Test:
  - "name": "John"
  - under the "headers", append: "spring.cloud.function.definition" : "generateFunnyName",
- To reade the entity from DynamoDB, append the following to the Event JSON and click Test:
  - "id": "YOUR_ID_HERE"
  - under the "headers", change "spring.cloud.function.definition" : "getFunnyNameById"


### Observations & comments
- Cold start execution time of a Lambda with 256 MBw RAM with this project deployed is < 1 sec total (Billed duration). This on is NodeJS level.
- Startup time on M1 with 32 GB ram is around 50 ms
- Memory footprint is < 150 MB
- Size of the zipped native image of this project is 23.5 MB. Image size can be reduced by 30% by performing UPX compression.
- Building the native image with GraalVM takes quite some time and requires loads of resources. On a M1 with 32 GB RAM, the build takes longer than 1 minute.
- GraalVM needs to be aware reflection, resources and proxies which is configured at build time through reflect-config.json. Spring Native is generating this automatically for the libraries it supports. For the unsupported ones, this will have to be configured manually by you.
  - This configuration can also be provided by Native hint Annotations as well (@NativeHint, @SeriallizationHint, etc)
- Images can be built through
  - Buildpacks (Paketo). It runs a Docker container with all dependencies inside which the native image is built. x86 is supported, ARM is in progress, see https://github.com/buildpacks/lifecycle/issues/435 for updates.
    - UPX compression can be used which reduces the size of the image by the factor of 4. This can improve the startup time due to lower resources used.
  - By using Native build tools installed locally (as this example shows). This produces a native executable compatible with the source OS. Cross-platform compilation is not (yet) possible.
- It supports Java and Kotlin (>1.5)
- Native testing is supported - Junit5 tests can be compiled as a native executable and executed on native. Mockito is not supported yet.
- Runtime created proxies (for example@Transactional or @PreAuthorize) are recognized by Spring Native out of the box which modifies the bytecode on build time in order to create the proxies which can be used on the runtime
  - If Spring does not recognize such a proxy, @AotProxyHint can be used


### Cons:
- Spring cloud function documentation and examples are not always up to date. It seems the Cloud function is not fully supported by Spring native
- Build takes quite long
- There is no cross-platform compiling possibility
- Inability to integrate this example with the API Gateway. The payload of the request is stored in the 'body' section of the request sent to the lambda from Gateway and is somehow inaccessible to the Functions. It seems there is a bug in Spring cloud Functions when running on native. Usage of APIGatewayV2HTTPEvent and APIGatewayProxyRequestEvent did not do the trick.
- Inability to properly debug a native app