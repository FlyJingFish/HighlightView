package com.flyjingfish.highlightviewlib;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class HighlightAnimHolder {
    public static final int INFINITE = ValueAnimator.INFINITE;
    public static final int RESTART = ValueAnimator.RESTART;
    public static final int REVERSE = ValueAnimator.REVERSE;
    public static final int FROM_LEFT = 1;
    public static final int FROM_RIGHT = 2;
    public static final int FROM_TOP = 3;
    public static final int FROM_BOTTOM = 4;
    private final HighlightDrawView mHighlightDrawView;
    private final HighlightView mHighlightView;
    private int mRepeatCount = 0;
    private int mRepeatMode = ValueAnimator.RESTART;
    private TimeInterpolator mInterpolator = new DecelerateInterpolator();
    private long mDuration;
    private ObjectAnimator mHighlightEffectAnim;
    private boolean isFinishLayout;
    private boolean isStartBeforeLayout;

    private float mHighlightRotateDegrees = 0;

    public HighlightAnimHolder(HighlightDrawView highlightDrawView, HighlightView highlightView) {
        this.mHighlightDrawView = highlightDrawView;
        this.mHighlightView = highlightView;
        if (highlightView.thisView() != null) {
            Context context = highlightView.thisView().getContext();
            if (context instanceof LifecycleOwner) {
                ((LifecycleOwner) context).getLifecycle().addObserver(new LifecycleEventObserver() {
                    @Override
                    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                        if (mHighlightEffectAnim == null) {
                            return;
                        }
                        if (event == Lifecycle.Event.ON_DESTROY) {
                            mHighlightEffectAnim.removeAllListeners();
                            mHighlightEffectAnim.cancel();
                        }
                    }
                });
            }
        }
    }

    @IntDef({RESTART, REVERSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RepeatMode {
    }

    @IntDef({FROM_LEFT, FROM_RIGHT, FROM_TOP, FROM_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StartDirection {
    }

    public void addLifecycleObserver(LifecycleOwner owner) {
        if (owner != null) {
            owner.getLifecycle().removeObserver(mLifecycleEventObserver);
            owner.getLifecycle().addObserver(mLifecycleEventObserver);
        }
    }

    private final LifecycleEventObserver mLifecycleEventObserver = new LifecycleEventObserver() {
        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            if (mHighlightEffectAnim == null) {
                return;
            }
            if (event == Lifecycle.Event.ON_START) {
                mHighlightEffectAnim.resume();
            } else if (event == Lifecycle.Event.ON_STOP) {
                mHighlightEffectAnim.pause();
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                mHighlightEffectAnim.removeAllListeners();
                mHighlightEffectAnim.cancel();
            }
        }
    };

    private int getWidth() {
        return mHighlightView.thisView().getWidth();
    }

    private int getHeight() {
        return mHighlightView.thisView().getHeight();
    }

    public float getStartHighlightOffset() {
        return mHighlightDrawView.getHighlightDraw().setStartHighlightOffset();
    }

    public void setStartHighlightOffset(float startHighlightOffset) {
        mHighlightDrawView.getHighlightDraw().setStartHighlightOffset(startHighlightOffset);
        mHighlightView.thisView().invalidate();
    }

    public void startHighlightEffect() {
        if (isFinishLayout) {
            startAnim();
        } else {
            isStartBeforeLayout = true;
        }
    }

    public void stopHighlightEffect() {
        if (mHighlightEffectAnim != null) {
            mHighlightEffectAnim.removeAllListeners();
            mHighlightEffectAnim.cancel();

            float[] location = getStartEndLocation();
            setStartHighlightOffset(location[0]);
        }
        isStartBeforeLayout = false;
    }

    public void resumeHighlightEffect() {
        if (mHighlightEffectAnim != null) {
            mHighlightEffectAnim.resume();
        } else {
            startHighlightEffect();
        }
    }

    public void pauseHighlightEffect() {
        if (mHighlightEffectAnim != null) {
            mHighlightEffectAnim.pause();
        }
    }

    public boolean isPaused() {
        if (mHighlightEffectAnim != null) {
            return mHighlightEffectAnim.isPaused();
        }
        return true;
    }


    protected void startAnim() {
        if (mHighlightEffectAnim == null) {
            int viewWidth = getWidth();
            mHighlightEffectAnim = ObjectAnimator.ofFloat(this, "startHighlightOffset", -getHighlightWidth(), viewWidth + getHighlightWidth());
        } else {
            mHighlightEffectAnim.cancel();
        }
        mHighlightEffectAnim.setFloatValues(getStartEndLocation());
        mHighlightEffectAnim.setDuration(mDuration);
        mHighlightEffectAnim.setInterpolator(mInterpolator);
        mHighlightEffectAnim.setRepeatCount(mRepeatCount);
        mHighlightEffectAnim.setRepeatMode(mRepeatMode);
        mHighlightEffectAnim.start();
    }

    private float[] getStartEndLocation() {
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        float[] location;
        float offset = getStartOffset();
        if (getStartDirection() == FROM_RIGHT) {
            location = new float[]{viewWidth + offset, -(offset + getHighlightWidth())};
        } else if (getStartDirection() == FROM_TOP) {
            location = new float[]{-(offset + getHighlightWidth()), viewHeight + offset};
        } else if (getStartDirection() == FROM_BOTTOM) {
            location = new float[]{viewHeight + offset, -(offset + getHighlightWidth())};
        } else {
            location = new float[]{-(offset + getHighlightWidth()), viewWidth + offset};
        }
        return location;
    }

    private float getStartOffset() {
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        double offset;
        if (getStartDirection() == FROM_RIGHT || getStartDirection() == FROM_LEFT) {
            double value1 = tan() * viewWidth / 2;
            if (value1 < viewHeight / 2f) {
                offset = (viewHeight / 2d - value1) * sin() + viewWidth / 2d / cos() - viewWidth / 2d;
            } else {
                offset = ((value1 - viewHeight / 2d) / tan()) * cos() + (viewHeight / 2d / sin()) - viewWidth / 2d;
            }
        } else {
            double value1 = tan() * viewHeight / 2;
            if (value1 < viewWidth / 2f) {
                offset = (viewWidth / 2d - value1) * sin() + viewHeight / 2d / cos() - viewHeight / 2d;
            } else {
                offset = ((value1 - viewWidth / 2d) / tan()) * cos() + (viewWidth / 2d / sin()) - viewHeight / 2d;
            }

        }
        return (float) offset;
    }

    private double tan() {
        double radians = Math.toRadians(Math.abs(mHighlightRotateDegrees));
        return Math.tan(radians);
    }

    private double sin() {
        double radians = Math.toRadians(Math.abs(mHighlightRotateDegrees));
        return Math.sin(radians);
    }

    private double cos() {
        double radians = Math.toRadians(Math.abs(mHighlightRotateDegrees));
        return Math.cos(radians);
    }

    public float getHighlightRotateDegrees() {
        return mHighlightDrawView.getHighlightDraw().getHighlightRotateDegrees();
    }

    public void setHighlightRotateDegrees(float rotateDegrees) {
        mHighlightRotateDegrees = rotateDegrees;
        mHighlightDrawView.getHighlightDraw().setHighlightRotateDegrees(rotateDegrees);
        mHighlightView.thisView().invalidate();
    }

    public float getHighlightWidth() {
        return mHighlightDrawView.getHighlightDraw().getHighlightWidth();
    }

    public void setHighlightWidth(float highlightWidth) {
        mHighlightDrawView.getHighlightDraw().setHighlightWidth(highlightWidth);
        mHighlightView.thisView().invalidate();
    }


    public void setDuration(long duration) {
        mDuration = duration;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setInterpolator(TimeInterpolator value) {
        if (value != null) {
            mInterpolator = value;
        } else {
            mInterpolator = new DecelerateInterpolator();
        }
    }

    public TimeInterpolator getInterpolator() {
        return mInterpolator;
    }

    public void setRepeatCount(int value) {
        mRepeatCount = value;
    }

    public int getRepeatCount() {
        return mRepeatCount;
    }

    public void setRepeatMode(@HighlightAnimHolder.RepeatMode int value) {
        mRepeatMode = value;
    }

    public int getRepeatMode() {
        return mRepeatMode;
    }

    public int getStartDirection() {
        return mHighlightDrawView.getHighlightDraw().getStartDirection();
    }

    public void setStartDirection(@HighlightAnimHolder.StartDirection int startDirection) {
        mHighlightDrawView.getHighlightDraw().setStartDirection(startDirection);
    }

    @ColorInt
    public int getHighlightColor() {
        return mHighlightDrawView.getHighlightDraw().getHighlightColor();
    }

    public void setHighlightColor(@ColorInt int highlightColor) {
        mHighlightDrawView.getHighlightDraw().setHighlightColor(highlightColor);
    }

    protected boolean isFinishLayout() {
        return isFinishLayout;
    }

    protected void setFinishLayout(boolean finishLayout) {
        isFinishLayout = finishLayout;
    }

    protected boolean isStartBeforeLayout() {
        return isStartBeforeLayout;
    }

    protected void setStartBeforeLayout(boolean startBeforeLayout) {
        isStartBeforeLayout = startBeforeLayout;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        setFinishLayout(true);
        if (isStartBeforeLayout()) {
            startAnim();
        }
    }
}
