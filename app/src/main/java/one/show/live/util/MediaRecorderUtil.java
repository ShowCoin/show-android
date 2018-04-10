package one.show.live.util;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;

import java.io.IOException;

public class MediaRecorderUtil {

	private MediaRecorder mMediaRecorder;
	private Camera mCamera;
	private String VideoPath;

	public MediaRecorderUtil(Camera mCamera, String videoPath) {
		this.mCamera = mCamera;
		this.VideoPath = videoPath;
		mMediaRecorder = new MediaRecorder();
	}

	/**
	 *开始录制视频
	 */
	public boolean start() {
		if (mMediaRecorder == null)
			return false;
		try{
			mMediaRecorder.start();
			return true;
		}catch (Exception e){}

		return false;
	}

	/**
	 *设置录制视频参数 一定要在 start()方法之前调用
	 */
	public boolean prepareVideoRecorder(int cameraWidth, int cameraHeight, int angle) {
		// Step 1: Unlock and set camera to MediaRecorder
		mCamera.unlock();
		mMediaRecorder.setCamera(mCamera);

		if (angle == 360)
			angle = 0;
		mMediaRecorder.setOrientationHint(angle);
		// Step 2: Set sources
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		CamcorderProfile camcorderProfile = CamcorderProfile
				.get(CamcorderProfile.QUALITY_HIGH);
		camcorderProfile.videoFrameWidth = cameraWidth;
		camcorderProfile.videoFrameHeight = cameraHeight;
		// Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
		mMediaRecorder.setProfile(camcorderProfile);
		mMediaRecorder.setVideoEncodingBitRate(2400000);
		// 每秒 4帧
//		mMediaRecorder.setVideoFrameRate(35);

		// mMediaRecorder.setVideoFrameRate(5);
		// mMediaRecorder.setCaptureRate(5);
		// mMediaRecorder.setVideoSize(320, 240);
		// Step 4: Set output file
		mMediaRecorder.setOutputFile(VideoPath);
		// Step 5: Prepare configured MediaRecorder
		try {
			mMediaRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			releaseMediaRecorder();
			return false;
		}
		return true;
	}

	/**
	 * 释放录制视频
	 * 录制结束后一定要释放资源
	 */
	public void releaseMediaRecorder() {
		if (mMediaRecorder != null) {
			// clear recorder configuration
			mMediaRecorder.reset();
			// release the recorder object
			mMediaRecorder.release();
			mMediaRecorder = null;
			// Lock camera for later use i.e taking it back from MediaRecorder.
			// MediaRecorder doesn't need it anymore and we will release it if
			// the activity pauses.
			mCamera.lock();
			mCamera = null;
		}
	}

}
