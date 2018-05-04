package one.show.live.util;

import android.view.View;

import java.util.Calendar;

/**
 * Created by Administrator on ..7/12 0012.
 */
public abstract class CheckOnDoubleClickUtils{

  /**
   * 控制不可连续点击的时间间隔[  修改控制时间间隔  ]
   */
  private static int MIN_CLICK_DELAY_TIME = 1000;//大时间间隔用于测试
  /**
   * 上一次点击的时间
   */
  private static long lastClickTime = 0;

  public static void onClick(View.OnClickListener clickListener) {
    long currentTime = Calendar.getInstance().getTimeInMillis();
    if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
      lastClickTime = currentTime;
      clickListener.onClick(null);
    }
  }

}
