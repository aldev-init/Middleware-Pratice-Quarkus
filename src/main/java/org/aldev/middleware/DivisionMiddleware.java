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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wildfly.common.array.Arrays2;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Provider
@Priority(2)
public class DivisionMiddleware implements ContainerRequestFilter {

    private Logger log = LoggerFactory.getLogger(DivisionMiddleware.class);

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        log.info("Enter Division Detection Middleware...");
        List<String> myDivison = new ArrayList<>(); // imagine my division
        int countPremission = 0;
        myDivison.add("CB1");
        myDivison.add("CB2");
        if(resourceInfo.getResourceMethod().isAnnotationPresent(DivisionAllowed.class)){
            DivisionAllowed divisionAllowed = resourceInfo.getResourceMethod().getAnnotation(DivisionAllowed.class);
            String[] divisionValue = divisionAllowed.value();

            for (String division : divisionValue){
                if (myDivison.contains(division)){
                    countPremission++;
                }
            }

            if (countPremission == 0){
                JsonObject response = new JsonObject();
                response.put("message","DIVISION_NOT_ALLOWED");
                Response.ResponseBuilder responseBuilder = Response.status(Response.Status.UNAUTHORIZED).entity(response);
                responseBuilder.header("Content-Type","application/json");
                requestContext.abortWith(responseBuilder.build());
            }
        }
    }
}
