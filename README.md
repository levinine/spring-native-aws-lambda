### AWS Lambda using Spring Cloud function built by Spring native 
This mini project contains:
- A Spring boot project consisting of two Spring Cloud functions.
  - Function `generateFunnyName` generates a funny name based on the input and saves it in a DynamoDB table
  - Function `getFunnyNameById` reads the funny name from the same DynamoDB table
- Since this Lambda contains two functions, user is able to pick which one will be executed by providing the name of the function as a value of the `spring.cloud.function.definition` header.


### How to build the  project
**Make sure you have at least 6GB+ memory for Docker to use for the build process.**
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


