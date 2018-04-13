package one.show.live.common.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import java.util.List;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public class CustomFragmentActivity extends FragmentActivity {
  private static final String TAG = "BaseActivity";

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    FragmentManager fm = getSupportFragmentManager();
    int index = requestCode >> 16;
    if (index != 0) {
      index--;
      if (fm.getFragments() == null || index < 0 || index >= fm.getFragments().size()) {
        Log.w(TAG,
            "Activity result fragment index out of range: 0x" + Integer.toHexString(requestCode));
        return;
      }
      List<Fragment> list = fm.getFragments();
      if(list != null && list.size() > 0){
        for(Fragment frag : list){
          //Fragment frag = fm.getFragments().get(index);
          if (frag == null) {
            Log.w(TAG,
                "Activity result no fragment exists for index: 0x" + Integer.toHexString(requestCode));
          } else {
            handleResult(frag, requestCode, resultCode, data);
          }
        }
      }
      return;
    }
  }

  /**
   * 递归调用，对所有子Fragement生效
   */
  private void handleResult(Fragment frag, int requestCode, int resultCode, Intent data) {
    frag.onActivityResult(requestCode & 0xffff, resultCode, data);
    List<Fragment> frags = frag.getChildFragmentManager().getFragments();
    if (frags != null) {
      for (Fragment f : frags) {
        if (f != null) handleResult(f, requestCode, resultCode, data);
      }
    }
  }
}
