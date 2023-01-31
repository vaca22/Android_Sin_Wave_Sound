package com.example.android_sound;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

/* loaded from: classes.dex */
public class Slider {
    protected int _sliderID;
    protected Rect _bounds = new Rect();
    protected RectF _tmpRct = new RectF();
    protected Paint _paint = new Paint(1);
    protected int _pointerW = 1;
    protected int _pointerH = 1;
    protected boolean _bHitted = false;
    protected boolean _bSlideHitted = false;
    protected int _hitOffset = 0;
    protected float _pointerPos = 0.0f;
    protected double _value = 0.0d;
    protected float _tolerance = 2.0f;
    protected SliderListener _listener = null;

    /* loaded from: classes.dex */
    public interface SliderListener {
        void onSliderChanged(int i, double d);

        void sliderredraw();
    }

    public Slider(int i) {
        this._sliderID = 0;
        this._sliderID = i;
    }

    public void setListener(SliderListener sliderListener) {
        this._listener = sliderListener;
    }

    public void setValue(double d) {
        if (d < 0.0d) {
            d = 0.0d;
        }
        if (d > 1.0d) {
            d = 1.0d;
        }
        this._value = d;
        double width = this._bounds.width() - this._pointerW;
        Double.isNaN(width);
        this._pointerPos = this._bounds.left + (this._pointerW / 2) + ((float) (d * width));
        if (this._listener != null) {
            this._listener.sliderredraw();
        }
    }

    public double getValue() {
        return this._value;
    }

    public void setBounds(Rect rect) {
        int width=0;
        this._bounds.set(rect);
        int height = this._bounds.height();
        this._pointerW = this._bounds.width() / 12;
        if (this._pointerW < 4) {
            this._pointerW = 4;
        }
        this._pointerH = (this._pointerW * 3) / 2;
        if (this._pointerH > height) {
            this._pointerH = height;
        }
        double d = this._value;
        double width2 = this._bounds.width() - this._pointerW;
        Double.isNaN(width2);
        this._pointerPos = this._bounds.left + (this._pointerW / 2) + ((float) (d * width2));
        this._tolerance = width / 8;
    }

    public int getLeft() {
        return this._bounds.left;
    }

    public int getTop() {
        return this._bounds.top;
    }

    public int getBottom() {
        return this._bounds.bottom;
    }

    public int getPointerTop() {
        return ((this._bounds.top + this._bounds.bottom) / 2) - (this._pointerH / 2);
    }

    public int getLineY() {
        return (this._bounds.top + this._bounds.bottom) / 2;
    }

    public int getLineTextTopY() {
        return ((this._bounds.top + this._bounds.bottom) / 2) + (this._pointerH / 4);
    }

    public int getHeight() {
        return this._bounds.height();
    }

    public int getWidth() {
        return this._bounds.width();
    }

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
            float f4 = this._pointerH / 2;
            float f5 = i2;
            this._tmpRct.left = this._pointerPos - f5;
            this._tmpRct.right = this._pointerPos + f5;
            this._tmpRct.top = f3 - f4;
            this._tmpRct.bottom = f3 + f4;
            this._paint.setStyle(Paint.Style.FILL);
            this._paint.setColor(Colors.YELLOW);
            float f6 = i2 / 3;
            canvas.drawRoundRect(this._tmpRct, f6, f6, this._paint);
            this._paint.setStyle(Paint.Style.STROKE);
            this._paint.setColor(Colors.BLACK);
            canvas.drawRoundRect(this._tmpRct, f6, f6, this._paint);
            this._paint.setStrokeWidth(strokeWidth);
            canvas.restoreToCount(save);
        } catch (Throwable unused) {
        }
    }

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
