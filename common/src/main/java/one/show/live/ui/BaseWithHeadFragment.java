package one.show.live.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import one.show.live.view.HeaderView;
import one.show.live.common.R;


/**
 * 带有title的Fragment基类，需要初始化
 *
 */
public abstract class BaseWithHeadFragment extends BaseFragment {
    protected HeaderView mHeadView;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if(rootView!=null){
            mHeadView = (HeaderView) rootView.findViewById(R.id.header_view);
            if (mHeadView != null) {
                initHeadView();
            }
        }

        return rootView;
    }

    protected abstract void initHeadView();

}
