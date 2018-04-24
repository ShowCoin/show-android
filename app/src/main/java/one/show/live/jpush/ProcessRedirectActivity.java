package one.show.live.jpush;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * 应用内或者push或者外部跳转的重定向中转页面
 */
public class ProcessRedirectActivity extends Activity {

    public static final String INTENT_KEY = "data";

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        finish();
    }

}
