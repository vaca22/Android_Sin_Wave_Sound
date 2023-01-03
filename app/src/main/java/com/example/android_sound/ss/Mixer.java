package com.example.android_sound.ss;

/* loaded from: classes.dex */
public class Mixer {
    protected short[] _audio;
    protected double[] _bus;
    private int _maxVal = 32767;
    private int _trLevel = (this._maxVal * 19) / 20;
    private int _release = 100;
    private int _attack = 100;
    private int _fadeInCounter = 0;
    private boolean _fadeIn = true;
    private int _fadeOutCounter = 0;
    private boolean _fadeOut = false;
    private double _maxTrK = 1.0d;

    public Mixer(int i) {
        this._bus = null;
        this._audio = null;
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

    public boolean isOut() {
        return this._fadeOut && this._fadeOutCounter >= this._release;
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


//    public short[] getAudioData() {
//        double d;
//        int i;
//        boolean z;
//        int i2;
//        double d2;
//        double d3;
//        double d4;
//        double d5;
//        int i3;
//        boolean z2;
//        int i4;
//        int length = this._bus.length;
//        double d6 = 0.0d;
//        double d7 = 0.0d;
//        for (int i5 = 0; i5 < length; i5 += 2) {
//            double d8 = this._bus[i5];
//            if (d8 > 0.0d) {
//                if (d8 > d7) {
//                    d7 = d8;
//                }
//            } else if (d8 < (-d7)) {
//                d7 = -d8;
//            }
//        }
//        if (d7 > this._maxVal) {
//            double d9 = d7 * 0.96d;
//            if (d9 > this._maxVal) {
//                double d10 = this._maxVal;
//                Double.isNaN(d10);
//                d = d10 / d9;
//                double d11 = 0.0d;
//                i = 0;
//                z = false;
//                boolean z3 = true;
//                int i6 = -1;
//                while (i < length) {
//                    double d12 = this._bus[i] * d;
//                    this._bus[i] = d12;
//                    int i7 = i + 1;
//                    this._bus[i7] = this._bus[i7] * d;
//                    if (z) {
//                        d5 = d;
//                        if (!z3) {
//                            i3 = i;
//                            z2 = z3;
//                            if (d12 >= d11) {
//                                if (d12 > (-this._trLevel)) {
//                                    d11 = -d11;
//                                    double d13 = this._maxVal;
//                                    double d14 = this._trLevel;
//                                    Double.isNaN(d13);
//                                    Double.isNaN(d14);
//                                    double d15 = d13 - d14;
//                                    double d16 = this._trLevel;
//                                    Double.isNaN(d16);
//                                    double d17 = d15 / (d11 - d16);
//                                    int i8 = i6;
//                                    i4 = i3;
//                                    while (i8 < i4) {
//                                        double d18 = this._trLevel;
//                                        double d19 = this._trLevel;
//                                        Double.isNaN(d19);
//                                        Double.isNaN(d18);
//                                        this._bus[i8] = -(d18 + (((-this._bus[i8]) - d19) * d17));
//                                        i8++;
//                                        d11 = d11;
//                                    }
//                                    z = false;
//                                    if (z) {
//                                    }
//                                }
//                                i4 = i3;
//                                if (z) {
//                                }
//                            }
//                            d11 = d12;
//                            i4 = i3;
//                            if (z) {
//                            }
//                        } else if (d12 > d11) {
//                            i3 = i;
//                            z2 = z3;
//                            d11 = d12;
//                            i4 = i3;
//                            if (z) {
//                                i = i4 - 2;
//                                z3 = z2;
//                                i6 = -1;
//                            } else {
//                                i = i4;
//                                z3 = z2;
//                            }
//                        } else if (d12 < this._trLevel) {
//                            double d20 = this._maxVal;
//                            double d21 = this._trLevel;
//                            Double.isNaN(d20);
//                            Double.isNaN(d21);
//                            double d22 = d20 - d21;
//                            double d23 = this._trLevel;
//                            Double.isNaN(d23);
//                            double d24 = d22 / (d11 - d23);
//                            int i9 = i6;
//                            while (i9 < i) {
//                                double d25 = this._bus[i9];
//                                double d26 = this._trLevel;
//                                double d27 = this._trLevel;
//                                Double.isNaN(d27);
//                                Double.isNaN(d26);
//                                this._bus[i9] = d26 + ((d25 - d27) * d24);
//                                i9++;
//                                i = i;
//                                z3 = z3;
//                            }
//                            z2 = z3;
//                            i4 = i;
//                            z = false;
//                            if (z) {
//                            }
//                        } else {
//                            z2 = z3;
//                            i4 = i;
//                            if (z) {
//                            }
//                        }
//                    } else {
//                        d5 = d;
//                        if (d12 > this._maxVal) {
//                            if (i6 < 0 || this._bus[i6] < d6) {
//                                i6 = i;
//                            }
//                            d11 = d12;
//                            z = true;
//                            z3 = true;
//                        } else if (d12 < (-this._maxVal)) {
//                            if (i6 < 0 || this._bus[i6] > d6) {
//                                i6 = i;
//                            }
//                            d11 = d12;
//                            z = true;
//                            z3 = false;
//                        } else if (i6 < 0) {
//                            if (d12 <= this._trLevel && d12 >= (-this._trLevel)) {
//                            }
//                            i6 = i;
//                        } else if (this._bus[i6] > d6) {
//                            if (d12 >= (-this._trLevel)) {
//                                if (d12 >= this._trLevel) {
//                                }
//                                i6 = -1;
//                            }
//                            i6 = i;
//                        } else {
//                            if (d12 <= this._trLevel) {
//                                if (d12 <= (-this._trLevel)) {
//                                }
//                                i6 = -1;
//                            }
//                            i6 = i;
//                        }
//                    }
//                    i += 2;
//                    d = d5;
//                    d6 = 0.0d;
//                }
//                boolean z4 = z3;
//                if (z) {
//                    if (z4) {
//                        double d28 = this._maxVal;
//                        double d29 = this._trLevel;
//                        Double.isNaN(d28);
//                        Double.isNaN(d29);
//                        double d30 = d28 - d29;
//                        double d31 = this._trLevel;
//                        Double.isNaN(d31);
//                        double d32 = d30 / (d11 - d31);
//                        while (i6 < length) {
//                            double d33 = this._bus[i6];
//                            double d34 = this._trLevel;
//                            double d35 = this._trLevel;
//                            Double.isNaN(d35);
//                            Double.isNaN(d34);
//                            this._bus[i6] = d34 + ((d33 - d35) * d32);
//                            i6++;
//                        }
//                    } else {
//                        double d36 = this._maxVal;
//                        double d37 = this._trLevel;
//                        Double.isNaN(d36);
//                        Double.isNaN(d37);
//                        double d38 = d36 - d37;
//                        double d39 = this._trLevel;
//                        Double.isNaN(d39);
//                        double d40 = d38 / ((-d11) - d39);
//                        while (i6 < length) {
//                            double d41 = -this._bus[i6];
//                            double d42 = this._trLevel;
//                            double d43 = this._trLevel;
//                            Double.isNaN(d43);
//                            Double.isNaN(d42);
//                            this._bus[i6] = -(d42 + ((d41 - d43) * d40));
//                            i6++;
//                        }
//                    }
//                }
//                for (i2 = 0; i2 < length; i2++) {
//                    if (this._fadeOut) {
//                        if (this._fadeOutCounter >= this._release) {
//                            d4 = 0.0d;
//                        } else {
//                            double d44 = this._fadeOutCounter;
//                            double d45 = this._release;
//                            Double.isNaN(d44);
//                            Double.isNaN(d45);
//                            d4 = 1.0d - (d44 / d45);
//                        }
//                        if (i2 % 2 == 1) {
//                            this._fadeOutCounter++;
//                        }
//                        d2 = this._bus[i2] * d4;
//                    } else if (this._fadeIn) {
//                        if (this._fadeInCounter >= this._attack) {
//                            this._fadeIn = false;
//                            d3 = 1.0d;
//                        } else {
//                            double d46 = this._fadeInCounter;
//                            double d47 = this._attack;
//                            Double.isNaN(d46);
//                            Double.isNaN(d47);
//                            d3 = d46 / d47;
//                        }
//                        if (i2 % 2 == 1) {
//                            this._fadeInCounter++;
//                        }
//                        d2 = this._bus[i2] * d3;
//                    } else {
//                        d2 = this._bus[i2];
//                    }
//                    if (d2 < (-this._maxVal)) {
//                        d2 = -this._maxVal;
//                    } else if (d2 > this._maxVal) {
//                        d2 = this._maxVal;
//                    }
//                    this._audio[i2] = (short) d2;
//                }
//                return this._audio;
//            }
//        }
//        d = 1.0d;
//        double d112 = 0.0d;
//        i = 0;
//        z = false;
//        boolean z32 = true;
//        int i62 = -1;
//        while (i < length) {
//        }
//        boolean z42 = z32;
//        if (z) {
//        }
//        while (i2 < length) {
//        }
//        return this._audio;
//    }

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
            Double.isNaN(d5);
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
                    Double.isNaN(d8);
                    Double.isNaN(d9);
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
                    Double.isNaN(d10);
                    Double.isNaN(d11);
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
