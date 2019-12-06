/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Paladion.teamwork.services;

import com.Paladion.teamwork.beans.SystemBean;
import com.Paladion.teamwork.DAO.AdminDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class AdminServiceImpl implements AdminService
{
    @Autowired
    @Qualifier(value = "AdminDAO")        
    AdminDAO AdminDAO;
    
    public void setAdminDAO(AdminDAO AdminDAO) {
        this.AdminDAO = AdminDAO;
    }
    
        @Override
    public boolean SaveSettings(SystemBean SysModel) {
        
        return AdminDAO.SaveSettings(SysModel);
        
    }

    @Override
    public SystemBean getSystemSettings() {
    return AdminDAO.getSystemSettings();    
    }
    
}
