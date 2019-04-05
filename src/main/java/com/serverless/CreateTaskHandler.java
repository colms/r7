package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.Map;

import com.serverless.dal.Task;

public class CreateTaskHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

      try {
          // get the 'body' from input
          JsonNode body = new ObjectMapper().readTree((String) input.get("body"));

					// create the Task object for post
					Task task = new Task();
					task.setDescription(body.get("description").asText());
          task.save(task);

          // send the response back
      		return ApiGatewayResponse.builder()
      				.setStatusCode(200)
							.setObjectBody(task)
							.setHeaders(Collections.singletonMap("Access-Control-Allow-Origin", "*"))
      				.build();

      } catch (Exception ex) {
          logger.error("Error in saving task: " + ex);

          // send the error response back
    			Response responseBody = new Response("Error in saving task: ", input);
    			return ApiGatewayResponse.builder()
    					.setStatusCode(500)
							.setObjectBody(responseBody)
							.setHeaders(Collections.singletonMap("Access-Control-Allow-Origin", "*"))
    					.build();
      }
	}
}
