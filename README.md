# Serverless REST API in Java/Maven using DynamoDB

![image](https://user-images.githubusercontent.com/8188/38645675-ec708d0e-3db2-11e8-8f8b-a4a37ed612b9.png)

The sample serverless service will create a REST API for tasks. It will be deployed to AWS. The data will be stored in a DynamoDB table. This is based on the blog post [REST API in Java using DynamoDB and Serverless](https://serverless.com/blog/how-to-create-a-rest-api-in-java-using-dynamodb-and-serverless/) and its [code](https://github.com/rupakg/aws-java-products-api).

A frontend for this can be found [here](https://github.com/colms/r7react).

## Build the Java project

Create the java artifact (jar) by:

```
$ cd r7
$ mvn clean install
```

We can see that we have an artifact in the `target` folder named `tasks-api-dev.jar`.

## Deploy the serverless app

```
$ sls deploy

Serverless: Packaging service...
Serverless: Creating Stack...
Serverless: Checking Stack create progress...
.....
Serverless: Stack create finished...
Serverless: Uploading CloudFormation file to S3...
Serverless: Uploading artifacts...
Serverless: Validating template...
Serverless: Updating Stack...
Serverless: Checking Stack update progress...
..................................
Serverless: Stack update finished...
Service Information
service: tasks-api
stage: dev
region: us-east-1
stack: tasks-api-dev
api keys:
  None
endpoints:
  GET - https://xxxxxxxxx.execute-api.us-east-1.amazonaws.com/dev/tasks
  GET - https://xxxxxxxxx.execute-api.us-east-1.amazonaws.com/dev/tasks/{id}
  POST - https://xxxxxxxxx.execute-api.us-east-1.amazonaws.com/dev/tasks
  DELETE - https://xxxxxxxxx.execute-api.us-east-1.amazonaws.com/dev/tasks/{id}
functions:
  listTasks: tasks-api-dev-listTasks
  getTask: tasks-api-dev-getTask
  createTask: tasks-api-dev-createTask
  deleteTask: tasks-api-dev-deleteTask
```

## Test the API

Here's how to use the API.

### Create Task

```
$ curl -X POST https://xxxxxxxxxx.execute-api.us-east-1.amazonaws.com/dev/tasks -d '{"description": "Task1"'}

{"id":"f42772e0-2fe2-4791-80c2-00fe436cf68f","description":"Task1","timestamp":1554493536939,"status":0}
```

### List Tasks

```
$ curl https://xxxxxxxxxx.execute-api.us-east-1.amazonaws.com/dev/tasks

[
  {"id":"437bc12a-d412-4ce1-aa7f-5781695f9c0f","description":"a","timestamp":1554491918223,"status":1},
  {"id":"06ec5d59-8233-4ba1-af36-b90876430633","description":"b","timestamp":1554491996423,"status":1},
  {"id":"b2d6258b-c124-437d-97e9-c53e2cc4bdd5","description":"d","timestamp":1554492307758,"status":1},
  {"id":"a655a5c6-73b2-4177-b7af-91935f7eec50","description":"f","timestamp":1554492515302,"status":1},
  {"id":"45dd77d3-0dcb-46c0-943f-49cce3c9b835","description":"c","timestamp":1554492260782,"status":0},
  {"id":"a4a44efe-4575-49f3-ade4-26b6c13ae6fc","description":"e","timestamp":1554492394062,"status":1}
]
```

**No Task(s) Found:**

```
$ curl https://xxxxxxxxxx.execute-api.us-east-1.amazonaws.com/dev/tasks

[]
```

### Get Task

```
$ curl https://xxxxxxxxxx.execute-api.us-east-1.amazonaws.com/dev/tasks/f42772e0-2fe2-4791-80c2-00fe436cf68f

{"id":"f42772e0-2fe2-4791-80c2-00fe436cf68f","description":"Task1","timestamp":1554493536939,"status":0}
```

**Task Not Found:**

```
curl https://xxxxxxxxxx.execute-api.us-east-1.amazonaws.com/dev/tasks/xxxx

"Task with id: 'xxxx' not found."
```

### DeleteTask

```
$ curl -X DELETE https://xxxxxxxxxx.execute-api.us-east-1.amazonaws.com/dev/tasks/24ada348-07e8-4414-8a8f-7903a6cb0253
```

**Task Not Found:**

```
curl -X DELETE https://xxxxxxxxxx.execute-api.us-east-1.amazonaws.com/dev/tasks/xxxx

"Task with id: 'xxxx' not found."
```

### UpdateTask

Update the status of this task:

```
curl -X PUT https://yh1vcbxakj.execute-api.us-east-1.amazonaws.com/dev/tasks -d '{"id":"f42772e0-2fe2-4791-80c2-00fe436cf68f", "description":"Task1", "status":1}'

{"id":"f42772e0-2fe2-4791-80c2-00fe436cf68f","description":"Task1","timestamp":1554494232127,"status":1}
```

## View the CloudWatch Logs

```
$ serverless logs --function getTask
```

## View the Metrics

View the metrics for the service:

```
$ serverless metrics

Service wide metrics
April 4, 2019 8:49 PM - April 5, 2019 8:49 PM

Invocations: 463 
Throttles: 0 
Errors: 3 
Duration (avg.): 1971.14ms
```

Or, view the metrics for only one function:

```
$ serverless metrics --function updateTask

updateTask
April 4, 2019 8:50 PM - April 5, 2019 8:50 PM

Invocations: 24 
Throttles: 0 
Errors: 2 
Duration (avg.): 249.71ms
```
