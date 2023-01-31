package com.example.android_sound;

import android.content.Context;
import android.os.SystemClock;

import com.example.android_sound.ss.SoundPlayer;

/* loaded from: classes.dex */
public class Player {
    public static final DrumLoop EMPTYLOOP4 = new EmptyLoop4();
    public static final DrumLoop EMPTYLOOP3 = new EmptyLoop3();
    public static final DrumLoop EMPTYLOOP44 = new EmptyLoop44();
    public static final DrumLoop EMPTYLOOP34 = new EmptyLoop34();
    protected static final DrumLoop LOOP1 = new Loop1();
    protected static final DrumLoop LOOP2 = new Loop2();
    protected static final DrumLoop LOOP3 = new Loop3();
    protected static final DrumLoop LOOP4 = new Loop4();
    protected static final DrumLoop LOOP5 = new Loop5();
    protected static final DrumLoop LOOP6 = new Loop6();
    protected static final DrumLoop LOOP7 = new Loop7();
    protected static final DrumLoop LOOP8 = new Loop8();
    protected SoundPlayer _sound = null;
    protected double _bpm = 120.0d;
    protected boolean _isPlaying = false;
    protected long _startTime = 0;
    protected double _step = 0.0d;
    protected double _prevTick = 0.0d;
    protected double _nextTick = 0.0d;
    protected PlayThread _thread = null;
    protected DrumLoop _takt = new Loop4();
    protected ITaktChangedListener _taktChangeListener = null;
    protected BPMChangeListener _bpmListener = null;
    private boolean _signalBPM = false;
    private Object _objSync = new Object();

    /* loaded from: classes.dex */
    public interface BPMChangeListener {
        void signalChange();
    }

    /* loaded from: classes.dex */
    public interface ITaktChangedListener {
        void setTakt(EditLoop editLoop);
    }

    /* loaded from: classes.dex */
    public static class Sounds {

        /* renamed from: B1 */
        public static final int f56B1 = 9;

        /* renamed from: B2 */
        public static final int f57B2 = 10;

        /* renamed from: C1 */
        public static final int f58C1 = 11;

        /* renamed from: C2 */
        public static final int f59C2 = 12;
        public static final int CLICK_BTN = 0;
        public static final int HH1 = 3;
        public static final int HH2 = 4;
        public static final int OHH1 = 1;
        public static final int OHH2 = 2;

        /* renamed from: R1 */
        public static final int f60R1 = 5;

        /* renamed from: R2 */
        public static final int f61R2 = 6;

        /* renamed from: S1 */
        public static final int f62S1 = 7;

        /* renamed from: S2 */
        public static final int f63S2 = 8;
    }

