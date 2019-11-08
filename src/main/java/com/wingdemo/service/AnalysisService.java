package com.wingdemo.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wingdemo.model.Booking;
import com.wingdemo.model.DelayRecord;
import com.wingdemo.model.PO;
import com.wingdemo.model.Record;
import com.wingdemo.model.Result;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author I501061
 *
 */

@Service
public class AnalysisService {
	private static final String FILE_NAME = "C:/Users/I501061/git/WING/wingdemo/Result.xlsx";

	/**
	 * Convert booking into data model
	 *
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public Map<String, Map<String, Booking>> convertDataToBookings(List<List<Object>> list) throws Exception {
		Map<String, Map<String, Booking>> POMap = new HashMap<String, Map<String, Booking>>();
		for (List<Object> cell : list) {
			String PO = cell.get(1).toString();
			String BookedDC = cell.get(0).toString();
			Record record = new Record(cell.get(2).toString(), cell.get(3).toString(),
					((Double) cell.get(4)).intValue(), (Date) cell.get(5));
			if (!POMap.containsKey(PO)) {
				POMap.put(PO, new HashMap<String, Booking>());
			}
			Map<String, Booking> PlanMap = POMap.get(PO);
			if (!PlanMap.containsKey(BookedDC)) {
				Booking booking = new Booking();
				booking.setBookedDC(BookedDC);
				booking.setPurchaseOrder(PO);
				PlanMap.put(BookedDC, booking);
			}
			Booking booking = PlanMap.get(BookedDC);
			booking.addRecord(record);
		}
		return POMap;
	}

	/**
	 * Convert Purchase Order into data model
	 *
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public Map<String, Map<String, PO>> convertDataToPOs(List<List<Object>> list) throws Exception {
		Map<String, Map<String, PO>> POMap = new HashMap<String, Map<String, PO>>();
		for (List<Object> cell : list) {
			String sourceERP = cell.get(0).toString();
			String vendor = cell.get(1).toString();
			String plant = cell.get(2).toString();
			String po = cell.get(3).toString();
			Record record = new Record(cell.get(4).toString(), cell.get(5).toString(),
					((Double) cell.get(8)).intValue(), (Date) cell.get(7));
			if (!POMap.containsKey(po)) {
				POMap.put(po, new HashMap<String, PO>());
			}
			Map<String, PO> PlanMap = POMap.get(po);
			if (!PlanMap.containsKey(plant)) {
				PO purcahseOrder = new PO();
				purcahseOrder.setPlant(plant);
				purcahseOrder.setPurchaseOrder(po);
				purcahseOrder.setSourceERP(sourceERP);
				purcahseOrder.setVendor(vendor);
				PlanMap.put(plant, purcahseOrder);
			}
			PO purcahseOrder = PlanMap.get(plant);
			purcahseOrder.addRecord(record);
		}
		return POMap;
	}

	/**
	 * Convert Purchase Order into data model
	 *
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public List<Result> compareBookingWithPO(Map<String, Map<String, PO>> poMap,
			Map<String, Map<String, Booking>> booksMap) throws Exception {
		//Create the new workbook and insert the first line
		List<Result> results = new ArrayList<Result>();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet delay_sheet = workbook.createSheet("delay");
		XSSFSheet notbooked_sheet = workbook.createSheet("notbooked");
		XSSFSheet normal_sheet = workbook.createSheet("normal");
		int delay_rowNum = 1, notbooked_rowNum = 1,normal_rowNum=1;
		prepareWorkSheet(delay_sheet);
		prepareWorkSheet(notbooked_sheet);
		prepareWorkSheet(normal_sheet);
		//Loop for traversing all the Purchase Order
		for (Iterator<Map.Entry<String, Map<String, PO>>> entries = poMap.entrySet().iterator(); entries.hasNext();) {
			Map.Entry<String, Map<String, PO>> entry = entries.next();
			for (Iterator<Map.Entry<String, PO>> poEntries = entry.getValue().entrySet().iterator(); poEntries
					.hasNext();) {
				Map.Entry<String, PO> poEntry = poEntries.next();
				PO po = poEntry.getValue();
				for (Record record : po.getRecords()) {
					//Initialize an empty match Result for an Purchase Order record
					Result result = new Result(po.getPurchaseOrder(), po.getPlant(),po.getSourceERP(), po.getVendor(),
							record.getLine(), record.getItem(), record.getItemQty(), record.getPlanDate());
					if (booksMap.containsKey(entry.getKey())) {
						if (booksMap.get(entry.getKey()).containsKey(poEntry.getKey())) {
							// Find out the booking with same PO and Plant
							Booking booking = booksMap.get(entry.getKey()).get(poEntry.getKey());
							
							List<Record> bksRecords = booking.getRecords().get(record.getItem());
							List<Record> restRecords = new ArrayList<Record>();
							int actualQty = result.getonTimeItemQty();
							if (bksRecords != null && (!bksRecords.isEmpty())) {
								for (Record bk : bksRecords) {
									if (actualQty == result.getItemQty()) {
										restRecords.add(bk);
									} else {
										long days = ChronoUnit.DAYS.between(bk.getPlanDate().toInstant(),
												record.getPlanDate().toInstant());
										actualQty = actualQty + bk.getItemQty(); 	 
										int rest = actualQty - result.getItemQty();	 
										if (days >= 0) {
											if (rest > 0) {
												bk.setItemQty(rest);
												result.setonTimeItemQty(result.getItemQty());
												result.setBookedQte(result.getItemQty());
												restRecords.add(bk);
											} else if (rest == 0) {
												result.setonTimeItemQty(result.getItemQty());
												result.setBookedQte(result.getItemQty());
											} else {
												result.setonTimeItemQty(actualQty);
												result.setBookedQte(result.getItemQty());
											}
										} else {
											if (rest > 0) {
												DelayRecord dealy = new DelayRecord(days, (bk.getItemQty() - rest),  
														bk.getPlanDate());
												result.addRecord(dealy);
												result.setBookedQte(result.getItemQty());
												bk.setItemQty(rest);
												restRecords.add(bk);
											} else if (rest == 0) {
												DelayRecord dealy = new DelayRecord(days, (bk.getItemQty()),
														bk.getPlanDate());
												result.addRecord(dealy);
												result.setBookedQte(result.getItemQty());
											} else {
												DelayRecord dealy = new DelayRecord(days, (bk.getItemQty()),
														bk.getPlanDate());
												result.addRecord(dealy);
												result.setBookedQte(actualQty);
											}
										}
									}
								}
								booking.getRecords().replace(record.getItem(), restRecords);
							}

						}
					}
					if (!result.getRecords().isEmpty()) {
						delay_rowNum=writeResultToExcel(delay_sheet, result, delay_rowNum);
					}else if (result.getBookedQte() < result.getItemQty()) {
						notbooked_rowNum=writeResultToExcel(notbooked_sheet, result, notbooked_rowNum);
					}else {
						normal_rowNum=writeResultToExcel(normal_sheet, result, normal_rowNum);
					}
					results.add(result);
				}
			}
		}

		try {
			FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
			workbook.write(outputStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return results;
	}
	private void prepareWorkSheet(XSSFSheet sheet) throws Exception {
		Row row = sheet.createRow(0);
		int colNum =0;
		Cell cell = row.createCell(colNum++);
		cell.setCellValue("SourceERP");
		cell = row.createCell(colNum++);
		cell.setCellValue("Vendor");
		cell = row.createCell(colNum++);
		cell.setCellValue("PurchaseOrder");
		cell = row.createCell(colNum++);
		cell.setCellValue("Plant");
		cell = row.createCell(colNum++);
		cell.setCellValue("Item");
		cell = row.createCell(colNum++);
		cell.setCellValue("Line");
		cell = row.createCell(colNum++);
		cell.setCellValue("PlanDate");
		cell = row.createCell(colNum++);
		cell.setCellValue("ItemQty");
		cell = row.createCell(colNum++);
		cell.setCellValue("onTimeItemQty");
		cell = row.createCell(colNum++);
		cell.setCellValue("bookedQty");
		cell = row.createCell(colNum++);
		cell.setCellValue("DelayDays");
		cell = row.createCell(colNum++);
		cell.setCellValue("DelayQte");
		cell = row.createCell(colNum++);
		cell.setCellValue("ShipDate");
	}
	private int writeResultToExcel(XSSFSheet sheet, Result result, int rowNum) throws Exception {
		if (result.getRecords().isEmpty()) {
			Row row = sheet.createRow(rowNum++);
			int colNum =0;
			Cell cell = row.createCell(colNum++);
			cell.setCellValue(result.getSourceERP());
			cell = row.createCell(colNum++);
			cell.setCellValue(result.getVendor());
			cell = row.createCell(colNum++);
			cell.setCellValue(result.getPurchaseOrder());
			cell = row.createCell(colNum++);
			cell.setCellValue(result.getBookedDC());
			cell = row.createCell(colNum++);
			cell.setCellValue(result.getItem());
			cell = row.createCell(colNum++);
			cell.setCellValue(result.getLine());
			cell = row.createCell(colNum++);
			cell.setCellValue(result.getPlanDate());
			cell = row.createCell(colNum++);
			cell.setCellValue(result.getItemQty());
			cell = row.createCell(colNum++);
			cell.setCellValue(result.getonTimeItemQty());
			cell = row.createCell(colNum++);
			cell.setCellValue(result.getBookedQte());
			
		}else {
		for (DelayRecord record : result.getRecords()) {
			Row row = sheet.createRow(rowNum++);
			int colNum =0;
			Cell cell = row.createCell(colNum++);
			cell.setCellValue(result.getSourceERP());
			cell = row.createCell(colNum++);
			cell.setCellValue(result.getVendor());
			cell = row.createCell(colNum++);
			cell.setCellValue(result.getPurchaseOrder());
			cell = row.createCell(colNum++);
			cell.setCellValue(result.getBookedDC());
			cell = row.createCell(colNum++);
			cell.setCellValue(result.getItem());
			cell = row.createCell(colNum++);
			cell.setCellValue(result.getLine());
			cell = row.createCell(colNum++);
			cell.setCellValue(result.getPlanDate());
			cell = row.createCell(colNum++);
			cell.setCellValue(result.getItemQty());
			cell = row.createCell(colNum++);
			cell.setCellValue(result.getonTimeItemQty());
			cell = row.createCell(colNum++);
			cell.setCellValue(result.getBookedQte());
			cell = row.createCell(colNum++);
			cell.setCellValue(record.getDelayDays());
			cell = row.createCell(colNum++);
			cell.setCellValue(record.getDelayQte());
			cell = row.createCell(colNum++);
			cell.setCellValue(record.getShipDate());
		}
		}
		return rowNum;
	}
}
