package one.show.live.util;

import android.content.Context;
import android.os.Environment;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.io.File;
import java.math.BigDecimal;

/**
 * 清除缓存的
 * 
 * @author Administrator
 * 
 */
public class DataCleanManager {

	/**
	 * 获取图片缓存大小
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String getTotalCacheSize(Context context) throws Exception {
		long cacheSize = getFolderSize(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			cacheSize += getFolderSize(context.getExternalCacheDir());
		}
		return getFormatSize(cacheSize);
	}

	/**
	 * 清除图片缓存
	 * 
	 * @param context
	 */
	public static void clearAllCache(Context context) {
		deleteDir(context.getCacheDir());
		//判断是不是有内存卡
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			deleteDir(context.getExternalCacheDir());
		}
	}

//	private static boolean deleteDir(File dir) {
//		if (dir != null && dir.isDirectory()) {
//			String[] children = dir.list();
//			for (int i = 0; i < children.length; i++) {
//				boolean success = deleteDir(new File(dir, children[i]));
//				if (!success) {
//					return false;
//				}
//			}
//		}
//		return dir.delete();
//	}
	public static void deleteDir(File file) {
		if (file!=null &&file.exists())  {
			if (file.isDirectory())   {
				File[] files = file.listFiles();
				if(files==null){
					return;
				}
				for (File subFile : files)    {
					if (subFile.isDirectory())
						deleteDir(subFile);
					else
						subFile.delete();
				}
			}
			file.delete();
		}
	}

	// 获取文件
	// Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
	// 目录,一般放一些长时间保存的数据
	// Context.getExternalCacheDir() -->
	// SDCard/Android/data/你的应用包名/cache/目录,一般存放临时缓存数据
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * 格式化单位
	 * 
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			// return size + "Byte";
			return "0KB";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
	}

	/**
	 * 清除webview缓存
	 * 
	 * @return
	 */
	public static void clearWebViewCache(Context self) {
	// 清除cookie即可彻底清除缓存
	CookieSyncManager.createInstance(self);
	CookieManager.getInstance().removeAllCookie();
	}
}
