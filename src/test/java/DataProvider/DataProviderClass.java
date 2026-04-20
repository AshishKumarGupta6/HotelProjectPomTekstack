package DataProvider;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class DataProviderClass {

    public static Object[][] getHotelData(String excelPath) throws IOException {
        FileInputStream fis = new FileInputStream(excelPath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet("Data1");

        int lastRow = sheet.getLastRowNum();
        DataFormatter formatter = new DataFormatter();

        int validCount = 0;
        for (int i = 1; i <= lastRow; i++) {
            Row row = sheet.getRow(i);
            if (row != null && !formatter.formatCellValue(row.getCell(0)).trim().isEmpty()) {
                validCount++;
            }
        }

        Object[][] data = new Object[validCount][11];

        int index = 0;
        for (int i = 1; i <= lastRow; i++) {
            Row row = sheet.getRow(i);
            if (row == null || formatter.formatCellValue(row.getCell(0)).trim().isEmpty()) continue;

            for (int j = 0; j < 11; j++) {
                data[index][j] = formatter.formatCellValue(row.getCell(j));
            }
            index++;
        }

        fis.close();
        workbook.close();
        return data;
    }
}
