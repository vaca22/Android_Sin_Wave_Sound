package com.example.android_sound;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;

/* loaded from: classes.dex */
public class PushBtn {
    protected static final int STATE_FOCUS = 1;
    protected static final int STATE_NONE = 0;
    protected static final int STATE_PUSHED = 2;
    protected int _id;
    protected Rect _rect = new Rect();
    protected RectF _tmpRect = new RectF();
    protected Rect _textRect = new Rect();
    protected TextPaint _tpaint = new TextPaint(1);
    protected int _color = Colors.BLACK;
    protected int _colorFill = Colors.BLUE_METAL;
    protected int _selectColor = Colors.RED;
    protected int _activeColor = Colors.BLACK;
    protected int _state = 0;
    protected int _oldState = 0;
    protected ButtonListener _listener = null;
    private boolean _canBeOff = true;
    protected String _text = "";

    /* loaded from: classes.dex */
    public interface ButtonListener {
        void onClick(int i);

        void playClick(int i);

        void redraw();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PushBtn(int i) {
        this._id = 0;
        this._id = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setListener(ButtonListener buttonListener) {
        this._listener = buttonListener;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setText(String str) {
        this._text = str;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setBounds(Rect rect) {
        this._rect.set(rect);
        this._tpaint.setTextSize(this._rect.height() / 3.0f);
    }

    protected void drawBack(Canvas canvas) {
        try {
            float strokeWidth = this._tpaint.getStrokeWidth();
            this._tpaint.setStrokeWidth(2.0f);
            float width = this._rect.width() / 20.0f;
            this._tmpRect.set(this._rect);
            if (this._state == 2) {
                this._tpaint.setColor(this._selectColor);
                this._tpaint.setStyle(Paint.Style.FILL_AND_STROKE);
            } else if (this._state == 1) {
                this._tpaint.setColor(this._selectColor);
                this._tpaint.setStyle(Paint.Style.FILL_AND_STROKE);
            } else {
                this._tpaint.setColor(this._colorFill);
                this._tpaint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawRoundRect(this._tmpRect, width, width, this._tpaint);
                this._tpaint.setColor(this._color);
                this._tpaint.setStyle(Paint.Style.STROKE);
            }
            canvas.drawRoundRect(this._tmpRect, width, width, this._tpaint);
            this._tpaint.setStrokeWidth(strokeWidth);
        } catch (Throwable unused) {
        }
    }

    protected void drawFace(Canvas canvas) {
        int length;
        try {
            if (this._text != null && (length = this._text.length()) > 0) {
                if (this._state == 2) {
                    this._tpaint.setColor(this._activeColor);
                    this._tpaint.setStyle(Paint.Style.FILL_AND_STROKE);
                } else if (this._state == 1) {
                    this._tpaint.setColor(this._color);
                    this._tpaint.setStyle(Paint.Style.FILL_AND_STROKE);
                } else {
                    this._tpaint.setColor(this._color);
                    this._tpaint.setStyle(Paint.Style.FILL_AND_STROKE);
                }
                this._tpaint.getTextBounds(this._text, 0, length, this._textRect);
                canvas.drawText(this._text, this._rect.left + ((this._rect.width() - this._textRect.width()) / 2), this._rect.bottom - ((this._rect.height() - this._textRect.height()) / 2), this._tpaint);
            }
        } catch (Throwable unused) {
        }
    }

    public void draw(Canvas canvas) {
        try {
            int save = canvas.save();
            drawBack(canvas);
            drawFace(canvas);
            canvas.restoreToCount(save);
        } catch (Throwable unused) {
        }
    }

    public boolean handleTouchEvent(MotionEvent motionEvent) {
        Log.e("gaga","yues");
        try {
            int actionMasked = motionEvent.getActionMasked();
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            if (actionMasked != 3) {
                switch (actionMasked) {
                    case 0:
                        if (this._rect.contains(x, y)) {
                            this._oldState = this._state;
                            this._state = 1;
                            if (this._listener != null) {
                                this._listener.redraw();
                                break;
                            }
                        }
                        break;
                    case 1:
                        if (this._state != 1) {
                            return false;
                        }
                        if (this._rect.contains(x, y)) {
                            if (this._canBeOff) {
                                if (this._oldState == 0) {
                                    this._state = 2;
                                } else {
                                    this._state = 0;
                                }
                            } else {
                                this._state = 2;
                            }
                            if (this._listener != null) {
                                this._listener.playClick(this._id);
                                this._listener.onClick(this._id);
                            }
                        } else {
                            this._state = this._oldState;
                        }
                        if (this._listener != null) {
                            this._listener.redraw();
                        }
                        return true;
                }
                return this._state == 1;
            } else if (this._state != 1) {
                return false;
            } else {
                this._state = this._oldState;
                if (this._listener != null) {
                    this._listener.redraw();
                }
                return true;
            }
        } catch (Throwable unused) {
            Log.e("gaga","yues");
            return this._state == 1;
        }
    }

    public boolean isOn() {
        return this._state == 2;
    }

    public void switchOff() {
        this._state = 0;
    }

    public void switchOn() {
        this._state = 2;
    }

    public void setCanBeOff(boolean z) {
        this._canBeOff = z;
    }
}
