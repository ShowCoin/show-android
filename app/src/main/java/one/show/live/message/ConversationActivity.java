package one.show.live.message.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import butterknife.BindView;
import one.show.live.R;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.DeviceUtils;
import one.show.live.message.presenter.ConversationPresenter;
import one.show.live.message.view.IConversationView;
import one.show.live.personal.ui.ZoomActivity;
import one.show.live.widget.TitleView;

/**
 * Created by clarkM1ss1on on 2018/4/28
 */
public class ConversationActivity
        extends BaseFragmentActivity
        implements IConversationView {

    public final static int INPUT_MODE_TYPE = 0;
    public final static int INPUT_MODE_VOICE = 1;


    @BindView(R.id.titleView)
    TitleView titleView;

    @BindView(R.id.messageList)
    RecyclerView messageList;

    @BindView(R.id.voiceMsgSwitchBtn)
    View voiceMsgSwitchBtn;

    @BindView(R.id.txtMsgEditText)
    EditText txtMsgEditText;

    @BindView(R.id.voiceMsgInputBtn)
    TextView voiceMsgInputBtn;

//    @BindView(R.id.emojiBtn)
//    View emojiBtn;

    @BindView(R.id.moreBtn)
    View moreBtn;

    @BindView(R.id.bottomPanelViewStub)
    ViewStub bottomPanelViewStub;

    private View bottomPanelView;

    private View imageEntryView;


    private ConversationPresenter presenter;

    private int inputMode = INPUT_MODE_TYPE;

    private int voiceBtnStatus = IConversationView.VOICE_STATUS_ACTION_UP;

    public static final String ARG_KEY_TARGET_UID = "uid";

    public static Intent getCallingIntent(Context ctx, String targetUid) {
        return new Intent(ctx, ConversationActivity.class)
                .putExtra(ARG_KEY_TARGET_UID, targetUid);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBarForLightTitle(Color.WHITE);
        setContentView(R.layout.activity_conversation);
        presenter = new ConversationPresenter(this);
        setupTitleView();
        setupRecyclerView();
        setupBottomBar();
        presenter.getMessage();
        presenter.requestTargetUserInfo();
    }

    private void setupRecyclerView() {
        messageList.setLayoutManager(new LinearLayoutManager(this));
        messageList.setAdapter(presenter.getAdapter());
        messageList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftInput();
                if (null != bottomPanelView) {
                    bottomPanelView.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }

    private void setupTitleView() {
        titleView.setTitle(R.string.conversation)
                .setTextColor(ContextCompat.getColor(this, R.color.black));
        titleView.setLayBac(R.color.color_ffffff);
        titleView.setLeftImage(R.drawable.back_black, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                hideSoftInput();
            }
        });

    }

    private void setupBottomBar() {
        voiceMsgSwitchBtn.setOnClickListener(presenter.getBottomBarFuncBtnClickListener());
//        emojiBtn.setOnClickListener(presenter.getBottomBarFuncBtnClickListener());
        moreBtn.setOnClickListener(presenter.getBottomBarFuncBtnClickListener());
        txtMsgEditText.setOnEditorActionListener(presenter.getOnEditorActionListener());
        txtMsgEditText.setOnClickListener(presenter.getInputEditorOnClickListener());
        voiceMsgInputBtn.setOnTouchListener(presenter.getVoiceMsgOnTouchListener());
        KeyboardVisibilityEvent.registerEventListener(this
                , presenter.getKeyboardVisibilityEventListener());
    }


    @Override
    protected void onStart() {
        super.onStart();
        presenter.initRecorder();
        presenter.initPlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.releaseRecorder();
        presenter.releasePlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.registerEvents();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unregisterEvent();
        hideSoftInput();
    }

    @Override
    public void switchToVoiceMsgMode() {
        inputMode = INPUT_MODE_VOICE;
        updateBottomBarStatus();
    }

    @Override
    public void switchToTypingMsgMode() {
        inputMode = INPUT_MODE_TYPE;
        updateBottomBarStatus();
    }

    @Override
    public String getTextMsg() {
        return txtMsgEditText
                .getText()
                .toString();
    }

    public void hideSoftInput() {
        txtMsgEditText.clearFocus();
        DeviceUtils.hideSoftInput(ConversationActivity.this, txtMsgEditText);
    }

    private void updateBottomBarStatus() {
        switch (inputMode) {
            case INPUT_MODE_VOICE:
                voiceMsgInputBtn.setVisibility(View.VISIBLE);
                txtMsgEditText.setVisibility(View.GONE);
                voiceMsgSwitchBtn.setSelected(true);
                DeviceUtils.hideSoftInput(this);
                break;
            default:
                voiceMsgInputBtn.setVisibility(View.GONE);
                txtMsgEditText.setVisibility(View.VISIBLE);
                voiceMsgSwitchBtn.setSelected(false);
                DeviceUtils.showSoftInput(this, txtMsgEditText);
                break;
        }
    }


    private void updateVoiceBtnStatus() {
        switch (voiceBtnStatus) {
            case VOICE_STATUS_ACTION_UP:
                voiceMsgInputBtn
                        .setText(R.string.press_to_record);
                voiceMsgInputBtn.setSelected(false);
                break;
            case VOICE_STATUS_ACTION_DOWN:
                voiceMsgInputBtn
                        .setText(R.string.release_to_send);
                voiceMsgInputBtn.setSelected(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void clearTypeModeInput() {
        txtMsgEditText.setText(null);
    }

    @Override
    public void scrollToBottom(final boolean smooth) {
        final int bottomIndex = presenter
                .getAdapter()
                .getItemCount() - 1;
        if (smooth && !isFinishing()) {
            messageList.smoothScrollToPosition(bottomIndex);
        } else {
            messageList.scrollToPosition(bottomIndex);
        }
    }

    @Override
    public void setTitleText(String text) {
        titleView.setTitle(text);
    }

    @Override
    public void setTitleText(int stringRes) {
        titleView.setTitle(getString(stringRes));
    }

    @Override
    public void setVoiceInputStatus(int status) {
        this.voiceBtnStatus = status;
        updateVoiceBtnStatus();
    }


    private void loadBottomPanelLayout() {
        if (null == bottomPanelViewStub
                .getParent()) {
            return;
        }
        bottomPanelView = bottomPanelViewStub
                .inflate();
        imageEntryView = bottomPanelView
                .findViewById(R.id.imageEntry);
        final ImageView iconView = imageEntryView
                .findViewById(R.id.panelItemIcon);
        iconView.setImageResource(R.drawable.selector_conversation_panel_image);
        final TextView entryTextView = imageEntryView
                .findViewById(R.id.panelItemText);
        entryTextView.setText(R.string.image);
        imageEntryView.setOnClickListener(presenter.getImageEntryOnClickListener());
    }


    @Override
    public void showBottomPanel() {
        loadBottomPanelLayout();
        bottomPanelView.setVisibility(View.VISIBLE);
        DeviceUtils.hideSoftInput(this, txtMsgEditText);
        scrollToBottom(true);
    }

    @Override
    public void hideBottomPanel() {
        if (null != bottomPanelView) {
            bottomPanelView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isBottomPanelVisible() {
        if (null == bottomPanelView) {
            return false;
        } else {
            return bottomPanelView.getVisibility() == View.VISIBLE;
        }
    }

    @Override
    public void moveToZoomView(View view, Uri remoteUri) {
        Intent intent = new Intent();
        intent.setClass(this, ZoomActivity.class);
        intent.putExtras(createViewInfoBundle(view, remoteUri.toString()));
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void updateTitleText(String txt) {
        titleView.setTitle(txt)
                .setTextColor(ContextCompat.getColor(this, R.color.black));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.resultDelivery(requestCode, resultCode, data);
    }

    private Bundle createViewInfoBundle(View view, String uri) {
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);
        Bundle b = new Bundle();
        int left = screenLocation[0];
        int top = screenLocation[1];
        int width = view.getWidth();
        int height = view.getHeight();
        b.putInt("left", left);
        b.putInt("top", top);
        b.putInt("width", width);
        b.putInt("height", height);
        b.putString("img", uri);
        return b;
    }


    //    @Override
//    public void moveToPhotoPiker() {
//
//    }


}
