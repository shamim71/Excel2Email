package com.versacomllc.emailer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import javax.swing.filechooser.FileSystemView;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WorkSheetProcessor extends Observable implements Runnable {

	private final String excelFile;

	@Override
	public void run() {

		try {
			processFile();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public WorkSheetProcessor(String excelFile) {
		super();
		this.excelFile = excelFile;

	}

	private void processFile() throws IOException {
		FileInputStream file = new FileInputStream(new File(excelFile));

		// Create Workbook instance holding reference to .xlsx file
		XSSFWorkbook workbook = new XSSFWorkbook(file);

		// Get first sheet from the workbook
		XSSFSheet sheet = workbook.getSheetAt(0);

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();
		boolean skipFirstRow = true;
		int total = 0;
		while (rowIterator.hasNext()) {

			Row row = rowIterator.next();
			if (skipFirstRow) {
				skipFirstRow = false;
				continue;
			}

			int cellPos = 0;
			Cell cell = row.getCell(cellPos);
			if (cell == null) {

				setChanged();
				Result res = new Result(0, "Processing finished");
				notifyObservers(res);
				break;
			}
			final String firstName = cell.getStringCellValue();
			Cell lastNameCell = row.getCell(++cellPos);
			String lastName = null;
			if (lastNameCell != null) {
				lastName = lastNameCell.getStringCellValue();
			}

			Cell phoneCell = row.getCell(++cellPos);
			String phone = null;
			if (phoneCell != null) {
				phone = phoneCell.getStringCellValue();
			}

			Cell emailCell = row.getCell(++cellPos);
			String email = null;
			if (emailCell != null) {
				email = emailCell.getStringCellValue();
			} else {
				setChanged();
				Result res = new Result(1,
						"Email address is missing for employee: " + firstName
								+ " " + lastName);
				notifyObservers(res);
			}

			final String sent = row.getCell(++cellPos).getStringCellValue();
			boolean toSent = sent.equalsIgnoreCase("Y") ? true : false;

			Cell messageCell = row.getCell(++cellPos);
			String message = null;
			if (messageCell != null) {
				message = messageCell.getStringCellValue();
			} else {
				setChanged();
				Result res = new Result(1,
						"Message content is missing for employee: " + firstName
								+ " " + lastName);
				notifyObservers(res);
			}
			/** Read attachment location */
			Cell attachmentCell = row.getCell(++cellPos);
			List<String> files = new ArrayList<String>();
			if (attachmentCell != null) {
				String filesPath = attachmentCell.getStringCellValue();
				if (filesPath != null && !filesPath.isEmpty()) {
					String fList[] = filesPath.split(",");
					for (String s : fList) {
						/** Validate if the File is exist or not */
						File f = new File(s);
						//files.add(s);
						if (f.exists()) {
							files.add(s);
						} else {
							setChanged();
							Result res = new Result(1, "Invalid attachment, "
									+ s + " for employee : " + firstName + " "
									+ lastName);
							notifyObservers(res);
						}
					}
				}
			}
			Cell addressCell = row.getCell(++cellPos);
			String address = null;
			if (addressCell != null) {
				address = addressCell.getStringCellValue();
			} else {
				setChanged();
				Result res = new Result(1,
						"Address is missing for employee: " + firstName
								+ " " + lastName);
				notifyObservers(res);
			}
			EmployeeInfo info = new EmployeeInfo(firstName, lastName, phone,
					email, toSent, message, address);
			info.setFilePaths(files);

			if (info.isValid()) {
				total++;
				setChanged();
				notifyObservers(info);
			}
		}

		setChanged();
		Result res = new Result(2, String.valueOf(total));
		notifyObservers(res);
	}
}
