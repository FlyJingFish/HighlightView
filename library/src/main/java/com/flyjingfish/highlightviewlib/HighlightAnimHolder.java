package com.flyjingfish.highlightviewlib;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

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
    private final HighlightDrawView highlightDrawView;
    private final HighlightView highlightView;
    private int mRepeatCount = 0;
    private int mRepeatMode = ValueAnimator.RESTART;
    private TimeInterpolator mInterpolator = new DecelerateInterpolator();
    private long mDuration;
    private ObjectAnimator mHighlightEffectAnim;
    private boolean isFinishLayout;
    private boolean isStartBeforeLayout;

    public HighlightAnimHolder(HighlightDrawView highlightDrawView,HighlightView highlightView) {
        this.highlightDrawView = highlightDrawView;
        this.highlightView = highlightView;
    }

    @IntDef({RESTART, REVERSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RepeatMode {}

    @IntDef({FROM_LEFT, FROM_RIGHT,FROM_TOP, FROM_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StartDirection {}

    public void addLifecycleObserver(LifecycleOwner owner) {
        if (owner != null) {
            owner.getLifecycle().removeObserver(mLifecycleEventObserver);
            owner.getLifecycle().addObserver(mLifecycleEventObserver);
        }
    }

    private final LifecycleEventObserver mLifecycleEventObserver = new LifecycleEventObserver() {
        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            if (mHighlightEffectAnim == null){
                return;
            }
            if (event == Lifecycle.Event.ON_RESUME){
                mHighlightEffectAnim.resume();
            }else if (event == Lifecycle.Event.ON_STOP){
                mHighlightEffectAnim.pause();
            }else if (event == Lifecycle.Event.ON_DESTROY){
                mHighlightEffectAnim.removeAllListeners();
                mHighlightEffectAnim.cancel();
            }
        }
    };

    private int getWidth(){
        return highlightView.thisView().getWidth();
    }

    private int getHeight(){
        return highlightView.thisView().getHeight();
    }

    public float getStartHighlightOffset() {
        return highlightDrawView.getHighlightDraw().getStartHighlightOffset();
    }

    public void setStartHighlightOffset(float startHighlightOffset) {
        highlightDrawView.getHighlightDraw().getStartHighlightOffset(startHighlightOffset);
        highlightView.thisView().invalidate();
    }

    public void startHighlightEffect() {
        if (isFinishLayout){
            startAnim();
        }else {
            isStartBeforeLayout = true;
        }
    }

    public void stopHighlightEffect() {
        if (mHighlightEffectAnim != null){
            mHighlightEffectAnim.cancel();
        }
        isStartBeforeLayout = false;
    }


    protected void startAnim(){
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        if (mHighlightEffectAnim == null){
            mHighlightEffectAnim = ObjectAnimator.ofFloat(this, "startHighlightOffset", -getHighlightWidth(), viewWidth+ getHighlightWidth());
        }else {
            mHighlightEffectAnim.cancel();
        }
        if (getStartDirection() == FROM_RIGHT){
            mHighlightEffectAnim.setFloatValues(viewWidth+ getHighlightWidth(),-getHighlightWidth());
        }else if (getStartDirection() == FROM_TOP){
            mHighlightEffectAnim.setFloatValues(-getHighlightWidth(), viewHeight+ getHighlightWidth());
        }else if (getStartDirection() == FROM_BOTTOM){
            mHighlightEffectAnim.setFloatValues(viewHeight+ getHighlightWidth(),-getHighlightWidth());
        }else {
            mHighlightEffectAnim.setFloatValues(-getHighlightWidth(), viewWidth+ getHighlightWidth());
        }
        mHighlightEffectAnim.setDuration(getDuration());
        mHighlightEffectAnim.setInterpolator(getInterpolator());
        mHighlightEffectAnim.setRepeatCount(getRepeatCount());
        mHighlightEffectAnim.setRepeatMode(getRepeatMode());
        mHighlightEffectAnim.start();
    }



    public float getHighlightRotateDegrees() {
        return highlightDrawView.getHighlightDraw().getHighlightRotateDegrees();
    }

    public void setHighlightRotateDegrees(float rotateDegrees) {
        highlightDrawView.getHighlightDraw().setHighlightRotateDegrees(rotateDegrees);
        highlightView.thisView().invalidate();
    }

    public float getHighlightWidth() {
        return highlightDrawView.getHighlightDraw().getHighlightWidth();
    }

    public void setHighlightWidth(float highlightWidth) {
        highlightDrawView.getHighlightDraw().setHighlightWidth(highlightWidth);
        highlightView.thisView().invalidate();
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
            mInterpolator = new LinearInterpolator();
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
        return highlightDrawView.getHighlightDraw().getStartDirection();
    }

    public void setStartDirection(@HighlightAnimHolder.StartDirection int startDirection) {
        highlightDrawView.getHighlightDraw().setStartDirection(startDirection);
    }

    @ColorInt
    public int getHighlightColor() {
        return highlightDrawView.getHighlightDraw().getHighlightColor();
    }

    public void setHighlightColor(@ColorInt int highlightColor) {
        highlightDrawView.getHighlightDraw().setHighlightColor(highlightColor);
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
}