package com.example.android_sound;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.MotionEvent;

/* loaded from: classes.dex */
public class WaveTypeSelector {
    private Rect _bounds = new Rect();
    private Rect _tmpRct = new Rect();
    private Paint _paint = new Paint(1);
    private int _typesCount = 3;
    private int _selectorW = 1;
    private int _margin = 1;
    private int _activeType = 0;
    private int _hitType = -1;
    private Path _path = new Path();
    private boolean _bHitted = false;
    private WaveTypeListener _listener = null;

    /* loaded from: classes.dex */
    public interface WaveTypeListener {
        void doredraw();

        void onWaveTypeChanged(int i);
    }

    public void setListener(WaveTypeListener waveTypeListener) {
        this._listener = waveTypeListener;
    }

    public void setBounds(Rect rect) {
        this._bounds.set(rect);
        int height = this._bounds.height();
        this._selectorW = this._bounds.width() / this._typesCount;
        if (height < this._selectorW) {
            this._selectorW = height;
        }
        this._margin = this._selectorW / 10;
    }

    public void draw(Canvas canvas) {
        try {
            int save = canvas.save();
            canvas.clipRect(this._bounds);
            int i = this._bounds.left;
            int height = this._bounds.top + ((this._bounds.height() - this._selectorW) / 2);
            for (int i2 = 0; i2 < this._typesCount; i2++) {
                drawType(canvas, i, height, i2);
                i += this._selectorW;
            }
            canvas.restoreToCount(save);
        } catch (Throwable unused) {
        }
    }

    private void drawType(Canvas canvas, int i, int i2, int i3) {
        if (this._hitType == i3) {
            this._tmpRct.set(i, i2, this._selectorW + i, this._selectorW + i2);
            this._paint.setStyle(Paint.Style.FILL_AND_STROKE);
            this._paint.setColor(Colors.BLUE_DARK);
            canvas.drawRect(this._tmpRct, this._paint);
        }
        int i4 = (this._selectorW / 2) - this._margin;
        int i5 = this._selectorW - (this._margin * 2);
        int i6 = i4 / 2;
        int i7 = i + this._margin;
        int i8 = this._margin + i2 + i6;
        this._path.reset();
        this._paint.setStyle(Paint.Style.STROKE);
        float strokeWidth = this._paint.getStrokeWidth();
        float f = i4 / 18.0f;
        if (f < 1.0f) {
            f = 1.0f;
        }
        this._paint.setStrokeWidth(f);
        if (i3 == this._activeType) {
            this._paint.setColor(Colors.ORANGE);
        } else {
            this._paint.setColor(Colors.GREY_LIGHT);
        }
        int i9 = i5 / 2;
        switch (i3) {
            case 0:
                int i10 = i5 / 4;
                float f2 = i8;
                this._path.moveTo(i7, f2);
                float f3 = i7 + i9;
                int i11 = i4 * 2;
                this._path.cubicTo(f3, i8 - i11, f3, i8 + i11, i7 + i5, f2);
                canvas.drawPath(this._path, this._paint);
                break;
            case 1:
                float f4 = i7;
                float f5 = i8;
                float f6 = i8 - i6;
                canvas.drawLine(f4, f5, f4, f6, this._paint);
                float f7 = i7 + i9;
                canvas.drawLine(f4, f6, f7, f6, this._paint);
                float f8 = i8 + i6;
                canvas.drawLine(f7, f6, f7, f8, this._paint);
                float f9 = i7 + i5;
                canvas.drawLine(f7, f8, f9, f8, this._paint);
                canvas.drawLine(f9, f8, f9, f5, this._paint);
                break;
            case 2:
                float f10 = i7;
                float f11 = i8 + i6;
                float f12 = i7 + i5;
                float f13 = i8 - i6;
                canvas.drawLine(f10, f11, f12, f13, this._paint);
                canvas.drawLine(f12, f13, f12, f11, this._paint);
                break;
        }
        this._paint.setStrokeWidth(strokeWidth);
        float f14 = i6 / 2;
        float f15 = (this._selectorW / 2) + i;
        float f16 = ((this._selectorW + i2) - this._margin) - f14;
        this._paint.setStyle(Paint.Style.FILL_AND_STROKE);
        if (i3 == this._activeType) {
            this._paint.setColor(Colors.RED);
        } else {
            this._paint.setColor(Colors.GREY_D);
        }
        canvas.drawCircle(f15, f16, f14, this._paint);
    }

    protected int hit(int i, int i2) {
        int i3 = 0;
        for (int i4 = 0; i4 < this._typesCount; i4++) {
            if (i <= this._selectorW + i3) {
                return i4;
            }
            i3 += this._selectorW;
        }
        return -1;
    }

    public boolean handleTouchEvent(MotionEvent motionEvent) {
        try {
            int actionMasked = motionEvent.getActionMasked();
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            if (actionMasked != 3) {
                int i = 0;
                switch (actionMasked) {
                    case 0:
                        this._hitType = -1;
                        this._bHitted = false;
                        if (this._bounds.contains(x, y)) {
                            this._hitType = hit(x, y);
                            this._bHitted = true;
                            if (this._listener != null) {
                                this._listener.doredraw();
                                break;
                            }
                        }
                        break;
                    case 1:
                        if (this._hitType < 0) {
                            return false;
                        }
                        if (this._bounds.contains(x, y) && hit(x, y) == this._hitType) {
                            this._activeType = this._hitType;
                            switch (this._activeType) {
                                case 1:
                                    i = 1;
                                    break;
                                case 2:
                                    i = 2;
                                    break;
                            }
                            if (this._listener != null) {
                                this._listener.onWaveTypeChanged(i);
                            }
                        }
                        this._hitType = -1;
                        if (this._listener != null) {
                            this._listener.doredraw();
                        }
                        return true;
                }
                return this._bHitted;
            }
            this._hitType = -1;
            if (this._listener != null) {
                this._listener.doredraw();
            }
            return true;
        } catch (Throwable unused) {
            return true;
        }
    }

    public void setType(int i) {
        this._activeType = i;
    }
}
