/**
 * 
 */
package com.wingdemo.model;

import java.util.Date;

/**
 * @author I501061
 *
 */
public class Record {
 
	private String Line;
	private String Item;
	private int ItemQty;
	private Date PlanDate;
	public Record(String l,String i,int q,Date p) {
		this.Line=l;
		this.Item=i;
		this.ItemQty=q;
		this.PlanDate=p;
	}
	
	public String getLine() {
		return Line;
	}
	public void setLine(String line) {
		Line = line;
	} 
	public String getItem() {
		return Item;
	}
	public void setItem(String item) {
		Item = item;
	}
	public int getItemQty() {
		return ItemQty;
	}
	public void setItemQty(int itemQty) {
		ItemQty = itemQty;
	}
	public Date getPlanDate() {
		return PlanDate;
	}
	public void setPlanDate(Date planDate) {
		PlanDate = planDate;
	}

 
}
