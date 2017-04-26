/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Paladion.teamwork.controllers;

import com.Paladion.teamwork.beans.ProjectBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Administrator
 */
@Controller
public class ProjectController {
	
	@RequestMapping(value="/CreateProject",method=RequestMethod.GET)
public String Login()
{
return "CreateProject";
}
    
    public String CreateProject(ProjectBean pBean){return "";}
    public String updateProject(ProjectBean pBean){return "";}
    public String deleteProject(String id){return "";}
    
}
