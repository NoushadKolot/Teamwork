package com.Paladion.teamwork.services;


import com.Paladion.teamwork.DAO.UserDAO;
import com.Paladion.teamwork.beans.LoginBean;
import com.Paladion.teamwork.beans.UserBean;
import java.util.List;

import com.Paladion.teamwork.DAO.LoginDAO;
import com.Paladion.teamwork.DAO.UserDAO;
import com.Paladion.teamwork.beans.LoginBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class UserServiceImpl implements UserService{
	
@Autowired
@Qualifier(value="UserDAO")
UserDAO userDAO;
	
@Override
	public void addUser(LoginBean loginBean) {
		userDAO.addUser(loginBean);
		System.out.println("com.Paladion.teamwork.services.UserServiceImpl.addUser()");	

}
	
	
	
	@Override
	public List<LoginBean> getUsersByRole(String role) {
		return userDAO.getUsersByRole(role);
	}
        
        @Override
	public List<LoginBean> ViewAllUser() {
		return userDAO.ViewAllUser();
		
}

    @Override
    public boolean DeleteUser(int id) {
        return userDAO.DeleteUser(id);
    }
    
    @Override
	 public boolean UpdateUser(int id) {
		return userDAO.UpdateUser(id);
		
}

    

    

    
        
		
}



