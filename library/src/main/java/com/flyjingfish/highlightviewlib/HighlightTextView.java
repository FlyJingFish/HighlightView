package com.flyjingfish.highlightviewlib;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class HighlightTextView extends AppCompatTextView {
    private float startHighlightOffset = 0;
    private float highlightWidth = 0;
    private float highlightRotateDegrees = 0;
    private int highlightColor;
    private int mStartDirection = START_LEFT;

    private boolean isFinishLayout;
    private boolean isStartBeforeLayout;
    private int mRepeatCount = 0;
    private int mRepeatMode = ValueAnimator.RESTART;
    private ObjectAnimator mHighlightEffectAnim;
    private TimeInterpolator mInterpolator = new DecelerateInterpolator();
    public static final int INFINITE = ValueAnimator.INFINITE;
    public static final int RESTART = ValueAnimator.RESTART;
    public static final int REVERSE = ValueAnimator.REVERSE;
    public static final int START_LEFT = 1;
    public static final int START_RIGHT = 2;
    public static final int START_TOP = 3;
    public static final int START_BOTTOM = 4;
    private long mDuration;

    public HighlightTextView(@NonNull Context context) {
        this(context,null);
    }

    public HighlightTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HighlightTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HighlightTextView);
        int highlightColor = a.getColor(R.styleable.HighlightTextView_highlight_text_highlightColor,Color.TRANSPARENT);
        long duration = a.getInteger(R.styleable.HighlightTextView_highlight_text_duration,1000);
        int repeatCount = a.getInteger(R.styleable.HighlightTextView_highlight_text_repeatCount,0);
        int repeatMode = a.getInt(R.styleable.HighlightTextView_highlight_text_repeatMode,RESTART);
        float highlightWidth = a.getDimension(R.styleable.HighlightTextView_highlight_text_highlightWidth,10);
        float highlightRotateDegrees = a.getFloat(R.styleable.HighlightTextView_highlight_text_highlightRotateDegrees,30);
        int startDirection = a.getColor(R.styleable.HighlightTextView_highlight_text_startDirection,START_LEFT);
        boolean autoStart = a.getBoolean(R.styleable.HighlightTextView_highlight_text_autoStart,false);
        a.recycle();

        setDuration(duration);
        setRepeatCount(repeatCount);
        setRepeatMode(repeatMode);
        setHighlightWidth(highlightWidth);
        setHighlightRotateDegrees(highlightRotateDegrees);
        setStartDirection(startDirection);
        setHighlightColor(highlightColor);
        if (autoStart){
            startHighlightEffect();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint textPaint = getPaint();
        int height = getHeight();
        float width = highlightWidth;
        float currentAngle = highlightRotateDegrees+90;
        float angle = currentAngle % 360;
        if (angle < 0) {
            angle = 360 + angle;
        }
        float x0, y0, x1, y1;
        if (angle >= 0 && angle <= 45) {
            float percent = angle / 45;
            x0 = width / 2 + width / 2 * percent;
            y0 = 0;
        } else if (angle <= 90) {
            float percent = (angle - 45) / 45;
            x0 = width;
            y0 = height / 2 * percent;
        } else if (angle <= 135) {
            float percent = (angle - 90) / 45;
            x0 = width;
            y0 = height / 2 * percent + height / 2;
        } else if (angle <= 180) {
            float percent = (angle - 135) / 45;
            x0 = width / 2 + width / 2 * percent;
            y0 = height;
        } else if (angle <= 225) {
            float percent = (angle - 180) / 45;
            x0 = width / 2 - width / 2 * percent;
            y0 = height;
        } else if (angle <= 270) {
            float percent = (angle - 225) / 45;
            x0 = 0;
            y0 = height - height / 2 * percent;
        } else if (angle <= 315) {
            float percent = (angle - 270) / 45;
            x0 = 0;
            y0 = height / 2 - height / 2 * percent;
        } else {
            float percent = (angle - 315) / 45;
            x0 = width / 2 * percent;
            y0 = 0;
        }
        x1 = width - x0;
        y1 = height - y0;
        if (mStartDirection == START_TOP||mStartDirection == START_BOTTOM){
            LinearGradient linearGradient = new LinearGradient(x0, y0+startHighlightOffset, x1, y1+startHighlightOffset,  new int[]{getCurrentTextColor(),highlightColor,highlightColor,getCurrentTextColor()}, new float[]{0,.45f,.55f,1}, Shader.TileMode.CLAMP);
            textPaint.setShader(linearGradient);
        }else {
            LinearGradient linearGradient = new LinearGradient(startHighlightOffset+x0, y0, startHighlightOffset+x1, y1,  new int[]{getCurrentTextColor(),highlightColor,highlightColor,getCurrentTextColor()}, new float[]{0,.45f,.55f,1}, Shader.TileMode.CLAMP);
            textPaint.setShader(linearGradient);
        }

        super.onDraw(canvas);

    }

    @IntDef({RESTART, REVERSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RepeatMode {}

    @IntDef({START_LEFT, START_RIGHT,START_TOP, START_BOTTOM})
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


    public float getStartHighlightOffset() {
        return startHighlightOffset;
    }

    public void setStartHighlightOffset(float startHighlightOffset) {
        this.startHighlightOffset = startHighlightOffset;
        invalidate();
    }

    public float getHighlightRotateDegrees() {
        return highlightRotateDegrees;
    }

    public void setHighlightRotateDegrees(float rotateDegrees) {
        this.highlightRotateDegrees = rotateDegrees;
        invalidate();
    }

    public float getHighlightWidth() {
        return highlightWidth;
    }

    public void setHighlightWidth(float highlightWidth) {
        this.highlightWidth = highlightWidth;
        invalidate();
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


    private void startAnim(){
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        if (mHighlightEffectAnim == null){
            mHighlightEffectAnim = ObjectAnimator.ofFloat(this, "startHighlightOffset", -getHighlightWidth(), viewWidth+ getHighlightWidth());
        }else {
            mHighlightEffectAnim.cancel();
        }
        if (getStartDirection() == START_RIGHT){
            mHighlightEffectAnim.setFloatValues(viewWidth+ getHighlightWidth(),-getHighlightWidth());
        }else if (getStartDirection() == START_TOP){
            mHighlightEffectAnim.setFloatValues(-getHighlightWidth(), viewHeight+ getHighlightWidth());
        }else if (getStartDirection() == START_BOTTOM){
            mHighlightEffectAnim.setFloatValues(viewHeight+ getHighlightWidth(),-getHighlightWidth());
        }else {
            mHighlightEffectAnim.setFloatValues(-getHighlightWidth(), viewWidth+ getHighlightWidth());
        }
        mHighlightEffectAnim.setDuration(mDuration);
        mHighlightEffectAnim.setInterpolator(mInterpolator);
        mHighlightEffectAnim.setRepeatCount(mRepeatCount);
        mHighlightEffectAnim.setRepeatMode(mRepeatMode);
        mHighlightEffectAnim.start();
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

    public void setRepeatMode(@HighlightImageView.RepeatMode int value) {
        mRepeatMode = value;
    }

    public int getRepeatMode() {
        return mRepeatMode;
    }

    public int getStartDirection() {
        return mStartDirection;
    }

    public void setStartDirection(@HighlightImageView.StartDirection int startDirection) {
        this.mStartDirection = startDirection;
    }

    @ColorInt
    public int getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(@ColorInt int highlightColor) {
        this.highlightColor = highlightColor;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        isFinishLayout = true;
        if (isStartBeforeLayout){
            startAnim();
        }
    }
}
