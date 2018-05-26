package one.show.live.upload.biz.avatarupload;

import android.content.Context;

import one.show.live.common.po.POMember;
import one.show.live.po.eventbus.EUpdateAvatar;
import one.show.live.upload.VODUploadCallback;
import one.show.live.upload.VODUploadClient;
import one.show.live.upload.VODUploadClientImpl;
import one.show.live.upload.model.UploadFileInfo;


/**
 * Created by samuel on 2018/5/6.
 * <p>
 * 头像上传工具类
 */
public class UploadAvatarUtils {

    private static VODUploadClient uploader;

    public static void uploadAvatar(Context context, String filePath,String smallPath) {
            startUploadWithAliYun(context, filePath,smallPath);
    }

    /**
     * 单例初始化阿里云上传
     *
     * @param context
     */
    private static void checkUploader(Context context) {
        if (uploader == null) {
            synchronized (UploadAvatarUtils.class) {
                if (uploader == null) {
                    uploader = new VODUploadClientImpl(context.getApplicationContext());

                    VODUploadCallback callback = new VODUploadCallback() {

                        @Override
                        public void onUploadSucceed(UploadFileInfo fileInfo) {
                            AvatarUploadInfo info = (AvatarUploadInfo) fileInfo;


                            if(info.isSmall()){
                                UploadFileInfo nextFileInfo = uploader.getNextUploadFileInfo(fileInfo);

                                        if(nextFileInfo instanceof AvatarUploadInfo){
                                            nextFileInfo.setSmallPath(fileInfo.getObject());
                                        }
                                return;
                            }

                            POMember.fixAvatar(info.getPoNew().getAvatar(),info.getPoNew().getLarge_avatar());

                            new EUpdateAvatar(info.getPoNew().getAvatar(),info.getPoNew().getLarge_avatar()).sendEvent();
                        }

                        @Override
                        public void onUploadFailed(UploadFileInfo fileInfo, String code, String message) {
//                            ToastUtils.showToast(message);
//                            AvatarUploadInfo info = (AvatarUploadInfo) fileInfo;
                        }

                        @Override
                        public void onUploadProgress(UploadFileInfo fileInfo, long bytesWritten, long totalSize) {
                        }

                        @Override
                        public void onUploadStarted(UploadFileInfo fileInfo) {

                        }
                    };

                    uploader.init(callback);
                }
            }
        }
    }

    private static void startUploadWithAliYun(Context context, String filePath,String smallPath) {
        checkUploader(context);
        AvatarUploadInfo info = new AvatarUploadInfo(filePath,false);
        AvatarUploadInfo smallInfo = new AvatarUploadInfo(smallPath,true);
        uploader.addFile(smallInfo);//先传裁剪的
        uploader.addFile(info);
        uploader.start();
    }


}
