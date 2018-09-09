/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.iteria.naturalparksfront.view;

import co.com.iteria.naturalparksfront.model.Parque;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Usuario
 */
@ManagedBean (name="ParkView")
@ViewScoped
public class ParkView {
    
    private String selectedId;
    private Parque selectedParque;
    private final String url = "http://localhost:8080/NaturalParksBack/webresources/api/v1/parks/{parkId}";
    
    public void init(){
        Client client = ClientBuilder.newClient();
        WebTarget rs = client.target(url);
        
        JsonObject jsonObj = (JsonObject)rs.resolveTemplate("parkId", selectedId)
                                            .request(MediaType.APPLICATION_JSON)
                                            .get(JsonObject.class);
            
        this.selectedParque = Parque.parkFromJSON(jsonObj);                                          
    }

    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    public Parque getSelectedParque() {
        return selectedParque;
    }

    public void setSelectedParque(Parque selectedParque) {
        this.selectedParque = selectedParque;
    }
    
    
    
    
    
}
