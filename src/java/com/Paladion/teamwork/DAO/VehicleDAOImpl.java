/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Paladion.teamwork.DAO;

import com.Paladion.teamwork.beans.UserDataBean;
import com.Paladion.teamwork.beans.VehicleBean;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author pal
 */
public class VehicleDAOImpl implements VehicleDAO {
    
    @Autowired
    @Qualifier(value="hibernate4AnnotatedSessionFactory")
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
	
    @Override
    public void addVehicleDao(VehicleBean VB) {	
        Session session1 = sessionFactory.getCurrentSession();
	Transaction tx = session1.beginTransaction();
        try{
            session1.save(VB);
            System.out.println("Vehicle create successfully");
            tx.commit();
        }
        catch(Exception e){
            System.out.println("Exception occured. "+e.getMessage());
        }
        finally{
            if(session1.isOpen()){
            System.out.println("-------------Closing session--------------");
            session1.close();
            }
        }
    }

    @Override
    public List<VehicleBean> GetAllVehicle() {
        Session session1 = sessionFactory.getCurrentSession();
	Transaction tx = session1.beginTransaction();
        try{        
            String SQL_QUERY1= "from VehicleBean";
            Query query2 = session1.createQuery(SQL_QUERY1);
	    List<VehicleBean> VehicleList = query2.list();
            tx.commit();
            return VehicleList;	
	}
        catch(Exception e){
        System.out.println("Exception occured. "+e.getMessage());
        return null;
        }
        finally{
            if(session1.isOpen()){
            System.out.println("-----------Closing session-------------");
            session1.close();
            }
        } //To change body of generated methods, choose Tools | Templates.
    }

   

    @Override
    public Object GetVehicleById(int id) {
           Session session1 = sessionFactory.getCurrentSession();
        Transaction tx = session1.beginTransaction();
        try{ 
            VehicleBean vbean=new VehicleBean();
            String SQL_QUERY1= "from VehicleBean where vehicleid=?";
            Query query2 = session1.createQuery(SQL_QUERY1);
            query2.setParameter(0, id);
	    List list2 = query2.list();
            Iterator it= list2.iterator();
                while(it.hasNext()){
                     vbean=(VehicleBean)it.next();
                }
            tx.commit();
            return vbean;       
        }catch(Exception e){
        System.out.println("Exception occured. "+e.getMessage());
        return null;
        }
        finally{
            if(session1.isOpen()){
            System.out.println("----------Closing session------------");
            session1.close();
            }
        } //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean UpdateVehicleDetails(VehicleBean vehicleBean) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try{
            String sql = "UPDATE vehicle SET model=?,regnumber=?,price=? where vehicleid=?";
            SQLQuery query = session.createSQLQuery(sql);
            query.setParameter(0,vehicleBean.getModel());
            query.setParameter(1,vehicleBean.getRegnumber());
            query.setParameter(2,vehicleBean.getPrice());
           query.setParameter(3,vehicleBean.getVehicleid());
            query.executeUpdate();
            tx.commit();
            return true;
        }catch(Exception e){
        System.out.println("Exception occured. "+e.getMessage());
        return false;
        }
        finally{
            if(session.isOpen()){
            System.out.println("-----------Closing session------------");
            session.close();
            }
        } //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteVehicle(int id) {
        
         Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try{
            String sql = "delete from vehicle where vehicleid=?";
            SQLQuery query = session.createSQLQuery(sql);
            query.setParameter(0, id);
            query.executeUpdate();
            tx.commit();
            return true;	
	}catch(Exception e){
        System.out.println("Exception occured. "+e.getMessage());
        return false;
        }
        finally{
            if(session.isOpen()){
            System.out.println("--------Closing session----------");
            session.close();
            }
        }
    
    }
    

    

    

}
