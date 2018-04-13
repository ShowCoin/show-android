package one.show.live.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


public class NetworkUtils {

	/**
	 * 是否为wifi链接
	 * @return
	 */
	public static boolean isWifiAvailable(Context ctx) {
		if (ctx != null) {
			ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
			return manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
		}
		return false;
	}

	/**
	 * 检测网络连接是否可用
	 * 
	 * @param ctx
	 * @return true 可用; false 不可用
	 */
	public synchronized static boolean isNetworkAvailable(Context ctx) {
		if (ctx != null) {
			ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm == null) {
				return false;
			}
			try {
				NetworkInfo[] netinfo = cm.getAllNetworkInfo();
				if (netinfo == null) {
					return false;
				}
				for (NetworkInfo element : netinfo) {
					if (element.isConnected()) {
						return true;
					}
				}
			} catch (Exception ex) {

			}
		}
		return false;
	}
	


	public static int getNetwork(Context context) {
		if (isWifiAvailable(context)) {
			return -1;
		} else {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (tm != null) {
				return tm.getNetworkType();
			}

		}

		return TelephonyManager.NETWORK_TYPE_UNKNOWN;
	}

	public static String getNetworkTypeName(Context context) {
		if (context != null) {
			ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectMgr != null) {
				NetworkInfo info = connectMgr.getActiveNetworkInfo();
				if (info != null) {
					switch (info.getType()) {
					case ConnectivityManager.TYPE_WIFI:
						return "WIFI";
					case ConnectivityManager.TYPE_MOBILE:
						return getNetworkTypeName(info.getSubtype());
					}
				}
			}
		}
		return getNetworkTypeName(TelephonyManager.NETWORK_TYPE_UNKNOWN);
	}

	public static String getNetworkTypeName(int type) {
		switch (type) {
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return "GPRS";
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return "EDGE";
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return "UMTS";
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return "HSDPA";
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return "HSUPA";
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return "HSPA";
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return "CDMA";
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return "CDMA - EvDo rev. 0";
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return "CDMA - EvDo rev. A";
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return "CDMA - EvDo rev. B";
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return "CDMA - 1xRTT";
		case TelephonyManager.NETWORK_TYPE_LTE:
			return "LTE";
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return "CDMA - eHRPD";
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return "iDEN";
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return "HSPA+";
		default:
			return "UNKNOWN";
		}
	}


	/**
	 * 将服务器返回的流转化成字符串
	 *
	 * @param inputStream 输入流
	 * @return 字符数组
	 * @throws IOException
	 */
	public byte[] readInputStream(InputStream inputStream) throws IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
			bos.flush();
		}
		bos.close();
		return bos.toByteArray();
	}
}
