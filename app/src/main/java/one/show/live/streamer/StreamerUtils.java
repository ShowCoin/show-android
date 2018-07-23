package one.show.live.streamer;

import android.graphics.Matrix;

/**
 * Created by clarkM1ss1on on 2018/6/18
 */
public class StreamerUtils {

    public static byte[] rotateYUVDegree90(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate the Y luma
        int i = 0;
        for (int x = 0; x < imageWidth; x++) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }
        // Rotate the U and V color components
        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i--;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i--;
            }
        }
        return yuv;
    }

    public static byte[] rotateYUVDegree270(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate the Y luma
        int i = 0;
        for (int x = imageWidth - 1; x >= 0; x--) {
            for (int y = 0; y < imageHeight; y++) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }// Rotate the U and V color components
        i = imageWidth * imageHeight;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i++;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i++;
            }
        }
        return yuv;
    }

    public static byte[] rotateYUVDegree270AndMirror(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate and mirror the Y luma
        int i = 0;
        int maxY = 0;
        for (int x = imageWidth - 1; x >= 0; x--) {
            maxY = imageWidth * (imageHeight - 1) + x * 2;
            for (int y = 0; y < imageHeight; y++) {
                yuv[i] = data[maxY - (y * imageWidth + x)];
                i++;
            }
        }
        // Rotate and mirror the U and V color components
        int uvSize = imageWidth * imageHeight;
        i = uvSize;
        int maxUV = 0;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            maxUV = imageWidth * (imageHeight / 2 - 1) + x * 2 + uvSize;
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[maxUV - 2 - (y * imageWidth + x - 1)];
                i++;
                yuv[i] = data[maxUV - (y * imageWidth + x)];
                i++;
            }
        }
        return yuv;
    }

    public static void upSideDown(byte[] srcData, byte[] dstData, int width, int height) {
        int dstLineOffset, srcLineOffset;
        for (int i = 0; i < height; i++) {
            dstLineOffset = i * width;
            srcLineOffset = (height - i - 1) * width;
            System.arraycopy(srcData, srcLineOffset, dstData, dstLineOffset, width);
        }
    }

    public static Matrix getPreviewTextureAdaptiveMatrix(int previewWidth, int previewHeight
            , int cameraWidth, int cameraHeight) {
        double dstAspect = (double) previewWidth / previewHeight;
        double srcAspect = (double) cameraWidth / cameraHeight;
        float scale;
        float offsetX = 0f;
        float offsetY = 0f;
        if (srcAspect > dstAspect) {
            scale = previewHeight / cameraHeight;
            offsetX = (float) Math.ceil((scale * cameraWidth - previewWidth) / 2);
        } else {
            scale = previewWidth / cameraWidth;
            offsetY = (float) Math.ceil((scale * cameraHeight - previewHeight) / 2);
        }
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        matrix.postTranslate(offsetX, offsetY);
        return matrix;
    }


//    public int getImageAngleByCamera(int camera) {
//        switch (camera) {
//            case Streamer.CAMERA_BACK:
//                return 270;
//            case Streamer.CAMERA_FRONT:
//            default:
//                return 90;
//        }
//    }

}
