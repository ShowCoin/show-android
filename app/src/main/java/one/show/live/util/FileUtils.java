package one.show.live.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import one.show.live.log.Logger;

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();


    private static String BASE_ROOT_PATH = null;

    public static String getRootPath(Context context) {
        if (BASE_ROOT_PATH == null) {
            initRootPath(context);
        }
        return BASE_ROOT_PATH;
    }

    public static boolean isFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            File f = new File(path);
            if (f != null && f.exists() && f.canRead() && (f.isFile() && f.length() > 0))
                return true;
        }
        return false;
    }


    /**
     * 创建目录
     *
     * @param dir
     */
    public static void createDir(String dir) {
        File f = new File(dir);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    /**
     * 创建文件
     *
     * @param file
     */
    public static void createFile(String file) {
        File f = new File(file);
        if (f != null && !f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建nomedia文件，屏蔽媒体文件
     *
     * @param path
     */
    public static void createNoMediaFile(String path) {
        String file = path + File.separator + ".nomedia";
        createFile(file);
    }

    /**
     * 获取文件的数量
     *
     * @param path
     * @return
     */
    public static int getFileCount(String path) {
        if (TextUtils.isEmpty(path)) {
            return 0;
        }
        File file = new File(path);
        if (file == null || file.isFile()) {
            return 0;
        }
        return file.listFiles().length;
    }


    /**
     * 删除nomedia
     *
     * @param path
     */
    public static void deleteNoMediaFile(String path) {
        String filePath = path + File.separator + ".nomedia";
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    public static String getExternalFileDir(Context context) {
        StringBuilder sb = new StringBuilder();
        File file = context.getExternalFilesDir("");
        if (file != null) {
            sb.append(file.getAbsolutePath());
        } else {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                sb.append(Environment.getExternalStorageDirectory().getPath()).append("/Android/data/").append(context.getPackageName())
                        .append("/files").toString();
            } else {
                sb.append(context.getCacheDir().getAbsolutePath());
            }

        }
        return sb.toString();
    }

    /**
     * 文件夹名称
     */
    private static final String FLODER_NAME = "photo";


    /**
     * 需要保存图片的路径
     *
     * @return
     */
    public static String getTakePictureFilePath(Context context) {
        //图片名称
        String filename = System.currentTimeMillis() + ".jpg";
//        String filePath = getThumbFileFloder(context) + File.separator + filename;

        String filePath = GlobalFunctions.GetAppDir(context) + File.separator + filename;
        // 兼容一些手机 不会自动创建目录的问题，需手动创建。如coolpad
        if (filePath != null) {
            File file = new File(filePath);
            File parent = new File(file.getParent());
            if (!parent.exists()) {
                parent.mkdirs();
            }
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                    ;
                }
            }
        }
        return filePath;
    }

    /**
     * 缩略图路径（不需要展示在图库）
     *
     * @param context
     * @return
     */
    public static String getThumbFileFloder(Context context) {
        String dir = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//如果挂载了SD卡
            dir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Android/data/" + context.getPackageName() + File.separator + FLODER_NAME;
        } else {//使用系统默认缓存目录
            dir = context.getCacheDir() + File.separator + FLODER_NAME;
        }

        File f = new File(dir);
        if (f != null && !f.exists()) {
            f.mkdirs();
        }
        createNoMediaFile(dir);

        return dir;
    }


    private static final String FOLDER_PICTURE = "花椒";
    private static final String FOLDER_ALBUM = "花椒相册";
    private static final String FOLDER_VIDEO_ALBUM = "花椒小视频";

    public static String getPictureFolder(Context context) {
        String dir = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//如果挂载了SD卡
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + FOLDER_PICTURE;

        } else {//使用系统默认缓存目录
            dir = context.getCacheDir() + File.separator + FOLDER_PICTURE;
        }

        File f = new File(dir);
        if (f != null && !f.exists()) {
            f.mkdirs();
        }

        return dir;
    }

    /**
     * 花椒相册
     *
     * @return
     */
    public static String getAlbumFolder(Context context) {
        String dir = getPictureFolder(context) + File.separator + FOLDER_ALBUM;

        File f = new File(dir);
        if (f != null && !f.exists()) {
            f.mkdirs();
        }

        return dir;
    }

    /**
     * 花椒小视频目录
     *
     * @return
     */
    public static String getVideoAlbumFolder(Context context) {
        String dir = getPictureFolder(context) + File.separator + FOLDER_VIDEO_ALBUM;

        File f = new File(dir);
        if (!f.exists()) {
            f.mkdirs();
        }

        return dir;
    }


    public static void makeDir(String dir) {
        File f = new File(dir);
        if (f != null && !f.exists()) {
            f.mkdirs();
        }
    }


    /**
     * 花椒精彩时刻相册文件夹
     *
     * @return
     */
    public static String getHJGalleryFolder(Context context) {
        String dir = getPictureFolder(context) + File.separator + "HJGallery";
        makeDir(dir);
        return dir;
    }

    /**
     * 花椒精彩时刻相册-图片文件夹
     *
     * @return
     */
    public static String getHJGalleryPhotoFolder(Context context) {
        String dir = getHJGalleryFolder(context) + File.separator + "photo";
        makeDir(dir);
        return dir;
    }

    /**
     * 花椒精彩时刻相册-视频文件夹
     *
     * @return
     */
    public static String getHJGalleryVideoFolder(Context context) {
        String dir = getHJGalleryFolder(context) + File.separator + "video";
        makeDir(dir);
        return dir;
    }

    /**
     * 花椒精彩时刻相册-视频源
     *
     * @return
     */
    public static String getHJGalleryVideoSourceFolder(Context context) {
        String dir = getHJGalleryVideoFolder(context) + File.separator + "source";
        makeDir(dir);
        return dir;
    }

    /**
     * 花椒精彩时刻相册-视频缩略图
     *
     * @return
     */
    public static String getHJGalleryVideoThumbFolder(Context context) {
        String dir = getHJGalleryVideoFolder(context) + File.separator + "thumb";
        makeDir(dir);
        return dir;
    }


    /**
     * 需要保存图片的路径
     *
     * @return
     */
    public static String getShareFilePath(Context context) {
        //图片名称
        String filename = "sharescreen.jpg";
        String dir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//如果挂载了SD卡
            dir = context.getExternalCacheDir() + File.separator + FLODER_NAME;

        } else {//使用系统默认缓存目录
            dir = context.getCacheDir() + File.separator + FLODER_NAME;
        }

        File fileDir = new File(dir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
            createNoMediaFile(dir);
        }

        return dir + File.separator + filename;
    }

    private static final String CAPTURE_FOLDER = "capture";

    /**
     * 需要截取图片的路径，不显示在图库
     *
     * @return
     */
    public static String getCaptureFilePath(Context context) {
        String dir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//如果挂载了SD卡
            dir = context.getExternalCacheDir() + File.separator + CAPTURE_FOLDER;

        } else {//使用系统默认缓存目录
            dir = context.getCacheDir() + File.separator + CAPTURE_FOLDER;
        }

        File fileDir = new File(dir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
            createNoMediaFile(dir);
        }

        return dir + File.separator;
    }

    private static final String ANIM_FOLDER = "effect_anim";

    public static String getAnimFilePath(Context context) {
        String dir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//如果挂载了SD卡
            dir = context.getExternalCacheDir() + File.separator + ANIM_FOLDER;

        } else {//使用系统默认缓存目录
            dir = context.getCacheDir() + File.separator + ANIM_FOLDER;
        }

        File fileDir = new File(dir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
            createNoMediaFile(dir);
        }

        return dir + File.separator;
    }


    //直播间礼物列表预览效果路径
    private static final String GIFT_PREVIEW_ANIM_FOLDER = "gift_preview_anim_folder";
    //小视频礼物动画
    private static final String GIFT_VIDEO_PNG_FOLDER = "gift_video_png_folder";


    /**
     * 礼物预览图片路径（png序列）
     *
     * @return
     */
    public static String getGiftPreviewAnimFilePath(Context context) {
        return getRootPath(context) + GIFT_PREVIEW_ANIM_FOLDER + File.separator;
    }

    /**
     * 小视频礼物动销
     *
     * @return
     */
    public static String getGiftVideoPngFilePath(Context context) {
        return getRootPath(context) + GIFT_VIDEO_PNG_FOLDER + File.separator;
    }

    /**
     * 需要保存图片的路径
     *
     * @return
     */
    public static String getReportFilePath(Context context, String name) {
        //图片名称
        String filename = name;
        String dir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//如果挂载了SD卡
            dir = context.getExternalCacheDir() + File.separator + FLODER_NAME;

        } else {//使用系统默认缓存目录
            dir = context.getCacheDir() + File.separator + FLODER_NAME;
        }

        File fileDir = new File(dir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        return dir + File.separator + filename;
    }

    public static String getShareFileScreenPath(Context context) {
        //图片名称
        String filename = "sharescreenScreen.jpg";
        String dir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//如果挂载了SD卡
            dir = context.getExternalCacheDir() + File.separator + FLODER_NAME;

        } else {//使用系统默认缓存目录
            dir = context.getCacheDir() + File.separator + FLODER_NAME;
        }
        File fileDir = new File(dir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return dir + File.separator + filename;
    }

    /**
     * Gets the content:// URI  from the given corresponding path to a file
     *
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static void updateGallery(String filename, Context context)// filename是我们的文件全名，包括后缀哦
    {
        MediaScannerConnection.scanFile(context, new String[]{filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        // CLog.i("Scanned " + path + ":");
                        // CLog.i("-> uri=" + uri);
                    }
                });
    }

    public static boolean copyFile(String srcPath, String destPath) {
        return copyFile(new File(srcPath), new File((destPath)));
    }

    public static boolean copyFile(File src, File dest) {
        try {
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            if (!dest.exists()) {
                dest.createNewFile();
            }

            FileOutputStream outputStream = new FileOutputStream(dest);
            FileInputStream inputStream = new FileInputStream(src);
            DataInputStream dataInput = new DataInputStream(inputStream);
            DataOutputStream dataOutput = new DataOutputStream(outputStream);
            byte[] wxj = new byte[1024];
            int length = dataInput.read(wxj);
            while (length != -1) {
                dataOutput.write(wxj, 0, length);
                length = dataInput.read(wxj);
            }
            outputStream.close();
            inputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 保存文件
     *
     * @param path
     * @param data
     * @return
     */
    public static boolean saveFile(String path, byte[] data) {
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file); // Get file output stream
            fos.write(data); // Written to the file
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取缓存图片的个数
     *
     * @return
     */
    public static int getPhotoCacheSize(Context context) {
        String folder = getPictureFolder(context);
        File file = new File(folder);
        if (file.isDirectory()) {
            File[] list = file.listFiles();
            if (list == null) {
                return 0;
            }
            return file.listFiles().length;
        }
        return 0;
    }

    /**
     * 删除缓存
     */
    public static void clear(Context context) {
        clearPhotoFile(getPictureFolder(context));
    }

    /**
     * 删除图片
     */
    public static void clearPhotoFile(String path) {
        File file = new File(path);
        if (file.isFile()) {
            file.delete();
        } else {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    if (f.isDirectory()) {
                        clearPhotoFile(f.getAbsolutePath());
                    } else {
                        f.delete();
                    }
                }
            }
        }
    }

    /**
     * 删除文件或者目录
     *
     * @param filepath 要删除的文件路径
     */
    public static void deleteFile(String filepath) {
        File file = new File(filepath);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    if (files.length > 0) {
                        File[] delFiles = file.listFiles();
                        if (delFiles != null && delFiles.length > 0) {//fix  delFiles 为空导致空指针
                            for (File delFile : delFiles) {
                                deleteFile(delFile.getAbsolutePath());
                            }
                        }
                    }
                }
            }
            file.delete();
        }
    }

    /**
     * 删除文件或者目录
     *
     * @param file 要删除的文件
     */
    public static void deleteFile(File file) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                File[] filelist = file.listFiles();
                if (filelist != null && filelist.length > 0) {
//                    File[] delFiles = file.listFiles();
                    for (File delFile : filelist) {
                        if (delFile.exists())
                            deleteFile(delFile);
                    }
                }
            }
            file.delete();
        }
    }

    public static void saveBitmap(Bitmap bitmap, String filePath) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(filePath));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }


    public static File getLatestFile(File[] files) {
        if (files == null || files.length == 0) {
            return null;
        } else {
            long lastMod = Long.MIN_VALUE;
            File choice = null;
            for (File file : files) {
                if (file.isDirectory()) {
                    continue;
                } else {
                    if (file.lastModified() > lastMod) {
                        choice = file;
                        lastMod = file.lastModified();
                    }
                }
            }
            return choice;
        }
    }

    public static void zipFile(File destZip, File... files) throws IOException {
        if (files == null || files.length == 0) {
            return;
        }
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(destZip));
        for (File file : files) {
            if (!file.isDirectory()) {
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zipOutputStream.putNextEntry(zipEntry);
                FileInputStream inputStream = new FileInputStream(file);
                copyStream(inputStream, zipOutputStream);
                inputStream.close();
                zipOutputStream.closeEntry();
            }
        }
        zipOutputStream.close();
    }

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        if (in == null || out == null) {
            return;
        }
        byte[] buffer = new byte[4096];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
    }

    public static boolean unzip(String zipFileString, String outPathString) {
        boolean b_ret = true;
        try {
            ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
            ZipEntry zipEntry;
            String szName = "";
            while ((zipEntry = inZip.getNextEntry()) != null) {
                szName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    // get the folder name of the widget
                    szName = szName.substring(0, szName.length() - 1);
                    File folder = new File(outPathString + File.separator + szName);
                    folder.mkdirs();
                } else {
                    int n_last_index = szName.lastIndexOf("/");
                    if (n_last_index != -1) {
                        String str_folder_name = outPathString + File.separator + szName.substring(0, n_last_index);
                        File folder = new File(str_folder_name);
                        if (!folder.isDirectory()) {
                            folder.mkdirs();
                        }
                    }
                    File file = new File(outPathString + File.separator + szName);
                    file.createNewFile();
                    // get the output stream of the file
                    FileOutputStream out = new FileOutputStream(file);
                    int len;
                    byte[] buffer = new byte[1024];
                    // read (len) bytes into buffer
                    while ((len = inZip.read(buffer)) != -1) {
                        // write (len) byte from buffer at the position 0
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                    out.close();
                }
            }
            inZip.close();
        } catch (Throwable e) {
            b_ret = false;
            Logger.e(TAG, "解压失败");
        }
        return b_ret;
    }

    /**
     * 文件复制
     *
     * @param s
     * @param t
     */
    public static void fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
            t.delete();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//
