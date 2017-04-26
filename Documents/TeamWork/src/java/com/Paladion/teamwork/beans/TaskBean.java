/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Paladion.teamwork.beans;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "Tasks",catalog="teamwork")
public class TaskBean implements Serializable{
	
	
@Id
	String taskid;
	
	String taskname, Description;

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public void setDescription(String Description) {
		this.Description = Description;
	}

	public String getTaskid() {
		return taskid;
	}

	public String getTaskname() {
		return taskname;
	}

	public String getDescription() {
		return Description;
	}
    
}
