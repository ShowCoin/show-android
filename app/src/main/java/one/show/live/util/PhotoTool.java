package one.show.live.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import one.show.live.MeetApplication;
import one.show.live.common.util.FileUtils;
import one.show.live.common.util.FrescoUtils;
import one.show.live.log.Logger;

public class PhotoTool {
    private int chooseFromAlbum;
    private int chooseFromCamera;
    private int cropBigPicture;

    private Uri imageUri;
    private Activity context;
    private Fragment fragment;
    private Bitmap temBitmap;

    private static final int PHOTO_REQUEST_CAMERA = 0x01 << 9;//上传相册调取拍照

    public PhotoTool(Activity context, int chooseFromAlbum, int chooseFromCamera, int cropBigPicture) {
        this.context = context;
        this.chooseFromAlbum = chooseFromAlbum;
        this.chooseFromCamera = chooseFromCamera;
        this.cropBigPicture = cropBigPicture;
        setPhotoPath();
    }

    public PhotoTool(Activity context, Fragment fragment, int chooseFromAlbum, int chooseFromCamera, int cropBigPicture) {
        this.context = context;
        this.fragment = fragment;
        this.chooseFromAlbum = chooseFromAlbum;
        this.chooseFromCamera = chooseFromCamera;
        this.cropBigPicture = cropBigPicture;
        setPhotoPath();
    }

    public boolean isSelected() {
        return new File(imageUri.getPath()).exists();
    }

    /**
     * 获取原图片存储路径
     * 备注：默认存在ImageLoad的缓存文件夹
     *
     * @return
     */
    public String getPhotoPath() {

        return imageUri.getPath();
    }

    /**
     * 设置图片的路径
     */
    private void setPhotoPath() {
        String path = FileUtils.getCacheDiskPath( "upload").getAbsolutePath();
        String name = Calendar.getInstance().getTimeInMillis() + ".jpg";

        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            file.deleteOnExit();
        }
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return;
            }
        }
        imageUri = FrescoUtils.getFileUri(path + File.separator + name);
    }

    /**
     * 获取Uri的路径
     *
     * @param uri
     */
    public String getPhotoPathByUri(Uri uri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index;
            if (cursor == null) {
                return null;
            }
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return new File(cursor.getString(column_index)).getPath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 从相册获取照片
     */
    @SuppressLint("InlinedApi")
    public void getPhotoFromAlbum() {
        try {
            Intent intent;
            if (Build.VERSION.SDK_INT < 19) {
                intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setType("video/*;image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 600);
                intent.putExtra("outputY", 600);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", false);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.putExtra("outputFormat", imageUri.getPath());
                intent.putExtra("noFaceDetection", false); // no face detection

            } else {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
                intent.addCategory(Intent.CATEGORY_OPENABLE);
            }
            if (fragment != null) {
                fragment.startActivityForResult(intent, chooseFromAlbum);
            } else {
                context.startActivityForResult(intent, chooseFromAlbum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 复制图片到缓存文件夹
     *
     * @param path
     */
    private void copyFile(String path) {
        try {
            InputStream in = new FileInputStream(new File(path));
            OutputStream out = new FileOutputStream(new File(imageUri.getPath()));
            byte[] b = new byte[1024];
            int len;
            while ((len = in.read(b)) > 0) {
                out.write(b, 0, len);
                out.flush();
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从相机获取
     */
    public void getPhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        if (fragment != null) {
            fragment.startActivityForResult(intent, chooseFromCamera);
        } else {
            context.startActivityForResult(intent, chooseFromCamera);
        }

    }

    /**
     * 裁剪图片
     *
     * @param uri
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    @SuppressLint("InlinedApi")
    public void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        setPhotoPath();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");// 才能出剪辑的小方框，不然没有剪辑功能，只能选取图片
        intent.putExtra("aspectX", 1); // 放大缩小比例的X
        intent.putExtra("aspectY", 1);// 放大缩小比例的X   这里的比例为：   1:1
        intent.putExtra("outputX", outputX);  //这个是限制输出图片大小
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
//        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            context.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 根据路径获取Bitm
     * 说明：当调用getPhotoFromCamera()获取图像时，在onActivityResult()中用此获取图片
     *
     * @return
     */
    public Bitmap getBitmap(boolean isReset) {
        if (imageUri == null) {
            return null;
        }

        if (isReset && temBitmap != null && !temBitmap.isRecycled()) {
            temBitmap.recycle();
        }
        temBitmap = BitmapFactory.decodeFile(imageUri.getPath());
        return temBitmap;
    }

    /**
     * @param requestCode 请求码
     * @param resultCode  响应码
     * @param data        数据
     * @return true 获取剪切图成功 false 还没获取到剪切图
     */
    @SuppressLint("InlinedApi")
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {//result is not correct
            return false;
        }

        if (requestCode == chooseFromAlbum) {
            if (Build.VERSION.SDK_INT >= 19) {
                copyFile(getPath(context, data.getData()));
                cropImageUri(imageUri, 480, 480, cropBigPicture);
                return false;
            }
            return true;
        } else if (requestCode == chooseFromCamera) {
            if (requestCode == PHOTO_REQUEST_CAMERA) {//判断是上传相册的就直接返回true
                return true;
            }
            cropImageUri(imageUri, 480, 480, cropBigPicture);

            return false;
            //decodeUriAsBitmap(imageUri);
        } else if (requestCode == cropBigPicture) {
            return true;
        }
        return false;
    }

    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param uri    需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static String rotateBitmapByDegree(Uri uri, int degree) {
        Bitmap bm = null;
        Bitmap returnBm = null;
        try {
            bm = MediaStore.Images.Media.getBitmap(MeetApplication.getContext().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;

        //根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            Logger.d("Nano",e+"");
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }

        String path = one.show.live.common.util.FileUtils.getCacheDiskPath("upload").getAbsolutePath();
        String name = Calendar.getInstance().getTimeInMillis() + ".jpg";


        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try
        {
            stream = new FileOutputStream(path+name);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        if(returnBm.compress(format, quality, stream)){
            return path+name;
        }
        return "";
    }

}