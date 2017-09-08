package com.cloudcomputing.assignment2;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReportDate implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public String DATE;
	
	public ReportDate(String DATE) {
		super();
		this.DATE = DATE;
	}

	public ReportDate()
	{
	super();
	}
	
}