//    public static class Const {
//        public static String SDCARD_PATH = Environment
//                .getExternalStorageDirectory().getAbsolutePath() + File.separator;
//        public static final String ID_RECENT_ALBUMS = "-123456";
//        public static String ENCODING = "UTF-8";
//
//        public static final long MINAVAILABLESPARE = 10L;
//        public static final long MINAVAILABLESPARE_RECORD = 200L;
//
//
//        public static final String[] defaultAlbum = new String[] {
//                "/storage/extSdCard/DCIM/Camera", // for samsung external sdcard
//                SDCARD_PATH + "DCIM/Camera", SDCARD_PATH + "DCIM/100MEDIA",
//                SDCARD_PATH + "DCIM/100ANDRO", SDCARD_PATH + "Camera" };
//        public static final String[] screenAlbum = new String[] {
//                SDCARD_PATH + "Screenshots", SDCARD_PATH + "Pictures/Screenshots",
//                SDCARD_PATH + "DCIM/Screenshots", };
//
//        public static final String[] cameraSnap = new String[] {
//                SDCARD_PATH + "花椒", SDCARD_PATH + "花椒/我的相册",SDCARD_PATH + "花椒/分享"
//        };
//    }


//    private static File mSDCacheDir;// 临时数据存放地
//    private static File mCacheDir;// 数据存放地
//    private static File snapshotFile;
//    private static File shareFile;
//    private static File recordFile;
//    private static File devicesFile;
//    private static File compressFile;
//    private static File logFile;
//    private static File userFile;
//
//    private static final String videoPath = Const.cameraSnap[1];
//    private static final String picturePath =  Const.cameraSnap[1];
//    private static final String sharePath = Const.cameraSnap[2];
//
//    private static void initCacheDir() {
//        mCacheDir = BaseApplication.getContext().getFilesDir();
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            mSDCacheDir = BaseApplication.getContext().getExternalCacheDir();
//            if (mSDCacheDir == null) {
//                mSDCacheDir =
//                        new File(Environment.getExternalStorageDirectory(), "com.huajiao/cache");
//            }
//            snapshotFile =
//                    new File(/*Environment.getExternalStorageDirectory().getPath() +*/ picturePath);
//            recordFile = new File(/*Environment.getExternalStorageDirectory().getPath() + */videoPath);
//            shareFile = new File(/*Environment.getExternalStorageDirectory().getPath() + */sharePath);
////            eventFile = new File(Environment.getExternalStorageDirectory().getPath() + eventPath);
//        } else {
//            mSDCacheDir = mCacheDir;
//            snapshotFile = new File(/*Environment.getRootDirectory().getPath() +*/ picturePath);
//            recordFile = new File(/*Environment.getRootDirectory().getPath() + */videoPath);
//            shareFile = new File(/*Environment.getRootDirectory().getPath() + */sharePath);
////            eventFile = new File(Environment.getRootDirectory().getPath() + eventPath);
//        }
//        if (!mSDCacheDir.exists()) {
//            mSDCacheDir.mkdirs();
//        }
//        if (!snapshotFile.exists()) {
//            snapshotFile.mkdirs();
//        }
//        if (!recordFile.exists()) {
//            recordFile.mkdirs();
//        }
//        if(!shareFile.exists()){
//            shareFile.mkdirs();
//        }
//
//        userFile = new File(mSDCacheDir, "/user");
//        devicesFile = new File(mSDCacheDir, "/devices");
//        compressFile = new File(mSDCacheDir,"/compress");
//        logFile = new File(mSDCacheDir.getAbsolutePath().replace("cache", "log"));
//
////        localZipFile = new File(mCacheDir, Const.UPDATE_FILENAME);
////        tempZipFile = new File(mSDCacheDir, Const.UPDATE_FILENAME);
//
//        if (!userFile.exists()) {
//            userFile.mkdir();
//        }
//
//        if (!devicesFile.exists()) {
//            devicesFile.mkdirs();
//        }
//
//        if (!logFile.exists()) {
//            logFile.mkdirs();
//        }
//
//        if (!compressFile.exists()){
//            compressFile.mkdir();
//        }
//
//    }

