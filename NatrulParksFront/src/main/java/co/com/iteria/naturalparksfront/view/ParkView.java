/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.iteria.naturalparksfront.view;

import co.com.iteria.naturalparksfront.model.Parque;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    private static ResourceBundle bundle;
    
    public void init(){
        UIViewRoot vr = FacesContext.getCurrentInstance().getViewRoot();
        bundle = ResourceBundle.getBundle("co.com.iteria.naturalparksfront.bundle.NatutalparkBundle", vr.getLocale());
        
        Client client = ClientBuilder.newClient();
        WebTarget rs = client.target(url);
        
        JsonObject jsonObj = (JsonObject)rs.resolveTemplate("parkId", selectedId)
                                            .request(MediaType.APPLICATION_JSON)
                                            .get(JsonObject.class);
            
        this.selectedParque = Parque.parkFromJSON(jsonObj);                                          
    }
    
    
    public void updatePark() {
        Client client = ClientBuilder.newClient();
        WebTarget rs = client.target(url);
        
        Response stresp  = rs.resolveTemplate("parkId", ""+selectedParque.getId())
                     .request(MediaType.APPLICATION_JSON_TYPE)
                     .put(selectedParque.toJson());
        
        if (stresp.getStatus() == 204){
            addMessage(bundle.getString("MSG_UPDATE_OK"));
        }else{
            addMessage(bundle.getString("MSG_UPDATE_FAIL"));
        }           
    }
    
    public void deletePark(){
        Client client = ClientBuilder.newClient();
        WebTarget rs = client.target(url);
        
        Response resp = rs.resolveTemplate("parkId", ""+selectedParque.getId())
                          .request(MediaType.APPLICATION_JSON_TYPE)
                          .delete();
        
         if (resp.getStatus() == 204){
            FacesContext.getCurrentInstance()
                        .getApplication()
                        .getNavigationHandler()
                        .handleNavigation(FacesContext.getCurrentInstance(),
                                           null, "ListaNaturalParkView.xhtml");
            addMessage(bundle.getString("MSG_DEL_OK"));
        }else{
            addMessage(bundle.getString("MSG_DEL_FAIL"));
        }   
    }
   
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
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
