package one.show.live.util;

import android.view.View;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public abstract class NoDoubleClickListener implements View.OnClickListener {

  /**
   * 控制不可连续点击的时间间隔[  修改控制时间间隔  ]
   */
  private int MIN_CLICK_DELAY_TIME = 2000;//大时间间隔用于测试
  /**
   * 上一次点击的时间
   */
  private long lastClickTime = 0;

  public int getDelayTime(){
    return MIN_CLICK_DELAY_TIME;
  }


  @Override
  public void onClick(View v) {
    long currentTime = Calendar.getInstance().getTimeInMillis();
    if (currentTime - lastClickTime > getDelayTime()) {
      lastClickTime = currentTime;
      onNoDoubleClick(v);
    }
  }

  public abstract void onNoDoubleClick(View view);
}