//    public static File getShareFile() {
//        if (shareFile == null || !shareFile.exists()) {
//            initCacheDir();
//        }
//        return shareFile;
//    }

    public static void writeFileSdcard(String filePath, String message, boolean append) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath, append);
            byte[] bytes = message.getBytes();
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void initRootPath(Context context) {
        if (BASE_ROOT_PATH == null) {
            File file = null;
            try {
                file = Environment.getExternalStorageDirectory();
                if (file.exists() && file.canRead() && file.canWrite()) {
                    //如果可读写，则使用此目录
                    String path = file.getAbsolutePath();
                    if (path.endsWith("/")) {
                        BASE_ROOT_PATH = file.getAbsolutePath() + "seeu/";
                    } else {
                        BASE_ROOT_PATH = file.getAbsolutePath() + "/seeu/";
                    }
                }
            } catch (Exception e) {

            }
            if (BASE_ROOT_PATH == null) {
                //如果走到这里，说明外置sd卡不可用
                if (context != null) {
                    file = context.getFilesDir();
                    String path = file.getAbsolutePath();
                    if (path.endsWith("/")) {
                        BASE_ROOT_PATH = file.getAbsolutePath() + "seeu/";
                    } else {
                        BASE_ROOT_PATH = file.getAbsolutePath() + "/seeu/";
                    }
                } else {
                    BASE_ROOT_PATH = "/sdcard/seeu/";
                }
            }
        }
        File file = new File(BASE_ROOT_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String getBase64EncodeStr(String filePath) {
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(filePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String str = null;

        if (data != null) {
            str = Base64.encodeToString(data, Base64.DEFAULT);
        }
        return str;
    }

    /**
     * 删除指定文件夹及文件夹下所有的文件
     * 注意：文件夹下某一个文件删除失败则会跳过，直接删除下一个文件
     *
     * @param file
     * @return
     */
    public static boolean delteFile(File file) {
        if (!file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    delteFile(f);
                }
            }
        }
        return file.delete();
    }

    /**
     * 保存bitmap至指定Picture文件夹
     */
    public static String saveBitmap(Bitmap bitmap, File mediaStorageDir) {
        String savePath = null;
        if (bitmap == null || mediaStorageDir == null)
            return savePath;

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mediaStorageDir);
            boolean successful = bitmap.compress(
                    Bitmap.CompressFormat.PNG, 75, fos);

            if (successful)
                savePath = mediaStorageDir.getAbsolutePath();
            else
                savePath = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            savePath = null;
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return savePath;
    }


    /**
     * 保存bitmap至指定Picture文件夹
     */
    public static String saveBitmap(Bitmap bitmap, File mediaStorageDir, String name) {
        String savePath = null;
        if (bitmap == null || mediaStorageDir == null)
            return savePath;

        String bitmapFileName = name + ".png";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mediaStorageDir + "/" + bitmapFileName);
            boolean successful = bitmap.compress(
                    Bitmap.CompressFormat.PNG, 75, fos);

            if (successful)
                savePath = mediaStorageDir.getAbsolutePath() + "/" + bitmapFileName;
            else
                savePath = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            savePath = null;
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return savePath;
    }

    /**
     * 获取DownLoadVideo文件
     */
    public static File getDownLoadVideoFile() {
        String downloadVideoPath = CONSTANT.rootPath + CONSTANT.downloadVideo;
        File videoFile = new File(downloadVideoPath);
        if (!videoFile.exists()) {
            if (!videoFile.mkdirs()) {
                return null;
            }
        }
        return videoFile;
    }

    /**
     * 获取DownLoadVideo文件路径
     */
    public static String getDownLoadVideoPath() {
        File file = getDownLoadVideoFile();
        if (file == null)
            return null;

        return file.getAbsolutePath();
    }

    /**
     * 获取DownLoadPicture文件路径
     */
    public static String getDownLoadPicturePath() {
        File file = getDownLoadPictureFile();
        if (file == null)
            return null;

        return file.getAbsolutePath();
    }

    /**
     * 获取DownLoadPicture文件
     */
    public static File getDownLoadPictureFile() {
        String downloadVideoPath = CONSTANT.rootPath + CONSTANT.downloadPicture;
        File videoFile = new File(downloadVideoPath);
        if (!videoFile.exists()) {
            if (!videoFile.mkdirs()) {
                return null;
            }
        }
        return videoFile;
    }
    /**
     * 获取DownLoad文件路径
     */
    public static String getDownLoadPath() {
        File file = getDownLoadFile();
        if (file == null)
            return null;

        return file.getAbsolutePath();
    }

    /**
     * 获取DownLoad文件
     */
    public static File getDownLoadFile() {
        String downloadVideoPath = CONSTANT.rootPath + CONSTANT.download;
        File videoFile = new File(downloadVideoPath);
        if (!videoFile.exists()) {
            if (!videoFile.mkdirs()) {
                return null;
            }
        }
        return videoFile;
    }

    public static void getFileList(File path, HashMap<String, String> fileList) {
        //如果是文件夹的话
        if (path.isDirectory()) {
            //返回文件夹中有的数据
            File[] files = path.listFiles();
            //先判断下有没有权限，如果没有权限的话，就不执行了
            if (null == files)
                return;

            for (int i = 0; i < files.length; i++) {
                getFileList(files[i], fileList);
            }
        }
        //如果是文件的话直接加入
        else {
            //进行文件的处理
            String filePath = path.getAbsolutePath();
            //文件名
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            //添加
            fileList.put(fileName, filePath);
        }
    }


    /**
     * 使用文件通道的方式复制文件
     *
     * @param oldPath 源文件路径
     * @param newPath 复制到的新文件路径
     */
    public static boolean fileChannelCopy(String oldPath, String newPath) {
        boolean isSuccess = false;

        if (oldPath == null || newPath == null)
            return isSuccess;

        File oldFile = new File(oldPath);
        if (!oldFile.exists())
            return isSuccess;

        File newFile = new File(newPath);
//        if (!newFile.exists())
//            newFile.mkdirs();

        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(oldFile);
            fo = new FileOutputStream(newFile);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
            isSuccess = true;
        } catch (IOException e) {
            isSuccess = false;
            e.printStackTrace();
        } finally {
            if (fi != null)
                try {
                    fi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            if (fo != null)
                try {
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            if (out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return isSuccess;
    }

    public static String hashMapToJson(HashMap map) {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry e = (Map.Entry) it.next();
            string += "'" + e.getKey() + "':";
            string += "'" + e.getValue() + "',";
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;
    }
}
