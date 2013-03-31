/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.datafx.test;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author johan
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Books {
//	@XmlList
	private ArrayList<Book> entries = new ArrayList<Book>();

	/**
	 * @return the entries
	 */
	public ArrayList<Book> getEntries() {
		return entries;
	}

	/**
	 * @param entries the entries to set
	 */
	public void setEntries(ArrayList<Book> entries) {
		this.entries = entries;
	}
	
}
