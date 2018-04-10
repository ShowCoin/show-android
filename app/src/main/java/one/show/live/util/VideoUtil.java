package one.show.live.util;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;

/**
 * Created by wyc on 17-8-17.
 */

public class VideoUtil {

    private static final String TAG = "VideoUtil";

    /**
     * 获取视频某一帧
     *
     * @param timeMs 毫秒
     */
    public static Bitmap getLocalVideoFrame(String filePath, long timeMs) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(filePath);
        if (timeMs == 0) //这样娶不到黑屏
            return retriever.getFrameAtTime();
        else
            return retriever.getFrameAtTime(timeMs * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
    }

    /**
     * 获取本地视频的第一帧
     *
     * @param filePath
     * @return
     */
    public static Bitmap getLocalVideoThumbnail(String filePath) {
        return getLocalVideoFrame(filePath, 0);
//        Bitmap bitmap = null;
//        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
//        //的接口，用于从输入的媒体文件中取得帧和元数据；
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        try {
//
//            Log.e(TAG, "filePath=" + filePath);
//            //根据文件路径获取缩略图
//            retriever.setDataSource(filePath);
//            //获得第一帧图片
//            bitmap = retriever.getFrameAtTime();
//            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
////            String width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度
////            String height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
////            String rotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION); // 视频旋转方向
////
////            Log.e(TAG, "getLocalVideoThumbnail: width ==" + width + ", " + height + ", " + rotation);
////            int w = bitmap.getWidth();
////            int h = bitmap.getHeight();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } finally {
//            retriever.release();
//        }
//        return bitmap;
    }




    /**
     * 获取本地视频的bitRate
     *
     * @param filePath
     * @return
     */
    public static int getVideoBitRate(String filePath) {
        int bitRate = 0;
        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
        //的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource(filePath);
            //转为kb
            bitRate = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)) / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitRate;
    }


    /**
     * 获取本地视频的宽高
     *
     * @return
     */
    public static boolean getSmallWidthHeight(String filePath) {
        try {
            //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
            //的接口，用于从输入的媒体文件中取得帧和元数据；
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            //根据文件路径获取缩略图
            retriever.setDataSource(filePath);
            String width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            String height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            retriever.release();
            if (width != null && height != null) {
                int videoWidth = Integer.parseInt(width);
                int videoHeight = Integer.parseInt(height);
            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 获取视频的时间长度
     */
    public static long getVideoDuration(String videoPath) {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.prepare();
            long duration = mediaPlayer.getDuration();
            mediaPlayer.stop();
            mediaPlayer.release();
            return duration;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public interface VideoClipListener {
        public void videoClipComplete(String path, int size, int index);
    }

    /**
     * 获取本地视频的宽高
     *
     * @return
     */
    public static boolean getLocalVideoWidthHeight(String path) {
        try {
            //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
            //的接口，用于从输入的媒体文件中取得帧和元数据；
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            //根据文件路径获取缩略图
            retriever.setDataSource(path);
            String width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            String height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            String rotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
            retriever.release();
            if (width != null && height != null) {
                int _width = Integer.parseInt(width);
                int _height = Integer.parseInt(height);
                // float _videoScale = bean.width * 1.0f / bean.height;
                float _videoRotation = Integer.parseInt(rotation); // 视频旋转方向
                return true;
            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
