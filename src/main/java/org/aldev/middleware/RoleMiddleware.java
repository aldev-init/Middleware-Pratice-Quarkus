package org.aldev.middleware;

import io.vertx.core.json.JsonObject;
import jakarta.annotation.Priority;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Provider
@Priority(1)
public class RoleMiddleware implements ContainerRequestFilter {

    private Logger log = LoggerFactory.getLogger(RoleMiddleware.class);

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        log.info("Enter Role Detection Middleware...");
        int countPremission = 0;
        List<String> myListRoles = new ArrayList<>(); // imagine my role
        myListRoles.add("CBE");
        // roles logic
        if(resourceInfo.getResourceMethod().isAnnotationPresent(RolesAllowed.class)){
            RolesAllowed listRoles = resourceInfo.getResourceMethod().getAnnotation(RolesAllowed.class);
            List<String> valueRoles = Arrays.asList(listRoles.value());

            for(String value : valueRoles){
                if (myListRoles.contains(value)){
                    countPremission++;
                }
            }
            if (countPremission == 0){
                JsonObject response = new JsonObject();
                response.put("message","ROLE_NOT_ALLOWED");
                Response.ResponseBuilder responseBuilder = Response.status(Response.Status.UNAUTHORIZED).entity(response);
                responseBuilder.header("Content-Type","application/json");
                requestContext.abortWith(responseBuilder.build());
            }
        }
        // logic if success
    }
}
