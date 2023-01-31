package com.example.android_sound;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextPaint;

import java.text.NumberFormat;

/* loaded from: classes.dex */
public class FrequencyDisplay {
    private Rect _bounds = new Rect();
    private TextPaint _tpaint = new TextPaint(1);
    private float _textSize = 12.0f;
    private Rect tmpRect = new Rect();

    public FrequencyDisplay() {
        this._tpaint.setColor(Colors.WHITE);
    }

    public void setBounds(Rect rect) {
        this._bounds.set(rect);
        int height = this._bounds.height();
        int width = this._bounds.width();
        if (height > width) {
            height = width;
        }
        this._textSize = height / 2;
        this._tpaint.setTextSize(this._textSize);
    }

    public void draw(Canvas canvas, double d) {
        try {
            int save = canvas.save();
            canvas.clipRect(this._bounds);
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(1);
            if (d < 150.0d) {
                numberFormat.setMinimumFractionDigits(1);
            } else {
                numberFormat.setMinimumFractionDigits(0);
            }
            String str = numberFormat.format(d) + " Hz";
            this._tpaint.getTextBounds(str, 0, str.length(), this.tmpRect);
            canvas.drawText(str, this._bounds.left + ((this._bounds.width() - this.tmpRect.width()) / 2), this._bounds.top + ((this._bounds.height() - this.tmpRect.height()) / 2) + ((int) (this._tpaint.getTextSize() + 0.9f)), this._tpaint);
            canvas.restoreToCount(save);
        } catch (Throwable unused) {
        }
    }
}
