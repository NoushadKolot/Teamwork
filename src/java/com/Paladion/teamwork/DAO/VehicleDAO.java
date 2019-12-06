/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Paladion.teamwork.DAO;

import com.Paladion.teamwork.beans.VehicleBean;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface VehicleDAO {
	
	public void addVehicleDao(VehicleBean vb);
//        public boolean deleteTask(int id);

    public List<VehicleBean> GetAllVehicle();



    public Object GetVehicleById(int id);

    public boolean UpdateVehicleDetails(VehicleBean vehicleBean);

   
    public boolean deleteVehicle(int id);


         
}
