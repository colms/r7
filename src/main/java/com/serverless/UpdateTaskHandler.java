package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import com.serverless.dal.Task;

public class UpdateTaskHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Map<String, String> headers  = new HashMap<String, String>() {{
            put("Access-Control-Allow-Origin", "*");
            put("Access-Control-Allow-Methods", "PUT");
            put("Access-Control-Allow-Headers", "Content-Type, Authorization, Content-Length, X-Requested-With, Accept");
        }};
        try {
            // get the 'body' from input
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
  
            // create the Task object for post
            Task task = new Task();
            Double doneStatus = body.get("status").asDouble();
            task.setId(body.get("id").asText());
            task.setDescription(body.get("description").asText());
            task.setStatus(doneStatus.intValue());
            task.save(task);
  
            // send the response back
            
            return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setObjectBody(task)
                .setHeaders(headers)
                .build();
  
        } catch (Exception ex) {
            logger.error("Error in saving task: " + ex);
  
            // send the error response back
            Response responseBody = new Response("Error in saving task: ", input);
            return ApiGatewayResponse.builder()
                .setStatusCode(500)
                .setObjectBody(responseBody)
                .setHeaders(headers)
                .build();
        }
    }
}
