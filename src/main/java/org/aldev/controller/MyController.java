package org.aldev.controller;

import io.vertx.core.json.JsonObject;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.aldev.annotations.DivisionAllowed;
import org.aldev.annotations.OperationAllowed;
import org.aldev.service.MyService;

import java.lang.annotation.Target;

@Path("/api")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class MyController {

    @Inject
    MyService myService;

    @GET
    @Path("/hello")
    public Response hello(){
        return Response.ok(new JsonObject("{\"message\":\"Hello From Service!!\"}")).build();
    }

    @GET
    @Path("/test")
    @RolesAllowed({"CBH","CBM"})
    @DivisionAllowed({"CB1","CB2"})
    public Response returnMyString(){
        return Response.ok(myService.myString()).build();
    }

    @GET
    @Path("/test2")
    @RolesAllowed("CBE")
    @DivisionAllowed({"CB3","CB1"})
    public Response returnMyStringCBE(){
        return Response.ok("Hallo CBE!!").build();
    }

    @GET
    @Path("/crudet")
    @RolesAllowed("CBE")
    @DivisionAllowed({"CB3","CB1"})
    @OperationAllowed({"Create","Update"})
    public Response returnMyStringOp(){
        return Response.ok("You Can Create And Update").build();
    }

    @GET
    @Path("/gelet")
    @RolesAllowed("CBE")
    @DivisionAllowed({"CB3","CB1"})
    @OperationAllowed({"Read","Delete"})
    public Response returnMyStringOp2(){
        return Response.ok("You Can Read And Delete").build();
    }



}
