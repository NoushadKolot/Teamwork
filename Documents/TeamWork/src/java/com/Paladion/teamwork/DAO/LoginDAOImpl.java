/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Paladion.teamwork.DAO;

import com.Paladion.teamwork.beans.LoginBean;
import com.Paladion.teamwork.beans.UserBean;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author Administrator
 */
public class LoginDAOImpl implements LoginDAO{
    
    @Autowired
    @Qualifier(value="hibernate4AnnotatedSessionFactory")
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public UserBean Login(LoginBean LB) {
        UserBean SessUserBean=null;
        Session session = this.sessionFactory.openSession();
        String userid="";
        String SQL_QUERY1 ="select userid from LoginBean as o where o.username=? and o.password=?";
        System.out.println(SQL_QUERY1);
        Query query1 = session.createQuery(SQL_QUERY1);
        query1.setParameter(0,LB.getUsername());
        query1.setParameter(1,LB.getPassword());
        List list1=query1.list();
        try
        {
        userid=(String)list1.get(0);
        }
        catch(IndexOutOfBoundsException ex)
        { return null;}
        String SQL_QUERY2 = "from UserBean as o where o.userid=?";
        System.out.println(SQL_QUERY2);
        Query query2 = session.createQuery(SQL_QUERY2);
        query2.setParameter(0,userid);
        
        List list2 = query2.list();
        Iterator it= list2.iterator();
        while(it.hasNext())
        {
         SessUserBean=(UserBean) it.next();
         System.out.print("Logged in user is"+SessUserBean.getName());
        }
                        
        if ((list2 != null) && (list2.size() > 0)) 
        {
        return SessUserBean;
        }
        else
        {
        return null;
        }            
    }
        
    
}
