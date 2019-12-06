    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Paladion.teamwork.controllers;

import com.Paladion.teamwork.beans.EmailBean;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.Paladion.teamwork.beans.UserDataBean;
import com.Paladion.teamwork.beans.MapTemplateTaskBean;
import com.Paladion.teamwork.beans.ActivityBean;
import com.Paladion.teamwork.beans.TemplateBean;
import com.Paladion.teamwork.beans.ActivityTransactionBean;
import com.Paladion.teamwork.beans.ActivityTransactionWrapper;
import com.Paladion.teamwork.beans.AllocationBean;
import com.Paladion.teamwork.beans.ProjectBean;
import com.Paladion.teamwork.beans.fileuploadBean;
import com.Paladion.teamwork.services.AdminService;
import com.Paladion.teamwork.services.TeamService;
import com.Paladion.teamwork.services.TemplateService;
import com.Paladion.teamwork.services.UserService;
import com.Paladion.teamwork.utils.CommonUtil;
import com.Paladion.teamwork.utils.ActivityValidator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.Paladion.teamwork.services.ActivityService;
import com.Paladion.teamwork.services.EmailService;
import com.Paladion.teamwork.services.ProjectService;
import com.Paladion.teamwork.services.TaskService;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author Administrator
 */
@Controller
@SessionAttributes("ProjectM")
public class ActivityController {
	
//@Autowired
//@Qualifier(value="ActivityValidator")
//ProjectValidator PV;
//
//@InitBinder
//protected void initBinder(WebDataBinder binder) {
//      binder.addValidators(PV);
//}
    
@Autowired
@Qualifier(value="TemplateService")
TemplateService TS;

@Autowired
@Qualifier(value="TaskService")
TaskService TaskS;

@Autowired
@Qualifier(value="CommonUtil")
CommonUtil CU;
    
@Autowired
@Qualifier(value="ActivityService")
ActivityService AS;

@Autowired
@Qualifier(value="ProjectService")
ProjectService PS;

@Autowired
@Qualifier(value="UserService")
UserService US;

@Autowired()
@Qualifier(value = "AdminService")
AdminService Aservice;

@Autowired()
@Qualifier(value = "TeamService")
TeamService TeamS;

@Autowired()
@Qualifier(value = "EmailService")
EmailService EmailS;

@Autowired
@Qualifier(value="EmailBean")
EmailBean eBean;

@Autowired
@Qualifier(value="ActivityValidator")
ActivityValidator projectBeanValidator;
   
@InitBinder("ProjectM")
protected void initProjectBeanBinder(WebDataBinder binder) {
      binder.addValidators(projectBeanValidator);
} 
	
@ModelAttribute("ProjectM")
public ActivityBean populate()
{
    return new ActivityBean();
}

@ModelAttribute("ActivityTB")
public ActivityTransactionBean populateATB()
{
    return new ActivityTransactionBean();
}

@ModelAttribute("AllocationB")
public AllocationBean populateBean()
{
    return new AllocationBean();
}

@ModelAttribute("filebean")
public fileuploadBean populate1()
{
    return new fileuploadBean();
}

static Logger log = Logger.getLogger(ActivityController.class.getName());

