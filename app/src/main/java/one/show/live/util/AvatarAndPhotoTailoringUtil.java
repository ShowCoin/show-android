package one.show.live.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;

import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.FrescoUtils;
import one.show.live.log.Logger;
import one.show.live.util.PhotoTool;
import one.show.live.widget.ActionSheetDialog;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Nano
 * 带裁剪功能的
 */

public class AvatarAndPhotoTailoringUtil {

    private Uri imageUri;
    private Uri smallImageUri;

    private static final int PERMISSIONS_REQUEST_CAMERA = 0x01 << 4;
    private static final int PERMISSIONS_REQUEST_GALLERY = 0x01 << 5;

    private static final int PHOTO_REQUEST_CAMERA = 0x01 << 9;//上传相册调取拍照
    private static final int PHOTO_REQUEST_ALBUM = 0x01 << 10;//上传相册调取拍照
    private static final int REQUEST_PICKER_AND_CROP = 0x01 << 11;//裁剪之后回调

    Context context;

    String endPath;

    public AvatarAndPhotoTailoringUtil(Context context, OnPathLisenter onUriLisenter) {
        this.context = context;
        this.onPathLisenter = onUriLisenter;
        setPhotoPath();
    }

    /**
     * 设置图片的路径
     */
    private void setPhotoPath() {
        String path = one.show.live.common.util.FileUtils.getCacheDiskPath("upload").getAbsolutePath();
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
     * 拍照或者相册
     */
    public void iosDialogButtomImage() {
        ActionSheetDialog dialog = new ActionSheetDialog(context);
        dialog.builder();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.addSheetItem(context.getString(one.show.live.R.string.taking_pictures), ActionSheetDialog.SheetItemColor.BlueDeauft,
                new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {

                        getImageFromCameraAlbum();//调用摄像头上传相册的
                    }
                }).addSheetItem(context.getString(one.show.live.R.string.photo_album), ActionSheetDialog.SheetItemColor.BlueDeauft,
                new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        getImageFromAlbum();
                    }
                });
        dialog.show();

    }

    /**
     * 通过相机拍照,
     */
    public void getImageFromCameraAlbum() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasLocationPermission = context.checkSelfPermission(Manifest.permission.CAMERA);
            if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                ((BaseFragmentActivity) context).requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
                return;
            }
        }
        getPhotoFromCamera();

    }

    /**
     * 从相机获取
     */
    public void getPhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        ((Activity) context).startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }

    /**
     * 通过相册上传头像的
     */
    public void getImageFromAlbum() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasLocationPermission = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                ((BaseFragmentActivity) context).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_GALLERY);
                return;
            }
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*"});
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        ((Activity) context).startActivityForResult(intent, PHOTO_REQUEST_ALBUM);
    }


    /**
     * 压缩图片
     *
     * @param oldFile
     */
    public void compressionPhoto(String oldFile) {

        String path = one.show.live.common.util.FileUtils.getCacheDiskPath("upload").getAbsolutePath() + File.separator;

        Luban.with(context)
                .load(oldFile)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                .setTargetDir(path)     // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        Logger.d("压缩开始");
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        Logger.d("压缩成功");
                        ArrayList<String> fileList = new ArrayList<>();
                        fileList.add(file.getPath());
//                        UploadMyMediaUtils.uploadMediaList(context, fileList);
//                        ToastUtils.showToast("相册上传中..");

                        onPathLisenter.onPath(file.getPath(),smallImageUri.getPath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        Logger.d("压缩失败");
                    }
                }).launch();  //启动压缩

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PHOTO_REQUEST_CAMERA://相机回调
                if (onPathLisenter != null) {

                    int degree = PhotoTool.getBitmapDegree(imageUri.getPath());

                    endPath = PhotoTool.rotateBitmapByDegree(imageUri, degree);//处理一下是否需要旋转

//                    compressionPhoto(path);
                    try {
                        startCropIntent(new File(endPath));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PHOTO_REQUEST_ALBUM://相册回调

                int degree = PhotoTool.getBitmapDegree(data.getData().getPath());

                endPath = PhotoTool.rotateBitmapByDegree(data.getData(), degree);

//                compressionPhoto(path);
                try {
                    startCropIntent(new File(endPath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_PICKER_AND_CROP://裁剪回调
                Logger.d("NANO", smallImageUri.getPath());
                compressionPhoto(endPath);
                break;
        }
    }

    OnPathLisenter onPathLisenter;

    public interface OnPathLisenter {

        /**
         * 返回压缩图和裁剪图
         * @param path
         * @param smallPath
         */
        void onPath(String path, String smallPath);
    }

    private void startCropIntent(File file) throws FileNotFoundException {

        String path = one.show.live.common.util.FileUtils.getCacheDiskPath("upload").getAbsolutePath();
        String name = Calendar.getInstance().getTimeInMillis() + ".jpg";
        smallImageUri = FrescoUtils.getFileUri(path + File.separator + name);

        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri uri = Uri.fromFile(file);// parse(pathUri);13
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        // 设置为true直接返回bitmap
        intent.putExtra("return-data", false);
        // 上面设为false的时候将MediaStore.EXTRA_OUTPUT关联一个Uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, smallImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        ((Activity) context).startActivityForResult(intent, REQUEST_PICKER_AND_CROP);
    }

}
