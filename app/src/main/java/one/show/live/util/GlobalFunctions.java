package one.show.live.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class GlobalFunctions
{

    public static boolean CheckSDCard() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static void MakeDir(String dir) {
        File f = new File(dir);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    public static String GetDir(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        int index = path.lastIndexOf("/");
        return path.substring(0, index);
    }

    public static String GetFileName(String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }

        int index = path.lastIndexOf("/");
        return path.substring(index + 1, path.length());
    }

    public static String GetAppDir(Context context) {
        if (context == null){
            return "";
        }
        String dir = "";
        if (CheckSDCard()) {
            dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            dir = context.getDir("show_private", Context.MODE_PRIVATE)
                    .getAbsolutePath();
        }
        dir = dir + File.separator + "show" + File.separator;
        MakeDir(dir);

        String str_Hide_FilePath = dir + ".nomedia";
        File fHide = new File(str_Hide_FilePath);
        if (!fHide.isFile()) {
            try {
                fHide.createNewFile();
            } catch (Exception e) {
            }

        }
        return dir;
    }


    /**
     * 需要保存图片的路径
     *
     * @return
     */
    public static String getTakePictureFilePath(Context context) {
        //图片名称
        String filename = System.currentTimeMillis() + ".jpg";
        String filePath = GetAppDir(context) + "photo" + File.separator + filename;

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
                }
            }
        }
        return filePath;
    }

    /**
     * 地图文件的坐标
     * @param context
     * @return
     */
    public static String getLocationPath(Context context) {
        String folder = GetAppDir(context) + "location" + File.separator;
        FileUtils.createDir(folder);//如果本地没有对应文件夹，则创建文件夹
        return folder;
    }



    /**
     * 语音录制文件存储位置
     * @param context
     * @return
     */
    public static String getVoicePath(Context context) {
        String folder = GetAppDir(context) + "gift" + File.separator;
        FileUtils.createDir(folder);//如果本地没有对应文件夹，则创建文件夹
        return folder;
    }

    /**
     * 启动app小视频
     * @return
     */
    public static String getSplashVideoPath(Context context) {
        String folder = GetAppDir(context) + "splash" + File.separator;
        FileUtils.createDir(folder);
        return folder;
    }


    /**
     * 获取剩余空间到小提示
     * @param context
     * @return
     */
    public static long GetAppDirFreeSize(Context context) {
        try {
            StatFs sf = new StatFs(GetAppDir(context));
            long blockSize = sf.getBlockSize();
            long freeBlocks = sf.getAvailableBlocks();
            return (freeBlocks * blockSize)/1024 /1024; //单位MB
        }catch (Exception e){

        }
        return 1000;//默认返回1个G
    }

    public static String getSnapPath(Context context) {
        String path = GetAppDir(context);
        String snapPath = path + "snap/";
        File file = new File(snapPath);
        if (!file.isDirectory() || !file.exists()) {
            MakeDir(snapPath);
        }
        return snapPath + System.currentTimeMillis() + ".jpg";
    }

    /**
     * 得到 图片旋转 的角度
     *
     * @param filepath
     * @return
     */
    static public int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (Throwable ex) {
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }


    public static void savePic(Bitmap b, String strFileName) {
        if (b == null) {
            return;
        }
        FileOutputStream fos = null;

        int rate = 85;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.JPEG, rate, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }


    public static String GetNameFromUrl(String url) {
        if (url != null) {
            int pos = url.lastIndexOf(File.separator);
            String Name = url.substring(pos + 1, url.length());
            return Name;
        }
        return "";
    }

    public static boolean copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            } else {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    public static String RC4(String aInput, String aKey) {
        int[] iS = new int[256];
        byte[] iK = new byte[256];

        for (int i = 0; i < 256; i++) {
            iS[i] = i;
        }

        int j = 1;

        for (short i = 0; i < 256; i++) {
            iK[i] = (byte) aKey.charAt((i % aKey.length()));
        }

        j = 0;

        for (int i = 0; i < 255; i++) {
            j = (j + iS[i] + iK[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
        }

        int i = 0;
        j = 0;
        char[] iInputChar = aInput.toCharArray();
        char[] iOutputChar = new char[iInputChar.length];
        for (short x = 0; x < iInputChar.length; x++) {
            i = (i + 1) % 256;
            j = (j + iS[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
            int t = (iS[i] + (iS[j] % 256)) % 256;
            int iY = iS[t];
            char iCY = (char) iY;
            iOutputChar[x] = (char) (iInputChar[x] ^ iCY);
        }

        return new String(iOutputChar);

    }

    public static String DesEncryptStr(String source, String key) {
        String encryptedStr = "";
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());

            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);

            // using DES in CBC mode
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

            // 初始化Cipher对象
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

            // 执行加密操作
            byte encryptedData[] = cipher.doFinal(source.getBytes());

            // 通过Base64将二进制数据变成文本
            encryptedStr = new String(Base64.encode(encryptedData,
                    Base64.DEFAULT));

        } catch (Exception e) {
        }

        return encryptedStr;
    }

    public static boolean SaveDataToFile(byte[] data, String newPath) {
        try {
            FileOutputStream fs = new FileOutputStream(newPath);
            fs.write(data, 0, data.length);
            fs.flush();
            fs.close();
        } catch (Throwable e) {
            return false;
        }
        return true;
    }

    public static int GetHeartType(String userid, String liveid) {
        try {
            if (userid == null || userid.length() < 2 || userid.length() > 10 || TextUtils.isEmpty(liveid)) {
                return 0;
            }

            long lg = Long.valueOf(userid) + Long.valueOf(liveid);
            return Math.abs((int) (lg % 7));
        } catch (Exception e) {
        }
        return 0;
    }

    public static String MD5(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        } catch (Exception e) {
            return "";
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }

        return md5StrBuff.toString();
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buff_size = 1024 * 5;
        try {
            byte[] bytes = new byte[buff_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buff_size);
                if (count == -1) {
                    break;

                }
                os.write(bytes, 0, count);
            }
        } catch (Exception e) {
            // TODO: handle exception

        }
    }

    public static Bitmap GetBitmapFromSD(Context context, String strPath) {
        if (strPath == null || strPath.length() <= 1) {
            return null;
        }
        File file = new File(strPath);
        if (!file.exists()) {
            return null;
        }
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(strPath));
            return BitmapFactory.decodeStream(is, null, opt);
        } catch (Throwable e) {
            return null;
        }
    }

    private static Bitmap getImageInWidth(String srcPath, float bitmapWidth) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = bitmapWidth;//这里设置高度为800f
        float ww = bitmapWidth;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;//压缩好比例大小后再进行质量压缩
    }

    public static Bitmap GetBitmapUrlFromSD(Context context, String strUrl) {
        if (TextUtils.isEmpty(strUrl)) {
            return null;
        }
        String lowercase = strUrl;
        lowercase.toLowerCase();
        lowercase.trim();
        String path_dir = GlobalFunctions.GetAppDir(context) + "files" + File.separator;
        GlobalFunctions.MakeDir(path_dir);
        String strPath = path_dir + GlobalFunctions.MD5(lowercase);
        return GetBitmapFromSD(context, strPath);
    }


    public static Bitmap GetBitmapUrlFromSD(Context context, String strUrl, float bitmapWidth) {
        if (TextUtils.isEmpty(strUrl)) {
            return null;
        }
        String lowercase = strUrl;
        lowercase.toLowerCase();
        lowercase.trim();
        String path_dir = GlobalFunctions.GetAppDir(context) + "files" + File.separator;
        GlobalFunctions.MakeDir(path_dir);
        String strPath = path_dir + GlobalFunctions.MD5(lowercase);
        return getImageInWidth(strPath, bitmapWidth);
    }

    public static boolean CheckUrlFileExisted(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        String lowercase = url;
        lowercase.toLowerCase();
        lowercase.trim();
        String path_dir = GlobalFunctions.GetAppDir(context) + "files" + File.separator;
        GlobalFunctions.MakeDir(path_dir);
        String mLocalPath = path_dir + GlobalFunctions.MD5(lowercase);
        File file = new File(mLocalPath);
        if (file == null) {
            return false;
        } else {
            if (file.exists()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static String GetSavePath(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String lowercase = url;
        lowercase.toLowerCase();
        lowercase.trim();
        String path_dir = GlobalFunctions.GetAppDir(context) + "files" + File.separator;
        GlobalFunctions.MakeDir(path_dir);
        String mLocalPath = path_dir + GlobalFunctions.MD5(lowercase);
        return mLocalPath;
    }


    public static Bitmap GetBitmapFromBitmap(Context context, InputStream is) {
        if (is == null) {
            return null;
        }

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        try {
            Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
            is.close();
            return bm;
        } catch (Throwable e) {
            return null;
        }
    }

    //游客头像缓存
    private static HashMap<Integer, SoftReference<Bitmap>> touristHeadCache = new HashMap<Integer, SoftReference<Bitmap>>();

    public static String RemoveReturnChar(String str_input) {
        String str_ret = "";

        if (TextUtils.isEmpty(str_input)) {
            return str_ret;
        }

        str_ret = str_input.replace("\n", "");
        return str_ret;
    }

    /**
     * 根据sd卡内存情况 清理部分文件缓存
     * 1.计算存储目录下的文件大小
     * 2.当文件总大小大于规定的最大缓存大小或者sdcard剩余空间小于最小SD卡可用空间的规定时，
     * 删除特定数量的最近没有被使用的文件
     */
    private static boolean removeCache(File dir) {
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return true;
        }
        // 根据文件的最后修改时间进行排序
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File arg0, File arg1) {
                if (arg0.lastModified() > arg1.lastModified()) {
                    return 1;
                } else if (arg0.lastModified() == arg1.lastModified()) {
                    return 0;
                }
                return -1;
            }
        });

        int removeLenght = files.length / 2;
        //删掉一半数量的缓存数据
        for (int i = 0; i < removeLenght; i++) {
            if (files[i].exists()) {
                files[i].delete();
            }
        }
        return true;
    }

    /**
     * @param f File 实例
     * @return 文件夹大小，单位：字节
     * @throws Exception
     * @throws
     * @Methods: getFileSize
     * @Description: 获取文件夹的大小，包含子文件夹也可以
     */
    public static long getFileSize(File f) throws Exception
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }


    /**
     * 为了加快部署，渠道号不再写到程序代码中，而是从文件中读取
     */
    public static String getChannel(Context c) {
        if (TextUtils.isEmpty(CID)) {
            AssetManager am = c.getAssets();
            DataInputStream dis = null;
            OutputStream os = null;
            String code = "";
            try {
//                File file = new File(c.getFilesDir(), CID_DAT);
//                if (file.exists()) {
//                    InputStream is = c.openFileInput(CID_DAT);
//                    dis = new DataInputStream(is);
//                    code = dis.readLine();
//                } else {
                dis = new DataInputStream(am.open(CID_DAT));
                code = dis.readLine();
//                    ASSET_CID = Integer.parseInt(code.trim());
//                    if (bAppdebug) {
//                        Log.d(TAG, "Init ASSET_CID = " + CID);
//                    }
//                    os = c.openFileOutput(CID_DAT, Context.MODE_PRIVATE);
//                    os.write(code.getBytes());
//                    os.flush();
//                }
                CID = code.trim();
//                LiveLog.d("liuwei", "Init CID = " + CID);
//                if (bAppdebug) {
//                    Log.d(TAG, "Init CID = " + CID);
//                }
            } catch (Exception e) {
//                if (bAppdebug) {
//                    Log.d(TAG, "Init CID = " + e);
//                }
            } finally {
                try {
                    if (dis != null)
                        dis.close();
                    if (os != null)
                        os.close();
                } catch (Exception e) {
                }
            }
        }

        return CID;
    }

    private static final String CID_DAT = "cid.dat";

    public static String CID = "";