	//Copy of this method is present below which redirects to beutifully arranged JSP page.
    @RequestMapping(value="/CreateActivity1",method=RequestMethod.GET)
    public ModelAndView CreateActivity(HttpServletRequest req)
    {   
        try
        {
        String[] authorizedRoles = {"admin","manager","lead","scheduling"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        ModelAndView model=new ModelAndView("CreateActivity");
	
            model.addObject("AllTemplates", TS.getAllTemplates());
            model.addObject("AllLeads", US.GetUsersByRole("lead"));
            model.addObject("AllTeams",  TeamS.getAllTeams());
            model.addObject("AllProjects",PS.getAllProjects());  
            return model;
	}
        catch(Exception ex){
        System.out.println("Exception occured. "+ex.getMessage());
        return new ModelAndView("Error");
        }   
    }
    
    
    @RequestMapping(value="/getEngineers",method=RequestMethod.POST)
    public @ResponseBody ModelAndView getEngineers(@ModelAttribute("ProjectM")@Validated ActivityBean AB, BindingResult result, HttpServletRequest req, HttpServletResponse response)
    {   
        try{ 
        String[] authorizedRoles = {"admin","manager","lead","scheduling"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        
        if (result.hasErrors()) {
            //validates the user input, this is server side validation
            System.out.println("Validation error in getEngineers()");
            try{
                ModelAndView model=new ModelAndView("CreateActivity");
                model.addObject("AllTemplates", TS.getAllTemplates());
                model.addObject("AllLeads", US.GetUsersByRole("lead"));
                model.addObject("AllTeams",  TeamS.getAllTeams());
                model.addObject("AllProjects",PS.getAllProjects());  
                return model;
            }
            catch(Exception ex){
            return new ModelAndView("Error");
            }                    
        }
      
	List<UserDataBean> availableEngineers = US.getAvailableEngineers(AB.getStartdate(),AB.getEnddate(),US.GetUsersByRole("engineer"));
        AB.setLead(US.GetUserById(AB.getLeadid()).getUsername());
        List <TemplateBean> TemplateList = TS.getAllTemplates();
        ModelAndView model=new ModelAndView("SelectEngineers");
        model.addObject("engineers",availableEngineers);
        model.addObject("AllTemplates", TemplateList);
        model.addObject("activitybean", AB);
        return model;
	}
        catch(Exception ex){
            System.out.println("Exception occured. "+ex.getMessage());
            return new ModelAndView("Error");
        }
    }
    

    //Schedule a activity
    @RequestMapping(value="/ScheduleActivity",method=RequestMethod.POST)
    public Object CreateNewProject(@ModelAttribute("ProjectM")@Validated ActivityBean AB, BindingResult result, HttpServletRequest req,Model E) throws Exception
    {
        HttpSession sess= req.getSession(false);
        ModelAndView results = null;
        List <TemplateBean> TemplateList;
        List <UserDataBean> LeadList;
        try{
        String[] authorizedRoles = {"admin","manager","lead","scheduling"};
        if(!CU.checkUserAuthorization(authorizedRoles, req))  return new ModelAndView("Error");
       
        if (result.hasErrors()) {
            //validates the user input, this is server side validation
            System.out.println("Validation error in Schedule Activity method");
            ModelAndView model=new ModelAndView("CreateActivity");
            try{
                model.addObject("AllTemplates", TS.getAllTemplates());
                model.addObject("AllLeads", US.GetUsersByRole("lead"));
                model.addObject("AllTeams",  TeamS.getAllTeams());
                model.addObject("AllProjects",PS.getAllProjects());
                return model;
            }
            catch(Exception ex){
            return new ModelAndView("Error");
            }                    
        }
	 
        boolean mailStatus = false;
        System.out.println("\n inside create Project POST method ");
        AB.setMandays(CU.getWorkingDays(AB.getStartdate(),AB.getEnddate()));
        AB.setStatus("New");
        //int opid=Integer.parseInt(AB.getOpid());
        
        if(AB.getProjectid() == 0){
            AB.setOpid("No Project Selected");
           mailStatus=EmailS.sendSchedulingMail(AB, req.getSession(false));
        }
        else{
            ProjectBean PB= PS.getProjectDeatails(AB.getProjectid());
            if(PB.getOpid() == null){
                AB.setOpid("Awaiting OPID");
            }
            else{
            AB.setOpid(PB.getOpid());
            }
            mailStatus=EmailS.sendSchedulingMail(AB, PB);
        }
                
        AS.addProject(AB);
        
        
        AllocationBean AloB = new AllocationBean();
        AloB.setActivityId(AB.getActivityid());
        AloB.setAllocationStartdate(AB.getStartdate());
        AloB.setAllocationEndenddate(AB.getEnddate());
        AloB.setStatus("Allocated");
        AloB.setEngineerId(AB.getEngtracker());
        if(AS.allocateResource(AloB)!=true){
        }
       
        
        System.out.println("Project Created with Project id "+AB.getActivityid());
        System.out.println("Man days :"+AB.getMandays());
       
        ActivityTransactionWrapper PTW=new ActivityTransactionWrapper();
        List<ActivityTransactionBean> PSBList ;
        List<ActivityTransactionBean> PTBList1;
        //ActivityBean PRDATA=AS.getProjectById(AB.getActivityid());
        List<MapTemplateTaskBean> MTTB=TS.getAllWeights(AB.getTemplateid());
            
        UserDataBean engBean = US.GetUserById(AB.getEngtracker());
        String eng =engBean.getUsername();
            
        PSBList=  CU.setTaskHours(AB, MTTB);
        PTW.setProjectlist(PSBList);
        for(ActivityTransactionBean PTB: PSBList){
            PTB.setUserid(engBean.getUserid());
            PTB.setEngname(engBean.getUsername());
        }
            
        PTBList1= CU.updateProjectTransaction(PSBList, AB,req.getSession(false));
        for(ActivityTransactionBean PTB : PTBList1){
        AS.insertProjectTransaction(PTB);
        }
            
//      results=new ModelAndView("AssignTaskToUsers");
//      //Engineer Availability Code Starts           
//      List<UserDataBean> availableEngineers = US.getAvailableEngineers(AB.getStartdate(), AB.getEnddate(),CU.getUsersByRole("engineer", sess));
//      Engineer Availability Code Ends
//      results.addObject("AllEngineers",availableEngineers);
//      results.addObject("ProjectW",PTW);

        
        UserDataBean sessuser=(UserDataBean) sess.getAttribute("Luser");
        if(sessuser.getRole().equalsIgnoreCase("scheduling")){
            if(mailStatus==true){
            return new ModelAndView("redirect:/Welcome.do","Message","Activity has been scheduled. Mail sent successfully.");
            }
            else{
            return new ModelAndView("redirect:/Welcome.do","Message","Activity has been scheduled. Scheduling mail not sent.");    
            }
        }
        
        
        results=new ModelAndView("DisplayActivityProgress");
        if(mailStatus == true){
        results.addObject("Message","Activity has been scheduled. Mail sent successfully.");    
        }
        else{
            results.addObject("Message","Activity has been scheduled. Scheduling mail not sent.");    
        }
        
        results.addObject("ProjectData",AB);
        results.addObject("TaskDetails",PTBList1);
        results.addObject("Engineer",eng);
        return results;
    }
    catch(Exception ex){
        System.out.println("Exception occured. "+ex.getMessage());
        TemplateList=TS.getAllTemplates();
        LeadList=US.GetUsersByRole("lead");   
        results = new ModelAndView("CreateActivity","Message","Activity Creation failed due to an error");
        results.addObject("AllTemplates", TemplateList);
        results.addObject("AllLeads", LeadList);
        results.addObject("AllTeams",  TeamS.getAllTeams());
        return results;
    }
        
    }
    
//    public String updateProject(ActivityBean pBean){return "";}
//    public String deleteProject(String id){return "";}
    
    @RequestMapping(value="/showAllActivity",method=RequestMethod.GET)
    public ModelAndView showAllActivity(HttpServletRequest req)
    {
        try{
        HttpSession sess= req.getSession(false);
        UserDataBean sessuser=(UserDataBean) sess.getAttribute("Luser");
	ModelAndView result=new ModelAndView("DisplayActivity");
        List<ActivityBean> PBList=(List<ActivityBean>)AS.getAllProjects(sessuser.getUserid(), sessuser.getRole());
        result.addObject("AllProjects",PBList );
        this.getAllProjectsDetails(req);
	return  result;
        }
        catch(Exception ex){
            System.out.println("Exception occured. "+ex.getMessage());
            return new ModelAndView("Error");
        }
    }
      
    @RequestMapping(value="/AssignTaskToEngineers", method=RequestMethod.GET)
    public ModelAndView AssignTask(HttpServletRequest req)
    {
        String[] authorizedRoles = {"admin","manager","lead"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        
        try{
        ModelAndView result= new ModelAndView("AssignTaskToUsers");
        result.addObject("AllEngineers",null);
        result.addObject("ProjectW",null);
        result.addObject("Message","Please Select a Project From the Project List");
        return result;
        }
        catch(Exception ex){
            System.out.println("Exception occured. "+ex.getMessage());
            return new ModelAndView("Error");
        }
    }
    
    //To assign engineers to the tasks in the project
    @RequestMapping(value="/AssignTaskToEngineers", method=RequestMethod.POST)
    public ModelAndView AssignTaskToEngineer(@ModelAttribute("ProjectW")ActivityTransactionWrapper ProjectW,HttpServletRequest req) throws Exception
    {
        try{
        String[] authorizedRoles = {"admin","manager","lead"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        
        String projid=req.getParameter("activityid");
        int projectid=Integer.parseInt(projid);
        ActivityBean PRDATA=AS.getProjectById(projectid);
	List <ActivityTransactionBean> PTBList=ProjectW.getProjectlist();
        List <ActivityTransactionBean> PTBList1=new ArrayList<ActivityTransactionBean>();
        
        PTBList1= CU.updateProjectTransaction(PTBList, PRDATA,req.getSession(false));
        for(ActivityTransactionBean PTB: PTBList1){
        AS.insertProjectTransaction(PTB);
        }
        CU.sendSchedulingMailToEngineers(PTBList1,req.getSession(false),PRDATA.getActivityname());
        ModelAndView result=new ModelAndView("DisplayActivityProgress");
        result.addObject("TaskDetails",PTBList1);
        result.addObject("ProjectData",PRDATA);
        return result;
        }
        catch(Exception ex){
            System.out.println("Exception occured. "+ex.getMessage());
            return new ModelAndView("Error");
        }
    }
    
    //To display individual project progress
    @RequestMapping(value="/showProgress",method=RequestMethod.GET)
    public ModelAndView showProjectProgress(@RequestParam int id,HttpServletRequest req) throws ParseException
    {
        try{
           ModelAndView result;
           List<ActivityTransactionBean> PSBList;
           ActivityBean PRDATA=AS.getProjectById(id);
           PSBList = AS.getProjectTransaction(id);
           String eng=PSBList.get(0).getEngname();
           //If engineers not assigned, redirect to assign engineers to tasks.
           if(PSBList.isEmpty()){
           //HttpSession sess=req.getSession(false);
           ActivityTransactionWrapper PTW=new ActivityTransactionWrapper();
           List<MapTemplateTaskBean> MTTB=TS.getAllWeights(PRDATA.getTemplateid());
           PSBList=  CU.setTaskHours(PRDATA, MTTB);
           PTW.setProjectlist(PSBList);
           result=new ModelAndView("AssignTaskToUsers");
           List<UserDataBean> Alleng=US.GetUsersByRole("engineer");
           List<UserDataBean> availableEngineers = US.getAvailableEngineers(PRDATA.getStartdate(), PRDATA.getEnddate(),Alleng);
             
           result.addObject("AllEngineers",availableEngineers);
           result.addObject("ProjectW",PTW);
           return result;
           }
           //return project progress
           
           else{
           result=new ModelAndView("DisplayActivityProgress");
           result.addObject("ProjectData",PRDATA);
           result.addObject("TaskDetails",PSBList);
           result.addObject("Engineer",eng);
           return result;
           }
        }
        catch(Exception ex){
            System.out.println("Exception occured. "+ex.getMessage());
            return new ModelAndView("Error");
        }
    }
    
    //Update status of the individual task in the project
    @RequestMapping(value="/updateTask_Status",method=RequestMethod.GET)
    public ModelAndView updateTaskStatus(@RequestParam int pid,@RequestParam int tid, @RequestParam String status, HttpServletRequest req) throws ParseException
    {
        String[] authorizedRoles = {"admin","manager","lead","engineer"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        
        try{
        boolean value= AS.updateTaskStatus(tid,status);
        if(value==true){
           List<ActivityTransactionBean> PSBList;
           ActivityBean PRDATA=AS.getProjectById(pid);
           PSBList = AS.getProjectTransaction(pid);
 
           ModelAndView result=new ModelAndView("DisplayActivityProgress");
           result.addObject("ProjectData",PRDATA);
           result.addObject("TaskDetails",PSBList);
           return result;
        }
        else{
            ModelAndView result=new ModelAndView("Error");
            result.addObject("Message","Something Went Wrong");
            return result;
        }
        }
        catch(Exception ex){
            System.out.println("Exception occured. "+ex.getMessage());
            return new ModelAndView("Error");
        }
    }
    
    //Update status of the individual Activity
    @RequestMapping(value="/updateProjectStatus",method=RequestMethod.GET)
    public ModelAndView updateProjectStatus(@RequestParam int pid,@RequestParam String status,HttpServletRequest req) throws ParseException
    {
        String[] authorizedRoles = {"admin","manager","lead"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        ModelAndView result=new ModelAndView();
        try{
        boolean value=false;
        //HttpSession sess= req.getSession(false);
        //UserDataBean sessuser=(UserDataBean) sess.getAttribute("Luser");
        //String role=sessuser.getRole();
        
        ActivityBean AB = AS.getProjectById(pid);
        
        //Insert check for all task status to be completed
        if(AB.getStatus().equalsIgnoreCase("progress") && status.equalsIgnoreCase("completed")){
        value= AS.updateProjectStatus(pid,status);
        //        if(status.equalsIgnoreCase("completed")){
        //            PS.updateTaskStatus(pid);
        //        }
        }
        
        else if(AB.getStatus().equalsIgnoreCase("new") && status.equalsIgnoreCase("progress"))
        {
            value= AS.updateProjectStatus(pid,status);
        }
        
        else if(AB.getStatus().equalsIgnoreCase("completed") && status.equalsIgnoreCase("progress")){
            value= AS.updateProjectStatus(pid,status);
        }
        
        else if(AB.getStatus().equalsIgnoreCase("On Hold") && status.equalsIgnoreCase("progress")){
            value= AS.updateProjectStatus(pid,status);
        }
        
        else{
           List <ActivityTransactionBean> ATBL = AS.getProjectTransaction(pid);
           result=new ModelAndView("DisplayActivityProgress");
           result.addObject("ProjectData",AS.getProjectById(pid));
           result.addObject("TaskDetails",ATBL);
           result.addObject("Engineer",ATBL.get(0).getEngname());
           result.addObject("Message","Invalid status selected.");
           return result;
        }
       
        if(value == true){
          List <ActivityTransactionBean> ATBL = AS.getProjectTransaction(pid);
          result=new ModelAndView("DisplayActivityProgress");
          result.addObject("ProjectData",AS.getProjectById(pid));
          result.addObject("TaskDetails",ATBL);
          result.addObject("Engineer",ATBL.get(0).getEngname());
          result.addObject("Message","Activity status updated successfully.");
	  return  result;
        }
        
        else{
            result=new ModelAndView("Customerror");
            result.addObject("Message","You are not authorized to perform the action.");
            return result;
        }
        }
        catch(Exception ex){
            ex.printStackTrace();
            return new ModelAndView("Error");
        }
    }
    
    
    @RequestMapping(value="/updateTaskDelay",method=RequestMethod.GET)
    public ModelAndView updateTaskDelayGet(HttpServletRequest req) throws ParseException
    {
        return new ModelAndView("Customerror");
    }
    
    //Update delay to individual tasks in a project
    
    
    
@RequestMapping(value="/uploadfiles",method=RequestMethod.GET)
public ModelAndView uploaddocs(@RequestParam String pid,HttpServletRequest req)
{
    try{
    HttpSession sess=req.getSession();
    sess.setAttribute("uploadPID", pid);
    return new ModelAndView("DocumentUpload","SysSettings",Aservice.getSystemSettings());
    }
    catch(Exception ex){
        ex.printStackTrace();
        return new ModelAndView("Error");
    }
}


@RequestMapping(value="/uploadfiles",method=RequestMethod.POST)    
public ModelAndView uploaddocstoProject(HttpServletRequest req,@ModelAttribute fileuploadBean filebean,Model model)
    {
    String[] authorizedRoles = {"admin","manager","lead","engineer"};
    if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
    try{    
    HttpSession sess=req.getSession();    
    String PID=(String) sess.getAttribute("uploadPID");    
    List<MultipartFile> upfiles = filebean.getFiles();    
    List<String> fileNames = new ArrayList<String>();    
    
    if (null != upfiles && upfiles.size() > 0) 
        {
            for (MultipartFile multipartFile : upfiles) {
 
                String fileName = multipartFile.getOriginalFilename();
                fileNames.add(fileName);
                String filepath=Aservice.getSystemSettings().getUploadpath();
                File uploadFile = new File(filepath+File.separator+"files"+File.separator+PID, fileName);
                System.out.println(uploadFile);
                if(!uploadFile.exists())uploadFile.mkdirs();
                try
                {
                multipartFile.transferTo(uploadFile);
                } catch (IOException e) 
                {
                    e.printStackTrace();
                }
            }
        }
     return new ModelAndView("DocumentUpload");
    }
    catch(Exception ex){
        ex.printStackTrace();
        return new ModelAndView("Error");
    }
  }


@RequestMapping(value="/Downloadfiles",method=RequestMethod.POST)    
public void  Downloadfiles(HttpServletRequest req,Model model,HttpServletResponse response) throws FileNotFoundException, IOException, Exception
    {
    HttpSession sess=req.getSession();    
    String PID=(String) sess.getAttribute("DownloadPID");    
    System.out.println("projectid"+PID);    
    String filepath=Aservice.getSystemSettings().getUploadpath();
    
    File downloadfile = new File(filepath+File.separator+"files"+File.separator+PID);
    System.out.println("folder " + downloadfile.toString());
    String sourceFolderName =  downloadfile.toString();
    String outputFileName = downloadfile.toString()+".zip";
 
    FileOutputStream fos = new FileOutputStream(outputFileName);
    ZipOutputStream zos = new ZipOutputStream(fos);
    //level - the compression level (0-9)
    zos.setLevel(9);
 
    System.out.println("Begin to compress folder : " + sourceFolderName + " to " + outputFileName);
    addFolder(zos, sourceFolderName, sourceFolderName);
 
    zos.close();
    System.out.println("Program ended successfully!");
    //file download
   
    String downloadFolder = downloadfile.toString();
    String filename1=downloadfile.toString()+".zip";
    Path file = Paths.get(filename1);
    if (Files.exists(file)) {
    response.setContentType("application/zip");
    response.addHeader("Content-Disposition", "attachment; filename=" + filename1);
    try {
        Files.copy(file, response.getOutputStream());
        response.getOutputStream().flush();
    } catch (IOException e) {
        System.out.println("Error :- " + e.getMessage());
    }
    } 
    else {
    System.out.println("Sorry File not found!!!!");
    }
    //file download        
        
    }
 
    private static void addFolder(ZipOutputStream zos,String folderName,String baseFolderName)throws Exception{
        try{
        File f = new File(folderName);
        if(f.exists()){
 
            if(f.isDirectory()){
                //Thank to peter 
                //For pointing out missing entry for empty folder
                if(!folderName.equalsIgnoreCase(baseFolderName)){
                    String entryName = folderName.substring(baseFolderName.length()+1,folderName.length()) + File.separatorChar;
                    System.out.println("Adding folder entry " + entryName);
                    ZipEntry ze= new ZipEntry(entryName);
                    zos.putNextEntry(ze);    
                }
                File f2[] = f.listFiles();
                for(int i=0;i<f2.length;i++){
                    addFolder(zos,f2[i].getAbsolutePath(),baseFolderName);    
                }
            }else{
                //add file
                //extract the relative name for entry purpose
                String entryName = folderName.substring(baseFolderName.length()+1,folderName.length());
                System.out.print("Adding file entry " + entryName + "...");
                ZipEntry ze= new ZipEntry(entryName);
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(folderName);
                int len;
                byte buffer[] = new byte[1024];
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
                zos.closeEntry();
                System.out.println("OK!");
 
            }
        }else{
            System.out.println("File or directory not found " + folderName);
        }
    }
    catch(Exception ex){
        ex.printStackTrace();
        return;
    }
}
//download files end

//download files
@RequestMapping(value="/Downloadfiles",method=RequestMethod.GET)
public ModelAndView Downloadfiles(@RequestParam String pid,HttpServletRequest req)
{
    try{
    HttpSession sess=req.getSession();
    sess.setAttribute("DownloadPID", pid);
    return new ModelAndView("downloadDocuments","SysSettings",Aservice.getSystemSettings());
    }
    catch(Exception ex){
        ex.printStackTrace();
        return new ModelAndView("Error");
    }
}
     
    @RequestMapping(value="/GetAllProjectDetails",method=RequestMethod.GET)
    public void getAllProjectsDetails(HttpServletRequest req)
    {
        try{
        HttpSession sess= req.getSession(false);
        UserDataBean sessuser=(UserDataBean) sess.getAttribute("Luser");
	ModelAndView result=new ModelAndView("Welcome");
        List<ActivityBean> PBList=(List<ActivityBean>)AS.getAllProjects(sessuser.getUserid(), sessuser.getRole());
        int total_projects=PBList.size();
        int project_new=0;
        int project_progress=0;
        int project_completed=0;
        for(ActivityBean PB:PBList){
            if(PB.getStatus().equalsIgnoreCase("new")){
                project_new++;
            }
            if(PB.getStatus().equalsIgnoreCase("progress")){
                project_progress++;
              
            }
            if(PB.getStatus().equalsIgnoreCase("completed")){
                project_completed++;
              
            }
        }
        System.out.println("No of completed projects : "+project_completed);
        System.out.println("No of on going projects : "+project_progress);
        System.out.println("No of new projects : "+project_new);
        }
        catch(Exception ex){
        ex.printStackTrace();
        return;
        }
    }
    
    
    @RequestMapping(value="/updateTaskDelay",method=RequestMethod.POST)
    public ModelAndView updateTaskDelay(HttpServletRequest req) throws ParseException
    {
        String[] authorizedRoles = {"admin","manager","lead","engineer"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        try{
        String tid=req.getParameter("transId");
        String delay=req.getParameter("taskDelayTime");
        String pid=req.getParameter("activityid");
        int delayHours=Integer.parseInt(delay);
        int projectId=Integer.parseInt(pid);
        int transid=Integer.parseInt(tid);
        List<ActivityTransactionBean> PTBList=AS.getProjectTransaction(projectId);
        List<ActivityTransactionBean> PTBList2=new ArrayList<>();
        for(ActivityTransactionBean PTBean: PTBList)
        {
            if(PTBean.getTransid()==transid)
            {
                float hours= PTBean.getTaskhours()+ delayHours;
                PTBean.setTaskhours(hours);
                PTBean.setTaskdays(hours/9);
                PTBean.setStatus("Delayed");
                PTBList2.add(PTBean);
            }
           if(PTBean.getTransid()>transid)
           {
                PTBList2.add(PTBean);
            }  
        }
        List<ActivityTransactionBean> PTBList3=CU.updateDelayForTasks(PTBList2, delayHours);
        AS.updateProjectTransaction(PTBList3);
        ModelAndView result;
        List<ActivityTransactionBean> PSBList;
        ActivityBean PRDATA=AS.getProjectById(projectId);
        PSBList = AS.getProjectTransaction(projectId);   
        result=new ModelAndView("DisplayActivityProgress");
        result.addObject("Message","Delay updated successfully");
        result.addObject("ProjectData",PRDATA);
        result.addObject("TaskDetails",PSBList);
        return result;
        }
        catch(Exception ex){
        ex.printStackTrace();
        return new ModelAndView("Error");
        }
    }
    
    
     @RequestMapping(value="/updateTaskStatus",method=RequestMethod.GET)
    public ModelAndView updateTask_Status(@RequestParam int pid,@RequestParam int tid, @RequestParam String status, HttpServletRequest req) throws ParseException
    {
        String[] authorizedRoles = {"admin","manager","lead","engineer"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        try{
        //List<ProjectTransactionBean> PTBList=PS.getProjectTransaction(pid);
        ActivityTransactionBean PTBean = AS.getTransactionOnTransID(tid);
        List<ActivityTransactionBean> PTBList2=new ArrayList<>();
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String defaultD = "1990-01-01 00:00:00";
        Date defaultDate = formatter.parse(defaultD);
        int flag=0;
      
        //Get current system time
//      DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date1 = new Date();
//      long diffHours=0;
        
            if( PTBean!= null && PTBean.getTransid()==tid)
            {
//                if(status.equalsIgnoreCase("progress")){date2 = PTBean.getTaskstartdate();}
//                else{date2 = PTBean.getTaskenddate();}
                
//              long diff = date1.getTime() - date2.getTime();
//              long diffMinutes = diff / (60 * 1000) % 60;
//              diffHours = diff / (60 * 60 * 1000);
                
//                if(diffHours<2){  The value for hours should be taken from sys settings.
//                   
//                    
//                        Code to update the task delay reason.
//                }
                
                //updatedDate=DateUtils.addHours(date2, (int)diffHours);
                
//                if(status.equalsIgnoreCase("completed")){
//                float hours= PTBean.getTaskhours()+ diffHours;
//                PTBean.setTaskhours(hours);
//                PTBean.setTaskdays(hours/9);
//                }
              

                if(status.equalsIgnoreCase("progress"))
                {
                    if(PTBean.getStatus().equalsIgnoreCase("On Hold")){
                        
                        Date holdDate=PTBean.getHolddate();
                        long diff = date1.getTime() - holdDate.getTime();
                        long diffMinutes = diff / (60 * 1000) % 60;
                        long diffHours = diff / (60 * 60 * 1000);
                        System.out.println("\n ---- Task was on hold for "+diffHours+" Hours and "+diffMinutes+ " Minutes ----- \n" );
                        
                        //Update the delay time into the activity
                        PTBean.setStatus("Progress");
                        PTBean.setStartdate(date1);
                    }
                    else if (PTBean.getStatus().equalsIgnoreCase("Completed")){
                        PTBean.setEnddate(defaultDate);
                        PTBean.setStatus("Progress");
                    }
                    
                    else if (PTBean.getStatus().equalsIgnoreCase("progress")){
                        flag=1;
                    }
                    
                    else if(PTBean.getStatus().equalsIgnoreCase("new")){
                        PTBean.setStartdate(date1); 
                        PTBean.setStatus("Progress");
                    }
                }
                
                if(status.equalsIgnoreCase("completed"))
                {
                    
                     if(PTBean.getStatus().equalsIgnoreCase("On Hold")){
                        if(PTBean.getStartdate().compareTo(defaultDate) == 0){
                        flag=1;
                        }
                        else{
                        Date holdDate=PTBean.getHolddate();
                        long diff = date1.getTime() - holdDate.getTime();
                        long diffMinutes = diff / (60 * 1000) % 60;
                        long diffHours = diff / (60 * 60 * 1000);
                        System.out.println("\n ---- Task was on hold for "+diffHours+" Hours and "+diffMinutes+ " Minutes ----- \n" );
                        
                        //Update the delay time into the activity
                        PTBean.setEnddate(date1);
                        PTBean.setStatus("Completed");
                        }
                       }
                     
                     else if(PTBean.getStatus().equalsIgnoreCase("new")){
                         flag=1;
                     }
                     else if(PTBean.getStatus().equalsIgnoreCase("completed")){
                         flag=1;
                     }
                    
                     else if(PTBean.getStatus().equalsIgnoreCase("progress")){
                        PTBean.setEnddate(date1); 
                        PTBean.setStatus("Completed");
                    }
                }
                if(status.equalsIgnoreCase("Hold")){
                    
                    if(PTBean.getStatus().equalsIgnoreCase("progress")){
                    PTBean.setHolddate(date1);
                    PTBean.setStatus("On Hold");
                    }
                    
                    else if(PTBean.getStatus().equalsIgnoreCase("completed")){
                    PTBean.setHolddate(date1);
                    PTBean.setStatus("On Hold");
                    PTBean.setEnddate(defaultDate); 
                    }
                    
                    else if(PTBean.getStatus().equalsIgnoreCase("new")){
                    PTBean.setHolddate(date1);
                    PTBean.setStatus("On Hold");
                    }
                    else if(PTBean.getStatus().equalsIgnoreCase("On Hold")){
                    flag=1;
                    }
                }

                PTBList2.add(PTBean);
            }
//           if(PTBean.getTransid()>tid)
//           {
//               
//               PTBList2.add(PTBean);
//            }  
        
       // List<ProjectTransactionBean> PTBList3=CU.updateDelayForTasks(PTBList2, (int)diffHours);
        AS.updateProjectTransaction(PTBList2);
        ModelAndView result=new ModelAndView("DisplayActivityProgress");
        if(flag==1){
            result.addObject("Message","Invalid Status Selected");
        }
        else{
        result.addObject("Message","Status updated successfully");
        }
        result.addObject("ProjectData",AS.getProjectById(pid));
        result.addObject("TaskDetails",AS.getProjectTransaction(pid));
        return result;
        }
        catch(Exception ex){
        ex.printStackTrace();
        return new ModelAndView("Error");
        }
    }
    
    
    @RequestMapping(value="/deleteProject",method=RequestMethod.GET)
    public ModelAndView delete_Project(@RequestParam int pid, HttpServletRequest req) throws ParseException
    {
        String[] authorizedRoles = {"admin","manager","lead","scheduling"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        try{
            if(AS.deleteProject(pid) ){
            return new ModelAndView("redirect:/Welcome.do","Message","Activity Deleted Successfully");
            }
            else{
            return new ModelAndView("Error","Message","Something Went Wrong");
            }
        }
        catch(Exception ex){
        ex.printStackTrace();
        return new ModelAndView("Error");
        }
    }
    
    
    @RequestMapping(value="/addActivityTask",method=RequestMethod.GET)
    public ModelAndView addActivityTask(@ModelAttribute("ActivityTB") ActivityTransactionBean ATB, HttpServletRequest req)
    {   
        try
        {
        String[] authorizedRoles = {"admin","manager","lead"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        
        HttpSession sess=req.getSession(false); 
        String aID= req.getParameter("activityId");
        int activityID = Integer.parseInt(aID);
        
        ActivityBean AB = AS.getProjectById(activityID);
        UserDataBean eng = US.GetUserById(AB.getEngtracker());
        List<UserDataBean> availableEngineers = US.getAvailableEngineers(AB.getStartdate(),AB.getEnddate(),US.GetUsersByRole("engineer"));
        availableEngineers.add(eng);
       
	ModelAndView model=new ModelAndView("addTasktoActivity");
	
        model.addObject("AllTasks", TaskS.getAllTasks());
        model.addObject("AllEngineers", availableEngineers);
        model.addObject("ActivityId",AB.getActivityid());
        return model;
	}
        catch(Exception ex){
        ex.printStackTrace();
        return new ModelAndView("Error");
        }
    }
    
    @RequestMapping(value="/addActivityTask",method=RequestMethod.POST)
    public ModelAndView addTasktoActivity(HttpServletRequest req)
    { 
        try
        {
        String[] authorizedRoles = {"admin","manager","lead"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        
        SimpleDateFormat formatter1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String defaultDate = "1990-01-01 00:00:00";
        Date parsedDate = formatter.parse(defaultDate);
        ActivityTransactionBean ATB = new ActivityTransactionBean();
        Calendar cal = Calendar.getInstance();
        String aID= req.getParameter("activityid");
        int activityID = Integer.parseInt(aID);
        
        String uID= req.getParameter("userid");
        int UserID = Integer.parseInt(uID);
        
            System.out.println(req.getParameter("taskstartdate"));
        
        String tsDate = req.getParameter("taskstartdate")+" 10:00:00";
        String teDate = req.getParameter("taskenddate")+" 19:00:00";
        
        Date startD = formatter1.parse(tsDate);
        Date endD = formatter1.parse(teDate);
        
        long diff = endD.getTime() - startD.getTime();
        int days = (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
        
        UserDataBean eng = US.GetUserById(UserID);
        
        cal.setTime(startD);
        ATB.setActivityid(activityID);
        ATB.setTaskstartdate(cal);
        ATB.setTaskenddate(endD);
        //ATB.setActivityid(Integer.parseInt(req.getParameter("activityid")));
        ATB.setTaskname(req.getParameter("taskname"));
        ATB.setTaskdays(days);
        ATB.setTaskhours(days*9);
        ATB.setUserid(UserID);
        ATB.setEngname(eng.getUsername());
        ATB.setStatus("New");
        ATB.setStartdate(parsedDate);
        ATB.setEnddate(parsedDate);
        ATB.setHolddate(parsedDate);
        AS.insertProjectTransaction(ATB);
        ModelAndView result=new ModelAndView("DisplayActivityProgress");
        result.addObject("Message","Task has been added successfully");
        result.addObject("ProjectData",AS.getProjectById(activityID));
        result.addObject("TaskDetails",AS.getProjectTransaction(activityID));
        return result;
	}
        catch(Exception ex){
        ex.printStackTrace();
        return new ModelAndView("Error");
        }
    }
    
    
    //Delete deblow method TestCreateAct
  
    @RequestMapping(value="/CreateActivity",method=RequestMethod.GET)
    public ModelAndView TestCreateAct(HttpServletRequest req)
    {   
        try
        {
        String[] authorizedRoles = {"admin","manager","lead","scheduling"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        ModelAndView model=new ModelAndView("CreateActivity");
	
            model.addObject("AllTemplates", TS.getAllTemplates());
            model.addObject("AllLeads", US.GetUsersByRole("lead"));
            model.addObject("AllTeams",  TeamS.getAllTeams());
            model.addObject("AllProjects",PS.getAllProjects());  
            return model;
	}
        catch(Exception ex){
        System.out.println("Exception occured. "+ex.getMessage());
        return new ModelAndView("Error");
        }   
    }
  
    
    

     @RequestMapping(value="/editActivityDetails",method=RequestMethod.GET)
    public ModelAndView editActivityDetails(@RequestParam int activityId, HttpServletRequest req) throws ParseException
    {
        String[] authorizedRoles = {"admin","manager","lead","scheduling"};
        if(!CU.checkUserAuthorization(authorizedRoles, req)) return new ModelAndView("Error");
        try{
            
            ActivityBean AB = AS.getProjectById(activityId);
            
            
            ModelAndView model=new ModelAndView("UpdateActivityDetails");
           
            model.addObject("ActivityBean",AB);
            //if(AB.getProjectid() > 0 )
            ProjectBean PB =PS.getProjectDeatails(AB.getProjectid());
            model.addObject("ProjectBean",PB );
            List<AllocationBean> ListAloB = AS.getEngAllocationForActivity(activityId);
            if(ListAloB.size() == 1){
                model.addObject("AllocationBean",ListAloB.get(0));
                model.addObject("EngineerBean", US.GetUserById(ListAloB.get(0).getEngineerId()));
            }
            if(ListAloB.size() > 1){
                  model.addObject("AllocationBeanList",ListAloB);
            }
            model.addObject("AllTemplates", TS.getAllTemplates());
            model.addObject("AllLeads", US.GetUsersByRole("lead"));
            model.addObject("AllTeams",  TeamS.getAllTeams());
            model.addObject("AllProjects",PS.getAllProjects());  
            
           
            
	
            return model; 
        }
        catch(Exception e){
        System.out.println("Exception occured. "+e.getMessage());
        return new ModelAndView("Error");
        }
    }
    
    
    
    
     @RequestMapping(value="/getEngineersAjax",method=RequestMethod.POST)
    public void getEngineersAjax(@RequestParam String txtFromDate,@RequestParam String txtToDate , HttpServletRequest req, HttpServletResponse response)
    {   
        try{ 
        String[] authorizedRoles = {"admin","manager","lead","scheduling"};
        if(!CU.checkUserAuthorization(authorizedRoles, req));
         //response.setContentType("text/html"); 
        
        Date sDate=new SimpleDateFormat("MM/dd/yyyy").parse(txtFromDate);  
        Date eDate=new SimpleDateFormat("MM/dd/yyyy").parse(txtToDate);  
        List<UserDataBean> availableEngineers = new ArrayList<UserDataBean>();
        availableEngineers = US.getAvailableEngineers(sDate,eDate,US.GetUsersByRole("engineer"));
    
        response.setContentType("application/json");
        
        for(UserDataBean ub : availableEngineers){
            ub.setPassword("");
            ub.setOtp("");
            }
        
        String json = new Gson().toJson(availableEngineers);
        response.getWriter().write(json);  
        }
        catch(Exception ex){
           ex.printStackTrace();
        }
   
    }
    
    
    @RequestMapping(value="/updateActivityDetails",method=RequestMethod.POST)
    public ModelAndView updateActivityDetails(@ModelAttribute("ProjectM") ActivityBean AB, BindingResult result, HttpServletRequest req) throws Exception
    {
       // HttpSession sess= req.getSession(false);
       // ModelAndView results = null;
       
        try{
        String[] authorizedRoles = {"admin","manager","lead","scheduling"};
        if(!CU.checkUserAuthorization(authorizedRoles, req))  return new ModelAndView("Error");
        
        ActivityBean ActBean = AS.getProjectById(AB.getActivityid());
       
         if(AB.getProjectid()!=0){
            ProjectBean PB = PS.getProjectDeatails(AB.getProjectid());
            AB.setOpid(PB.getOpid()); }
        
        if(AB.getStartdate() == null)
        {AB.setStartdate(ActBean.getStartdate());}
        
        if(AB.getEnddate()== null)
        {AB.setEnddate(ActBean.getEnddate());}
        
        if(AB.getStartdate()!=null && AB.getEnddate()!=null){
        AB.setMandays(CU.getWorkingDays(AB.getStartdate(), AB.getEnddate()));
        }
        else{
            //Error - return no dates selected.
        }
        
        if(AB.getEngtracker() == 0)
        {AB.setEngtracker(ActBean.getEngtracker());}
      
        if(AB.getLeadid()!=0){
        AB.setLead(US.GetUserById(AB.getLeadid()).getUsername());}
        
        //update all feilds
        AB.setTemplateid(ActBean.getTemplateid());
        AB.setStatus(ActBean.getStatus());
        AB.setAssessmentType(ActBean.getAssessmentType());
        AB.setCompliance(ActBean.getCompliance());
        AB.setWhitelisting(ActBean.getWhitelisting());
        AB.setRequirements(ActBean.getRequirements());
        AB.setDetails(ActBean.getDetails());
        
        AS.updateActivityBean(AB);
        
        List<ActivityTransactionBean> PTBList1 = new ArrayList<ActivityTransactionBean>();
        //Update the activityTransactionBean if startdate or enddate or engineer is changed during update
        if(AB.getStartdate()!=ActBean.getStartdate() || 
                AB.getEnddate()!=ActBean.getEnddate() || ( 
                AB.getEngtracker()!=ActBean.getEngtracker() && AB.getEngtracker()!=0))
            {
            ActivityTransactionWrapper PTW=new ActivityTransactionWrapper();
            
            List<ActivityTransactionBean> DBATB = new ArrayList<ActivityTransactionBean>();
            List<ActivityTransactionBean> PSBList = new ArrayList<ActivityTransactionBean>();
            
            //ActivityBean PRDATA=AS.getProjectById(AB.getActivityid());
            DBATB = AS.getProjectTransaction(AB.getActivityid());
            List<MapTemplateTaskBean> MTTB=TS.getAllWeights(AB.getTemplateid());
            UserDataBean engBean = US.GetUserById(AB.getEngtracker());
            String eng =engBean.getUsername();
            int engid = engBean.getUserid();
            PSBList=  CU.setTaskHours(AB, MTTB);
            PTW.setProjectlist(PSBList);
                for(ActivityTransactionBean PTB: PSBList){
                PTB.setUserid(engid);
                PTB.setEngname(eng);
                }
                
            int i=0;
            PTBList1= CU.updateProjectTransaction(PSBList, AB,req.getSession(false));
            
            for(ActivityTransactionBean PTB : PTBList1){
                PTB.setTransid(DBATB.get(i).getTransid());
                //if(DBATB.get(i).getStatus().equalsIgnoreCase("completed")){
                //    Date date = DBATB.get(i).getTaskstartdate();
                //    Calendar cal = Calendar.getInstance();
                //    cal.setTime(date);
                //   PTB.setTaskstartdate(cal);
                //   PTB.setTaskenddate(DBATB.get(i).getTaskenddate());
                //   PTB.setEnddate(DBATB.get(i).getEnddate());
                //   PTB.setStartdate(DBATB.get(i).getStartdate());
                //   PTB.setStatus(DBATB.get(i).getStatus());
                //}
                i++;
                }
            
            //update the activityTansactionBeans to database
            
            
            AS.updateProjectTran(PTBList1);
            
                List<AllocationBean> AloBList = AS.getEngAllocationForActivity(AB.getActivityid());
                for(AllocationBean AloB : AloBList){
                    if(AloB.getActivityId() == AB.getActivityid()){
                    AloB.setAllocationStartdate(AB.getStartdate());
                    AloB.setAllocationEndenddate(AB.getEnddate());
                    AloB.setStatus("Allocated");
                    AloB.setEngineerId(AB.getEngtracker());
                    AS.updateResourceAllocation(AloB);
                    }
                }
            }
        
        List<ActivityTransactionBean> ActTB = AS.getProjectTransaction(AB.getActivityid());
        String actEng=ActTB.get(0).getEngname();
        ModelAndView model=new ModelAndView("DisplayActivityProgress");
        model.addObject("ProjectData",AB);
        model.addObject("TaskDetails",ActTB);
        model.addObject("Engineer",actEng);
        model.addObject("Message","Activity details are updated successfully.");
        return model;
        }
        catch(Exception ex){
        System.out.println("Exception occured. "+ex.getMessage());
        return new ModelAndView("Error");
        }
    }
    
    
    
}
