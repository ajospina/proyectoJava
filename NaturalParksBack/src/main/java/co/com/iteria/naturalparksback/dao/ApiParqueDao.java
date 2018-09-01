/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */ 
package co.com.iteria.naturalparksback.dao;

import co.com.iteria.naturalparksback.model.ApiParque;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;

/**
 *
 * @author Usuario
 */
public class ApiParqueDao { 
    
    public boolean insertPark(ApiParque p){
        SessionFactory sf= null;
        Session sesion = null;
        Transaction tx =null;
        boolean rs= true;
        try{
            sf = HibernateUtil.getSessionFactory();
            sesion = sf.openSession();
            tx = sesion.beginTransaction();
            sesion.save(p);
            tx.commit();
            sesion.close();
        }catch(Exception e){
            tx.rollback();
            rs= false;      
        }
        return rs;
    }
    
    
    public boolean updatePark(ApiParque p){
        boolean rs = true;
        SessionFactory sf= null;
        Session sesion = null;
        Transaction tx =null;
        
        try{
            sf = HibernateUtil.getSessionFactory();
            sesion = sf.openSession();
            tx = sesion.beginTransaction();
            sesion.update(p);
            tx.commit();
            sesion.close();
        }catch(Exception e){
            tx.rollback();
            rs= false;      
        }
        return rs;
    }
    
    public ApiParque getPark (int code){
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session sesion = sf.openSession();
        
        ApiParque p = (ApiParque)sesion.get(ApiParque.class, code);
        sesion.close();
        return p;
    }
    
    public List<ApiParque> getParkByStatus(String status){
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session sesion = sf.openSession();
        
        Query query = null;
        if (status == null || status.length() == 0){
            query = sesion.createQuery("From ApiParque");
        }else{
            query = sesion.createQuery("FROM ApiParque where upper(status) = ?");
            query.setString(0,status.toUpperCase());            
        }
        
        
        List<ApiParque> rs = query.list();
        sesion.close();
        
        return rs;
    }
    
    public List<ApiParque> getAllPark(){        
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session sesion = sf.openSession();
        
        Query query = sesion.createQuery("From ApiParque");
        List<ApiParque> rs = query.list();
        sesion.close();
        
        return rs;
    }
    
    public boolean deletepark(int id){
        boolean rs = true;
        SessionFactory sf= null;
        Session sesion = null;
        Transaction tx =null;
        
        ApiParque p = new ApiParque();
        p.setId(id);
        
        try{
            sf = HibernateUtil.getSessionFactory();
            sesion = sf.openSession();
            tx = sesion.beginTransaction();
            sesion.delete(p);
            tx.commit();
            sesion.close();
        }catch(Exception e){
            tx.rollback();
            rs= false;      
        }
        return rs;
    }
    
    public int getMaxId(){
        int id = 0;
        
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session sesion = sf.openSession();
        
        Query query = sesion.createQuery("select max(id ) from ApiParque");
        
        List<Integer> l = query.list();
        if (!l.isEmpty()){
            
            id = ((Integer)l.get(0)).intValue() +1;
        }
        
        return id;  
    }

}
