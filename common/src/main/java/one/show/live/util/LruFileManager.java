package one.show.live.util;

import java.io.File;

import android.support.v4.util.LruCache;


public class LruFileManager extends LruCache<String, Long> {

	public static final int MAX_SIZE = 256 * 1024 * 1024;

	public LruFileManager() {
		super(MAX_SIZE);
	}

	public LruFileManager(int maxSize) {
		super(maxSize);
	}

	@Override
	protected int sizeOf(String key, Long value) {
		return value.intValue();
	}

	@Override
	protected void entryRemoved(boolean evicted, String key, Long oldValue, Long newValue) {
		if (evicted) {
			FileUtils.deleteFile(key);
			FileUtils.deleteDir(key);
		}

	}

	public void put(File file) {
		try{
		if (file != null && file.exists())
			put(file.getPath(), FileUtils.getFileLength(file));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/** 初始化 */
	public void initialize(File dir) {
		try{
		if (dir != null && dir.isDirectory() && dir.exists() && dir.canRead()) {
			File[] files = dir.listFiles();
			if (files != null) {
				for (File file : files) {
					put(file.getPath(), FileUtils.getFileLength(file));
				}
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//
	//	@Override
	//	protected int sizeOf(String key, File value) {
	//		return (int) value.length();
	//	}
	//	
	//	@Override
	//	protected void entryRemoved(boolean evicted, String key, File oldValue, File newValue) {
	//		//super.entryRemoved(evicted, key, oldValue, newValue);
	//		//将一个最不经常用的文件删除
	//		FileUtils.deleteDir(oldValue);
	//	}
}
