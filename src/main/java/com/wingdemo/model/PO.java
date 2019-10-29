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
public class PO {
	private String Plant;
	private String PurchaseOrder;
	private String SourceERP;
	private String Vendor;
	private List<Record> Records = new ArrayList<Record>();

	public String getPlant() {
		return Plant;
	}

	public void setPlant(String plant) {
		Plant = plant;
	}

	public String getSourceERP() {
		return SourceERP;
	}

	public void setSourceERP(String sourceERP) {
		SourceERP = sourceERP;
	}

	public String getVendor() {
		return Vendor;
	}

	public void setVendor(String vendor) {
		Vendor = vendor;
	}

	public String getPurchaseOrder() {
		return PurchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		PurchaseOrder = purchaseOrder;
	}

	public void addRecord(Record record) {

		Records.add(record);
	}

	public List<Record> getRecords() {
		return Records;
	}

}
