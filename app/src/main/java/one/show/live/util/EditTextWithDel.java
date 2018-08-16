package one.show.live.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import one.show.live.R;


public class EditTextWithDel extends AppCompatEditText {
    private final static String TAG = "EditTextWithDel";
    private Drawable imgInable;
    private Drawable imgAble;
    private boolean isShowDelete = true;

    public EditTextWithDel(Context context) {
        super(context);
        init(context, null);
    }

    public EditTextWithDel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);

    }

    public EditTextWithDel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.edittextwithdel);
            isShowDelete = typedArray.getBoolean(R.styleable.edittextwithdel_is_show_delete, true);
        }
        imgInable = null;
        imgAble = ContextCompat.getDrawable(context, R.drawable.selector_edit_delete);
        imgAble.setBounds(2, 2, 2, 2);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editTextContentListener != null) {
                    editTextContentListener.afterTextChanged(EditTextWithDel.this, s.toString().trim());
                }
                setDrawable();
            }
        });
        setDrawable();

    }

    // 设置删除图片
    private void setDrawable() {
        if (!isShowDelete) {
            return;
        }
        if (length() < 1)
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgInable, null);
        else
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null);
    }

    // 处理删除事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgAble != null && event.getAction() == MotionEvent.ACTION_DOWN) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 100;
            if (rect.contains(eventX, eventY)) {
                setText("");
            } else {
                if (edittextClick != null)
                    edittextClick.Editclick(this);
            }

        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }


    public interface EditTextContentListener {
        void afterTextChanged(EditTextWithDel edtext, String content);
    }

    private EditTextContentListener editTextContentListener;

    public void setEditTextContentListener(EditTextContentListener editTextContentListener) {
        this.editTextContentListener = editTextContentListener;
    }

    public interface EdittextClick {
        void Editclick(EditTextWithDel editTextWithDel);
    }

    private EdittextClick edittextClick;

    public void setEdittextClick(EdittextClick edittextClick) {
        this.edittextClick = edittextClick;
    }

}
