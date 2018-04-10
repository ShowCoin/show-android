package one.show.live.live.widget;

import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;
import android.view.View;

public abstract class TouchableSpan extends CharacterStyle implements UpdateAppearance     {

    public abstract boolean onLongClick(View widget);

    public abstract boolean onClick(View widget);

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }

}