package one.show.live.common.view;


import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import one.show.live.common.util.UIUtils;
import one.show.live.common.common.R;

public class UIActionSheet extends Dialog {
    private boolean isDismiss;
    private View contentLayout;

    public UIActionSheet(Context context) {
        super(context);
    }

    public UIActionSheet(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected UIActionSheet(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setContentLayout(View contentLayout) {
        this.contentLayout = contentLayout;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.enter_from_buttom);
        contentLayout.startAnimation(animation);
    }

    @Override
    public void dismiss() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.exit_from_buttom);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                UIActionSheet.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        contentLayout.startAnimation(animation);
    }

    static UIActionSheet dialog;

    public static class Builder {

        private Context context;
        private CharSequence cancel;
        private CharSequence[] items;
        public static boolean changeItemHeight = false;
        private OnItemClickListener listener;

        private LinearLayout linearLayout;

        public Builder(Context context) {
            this.context = context;
        }

        public void setCancel(String cancel) {
            this.cancel = cancel;
        }

        public void setItems(CharSequence... items) {
            this.items = items;
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        public UIActionSheet create() {
            dialog = new UIActionSheet(context, R.style.dialog);
            View layout = View.inflate(context, R.layout.view_ui_action_sheet, null);
            linearLayout = (LinearLayout) layout.findViewById(R.id.content_layout);
            setItem(dialog, linearLayout);


            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);
            dialog.setContentView(layout);
            dialog.setContentLayout(linearLayout);
            return dialog;
        }

        @SuppressWarnings("deprecation")
        private void setItem(final Dialog dialog, LinearLayout layout) {
            int color = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                color = context.getResources().getColor(R.color.blackPieceColor, context.getTheme());
            } else {
                color = context.getResources().getColor(R.color.blackPieceColor);
            }

            for (int i = 0; i < items.length; i++) {
                layout.addView(getButton(dialog, items[i], color, i));
                layout.addView(getLine());
            }

            if (!TextUtils.isEmpty(cancel)) {
                TextView button = getButton(dialog, "取消", context.getResources().getColor(R.color.orangeColor), items.length + 1);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setClickable(false);
                        dialog.dismiss();
                    }
                });
                layout.addView(button);
                layout.addView(getLine());
            }

        }

        private TextView getButton(final Dialog dialog, CharSequence title, int color, int position) {
            TextView button = getTextView(title, color);
            button.setTag(position);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(dialog, v, Integer.valueOf(v.getTag().toString()));
                    }
                }
            });
            return button;
        }

        private TextView getTextView(CharSequence title, int color) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UIUtils.dip2px(context, changeItemHeight ? 50 : 66));
            TextView textView = new TextView(context);
            textView.setText(title);
            textView.setTextSize(15);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.item_white);
            textView.setLayoutParams(params);
            textView.setTextColor(color);
            return textView;
        }

        private View getLine() {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            View view = new View(context);
            view.setBackgroundResource(R.drawable.shape_divider_dialog_list);
            view.setLayoutParams(params);
            return view;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Dialog dialog, View v, int position);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isDismiss){
                return true;
            }
            isDismiss = true;
            dismiss();
            return true;
        }
        return super.onTouchEvent(event);
    }

}
