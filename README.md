# Rabobank Customer Statement Processor

This application is developed to validate customer statement records.

# Technical Details

## Server
The application is hosted on port `8080` and the servlet context path is `/statement-processor`

## Security
Application is secured with basic authentication with credentials `user:pass`.
Security is enabled by default.we can also disable security with application configuration.

# Usage

## Input
The format of the file is a simplified format of the MT940 format. The format is as follows:

| Field | Description |
| ---- | ---- |
| Transaction reference | A numeric value |
| Account number | An IBAN  |
| Start Balance | The starting balance in Euros  |
| Mutation | Either an addition (+) or a deduction (-) |
| Description | Free text  |
| End Balance | The end balance in Euros |

## Output
There are two validations:
* all transaction references should be unique
* the end balance needs to be validated

At the end of the processing, a report needs to be created which will display both the transaction reference and description of each of the failed records.

## How to use
1.	Clone the project Rabobank (Spring REST project).
https://github.com/AjayAnand86/customer-statement-processor.git

2.Run maven clean install command to install dependency.

3.the server should be started as a Spring Boot project. The server can be started up via the following command:

`mvn spring-boot:run`

Secondly, the jar file of the project could be used in a Containerized environment.

`java -jar target/customer-statement-processor-0.0.1-SNAPSHOT.jar`

## The service
The service is a REST API to validate CSV and XML files and can be used with `POST` request.Service also has simple UI to  upload file from browser.
The XML or CSV file which contains transaction statements record can be posted to the service and gets validated. Files in other format will return 400 error code.

## Using the service

### Via the UI
To access the web page to use the service, navigate to the root (`/statement-processor`). For example: `http://localhost:8080/statement-processor/`
Via the web page, file uploader could be used to validate files. If the security is enabled, authentication should be required on the browser.
Then, `username: user` and `password: pass` should be entered.

## Via Postman
Either PostMan or an equivalent applications can be used to use the service.

## Via cURL
The endpoint can be used via `cURL` using the following command:

```bash
curl -X POST "http://localhost:8080/statement-processor/validateFile" -H "accept: */*" -H "Content-Type: multipart/form-data" -F "file=@records_valid.xml;type=text/xml"

```
## Via SwaggerUI

http://localhost:8080/statement-processor/swagger-ui.html#

## Screen shots

### Swagger-Ui: 
![swagger_ui](https://user-images.githubusercontent.com/35381348/63217363-8f254780-c145-11e9-8cd7-822409e22f61.PNG)

### XML File processing
![swagger_xml_processing](https://user-images.githubusercontent.com/35381348/63217583-10caa480-c149-11e9-9eef-9e095f4652a3.PNG)

### CSV File processing
![swagger_csv_processing](https://user-images.githubusercontent.com/35381348/63217591-250ea180-c149-11e9-868f-89f54ee75304.PNG)

## Testing
The tests can be performed via following Maven command:

`mvn clean test`

## Documentation
The JavaDoc is configured can be generated via the following command:

`mvn javadoc:javadoc`

The generated javadoc can be found in the folder /target/site/apidocs. Below is the screenshot for the same.

### Screenshot
![java_doc](https://user-images.githubusercontent.com/35381348/63217593-2fc93680-c149-11e9-92a1-0e49873372b8.PNG)

## Improvements

1. API monitoring can be added to monitor API service status
2. Error handling can be improved. 
3. Static code analysis
4. Test case coverage to increase

## Contributor
Ajay Anand
