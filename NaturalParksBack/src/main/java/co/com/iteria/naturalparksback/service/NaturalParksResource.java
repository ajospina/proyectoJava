/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.iteria.naturalparksback.service;

import co.com.iteria.naturalparksback.dao.ApiParqueDao;
import co.com.iteria.naturalparksback.model.ApiParque;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Usuario
 */
@Path("/parks")
public class NaturalParksResource {

    @Context
    private UriInfo context;
    private ApiParqueDao parkdao;
    private String urlpath = "parks";

    /**
     * Creates a new instance of NaturalParksResource
     */
    public NaturalParksResource() {
        parkdao = new ApiParqueDao();
    }

    /**
     * Retrieves representation of an instance of co.com.iteria.naturalparksback.service.NaturalParksResource
     * @return an instance of java.lang.String
     */
    @GET
    @Path("{parkId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getParkById(@PathParam("parkId") int id) {
        ApiParque p  = parkdao.getPark(id);
        JsonObject json = null;
        Response.Status st = null;
        if (p != null){
            json = 
                Json.createObjectBuilder().add("id", p.getId())
                                          .add("name", p.getName())  
                                          .add("state", p.getState()) 
                                          .add("capacity", p.getCapacity()) 
                                          .add("status", p.getStatus()) 
                        .build();
            
            st = Response.Status.OK;
        }else{
            st = Response.Status.BAD_REQUEST;
        }
        System.out.println("lk");
        return Response.status(st).entity(json).build();  
    }
   
    /*Listado de todos los parques*/
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String  getParkByStatus(@QueryParam("status") String status ) {
        
        List<ApiParque> ls = ls = parkdao.getParkByStatus(status);
       
        String rs = "[";
        
        for (int i=0; i<ls.size(); i++ ){
             ApiParque l = ls.get(i);
             rs += l.getStrJson();
             
             if (i<(ls.size()-1)){
                 rs +=",";
             }
        }
      
        rs += "]";
        return rs;  
    }
    
    
    /*Guarda un parque*/
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insPark(JsonObject js ) {
        
       
       String name = js.getString("name");
       String state = js.getString("state");
       int capa = js.getInt("capacity");
       int id_park = parkdao.getMaxId();
       
       ApiParque p = new ApiParque(id_park, name, state, capa, "Open");
       
        Response.Status st = null;
        
        boolean b = parkdao.insertPark(p);
        
        URI location= null;
        try {
            location = new URI(urlpath+"/"+p.getId());
        } catch (URISyntaxException ex) {
            Logger.getLogger(NaturalParksResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Response.ResponseBuilder responseBuilder = Response.accepted();
        responseBuilder.status(Response.Status.CREATED);
        responseBuilder.type(MediaType.APPLICATION_JSON);
        responseBuilder.entity(p.getStrJson());
        responseBuilder.location(location);
        
        
        return responseBuilder.build();
    }
    
    /*Actualiza los datos de un parque*/
    @PUT
    @Path("{parkId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePark(@PathParam("parkId") int id, JsonObject js){
       String name = js.getString("name");
       String state = js.getString("state");
       String status = js.getString("status");
       int capa = js.getInt("capacity");
       int id_park = js.getInt("id");
       
        Response.ResponseBuilder responseBuilder = Response.accepted();
        
       if (id >0 && (id == id_park)){
           ApiParque p = new ApiParque(id_park, name, state, capa, status);
           boolean b = parkdao.updatePark(p);
            responseBuilder.status(Response.Status.NO_CONTENT);
       }else{
           responseBuilder.status(Response.Status.NOT_FOUND);
       }
       
        responseBuilder.type(MediaType.APPLICATION_JSON);
        return responseBuilder.build();
    }
    
    /*Borra un parque*/
    @DELETE
    @Path("{parkId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delPark(@PathParam("parkId") int id){
        
        Response.ResponseBuilder responseBuilder = Response.accepted();
       if (id >0 ){
           boolean b = parkdao.deletepark(id);
            responseBuilder.status(Response.Status.NO_CONTENT);
       }else{
           responseBuilder.status(Response.Status.NOT_FOUND);
       }
       
        responseBuilder.type(MediaType.APPLICATION_JSON);
        return responseBuilder.build();
    }
    
}
