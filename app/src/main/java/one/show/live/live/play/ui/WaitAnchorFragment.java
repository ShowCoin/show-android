package one.show.live.live.play.ui;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.show.live.R;
import one.show.live.ui.BaseFragment;

public class WaitAnchorFragment extends BaseFragment {

    @BindView(R.id.wait_image)
    ImageView waitImage;

    private AnimationDrawable animationDrawable;

    @Override
    protected int getContentView() {
        return R.layout.fragment_wait_anchor;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView() {
        super.initView();
        animationDrawable = (AnimationDrawable) waitImage.getDrawable();
        animationDrawable.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        animationDrawable.stop();
    }
}
