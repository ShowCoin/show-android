package one.show.live.log;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.database.sqlite.SQLiteFullException;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import one.show.live.BuildConfig;

public class Logger {
	/**
	 * 程序是否Debug版本
	 */
	//	private static boolean IsDebug = false;
	public static final String TAG = "[beke]";

	//	public static void setLog(boolean isLog) {
	//		IsDebug = isLog;
	//	}

	public static boolean getIsDebug() {
		return BuildConfig.IS_DEBUG;
	}

	public static void printStackTrace(String TAG, Exception e) {
		if (getIsDebug()) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	public static void printStackTrace(String TAG, IOException e) {
		if (getIsDebug()) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}


	public static void printStackTrace(String TAG, MalformedURLException e) {
		if (getIsDebug()) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	/**
	 * 非法参数
	 * 
	 * @param e
	 */
	public static void printStackTrace(String TAG, IllegalArgumentException e) {
		if (getIsDebug()) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	public static void printStackTrace(String TAG, ActivityNotFoundException e) {
		if (getIsDebug()) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	public static void printStackTrace(String TAG, IndexOutOfBoundsException e) {
		if (getIsDebug()) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	/**
	 * 
	 * @param e
	 */
	public static void printStackTrace(String TAG, FileNotFoundException e) {
		if (getIsDebug()) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	// ~~~ 数据库相关

	public static void printStackTrace(String TAG, android.database.sqlite.SQLiteException e) {
		if (getIsDebug()) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	/**
	 * 数据库文件已达到最大空间(数据库已满)
	 * 
	 * @param e
	 */
	public static void printStackTrace(String TAG, SQLiteFullException e) {
		if (getIsDebug()) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	/**
	 * 未捕获的异常
	 * 
	 * @param TAG
	 * @param e
	 */
	public static void printStackTrace(String TAG, Throwable e) {
		if (getIsDebug()) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	/**
	 * 记录错误日志
	 * 
	 * @param TAG
	 * @param ex
	 */
	private static void logException(String TAG, Throwable ex) {

	}

	public static void d(String tag, String msg) {
		if (getIsDebug()) {
			Log.d(tag, msg);
		}
	}

	public static void d(String msg) {
		if (getIsDebug()) {
			Log.d(TAG, msg);
		}
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (getIsDebug()) {
			Log.d(tag, msg, tr);
		}
	}

	public static void e(Throwable tr) {
		if (getIsDebug()) {
			Log.e(TAG, "", tr);
		}
		//		else {
		//			new CrashUncaughtException().saveCrashInfoToFile("Logger.e(Throwable tr) ", Log.getStackTraceString(tr));	
		//		}

	}

	public static void w(Throwable tr) {
		if (getIsDebug()) {
			Log.w(TAG, "", tr);
		}
	}

	public static void i(String msg) {
		if (getIsDebug()) {
			Log.i(TAG, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (getIsDebug()) {
			Log.i(tag, msg);
		}
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (getIsDebug()) {
			Log.i(tag, msg, tr);
		}

	}

	public static void e(String tag, String msg) {
		if (getIsDebug()) {
			Log.e(tag, msg);
		}
	}

	public static void e(String msg) {
		if (getIsDebug()) {
			Log.e(TAG, msg);
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (getIsDebug()) {
			Log.e(tag, msg, tr);
		}
	}

	public static void e(String msg, Throwable tr) {
		if (getIsDebug()) {
			Log.e(TAG, msg, tr);
		}
	}

	public static void systemErr(String msg) {
		systemErr(null, msg);
	}

	public static void systemErr(Context context, String msg) {
		try {
			if (getIsDebug()) {
				if (msg != null) {
					Log.e(TAG + (context != null ? "[" + context + "]" : ""), msg);
				}

			}
		} catch (Exception e) {
		}

	}
}
