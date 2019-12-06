/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Paladion.teamwork.beans;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "vehicle",catalog="teamwork")
public class VehicleBean implements Serializable{
	
@Id
@GenericGenerator(name="gen",strategy="increment")
@GeneratedValue(generator="gen")
@Column(name = "vehicleid", unique = true, nullable = false, precision = 15, scale = 0)
int vehicleid;	 	
	
@Column(name = "regnumber")
String regnumber;

@Column(name = "model")
String model;
	
@Column(name = "price")
int price;

    public int getVehicleid() {
        return vehicleid;
    }
    public void setVehicleid(int vehicleid) {
        this.vehicleid = vehicleid;
    }
    public String getRegnumber() {
        return regnumber;
    }
    public void setRegnumber(String regnumber) {
        this.regnumber = regnumber;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
