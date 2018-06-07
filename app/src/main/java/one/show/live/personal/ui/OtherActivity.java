package one.show.live.personal.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import one.show.live.R;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.home.ui.MeFragment;

/**
 * Created by Nano on Show.
 * 他人的个人中心
 */

public class OtherActivity extends BaseFragmentActivity {


    public static Intent getCallingIntent(Context context, String uid) {

        Intent intent = new Intent(context, OtherActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        intent.putExtras(bundle);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        initTransparentWindow();
        openFragment(MeFragment.newInstance(getIntent().getExtras().getString("uid"),MeFragment.OTHER));
    }


    public void openFragment(Fragment mFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.login_frame, mFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
