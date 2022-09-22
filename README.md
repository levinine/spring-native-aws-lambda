### AWS Lambda custom runtime example

This example demonstrates using Spring Cloud Function with AWS Custom Runtime. Custom Runtime provided by Spring Cloud Function.
**Make sure you have at least 6GB+ for Docker to use for the build process.**

### Build project (MAC users)
If you are running on Mac, you should build in docker image to ensure AWS compatible binary. To do that, navigate to the root of the project and execute `run-dev-container.sh` script:

```
$> ./run-dev-container.sh
```

That will download and start the docker container where you can now build the project:

```
$> ./build.sh

```

### Build project (non-MAC users)
Builds a native-zip ZIP file in target by executing provided `build.sh` script

```
$ ./build.sh
```



### Deploy and test

- Navigate to AWS Lambda dashboard and create a new function using the `Author from scratch` (name it anyway you want).
- In "Runtime" go to `Custm runtime` and select `Provide your own bootstrap on Amazon Linux 2`.
- Architecture should be `x84_64`
- Create the Lambda
- When it's created, scroll down to the section `Edit runtime settings` and click `Edit`
- Set the Handler to `generateFunnyName`
- Click `Save`
- Open the `Test` tab
- Upload generated `native-lambda-native.zip` from the target folder.
- Finally test by providing JSON-style string value via 'Test' tab. For example `{"name":"John"}`.