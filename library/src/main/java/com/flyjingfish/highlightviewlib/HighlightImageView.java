package com.flyjingfish.highlightviewlib;

import static com.flyjingfish.highlightviewlib.HighlightAnimHolder.FROM_LEFT;
import static com.flyjingfish.highlightviewlib.HighlightAnimHolder.RESTART;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class HighlightImageView extends AppCompatImageView implements HighlightView {
    private final HighlightFrontImageView mFrontImageView;
    private final HighlightAnimHolder mHighlightAnimHolder;

    public HighlightImageView(@NonNull Context context) {
        this(context, null);
    }

    public HighlightImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HighlightImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFrontImageView = new HighlightFrontImageView(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HighlightImageView);
        int highlightColor = a.getColor(R.styleable.HighlightImageView_highlight_image_highlightColor,Color.TRANSPARENT);
        long duration = a.getInteger(R.styleable.HighlightImageView_highlight_image_duration,1000);
        int repeatCount = a.getInteger(R.styleable.HighlightImageView_highlight_image_repeatCount,0);
        int repeatMode = a.getInt(R.styleable.HighlightImageView_highlight_image_repeatMode,RESTART);
        float highlightWidth = a.getDimension(R.styleable.HighlightImageView_highlight_image_highlightWidth,10);
        float highlightRotateDegrees = a.getFloat(R.styleable.HighlightImageView_highlight_image_highlightRotateDegrees,30);
        int startDirection = a.getColor(R.styleable.HighlightImageView_highlight_image_startDirection,FROM_LEFT);
        boolean autoStart = a.getBoolean(R.styleable.HighlightImageView_highlight_image_autoStart,false);
        a.recycle();

        mHighlightAnimHolder = new HighlightAnimHolder(mFrontImageView,this);

        mHighlightAnimHolder.setDuration(duration);
        mHighlightAnimHolder.setRepeatCount(repeatCount);
        mHighlightAnimHolder.setRepeatMode(repeatMode);
        mHighlightAnimHolder.setHighlightWidth(highlightWidth);
        mHighlightAnimHolder.setHighlightRotateDegrees(highlightRotateDegrees);
        mHighlightAnimHolder.setStartDirection(startDirection);
        mHighlightAnimHolder.setHighlightColor(highlightColor);
        mFrontImageView.setBackground(null);
        if (autoStart){
            mHighlightAnimHolder.startHighlightEffect();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mFrontImageView.draw(canvas);
    }

    @Override
    public View thisView() {
        return this;
    }

    public HighlightAnimHolder getHighlightAnimHolder() {
        return mHighlightAnimHolder;
    }

    private static class HighlightFrontImageView extends AppCompatImageView implements HighlightDrawView{
        private final HighlightDraw highlightDraw;

        public HighlightFrontImageView(@NonNull Context context) {
            this(context, null);
        }

        public HighlightFrontImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public HighlightFrontImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);

            highlightDraw = new HighlightDraw(this);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            highlightDraw.onDraw(canvas);
            super.onDraw(canvas);
        }

        @Override
        public View getView() {
            return this;
        }

        @Override
        public HighlightDraw getHighlightDraw() {
            return highlightDraw;
        }
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        if (mFrontImageView != null){
            mFrontImageView.setLayoutParams(params);
        }
        super.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mFrontImageView.measure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mFrontImageView.layout(left, top, right, bottom);
        super.onLayout(changed, left, top, right, bottom);
        mHighlightAnimHolder.setFinishLayout(true);
        if (mHighlightAnimHolder.isStartBeforeLayout()){
            mHighlightAnimHolder.startAnim();
        }
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (mFrontImageView != null){
            mFrontImageView.setScaleType(scaleType);
        }
        super.setScaleType(scaleType);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if (mFrontImageView != null){
            mFrontImageView.setImageBitmap(bm);
        }
        super.setImageBitmap(bm);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        if (mFrontImageView != null){
            mFrontImageView.setImageDrawable(drawable);
        }
        super.setImageDrawable(drawable);
    }

    @Override
    public void setImageResource(int resId) {
        if (mFrontImageView != null){
            mFrontImageView.setImageResource(resId);
        }
        super.setImageResource(resId);
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        if (mFrontImageView != null){
            mFrontImageView.setImageURI(uri);
        }
        super.setImageURI(uri);
    }

    @Override
    public void setImageAlpha(int alpha) {
        if (mFrontImageView != null){
            mFrontImageView.setImageAlpha(alpha);
        }
        super.setImageAlpha(alpha);
    }

    @Override
    public void setImageLevel(int level) {
        if (mFrontImageView != null){
            mFrontImageView.setImageLevel(level);
        }
        super.setImageLevel(level);
    }

    @Override
    public void setImageState(int[] state, boolean merge) {
        if (mFrontImageView != null){
            mFrontImageView.setImageState(state, merge);
        }
        super.setImageState(state, merge);
    }

    @Override
    public void setImageTintList(@Nullable ColorStateList tint) {
        if (mFrontImageView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mFrontImageView.setImageTintList(tint);
        }
        super.setImageTintList(tint);
    }

    @Override
    public void setImageTintMode(@Nullable PorterDuff.Mode tintMode) {
        if (mFrontImageView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mFrontImageView.setImageTintMode(tintMode);
        }
        super.setImageTintMode(tintMode);
    }

    @Override
    public void setImageMatrix(Matrix matrix) {
        if (mFrontImageView != null){
            mFrontImageView.setImageMatrix(matrix);
        }
        super.setImageMatrix(matrix);
    }
}
