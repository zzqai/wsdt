package com.qingxinmei.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zengzhiqiang
 * @data 2020-05-15 19:00:48 周五
 */
public final class RegExpStringUtils {

	private static final Pattern PATTERN_UNDERLINE = Pattern.compile("_(\\w)");
	private static final Pattern PATTERN_HUMP = Pattern.compile("[A-Z]");

	public static String firstWordToUp(String src) {
		if (src == null || src.length() < 1) {
			return null;
		}
		char[] charArray = src.toCharArray();
		if (charArray[0] > 'Z') {
			charArray[0] -= 32;
		}
		return new String(charArray);
	}

	/**
	 * - 驼峰转下划线
	 *
	 * @apiNote: createTime => create_time
	 * @param src
	 * @return
	 */
	public static String humpToUnderline(String src) {
		if (src == null) {
			return null;
		}
		Matcher matcher = PATTERN_HUMP.matcher(src);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "_" + matcher.group(0));
		}
		matcher.appendTail(sb);
		return sb.toString().toLowerCase();
	}

	/**
	 * - 下划线转驼峰
	 *
	 * @apiNote is_del => isDel
	 * @param src
	 * @return
	 */
	public static String underlineToHump(String src) {
		if (src == null || src.length() < 1 || src.replaceAll("_", "").length() < 1) {
			return null;
		}
		Matcher matcher = PATTERN_UNDERLINE.matcher(src);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
		}
		matcher.appendTail(sb);
		return sb.substring(0, 1).toLowerCase() + sb.substring(1);
	}

	private RegExpStringUtils() {
	}
}
