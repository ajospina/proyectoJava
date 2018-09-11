/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.iteria.naturalparksfront.view;

import java.io.Serializable;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author Usuario
 */
@ManagedBean
@SessionScoped
public class TemplateView implements Serializable{
    private String languaje = null;
    public static final String LANG_ES = "es";
    public static final String LANG_EN = "en";

    public String getLanguaje() {
        return languaje;
    }

    public void setLanguaje(String languaje) {
        this.languaje = languaje;
    }
    
    @PostConstruct
    public void init(){
        System.out.println(Locale.getDefault());
        UIViewRoot vr = FacesContext
                        .getCurrentInstance()
                        .getViewRoot();
        
        languaje = vr.getLocale().getLanguage();
        
    }
    
    //caputa el cambio de combo
    public void onLangChange(ValueChangeEvent vc){
        UIViewRoot vr =  FacesContext.getCurrentInstance().getViewRoot();
        
        if(LANG_ES.equals(vc.getNewValue())){
            vr.setLocale(new Locale(LANG_ES));            
        }else{
            vr.setLocale(Locale.US);
        }
    }
}
