package com.example.android_sound.ss;

/* loaded from: classes.dex */
public class Mixer {
    protected short[] _audio;
    protected double[] _bus;
    private int _maxVal = 32767;
    private int _release = 100;
    private int _attack = 100;
    private int _fadeInCounter = 0;
    private boolean _fadeIn = true;
    private int _fadeOutCounter = 0;
    private boolean _fadeOut = false;
    private double _maxTrK = 1.0d;

    public Mixer(int i) {
        i = i < 0 ? 100 : i;
        this._bus = new double[i];
        this._audio = new short[i];
    }

    public void resetLimit() {
        this._maxTrK = 1.0d;
    }

    public void setRelease(int i) {
        this._release = i;
    }

    public void setAttack(int i) {
        this._attack = i;
    }

    public void stop() {
        this._fadeOut = true;
        this._fadeOutCounter = 0;
    }

    public void start() {
        this._fadeIn = true;
        this._fadeOut = false;
        this._fadeOutCounter = 0;
        this._fadeInCounter = 0;
        resetLimit();
    }



    public double[] getBus() {
        return this._bus;
    }

    public void clearBus() {
        int length = this._bus.length;
        for (int i = 0; i < length; i++) {
            this._bus[i] = 0.0d;
        }
    }


    public short[] getAudioDataClean() {
        double d;
        double d2;
        int length = this._bus.length;
        double d3 = 0.0d;
        for (int i = 0; i < length; i++) {
            double d4 = this._bus[i];
            if (d4 > 0.0d) {
                if (d4 > d3) {
                    d3 = d4;
                }
            } else if (d4 < (-d3)) {
                d3 = -d4;
            }
        }
        if (d3 > this._maxVal) {
            double d5 = this._maxVal;
            double d6 = d5 / d3;
            if (this._maxTrK > d6) {
                this._maxTrK = d6;
            }
        }
        for (int i2 = 0; i2 < length; i2++) {
            double d7 = 1.0d;
            if (this._fadeOut) {
                if (this._fadeOutCounter >= this._release) {
                    d2 = 0.0d;
                } else {
                    double d8 = this._fadeOutCounter;
                    double d9 = this._release;
                    d2 = 1.0d - (d8 / d9);
                }
                if (i2 % 2 == 1) {
                    this._fadeOutCounter++;
                }
                d = this._bus[i2] * this._maxTrK * d2;
            } else if (this._fadeIn) {
                if (this._fadeInCounter >= this._attack) {
                    this._fadeIn = false;
                } else {
                    double d10 = this._fadeInCounter;
                    double d11 = this._attack;
                    d7 = d10 / d11;
                }
                if (i2 % 2 == 1) {
                    this._fadeInCounter++;
                }
                d = this._bus[i2] * this._maxTrK * d7;
            } else {
                d = this._maxTrK * this._bus[i2];
            }
            if (d < (-this._maxVal)) {
                d = -this._maxVal;
            } else if (d > this._maxVal) {
                d = this._maxVal;
            }
            this._audio[i2] = (short) d;
        }
        return this._audio;
    }

    public int getBusLen() {
        return this._bus.length;
    }
}
