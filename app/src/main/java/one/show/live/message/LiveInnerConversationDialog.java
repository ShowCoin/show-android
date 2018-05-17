package one.show.live.message.ui;

import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import butterknife.BindView;
import one.show.live.R;
import one.show.live.common.ui.BaseDialogFragment;
import one.show.live.message.presenter.ConversationPresenter;
import one.show.live.message.view.IConversationView;

/**
 * Created by clarkM1ss1on on 2018/5/16
 */
public class LiveInnerConversationDialog
        extends BaseDialogFragment implements IConversationView {

    public final static String ARG_TARGET_ID = "targetId";
    public final static String TAG = "LiveInnerConversationDialog";

//    @BindView(R.id.messages_title)
//    TitleView titleView;
//
//    @BindView(R.id.messages_cv_list)
//    RecyclerView messageListView;

//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setupWindow();
//        setContentView(R.layout.activity_conversation);
//        setupTitleView();
//    }


    @BindView(R.id.bottomBar)
    View bottomBar;


    private ConversationPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater
                .inflate(R.layout.activity_conversation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new ConversationPresenter(this);
        setupEditText();
    }

    @Override
    public void onStart() {
        super.onStart();
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
        setupWindow();
    }

    private void setupWindow() {

        final Window window = getDialog()
                .getWindow();
        int windowHeight = getResources()
                .getDimensionPixelSize(R.dimen.live_inner_conversation_window_height);
        final WindowManager.LayoutParams lp = window
                .getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = windowHeight;
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawable(null);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void setupEditText() {
//        KeyboardVisibilityEvent.registerEventListener(getActivity(), presenter.getKeyboardVisibilityEventListener());

        getView().getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
//                        final Rect rect = new Rect();
//                        getView().getWindowVisibleDisplayFrame(rect);
//                        int bottomGap = getView().getRootView().getHeight()
//                                - rect.bottom;
//                        if (bottomGap > 100) {
//                            adjustBottomMargin(bottomGap);
//                        } else {
//                            adjustBottomMargin(0);
//                        }
                    }
                });


    }

    private void adjustBottomMargin(int margin) {

        final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) bottomBar
                .getLayoutParams();
        lp.bottomMargin = margin;
        bottomBar.setLayoutParams(lp);
//        if (null != getDialog()) {

//            final Window window = getDialog()
//                    .getWindow();
//            final WindowManager.LayoutParams lp  = window
//                    .getAttributes();
//
//        }
    }

//    private void setupTitleView() {
////        titleView.setTitle(R.string.message);
//    }

    public static Bundle getCallingBundle(String targetId) {
        final Bundle bundle = new Bundle();
        bundle.putString(ARG_TARGET_ID, targetId);
        return bundle;
    }

    @Override
    public void switchToVoiceMsgMode() {

    }

    @Override
    public void switchToTypingMsgMode() {

    }

    @Override
    public String getTextMsg() {
        return null;
    }

    @Override
    public void clearTypeModeInput() {

    }

    @Override
    public void scrollToBottom(boolean smooth) {

    }

    @Override
    public void setTitleText(String text) {

    }

    @Override
    public void setTitleText(int stringRes) {

    }

    @Override
    public void setVoiceInputStatus(int status) {

    }

    @Override
    public void showBottomPanel() {

    }

    @Override
    public void hideBottomPanel() {

    }

    @Override
    public boolean isBottomPanelVisible() {
        return false;
    }

    @Override
    public void moveToZoomView(View v, Uri remoteUri) {

    }

    @Override
    public void updateTitleText(String txt) {

    }

    @Override
    public void invokeRefreshComplete() {

    }

    @Override
    public void scrollTo(int position, boolean smooth) {

    }


    @Override
    public void adjustWindow() {
        if (null != getDialog()) {
//            final Window window = getDialog().getWindow();
//            int height = getResources().getDimensionPixelSize(R.dimen.live_inner_conversation_window_height)
//                    + getResources().getDimensionPixelSize(R.dimen.live_inner_conversation_window_adjust_margin);
//            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height);
        }
    }

    @Override
    public void restoreWindow() {
        if (null != getDialog()) {
//            final Window window = getDialog().getWindow();
//            int height = getResources().getDimensionPixelSize(R.dimen.live_inner_conversation_window_height);
//            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height);
        }
    }
}
