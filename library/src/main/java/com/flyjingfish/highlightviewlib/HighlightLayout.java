package com.flyjingfish.highlightviewlib;

import static com.flyjingfish.highlightviewlib.HighlightAnimHolder.FROM_LEFT;
import static com.flyjingfish.highlightviewlib.HighlightAnimHolder.RESTART;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HighlightLayout extends LinearLayout implements HighlightView ,HighlightDrawView{
    private final HighlightAnimHolder mHighlightAnimHolder;
    private final HighlightDraw mHighlightDraw;
    private final Paint mImagePaint;

    public HighlightLayout(@NonNull Context context) {
        this(context, null);
    }

    public HighlightLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HighlightLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

        mHighlightDraw = new HighlightDraw(this);

        mHighlightAnimHolder = new HighlightAnimHolder(this,this);

        mHighlightAnimHolder.setDuration(duration);
        mHighlightAnimHolder.setRepeatCount(repeatCount);
        mHighlightAnimHolder.setRepeatMode(repeatMode);
        mHighlightAnimHolder.setHighlightWidth(highlightWidth);
        mHighlightAnimHolder.setHighlightRotateDegrees(highlightRotateDegrees);
        mHighlightAnimHolder.setStartDirection(startDirection);
        mHighlightAnimHolder.setHighlightColor(highlightColor);
        if (autoStart){
            mHighlightAnimHolder.startHighlightEffect();
        }

        mImagePaint = new Paint();
        mImagePaint.setColor(Color.BLACK);
        mImagePaint.setAntiAlias(true);
        mImagePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mImagePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mImagePaint.setXfermode(null);
        super.dispatchDraw(canvas);

        mImagePaint.setXfermode(null);
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(),  canvas.getHeight()), mImagePaint, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        mImagePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(),  canvas.getHeight()),  mImagePaint, Canvas.ALL_SAVE_FLAG);
        getHighlightDraw().onDraw(canvas);
        super.dispatchDraw(canvas);
    }

    @Override
    public View thisView() {
        return this;
    }

    public HighlightAnimHolder getHighlightAnimHolder() {
        return mHighlightAnimHolder;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHighlightAnimHolder.setFinishLayout(true);
        if (mHighlightAnimHolder.isStartBeforeLayout()){
            mHighlightAnimHolder.startAnim();
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public HighlightDraw getHighlightDraw() {
        return mHighlightDraw;
    }
}
