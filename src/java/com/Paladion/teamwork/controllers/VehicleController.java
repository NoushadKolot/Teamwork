/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Paladion.teamwork.controllers;

import com.Paladion.teamwork.beans.UserDataBean;
import com.Paladion.teamwork.beans.VehicleBean;
import com.Paladion.teamwork.services.VehicleService;
import com.Paladion.teamwork.utils.CommonUtil;
import java.text.ParseException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;



/**
 *
 * @author Administrator
 */
@Controller
public class VehicleController {	
    
@ModelAttribute("VehicleM")
public VehicleBean populate()
{
    return new VehicleBean();
}


@Autowired
@Qualifier(value="CommonUtil")
CommonUtil CU;
   
@Autowired
@Qualifier(value="VehicleService")
VehicleService VS;
		


	
@RequestMapping(value="/CreateVehicle",method=RequestMethod.GET)
     public ModelAndView CreateVehicle(HttpServletRequest req)
    {   
        String[] authorizedRoles = {"admin","manager","lead"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        
	return new ModelAndView("CreateVehicle");
    }
	
@RequestMapping(value="/CreateVehicle",method=RequestMethod.POST)
    public ModelAndView createVehicle(@ModelAttribute("VehicleM") VehicleBean VB,BindingResult result,HttpServletRequest req) 
    {
        String[] authorizedRoles = {"admin","manager","lead"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        try{
        if (result.hasErrors()) {
            //validates the user input, this is server side validation
            System.out.println("error!!!!!!!!");
            return new ModelAndView("CreateVehicle");
        }
        
	System.out.println("\n inside create Vehicle method ");
	VS.addVehicle(VB); 	
	System.out.println("vehicle Created with Taskid"+VB.getVehicleid());
	return new ModelAndView( "CreateVehicle","Message","Vehicle Created Successfully");
        }
        catch(Exception ex){
        ex.printStackTrace();
        return new ModelAndView("Error");
        }
    }	
    
    
//@RequestMapping(value="/GetAllTasks",method=RequestMethod.GET)
//public ModelAndView GetAllTasks(HttpServletRequest req)
//{ 
//    String[] authorizedRoles = {"admin","manager","lead"};
//    if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
//    try{
//    ModelAndView result=new ModelAndView("DisplayTasks");
//    List<TaskBean> TBList= TS.getAllTasks();
//    result.addObject("AllTasks",TBList);
//    return result; 
//    }
//    catch(Exception ex){
//    ex.printStackTrace();
//    return new ModelAndView("Error");
//    }
//}
//
//
//@RequestMapping(value="/DeleteTask",method=RequestMethod.GET)
//    public ModelAndView DeleteTask(@RequestParam int id, HttpServletRequest req) throws ParseException
//    {
//        String[] authorizedRoles = {"admin","manager","lead"};
//        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
//        try{
//        if(id!=0)
//           {
//               boolean value= TS.deleteTask(id);
//               ModelAndView result=new ModelAndView("DisplayTasks");
//               List<TaskBean> TBList= TS.getAllTasks();
//               result.addObject("AllTasks",TBList);
//               result.addObject("Message","Task Deleted Succefully");
//               return result; 
//           }
//           else{
//                return new ModelAndView("Error");
//            }
//        }
//        catch(Exception ex){
//        ex.printStackTrace();
//        return new ModelAndView("Error");
//        }
//    }  
   @RequestMapping(value="/ViewAllVehicle",method=RequestMethod.GET)
public ModelAndView ViewAllVehicle(HttpServletRequest req )
    {
        String[] authorizedRoles = {"admin","manager","scheduling"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        try{
        System.out.println("ViewAllVehicle");
    
	List<VehicleBean> vehicleList=VS.GetAllVehicle();
	ModelAndView result=new ModelAndView("ViewAllVehicle");
        result.addObject("AllVehicle",vehicleList);
	return result;
        }
        catch(Exception ex){
        ex.printStackTrace();
        return new ModelAndView("Error");
        }
    }

@RequestMapping(value="/DeleteVehicle",method=RequestMethod.GET)
    public ModelAndView DeleteVehicle(@RequestParam int id, HttpServletRequest req) throws ParseException
    {
        String[] authorizedRoles = {"admin","manager"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        try{
        ModelAndView result=new ModelAndView("ViewAllVehicle");
        if(id!=0){
           boolean status =  VS.DeleteVehicle(id);
            List<VehicleBean> vehicleList=VS.GetAllVehicle();
           
	    result.addObject("AllVehicle",vehicleList);
            result.addObject("Message","vehicle deleted successfully");    
            }
            else{
            result=new ModelAndView("Welcome");
            }
            return result;
        }
        catch(Exception ex){
        ex.printStackTrace();
        return new ModelAndView("Error");
        }
    }
@RequestMapping(value="/GetVehicleDetails",method=RequestMethod.GET)
public ModelAndView GetVehicleDetails(@RequestParam int id, HttpServletRequest req)
{
    String[] authorizedRoles = {"admin","manager"};
    if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
    try{   
    ModelAndView result=new ModelAndView("UpdateVehicle");
    if(id!=0)
    {
        result.addObject("VehicleDetail",VS.GetVehicleById(id));
       
        return result;
    }
    else{
        result=new ModelAndView("Error");
    }
    return result;
    }
    catch(Exception ex){
    ex.printStackTrace();
    return new ModelAndView("Error");
    }
}

    @RequestMapping(value="/UpdateVehicleDetails",method=RequestMethod.POST)
    public ModelAndView UpdateVehicleDetails(@ModelAttribute("VehicleM") VehicleBean vehicleBean,  HttpServletRequest req)
    {
        String[] authorizedRoles = {"admin","manager"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        
        try{
        System.out.println("in update vehicle details controller method");
        VS.UpdateVehicleDetails(vehicleBean);
        List<VehicleBean> VehicleList=VS.GetAllVehicle();
	ModelAndView result=new ModelAndView("ViewAllVehicle");
        result.addObject("AllVehicle",VehicleList);
        result.addObject("Message","Vehicle Details Updated Successfully");
	return result;
        }
        catch(Exception ex){
        ex.printStackTrace();
        return new ModelAndView("Error");
        }
        
    } 
 


}


