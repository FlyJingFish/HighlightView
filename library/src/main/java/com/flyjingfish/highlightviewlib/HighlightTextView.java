package com.flyjingfish.highlightviewlib;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.ViewGroup;
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
    private final HighlightFrontTextView mFrontTextView;
    private boolean isFinishLayout;
    private boolean isStartBeforeLayout;
    private int mRepeatCount = 0;
    private int mRepeatMode = ValueAnimator.RESTART;
    private ObjectAnimator mHighlightEffectAnim;
    private TimeInterpolator mInterpolator = new DecelerateInterpolator();
    public static final int INFINITE = ValueAnimator.INFINITE;
    public static final int RESTART = ValueAnimator.RESTART;
    public static final int REVERSE = ValueAnimator.REVERSE;
    public static final int FROM_LEFT = 1;
    public static final int FROM_RIGHT = 2;
    public static final int FROM_TOP = 3;
    public static final int FROM_BOTTOM = 4;
    private long mDuration;
    private final AppCompatTextView mTextView;


    public HighlightTextView(@NonNull Context context) {
        this(context, null);
    }

    public HighlightTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HighlightTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFrontTextView = new HighlightFrontTextView(context, attrs, defStyleAttr);
        mTextView = new AppCompatTextView(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HighlightTextView);
        int highlightColor = a.getColor(R.styleable.HighlightTextView_highlight_text_highlightColor,Color.TRANSPARENT);
        long duration = a.getInteger(R.styleable.HighlightTextView_highlight_text_duration,1000);
        int repeatCount = a.getInteger(R.styleable.HighlightTextView_highlight_text_repeatCount,0);
        int repeatMode = a.getInt(R.styleable.HighlightTextView_highlight_text_repeatMode,RESTART);
        float highlightWidth = a.getDimension(R.styleable.HighlightTextView_highlight_text_highlightWidth,10);
        float highlightRotateDegrees = a.getFloat(R.styleable.HighlightTextView_highlight_text_highlightRotateDegrees,30);
        int startDirection = a.getColor(R.styleable.HighlightTextView_highlight_text_startDirection,FROM_LEFT);
        boolean autoStart = a.getBoolean(R.styleable.HighlightTextView_highlight_text_autoStart,false);
        a.recycle();

        setDuration(duration);
        setRepeatCount(repeatCount);
        setRepeatMode(repeatMode);
        setHighlightWidth(highlightWidth);
        setHighlightRotateDegrees(highlightRotateDegrees);
        setStartDirection(startDirection);
        setHighlightColor(highlightColor);
        mFrontTextView.setBackground(null);
        mTextView.setBackground(null);
        mFrontTextView.setTextColor(Color.BLACK);
        mTextView.setTextColor(Color.BLACK);
        if (autoStart){
            startHighlightEffect();
        }

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

    @Override
    protected void onDraw(Canvas canvas) {
        getPaint().setXfermode(null);
        super.onDraw(canvas);

        getPaint().setXfermode(null);
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(),  canvas.getHeight()),  getPaint(), Canvas.ALL_SAVE_FLAG);
        mTextView.draw(canvas);
        getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(),  canvas.getHeight()),  getPaint(), Canvas.ALL_SAVE_FLAG);
        mFrontTextView.draw(canvas);
    }


    public float getStartHighlightOffset() {
        return mFrontTextView.getStartHighlightOffset();
    }

    public void setStartHighlightOffset(float startHighlightOffset) {
        mFrontTextView.getStartHighlightOffset(startHighlightOffset);
        invalidate();
    }

    public float getHighlightRotateDegrees() {
        return mFrontTextView.getHighlightRotateDegrees();
    }

    public void setHighlightRotateDegrees(float rotateDegrees) {
        mFrontTextView.setHighlightRotateDegrees(rotateDegrees);
        invalidate();
    }

    public float getHighlightWidth() {
        return mFrontTextView.getHighlightWidth();
    }

    public void setHighlightWidth(float highlightWidth) {
        mFrontTextView.setHighlightWidth(highlightWidth);
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
            mHighlightEffectAnim = ObjectAnimator.ofFloat(this, "startHighlightOffset", -mFrontTextView.getHighlightWidth(), viewWidth+ mFrontTextView.getHighlightWidth());
        }else {
            mHighlightEffectAnim.cancel();
        }
        if (mFrontTextView.getStartDirection() == FROM_RIGHT){
            mHighlightEffectAnim.setFloatValues(viewWidth+ mFrontTextView.getHighlightWidth(),-mFrontTextView.getHighlightWidth());
        }else if (mFrontTextView.getStartDirection() == FROM_TOP){
            mHighlightEffectAnim.setFloatValues(-mFrontTextView.getHighlightWidth(), viewHeight+ mFrontTextView.getHighlightWidth());
        }else if (mFrontTextView.getStartDirection() == FROM_BOTTOM){
            mHighlightEffectAnim.setFloatValues(viewHeight+ mFrontTextView.getHighlightWidth(),-mFrontTextView.getHighlightWidth());
        }else {
            mHighlightEffectAnim.setFloatValues(-mFrontTextView.getHighlightWidth(), viewWidth+ mFrontTextView.getHighlightWidth());
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
        return mFrontTextView.getStartDirection();
    }

    public void setStartDirection(@HighlightImageView.StartDirection int startDirection) {
        mFrontTextView.setStartDirection(startDirection);
    }

    @ColorInt
    public int getHighlightColor() {
        return mFrontTextView.getHighlightColor();
    }

    public void setHighlightColor(@ColorInt int highlightColor) {
        if (mFrontTextView != null)
        mFrontTextView.setHighlightColor(highlightColor);
    }
    private static class HighlightFrontTextView extends AppCompatTextView {
        private float startHighlightOffset = 0;
        private float highlightWidth = 0;
        private final Paint mImagePaint;
        private float highlightRotateDegrees = 0;
        private int highlightColor;
        private int highlightEndColor;
        private int mStartDirection = FROM_LEFT;
        private AppCompatTextView mTextView;

        public HighlightFrontTextView(@NonNull Context context) {
            this(context, null);
        }

        public HighlightFrontTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public HighlightFrontTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            mTextView = new AppCompatTextView(context, attrs, defStyleAttr);
            mImagePaint = new Paint();
            mImagePaint.setColor(Color.BLACK);
            mImagePaint.setAntiAlias(true);
            mImagePaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mImagePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        }


        @SuppressLint("DrawAllocation")
        @Override
        protected void onDraw(Canvas canvas) {
            int hypotenuseLength = (int) Math.sqrt(Math.pow(getWidth(),2)+Math.pow(getHeight(),2));
            int left = -(hypotenuseLength - getWidth())/2;
            int top = -(hypotenuseLength - getHeight())/2;
            int right = getWidth() + (hypotenuseLength - getWidth())/2;
            int bottom = getHeight() + (hypotenuseLength - getHeight())/2;

            canvas.rotate(highlightRotateDegrees,getWidth()/2,getHeight()/2);

            mImagePaint.setXfermode(null);
            canvas.saveLayer(new RectF(left, top, right, bottom), mImagePaint, Canvas.ALL_SAVE_FLAG);
            if (mStartDirection == FROM_TOP||mStartDirection == FROM_BOTTOM){
                LinearGradient linearGradient =
                        new LinearGradient(0, startHighlightOffset, 0, startHighlightOffset + highlightWidth,
                                new int[]{highlightEndColor, highlightColor,highlightColor, highlightEndColor},
                                new float[]{0,.45f,.55f,1}, Shader.TileMode.CLAMP);
                mImagePaint.setShader(linearGradient);
                canvas.drawRect(left, startHighlightOffset, right,startHighlightOffset + highlightWidth,mImagePaint);
            }else {
                LinearGradient linearGradient =
                        new LinearGradient(startHighlightOffset, 0, startHighlightOffset + highlightWidth, 0,
                                new int[]{highlightEndColor, highlightColor,highlightColor, highlightEndColor},
                                new float[]{0,.45f,.55f,1}, Shader.TileMode.CLAMP);
                mImagePaint.setShader(linearGradient);
                canvas.drawRect(startHighlightOffset, top, startHighlightOffset + highlightWidth,bottom,mImagePaint);
            }
            mImagePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.saveLayer(new RectF(left, top, right, bottom), mImagePaint, Canvas.ALL_SAVE_FLAG);
            canvas.rotate(-highlightRotateDegrees,getWidth()/2,getHeight()/2);
            super.onDraw(canvas);

        }

        public float getHighlightRotateDegrees() {
            return highlightRotateDegrees;
        }

        public void setHighlightRotateDegrees(float highlightRotateDegrees) {
            this.highlightRotateDegrees = highlightRotateDegrees;
        }

        public float getStartHighlightOffset() {
            return startHighlightOffset;
        }

        public void getStartHighlightOffset(float startHighlightOffset) {
            this.startHighlightOffset = startHighlightOffset;
        }

        public float getHighlightWidth() {
            return highlightWidth;
        }

        public void setHighlightWidth(float highlightWidth) {
            this.highlightWidth = highlightWidth;
        }

        @ColorInt
        public int getHighlightColor() {
            return highlightColor;
        }

        public void setHighlightColor(@ColorInt int highlightColor) {
            this.highlightColor = highlightColor;

            float[] resourceColorHsv = new float[3];
            Color.colorToHSV(highlightColor, resourceColorHsv);

            this.highlightEndColor = Color.HSVToColor(1,resourceColorHsv);
        }

        public int getStartDirection() {
            return mStartDirection;
        }

        public void setStartDirection(@HighlightImageView.StartDirection int startDirection) {
            this.mStartDirection = startDirection;
        }
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        if (mFrontTextView != null){
            mFrontTextView.setLayoutParams(params);
        }

        if (mTextView != null){
            mTextView.setLayoutParams(params);
        }
        super.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mFrontTextView.measure(widthMeasureSpec, heightMeasureSpec);
        mTextView.measure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mFrontTextView.layout(left, top, right, bottom);
        mTextView.layout(left, top, right, bottom);
        super.onLayout(changed, left, top, right, bottom);
        isFinishLayout = true;
        if (isStartBeforeLayout){
            startAnim();
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (mTextView != null){
            mTextView.setText(text, type);
        }
        if (mFrontTextView != null){
            mFrontTextView.setText(text, type);
        }
    }

}
