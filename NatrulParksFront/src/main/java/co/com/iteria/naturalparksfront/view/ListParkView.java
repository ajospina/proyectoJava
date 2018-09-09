/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.iteria.naturalparksfront.view;

import co.com.iteria.naturalparksfront.model.ApiParque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;


/**
 *
 * @author Usuario
 */
@ManagedBean (name="ntParksView")
@ViewScoped
public class ListParkView {
    
    private List<ApiParque> lparks = new ArrayList();
    private String url = "http://localhost:8080/NaturalParksBack/webresources/api/v1/parks/";
    
    public String listParks(){
        
        Client cliente  = ClientBuilder.newClient();        
        WebTarget rs = cliente.target(url);
        
        JsonArray jsonarray;
        jsonarray = (JsonArray)rs.request(MediaType.APPLICATION_JSON).get(JsonArray.class);
        
        Iterator iter =  jsonarray.iterator();
        
        lparks.clear();
        while(iter.hasNext()){
            JsonObject val = (JsonObject)iter.next();
            
            ApiParque p  = ApiParque.parkFromJSON(val);
            lparks.add(p);
        }
        
        return null;
    }

    public List<ApiParque> getLparks() {
        return lparks;
    }

    public void setLparks(List<ApiParque> lparks) {
        this.lparks = lparks;
    }   
}
