package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.Map;

import com.serverless.dal.Task;

public class GetTaskHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

    try {
        // get the 'pathParameters' from input
        Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
        String taskId = pathParameters.get("id");

        // get the Task by id
        Task task = new Task().get(taskId);

        // send the response back
        if (task != null) {
          return ApiGatewayResponse.builder()
      				.setStatusCode(200)
              .setObjectBody(task)
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
        logger.error("Error in retrieving task: " + ex);

        // send the error response back
  			Response responseBody = new Response("Error in retrieving task: ", input);
  			return ApiGatewayResponse.builder()
  					.setStatusCode(500)
  					.setObjectBody(responseBody)
  					.build();
    }
	}
}
