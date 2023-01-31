package com.example.android_sound.ss;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


public class SoundPlayer {
    protected static final int CONF = 12;
    protected static final int FORMAT = 2;
    protected static final int MODE = 1;
    protected static final int SAMPLE_RATE = 44100;
    protected static final int STREAM_TYPE = 3;
    protected static final String TEST_NAME = "GeneratedTrack";
    protected long _framePos = 0;
    private AudioTrack _audio = null;
    private AudioThread _thread = null;
    private FeedThread _feeder = null;
    private int _bufSize = 100;
    private boolean _stoped = false;
    private Generator[] _generators = null;
    private Mixer _mixer = null;
    private boolean _isPlaying = false;

    public SoundPlayer(Context context) {
        initAudio();
        createGenertors();
    }

    public void createGenertors() {
        this._generators = new Generator[1];
        this._generators[0] = new Generator();
        this._generators[0].setFrequency(500);
        this._generators[0].setRelease(441);
        this._generators[0].setAttack(220);
    }



    public void resetGenerators() {
        try {
            int length = this._generators.length;
            for (Generator generator : this._generators) {
                generator.reset();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void initAudio() {
        try {
            this._bufSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
            this._mixer = new Mixer(this._bufSize / 4);
            this._mixer.setRelease(441);
            this._mixer.setAttack(220);
            createAudioTrack(this._mixer.getBusLen() / 2);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    protected void createAudioTrack(int i) {

        AudioAttributes build = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
        AudioFormat.Builder builder = new AudioFormat.Builder();
        builder.setEncoding(AudioFormat.ENCODING_PCM_16BIT);
        builder.setChannelMask(12);
        builder.setSampleRate(SAMPLE_RATE);
        this._audio = new AudioTrack(build, builder.build(), this._bufSize, 1, 0);

        if (this._audio.getState() != 1) {
            return;
        }
        this._feeder = new FeedThread(i * 2);
        this._feeder.start();
    }

    protected void initAudioFeed(Handler handler, int i) {
        this._audio.setPlaybackPositionUpdateListener(new AudioPosListener(), handler);
        this._audio.setPositionNotificationPeriod(i);
    }

    public synchronized void generate() {
        if (this._audio == null) {
            return;
        }
        loadAudioBuffer();
        short[] audio = getAudio();

        this._audio.write(audio, 0, audio.length);
        loadAudioBuffer();
        short[] audio2 = getAudio();
        this._audio.write(audio2, 0, audio2.length);
        loadAudioBuffer();
        short[] audio3 = getAudio();
        this._audio.write(audio3, 0, audio3.length);
        loadAudioBuffer();
        short[] audio4 = getAudio();
        this._audio.write(audio4, 0, audio4.length);
        this._stoped = false;
        this._audio.play();
    }


    public void feed() {
        loadAudioBuffer();
        short[] audio = getAudio();
        this._audio.write(audio, 0, audio.length);
        loadAudioBuffer();
        short[] audio2 = getAudio();
        this._audio.write(audio2, 0, audio2.length);
        loadAudioBuffer();
        short[] audio3 = getAudio();
        this._audio.write(audio3, 0, audio3.length);
        loadAudioBuffer();
        short[] audio4 = getAudio();
        this._audio.write(audio4, 0, audio4.length);
    }

    private short[] getAudio() {
        return this._mixer.getAudioDataClean();
    }

    protected synchronized void loadAudioBuffer() {
        this._mixer.clearBus();
        double[] bus = this._mixer.getBus();
        int length = bus.length;
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (i2 < length) {
                for (Generator generator : this._generators) {
                    short nextSample = generator.nextSample();
                    double d = bus[i];
                    double d3 = generator.f72_R;
                    bus[i] = d + (d3 * (double) nextSample);
                    double d4 = bus[i2];
                    double d5 = generator.f71_L;
                    bus[i2] = d4 + ((double) nextSample * d5);
                }
                this._framePos++;
                i += 2;
            }else{
                break;
            }
        }

    }

    public void play() {
        resetGenerators();
        if (this._audio == null) {
            try {
                createAudioTrack(this._mixer.getBusLen() / 2);
            } catch (Throwable ignored) {
            }
        }
        this._framePos = 0L;
        this._isPlaying = true;
        this._mixer.start();
        this._thread = new AudioThread();
        this._thread.start();
    }

    protected void finalize() {
        try {
            if (this._audio == null) {
                return;
            }
            this._isPlaying = false;
            this._audio.flush();
            this._audio.stop();
            this._audio.release();
            this._audio = null;
            this._feeder.stopLoop();
        } catch (Throwable unused) {
        }
    }

    public void stop() {
        this._mixer.stop();
        this._mixer.resetLimit();
        this._isPlaying = false;
    }

    public long framePos() {
        return this._framePos;
    }

    private void stopPrv() {
        try {
            if (this._stoped) {
                return;
            }
            this._framePos = 0L;
            this._stoped = true;
            this._isPlaying = false;
            if (this._audio == null) {
                return;
            }
            this._audio.flush();
            this._audio.stop();
            this._audio.release();
            this._feeder.stopLoop();
            this._audio = null;
            System.gc();
        } catch (Throwable unused) {
        }
    }

    public boolean isPlaying() {
        return this._isPlaying;
    }

    public void onGeneratorStopped() {
        if (this._mixer == null) {
            return;
        }
        this._mixer.resetLimit();
    }

    /* loaded from: classes.dex */
    public static class Generator {
        public static final int TYPE_SINE = 0;
        public static final int TYPE_SQUIRE = 1;
        public static final int TYPE_UP = 2;
        protected static final short _maxA = Short.MAX_VALUE;
        protected static double DOUBLE_PRECISION = 1.0E-5d;
        public double _modulation = 0.0d;
        /* renamed from: _L */
        public double f71_L = 1.0d;
        /* renamed from: _R */
        public double f72_R = 1.0d;
        protected boolean _bOn = false;
        protected long _framePos = 0;
        protected short _ATop = 24575;
        protected double _frequency = 220.0d;
        protected double _newfrequency = 220.0d;
        protected boolean _changeF = false;
        protected boolean _wasNegative = false;
        protected double _alphaError = 0.0d;
        protected double _volume = 0.5d;
        protected int _type = 0;
        private int _release = 100;
        private int _attack = 100;
        private int _fadeInCounter = 0;
        private boolean _fadeIn = true;
        private int _fadeOutCounter = 0;
        private boolean _fadeOut = false;
        private double _faze = 0.0d;

        public void setType(int i) {
            this._type = i;
        }

        public double getModulation() {
            return this._modulation;
        }

        public void setModulation(double d) {
            this._modulation = d;
        }

        public double getVolume() {
            return this._volume;
        }

        public void setVolume(double d) {
            this._volume = d;
        }

        public double getFaze() {
            return this._faze;
        }

        public void setFaze(double d) {
            if (d < DOUBLE_PRECISION) {
                d = 0.0d;
            }
            if (d > 6.283185307179586d) {
                d = 0.0d;
            }
            this._faze = d;
        }

        public short getMaxA() {
            return (short) (this._volume * 32767.0d);
        }

        public void setFrequencyForced(double d) {
            this._newfrequency = d;
            this._changeF = false;
            this._wasNegative = false;
            this._frequency = this._newfrequency;
            this._framePos = 0L;
            this._alphaError = 0.0d;
        }

        public void setFrequency(double d) {
            this._newfrequency = d;
            this._wasNegative = false;
            this._frequency=d;
            this._changeF = true;
        }

        public void changeFrequency(double d) {
            this._frequency = this._newfrequency;
            this._alphaError = Math.asin(d) - this._faze;
            if (this._alphaError > 6.283185307179586d) {
                this._alphaError -= 6.283185307179586d;
            }
            this._framePos = 1L;
            this._wasNegative = false;
            this._changeF = false;
        }

        public void reset() {
            this._framePos = 0L;
            this._alphaError = 0.0d;
            this._changeF = false;
            this._wasNegative = false;
        }

        public short nextSample() {

                return nextSinSample();


        }

        public void setOn(boolean z, long j) {
            this._bOn = z;
            if (this._bOn) {
                this._framePos = j;
                this._fadeIn = true;
                this._fadeOut = false;
                this._fadeOutCounter = 0;
                this._fadeInCounter = 0;
                this._alphaError = 0.0d;
                this._changeF = false;
                this._wasNegative = false;
                return;
            }
            reset();
        }

        public void stop() {
            this._fadeOut = true;
            this._fadeOutCounter = 0;
        }

        private short nextSinSample() {
            double d = getMaxA();
            short s;
            double d4 = this._framePos;
            double sin = Math.sin((d4 * 2.2675736961451248E-5d * 2.0d * 3.141592653589793d * this._frequency) + this._alphaError + this._faze);
            if (this._modulation != 0.0d) {
                double d8 = (int) (60.0d - (this._modulation * 50.0d));
                double d9 = 1.0d / d8;
                double d10 = (int) (sin / d9);
                sin = d10 * d9;
                if (sin < 0.0d) {
                    sin *= (this._modulation / 2.0d) + 0.5d;
                }
            }

            s = (short) (d * sin);

            this._framePos++;
            if (this._changeF) {
                if (this._frequency <= 10.0d) {
                    changeFrequency(sin);
                } else if (s < 0) {
                    this._wasNegative = true;
                } else if (this._wasNegative) {
                    changeFrequency(sin);
                }
            }

            return s;
        }

        private short nextSquireSample() {
            double d = _volume;
            short s = (short) (this._volume * 32767.0d);
            double d2 = this._framePos;
            double d3 = d2 * 2.2675736961451248E-5d;
            double sin = Math.sin((d3 * 2.0d * 3.141592653589793d * this._frequency) + this._alphaError + this._faze);
            short s2 = (short) (d * sin);
            this._framePos++;
            if (this._changeF) {
                if (this._frequency <= 10.0d) {
                    changeFrequency(sin);
                } else if (s2 < 0) {
                    this._wasNegative = true;
                } else if (this._wasNegative) {
                    changeFrequency(sin);
                }
            }
            double d4 = 0.0d;
            double d5 = 1.0d;
            if (this._fadeOut) {
                if (this._fadeOutCounter >= this._release) {
                    this._bOn = false;
                } else {
                    double d6 = this._fadeOutCounter;
                    double d7 = this._release;
                    d4 = 1.0d - (d6 / d7);
                }
                this._fadeOutCounter++;
                if (sin > DOUBLE_PRECISION) {
                    return (short) (this._volume * 32767.0d * d4);
                }
                if (sin < DOUBLE_PRECISION) {
                    return (short) (-((short) (this._volume * 32767.0d * d4)));
                }
            } else if (this._fadeIn) {
                if (this._fadeInCounter >= this._attack) {
                    this._fadeIn = false;
                } else {
                    double d8 = this._fadeInCounter;
                    double d9 = this._attack;
                    d5 = d8 / d9;
                }
                this._fadeInCounter++;
                if (sin > DOUBLE_PRECISION) {
                    return (short) (this._volume * 32767.0d * d5);
                }
                if (sin < DOUBLE_PRECISION) {
                    return (short) (-((short) (this._volume * 32767.0d * d5)));
                }
            } else if (this._modulation != 0.0d) {
                double d10 = 1.0d / this._frequency;
                double d11 = (long) (d3 / d10);
                double d12 = d10 / 2.0d;
                return d3 - (d11 * d10) < d12 + ((this._modulation * d12) / 2.0d) ? s : (short) (-s);
            } else if (sin > DOUBLE_PRECISION) {
                return s;
            } else {
                if (sin < DOUBLE_PRECISION) {
                    return (short) (-s);
                }
            }
            return (short) 0;
        }

        private short nextUpSample() {
            short s;
            double d;
            double d2;
            double d3;
            double d4;
            double d5;
            double d6;
            short s2 = (short) (this._volume * 32767.0d);
            double d7 = 1.0d / this._frequency;
            double d8 = this._framePos;
            double d9 = d8 * 2.2675736961451248E-5d;
            double sin = Math.sin((d9 * 2.0d * 3.141592653589793d * this._frequency) + this._alphaError);
            double d10 = (d9 / d7) + ((this._faze * d7) / 6.283185307179586d);
            double d11 = (long) d10;
            double d12 = d10 - d11;
            if (this._fadeOut) {
                if (this._fadeOutCounter >= this._release) {
                    this._bOn = false;
                    d6 = 0.0d;
                } else {
                    double d13 = this._fadeOutCounter;
                    double d14 = this._release;
                    d6 = 1.0d - (d13 / d14);
                }
                this._fadeOutCounter++;
                double d15 = s2;
                s = (short) ((((d12 * 2.0d) * d15) - d15) * d6);
            } else if (this._fadeIn) {
                if (this._fadeInCounter >= this._attack) {
                    this._fadeIn = false;
                    d3 = 1.0d;
                } else {
                    double d16 = this._fadeInCounter;
                    double d17 = this._attack;
                    d3 = d16 / d17;
                }
                this._fadeInCounter++;
                double d18 = s2;
                s = (short) ((((d12 * 2.0d) * d18) - d18) * d3);
            } else if (this._modulation != 0.0d) {
                double d19 = d12 / (1.0d - (this._modulation / 2.0d));
                if (d19 >= 1.0d) {
                    d = 0.0d;
                } else {
                    double d20 = s2;
                    d = ((d19 * 2.0d) * d20) - d20;
                }
                double d21 = (s2 * 2) / ((int) (26.0d - (this._modulation * 20.0d)));
                d2 = ((int) (d / d21));
                s = (short) (d2 * d21);
            } else {
                double d22 = s2;
                s = (short) (((d12 * 2.0d) * d22) - d22);
            }
            this._framePos++;
            if (this._changeF) {
                if (this._frequency < 10.0d) {
                    changeFrequency(sin);
                    return s;
                } else if (sin < 0.0d) {
                    this._wasNegative = true;
                    return s;
                } else if (this._wasNegative) {
                    changeFrequency(sin);
                    double d23 = 1.0d / this._frequency;
                    d4 = ((d9 / d23) + ((this._faze * d23) / 6.283185307179586d));
                    d5 = ((long) ((d9 / d23) + ((this._faze * d23) / 6.283185307179586d)));
                    double d24 = s2;
                    return (short) ((((d4 - d5) * 2.0d) * d24) - d24);
                } else {
                    return s;
                }
            }
            return s;
        }

        public void setRelease(int i) {
            this._release = i;
        }

        public void setAttack(int i) {
            this._attack = i;
        }
    }

    /* loaded from: classes.dex */
    protected class AudioRunnable implements Runnable {


        @Override // java.lang.Runnable
        public void run() {

            SoundPlayer.this.generate();
        }
    }

    /* loaded from: classes.dex */
    protected class AudioThread extends Thread {
        @Override
        public void run() {
            new AudioRunnable().run();
        }


    }


    public class FeedThread extends Thread {
        protected Handler _handler = null;
        protected int _notifyPeriod;

        public FeedThread(int i) {
            this._notifyPeriod = i;
        }

        public void stopLoop() {
            try {
                Looper.myLooper().quit();
            } catch (Throwable unused) {
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                Looper.prepare();
                this._handler = new Handler();
                SoundPlayer.this.initAudioFeed(this._handler, this._notifyPeriod);
                Looper.loop();
            } catch (Throwable unused) {
            }
        }
    }


    public class AudioPosListener implements AudioTrack.OnPlaybackPositionUpdateListener {


        @Override // android.media.AudioTrack.OnPlaybackPositionUpdateListener
        public void onMarkerReached(AudioTrack audioTrack) {
        }

        @Override // android.media.AudioTrack.OnPlaybackPositionUpdateListener
        public void onPeriodicNotification(AudioTrack audioTrack) {

            SoundPlayer.this.feed();
        }
    }
}
