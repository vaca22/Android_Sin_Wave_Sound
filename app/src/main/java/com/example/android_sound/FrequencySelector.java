package com.example.android_sound;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

/* loaded from: classes.dex */
public class FrequencySelector {
    private double _angleStep1;
    private double _angleStep2;
    private Rect _rect = new Rect();
    private RectF _tmpRect = new RectF();
    private RectF _tmpRectR = new RectF();
    private Paint _paint = new Paint(1);
    private int _backColor = Colors.BLACK;
    private float _cx = 0.0f;
    private float _cy = 0.0f;
    private float _rotation1 = 0.0f;
    private float _rotation2 = 0.0f;
    private float _valuesOn360_1 = 20.0f;
    private float _valuesOn360_2 = 100.0f;
    private boolean _touchInside = false;
    private float _r1 = 0.0f;
    private float _r2 = 0.0f;
    private boolean _isSelected = false;
    private double _startAlpha = 0.0d;
    private double _prevAlpha = 0.0d;
    private double _valueAngle = 0.0d;
    private int _stepsPassed1 = 0;
    private int _stepsPassed2 = 0;
    private FrequencySelectorListener _listener = null;

    /* loaded from: classes.dex */
    public interface FrequencySelectorListener {
        void changeValue(float f, boolean z);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public FrequencySelector() {
        this._angleStep1 = 0.15707963267948966d;
        this._angleStep2 = 0.15707963267948966d;
        double d = this._valuesOn360_1;
        Double.isNaN(d);
        this._angleStep1 = 6.283185307179586d / d;
        double d2 = this._valuesOn360_2;
        Double.isNaN(d2);
        this._angleStep2 = 6.283185307179586d / d2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setListener(FrequencySelectorListener frequencySelectorListener) {
        this._listener = frequencySelectorListener;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setValuesOn360_Outside(float f) {
        this._valuesOn360_1 = f;
        double d = this._valuesOn360_1;
        Double.isNaN(d);
        this._angleStep1 = 6.283185307179586d / d;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setValuesOn360_Inside(float f) {
        this._valuesOn360_2 = f;
        double d = this._valuesOn360_2;
        Double.isNaN(d);
        this._angleStep2 = 6.283185307179586d / d;
    }

    protected void setBackColor(int i) {
        this._backColor = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setBounds(Rect rect) {
        this._rect.set(rect);
        this._cx = (this._rect.left + this._rect.right) / 2;
        this._cy = (this._rect.top + this._rect.bottom) / 2;
        int width = this._rect.width();
        int height = this._rect.height();
        if (width < height) {
            this._r1 = width / 2;
        } else {
            this._r1 = height / 2;
        }
        this._r2 = (this._r1 * 10.0f) / 18.0f;
    }

    protected float rotatePointX(float f, float f2, float f3) {
        double d = f3;
        Double.isNaN(d);
        double d2 = (d * 3.141592653589793d) / 180.0d;
        double cos = Math.cos(d2);
        double d3 = f - this._cx;
        Double.isNaN(d3);
        double sin = Math.sin(d2);
        double d4 = f2 - this._cy;
        Double.isNaN(d4);
        double d5 = this._cx;
        Double.isNaN(d5);
        return (float) (((cos * d3) - (sin * d4)) + d5);
    }

    protected float rotatePointY(float f, float f2, float f3) {
        double d = f3;
        Double.isNaN(d);
        double d2 = (d * 3.141592653589793d) / 180.0d;
        double sin = Math.sin(d2);
        double d3 = f - this._cx;
        Double.isNaN(d3);
        double cos = Math.cos(d2);
        double d4 = f2 - this._cy;
        Double.isNaN(d4);
        double d5 = this._cy;
        Double.isNaN(d5);
        return (float) ((sin * d3) + (cos * d4) + d5);
    }

    private void drawOutside(Canvas canvas) {
        this._paint.setColor(Colors.GREY_LIGHT);
        this._paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(this._cx, this._cy, this._r1, this._paint);
        this._paint.setStyle(Paint.Style.STROKE);
        this._paint.setColor(this._backColor);
        float strokeWidth = this._paint.getStrokeWidth();
        this._paint.setStrokeWidth(this._r1 / 32.0f);
        float f = this._r1 * 0.95f;
        canvas.drawCircle(this._cx, this._cy, f, this._paint);
        this._paint.setStrokeWidth(strokeWidth);
        this._paint.setStyle(Paint.Style.FILL_AND_STROKE);
        float f2 = (f * 6.0f) / 7.0f;
        float f3 = f2 / 4.0f;
        float f4 = (f3 / 6.0f) / 2.0f;
        this._tmpRect.top = this._cy - f2;
        this._tmpRect.bottom = this._tmpRect.top + f3;
        this._tmpRect.left = this._cx - f4;
        this._tmpRect.right = this._cx + f4;
        int save = canvas.save();
        canvas.rotate(this._rotation1, this._cx, this._cy);
        for (int i = 0; i < 10; i++) {
            this._tmpRectR.set(this._tmpRect);
            canvas.rotate(36.0f, this._cx, this._cy);
            canvas.drawRoundRect(this._tmpRectR, f4, f4, this._paint);
        }
        canvas.restoreToCount(save);
    }

    private void drawInside(Canvas canvas) {
        this._paint.setColor(Colors.YELLOW);
        this._paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(this._cx, this._cy, this._r2, this._paint);
        this._paint.setStyle(Paint.Style.STROKE);
        this._paint.setColor(this._backColor);
        float strokeWidth = this._paint.getStrokeWidth();
        this._paint.setStrokeWidth(this._r1 / 40.0f);
        float f = this._r2;
        canvas.drawCircle(this._cx, this._cy, f, this._paint);
        this._paint.setStrokeWidth(strokeWidth);
        this._paint.setStyle(Paint.Style.FILL_AND_STROKE);
        float f2 = (f * 4.0f) / 5.0f;
        float f3 = f2 / 3.0f;
        float f4 = (f3 / 6.0f) / 2.0f;
        this._tmpRect.top = this._cy - f2;
        this._tmpRect.bottom = this._tmpRect.top + f3;
        this._tmpRect.left = this._cx - f4;
        this._tmpRect.right = this._cx + f4;
        int save = canvas.save();
        canvas.rotate(this._rotation2, this._cx, this._cy);
        for (int i = 0; i < 10; i++) {
            this._tmpRectR.set(this._tmpRect);
            canvas.rotate(36.0f, this._cx, this._cy);
            canvas.drawRoundRect(this._tmpRectR, f4, f4, this._paint);
        }
        canvas.restoreToCount(save);
    }

    public void draw(Canvas canvas) {
        try {
            drawOutside(canvas);
            drawInside(canvas);
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
                    this._isSelected = false;
                    if (this._rect.contains(x, y)) {
                        this._touchInside = true;
                        this._isSelected = true;
                        this._startAlpha = getAlpha(x, y);
                        this._prevAlpha = this._startAlpha;
                        this._valueAngle = 0.0d;
                        this._stepsPassed1 = 0;
                        this._stepsPassed2 = 0;
                        int i = x - ((this._rect.left + this._rect.right) / 2);
                        if (i < 0) {
                            i = -i;
                        }
                        if (i >= this._r2) {
                            this._touchInside = false;
                        }
                        if (this._touchInside) {
                            int i2 = y - ((this._rect.top + this._rect.bottom) / 2);
                            if (i2 < 0) {
                                i2 = -i2;
                            }
                            if (i2 >= this._r2) {
                                this._touchInside = false;
                                break;
                            }
                        }
                    }
                    break;
                case 1:
                case 3:
                    if (this._isSelected) {
                        this._isSelected = false;
                        this._valueAngle = 0.0d;
                        this._stepsPassed1 = 0;
                        this._stepsPassed2 = 0;
                        return true;
                    }
                    return false;
                case 2:
                    if (!this._isSelected) {
                        return false;
                    }
                    double alpha = getAlpha(x, y);
                    double d = alpha - this._prevAlpha;
                    if (d > 3.141592653589793d) {
                        d -= 6.283185307179586d;
                    } else if (d < -3.141592653589793d) {
                        d += 6.283185307179586d;
                    }
                    this._prevAlpha = alpha;
                    this._valueAngle += d;
                    double d2 = this._valueAngle;
                    if (d2 < 0.0d) {
                        d2 += 6.283185307179586d;
                    }
                    if (d2 > 6.283185307179586d) {
                        d2 -= 6.283185307179586d;
                    }
                    float f = (float) ((d2 * 360.0d) / 6.283185307179586d);
                    if (this._touchInside) {
                        this._rotation2 = f;
                    } else {
                        this._rotation1 = f;
                    }
                    if (this._listener != null) {
                        this._listener.changeValue(getvalue(), this._touchInside);
                        break;
                    }
                    break;
            }
            return this._isSelected;
        } catch (Throwable unused) {
            return this._isSelected;
        }
    }

    private float getvalue1() {
        double d = this._valueAngle;
        double d2 = this._stepsPassed1;
        double d3 = this._angleStep1;
        Double.isNaN(d2);
        int i = (int) ((d - (d2 * d3)) / this._angleStep1);
        if (i == 0) {
            return 0.0f;
        }
        this._stepsPassed1 += i;
        double d4 = i;
        double d5 = this._angleStep1;
        Double.isNaN(d4);
        double d6 = this._valuesOn360_1;
        Double.isNaN(d6);
        return (float) (((d4 * d5) * d6) / 6.283185307179586d);
    }

    private float getvalue2() {
        double d = this._valueAngle;
        double d2 = this._stepsPassed2;
        double d3 = this._angleStep2;
        Double.isNaN(d2);
        int i = (int) ((d - (d2 * d3)) / this._angleStep2);
        if (i == 0) {
            return 0.0f;
        }
        this._stepsPassed2 += i;
        double d4 = i;
        double d5 = this._angleStep2;
        Double.isNaN(d4);
        double d6 = this._valuesOn360_2;
        Double.isNaN(d6);
        return (float) (((d4 * d5) * d6) / 6.283185307179586d);
    }

    private float getvalue() {
        if (!this._touchInside) {
            return getvalue1();
        }
        return getvalue2();
    }

    private double getAlpha(int i, int i2) {
        float f = i - this._cx;
        float f2 = this._cy - i2;
        if (f2 == 0.0f) {
            return f > 0.0f ? 1.5707963267948966d : 4.71238898038469d;
        }
        double atan = Math.atan(f / f2);
        if (f2 < 0.0f) {
            atan -= 3.141592653589793d;
        }
        if (atan < 0.0d) {
            atan += 6.283185307179586d;
        }
        return atan > 6.283185307179586d ? atan - 6.283185307179586d : atan;
    }

    public Rect getBounds() {
        return this._rect;
    }
}
