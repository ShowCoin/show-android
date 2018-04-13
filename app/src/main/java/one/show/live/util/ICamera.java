package one.show.live.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.view.Surface;
import android.view.TextureView;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 照相机工具类
 */
public class ICamera {

	private static final String TAG = "ICamera";

	public Camera mCamera;
	public int cameraWidth;
	public int cameraHeight;
	public boolean isBackCamera = false;
	public int angle;

	public ICamera() {
	}

	/**
	 * 打开相机
	 */
	public Camera openCamera(Activity activity) {
		try {

			int cameraId = getCameraDirection();

			mCamera = Camera.open(cameraId);
			CameraInfo cameraInfo = new CameraInfo();
			Camera.getCameraInfo(cameraId, cameraInfo);
			Camera.Parameters params = mCamera.getParameters();
			Camera.Size bestPreviewSize = calBestPreviewSize(
					mCamera.getParameters(), 1280, 720);
			cameraWidth = bestPreviewSize.width;
			cameraHeight = bestPreviewSize.height;
			params.setPreviewSize(cameraWidth, cameraHeight);
			mCamera.setDisplayOrientation(getCameraAngle(activity, cameraId));
			List<String> focusModes = params.getSupportedFocusModes();
			if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
				params
						.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
			}

			mCamera.setParameters(params);

			Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
			return mCamera;
		} catch (Exception e) {
			return null;
		}
	}

	private int getCameraDirection(){
		int cameraId = 0;
		if (isBackCamera)
			cameraId = 0;
		else{
			if (hasFrontFacingCamera()) {
				isBackCamera = false;
				cameraId = 1;
			} else {
				isBackCamera = true;
				cameraId = 0;
			}
		}

		return cameraId;
	}

	// 通过屏幕参数、相机预览尺寸计算布局参数
	public RelativeLayout.LayoutParams getLayoutParam() {
		Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
		float scale = Math.max(ScreenUtil.mWidth * 1.0f / previewSize.height,
				ScreenUtil.mHeight * 1.0f / previewSize.width);
		int layout_width = (int) (scale * previewSize.height);
		int layout_height = (int) (scale * previewSize.width);

		RelativeLayout.LayoutParams layout_params = new RelativeLayout.LayoutParams(
				layout_width, layout_height);

		return layout_params;
	}

	/**
	 * 转换摄像头
	 */
	public Camera changeCameraOnline(Activity activity, TextureView textureView) {
		closeCamera();
		isBackCamera = !isBackCamera;
		return showPreview(activity, textureView);
	}

	public Camera showPreview(Activity activity, TextureView textureView){
		mCamera = openCamera(activity);
		startPreview(textureView.getSurfaceTexture());
		textureView.setLayoutParams(getLayoutParam());

		return mCamera;
	}

	/**
	 * 开始检测脸
	 */
	public void setPreviewCallback(Camera.PreviewCallback mActivity) {
		try{
			if (mCamera != null) {
				mCamera.setPreviewCallback(mActivity);
			}
		} catch(Exception e){}
	}

	public void startPreview(SurfaceTexture surfaceTexture) {
		if (mCamera != null) {
			try {
				mCamera.setPreviewTexture(surfaceTexture);
				mCamera.startPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void closeCamera() {
		try{
			if (mCamera != null) {
				mCamera.stopPreview();
				mCamera.setPreviewCallback(null);
				mCamera.release();
				mCamera = null;
			}
		}catch (Exception e){}
	}

	/**
	 * 通过传入的宽高算出最接近于宽高值的相机大小
	 */
	private Camera.Size calBestPreviewSize(Camera.Parameters camPara,
                                           final int width, final int height) {
		List<Camera.Size> allSupportedSize = camPara.getSupportedPreviewSizes();
		ArrayList<Camera.Size> widthLargerSize = new ArrayList<Camera.Size>();
		for (Camera.Size tmpSize : allSupportedSize) {
			if (tmpSize.width > tmpSize.height) {
				widthLargerSize.add(tmpSize);
			}
		}

		Collections.sort(widthLargerSize, new Comparator<Camera.Size>() {
			@Override
			public int compare(Camera.Size lhs, Camera.Size rhs) {
				int off_one = Math.abs(lhs.width * lhs.height - width * height);
				int off_two = Math.abs(rhs.width * rhs.height - width * height);
				return off_one - off_two;
			}
		});

		return widthLargerSize.get(0);
	}

	/**
	 * 打开前置或后置摄像头
	 */
	public Camera getCameraSafely(int cameraId) {
		Camera camera = null;
		try {
			camera = Camera.open(cameraId);
		} catch (Exception e) {
			camera = null;
		}
		return camera;
	}

	public RelativeLayout.LayoutParams getParams() {
		if (mCamera == null)
			return new RelativeLayout.LayoutParams(0, 0);

		Camera.Parameters camPara = mCamera.getParameters();
		// 注意Screen是否初始化
		Camera.Size bestPreviewSize = calBestPreviewSize(camPara,
				ScreenUtil.mWidth, ScreenUtil.mHeight);
		cameraWidth = bestPreviewSize.width;
		cameraHeight = bestPreviewSize.height;
		camPara.setPreviewSize(cameraWidth, cameraHeight);
		mCamera.setParameters(camPara);

		float scale = bestPreviewSize.width / bestPreviewSize.height;

		RelativeLayout.LayoutParams layoutPara = new RelativeLayout.LayoutParams(
				(int) (bestPreviewSize.width),
				(int) (bestPreviewSize.width / scale));

		layoutPara.addRule(RelativeLayout.CENTER_HORIZONTAL);// 设置照相机水平居中
		return layoutPara;
	}

	public Bitmap getBitMap(byte[] data, Camera camera, boolean mIsFrontalCamera) {
		int width = camera.getParameters().getPreviewSize().width;
		int height = camera.getParameters().getPreviewSize().height;
		YuvImage yuvImage = new YuvImage(data, camera.getParameters()
				.getPreviewFormat(), width, height, null);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		yuvImage.compressToJpeg(new Rect(0, 0, width, height), 80,
				byteArrayOutputStream);
		byte[] jpegData = byteArrayOutputStream.toByteArray();
		// 获取照相后的bitmap
		Bitmap tmpBitmap = BitmapFactory.decodeByteArray(jpegData, 0,
				jpegData.length);
		Matrix matrix = new Matrix();
		matrix.reset();
		if (mIsFrontalCamera) {
			matrix.setRotate(-90);
		} else {
			matrix.setRotate(90);
		}
		tmpBitmap = Bitmap.createBitmap(tmpBitmap, 0, 0, tmpBitmap.getWidth(),
				tmpBitmap.getHeight(), matrix, true);
		tmpBitmap = tmpBitmap.copy(Bitmap.Config.ARGB_8888, true);

		int hight = tmpBitmap.getHeight() > tmpBitmap.getWidth() ? tmpBitmap
				.getHeight() : tmpBitmap.getWidth();

		float scale = hight / 800.0f;

		if (scale > 1) {
			tmpBitmap = Bitmap.createScaledBitmap(tmpBitmap,
					(int) (tmpBitmap.getWidth() / scale),
					(int) (tmpBitmap.getHeight() / scale), false);
		}
		return tmpBitmap;
	}

	/**
	 *获取照相机旋转角度 
	 */
	public int getCameraAngle(Activity activity, int cameraId) {
		int rotateAngle = 90;
		CameraInfo info = new CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
			rotateAngle = (info.orientation + degrees) % 360;
			rotateAngle = (360 - rotateAngle) % 360; // compensate the mirror
		} else { // back-facing
			rotateAngle = (info.orientation - degrees + 360) % 360;
		}

		angle = rotateAngle;
		return rotateAngle;
	}


	public void autoFocus() {
		try{
			if (isBackCamera && mCamera != null) {
				mCamera.cancelAutoFocus();
				Camera.Parameters parameters = mCamera.getParameters();
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
				mCamera.setParameters(parameters);
				mCamera.autoFocus(null);
			}
		}catch (Exception e){}
	}

	public boolean hasBackFacingCamera() {
		final int CAMERA_FACING_BACK = 0;
		return checkCameraFacing(CAMERA_FACING_BACK);
	}

	public boolean hasFrontFacingCamera() {
		final int CAMERA_FACING_BACK = 1;
		return checkCameraFacing(CAMERA_FACING_BACK);
	}

	private  boolean checkCameraFacing(final int facing) {
		if (getSdkVersion() < Build.VERSION_CODES.GINGERBREAD) {
			return false;
		}
		final int cameraCount = Camera.getNumberOfCameras();
		CameraInfo info = new CameraInfo();
		for (int i = 0; i < cameraCount; i++) {
			Camera.getCameraInfo(i, info);
			if (facing == info.facing) {
				return true;
			}
		}
		return false;
	}

	public int getSdkVersion() {
		return Build.VERSION.SDK_INT;
	}
}