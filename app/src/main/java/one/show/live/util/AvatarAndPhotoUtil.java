package one.show.live.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.FrescoUtils;
import one.show.live.log.Logger;
import one.show.live.widget.ActionSheetDialog;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Nano
 */

public class AvatarAndPhotoUtil {

    private Uri imageUri;

    private static final int PERMISSIONS_REQUEST_CAMERA = 0x01 << 4;
    private static final int PERMISSIONS_REQUEST_GALLERY = 0x01 << 5;

    private static final int PHOTO_REQUEST_CAMERA = 0x01 << 9;//上传相册调取拍照
    private static final int PHOTO_REQUEST_ALBUM = 0x01 << 10;//上传相册调取拍照

    Context context;

    public AvatarAndPhotoUtil(Context context, OnPathLisenter onUriLisenter) {
        this.context = context;
        this.onPathLisenter = onUriLisenter;
        setPhotoPath();
    }

    /**
     * 设置图片的路径
     */
    private void setPhotoPath() {
        String path = one.show.live.common.util.FileUtils.getCacheDiskPath( "upload").getAbsolutePath();
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
        ((Activity)context).startActivityForResult(intent, PHOTO_REQUEST_ALBUM);
    }


    /**
     * 压缩图片
     *
     * @param oldFile
     */
    public void compressionPhoto(String oldFile) {

        String path = one.show.live.common.util.FileUtils.getCacheDiskPath( "upload").getAbsolutePath() + File.separator;

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
                        onPathLisenter.onPath(file.getPath());
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

                    String path = PhotoTool.rotateBitmapByDegree(imageUri, degree);

                    compressionPhoto(path);
                }
                break;
            case PHOTO_REQUEST_ALBUM:

                int degree = PhotoTool.getBitmapDegree(data.getData().getPath());

                String path = PhotoTool.rotateBitmapByDegree(data.getData(), degree);

                compressionPhoto(path);
                break;
        }
    }

    OnPathLisenter onPathLisenter;

    public interface OnPathLisenter {
        void onPath(String path);
    }

}
