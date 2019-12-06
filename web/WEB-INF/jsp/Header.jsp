<%-- 
    Document   : Header.jsp
    Created on : Jun 20, 2017, 2:58:56 PM
    Author     : santosh.babar
--%>
<%@page import="com.Paladion.teamwork.beans.UserDataBean"%>
<!DOCTYPE html>
<html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<head>  
   <style>
       
.container {
    overflow: hidden;
    background-color: #333;
    font-family: Arial;
}

.container a {
    float: left;
    font-size: 16px;
    color: white;
    text-align: center;
    padding: 14px 16px;
    text-decoration: none;
}

.dropdown {
    float: left;
    overflow: auto;
}

.dropdown .dropbtn {
    font-size: 16px;    
    border: none;
    outline: none;
    color: white;
    padding: 14px 16px;
    background-color: inherit;
}

.dropdown .dropbtn1 {
    font-size: 16px;    
    border: none;
    outline: none;
    color: white;
    padding: 14px 16px;
    background-color: #cc0000;
}

.container a:hover, .dropdown:hover .dropbtn {
    background-color: #cc0000;
}

.dropdown-content {
    display: none;
    position: absolute;
    background-color: #999966;
    min-width: 160px;
    box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
    z-index: 1;
}

.dropdown-content a {
    float: none;
    color: black;
    padding: 12px 16px;
    text-decoration: none;
    display: block;
    text-align: left;
}

.dropdown-content a:hover {
    background-color: #ddd;
}

.dropdown:hover .dropdown-content {
    display: block;
}

.textarea{
    text-align: left;
    font-size: 16px;
    font-family: 'Roboto', sans-serif;
    height: 80px;
    padding: 4px 6px 6px 6px; 
    border: 2px solid #b9b9b9;
}  

.textarea:hover {
  border: 2px solid #b9b9b9;
  
  border-top: 5px solid #a0a0a0;
  -moz-box-shadow: inset 0 1px 2px rgba(0,0,0,0.1);
  -webkit-box-shadow: inset 0 1px 2px rgba(0,0,0,0.1);
  box-shadow: inset 0 1px 2px rgba(0,0,0,0.1);
}


</style>
</head>
    
<body>
    <%-- Session Validation Code --%>
    <% 
        if (session==null||null==session.getAttribute("Luser"))
           {
               response.sendRedirect("Login.do");
               return;
           }
    %> <%-- Session Code Ends --%>
    
     
    <%-- Fetch the UserName from the Session --%>
    <%! UserDataBean b; String name; String role;%>
    <% 
        b=(UserDataBean)session.getAttribute("Luser"); 
        name=b.getUsername().toString();
        role=b.getRole().toString();
    %>
    
    <%-- Header Code Begins --%> 
    <br><br>   
    <div align="center">
    <ul>
        <li><a href="Welcome.do">Home</a></li>
        
        <% if(role.equalsIgnoreCase("Manager")||role.equalsIgnoreCase("Admin")||role.equalsIgnoreCase("Scheduling"))
        {%>
        <div class="dropdown">
            <button class="dropbtn">Projects</button>
                <div class="dropdown-content">
                    <% if(role.equalsIgnoreCase("Manager")||role.equalsIgnoreCase("Scheduling"))
                    {%>
                    <a href="CreateProject.do">Create New Project</a>
                  
                    <a href="GetAllProjects.do">View All Projects</a>
                    
                    <a href="CreateVehicle.do">Create New Vehicle</a>
                    
                    <a href="ViewAllVehicle.do">View All Vehicle</a>
                      <%}%>
                </div>
        </div>
        <%}%>
        <div class="dropdown">
            <button class="dropbtn">Activities</button>
                <div class="dropdown-content">
                    <% if(role.equalsIgnoreCase("Manager")||role.equalsIgnoreCase("Scheduling"))
                    {%>
                    <a href="CreateActivity.do">Schedule Activity</a>
                    
                    <a href="RequestSchedule.do">Request Schedule</a>
                    <%}%>
                    <a href="showAllActivity.do">View All Activities</a>
                    
                </div>
        </div> 
        
        <% if(role.equalsIgnoreCase("Manager")||role.equalsIgnoreCase("Admin")||role.equalsIgnoreCase("Scheduling"))
        {%>
        <div class="dropdown">
            <button class="dropbtn">Administration</button>
                <div class="dropdown-content">
                <a href="CreateCompany.do">Create Company</a>
                <a href="GetAllCompany.do">View All Companies</a>
                <a href="CreateTeam.do">Teams</a>
                <a href="ViewAllUser.do">View Users</a>
                <% if(role.equalsIgnoreCase("Manager")||role.equalsIgnoreCase("Admin"))
                    {%>
                    <a href="CreateUser.do">Create User</a>
                    <a href="Administration.do">System Properties</a>
                <%}%>
                </div>
        </div> 
        <%}%>
        
        <% if(role.equalsIgnoreCase("Lead")||role.equalsIgnoreCase("Manager"))
        {%>
          <div class="dropdown">
            <button class="dropbtn">Tasks</button>
                <div class="dropdown-content">
                <a href="CreateTask.do">Create Task</a>
                <a href="GetAllTasks.do">View Tasks</a>
                </div>
        </div> 
        <%}%>
        
        
         <% if(role.equalsIgnoreCase("Lead")||role.equalsIgnoreCase("Manager"))
        {%>
          <div class="dropdown">
            <button class="dropbtn">Activity Template</button>
                <div class="dropdown-content">
                <a href="CreateTaskTemplate.do">Create Activity Template</a>
                <a href="GetAllTaskTemplates.do">View Activity Templates</a>
                </div>
        </div> 
          <%}%>
        <div class="dropdown">
            <a href="index.do"><button class="dropbtn">KB (In Progress)</button></a>
                <div class="dropdown-content">
                <a href="threatProfile.do">Threat Profile</a>
                <a href="CodeUnderstanding.do">Code Understanding</a>
                <a href="Info.do">Information Gathering Sheet</a>
                <a href="CodeUnderstanding.do">Code Understanding</a>
                </div>
        </div> 
        <li style="float:right"><a class="active" href="Logout.do">Logout</a></li>
       
        <div class="dropdown" style="float:right">
            <button class="dropbtn"><%=name%></button>
                <div class="dropdown-content">
                <a href="ChangePassword.do">Change Password</a>
                </div>
        </div> 
    
    </ul>
    </div>
         
    <br>  
    <br>
    <div> 
        <script>
        if("${Message}"){            
        alert("${Message}");
        }
        
        </script>
        
    
        
    <%-- Header Code Ends --%>  
   

