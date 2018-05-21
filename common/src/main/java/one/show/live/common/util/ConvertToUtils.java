package one.show.live.common.util;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

/**
 * 转换的工具类
 * @author samuel
 *
 */
public class ConvertToUtils {

	private static final String EMPTY_STRING = "";

	/**
	 * 非空判断-项目无使用,暂留
	 * @param str
	 * @return
	 */
	public static String toString(String str) {
		if (StringUtils.isNullOrEmpty(str)) {
			return EMPTY_STRING;
		} else {
			return str;
		}
	}

	/**
	 * 非空判断
	 * @param o
	 * @return
	 */
	public static String toString(Object o) {
		if (StringUtils.isNullOrEmpty(o)) {
			return EMPTY_STRING;
		} else {
			return o.toString();
		}
	}

	/**
	 * 转换字符串为int
	 * 
	 * @param str
	 * @return
	 */
	public static int toInt(String str) {
		return toInt(str, 0);
	}

	/**
	 * 转换字符串为int
	 * 
	 * @param str
	 * @param def
	 *            默认值
	 * @return
	 */
	public static int toInt(String str, int def) {
		if (StringUtils.isNullOrEmpty(str)) {
			return def;
		}
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return def;
		}
	}

	/**
	 * 转换字符串为boolean
	 * 
	 * @param str
	 * @return
	 */
	public static boolean toBoolean(String str) {
		return toBoolean(str, false);
	}

	/**
	 * 转换字符串为boolean
	 * 
	 * @param str
	 * @param def
	 * @return
	 */
	public static boolean toBoolean(String str, boolean def) {
		if (StringUtils.isNullOrEmpty(str)) {
			return def;
		}
		if ("false".equalsIgnoreCase(str) || "0".equals(str)) {
			return false;
		} else if ("true".equalsIgnoreCase(str) || "1".equals(str)) {
			return true;
		} else {
			return def;
		}
	}

	/**
	 * 转换字符串为float
	 * 
	 * @param str
	 * @return
	 */
	public static float toFloat(String str) {
		return toFloat(str, 0F);
	}

	/**
	 * 转换字符串为float
	 * 
	 * @param str
	 * @param def
	 * @return
	 */
	public static float toFloat(String str, float def) {
		if (StringUtils.isNullOrEmpty(str)) {
			return def;
		}
		try {
			return Float.parseFloat(str);
		} catch (NumberFormatException e) {
			return def;
		}
	}

	/**
	 * 转换字符串为long
	 * 
	 * @param str
	 * @return
	 */
	public static long toLong(String str) {
		return toLong(str, 0L);
	}

	/**
	 * 转换字符串为long
	 * 
	 * @param str
	 * @param def
	 * @return
	 */
	public static long toLong(String str, long def) {
		if (StringUtils.isNullOrEmpty(str)) {
			return def;
		}
		try {
			return Long.parseLong(str);
		} catch (NumberFormatException e) {
			return def;
		}
	}

	/**
	 * 转换字符串为short
	 * 
	 * @param str
	 * @return
	 */
	public static short toShort(String str) {
		return toShort(str, (short) 0);
	}

	/**
	 * 转换字符串为short
	 * 
	 * @param str
	 * @param def
	 * @return
	 */
	public static short toShort(String str, short def) {
		if (StringUtils.isNullOrEmpty(str)) {
			return def;
		}
		try {
			return Short.parseShort(str);
		} catch (NumberFormatException e) {
			return def;
		}
	}

	/** 颜色转化 */
	public static int toColor(String str, int def) {
		if (StringUtils.isNullOrEmpty(str)) {
			return def;
		}
		try {
			return Color.parseColor(str);
		} catch (Exception e) {
			return def;
		}
	}

	/**
	 * px = dp * (dpi / 160)
	 * @param ctx
	 * @param dip
	 * @return
	 */
	public static int dipToPX(final Context ctx, float dip) {
		int result = 0;
		if (ctx == null || dip <= 0)
			return 0;
		try {
			result = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, ctx.getResources().getDisplayMetrics());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * sp*ppi/160 =px
	 * 
	 * @param ctx
	 * @return
	 */
	public static int spToPX(final Context ctx, float sp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, ctx.getResources().getDisplayMetrics());
	}
}
