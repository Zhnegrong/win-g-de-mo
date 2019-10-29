/**
 * 
 */
package com.wingdemo.model;

import java.util.Date;

/**
 * @author I501061
 *
 */
public class DelayRecord {
 

	private long DelayDays;
	private int DelayQte; 
	private Date ShipDate;

	public DelayRecord(long days,int qte,Date d) {
		this.DelayDays=days;
		this.DelayQte=qte;
		this.ShipDate=d; 
	}

	public long getDelayDays() {
		return DelayDays;
	}

	public void setDelayDays(long delayDays) {
		DelayDays = delayDays;
	}

	public int getDelayQte() {
		return DelayQte;
	}

	public void setDelayQte(int delayQte) {
		DelayQte = delayQte;
	}

	public Date getShipDate() {
		return ShipDate;
	}

	public void setShipDate(Date shipDate) {
		ShipDate = shipDate;
	}
	 
 
}
