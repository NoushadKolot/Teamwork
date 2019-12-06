/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Paladion.teamwork.controllers;

import com.Paladion.teamwork.beans.UserDataBean;
import com.Paladion.teamwork.services.AdminService;
import com.Paladion.teamwork.services.EmailService;

import com.Paladion.teamwork.services.LoginService;
import com.Paladion.teamwork.services.TeamService;
import com.Paladion.teamwork.services.UserService;
import com.Paladion.teamwork.utils.CommonUtil;
import com.Paladion.teamwork.utils.userValidator;
import java.text.ParseException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
/**
 *
 * @author user
 */
@Controller
public class UserController {
	
@Autowired
@Qualifier(value="userValidator")
userValidator UV;
   
@InitBinder
protected void initBinder(WebDataBinder binder) {
      binder.addValidators(UV);
} 
    
 @Autowired
 @Qualifier(value="LoginService")
 LoginService LS;
 
 UserDataBean lb=null;
	
@Autowired
@Qualifier(value="UserService")
UserService userService;

@Autowired
@Qualifier(value="TeamService")
TeamService TService;

@Autowired
@Qualifier(value="EmailService")
EmailService EService;

@Autowired
@Qualifier(value="CommonUtil")
CommonUtil CU;

@Autowired
@Qualifier(value="AdminService")
AdminService AdminS;
	
@ModelAttribute("UserM")
 public UserDataBean PopulateLoginBean() 
{
   return new UserDataBean(); // populates form for the first time if its null
}
	
