package com.qingxinmei;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

/**
 * @author zengzhiqiang
 * @data 2020-05-15 18:47:52 周五
 */

import org.junit.jupiter.api.Test;

import com.qingxinmei.util.RegExpStringUtils;

public class DataSourceTest {

	static int[] columnAttr = new int[] { 4, 5, 6, 7, 9, 16, 17, 18 };
	static String packages = "package com.qingxinmei.entity;";

	@Test
	public void test01() throws SQLException, IOException {
		DatabaseMetaData metaData = conn.getMetaData();
		ResultSet resultSet = metaData.getTables(null, "ZFZJ_FUXIN", "%", new String[] { "TABLE" });
		List<String> tableNames = new ArrayList<String>();
		while (resultSet.next()) {
			tableNames.add(resultSet.getString("TABLE_NAME"));
		}
		for (int i = 0; i < tableNames.size(); i++) {
			ResultSet columns = metaData.getColumns(null, "ZFZJ_FUXIN", tableNames.get(i), null);
			List<TableStructure> list = new ArrayList<>();
			while (columns.next()) {
				list.add(new TableStructure().setColumnName(columns.getString(4)).setDbType(columns.getString(6))
						.setColumnSize(columns.getInt(7)).setDecimalDigits(columns.getInt(9)));
			}
			final Set<String> imports = new HashSet<String>();
			StringBuffer sb = new StringBuffer();

			list.forEach(t -> {
				sb.append("\t").append(t.generate()).append("\r\n");
				if (t.getNeedImportPackage()) {
					System.out.println(t.getJavaType().getName());
					imports.add(t.getJavaType().getName());
				}
			});
			String aa = "";
			for (String str : imports) {
				aa += str + ";\r\n";
			}
			String clzName = RegExpStringUtils
					.firstWordToUp(RegExpStringUtils.underlineToHump(tableNames.get(i).toLowerCase()));
			FileUtils.writeStringToFile(new File("D:/java/" + clzName + ".java"),
					packages + "\r\n" + aa + "\r\npublic class " + clzName + " {\r\n" + sb.toString() + "}",
					StandardCharsets.UTF_8);
		}
	}

	static Connection conn;
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.8:1521:orcl", "zfzj_fuxin", "1");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
