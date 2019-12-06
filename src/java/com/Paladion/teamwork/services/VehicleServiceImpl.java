/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Paladion.teamwork.services;

import com.Paladion.teamwork.DAO.VehicleDAO;
import com.Paladion.teamwork.beans.VehicleBean;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author pal
 */
public class VehicleServiceImpl implements VehicleService {
    
@Autowired
@Qualifier(value="VehicleDAO")
 VehicleDAO VD;
    
    public void addVehicle(VehicleBean vb){
        
        
        VD.addVehicleDao(vb);
    }

    @Override
    public List<VehicleBean> GetAllVehicle() {
        return VD.GetAllVehicle(); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public Object GetVehicleById(int id) {
        return VD.GetVehicleById(id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean UpdateVehicleDetails(VehicleBean vehicleBean) {
        return VD.UpdateVehicleDetails(vehicleBean);//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean DeleteVehicle(int id) {
     return VD.deleteVehicle(id);   //To change body of generated methods, choose Tools | Templates.
    }
    
 
   
}
