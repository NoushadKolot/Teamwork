/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Paladion.teamwork.services;

import com.Paladion.teamwork.beans.TaskBean;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface TaskService {
	
	public void addTask(TaskBean tb);
    public void editTask(TaskBean tb);
    public void deleteTask(TaskBean tb);
    public List<TaskBean> getAllTasks();
    public boolean deleteTask(int id);
    
}
