/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Paladion.teamwork.services;

import com.Paladion.teamwork.beans.UserDataBean;
import com.Paladion.teamwork.beans.VehicleBean;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface VehicleService {
	
	public void addVehicle(VehicleBean vb);
//    public void editTask(TaskBean tb);
//    public void deleteTask(TaskBean tb);
//    public List<TaskBean> getAllTasks();
//    public boolean deleteTask(int id);

    public List<VehicleBean> GetAllVehicle();

    public boolean DeleteVehicle(int id);

    public Object GetVehicleById(int id);

    public boolean UpdateVehicleDetails(VehicleBean vehicleBean);







    
}
