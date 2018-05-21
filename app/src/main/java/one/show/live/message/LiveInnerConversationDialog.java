package one.show.live.message.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrFrameLayout;
import one.show.live.R;
import one.show.live.common.ui.BaseDialogFragment;
import one.show.live.common.util.DeviceUtils;
import one.show.live.common.util.StatusBarUtil;
import one.show.live.common.view.RefreshHeadImage;
import one.show.live.log.Logger;
import one.show.live.message.presenter.ConversationPresenter;
import one.show.live.message.view.IConversationView;
import one.show.live.personal.ui.ZoomActivity;
import one.show.live.util.DialogUtil;
import one.show.live.util.PhotoTool;
import one.show.live.widget.ActionSheetDialog;
import one.show.live.widget.TitleView;

/**
 * Created by clarkM1ss1on on 2018/5/16
 */
public class LiveInnerConversationDialog
        extends BaseDialogFragment implements IConversationView {

    //    public final static String ARG_TARGET_ID = "targetId";
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

    @BindView(R.id.ptrFrame)
    PtrFrameLayout ptrFrame;

//    @BindView(R.id.emojiBtn)
//    View emojiBtn;

    @BindView(R.id.moreBtn)
    View moreBtn;

    @BindView(R.id.bottomPanelViewStub)
    ViewStub bottomPanelViewStub;


    private View bottomPanelView;

    private View imageEntryView;

    private PhotoTool photoTool;

    private ConversationPresenter presenter;

    private int inputMode = INPUT_MODE_TYPE;

    private int voiceBtnStatus = IConversationView.VOICE_STATUS_ACTION_UP;

//    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
//        @Override
//        public void onGlobalLayout() {
//
////            if (!isContextAlive()) {
////                return;
////            }
//
//
//        }
//    };


    @BindView(R.id.bottomBar)
    View bottomBar;


//    private ConversationPresenter presenter;

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
        photoTool = new PhotoTool(getActivity(), this, REQUEST_CODE_GET_PHOTO_FROM_ALBUM
                , REQUEST_CODE_GET_PHOTO_FROM_CAMERA
                , REQUEST_CODE_GET_CROP_PHOTO);
        setupTitleView();
        setupPtrView();
        setupRecyclerView();
        setupBottomBar();
//        setupEditText();
    }


    @Override
    public void onStart() {
        super.onStart();
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
        setupWindow();
//        getView().getViewTreeObserver()
//                .addOnGlobalLayoutListener(globalLayoutListener);
        presenter.initRecorder();
        presenter.initPlayer();
        presenter.getMessage();
        presenter.requestTargetUserInfo();
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
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void setupRecyclerView() {
        final LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setReverseLayout(true);
        lm.setStackFromEnd(true);
        messageList.setLayoutManager(lm);
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

    protected void setupTitleView() {
        titleView.setTitle(R.string.conversation);
//                .setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        titleView.setLayBac(R.color.color_ebeaee);
        titleView.setTitleViewHeight(R.dimen.live_inner_title_height);
        titleView.setLeftImage(R.drawable.back_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
                hideSoftInput();
            }
        });

    }

    protected void setupPtrView() {
        final RefreshHeadImage header = new RefreshHeadImage(getActivity());
        ptrFrame.setDurationToCloseHeader(500);
        ptrFrame.setHeaderView(header);
        ptrFrame.addPtrUIHandler(header);
        ptrFrame.setEnabledNextPtrAtOnce(false);
        ptrFrame.setPtrHandler(presenter.getPtrFrameHandler());
    }

    protected void setupBottomBar() {
        voiceMsgSwitchBtn.setOnClickListener(presenter.getBottomBarFuncBtnClickListener());
//        emojiBtn.setOnClickListener(presenter.getBottomBarFuncBtnClickListener());
        moreBtn.setOnClickListener(presenter.getBottomBarFuncBtnClickListener());
        txtMsgEditText.setOnEditorActionListener(presenter.getOnEditorActionListener());
        txtMsgEditText.setOnClickListener(presenter.getInputEditorOnClickListener());
        voiceMsgInputBtn.setOnTouchListener(presenter.getVoiceMsgOnTouchListener());
        KeyboardVisibilityEvent.registerEventListener(getActivity()
                , presenter.getKeyboardVisibilityEventListener());
    }

    public static Bundle getCallingBundle(String targetId) {
        final Bundle bundle = new Bundle();
        bundle.putString(IConversationView.ARG_KEY_TARGET_UID, targetId);
        return bundle;
    }


    @Override
    public void onStop() {
        super.onStop();
        presenter.releaseRecorder();
        presenter.releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.registerEvents();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unregisterEvent();
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

    private void hideSoftInput() {
        txtMsgEditText.clearFocus();
        DeviceUtils.hideSoftInput(getActivity(), txtMsgEditText);
    }

    private void updateBottomBarStatus() {
        switch (inputMode) {
            case INPUT_MODE_VOICE:
                voiceMsgInputBtn.setVisibility(View.VISIBLE);
                txtMsgEditText.setVisibility(View.GONE);
                voiceMsgSwitchBtn.setSelected(true);
                DeviceUtils.hideSoftInput(getActivity());
                break;
            default:
                voiceMsgInputBtn.setVisibility(View.GONE);
                txtMsgEditText.setVisibility(View.VISIBLE);
                voiceMsgSwitchBtn.setSelected(false);
                DeviceUtils.showSoftInput(getActivity(), txtMsgEditText);
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
    public void scrollToBottom(boolean smooth) {

        if (isContextAlive()) {
            if (smooth) {
                messageList.smoothScrollToPosition(0);
            } else {
                messageList.scrollToPosition(0);
            }
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
        DeviceUtils.hideSoftInput(getActivity(), txtMsgEditText);
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

    //TODO to be refactor
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

    @Override
    public void moveToZoomView(View v, Uri remoteUri) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ZoomActivity.class);
        intent.putExtras(createViewInfoBundle(v, remoteUri.toString()));
        startActivity(intent);
        //TODO
//        overridePendingTransition(0, 0);
    }

    @Override
    public void popupPhotoActionSheet() {
        if (!isContextAlive()) {
            return;
        }
        DialogUtil.popupPhotoPickAction(getActivity()
                , new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        presenter.requestPermissionToGetCameraPhoto();
                    }
                }
                , new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        moveToAlbum();
                    }
                });
    }

    @Override
    public void moveToCamera() {
        photoTool.getPhotoFromCamera();
    }

    @Override
    public void moveToAlbum() {
        photoTool.getPhotoFromAlbum();
    }


    @Override
    public void updateTitleText(String txt) {
        titleView.setTitle(txt)
                .setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
    }

    @Override
    public void invokeRefreshComplete() {
        ptrFrame.refreshComplete();
    }

    @Override
    public void scrollTo(int position, boolean smooth) {
        if (smooth) {
            messageList.smoothScrollToPosition(position);
        } else {
            messageList.scrollToPosition(position);
        }
    }


    @Override
    public void adjustWindow() {
        if (null != getDialog() && !adjustmentWasDone()) {

            int[] location = new int[2];
            getView().getLocationOnScreen(location);

            final Window window = getDialog()
                    .getWindow();
            final WindowManager.LayoutParams lp = window
                    .getAttributes();
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);


            Rect rect = new Rect();
            getView().getWindowVisibleDisplayFrame(rect);
            //计算出可见屏幕的高度
            int displayHeight = rect.bottom - rect.top;
            //获得屏幕整体的高度
            int hight = getView().getHeight();
            //获得键盘高度
            int keyboardHeight = hight + location[1] - displayHeight;

            Logger.e(TAG, "keyboard height  :" + keyboardHeight);
            adjustBottomMargin(keyboardHeight);
            scrollToBottom(true);

        }
    }

    private void adjustBottomMargin(int margin) {

        final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) bottomBar
                .getLayoutParams();
        lp.bottomMargin = margin;
        bottomBar.setLayoutParams(lp);

        final Window window = getDialog()
                .getWindow();
        if (null != window) {
            WindowManager.LayoutParams windowLp = window
                    .getAttributes();
        }
    }

    private boolean adjustmentWasDone() {
        final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) bottomBar
                .getLayoutParams();
        return lp.bottomMargin != 0;
    }


    @Override
    public void restoreWindow() {
        if (null != getDialog()) {

            setupWindow();

            adjustBottomMargin(0);

            scrollToBottom(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_GET_PHOTO_FROM_ALBUM:
                if (resultCode == Activity.RESULT_OK) {
                    final String path = photoTool
                            .getPath(getActivity(), data.getData());
                    presenter.onGetPhotoResult(path);
                }
                break;
            case REQUEST_CODE_GET_PHOTO_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    presenter.onGetPhotoResult(photoTool.getPhotoPath());
                }
                break;

            default:
                break;
        }
    }
}
