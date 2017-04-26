<%-- 
    Document   : CreateProject
    Created on : 24 Apr, 2017, 5:28:38 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
    <script type="text/javascript">
      $(function() {
          $('#idDateField').datepicker();
      });
  </script>
    
       <%@include file="Header.jsp" %>
        <h1>Create New Project</h1>
	   <form:form action="CreateProject.do" method="post" commandName="ProjectM">

	   <table border="2">
	   <tr><td> Project Name : </td><td>
	   <form:input path="projectname" /> 
	   </td> </tr>
	  
	  <tr><td> Engineer : </td><td>
	   <form:input path="engineer" /> 
	   </td> </tr>
	   <tr>    
          <td colspan="2"><input type="submit" value="Save" /></td>    
         </tr>    
        </table> 
	   
</form:form>
<center>${Lerror}</center><br>      
	   
    </body>
</html>
