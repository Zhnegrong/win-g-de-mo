/**
 * 
 */
package com.wingdemo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author I501061
 *
 */
public class Booking {
	private String BookedDC;
	private String PurchaseOrder;

	private Map<String, List<Record>> Records = new HashMap<String, List<Record>>();

	public String getBookedDC() {
		return BookedDC;
	}

	public void setBookedDC(String bookedDC) {
		BookedDC = bookedDC;
	}

	public String getPurchaseOrder() {
		return PurchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		PurchaseOrder = purchaseOrder;
	}

	public void addRecord(Record record) {
		if (!Records.containsKey(record.getItem())) {
			Records.put(record.getItem(), new ArrayList<Record>());
		}
		Records.get(record.getItem()).add(record);
	}

	public Map<String, List<Record>> getRecords() {
		return Records;
	}

}
