package com.qingxinmei;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 表结构实体类
 * 
 * @author zengzhiqiang
 * @data 2020-05-15 19:35:15 周五
 */
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class TableStructure {
	private static final String columnDefinition = "private %s %s;";
	/** 列名称 */
	@Getter
	@Setter
	private String columnName;
	/** java.sql.Types中对应的类型 */
	@Getter
	@Setter
	private Integer javaSqlType;
	/** 数据库中的类型 */
	@Getter
	@Setter
	private String dbType;
	/** 字符串最大长度 或 数字类型的最大位数(包括小数部分) */
	@Getter
	@Setter
	private Integer columnSize;
	/** 小数位数 */
	@Getter
	@Setter
	private Integer decimalDigits;
	@Getter
	@Setter
	private Class<?> javaType;
	@Getter
	@Setter
	private String attrName;
	@Getter
	@Setter
	private Boolean needImportPackage = false;

	public String generate() {
		ColumnType.generate(this);
		return String.format(columnDefinition, javaType.getSimpleName(), attrName);
	}

}
