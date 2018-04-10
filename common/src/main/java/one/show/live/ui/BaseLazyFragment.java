package one.show.live.ui;

import android.os.Bundle;

public abstract class BaseLazyFragment extends BaseFragment {

    private boolean isPrepared;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    private boolean isFirstResume = true;


    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible(true);
        }
    }


    private boolean isFirstVisible = true;
    private boolean isFirstInVisible = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible(false);
            }
        } else {
            if (isFirstInVisible) {
                isFirstInVisible = false;
                return;
            }
            onUserInvisible(false);
        }
    }


    public synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }


    public abstract void onFirstUserVisible();

    /**
     * fragment可见（切换回来或者onResume）
     */
    public abstract void onUserVisible(boolean isResume);

    /**
     * /**
     * fragment不可见（切换掉或者onPause）
     */
    public abstract void onUserInvisible(boolean isPause);

}