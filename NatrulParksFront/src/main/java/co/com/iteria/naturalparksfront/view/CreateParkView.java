/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.iteria.naturalparksfront.view;

import co.com.iteria.naturalparksfront.model.Parque;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Usuario
 */

@ManagedBean (name="CParkView")
@ViewScoped
public class CreateParkView {
    private Parque park;
    private static ResourceBundle bundle;
    private final String url = "http://localhost:8080/NaturalParksBack/webresources/api/v1/parks";
    
    
    public void init(){
        park = new Parque(0, "", "", 0, "");
        UIViewRoot vr = FacesContext.getCurrentInstance().getViewRoot();
        bundle = ResourceBundle.getBundle("co.com.iteria.naturalparksfront.bundle.NatutalparkBundle", vr.getLocale());
        
    }
    
    public void createPark(){
        
        if(valcampos()){
            Client client = ClientBuilder.newClient();
            WebTarget rs = client.target(url);
            
            Response resp  = rs.request(MediaType.APPLICATION_JSON_TYPE)
                               .post(park.toJson());
            
            if (resp.getStatus() == 201){
                String str = resp.getLocation().toString().replaceAll(".xhtml", "");
                str = str.substring(str.lastIndexOf("/")+1);
                
                 String urlredirect = "ParkView.xhtml?action=edit&code="+str;
                 System.out.println(urlredirect);
                 FacesContext.getCurrentInstance()
                        .getApplication()
                        .getNavigationHandler()
                        .handleNavigation(FacesContext.getCurrentInstance(),
                                           null, urlredirect);
            }else{
                 setMessage("Error", bundle.getString("MSG_ERR_ADDP"));
            }
            
        }
    }

    private boolean valcampos(){
        
        String strms = "";
        boolean rt = true;
        
        if(park.getName() == null || park.getName().length() ==0){
            strms += bundle.getString("MSG_ERR_TXT")+" "+
                     bundle.getString("TITLE_NAME_HD")+" "+
                     bundle.getString("MSG_NO_VOID")+"\n";
            rt= false;
        }
        
        if(park.getState()== null || park.getState().length() ==0){
            strms += bundle.getString("MSG_ERR_TXT")+" "+
                     bundle.getString("TITLE_STATE_HD")+" "+
                     bundle.getString("MSG_NO_VOID")+"\n";
            rt= false;
        }
        if(park.getStatus()== null || park.getStatus().length() ==0){
            strms += bundle.getString("MSG_ERR_TXT")+" "+
                     bundle.getString("TITLE_TITLE_STATUS_HD")+" "+
                     bundle.getString("MSG_NO_VOID")+"\n";
            rt= false;
        }
        if(park.getCapacity() ==0){
            strms += "jgfjh"+ bundle.getString("MSG_ERR_TXT")+" "+
                     bundle.getString("TITLE_CAPACITY_HD")+" "+
                     bundle.getString("MSG_NO_VOID")+"\n";
            rt= false;
        }
        
        if(strms.length()>0){
            setMessage("Error", strms);
        }
        
        return rt;
    }
    
     public void setMessage(String strtitle, String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
         
        context.addMessage(null, new FacesMessage(strtitle, msg));
    }
     
    public Parque getP() {
        return park;
    }

    public void setP(Parque p) {
        this.park = p;
    }
}