    @RequestMapping(value="/CreateUser",method=RequestMethod.GET)
    public ModelAndView createUser(HttpServletRequest req)
    {   
        String[] authorizedRoles = {"admin","manager"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        try{
        ModelAndView result= new ModelAndView("CreateUser");
        result.addObject("AllTeams",TService.getAllTeams());
        return result;
        }
        catch(Exception ex){
        ex.printStackTrace();
        return new ModelAndView("Error");
        }
    }
	
    @RequestMapping(value="/CreateUser",method=RequestMethod.POST)
    public ModelAndView createUser(@ModelAttribute("UserM")@Validated UserDataBean userBean,BindingResult result,HttpServletRequest req )
    {
        String[] authorizedRoles = {"admin","manager"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        try{
        if (result.hasErrors()) {
            //validates the user input, this is server side validation
            System.out.println("error!!!!!!!!");
            ModelAndView model= new ModelAndView("CreateUser");
            model.addObject("AllTeams",TService.getAllTeams());
            return model;
        }
        System.out.println("in user controller create user post method");
        userBean.setPassword(CU.randomGenetator(12));
	boolean results = userService.addUser(userBean);
	if(results==true){
               
            if(EService.sendUserCreationMail(userBean))
            {
                 ModelAndView model= new ModelAndView("CreateUser");
                 model.addObject("AllTeams",TService.getAllTeams());
                 model.addObject("Message","User has been created successfully");
                 return model;
            }
            else{
                return new ModelAndView("Error");
            }
            //Send Email to user
//            EmailBean ebean=new EmailBean();
//            EmailUtil eutil=new EmailUtil();
//            ebean.setTo(userBean.getEmail());
//            String subject="Paladion TeamWork- User Account Created";
//            StringBuilder mess=new StringBuilder();
//                
//            mess.append("Dear ").append(loginBean.getUsername())
//            .append("\n\nYour account has been created in the Paladion Teamwork Protal ")
//            .append("(http://10.0.1.128/TeamWork/).\nPlease Log into your account using the following credentials.\n\n")
//            .append("UserName : ").append(loginBean.getEmail()).append("\nPassword : ").append(loginBean.getPassword())
//            .append("\n\nBest Regards,\nTeam Paladion");
//               
//            String message=mess.toString();
//                
//            //String message="Dear "+loginBean.getUsername()+"\n\nYour account has been created in the Paladion Teamwork Application ( http://10.0.1.128/TeamWork/ ).\nPlease Log into your account using the following credentials\n\nUsername: "+loginBean.getEmail() +"\nPassword: "+loginBean.getPassword()+"\n\n\n\nBest Regards\nTeam Paladion";
//            ebean.setSubject(subject);
//            ebean.setMessage(message);
//            HttpSession Sess=req.getSession(false);
//            SystemBean syssetting = AdminS.getSystemSettings();
//            eutil.sendEmail(ebean, syssetting);
               
            //Update user list in session
                
            //important get current user deatils
            //Sess.setAttribute("AllUsers", userService.GetAllUser());
           
        }
           
        else{
            ModelAndView object= new ModelAndView("CreateUser");
            object.addObject("AllTeams",TService.getAllTeams());
            object.addObject("Message","User creation failed. Email is registered.");
        return object;
        }
        }
        catch(Exception ex){
        ex.printStackTrace();
        return new ModelAndView("Error");
        }
    }


@RequestMapping(value="/ViewAllUser",method=RequestMethod.GET)
public ModelAndView ViewAllUser(HttpServletRequest req )
    {
        String[] authorizedRoles = {"admin","manager","scheduling"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        try{
        System.out.println("ViewAllUser");
    
	List<UserDataBean> userList=userService.GetAllUser();
	ModelAndView result=new ModelAndView("ViewAllUser");
        result.addObject("AllUsers",userList);
	return result;
        }
        catch(Exception ex){
        ex.printStackTrace();
        return new ModelAndView("Error");
        }
    }

@RequestMapping(value="/DeleteUser",method=RequestMethod.GET)
    public ModelAndView DeleteUser(@RequestParam int id, HttpServletRequest req) throws ParseException
    {
        String[] authorizedRoles = {"admin","manager"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        try{
        ModelAndView result=new ModelAndView("ViewAllUser");
        if(id!=0){
            userService.DeleteUser(id);
            List<UserDataBean> userList=userService.GetAllUser();
            HttpSession Sess=req.getSession(false);
            //update user list in session
            Sess.setAttribute("AllUsers", userList);
	    result.addObject("AllUsers",userList);
            result.addObject("Message","User deactivated successfully");    
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
    
@RequestMapping(value="/GetUserDetails",method=RequestMethod.GET)
public ModelAndView GetUserDetails(@RequestParam int id, HttpServletRequest req)
{
    String[] authorizedRoles = {"admin","manager"};
    if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
    try{   
    ModelAndView result=new ModelAndView("UpdateUser");
    if(id!=0)
    {
        result.addObject("UserDetail",userService.GetUserById(id));
        result.addObject("AllTeams",TService.getAllTeams());
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
    
    @RequestMapping(value="/UpdateUserDetails",method=RequestMethod.POST)
    public ModelAndView updateUserDetails(@ModelAttribute("UserM") UserDataBean UserBean,  HttpServletRequest req)
    {
        String[] authorizedRoles = {"admin","manager"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        
        try{
        System.out.println("in update user details controller method");
        userService.UpdateUserDetails(UserBean);
        List<UserDataBean> userList=userService.GetAllUser();
	ModelAndView result=new ModelAndView("ViewAllUser");
        result.addObject("AllUsers",userList);
        result.addObject("Message","User Details Updated Successfully");
	return result;
        }
        catch(Exception ex){
        ex.printStackTrace();
        return new ModelAndView("Error");
        }
        
    } 
    
    //Method to test ajax call, delete the method method
    
    @RequestMapping(value="/GetUserServlet",method=RequestMethod.GET)
public HttpServletResponse UserDetails(@RequestParam String userName, HttpServletRequest req, HttpServletResponse response)
{
    System.out.println("Reached method");
    String[] authorizedRoles = {"admin","manager"};
    if(!CU.checkUserAuthorization(authorizedRoles, req));
    try{  
      response.setContentType("text/plain");  
        
    
    if(userName != null)
    {
        UserDataBean ub = userService.GetUserById(4089);
        //result.addObject("AllTeams",TService.getAllTeams());
       // return ub.toString();
        
        String greetings = "Hello " + userName;
		
        
	response.getWriter().write("sas");
        return response;
    }
  
  //  return "User Not found!!";
    }
    catch(Exception ex){
    ex.printStackTrace();
    return response;
    }
    return response;
}


   @RequestMapping(value="/getAjax",method=RequestMethod.GET)
    public ModelAndView getAjax(HttpServletRequest req)
    {   String[] authorizedRoles = {"admin","manager"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        try{
        ModelAndView result= new ModelAndView("ajaxSample");
       // result.addObject("AllTeams",TService.getAllTeams());
        return result;
        }
        catch(Exception ex){
        ex.printStackTrace();
        return new ModelAndView("Error");
        }
    } 
    
}

