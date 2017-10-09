/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Paladion.teamwork.controllers;

import com.Paladion.teamwork.beans.TeamBean;
import com.Paladion.teamwork.services.TeamService;
import com.Paladion.teamwork.utils.CommonUtil;
import java.text.ParseException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author santosh.babar
 */

@Controller
public class TeamController {
    
@Autowired
@Qualifier(value="CommonUtil")
CommonUtil CU;

@Autowired
@Qualifier(value="TeamService")
TeamService TS;
    
@ModelAttribute("TeamM")
public TeamBean populate()
{
    return new TeamBean();
}
    
    @RequestMapping(value="/CreateTeam",method=RequestMethod.GET)
     public ModelAndView CreateTeam(HttpServletRequest req)
    {   
        String[] authorizedRoles = {"admin","manager"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        ModelAndView result=new ModelAndView("CreateTeam");
        //List<TeamBean> TeamList= TS.getAllTeams();
        //result.addObject("AllTeams",TeamList);
        return result;
    }
     
     
     
@RequestMapping(value="/CreateTeam",method=RequestMethod.POST)
    public ModelAndView createTask(@ModelAttribute("TeamM")TeamBean TB, HttpServletRequest req) 
    {
        String[] authorizedRoles = {"admin","manager"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        
	System.out.println("\n inside create Team method ");
	TS.addTeam(TB); 	
	return new ModelAndView( "CreateTeam","Message","Team Created Successfully"  );  
    }	
    
    
@RequestMapping(value="/GetAllTeams",method=RequestMethod.GET)
public ModelAndView GetAllTasks(HttpServletRequest req)
{ 
    String[] authorizedRoles = {"admin","manager"};
    if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
    
    ModelAndView result=new ModelAndView("DisplayTasks");
    List<TeamBean> TeamList= TS.getAllTeams();
    result.addObject("AllTeams",TeamList);
    return result; 
}


@RequestMapping(value="/DeleteTeam",method=RequestMethod.GET)
    public ModelAndView DeleteTask(@RequestParam int id, HttpServletRequest req) throws ParseException
    {
        String[] authorizedRoles = {"admin","manager"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        
        if(id!=0)
           {
               boolean value= TS.deleteTeam(id);
               ModelAndView result=new ModelAndView("DisplayTasks");
               List<TeamBean> TBList= TS.getAllTeams();
               result.addObject("AllTeams",TBList);
               result.addObject("Message","Team Deleted Succefully");
               return result; 
           }
           else{
                return new ModelAndView("Error");
            }
    }
}