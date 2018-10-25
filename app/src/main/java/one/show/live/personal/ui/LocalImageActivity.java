package one.show.live.personal.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.api.BaseRequest;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.AppUtil;
import one.show.live.widget.TitleView;


public class LocalImageActivity extends BaseFragmentActivity {


    private List<String> images;

    @Override
    protected int getContentView() {
        return R.layout.activity_grid_view;
    }

    @Override
    protected void initView() {
        gvImages = (GridView) findViewById(R.id.gv_images);
    }

    @Override
    protected void testTransferee() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE);
        } else {
            images = ImageConfig.getLatestPhotoPaths(this, 9);
            if (images != null && !images.isEmpty()) {
                initTransfereeConfig();
                gvImages.setAdapter(new LocalImageActivity.NineGridAdapter());
            }
        }
    }

    private void initTransfereeConfig() {
        config = TransferConfig.build()
                .setSourceImageList(images)
                .setThumbnailImageList(images)
                .setMissPlaceHolder(R.mipmap.ic_empty_photo)
                .setErrorPlaceHolder(R.mipmap.ic_empty_photo)
                .setProgressIndicator(new ProgressBarIndicator())
                .setIndexIndicator(new NumberIndexIndicator())
                .setJustLoadHitImage(true)
                .bindListView(gvImages, R.id.iv_thum);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE) {
            images = ImageConfig.getLatestPhotoPaths(this, 9);
            if (images != null && !images.isEmpty()) {
                initTransfereeConfig();
                gvImages.setAdapter(new LocalImageActivity.NineGridAdapter());
            }
        } else {
        }
    }

    private class NineGridAdapter extends CommonAdapter<String> {

        public NineGridAdapter() {
            super(LocalImageActivity.this, R.layout.item_image, images);
        }

        @Override
        protected void convert(ViewHolder viewHolder, String item, final int position) {
            final ImageView imageView = viewHolder.getView(R.id.iv_thum);
            ImageLoader.getInstance().displayImage(item, imageView, options);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    config.setNowThumbnailIndex(position);
                    transferee.apply(config).show();
                }
            });
        }
    }
}
