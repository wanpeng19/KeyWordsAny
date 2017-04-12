package javafx.scene.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class KeyWordsXlsReader {

	public static List<String> readConfigXlsx(File file, int sheetIndex) throws Exception {
		List<String> keys = new ArrayList<String>();
		InputStream is = new FileInputStream(file);
		try {

			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			// Read the Sheet
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheetIndex);

			if (xssfSheet == null)
				throw new Exception("sheet" + sheetIndex + "不存在");

			// Read the Row
			for (int rowNum = 0; rowNum <= 10000; rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);

				if (xssfRow != null) {
					XSSFCell cell1 = xssfRow.getCell(0);
					String value = getValue(cell1);
					keys.add(value.trim());
					if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			is.close();
		}
		return keys;
	}

	private static String getValue(XSSFCell xssfRow) {
		if (xssfRow == null)
			return "";
		if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfRow.getBooleanCellValue());
		} else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
			return String.valueOf((int) xssfRow.getNumericCellValue());
		} else {
			return String.valueOf(xssfRow.getStringCellValue());
		}
	}

}