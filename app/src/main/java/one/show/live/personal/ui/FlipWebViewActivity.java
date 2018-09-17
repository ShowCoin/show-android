package one.show.live.personal.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.api.BaseRequest;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.AppUtil;
import one.show.live.widget.TitleView;


public class FlipWebViewActivity extends BaseFragmentActivity {


    private FlipViewController flipView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.activity_title);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        flipView = new FlipViewController(this, FlipViewController.HORIZONTAL);

        flipView.setAdapter(new MyBaseAdapter(this, flipView));

        setContentView(flipView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flipView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        flipView.onPause();
    }

    private static class MyBaseAdapter extends BaseAdapter {

        List<String> urls = new ArrayList<String>();
        FlipViewController controller;
        Activity activity;
        int activeLoadingCount = 0;

        private MyBaseAdapter(Activity activity, FlipViewController controller) {
            this.activity = activity;
            this.controller = controller;
        }

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            WebView webView = new WebView(controller.getContext());
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    activity.setProgressBarIndeterminateVisibility(true);
                    activeLoadingCount++;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    controller.refreshPage(
                            view);//This works as the webView is the view for a page. Please use refreshPage(int pageIndex) if the webview is only a part of page view.

                    activeLoadingCount--;
                    activity.setProgressBarIndeterminateVisibility(activeLoadingCount == 0);
                }
            });

            webView.setWebChromeClient(new WebChromeClient() {
                private int lastRefreshProgress = 0;

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress - lastRefreshProgress
                            > 20) { //limit the invocation frequency of refreshPage
                        controller.refreshPage(view);
                        lastRefreshProgress = newProgress;
                    }
                }
            });

            webView.loadUrl(urls.get(position));

            return webView;
        }
    }
}
}
