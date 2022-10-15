package indi.wangsc.paperwork.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class XssfExcel implements Excel {
    private String filePath;
    private File excelFile;
    private XSSFWorkbook workbook;
    private int sheetAt;
    private Sheet sheet;

    public XssfExcel() {
    }

    public XssfExcel(String filePath) {
        this.filePath = filePath;
        this.excelFile = new File(filePath);
        this.sheetAt = 0;
        try {
            if (excelFile.exists()) {
                this.workbook = new XSSFWorkbook(new FileInputStream(excelFile));
            } else {
                this.workbook = new XSSFWorkbook();
                this.sheet = this.workbook.createSheet();
                //workbook.write(new FileOutputStream(excelFile));
                save();
            }
            this.sheet = workbook.getSheetAt(sheetAt);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public XssfExcel(String filePath, int sheetAt) {
        this(filePath);
        this.sheetAt = sheetAt;
    }

    @Override
    public void writeGrid(Grid grid) {
        Sheet sheet = this.workbook.getSheetAt(this.sheetAt);
        Row row = sheet.createRow(grid.getRowAt());
        Cell cell = row.createCell(grid.getColAt());
        cell.setCellValue(String.valueOf(grid.getData()));
        try {
            this.workbook.write(new FileOutputStream((excelFile)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        try {
            FileOutputStream out = new FileOutputStream(excelFile);
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * shift the range which from 'rowAt' to end-row 'n' times
     *
     * @param rowAt from specific row
     * @param n shift specific lines
     */
    public void shiftRows(int rowAt, int n) {
        int lastRowNum = sheet.getLastRowNum();
        if (sheet.getRow(rowAt) != null && lastRowNum > -1 && lastRowNum >= rowAt) {
            sheet.shiftRows(rowAt, lastRowNum, n);
            save();
        }
    }

    public void writeLine(Line line) {
        shiftRows(line.getRowAt(), 1);
        Row row = sheet.createRow(line.getRowAt());
        for (Map.Entry<Integer, Object> entry : line.getData().entrySet()) {
            row.createCell(entry.getKey()).setCellValue(String.valueOf(entry.getValue()));
        }
        save();
    }

    public void writeLines(int rowAt, List<Line> linesList) {
        int linesSize = linesList.size();
        shiftRows(rowAt, linesSize);
        for (int i = 0; i < linesSize; i++) {
            Row row=sheet.createRow(rowAt+i);
            for (Map.Entry<Integer, Object> entry : linesList.get(i).getData().entrySet()) {
                row.createCell(entry.getKey()).setCellValue(String.valueOf(entry.getValue()));
            }
        }
        save();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public File getExcelFile() {
        return excelFile;
    }

    public void setExcelFile(File excelFile) {
        this.excelFile = excelFile;
    }

    public XSSFWorkbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public int getSheetAt() {
        return sheetAt;
    }

    public void setSheetAt(int sheetAt) {
        this.sheetAt = sheetAt;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }
}