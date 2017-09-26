/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Paladion.teamwork.services;

import com.Paladion.teamwork.DAO.TemplateDAO;
import com.Paladion.teamwork.beans.MapTemplateTaskBean;
import com.Paladion.teamwork.beans.TaskBean;
import com.Paladion.teamwork.beans.TemplateBean;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author user
 */
public class TemplateServiceImpl implements TemplateService{
	
@Autowired
@Qualifier(value="TemplateDAO")
 TemplateDAO TempD;

	@Override
	public void addTemplate(TemplateBean tempb) {
		TempD.addTemplateDao(tempb);
		}

	@Override
	public void editTemplate(TemplateBean tempb) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void deleteTemplate(TemplateBean tempb) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean addTaskToTemplate(MapTemplateTaskBean MTT) {
		
            return TempD.addTaskToTemplate(MTT);
		
	}

    @Override
    public List<TaskBean> getAllTasksforTemplate() {
    return TempD.getAllTasksforTemplate();
    }

    
    
    @Override
    public List<TemplateBean>getAllTemplates(){
        
        return TempD.getAllTemplates();
    }
    
    
    @Override
    public boolean deleteTemplate(int id){
        
        return TempD.deleteTemplate(id);
    }
    
	
    	@Override
	public List <MapTemplateTaskBean> getAllWeights(int tempID) {
	
	return TempD.getAllWeights(tempID);
        }
}
