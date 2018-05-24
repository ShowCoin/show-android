package one.show.live.personal.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;


import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.po.POMember;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.FrescoUtils;
import one.show.live.upload.biz.avatarupload.UploadAvatarUtils;
import one.show.live.util.AvatarAndPhotoTailoringUtil;
import one.show.live.widget.TitleView;

/**
 * Created by Nano on Show.
 * 上传页面
 */

public class UpDataHeadActivity extends BaseFragmentActivity implements AvatarAndPhotoTailoringUtil.OnPathLisenter{

    @BindView(R.id.updata_head_img)
    SimpleDraweeView updataHeadImg;
    @BindView(R.id.updata_head_title)
    TitleView updataHeadTitle;
    AvatarAndPhotoTailoringUtil avatarAndPhotoUtil;

    String imagePath;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, UpDataHeadActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updtahead);
        initBaseView();
    }

    private void initBaseView() {

        updataHeadTitle.setTitle(getString(R.string.picture))
                .setTextColor(ContextCompat.getColor(this, R.color.color_333333));
        updataHeadTitle.setLayBac(R.color.color_ffffff);
        updataHeadTitle.setLeftImage(R.drawable.back_black);

        avatarAndPhotoUtil = new AvatarAndPhotoTailoringUtil(this, this);

        if (TextUtils.isEmpty(POMember.getInstance().getLarge_avatar())) {
            FrescoUtils.bind(updataHeadImg, POMember.getInstance().getAvatar());
        } else {
            FrescoUtils.bind(updataHeadImg, POMember.getInstance().getLarge_avatar());
        }

    }

    @OnClick({R.id.updata_head_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.updata_head_img:
                avatarAndPhotoUtil.iosDialogButtomImage();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        avatarAndPhotoUtil.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPath(String path,String smallPath) {
        if (isContextAlive()) {
            imagePath = path;
            FrescoUtils.bind(updataHeadImg, Uri.parse("file://"+path));
            startUploadAvatar(path,smallPath);
        }
    }

    /**
     * 上传头像
     *
     * @param filePath
     */
    public void startUploadAvatar(String filePath,String smallPath) {
        UploadAvatarUtils.uploadAvatar(UpDataHeadActivity.this, filePath,smallPath);
    }
}
