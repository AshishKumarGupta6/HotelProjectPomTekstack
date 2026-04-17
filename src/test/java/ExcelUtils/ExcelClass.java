package ExcelUtils;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelClass {

    public static void setCellData(String xPath, String xSheet, int rowIndex, int cellIndex, String value) throws IOException {
        FileInputStream file = new FileInputStream(xPath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheet(xSheet);
        XSSFRow row = sheet.getRow(rowIndex);

        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        XSSFCell cell = row.getCell(cellIndex);
        if (cell == null) {
            cell = row.createCell(cellIndex);
        }
        cell.setCellValue(value);
        file.close();

        FileOutputStream fileOut = new FileOutputStream(xPath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    public static void setGreenColor(String xPath, String xSheet, int rowIndex, int cellIndex) throws IOException {
        FileInputStream file = new FileInputStream(xPath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheet(xSheet);
        XSSFRow row = sheet.getRow(rowIndex);
        XSSFCell cell = row.getCell(cellIndex);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(style);
        file.close();

        FileOutputStream fileOut = new FileOutputStream(xPath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    public static void setRedColor(String xPath, String xSheet, int rowIndex, int cellIndex) throws IOException {
        FileInputStream file = new FileInputStream(xPath);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheet(xSheet);
        XSSFRow row = sheet.getRow(rowIndex);
        XSSFCell cell = row.getCell(cellIndex);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.RED.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(style);
        file.close();

        FileOutputStream fileOut = new FileOutputStream(xPath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }
}
