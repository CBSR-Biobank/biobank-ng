package edu.ualberta.med.biobank.services.catalogue;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import edu.ualberta.med.biobank.dtos.AliquotDTO;
import edu.ualberta.med.biobank.util.DateUtil;

record SheetConfig(String heading, Integer width) {
}

/**
 * Creates a spreadsheet containing the aliquots present in the inventory for a study.
 */
class StudyCatalogueWriter {

    private static final SheetConfig[] CONFIG = new SheetConfig[] {
        new SheetConfig("Patient #", 3500),
        new SheetConfig("Visit #", 2500),
        new SheetConfig("Inventory ID", 4000),
        new SheetConfig("Specimen Type", 7000),
        new SheetConfig("Time Drawn", 6500),
        new SheetConfig("Quantity", 4500),
        new SheetConfig("Center", 3000),
        new SheetConfig("Top Container", 3000)
    };

    private Collection<AliquotDTO> aliquots;

    private Workbook workbook;

    private Sheet sheet;

    private int currentRow = 0;

    private CellStyle bodyCellStyle;

    public StudyCatalogueWriter(Collection<AliquotDTO> aliquots) {
        this.aliquots = aliquots;
    }

    public void write(String filename) throws IOException {
        workbook = new SXSSFWorkbook(100);
        sheet = workbook.createSheet("Specimens");

        createHeaders();

        sheet.createFreezePane(0, 1);

        FileOutputStream outputStream = new FileOutputStream(filename);
        workbook.write(outputStream);
        workbook.close();
    }

    private void createHeaders() {
        var font = workbook.createFont();
        font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
        //font.setFontHeightInPoints((short)10);
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);

        var headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFont(font);

        bodyCellStyle = workbook.createCellStyle();
        bodyCellStyle.setWrapText(true);

        Row header = sheet.createRow(currentRow++);

        var index = 0;
        for (SheetConfig config : CONFIG) {
            sheet.setColumnWidth(index, config.width());

            Cell headerCell = header.createCell(index);
            headerCell.setCellValue(config.heading());
            headerCell.setCellStyle(headerStyle);

            index++;
        }

        for (AliquotDTO aliquot: aliquots) {
            createRow(aliquot);
        }
    }

    private void createRow(AliquotDTO aliquot) {
        var row = sheet.createRow(currentRow++);
        row.setHeight((short)-1);

        int cellColumn = 0;
        createCell(row, cellColumn, aliquot.pnumber());
        createCell(row, ++cellColumn, aliquot.visitNumber().toString());
        createCell(row, ++cellColumn, aliquot.inventoryId());
        createCell(row, ++cellColumn, aliquot.specimenType());

        var timeDrawn = aliquot.timeDrawn() != null ? DateUtil.dateToString(aliquot.timeDrawn()) : "";
        createCell(row, ++cellColumn, timeDrawn);

        var quantity = aliquot.quantity() != null ? aliquot.quantity().toPlainString() : "";
        createCell(row, ++cellColumn, quantity);

        createCell(row, ++cellColumn, aliquot.center());
        createCell(row, ++cellColumn, aliquot.topContainer());
    }

    private void createCell(Row row, int cellColumn, String value) {
        var cell = row.createCell(cellColumn);
        cell.setCellValue(value);
        cell.setCellStyle(bodyCellStyle);
    }
}
