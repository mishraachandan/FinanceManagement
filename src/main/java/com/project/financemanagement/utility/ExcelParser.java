package com.project.financemanagement.utility;

import com.project.financemanagement.request.MyObject;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelParser {

    private static final Logger logger = LoggerFactory.getLogger(ExcelParser.class);

    private ExcelParser(){}
    public static List<MyObject> parseExcelFile(InputStream is) throws IOException, InvalidFormatException {
        List<MyObject> myObjects = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(is);

        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip header row
            }

            MyObject obj = new MyObject();
            obj.setField1(getCellValueAsString(row.getCell(0)));
            obj.setField2(getCellValueAsString(row.getCell(1)));
            obj.setField3(getCellValueAsString(row.getCell(2)));
            obj.setField4(getCellValueAsString(row.getCell(3)));
            obj.setField5(getCellValueAsString(row.getCell(4)));
            // Add more fields as needed

            myObjects.add(obj);
        }

        workbook.close();
        return myObjects;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getRichStringCellValue().getString().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString(); // Adjust date format if needed
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    private static Double getCellValueAsDouble(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getRichStringCellValue().getString().trim());
                } catch (NumberFormatException e) {
                    return null; // Handle if the string cannot be parsed as double
                }
            default:
                return null;
        }
    }
}
