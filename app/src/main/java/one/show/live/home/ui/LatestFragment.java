package one.show.live.home.ui;

import one.show.live.R;
import one.show.live.common.ui.BaseFragment;

/**
 * Created by Nano on 2018/4/8.
 * 首页最新
 */

public class LatestFragment extends BaseFragment{

    public static LatestFragment newInstance(){
        LatestFragment latestFragment = new LatestFragment();
        return latestFragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_latest;
    }
}
