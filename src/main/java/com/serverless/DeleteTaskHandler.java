package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.Map;

import com.serverless.dal.Task;

public class DeleteTaskHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

    try {
        // get the 'pathParameters' from input
        Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
        String taskId = pathParameters.get("id");

        // get the Task by id
        Boolean success = new Task().delete(taskId);

        // send the response back
        if (success) {
          return ApiGatewayResponse.builder()
              .setStatusCode(204)
              .setHeaders(Collections.singletonMap("Access-Control-Allow-Origin", "*"))
      				.build();
        } else {
          return ApiGatewayResponse.builder()
      				.setStatusCode(404)
              .setObjectBody("Task with id: '" + taskId + "' not found.")
              .setHeaders(Collections.singletonMap("Access-Control-Allow-Origin", "*"))
      				.build();
        }
    } catch (Exception ex) {
        logger.error("Error in deleting task: " + ex);

        // send the error response back
  			Response responseBody = new Response("Error in deleting task: ", input);
  			return ApiGatewayResponse.builder()
  					.setStatusCode(500)
  					.setObjectBody(responseBody)
  					.build();
    }
	}
}
