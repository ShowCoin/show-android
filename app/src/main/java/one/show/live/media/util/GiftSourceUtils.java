package one.show.live.media.util;

import java.io.File;

import android.content.Context;
import android.content.Intent;

import one.show.live.common.util.FileUtils;
import one.show.live.common.util.StringUtils;
import one.show.live.media.po.POGift;
import one.show.live.media.po.POIMGift;
import one.show.live.media.service.DownloaderService;

public class GiftSourceUtils {

	/**
	 * 检查礼物是否需要下载
	 * 
	 * @param context
	 * @param item
	 */
	public static boolean checkNeedDownloadGift(final Context context, final POIMGift item) {
		if (context != null && item != null) {
			File fileDirectory = FileUtils.getGiftDownloadDirectory(context);
			File giftFile = new File(fileDirectory,item.getGiftName());
			if(FileUtils.checkFile(giftFile)){
				return true;
			}else{
//				if(DownloaderService.downLoadUrls.contains(item.sourceUrl)){
//					DownloaderService.DownLoadBean bean = DownloaderService.downLoadUrls.get(item.sourceUrl);
//					if(bean==null||!bean.isDownloading()){
//						if(FileUtils.checkFile(giftFile)){
//							return true;
//						}else {
//							download(context, item.sourceUrl, item.getGiftName(), fileDirectory, ".zip");
//						}
//					}
//				}else{
//					download(context, item.sourceUrl, item.getGiftName(), fileDirectory, ".zip");
//				}
			}
		}
		return false;
	}

	/**
	 * 下载文件
	 * @param context
	 * @param url
	 * @param fileName
	 * @param fileDir
	 * @param fileNameEndType
     */
	private static void download(final Context context, final String url,final String fileName,final File fileDir,final String fileNameEndType) {
		if (context != null && StringUtils.isNotEmpty(url) && fileDir!=null &&StringUtils.isNotEmpty(fileName)) {

			if (fileDir != null && !fileDir.exists()) {
				fileDir.mkdirs();
			}

			String fileDirPath = fileDir.getAbsolutePath();

			DownloaderService.DownLoadBean  item = new DownloaderService.DownLoadBean(url,fileName,fileDirPath,fileNameEndType);
			item.status = DownloaderService.DownLoadBean.DOWNLOAD_STATUS_INIT;

			FileUtils.deleteFile(item.getFilePath());
			FileUtils.deleteFile(item.getUnzipFilePath());

			if (DownloaderService.downLoadUrls != null) {
				DownloaderService.downLoadUrls.put(item.url, item);
			}
			Intent intent = new Intent(context, DownloaderService.class);
			intent.putExtra("dotype", 0);
			intent.putExtra("url", url);
			intent.putExtra("fileName", fileName);
			intent.putExtra("fileDirPath", fileDirPath);
			intent.putExtra("fileNameEndType", fileNameEndType);
			context.startService(intent);
		}
	}

	public static void deleteDownload(Context context, String url) {
		Intent intent = new Intent(context, DownloaderService.class);
		intent.putExtra("dotype", 1);
		intent.putExtra("url", url);
		context.startService(intent);
	}

}
