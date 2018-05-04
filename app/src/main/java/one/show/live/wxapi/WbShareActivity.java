package one.show.live.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

import org.greenrobot.eventbus.EventBus;

import one.show.live.R;
import one.show.live.common.po.POEventBus;
import one.show.live.util.EventBusKey;

/**
 * Created by Nano on 2018/4/26.
 * 微博分享
 */

public class WbShareActivity extends Activity implements WbShareCallback {

    private WbShareHandler shareHandler;


    public static Intent getCallingIntent(Context context, String title, String content, String actionUrl, String imgePath) {
        Intent intent = new Intent(context, WbShareActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("content",content);
        bundle.putString("actionUrl",actionUrl);
        bundle.putString("imgePath",imgePath);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
        sendMultiMessage();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareHandler.doResultIntent(intent, this);
    }

    @Override
    public void onWbShareSuccess() {
        EventBus.getDefault()
                .post(new POEventBus(EventBusKey.EVENT_SHARE_BY_WEIBO_SUCCESS, null));
        finish();
    }

    @Override
    public void onWbShareCancel() {
        finish();
    }

    @Override
    public void onWbShareFail() {
        EventBus.getDefault()
                .post(new POEventBus(EventBusKey.EVENT_SHARE_BY_WEIBO_FAILED, null));
        finish();
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private void sendMultiMessage() {

        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

        weiboMessage.textObject = getTextObj();

        weiboMessage.imageObject = getImageObj();

        shareHandler.shareMessage(weiboMessage, false);

    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        StringBuffer buffer = new StringBuffer();
        if (!TextUtils.isEmpty(getIntent().getStringExtra("title")))
            buffer.append("#" + getIntent().getStringExtra("title") + "#");
        if (!TextUtils.isEmpty(getIntent().getStringExtra("content")))
            buffer.append(getIntent().getStringExtra("content"));
        if (!TextUtils.isEmpty(getIntent().getStringExtra("actionUrl")))
            buffer.append(getIntent().getStringExtra("actionUrl"));
        textObject.text = buffer.toString();
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }
}
