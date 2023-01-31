package com.example.android_sound;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class PlayView extends View {
    protected WeakReference<SoundGenerator> _activity;
    protected Rect _drawRect;
    protected Paint _paint;
    protected WeakReference<Player> _playerRef;
    protected Rect _tmpRect;
    protected Paint _tpaint;

    public void init() {
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    public PlayView(Context context) {
        super(context);
        this._activity = null;
        this._drawRect = new Rect();
        this._paint = new Paint();
        this._tpaint = new TextPaint(1);
        this._tmpRect = new Rect();
        this._playerRef = null;
        setLayerType(1, null);
        init();
    }

    public PlayView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this._activity = null;
        this._drawRect = new Rect();
        this._paint = new Paint();
        this._tpaint = new TextPaint(1);
        this._tmpRect = new Rect();
        this._playerRef = null;
        setLayerType(1, null);
        init();
    }

    public PlayView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this._activity = null;
        this._drawRect = new Rect();
        this._paint = new Paint();
        this._tpaint = new TextPaint(1);
        this._tmpRect = new Rect();
        this._playerRef = null;
        setLayerType(1, null);
        init();
    }

    public void setPlayer(Player player) {
        try {
            this._playerRef = null;
            if (player == null) {
                return;
            }
            this._playerRef = new WeakReference<>(player);
        } catch (Throwable unused) {
        }
    }

    protected Player player() {
        if (this._playerRef == null) {
            return null;
        }
        return this._playerRef.get();
    }

    public void init(SoundGenerator soundGenerator) {
        this._activity = new WeakReference<>(soundGenerator);
    }

    public SoundGenerator activity() {
        if (this._activity == null) {
            return null;
        }
        return this._activity.get();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        getDrawingRect(this._drawRect);
        drawBackground(canvas);
    }

    protected void drawBackground(Canvas canvas) {
        this._paint.setColor(Colors.GREY);
        this._paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(this._drawRect, this._paint);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
    }

    public void postRedraw() {
        try {
            activity().runOnUiThread(new Runnable() { // from class: com.finestandroid.soundgenerator.PlayView.1
                @Override // java.lang.Runnable
                public void run() {
                    PlayView.this.postInvalidate();
                }
            });
        } catch (Throwable unused) {
        }
    }

    public void redraw() {
        postInvalidate();
    }
}
