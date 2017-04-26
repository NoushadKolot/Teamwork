/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Paladion.teamwork.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Administrator
 */
public class TemplateController {
		@RequestMapping(value="/CreateTaskTemplate",method=RequestMethod.GET)
public String Login()
{
return "CreateTaskTemplate";
}
    
}