//    public static String getChannel(Context context)
//    {
//        ApplicationInfo appinfo = context.getApplicationInfo();
//        String sourceDir = appinfo.sourceDir;
//
//        String ret = "";
//        ZipFile zipfile = null;
//        try
//        {
//            zipfile = new ZipFile(sourceDir);
//            Enumeration<?> entries = zipfile.entries();
//            while (entries.hasMoreElements())
//            {
//                ZipEntry entry = ((ZipEntry) entries.nextElement());
//                String entryName = entry.getName();
//
//                if (entryName.startsWith("META-INF/channel"))
//                {
//                    ret = entryName;
//                    break;
//                }
//            }
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        finally
//        {
//            if (zipfile != null)
//            {
//                try
//                {
//                    zipfile.close();
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        String[] split = ret.split("_");
//        if (split != null && split.length >= 2)
//        {
//            return ret.substring(split[0].length() + 1);
//
//        }
//        else
//        {
//            return "";
//        }
//    }

	/* public static String getFileMD5(File file) {
	     if (!file.isFile()) {
	      return null;
	     }
	     MessageDigest digest = null;
	     FileInputStream in = null;
	     byte buffer[] = new byte[1024];
	     int len;
	     try {
	      digest = MessageDigest.getInstance("MD5");
	      in = new FileInputStream(file);
	      while ((len = in.read(buffer, 0, 1024)) != -1) {
	       digest.update(buffer, 0, len);
	      }
	      in.close();
	     } catch (Exception e) {
	      e.printStackTrace();
	      return null;
	     }
	     BigInteger bigInt = new BigInteger(1, digest.digest());
	     return bigInt.toString(16);
	    }*/

    public static String getFileMD5(File file) {

        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[2048];
            int length = -1;
            long s = System.currentTimeMillis();
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }

            //32位加密
            byte[] b = md.digest();
            return byteToHexString(b);

            // 16位加密
            // return buf.toString().substring(8, 24);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 把byte[]数组转换成十六进制字符串表示形式
     *
     * @param tmp 要转换的byte[]
     * @return 十六进制字符串表示形式
     */

    private static String byteToHexString(byte[] tmp) {
        char hexdigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        String s;
        // 用字节表示就是 16 个字节
        char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
        // 所以表示成 16 进制需要 32 个字符
        int k = 0; // 表示转换结果中对应的字符位置
        for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
            // 转换成 16 进制字符的转换
            byte byte0 = tmp[i]; // 取第 i 个字节
            str[k++] = hexdigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
            // >>> 为逻辑右移，将符号位一起右移
            str[k++] = hexdigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
        }
        s = new String(str); // 换后的结果转换为字符串
        return s;
    }

    public static long TimeStringTranToUnix(String time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(time);
            long l = d.getTime();
            return l;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
}
