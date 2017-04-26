<%-- 
    Document   : Welcome
    Created on : 13 Apr, 2017, 8:45:16 PM
    Author     : Administrator
--%>

<%@page import="com.Paladion.teamwork.beans.UserBean"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Paladion Teamwork</title>
    </head>
    
        <%@include file="Header.jsp" %>
        <%! UserBean b; String name;%>
        <% b=(UserBean)session.getAttribute("Luser"); 
        name=b.getName().toString(); 
        %>
        <h4 align="center">Welcome <%=name%></h4>
         <a href="CreateProject.do" style="text-decoration:none"> <input type="button" value="Start New Project" style="height:30px; width:180px"/> </a>
	    <br><br>
	   <a href="CreateTask.do" style="text-decoration:none"> <input type="button" value="Create New Task" style="height:30px; width:180px"/> </a>
	   <br><br><a href="CreateTaskTemplate.do" style="text-decoration:none"> <input type="button" value="Create New Task Template" style="height:30px; width:230px"/> </a>
        
        <hr>
        <table border="2" width="60%">
            
            <tr><th width="17%">Project ID </th>
                <th width="20%">Project Title</th>
                <th width="17%">Lead</th>
                <th width="17%">Start Date</th>
                <th width="17%">End Date</th>
                <th width="17%">Status</th></tr>
            
            
            
        </table>
        
        
