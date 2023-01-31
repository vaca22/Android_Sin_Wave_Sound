package com.example.android_sound;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.android_sound.ss.SoundPlayer;
import com.example.android_sound.Slider;

import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class GeneratorView extends View implements PushBtn.ButtonListener, FrequencySelector.FrequencySelectorListener, WaveTypeSelector.WaveTypeListener, Slider.SliderListener {
    public static final int FAZE_SLIDER = 1;
    public static final int LR_SLIDER = 3;
    public static final double MAX_F = 20000.0d;
    public static final double MIN_F = 0.1d;
    public static final int MODULATION_SLIDER = 2;
    private static final int PLAY__BTN = 5;
    public static final int VOLUME_SLIDER = 0;
    protected WeakReference<SoundGenerator> _activity;
    private int _colorBack;
    protected Rect _controlRect;
    protected Rect _drawRect;
    protected FrequencyDisplay _fdisplay;
    private double _frequency;
    private int _generatorIndex;
    private boolean _isPlaying;
    private boolean _isPro;
    protected Slider _modulator;
    protected Paint _paint;
    protected PushBtn _playBtn;
    private Player _player;
    protected FrequencySelector _selector;
    private SoundPlayer _soundPlayer;
    protected SliderCentered _stereoPro;
    protected Rect _tmpRect;
    protected TextPaint _tpaint;
    protected WaveTypeSelector _typeSelector;
    protected Slider _volume;

    public void playFeedback() {
    }

    public GeneratorView(Context context) {
        super(context);
        this._playBtn = new PushBtn(5);
        this._fdisplay = new FrequencyDisplay();
        this._selector = new FrequencySelector();
        this._typeSelector = new WaveTypeSelector();
        this._volume = new Slider(0);
        this._stereoPro = new SliderCentered(3);
        this._modulator = new Slider(2);

        this._drawRect = new Rect();
        this._controlRect = new Rect();
        this._paint = new Paint(1);
        this._tpaint = new TextPaint(1);
        this._tmpRect = new Rect();
        this._frequency = 220.0d;
        this._player = null;
        this._isPlaying = false;
        this._generatorIndex = 0;
        this._soundPlayer = null;
        this._colorBack = Colors.BLUE_BACK;
        this._isPro = false;
        setLayerType(1, null);
    }

    public GeneratorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this._playBtn = new PushBtn(5);
        this._fdisplay = new FrequencyDisplay();
        this._selector = new FrequencySelector();
        this._typeSelector = new WaveTypeSelector();
        this._volume = new Slider(0);
        this._stereoPro = new SliderCentered(3);
        this._modulator = new Slider(2);

        this._drawRect = new Rect();
        this._controlRect = new Rect();
        this._paint = new Paint(1);
        this._tpaint = new TextPaint(1);
        this._tmpRect = new Rect();
        this._frequency = 220.0d;
        this._player = null;
        this._isPlaying = false;
        this._generatorIndex = 0;
        this._soundPlayer = null;
        this._colorBack = Colors.BLUE_BACK;
        this._isPro = false;
        setLayerType(1, null);
    }

    public GeneratorView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this._playBtn = new PushBtn(5);
        this._fdisplay = new FrequencyDisplay();
        this._selector = new FrequencySelector();
        this._typeSelector = new WaveTypeSelector();
        this._volume = new Slider(0);
        this._stereoPro = new SliderCentered(3);
        this._modulator = new Slider(2);
        this._activity = null;
        this._drawRect = new Rect();
        this._controlRect = new Rect();
        this._paint = new Paint(1);
        this._tpaint = new TextPaint(1);
        this._tmpRect = new Rect();
        this._frequency = 220.0d;
        this._player = null;
        this._isPlaying = false;
        this._generatorIndex = 0;
        this._soundPlayer = null;
        this._colorBack = Colors.BLUE_BACK;
        this._isPro = false;
        setLayerType(1, null);
    }

    public void setBackColor(int i) {
        this._colorBack = i;
    }

    public SoundPlayer.Generator generator() {
        if (this._soundPlayer == null) {
            return null;
        }
        return this._soundPlayer.getGenerator(this._generatorIndex);
    }

    public void finaize() {
        try {
            this._typeSelector.setListener(null);
            this._selector.setListener(null);
            this._playBtn.setListener(null);
            this._volume.setListener(null);
            this._modulator.setListener(null);
            this._stereoPro.setListener(null);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void init(SoundGenerator soundGenerator, Player player, int i) {
        this._activity = new WeakReference<>(soundGenerator);
        this._generatorIndex = i;
        this._player = player;
        this._typeSelector.setListener(this);
        this._volume.setListener(this);
        this._modulator.setListener(this);
        this._isPro = true;
        this._stereoPro.setListener(this);
        this._selector.setListener(this);
        this._soundPlayer = this._player._sound;
        this._playBtn.setText("Play");
        this._playBtn.setListener(this);
        setHapticFeedbackEnabled(true);
        setControlsBounds();
        reloadGeneratorSettings();
    }

    public void setPro(boolean z) {
        try {
            boolean z2 = this._isPro;
            this._isPro = z;
            if (z2 != this._isPro) {
                setControlsBounds();
                postInvalidate();
                requestLayout();
            }
        } catch (Throwable unused) {
        }
    }

    public void reloadGeneratorSettings() {
        try {
            SoundPlayer.Generator generator = generator();
            if (generator != null) {
                this._volume.setValue(Math.sqrt(generator.getVolume()));
                this._modulator.setValue(generator.getModulation());
                double d = generator.f71_L;
                double d2 = 0.5d;
                if (d < 0.999d) {
                    d2 = d / 2.0d;
                } else {
                    double d3 = generator.f72_R;
                    if (d3 < 0.999d) {
                        d2 = 0.5d + ((1.0d - d3) / 2.0d);
                    }
                }
                this._stereoPro.setValue(d2);


                int i = 0;

                this._typeSelector.setType(i);
            }
        } catch (Throwable unused) {
        }
    }

    public void startPlayIfOn() {
        try {
            SoundPlayer.Generator generator = generator();

            startPlay(true);
        } catch (Throwable th) {
            th.printStackTrace();
        }
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
        drawFace(canvas);
    }

    protected void drawBackground(Canvas canvas) {
        this._paint.setColor(this._colorBack);
        this._paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(this._drawRect, this._paint);
    }

    protected void drawFace(Canvas canvas) {
        int left=0;
        this._tpaint.setStyle(Paint.Style.FILL_AND_STROKE);
        if (this._modulator.getValue() < 1.0E-4d) {
            this._tpaint.setColor(Colors.GREY_LIGHT);
        } else {
            this._tpaint.setColor(Colors.PINK);
        }
        float height = this._modulator.getHeight() / 4;
        this._tpaint.setTextSize(height);
        int left2 = this._modulator.getLeft();
        int pointerTop = this._modulator.getPointerTop() - (((int) height) / 2);
        this._tpaint.getTextBounds("modulation", 0, "modulation".length(), this._tmpRect);
        canvas.drawText("modulation", left2 + ((this._modulator.getWidth() - this._tmpRect.width()) / 2), pointerTop, this._tpaint);
        if (this._isPro) {
            this._tpaint.setColor(Colors.GREY_LIGHT);
            float lineTextTopY = (int) (this._stereoPro.getLineTextTopY() + height);
            canvas.drawText("L", this._stereoPro.getLeft(), lineTextTopY, this._tpaint);
            this._tpaint.getTextBounds("R", 0, "R".length(), this._tmpRect);
            canvas.drawText("R", left + (this._stereoPro.getWidth() - this._tmpRect.width()), lineTextTopY, this._tpaint);
        }
        this._fdisplay.draw(canvas, this._frequency);
        this._selector.draw(canvas);
        this._playBtn.draw(canvas);
        this._typeSelector.draw(canvas);
        this._volume.draw(canvas);
        this._modulator.draw(canvas);
        if (this._isPro) {
            this._stereoPro.draw(canvas);
        }
    }

    public void start(Activity activity) {
        System.gc();
    }

    public void stop() {
        try {
            if (this._player != null) {
                this._player.stop();
            }
            System.gc();
            this._playBtn._state = 0;
        } catch (Throwable unused) {
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        Log.e("gaga","yues44");
        try {
            if (this._typeSelector.handleTouchEvent(motionEvent) || this._volume.handleTouchEvent(motionEvent) || this._modulator.handleTouchEvent(motionEvent)) {
                return true;
            }
            if ((this._isPro && this._stereoPro.handleTouchEvent(motionEvent)) || this._selector.handleTouchEvent(motionEvent)) {
                return true;
            }
            return this._playBtn.handleTouchEvent(motionEvent) ? true : true;
        } catch (Throwable unused) {
            return true;
        }

    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        setControlsBounds();
    }

    protected boolean setControlsBounds() {
        boolean z;
        int i;
        try {
            getDrawingRect(this._drawRect);
            int width = this._drawRect.width();
            int height = this._drawRect.height();
            if (width > height) {
                i = height;
                z = false;
            } else {
                z = true;
                i = width;
            }
            if (z) {
                int i2 = (i * 7) / 10;
                int i3 = i2 / 2;
                int i4 = i3 / 12;
                int i5 = this._drawRect.left;
                int i6 = this._drawRect.top;
                this._tmpRect.set(this._drawRect.left + i4, i6, this._drawRect.right, (i3 / 2) + i6);
                this._typeSelector.setBounds(this._tmpRect);
                int i7 = this._drawRect.left;
                int i8 = i6 + (i3 / 2);
                int i9 = (i / 6) + i8;
                this._tmpRect.set(i7, i8, this._drawRect.right, i9);
                this._fdisplay.setBounds(this._tmpRect);
                int i10 = ((width - i2) / 2) + i7;
                int i11 = i8 + i2;
                this._tmpRect.set(i10, i9, i10 + i2, i11);
                this._selector.setBounds(this._tmpRect);
                int i12 = this._drawRect.bottom - i11;
                int i13 = (i12 * 2) / 7;
                int i14 = this._drawRect.right - i4;
                int i15 = i7 + i4;
                int i16 = i11 + i4;
                int i17 = i11 + i13;
                this._tmpRect.set(i15, i16, i14, i17 + i4);
                this._volume.setBounds(this._tmpRect);
                int i18 = (this._drawRect.left + this._drawRect.right) / 2;
                this._tmpRect.set(i15, (i4 * 3) + i17, i18 - i4, this._drawRect.bottom - i4);
                this._playBtn.setBounds(this._tmpRect);
                int i19 = (i12 - i13) / 2;
                int i20 = i17 + i19;
                this._tmpRect.set(i18, i17, i14, i20);
                this._stereoPro.setBounds(this._tmpRect);
                if (!this._isPro) {
                    i20 = i17 + (i19 / 2);
                }
                this._tmpRect.set(i18, i20, i14, i19 + i20);
                this._modulator.setBounds(this._tmpRect);
            } else {
                int i21 = (i * 9) / 10;
                int i22 = this._drawRect.right - i21;
                int i23 = this._drawRect.top;
                int i24 = i22 + i21;
                int i25 = (i / 5) + i23;
                this._tmpRect.set(i22, i23, i24, i25);
                this._fdisplay.setBounds(this._tmpRect);
                int i26 = i23 + i21;
                this._tmpRect.set(i22, i25, i24, i26);
                this._selector.setBounds(this._tmpRect);
                int i27 = i21 / 2;
                int i28 = this._drawRect.left;
                this._tmpRect.set(i28, i23, this._drawRect.right - i21, (i27 / 2) + i23);
                this._typeSelector.setBounds(this._tmpRect);
                int i29 = this._drawRect.right - i21;
                int i30 = i23 + i27;
                this._tmpRect.set((i27 / 10) + i28, (i27 / 2) + i23, i29, i30);
                this._volume.setBounds(this._tmpRect);
                int i31 = (i29 - i28) / 2;
                int i32 = (i29 - i31) / 12;
                this._tmpRect.set(i28 + i32, (i27 / 10) + i30 + i32, i31 - i32, i26);
                this._playBtn.setBounds(this._tmpRect);
                int i33 = ((this._drawRect.bottom - i23) - i27) / 2;
                int i34 = i33 / 2;
                this._tmpRect.set(i31, i30, i29, i30 + i33);
                this._stereoPro.setBounds(this._tmpRect);
                if (this._isPro) {
                    i34 = i33;
                }
                int i35 = i30 + i34;
                this._tmpRect.set(i31, i35, i29, i33 + i35);
                this._modulator.setBounds(this._tmpRect);
            }
            return z;
        } catch (Throwable unused) {
            return false;
        }
    }

    public void postRedrawUI() {
        SoundGenerator activity = activity();
        if (activity == null) {
            return;
        }
        try {
            activity.runOnUiThread(new Runnable() { // from class: com.finestandroid.soundgenerator.GeneratorView.1
                @Override // java.lang.Runnable
                public void run() {
                    GeneratorView.this.invalidate();
                }
            });
        } catch (Throwable unused) {
        }
    }

    @Override // com.finestandroid.soundgenerator.PushBtn.ButtonListener
    public void onClick(int i) {
        if (i == 5) {
            try {
                if (!this._playBtn.isOn()) {
                    stopPlay();
                } else {
                    startPlay(true);
                }
            } catch (Throwable unused) {
            }
        }
    }

    public void startPlay(boolean z) {
        this._isPlaying = true;
        try {
            SoundPlayer.Generator generator = generator();
            if (generator != null) {
                synchronized (this._soundPlayer) {
                    generator.setOn(true, this._soundPlayer.framePos());
                }
            }
            this._playBtn.switchOn();
            if (this._player == null) {
                return;
            }
            this._player.playSound();
            postRedraw();
        } catch (Throwable unused) {
        }
    }

    public void stopPlay() {
        this._isPlaying = false;
        try {
            SoundPlayer.Generator generator = generator();
            if (generator != null) {
                generator.stop();
            }
            this._playBtn.switchOff();
            if (this._player == null) {
                return;
            }
            this._player.stopSound();
            postRedraw();
        } catch (Throwable unused) {
        }
    }

    @Override // com.finestandroid.soundgenerator.PushBtn.ButtonListener
    public void playClick(int i) {
        if (this._player == null) {
        }
    }

    @Override // com.finestandroid.soundgenerator.PushBtn.ButtonListener
    public void redraw() {
        postInvalidate();
    }

    protected Player getPlayer() {
        return this._player;
    }

    public void postRedraw() {
        try {
            activity().runOnUiThread(new Runnable() { // from class: com.finestandroid.soundgenerator.GeneratorView.2
                @Override // java.lang.Runnable
                public void run() {
                    GeneratorView.this.postInvalidate();
                }
            });
        } catch (Throwable unused) {
        }
    }

    public void setFrequency(double d) {
        if (d > 20000.0d) {
            this._frequency = 20000.0d;
        } else if (d < 0.1d) {
            this._frequency = 0.1d;
        } else {
            this._frequency = d;
        }
        SoundPlayer.Generator generator = generator();
        if (generator != null) {
            if (this._soundPlayer.isPlaying()) {
                generator.setFrequency(this._frequency);
            } else {
                generator.setFrequencyForced(this._frequency);
            }
        }
        if (this._selector != null) {
            this._selector.setValuesOn360_Outside((int) (((this._frequency * 20.0d) / 20000.0d) + 30.0d));
            this._selector.setValuesOn360_Inside((int) (((this._frequency * 50.0d) / 20000.0d) + 50.0d));
        }
    }

    @Override // com.finestandroid.soundgenerator.FrequencySelector.FrequencySelectorListener
    public void changeValue(float f, boolean z) {
        double d;
        float f2;
        double d2;
        try {
            if (z) {
                if (this._frequency < 1000.0d) {
                    d2 = ((this._frequency * 100.0d) / 20000.0d) + 1.0d;
                } else {
                    d2 = (((this._frequency * 10.0d) + 100000.0d) / 20000.0d) + 1.0d;
                }
                double d3 = f;
                Double.isNaN(d3);
                f2 = (int) (d3 * d2);
                this._frequency = (int) this._frequency;
            } else {
                if (this._frequency < 10.0d) {
                    d = 0.01d;
                } else if (this._frequency < 20.0d) {
                    d = 0.05d;
                } else if (this._frequency < 50.0d) {
                    d = 0.1d;
                } else if (this._frequency < 80.0d) {
                    d = 0.2d;
                } else {
                    d = this._frequency < 150.0d ? 0.5d : 1.0d;
                }
                double d4 = f;
                Double.isNaN(d4);
                f2 = (float) (d4 * d);
                double d5 = this._frequency;
                double d6 = f2;
                Double.isNaN(d6);
                double d7 = d5 + d6;
                int i = (int) (10.0d * d7);
                if (this._frequency < 50.0d && d7 >= 50.0d) {
                    if (i % 2 != 0) {
                        Double.isNaN(d6);
                        f2 = (float) (d6 + 0.1d);
                    }
                } else if (this._frequency < 80.0d) {
                    if (d7 >= 80.0d && i % 5 != 0) {
                        double d8 = 5 - (i % 5);
                        Double.isNaN(d8);
                        Double.isNaN(d6);
                        f2 = (float) (d6 + (d8 * 0.1d));
                    }
                } else if (this._frequency < 150.0d) {
                    if (d7 < 80.0d) {
                        if (i % 2 != 0) {
                            Double.isNaN(d6);
                            f2 = (float) (d6 - 0.1d);
                        }
                    } else if (d7 >= 150.0d && i % 10 != 0) {
                        Double.isNaN(d6);
                        f2 = (float) (d6 + 0.5d);
                    }
                }
            }
            double d9 = this._frequency;
            double d10 = f2;
            Double.isNaN(d10);
            double d11 = d9 + d10;
            if (d11 <= 0.0d) {
                d11 = 0.1d;
            }
            setFrequency(d11);
            postInvalidate();
        } catch (Throwable unused) {
        }
    }

    @Override // com.finestandroid.soundgenerator.WaveTypeSelector.WaveTypeListener
    public void doredraw() {
        postRedraw();
    }

    @Override // com.finestandroid.soundgenerator.WaveTypeSelector.WaveTypeListener
    public void onWaveTypeChanged(int i) {
        try {
            SoundPlayer.Generator generator = generator();
            if (generator == null) {
                return;
            }
            generator.setType(i);
            postRedraw();
        } catch (Throwable unused) {
        }
    }

    @Override
    public void sliderredraw() {
        postRedraw();
    }

    @Override
    public void onSliderChanged(int i, double d) {
        double d2;
        try {
            switch (i) {
                case 0:
                    SoundPlayer.Generator generator = generator();
                    if (generator != null) {
                        generator.setVolume(d * d);
                        return;
                    }
                    return;
                case 1:
                    SoundPlayer.Generator generator2 = generator();
                    if (generator2 != null) {
                        generator2.setFaze(d * 3.141592653589793d);
                        return;
                    }
                    return;
                case 2:
                    SoundPlayer.Generator generator3 = generator();
                    if (generator3 != null) {
                        generator3.setModulation(d);
                        return;
                    }
                    return;
                case 3:
                    SoundPlayer.Generator generator4 = generator();
                    if (generator4 != null) {
                        double d3 = 1.0d;
                        if (d < 0.4999d) {
                            double d4 = d * 2.0d;
                            d2 = 1.0d;
                            d3 = d4;
                        } else {
                            d2 = d > 0.5005d ? (1.0d - d) * 2.0d : 1.0d;
                        }
                        generator4.f71_L = d3;
                        generator4.f72_R = d2;
                        return;
                    }
                    return;
                default:
                    return;
            }
        } catch (Throwable unused) {
        }
    }
}
