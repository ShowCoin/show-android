/**
 * @filename        MD5.java
 * @function        字符串Md5加密
 * @author          skyz <skyzhw@gmail.com>
 * @datetime        2011-09-03
 * @lastmodify      2011-09-03
 * @version         1.0
 * @copyright       zxtd.com.cn All rights reserved.
 */
package one.show.live.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	public MD5() {
	}

	public static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return (new StringBuilder()).append(hexDigits[d1])
				.append(hexDigits[d2]).toString();
	}

	/**
	 * md5加密
	 * @param origin 要加密的字符串
	 * @return
	 */
	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = origin;
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes()));
		} catch (NoSuchAlgorithmException ignored) {
		}
		return resultString;
	}

}