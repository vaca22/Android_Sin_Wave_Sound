package com.example.android_sound;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/* loaded from: classes.dex */
public class SliderCentered extends Slider {
    public SliderCentered(int i) {
        super(i);
    }

    @Override // com.finestandroid.soundgenerator.Slider
    public void draw(Canvas canvas) {
        try {
            int save = canvas.save();
            canvas.clipRect(this._bounds);
            int i = this._bounds.left;
            int height = this._bounds.top + (this._bounds.height() / 2);
            int i2 = this._pointerW / 2;
            float f = i2 / 5;
            if (f < 1.0f) {
                f = 1.0f;
            }
            float strokeWidth = this._paint.getStrokeWidth();
            this._paint.setStrokeWidth(f);
            this._paint.setStyle(Paint.Style.STROKE);
            this._paint.setColor(Colors.GREY_LIGHT);
            float f2 = i + i2;
            float f3 = height;
            canvas.drawLine(f2, f3, this._bounds.right - i2, f3, this._paint);
            int i3 = this._pointerH / 5;
            float f4 = (i + this._bounds.right) / 2;
            float f5 = height - i3;
            float f6 = height + i3;
            canvas.drawLine(f4, f5, f4, f6, this._paint);
            canvas.drawLine(f2, f5, f2, f6, this._paint);
            canvas.drawLine(this._bounds.right - i2, f5, this._bounds.right - i2, f6, this._paint);
            float f7 = this._pointerH / 2;
            float f8 = i2;
            this._tmpRct.left = this._pointerPos - f8;
            this._tmpRct.right = this._pointerPos + f8;
            this._tmpRct.top = f3 - f7;
            this._tmpRct.bottom = f3 + f7;
            this._paint.setStyle(Paint.Style.FILL);
            this._paint.setColor(Colors.YELLOW);
            float f9 = i2 / 3;
            canvas.drawRoundRect(this._tmpRct, f9, f9, this._paint);
            this._paint.setStyle(Paint.Style.STROKE);
            this._paint.setColor(Colors.BLACK);
            canvas.drawRoundRect(this._tmpRct, f9, f9, this._paint);
            this._paint.setStrokeWidth(strokeWidth);
            canvas.restoreToCount(save);
        } catch (Throwable unused) {
        }
    }

    @Override // com.finestandroid.soundgenerator.Slider
    public boolean handleTouchEvent(MotionEvent motionEvent) {
        try {
            int actionMasked = motionEvent.getActionMasked();
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            switch (actionMasked) {
                case 0:
                    this._bHitted = false;
                    this._bSlideHitted = false;
                    this._hitOffset = 0;
                    if (this._bounds.contains(x, y)) {
                        float f = x;
                        if (f > this._pointerPos - this._tolerance && f < this._pointerPos + this._tolerance) {
                            this._bSlideHitted = true;
                            this._hitOffset = (int) (f - this._pointerPos);
                        }
                        this._bHitted = true;
                        break;
                    }
                    break;
                case 1:
                case 2:
                    if (this._bHitted && this._bSlideHitted) {
                        this._pointerPos = x - this._hitOffset;
                        double d = this._pointerPos - this._bounds.left;
                        double width = this._bounds.width();
                        Double.isNaN(d);
                        Double.isNaN(width);
                        this._value = d / width;
                        if (this._value < 0.0d) {
                            this._value = 0.0d;
                            setValue(this._value);
                        }
                        if (this._value > 1.0d) {
                            this._value = 1.0d;
                            setValue(this._value);
                        }
                        if (1 == actionMasked && this._value > 0.475d && this._value < 0.525d) {
                            this._value = 0.5d;
                            double d2 = this._value;
                            double width2 = this._bounds.width() - this._pointerW;
                            Double.isNaN(width2);
                            this._pointerPos = this._bounds.left + (this._pointerW / 2) + ((float) (d2 * width2));
                        }
                        if (this._listener != null) {
                            this._listener.onSliderChanged(this._sliderID, this._value);
                        }
                        if (this._listener != null) {
                            this._listener.sliderredraw();
                        }
                        if (1 == actionMasked) {
                            this._bHitted = false;
                            this._bSlideHitted = false;
                        }
                        return true;
                    }
                    return false;
                case 3:
                    if (this._listener != null) {
                        this._listener.sliderredraw();
                    }
                    this._bHitted = false;
                    this._bSlideHitted = false;
                    return true;
            }
            return this._bHitted;
        } catch (Throwable unused) {
            return true;
        }
    }
}
