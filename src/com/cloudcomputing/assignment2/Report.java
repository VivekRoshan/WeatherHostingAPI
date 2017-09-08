package com.cloudcomputing.assignment2;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Report implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@XmlElement
	public String DATE;
	@XmlElement
	public double TMAX;
	@XmlElement
	public double TMIN;
	
/*	public int getdATE() {
		return dATE;
	}

	public void setdATE(int dATE) {
		this.dATE = dATE;
	}

	public double gettMAX() {
		return tMAX;
	}

	public void settMAX(double tMAX) {
		this.tMAX = tMAX;
	}

	public double gettMMIN() {
		return tMIN;
	}

	public void settMMIN(double tMIN) {
		this.tMIN = tMIN;
	}
*/
	public Report(String dATE, Double tMAX, Double tMIN) {
		super();
		this.DATE = dATE;
		this.TMAX = tMAX;
		this.TMIN = tMIN;
	}

	public Report()
	{
	super();
	}
	
}