    /* loaded from: classes.dex */
    protected class PlayRunnable implements Runnable {
        protected PlayRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Player.this.play();
        }
    }

    /* loaded from: classes.dex */
    protected class PlayThread extends Thread {
        public PlayThread() {
            super(new PlayRunnable());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static abstract class Takt {
        protected int _count = 0;
        protected int _step = 0;

        public abstract int chanelsCount();

        public abstract int getChanelStream(int i);

        public abstract int getSampleID(int i);

        public abstract void setChanelStream(int i, int i2);

        protected Takt() {
        }

        public void reset() {
            this._step = 0;
        }

        public int getStepsCount() {
            return this._count;
        }

        public boolean hasNextStep() {
            return this._step < this._count - 1;
        }

        public void moveToNext() {
            if (this._step < this._count - 1) {
                this._step++;
            } else {
                this._step = 0;
            }
        }

        public int getStep() {
            return this._step;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class DrumTakt extends Takt {
        protected int[] _hh = null;
        protected int[] _ride = null;
        protected int[] _snare = null;
        protected int[] _bass = null;
        protected int[] _crush = null;
        protected int[] _streams = new int[6];

        @Override // com.finestandroid.soundgenerator.Player.Takt
        public int chanelsCount() {
            return 5;
        }

        protected DrumTakt() {
        }

        public void setPlayPos(int i) {
            this._step = i;
            if (this._step < 0) {
                this._step = 0;
            }
            if (this._count <= 0) {
                this._step = 0;
            } else if (this._step >= this._count) {
                this._step = this._count - 1;
            }
        }

        protected void init(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int[] iArr5) {
            this._hh = iArr;
            this._ride = iArr2;
            this._snare = iArr3;
            this._bass = iArr4;
            this._crush = iArr5;
            this._count = this._hh.length;
            int length = this._streams.length;
            for (int i = 0; i < length; i++) {
                this._streams[i] = 0;
            }
        }

        @Override // com.finestandroid.soundgenerator.Player.Takt
        public void reset() {
            super.reset();
            int length = this._streams.length;
            for (int i = 0; i < length; i++) {
                this._streams[i] = 0;
            }
        }

        @Override // com.finestandroid.soundgenerator.Player.Takt
        public int getSampleID(int i) {
            switch (i) {
                case 0:
                    if (this._hh[this._step] == 1) {
                        return 3;
                    }
                    if (this._hh[this._step] == 2) {
                        return 4;
                    }
                    if (this._hh[this._step] == 3) {
                        return 1;
                    }
                    return this._hh[this._step] == 4 ? 2 : 0;
                case 1:
                    if (this._ride[this._step] == 1) {
                        return 5;
                    }
                    return this._ride[this._step] == 2 ? 6 : 0;
                case 2:
                    if (this._snare[this._step] == 1) {
                        return 7;
                    }
                    return this._snare[this._step] == 2 ? 8 : 0;
                case 3:
                    if (this._bass[this._step] == 1) {
                        return 9;
                    }
                    return this._bass[this._step] == 2 ? 10 : 0;
                case 4:
                    if (this._crush[this._step] == 1) {
                        return 11;
                    }
                    return this._crush[this._step] == 2 ? 12 : 0;
                default:
                    return 0;
            }
        }

        @Override // com.finestandroid.soundgenerator.Player.Takt
        public int getChanelStream(int i) {
            return this._streams[i];
        }

        @Override // com.finestandroid.soundgenerator.Player.Takt
        public void setChanelStream(int i, int i2) {
            this._streams[i] = i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class DrumLoop extends Takt {
        protected DrumTakt _intro = null;
        protected DrumTakt[] _loop = null;
        protected DrumTakt _takt = null;
        protected int _loopIndex = 0;
        protected String _name = "";

        @Override // com.finestandroid.soundgenerator.Player.Takt
        public int chanelsCount() {
            return 5;
        }

        @Override // com.finestandroid.soundgenerator.Player.Takt
        public boolean hasNextStep() {
            return true;
        }

        protected void init(DrumTakt drumTakt, DrumTakt[] drumTaktArr) {
            this._intro = drumTakt;
            this._loop = drumTaktArr;
            this._count = this._intro.getStepsCount();
            int length = this._loop.length;
            for (int i = 0; i < length; i++) {
                this._count += this._loop[i].getStepsCount();
            }
            this._takt = this._intro;
            this._loopIndex = 0;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void setName(String str) {
            this._name = str;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public String getName() {
            return this._name == null ? "" : this._name;
        }

        @Override // com.finestandroid.soundgenerator.Player.Takt
        public void reset() {
            if (this._intro != null) {
                this._intro.reset();
            }
            this._loopIndex = 0;
            int length = this._loop.length;
            for (int i = 0; i < length; i++) {
                this._loop[i].reset();
            }
            this._takt = this._intro;
            if (this._takt != null || length <= 0) {
                return;
            }
            this._takt = this._loop[0];
        }

        public boolean isSet() {
            return this._takt != null;
        }

        @Override // com.finestandroid.soundgenerator.Player.Takt
        public int getStepsCount() {
            return this._count;
        }

        @Override // com.finestandroid.soundgenerator.Player.Takt
        public void moveToNext() {
            if (this._takt == null) {
                return;
            }
            if (this._takt.hasNextStep()) {
                this._takt.moveToNext();
                return;
            }
            if (this._takt == this._intro) {
                this._takt = this._loop[0];
                this._takt.reset();
                this._loopIndex = 0;
            }
            int i = 0;
            while (!this._takt.hasNextStep()) {
                this._loopIndex++;
                i++;
                if (this._loopIndex >= this._loop.length) {
                    this._loopIndex = 0;
                }
                this._takt = this._loop[this._loopIndex];
                this._takt.reset();
                if (i > this._loop.length) {
                    return;
                }
            }
        }

        public int getTaktIndex() {
            if (this._takt == null || this._takt == this._intro) {
                return 0;
            }
            if (this._intro == null) {
                return this._loopIndex;
            }
            return this._loopIndex + 1;
        }

        public int getTaktStep() {
            if (this._takt == null) {
                return 0;
            }
            return this._takt.getStep();
        }

        @Override // com.finestandroid.soundgenerator.Player.Takt
        public int getSampleID(int i) {
            if (this._takt == null) {
                return 0;
            }
            return this._takt.getSampleID(i);
        }

        @Override // com.finestandroid.soundgenerator.Player.Takt
        public int getChanelStream(int i) {
            if (this._takt == null) {
                return 0;
            }
            return this._takt.getChanelStream(i);
        }

        @Override // com.finestandroid.soundgenerator.Player.Takt
        public void setChanelStream(int i, int i2) {
            if (this._takt == null) {
                return;
            }
            this._takt.setChanelStream(i, i2);
        }
    }

    /* loaded from: classes.dex */
    protected static class HHIntro extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f28hh = {3, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] bass = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected HHIntro() {
            init(f28hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class SnareIntro extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f55hh = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {1, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 2, 0};
        private static final int[] bass = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected SnareIntro() {
            init(f55hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class AIntro extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f21hh = {2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 2, 0};
        private static final int[] bass = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected AIntro() {
            init(f21hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class SRIntro extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f54hh = {0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] bass = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] crush = {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected SRIntro() {
            init(f54hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class BBIntro extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f22hh = {2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 1, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] bass = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected BBIntro() {
            init(f22hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class LGIntro extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f42hh = {2, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] bass = {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 2, 0, 2, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 2, 0, 2, 0};

        protected LGIntro() {
            init(f42hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class LGBeat1 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f36hh = {0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] bass = {2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected LGBeat1() {
            init(f36hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class LGBeat2 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f37hh = {0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2};
        private static final int[] bass = {2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 2, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 2, 0, 0, 0};

        protected LGBeat2() {
            init(f37hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class LGBeat3 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f38hh = {2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 3, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] bass = {2, 0, 0, 0, 0, 0, 0, 1, 2, 0, 1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 1, 2, 0, 1, 0, 0, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected LGBeat3() {
            init(f38hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class LGBeat4 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f39hh = {2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 1, 0, 2, 0, 1, 0};
        private static final int[] bass = {2, 0, 0, 0, 0, 0, 0, 1, 2, 0, 1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected LGBeat4() {
            init(f39hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class LGBeat5 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f40hh = {0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] bass = {2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected LGBeat5() {
            init(f40hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class LGBeat6 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f41hh = {2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1};
        private static final int[] bass = {2, 0, 0, 0, 0, 0, 0, 1, 2, 0, 1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 1, 2, 0, 2, 0, 1, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 2, 0, 0, 0};

        protected LGBeat6() {
            init(f41hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class RockBeat1 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f46hh = {0, 0, 3, 0, 2, 0, 3, 0, 2, 0, 3, 0, 2, 0, 3, 0, 2, 0, 3, 0, 2, 0, 3, 0, 2, 0, 3, 0, 2, 0, 3, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0};
        private static final int[] snare = {0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] bass = {2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] crush = {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected RockBeat1() {
            init(f46hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class Intro44 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f30hh = {3, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] bass = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected Intro44() {
            init(f30hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class Intro34 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f29hh = {2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] bass = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected Intro34() {
            init(f29hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class EmptyBeat44 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f27hh = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] bass = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected EmptyBeat44() {
            init(f27hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class EmptyBeat34 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f26hh = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] bass = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected EmptyBeat34() {
            init(f26hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class RockBeat2 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f47hh = {0, 0, 3, 0, 2, 0, 3, 0, 2, 0, 3, 0, 2, 0, 3, 0, 2, 0, 3, 0, 2, 0, 3, 0, 2, 0, 3, 0, 2, 0, 3, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] bass = {2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] crush = {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected RockBeat2() {
            init(f47hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class RockBeat3 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f48hh = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] ride = {2, 2, 2, 0, 0, 0, 2, 0, 2, 2, 2, 0, 2, 2, 2, 0, 2, 2, 2, 0, 0, 0, 2, 0, 2, 2, 2, 0, 2, 2, 2, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] bass = {2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected RockBeat3() {
            init(f48hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class MTNBeat1 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f43hh = {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] bass = {2, 0, 2, 0, 0, 0, 2, 0, 2, 1, 0, 0, 2, 0, 0, 0, 2, 0, 2, 0, 2, 1, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0};
        private static final int[] crush = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};

        protected MTNBeat1() {
            init(f43hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class MTNBeat2 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f44hh = {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] bass = {2, 0, 2, 0, 0, 0, 2, 0, 2, 1, 0, 0, 2, 0, 0, 0, 2, 0, 2, 0, 0, 0, 2, 1, 2, 0, 2, 1, 2, 0, 0, 0};
        private static final int[] crush = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};

        protected MTNBeat2() {
            init(f44hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class MTNBeat3 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f45hh = {0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 2, 1, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 2, 1, 2, 1, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] bass = {2, 0, 2, 0, 0, 0, 2, 0, 2, 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0};
        private static final int[] crush = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};

        protected MTNBeat3() {
            init(f45hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class SBeat1 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f49hh = {3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] bass = {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected SBeat1() {
            init(f49hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class SBeat2 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f50hh = {0, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] bass = {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1};
        private static final int[] crush = {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected SBeat2() {
            init(f50hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class SBeat3 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f51hh = {0, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 2, 2, 2, 0, 0, 1, 2};
        private static final int[] bass = {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1, 2, 1, 2, 0, 2, 0, 2, 1, 2, 0};
        private static final int[] crush = {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0};

        protected SBeat3() {
            init(f51hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class SBeat4 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f52hh = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] ride = {0, 0, 2, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 1, 2, 2, 0, 2, 0, 2, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] bass = {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1};
        private static final int[] crush = {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected SBeat4() {
            init(f52hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class SBeat5 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f53hh = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] bass = {2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0};
        private static final int[] crush = {2, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0};

        protected SBeat5() {
            init(f53hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class BBeat1 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f23hh = {0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 2, 0, 2, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0};
        private static final int[] bass = {2, 0, 0, 0, 2, 1, 0, 1, 0, 2, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 1, 0, 1, 0, 2, 0, 0, 2, 1, 0, 0};
        private static final int[] crush = {1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected BBeat1() {
            init(f23hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class BBeat2 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f24hh = {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 2, 0, 2, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0};
        private static final int[] bass = {2, 0, 0, 0, 2, 1, 0, 1, 0, 2, 0, 0, 2, 1, 0, 0, 2, 0, 0, 0, 2, 1, 0, 1, 0, 2, 0, 0, 2, 1, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected BBeat2() {
            init(f24hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class BBeat3 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f25hh = {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 2, 0, 2, 0, 0, 0, 2, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 2, 1, 1, 1, 2, 1, 1, 1};
        private static final int[] bass = {2, 0, 0, 0, 2, 1, 0, 1, 0, 2, 0, 0, 2, 1, 0, 0, 2, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected BBeat3() {
            init(f25hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class ABeat3 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f17hh = {0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] bass = {2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0};
        private static final int[] crush = {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected ABeat3() {
            init(f17hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class ABeat4 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f18hh = {3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] bass = {2, 0, 2, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected ABeat4() {
            init(f18hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class ABeat5 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f19hh = {0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] bass = {2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] crush = {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected ABeat5() {
            init(f19hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class ABeat6 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f20hh = {3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 2, 0, 2, 0};
        private static final int[] bass = {2, 0, 2, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected ABeat6() {
            init(f20hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class ABeat1 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f15hh = {0, 0, 0, 0, 3, 0, 2, 0, 2, 0, 0, 0, 3, 0, 2, 0, 3, 0, 2, 0, 3, 0, 2, 0, 3, 0, 2, 0, 3, 0, 2, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] bass = {2, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0};
        private static final int[] crush = {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected ABeat1() {
            init(f15hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class ABeat2 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f16hh = {3, 0, 2, 0, 3, 0, 2, 0, 2, 0, 0, 0, 3, 0, 2, 0, 3, 0, 2, 0, 3, 0, 2, 0, 3, 0, 2, 0, 3, 0, 2, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 1, 0, 1};
        private static final int[] bass = {2, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected ABeat2() {
            init(f16hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class JWlzIntro extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f35hh = {3, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] bass = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected JWlzIntro() {
            init(f35hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class JWlzBeat1 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f31hh = {2, 0, 0, 0, 3, 0, 0, 0, 3, 0, 1, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 2, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1, 0, 1, 2, 0, 0, 0};
        private static final int[] bass = {2, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 0, 2, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected JWlzBeat1() {
            init(f31hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class JWlzBeat2 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f32hh = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] ride = {2, 0, 2, 0, 2, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 1, 0, 2, 0, 0, 0, 2, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] bass = {2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected JWlzBeat2() {
            init(f32hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class JWlzBeat3 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f33hh = {0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 2, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0};
        private static final int[] bass = {2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 0, 2, 0};
        private static final int[] crush = {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        protected JWlzBeat3() {
            init(f33hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class JWlzBeat4 extends DrumTakt {

        /* renamed from: hh */
        private static final int[] f34hh = {2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] ride = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        private static final int[] snare = {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 2, 0, 0, 0, 1, 0, 2, 0, 0, 0};
        private static final int[] bass = {2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0};
        private static final int[] crush = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0};

        protected JWlzBeat4() {
            init(f34hh, ride, snare, bass, crush);
        }
    }

    /* loaded from: classes.dex */
    protected static class Loop5 extends DrumLoop {
        private static final DrumTakt intro = new JWlzIntro();
        private static final DrumTakt[] loop = {new JWlzBeat3(), new JWlzBeat3(), new JWlzBeat3(), new JWlzBeat3(), new JWlzBeat2(), new JWlzBeat2(), new JWlzBeat2(), new JWlzBeat2(), new JWlzBeat3(), new JWlzBeat3(), new JWlzBeat3(), new JWlzBeat1(), new JWlzBeat2(), new JWlzBeat2(), new JWlzBeat2(), new JWlzBeat2()};

        protected Loop5() {
            init(intro, loop);
        }
    }

    /* loaded from: classes.dex */
    protected static class Loop3 extends DrumLoop {
        private static final DrumTakt intro = new HHIntro();
        private static final DrumTakt[] loop = {new RockBeat2(), new RockBeat1(), new RockBeat2(), new RockBeat2(), new RockBeat2(), new RockBeat1(), new RockBeat3(), new RockBeat3()};

        protected Loop3() {
            init(intro, loop);
        }
    }

    /* loaded from: classes.dex */
    protected static class Loop4 extends DrumLoop {
        private static final DrumTakt intro = new HHIntro();
        private static final DrumTakt[] loop = {new HHIntro(), new HHIntro()};

        protected Loop4() {
            init(intro, loop);
        }
    }

    /* loaded from: classes.dex */
    protected static class EmptyLoop44 extends DrumLoop {
        private static final DrumTakt intro = new Intro44();
        private static final DrumTakt[] loop = {new EmptyBeat44(), new EmptyBeat44()};

        protected EmptyLoop44() {
            init(intro, loop);
        }
    }

    /* loaded from: classes.dex */
    protected static class EmptyLoop34 extends DrumLoop {
        private static final DrumTakt intro = new Intro34();
        private static final DrumTakt[] loop = {new EmptyBeat34(), new EmptyBeat34()};

        protected EmptyLoop34() {
            init(intro, loop);
        }
    }

    /* loaded from: classes.dex */
    protected static class EmptyLoop3 extends DrumLoop {
        private static final DrumTakt intro = new EmptyBeat34();
        private static final DrumTakt[] loop = {new EmptyBeat34()};

        protected EmptyLoop3() {
            init(intro, loop);
        }
    }

    /* loaded from: classes.dex */
    protected static class EmptyLoop4 extends DrumLoop {
        private static final DrumTakt intro = new EmptyBeat44();
        private static final DrumTakt[] loop = {new EmptyBeat44()};

        protected EmptyLoop4() {
            init(intro, loop);
        }
    }

    /* loaded from: classes.dex */
    protected static class Loop1 extends DrumLoop {
        private static final DrumTakt intro = new SRIntro();
        private static final DrumTakt[] loop = {new SBeat1(), new SBeat1(), new SBeat2(), new SBeat3(), new SBeat4(), new SBeat4(), new SBeat2(), new SBeat1(), new SBeat1(), new SBeat3(), new SBeat4(), new SBeat4(), new SBeat5(), new SBeat5()};

        protected Loop1() {
            init(intro, loop);
        }
    }

    /* loaded from: classes.dex */
    protected static class Loop6 extends DrumLoop {
        private static final DrumTakt intro = new AIntro();
        private static final DrumTakt[] loop = {new ABeat3(), new ABeat4(), new ABeat5(), new ABeat6(), new ABeat3(), new ABeat4(), new ABeat5(), new ABeat6(), new ABeat1(), new ABeat2()};

        protected Loop6() {
            init(intro, loop);
        }
    }

    /* loaded from: classes.dex */
    protected static class Loop7 extends DrumLoop {
        private static final DrumTakt intro = new HHIntro();
        private static final DrumTakt[] loop = {new MTNBeat1(), new MTNBeat2(), new MTNBeat1(), new MTNBeat2(), new MTNBeat1(), new MTNBeat3()};

        protected Loop7() {
            init(intro, loop);
        }
    }

    /* loaded from: classes.dex */
    protected static class Loop8 extends DrumLoop {
        private static final DrumTakt intro = new LGIntro();
        private static final DrumTakt[] loop = {new LGBeat1(), new LGBeat5(), new LGBeat1(), new LGBeat2(), new LGBeat3(), new LGBeat3(), new LGBeat3(), new LGBeat4(), new LGBeat3(), new LGBeat3(), new LGBeat3(), new LGBeat6()};

        protected Loop8() {
            init(intro, loop);
        }
    }

    /* loaded from: classes.dex */
    public static class EditTakt extends DrumTakt {
        public static final int BASS = 3;
        public static final int CRUSH = 4;
        public static final int HIHAT = 0;
        public static final int RIDE = 1;
        public static final int SNARE = 2;
        protected int _taktLen;

        @Override // com.finestandroid.soundgenerator.Player.DrumTakt, com.finestandroid.soundgenerator.Player.Takt
        public /* bridge */ /* synthetic */ int chanelsCount() {
            return super.chanelsCount();
        }

        @Override // com.finestandroid.soundgenerator.Player.DrumTakt, com.finestandroid.soundgenerator.Player.Takt
        public /* bridge */ /* synthetic */ int getChanelStream(int i) {
            return super.getChanelStream(i);
        }

        @Override // com.finestandroid.soundgenerator.Player.DrumTakt, com.finestandroid.soundgenerator.Player.Takt
        public /* bridge */ /* synthetic */ int getSampleID(int i) {
            return super.getSampleID(i);
        }

        @Override // com.finestandroid.soundgenerator.Player.Takt
        public /* bridge */ /* synthetic */ int getStep() {
            return super.getStep();
        }

        @Override // com.finestandroid.soundgenerator.Player.Takt
        public /* bridge */ /* synthetic */ int getStepsCount() {
            return super.getStepsCount();
        }

        @Override // com.finestandroid.soundgenerator.Player.Takt
        public /* bridge */ /* synthetic */ boolean hasNextStep() {
            return super.hasNextStep();
        }

        @Override // com.finestandroid.soundgenerator.Player.Takt
        public /* bridge */ /* synthetic */ void moveToNext() {
            super.moveToNext();
        }

        @Override // com.finestandroid.soundgenerator.Player.DrumTakt, com.finestandroid.soundgenerator.Player.Takt
        public /* bridge */ /* synthetic */ void reset() {
            super.reset();
        }

        @Override // com.finestandroid.soundgenerator.Player.DrumTakt, com.finestandroid.soundgenerator.Player.Takt
        public /* bridge */ /* synthetic */ void setChanelStream(int i, int i2) {
            super.setChanelStream(i, i2);
        }

        @Override // com.finestandroid.soundgenerator.Player.DrumTakt
        public /* bridge */ /* synthetic */ void setPlayPos(int i) {
            super.setPlayPos(i);
        }

        protected EditTakt(int i) {
            this._taktLen = 16;
            this._taktLen = i;
            init();
        }

        protected void init() {
            this._hh = new int[this._taktLen];
            this._ride = new int[this._taktLen];
            this._snare = new int[this._taktLen];
            this._bass = new int[this._taktLen];
            this._crush = new int[this._taktLen];
            this._count = this._taktLen;
            int length = this._streams.length;
            for (int i = 0; i < length; i++) {
                this._streams[i] = 0;
            }
            for (int i2 = 0; i2 < this._taktLen; i2++) {
                this._hh[i2] = 0;
                this._ride[i2] = 0;
                this._snare[i2] = 0;
                this._bass[i2] = 0;
                this._crush[i2] = 0;
            }
        }

        public void copyFromDrumtakt(int i, DrumTakt drumTakt) {
            int i2;
            boolean z;
            if (drumTakt == null) {
                i2 = 0;
                z = true;
            } else {
                i2 = drumTakt._count;
                z = false;
            }
            boolean z2 = z;
            for (int i3 = 0; i3 < this._taktLen; i3++) {
                int i4 = i3 + i;
                if (i4 >= i2) {
                    z2 = true;
                }
                if (z2) {
                    this._hh[i3] = 0;
                    this._ride[i3] = 0;
                    this._snare[i3] = 0;
                    this._bass[i3] = 0;
                    this._crush[i3] = 0;
                } else {
                    this._hh[i3] = drumTakt._hh[i4];
                    this._ride[i3] = drumTakt._ride[i4];
                    this._snare[i3] = drumTakt._snare[i4];
                    this._bass[i3] = drumTakt._bass[i4];
                    this._crush[i3] = drumTakt._crush[i4];
                }
            }
        }

        protected int getSampleVlue(int i, int i2) {
            if (i >= 0 && i < this._taktLen) {
                switch (i2) {
                    case 0:
                        return this._hh[i];
                    case 1:
                        return this._ride[i];
                    case 2:
                        return this._snare[i];
                    case 3:
                        return this._bass[i];
                    case 4:
                        return this._crush[i];
                    default:
                        return 0;
                }
            }
            return 0;
        }

        protected void setSample(int i, int i2, int i3) {
            if (i >= 0 && i < this._taktLen && i3 >= 0) {
                switch (i2) {
                    case 0:
                        this._hh[i] = i3;
                        return;
                    case 1:
                        this._ride[i] = i3;
                        return;
                    case 2:
                        this._snare[i] = i3;
                        return;
                    case 3:
                        this._bass[i] = i3;
                        return;
                    case 4:
                        this._crush[i] = i3;
                        return;
                    default:
                        return;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class EditLoop extends DrumLoop {
        protected int _loopLen;
        protected int _taktLen;

        public int getSoundIDByValue(int i, int i2) {
            switch (i) {
                case 0:
                    if (i2 == 1) {
                        return 3;
                    }
                    if (i2 == 2) {
                        return 4;
                    }
                    if (i2 == 3) {
                        return 1;
                    }
                    return i2 == 4 ? 2 : 0;
                case 1:
                    if (i2 == 1) {
                        return 5;
                    }
                    return i2 == 2 ? 6 : 0;
                case 2:
                    if (i2 == 1) {
                        return 7;
                    }
                    return i2 == 2 ? 8 : 0;
                case 3:
                    if (i2 == 1) {
                        return 9;
                    }
                    return i2 == 2 ? 10 : 0;
                case 4:
                    if (i2 == 1) {
                        return 11;
                    }
                    return i2 == 2 ? 12 : 0;
                default:
                    return 0;
            }
        }

        @Override // com.finestandroid.soundgenerator.Player.DrumLoop, com.finestandroid.soundgenerator.Player.Takt
        public /* bridge */ /* synthetic */ int chanelsCount() {
            return super.chanelsCount();
        }

        @Override // com.finestandroid.soundgenerator.Player.DrumLoop, com.finestandroid.soundgenerator.Player.Takt
        public /* bridge */ /* synthetic */ int getChanelStream(int i) {
            return super.getChanelStream(i);
        }

        @Override // com.finestandroid.soundgenerator.Player.DrumLoop, com.finestandroid.soundgenerator.Player.Takt
        public /* bridge */ /* synthetic */ int getSampleID(int i) {
            return super.getSampleID(i);
        }

        @Override // com.finestandroid.soundgenerator.Player.Takt
        public /* bridge */ /* synthetic */ int getStep() {
            return super.getStep();
        }

        @Override // com.finestandroid.soundgenerator.Player.DrumLoop, com.finestandroid.soundgenerator.Player.Takt
        public /* bridge */ /* synthetic */ int getStepsCount() {
            return super.getStepsCount();
        }

        @Override // com.finestandroid.soundgenerator.Player.DrumLoop
        public /* bridge */ /* synthetic */ int getTaktIndex() {
            return super.getTaktIndex();
        }

        @Override // com.finestandroid.soundgenerator.Player.DrumLoop
        public /* bridge */ /* synthetic */ int getTaktStep() {
            return super.getTaktStep();
        }

        @Override // com.finestandroid.soundgenerator.Player.DrumLoop, com.finestandroid.soundgenerator.Player.Takt
        public /* bridge */ /* synthetic */ boolean hasNextStep() {
            return super.hasNextStep();
        }

        @Override // com.finestandroid.soundgenerator.Player.DrumLoop
        public /* bridge */ /* synthetic */ boolean isSet() {
            return super.isSet();
        }

        @Override // com.finestandroid.soundgenerator.Player.DrumLoop, com.finestandroid.soundgenerator.Player.Takt
        public /* bridge */ /* synthetic */ void moveToNext() {
            super.moveToNext();
        }

        @Override // com.finestandroid.soundgenerator.Player.DrumLoop, com.finestandroid.soundgenerator.Player.Takt
        public /* bridge */ /* synthetic */ void reset() {
            super.reset();
        }

        @Override // com.finestandroid.soundgenerator.Player.DrumLoop, com.finestandroid.soundgenerator.Player.Takt
        public /* bridge */ /* synthetic */ void setChanelStream(int i, int i2) {
            super.setChanelStream(i, i2);
        }

        public EditLoop(int i) {
            this._loopLen = 4;
            this._taktLen = 16;
            this._taktLen = i;
            this._intro = null;
            this._loop = null;
            this._count = 0;
        }

        public EditLoop(int i, int i2) {
            this._loopLen = 4;
            this._taktLen = 16;
            this._taktLen = i;
            this._loopLen = i2;
            initEmpty();
        }

        public EditLoop(int i, DrumLoop drumLoop) {
            this._loopLen = 4;
            this._taktLen = 16;
            this._taktLen = i;
            this._loopLen = 4;
            if (drumLoop == null) {
                initEmpty();
                return;
            }
            initCopy(drumLoop);
            this._name = drumLoop._name;
        }

        protected void initCopy(DrumLoop drumLoop) {
            this._intro = new EditTakt(this._taktLen);
            ((EditTakt) this._intro).copyFromDrumtakt(0, drumLoop._intro);
            int length = drumLoop._loop.length;
            this._loopLen = 0;
            for (int i = 0; i < length; i++) {
                int i2 = drumLoop._loop[i]._count / this._taktLen;
                if (i2 == 0) {
                    i2 = 1;
                }
                this._loopLen += i2;
            }
            this._loop = new EditTakt[this._loopLen];
            DrumTakt drumTakt = null;
            int i3 = 0;
            int i4 = 0;
            for (int i5 = 0; i5 < this._loopLen; i5++) {
                this._loop[i5] = new EditTakt(this._taktLen);
                EditTakt editTakt = (EditTakt) this._loop[i5];
                if (drumTakt == null) {
                    drumTakt = drumLoop._loop[i3];
                } else if (drumTakt._count <= i4) {
                    i3++;
                    drumTakt = drumLoop._loop[i3];
                    i4 = 0;
                }
                editTakt.copyFromDrumtakt(i4, drumTakt);
                i4 += this._taktLen;
            }
        }

        public void addIntro(EditTakt editTakt) {
            if (editTakt == null) {
                return;
            }
            if (this._intro != null) {
                this._count -= this._intro.getStepsCount();
            }
            this._intro = editTakt;
            this._count += this._intro.getStepsCount();
            if (this._takt == null) {
                this._takt = editTakt;
            }
            this._loopIndex = 0;
        }

        public void removeTakt(int i) {
            if (i < 0) {
                return;
            }
            if (i == 0 && this._intro != null) {
                this._count -= this._intro.getStepsCount();
                if (this._takt == this._intro) {
                    this._takt = null;
                    if (this._loop != null && this._loop.length > 0) {
                        this._takt = this._loop[0];
                    }
                }
                this._intro = null;
                return;
            }
            if (this._intro != null) {
                i--;
            }
            int length = this._loop.length;
            EditTakt[] editTaktArr = new EditTakt[length - 1];
            for (int i2 = 0; i2 < i; i2++) {
                editTaktArr[i2] = (EditTakt) this._loop[i2];
            }
            EditTakt editTakt = (EditTakt) this._loop[i];
            while (true) {
                i++;
                if (i >= length) {
                    break;
                }
                editTaktArr[i - 1] = (EditTakt) this._loop[i];
            }
            this._loop = editTaktArr;
            this._loopLen = this._loop.length;
            this._count -= editTakt.getStepsCount();
            if (this._takt == editTakt) {
                this._takt = null;
                if (this._intro != null) {
                    this._takt = this._intro;
                } else if (this._loopLen > 0) {
                    this._takt = this._loop[0];
                }
            }
        }

        public void insertTakt(int i, EditTakt editTakt) {
            int i2;
            if (editTakt != null && i >= 0) {
                int length = this._loop.length;
                EditTakt[] editTaktArr = new EditTakt[length + 1];
                int i3 = 0;
                if (this._intro == null || i - 1 >= 0) {
                    i2 = 0;
                } else {
                    editTaktArr[0] = (EditTakt) this._intro;
                    this._intro = editTakt;
                    i2 = 1;
                }
                while (i2 < i) {
                    editTaktArr[i2] = (EditTakt) this._loop[i2];
                    i2++;
                }
                if (i >= 0) {
                    editTaktArr[i] = editTakt;
                    while (i < length) {
                        int i4 = i + 1;
                        editTaktArr[i4] = (EditTakt) this._loop[i];
                        i = i4;
                    }
                } else {
                    while (i3 < length) {
                        int i5 = i3 + 1;
                        editTaktArr[i5] = (EditTakt) this._loop[i3];
                        i3 = i5;
                    }
                }
                this._loop = editTaktArr;
                this._loopLen = this._loop.length;
                this._count += editTakt.getStepsCount();
                if (this._takt == null) {
                    this._takt = editTakt;
                }
            }
        }

        public void addTakt(EditTakt editTakt) {
            if (editTakt == null) {
                return;
            }
            if (this._loop == null) {
                this._loop = new EditTakt[1];
                this._loop[0] = editTakt;
            } else {
                int length = this._loop.length;
                EditTakt[] editTaktArr = new EditTakt[length + 1];
                for (int i = 0; i < length; i++) {
                    editTaktArr[i] = (EditTakt) this._loop[i];
                }
                editTaktArr[length] = editTakt;
                this._loop = editTaktArr;
                this._loopLen = this._loop.length;
            }
            this._count += editTakt.getStepsCount();
            if (this._takt == null) {
                this._takt = editTakt;
            }
            this._loopIndex = 0;
        }

        protected void initEmpty() {
            this._intro = new EditTakt(this._taktLen);
            this._loop = new EditTakt[this._loopLen];
            this._count = this._intro.getStepsCount();
            int length = this._loop.length;
            for (int i = 0; i < length; i++) {
                this._loop[i] = new EditTakt(this._taktLen);
                this._count += this._loop[i].getStepsCount();
            }
            this._takt = this._intro;
            this._loopIndex = 0;
        }

        protected void changeSample(int i, int i2, int i3, int i4) {
            int i5=0;
            if (i < 0) {
                return;
            }
            EditTakt editTakt = null;
            if (i == 0) {
                editTakt = (EditTakt) this._intro;
            } else if (this._loop != null && i - 1 < this._loop.length) {
                editTakt = (EditTakt) this._loop[i5];
            }
            if (editTakt == null) {
                return;
            }
            editTakt.setSample(i2, i3, i4);
        }

        public int getTaktsCount() {
            int i = this._intro != null ? 1 : 0;
            return this._loop != null ? i + this._loop.length : i;
        }

        public EditTakt getTakt(int i) {
            if (i < 0) {
                return null;
            }
            if (i == 0) {
                if (this._intro != null) {
                    return (EditTakt) this._intro;
                }
                if (this._loop != null && this._loop.length > i) {
                    return (EditTakt) this._loop[i];
                }
                return null;
            }
            if (this._intro != null) {
                i--;
            }
            if (this._loop != null && this._loop.length > i) {
                return (EditTakt) this._loop[i];
            }
            return null;
        }

        public boolean hasIntro() {
            return this._intro != null;
        }

        public int getTaktLen() {
            return this._taktLen;
        }

        public void setPlayTakt(int i, int i2) {
            if (i == 0) {
                if (this._intro != null) {
                    this._takt = this._intro;
                } else if (this._loop != null && this._loop.length > 0) {
                    this._takt = this._loop[0];
                }
                this._loopIndex = 0;
            } else {
                if (this._intro != null) {
                    i--;
                }
                if (i < 0 || this._loop == null || i >= this._loop.length) {
                    return;
                }
                this._takt = this._loop[i];
                this._loopIndex = i;
            }
            ((EditTakt) this._takt).setPlayPos(i2);
        }
    }

    /* loaded from: classes.dex */
    protected static class Loop2 extends DrumLoop {
        private static final DrumTakt intro = new BBIntro();
        private static final DrumTakt[] loop = {new BBeat1(), new BBeat2(), new BBeat1(), new BBeat3(), new BBeat2(), new BBeat3(), new BBeat2(), new BBeat3()};

        protected Loop2() {
            init(intro, loop);
        }
    }

    public void setTaktListener(ITaktChangedListener iTaktChangedListener) {
        this._taktChangeListener = iTaktChangedListener;
    }

    public void setBPMChangeListener(BPMChangeListener bPMChangeListener) {
        this._bpmListener = bPMChangeListener;
    }

    public void init(Context context) {
        this._sound = new SoundPlayer(context);
    }

    protected void setTakt(int i) {
        boolean z = this._isPlaying;
        stop();
        if (this._takt != null) {
            this._takt.reset();
        }
        switch (i) {
            case 0:
                this._takt = new EditLoop(16, LOOP1);
                break;
            case 1:
                this._takt = new EditLoop(16, LOOP2);
                break;
            case 2:
                this._takt = new EditLoop(16, LOOP3);
                break;
            case 3:
                this._takt = new EditLoop(16, LOOP4);
                break;
            case 4:
                this._takt = new EditLoop(12, LOOP5);
                break;
            case 5:
                this._takt = new EditLoop(16, LOOP6);
                break;
            default:
                this._takt = new EditLoop(16, LOOP1);
                break;
        }
        if (this._taktChangeListener != null) {
            this._taktChangeListener.setTakt((EditLoop) this._takt);
        }
        System.gc();
        if (z) {
            start(true);
            synchronized (this._objSync) {
                this._signalBPM = true;
            }
        }
    }

    protected void setTakt(EditLoop editLoop) {
        if (editLoop == null) {
            return;
        }
        boolean z = this._isPlaying;
        stop();
        if (this._takt != null) {
            this._takt.reset();
        }
        this._takt = editLoop;
        this._takt.reset();
        if (this._taktChangeListener != null) {
            this._taktChangeListener.setTakt((EditLoop) this._takt);
        }
        System.gc();
        if (z) {
            start(true);
            synchronized (this._objSync) {
                initTimes();
            }
        }
    }

    public void setBPM(float f) {
        synchronized (this._objSync) {
            this._bpm = f;
            initTimes();
        }
    }

    public void synchronize() {
        synchronized (this._objSync) {
            initTimes();
        }
    }

    public void initTimes() {
        this._step = 60000.0d / this._bpm;
        this._step /= 4.0d;
        this._signalBPM = true;
    }

    public void stop() {
        this._isPlaying = false;
    }

    protected synchronized void play() {
        try {
            this._startTime = SystemClock.uptimeMillis();
            this._prevTick = this._startTime;
            this._nextTick = this._prevTick + this._step;
            playTick(0);
            long j = (int) (this._step / 3.0d);
            int i = 1;
            while (this._isPlaying) {
                Thread.sleep(j);
                for (long uptimeMillis = SystemClock.uptimeMillis(); uptimeMillis < this._nextTick; uptimeMillis = SystemClock.uptimeMillis()) {
                    if (!this._isPlaying) {
                        return;
                    }
                }
                playTick(i);
                i++;
                this._prevTick = this._nextTick;
                this._nextTick = this._prevTick + this._step;
                j = (int) (this._step / 3.0d);
            }
            notifyAll();
        } catch (Throwable unused) {
        }
    }

    protected void playTick(int i) {
        synchronized (this._objSync) {
            if (this._signalBPM) {
                if (this._bpmListener != null) {
                    this._bpmListener.signalChange();
                }
                this._signalBPM = false;
            }
            int chanelsCount = this._takt.chanelsCount();
            for (int i2 = 0; i2 < chanelsCount; i2++) {
                if (this._takt.getSampleID(i2) > 0) {
                    if (this._takt.getChanelStream(i2) > 0) {
                        stopSound();
                    }
                    this._takt.setChanelStream(i2, playSound());
                }
            }
            this._takt.moveToNext();
        }
    }

    public int playSound() {
        if (this._sound == null) {
            return 0;
        }
        try {
            if (this._sound.isPlaying()) {
                return 1;
            }
            this._sound.play();
            return 1;
        } catch (Throwable unused) {
            return 0;
        }
    }

    public void stopSound() {
        if (this._sound == null) {
            return;
        }
        this._sound.stop();
    }

    public void start(boolean z) {
        try {
            stop();
            play();
        } catch (Throwable unused) {
        }
    }

    public long getStartTime() {
        return this._startTime;
    }

    public int getCurrentTakt() {
        if (this._takt == null) {
            return 0;
        }
        return this._takt.getTaktIndex();
    }

    public int getCurrentTaktPos() {
        if (this._takt == null) {
            return 0;
        }
        return this._takt.getTaktStep();
    }

    public double getTimeStep() {
        return this._step;
    }

    public long getNextTickTime() {
        return (long) this._nextTick;
    }

    public Object getBPMSync() {
        return this._objSync;
    }

    public void playDrum(int i, int i2) {
        if (this._takt != null && (this._takt instanceof EditLoop) && ((EditLoop) this._takt).getSoundIDByValue(i, i2) > 0) {
            playSound();
        }
    }

    public void setPlayPos(int i, int i2) {
        if (this._takt != null && (this._takt instanceof EditLoop)) {
            ((EditLoop) this._takt).setPlayTakt(i, i2);
        }
    }
}
