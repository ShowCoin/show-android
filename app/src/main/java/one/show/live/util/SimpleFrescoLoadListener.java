package one.show.live.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.image.CloseableImage;

import one.show.live.util.FrescoUtils;

public abstract class SimpleFrescoLoadListener implements FrescoUtils.OnLoadListener {
	@Override
	public boolean onFullResult(Context context, Uri uri, DataSource<CloseableReference<CloseableImage>> dataSource) {
		return false;
	}

	@Override
	public void onResult(Context context, Uri uri, Bitmap bitmap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(Context context, Uri uri, Throwable throwable) {
		// TODO Auto-generated method stub

	};

}