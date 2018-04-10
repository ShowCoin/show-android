package one.show.live.util;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class FileUtils {
    private static final String TAG = "FileUtils";


    public final static String GIFT_DOWNLOADCACHE = "/gift";//礼物资源文件夹

    public final static String FILE_DOWNLOAD_PATH = "/mnt/sdcard/Android/data/mobi.hifun.seeu/cache/download";//默认下载的文件地址

    public static File getGiftDownloadDirectory(Context context) {
        //获取SD卡下面的缓存文件夹
        File f = null;
        if (context != null) {
            f = new File(getBekeFileDataPath(context), GIFT_DOWNLOADCACHE);
        }
        return f;
    }


    public static File getBekeFileDataPath(Context context) {
        if (DeviceUtils.isZte()) {
            return createForZte(context);
        } else {
            return getFilesPath(context);
        }
    }


    public static File createForZte(Context context) {
        File f = new File("/mnt/sdcard-ext/");
        if (!f.exists()) {
            f = new File("/mnt/sdcard/");
        }
        if (!f.exists() && context != null) {
            f = context.getFilesDir();
        }
        return new File(f, "/Android/data/mobi.hifun.seeu/files");
    }


    private static File getFilesPath(Context context) {
        if (context != null) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                try {
                    return new File(context.getExternalFilesDir("").getPath());
                } catch (Exception e) {

                }

            } else {
                return context.getFilesDir();
            }
        }
        return new File("/mnt/sdcard/Android/data/mobi.hifun.seeu/files");
    }


    /**
     * 获取上级文件夹名称
     */
    public static String getParentDirName(String path) {
        if (StringUtils.isNotEmpty(path)) {
            File f = new File(path);
            if (f != null) {
                f = f.getParentFile();
                if (checkFile(f) && f.isDirectory())
                    return f.getName();
            }
        }
        return "";
    }

    /**
     * 检测文件是否可用
     */
    public static boolean checkFile(File f) {
        if (f != null && f.exists() && f.canRead() && (f.isDirectory() || (f.isFile() && f.length() > 0))) {
            return true;
        }
        return false;
    }

    /**
     * 检测文件是否可用
     */
    public static boolean checkFile(String path) {
        if (StringUtils.isNotEmpty(path)) {
            File f = new File(path);
            if (f != null && f.exists() && f.canRead() && (f.isDirectory() || (f.isFile() && f.length() > 0)))
                return true;
        }
        return false;
    }

    /**
     * 获取文件的长度
     */
    public static long getFileLength(File f) {
        if (f != null && f.canRead() && f.exists()) {
            if (f.isDirectory() && f.listFiles() != null) {
                long result = 0;
                for (File ff : f.listFiles()) {
                    if (ff != null && ff.isFile() && f.exists() && ff.canRead()) {
                        result += ff.length();
                    } else if (ff.isDirectory()) {
                        result += getFileLength(ff);
                    }
                }
                return result;
            } else if (f != null && f.isFile()) {
                return f.length();
            }
        }
        return 0;
    }

    /**
     * 拼接路径
     * concatPath("/mnt/sdcard", "/DCIM/Camera")  	=>		/mnt/sdcard/DCIM/Camera
     * concatPath("/mnt/sdcard", "DCIM/Camera")  	=>		/mnt/sdcard/DCIM/Camera
     * concatPath("/mnt/sdcard/", "/DCIM/Camera")  =>		/mnt/sdcard/DCIM/Camera
     */
    public static String concatPath(String... paths) {
        StringBuilder result = new StringBuilder();
        if (paths != null) {
            for (String path : paths) {
                if (path != null && path.length() > 0) {
                    int len = result.length();
                    boolean suffixSeparator = len > 0 && result.charAt(len - 1) == File.separatorChar;//后缀是否是'/'
                    boolean prefixSeparator = path.charAt(0) == File.separatorChar;//前缀是否是'/'
                    if (suffixSeparator && prefixSeparator) {
                        result.append(path.substring(1));
                    } else if (!suffixSeparator && !prefixSeparator) {//补前缀
                        result.append(File.separatorChar);
                        result.append(path);
                    } else {
                        result.append(path);
                    }
                }
            }
        }
        return result.toString();
    }

    public static StringBuilder readFile(String filePath, String charsetName) {
        return readFile(new File(filePath), charsetName);
    }

    public static StringBuilder readFile(File file) {
        return readFile(file, "utf-8");
    }

    /**
     * read file
     *
     * @param file
     * @param charsetName The name of a supported {@link java.nio.charset.Charset
     *                    </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static StringBuilder readFile(File file, String charsetName) {
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * 获取文件名
     *
     * @param url
     * @return
     */
    public static String getFileName(String url) {
        if (TextUtils.isEmpty(url))
            return "";
        int index = url.lastIndexOf('/');
        int end = url.lastIndexOf('?');
        if (end > 0)
            return index < 0 ? url : url.substring(index + 1, end);
        else
            return index < 0 ? url : url.substring(index + 1);
    }

    public static String getPath(String uri) {
        // Log.i("FileUtils#getPath(%s)", uri);
        if (StringUtils.isEmpty(uri))
            return null;
        if (uri.startsWith("file://") && uri.length() > 7)
            return Uri.decode(uri.substring(7));
        return Uri.decode(uri);
    }

    public static String getName(String uri) {
        String path = getPath(uri);
        if (path != null)
            return new File(path).getName();
        return null;
    }

    // Work out the filename extension. If there isn't one, we keep
    // it as the empty string ("").
    public static String getExtension(File file) {
        String extension = "";
        String filename = file.getName();
        int dotPos = filename.lastIndexOf(".");
        if (dotPos >= 0) {
            extension = filename.substring(dotPos);
        }
        return extension.toLowerCase();
    }

    public static String getExtension(String filename) {
        String extension = "";
        if (filename != null) {
            int dotPos = filename.lastIndexOf(".");
            if (dotPos >= 0) {
                extension = filename.substring(dotPos);
            }
        }
        return extension.toLowerCase();
    }

    /**
     * 文件拷贝
     *
     * @param from
     * @param to
     * @return
     */
    public static boolean fileCopy(String from, String to) {
        boolean result = false;

        int size = 1 * 1024;

        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(from);
            out = new FileOutputStream(to);
            byte[] buffer = new byte[size];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
        }
        return result;
    }

    public static boolean fileCopy(File from, File to) {
        boolean result = false;

        int size = 1 * 1024;

        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(from);
            out = new FileOutputStream(to);
            byte[] buffer = new byte[size];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
        }
        return result;
    }

    /**
     * 删除文件
     *
     * @param f
     * @return
     */
    public static boolean deleteFile(File f) {
        if (f != null && f.exists() && !f.isDirectory()) {
            return f.delete();
        }
        return false;
    }

    public static boolean deleteFile(String f) {
        if (f != null && f.length() > 0) {
            return deleteFile(new File(f));
        }
        return false;
    }

    /**
     * 删除文件夹
     *
     * @param f
     */

    public static void deleteDir(File f) {
        if (f != null && f.exists() && f.isDirectory()) {
            File[] tempFiles = f.listFiles();
            if (tempFiles != null) {
                for (File file : tempFiles) {
                    if (file != null && file.exists()) {
                        if (file.isDirectory()) {
                            deleteDir(file);
                        } else {
                            file.delete();
                        }

                    }
                }
            }
            f.delete();
        }
    }


    public static void deleteDir1(File f) {
        if (f != null && f.exists() && f.isDirectory()) {
            File[] tempFiles = f.listFiles();
            if (tempFiles != null) {
                for (File file : tempFiles) {
                    if (file != null && file.exists()) {
                        if (file.isDirectory()) {
                            deleteDir(file);
                        } else {
                            file.delete();
                        }

                    }
                }
            }
        }
    }

    public static void deleteDir(String f) {
        if (f != null && f.length() > 0) {
            deleteDir(new File(f));
        }
    }

    /**
     * 返回抽象路径名的规范路径名字符串。 项目无用到,暂留
     */
    public static String getCanonical(File f) {
        if (f == null)
            return null;

        try {
            return f.getCanonicalPath();
        } catch (IOException e) {
            return f.getAbsolutePath();
        }
    }

    /**
     * sd卡是否存在,暂留
     *
     * @return
     */
    public static boolean sdAvailable() {
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState()) || Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 外部存储是否可用 (存在且具有读写权限)
     */
    public static boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机内部空间大小
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();//Gets the Android data directory
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize(); //每个block 占字节数
        long totalBlocks = stat.getBlockCount(); //block总数
        return totalBlocks * blockSize;
    }

    /**
     * 获取手机外部可用空间大小
     *
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        if (isExternalStorageAvailable()) {
            File path = Environment.getExternalStorageDirectory();//获取SDCard根目录
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return -1;
        }
    }

    public static final double KB = 1024.0;
    public static final double MB = KB * KB;
    public static final double GB = KB * KB * KB;

    /**
     * 传入目录,返回目录可用空间单位 MB
     *
     * @param context
     * @param file
     * @return
     */
    public static double showFileAvailableForVideoImage(Context context, File file) {
        try {
            StatFs sf = new StatFs(file.getPath());// StatFs用于对系统的存储容量进行检测。报错是因为构建StatFs对象时使用了非法参数。
            long blockSize = sf.getBlockSize();
            // long blockCount = sf.getBlockCount();
            long availCount = sf.getAvailableBlocks();
            return availCount * blockSize / MB;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取视频存储目录<获取目录用之前应判断File是否为null> 主卡可用空间大于200m则使用主卡,否则尝试使用外卡大于200m
     *
     * @param context
     * @return
     */
    public static File getDiskDiretoryForVideo(Context context) {
        String[] str = getVolumePaths(context);
        if (str != null) {
            for (int i = 0; i < str.length; i++) {
                File file = new File(str[i]);
                if (file != null && file.exists() && file.canWrite()) {
                    if (Environment.getExternalStorageDirectory().getPath().equals(str[i])) {
                        if (FileUtils.showFileAvailableForVideoImage(context, file) >= Constants.AVAILABLE_SPACE) {
                            return new File(file + File.separator + Constants.VIDEO_DOWNLOADCACHE);
                        }
                    } else {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {//小于4.4
                            if (FileUtils.showFileAvailableForVideoImage(context, file) >= Constants.AVAILABLE_SPACE) {
                                return new File(file + File.separator + Constants.VIDEO_DOWNLOADCACHE);
                            }
                        }
                    }
                }
            }
        } else {//反射失败,使用系统方法
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                return new File(Environment.getExternalStorageDirectory() + File.separator + Constants.VIDEO_DOWNLOADCACHE);
            }
        }
        return null;
    }

    //	/**
    //	 * old 视频地址获取和计算大小的组合搭配有问题,暂留
    //	 * @param context
    //	 * @return
    //	 */
    //	public static double showFileAvailableForVideoRecord(Context context) {
    //		String cachePath = "";
    //		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
    //			try {
    //				cachePath = Environment.getExternalStorageDirectory().getPath();
    //			} catch (Exception ex) {
    //				Log.e("[ImageCache]", "[getDiskCacheDir]", ex);
    //				try {
    //					cachePath = context.getCacheDir().getPath();
    //				} catch (Exception e) {
    //					Log.e("[ImageCache]", "[getDiskCacheDir]", e);
    //				}
    //			}
    //		} else if (DeviceUtils.isZte()) {
    //			cachePath = "/mnt/sdcard/";
    //			File f = new File(cachePath);
    //			if (!f.exists()) {
    //				cachePath = "/mnt/sdcard-ext/";
    //				f = new File(cachePath);
    //				if (!f.exists()) {
    //					cachePath = context.getCacheDir().getPath();
    //				}
    //			}
    //		} else {
    //			cachePath = context.getCacheDir().getPath();
    //		}
    //		try {
    //			StatFs sf = new StatFs(cachePath);// StatFs用于对系统的存储容量进行检测。报错是因为构建StatFs对象时使用了非法参数。
    //			long blockSize = sf.getBlockSize();
    //			// long blockCount = sf.getBlockCount();
    //			long availCount = sf.getAvailableBlocks();
    //			return availCount * blockSize / MB;
    //		} catch (Exception e) {
    //			Logger.e(e);
    //		}
    //		return 0;
    //	}

    /**
     * 反射隐藏的getVolumePaths方法
     *
     * @param context
     * @return
     */
    public static String[] getVolumePaths(Context context) {
        Method mMethodGetPaths = null;
        StorageManager mStorageManager;
        if (context != null) {
            mStorageManager = (StorageManager) context.getSystemService(Activity.STORAGE_SERVICE);
            try {
                mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            String[] paths = null;
            try {
                paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return paths;
        }
        return null;
    }

    public static double showFileAvailable(Context context) {
        String cachePath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            try {
                cachePath = context.getExternalCacheDir().getPath();
            } catch (Exception ex) {
                Log.e("[ImageCache]", "[getDiskCacheDir]", ex);
                try {
                    cachePath = context.getCacheDir().getPath();
                } catch (Exception e) {
                    Log.e("[ImageCache]", "[getDiskCacheDir]", e);
                }
            }
        } else if (DeviceUtils.isZte()) {
            cachePath = "/mnt/sdcard-ext/";
            File f = new File(cachePath);
            if (!f.exists()) {
                cachePath = "/mnt/sdcard/";
                f = new File(cachePath);
                if (!f.exists()) {
                    cachePath = context.getCacheDir().getPath();
                }
            }
        } else {
            cachePath = context.getCacheDir().getPath();
        }


        try {
            StatFs sf = new StatFs(cachePath);// StatFs用于对系统的存储容量进行检测。报错是因为构建StatFs对象时使用了非法参数。
            long blockSize = sf.getBlockSize();
            // long blockCount = sf.getBlockCount();
            long availCount = sf.getAvailableBlocks();
            return availCount * blockSize / MB;
        } catch (Exception e) {
            e.printStackTrace();

        }

        return 0;
    }

    /**
     * 剩余空间 M
     *
     * @return
     */
    public static double showFileAvailable() {
        String path = Environment.getExternalStorageDirectory().getPath();
        if (DeviceUtils.isZte()) {
            if (!Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).exists()) {
                path = path.replace("/sdcard", "/sdcard-ext");
            }
        } else {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                return 0;
            }
        }
        try {
            StatFs sf = new StatFs(path);// StatFs用于对系统的存储容量进行检测。报错是因为构建StatFs对象时使用了非法参数。
            long blockSize = sf.getBlockSize();
            // long blockCount = sf.getBlockCount();
            long availCount = sf.getAvailableBlocks();
            return availCount * blockSize / MB;
        } catch (Exception e) {
            e.printStackTrace();
            //ZTE的设备，再尝试一下
            if (DeviceUtils.isZte()) {
                try {
                    StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
                    long blockSize = sf.getBlockSize();
                    // long blockCount = sf.getBlockCount();
                    long availCount = sf.getAvailableBlocks();
                    return availCount * blockSize / MB;
                } catch (Exception ex) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    /**
     * 获取文件夹下面文件的个数,项目无用到,暂留
     *
     * @param f
     * @return
     */
    public static long getDirectoryFileSize(File f) {
        if (f != null) {
            if (f.isDirectory()) {
                long result = 0;
                for (File file : f.listFiles()) {
                    result += file.length();
                }
                return result;
            } else {
                return f.length();
            }
        }
        return 0;
    }

    public static String getFileExtension(String filename) {
        String extension = "";
        if (filename != null) {
            int dotPos = filename.lastIndexOf(".");
            if (dotPos >= 0 && dotPos < filename.length() - 1) {
                extension = filename.substring(dotPos + 1);
            }
        }
        return extension.toLowerCase();
    }

    public static File getCacheDiskPath(Context context, String uniqueName) {
        String cachePath = "/mnt/sdcard/Android/data/mobi.hifun.seeu/cache";
        if (context != null) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//去掉isExternalStorageRemovable判断，待验证
                try {
                    cachePath = context.getExternalCacheDir().getPath();
                } catch (Exception ex) {
                    Log.e("cache", "[getDiskCacheDir]", ex);
                    try {
                        cachePath = context.getCacheDir().getPath();
                    } catch (Exception e) {
                        Log.e("cache", "[getDiskCacheDir]", e);
                    }
                }
            } else {
                try {
                    cachePath = context.getCacheDir().getPath();
                } catch (Exception ex) {
                    Log.e("cache", "[getDiskCacheDir]", ex);
                }
            }
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public static File getCacheDiskPathAll(Context context) {
        String cachePath = "/mnt/sdcard/Android/data/mobi.hifun.seeu/cache";
        if (context != null) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//去掉isExternalStorageRemovable判断，待验证
                try {
                    cachePath = context.getExternalCacheDir().getPath();
                } catch (Exception ex) {
                    Log.e("cache", "[getDiskCacheDir]", ex);
                    try {
                        cachePath = context.getCacheDir().getPath();
                    } catch (Exception e) {
                        Log.e("cache", "[getDiskCacheDir]", e);
                    }
                }
            } else {
                try {
                    cachePath = context.getCacheDir().getPath();
                } catch (Exception ex) {
                    Log.e("cache", "[getDiskCacheDir]", ex);
                }
            }
        }
        return new File(cachePath);
    }

    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }

        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    //	private boolean combineFiles(String yuv, String pcm) throws IOException {
    //		//合并视频文件
    //		FileOutputStream mVideoFile = new FileOutputStream(yuv);
    //		FileOutputStream mAudioFile = new FileOutputStream(pcm);
    //
    //		try {
    //
    //			FileChannel mVideoChannel = mVideoFile.getChannel();
    //			FileChannel mAudioChannel = mAudioFile.getChannel();
    //
    //			for (MediaPart part : mMediaObject.mediaList) {
    //				FileInputStream mediaStream = new FileInputStream(part.mediaPath);
    //				FileChannel inVideoFileChannel = mediaStream.getChannel();
    //				inVideoFileChannel.transferTo(0, inVideoFileChannel.size(), mVideoChannel);
    //				inVideoFileChannel.close();
    //				mediaStream.close();
    //
    //				FileInputStream audioStream = new FileInputStream(part.audioPath);
    //				FileChannel inAudioFileChannel = audioStream.getChannel();
    //				inAudioFileChannel.transferTo(0, inAudioFileChannel.size(), mAudioChannel);
    //				inAudioFileChannel.close();
    //				audioStream.close();
    //			}
    //
    //			mVideoChannel.close();
    //			mAudioChannel.close();
    //
    //			return true;
    //		} catch (IOException e) {
    //			throw e;
    //		} finally {
    //			try {
    //				if (mVideoFile != null)
    //					mVideoFile.close();
    //			} catch (Exception e) {
    //
    //			}
    //			try {
    //				if (mAudioFile != null)
    //					mAudioFile.close();
    //			} catch (Exception e) {
    //
    //			}
    //		}
    //	}


    /**
     * 复制单个文件
     *
     * @param srcFileName  待复制的文件名
     * @param destFileName 目标文件名
     * @param overlay      如果目标文件存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyFile(String srcFileName, String destFileName, boolean overlay) {
        File srcFile = new File(srcFileName);

        // 判断源文件是否存在
        if (!srcFile.exists()) {
            return false;
        } else if (!srcFile.isFile()) {
            return false;
        }

        // 判断目标文件是否存在
        File destFile = new File(destFileName);
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                new File(destFileName).delete();
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            }
        }

        // 复制文件
        InputStream in = null;
        OutputStream out = null;
        int size = 1024;
        byte[] buffer = new byte[size];
        try {
            in = new BufferedInputStream(new FileInputStream(srcFile), size);
            out = new BufferedOutputStream(new FileOutputStream(destFile), size);
            while (in.read(buffer) != -1) {
                out.write(buffer);
            }
            out.flush();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
