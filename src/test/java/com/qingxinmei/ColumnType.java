package com.qingxinmei;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import com.qingxinmei.util.RegExpStringUtils;

/**
 * @author zengzhiqiang
 * @data 2020-05-15 19:53:32 周五
 */
public class ColumnType {

	private final Class<?> javaType;
	private final Boolean needImport;

	public static void generate(TableStructure ts) {
		String attrName = RegExpStringUtils.underlineToHump(ts.getColumnName().toLowerCase());
		ts.setAttrName(attrName);
		ColumnType columnType = null;
		if (ts.getDbType().equalsIgnoreCase(ColumnType.ColumnTypeKey.VARCHAR2.toString())) {
			columnType = ColumnType.getColumnType(ColumnType.ColumnTypeKey.VARCHAR2);
			ts.setJavaType(columnType.getJavaType());
			ts.setNeedImportPackage(columnType.needImport);

		}
		if (ts.getDbType().equalsIgnoreCase(ColumnType.ColumnTypeKey.DATE.toString())) {
			columnType = ColumnType.getColumnType(ColumnType.ColumnTypeKey.DATE);
			ts.setJavaType(columnType.getJavaType());
			ts.setNeedImportPackage(true);
		}
		if (ts.getDbType().equalsIgnoreCase(ColumnType.ColumnTypeKey.NUMBER.toString())) {
			columnType = null;
			if (ts.getDecimalDigits() == 0) {
				if (ts.getColumnSize() > 0 && ts.getColumnSize() < 10) {
					columnType = ColumnType.getColumnType(ColumnType.ColumnTypeKey.NUMBER);
				}
				if (ts.getColumnSize() >= 10 && ts.getColumnSize() < 20) {
					columnType = ColumnType.getColumnType(ColumnType.ColumnTypeKey.NUMBER2);
				}
				if (ts.getColumnSize() >= 20) {
					columnType = ColumnType.getColumnType(ColumnType.ColumnTypeKey.NUMBER3);
					ts.setNeedImportPackage(true);
				}
			} else {
				columnType = ColumnType.getColumnType(ColumnType.ColumnTypeKey.NUMBER3);
				ts.setNeedImportPackage(true);
			}
			ts.setJavaType(columnType.getJavaType());
		}
	}

	private ColumnType(Class<?> javaType, boolean needImport) {
		this.javaType = javaType;
		this.needImport = needImport;
	}

	public Class<?> getJavaType() {
		return javaType;
	}

	public boolean isNeedImport() {
		return needImport;
	}

	static final HashMap<String, ColumnType> ColumnTypes = new HashMap<>();
	static {
		ColumnTypes.put(ColumnTypeKey.VARCHAR2.toString(), new ColumnType(String.class, false));
		ColumnTypes.put(ColumnTypeKey.DATE.toString(), new ColumnType(Date.class, true));
		ColumnTypes.put(ColumnTypeKey.NUMBER.toString(), new ColumnType(Integer.class, false));
		ColumnTypes.put(ColumnTypeKey.NUMBER2.toString(), new ColumnType(Long.class, false));
		ColumnTypes.put(ColumnTypeKey.NUMBER3.toString(), new ColumnType(BigDecimal.class, true));
	}

	public static ColumnType getColumnType(ColumnTypeKey key) {
		return ColumnTypes.get(key.toString());
	}

	public static enum ColumnTypeKey {
		VARCHAR2, DATE, NUMBER, NUMBER2, NUMBER3;
	}
}
