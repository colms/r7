package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.Map;
import java.util.List;

import com.serverless.dal.Task;

public class ListTasksHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
    try {
        // get all tasks
		List<Task> tasks = new Task().list();
		
        // send the response back
        return ApiGatewayResponse.builder()
    				.setStatusCode(200)
					.setObjectBody(tasks)
					.setHeaders(Collections.singletonMap("Access-Control-Allow-Origin", "*"))
    				.build();
    } catch (Exception ex) {
        logger.error("Error in listing tasks: " + ex);

        // send the error response back
  			Response responseBody = new Response("Error in listing tasks: ", input);
  			return ApiGatewayResponse.builder()
  					.setStatusCode(500)
					.setObjectBody(responseBody)
					.setHeaders(Collections.singletonMap("Access-Control-Allow-Origin", "*"))
  					.build();
    }
	}
}
