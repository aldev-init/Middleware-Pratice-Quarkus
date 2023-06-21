package org.aldev.middleware;

import io.vertx.core.json.JsonObject;
import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.aldev.annotations.DivisionAllowed;
import org.aldev.annotations.OperationAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Provider
@Priority(3)
public class OperationMiddleware implements ContainerRequestFilter {

    private Logger log = LoggerFactory.getLogger(OperationMiddleware.class);

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        log.info("Enter Operation Detection Middleware...");
        List<String> myOperation = new ArrayList<>(); // imagine my division
        int countPremission = 0;
        myOperation.add("Read");
        myOperation.add("Delete");
        if(resourceInfo.getResourceMethod().isAnnotationPresent(OperationAllowed.class)){
            OperationAllowed divisionAllowed = resourceInfo.getResourceMethod().getAnnotation(OperationAllowed.class);
            String[] divisionValue = divisionAllowed.value();

            for (String division : divisionValue){
                if (myOperation.contains(division)){
                    countPremission++;
                }
            }

            if (countPremission == 0){
                JsonObject response = new JsonObject();
                response.put("message","OPERATION_NOT_ALLOWED");
                Response.ResponseBuilder responseBuilder = Response.status(Response.Status.UNAUTHORIZED).entity(response);
                responseBuilder.header("Content-Type","application/json");
                requestContext.abortWith(responseBuilder.build());
            }
        }
    }
}
