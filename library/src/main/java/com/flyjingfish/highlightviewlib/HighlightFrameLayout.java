package com.flyjingfish.highlightviewlib;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HighlightFrameLayout extends FrameLayout implements HighlightView, HighlightDrawView, AnimHolder {
    private final HighlightAnimHolder mHighlightAnimHolder;
    private final HighlightDraw mHighlightDraw;
    private final Paint mImagePaint;

    public HighlightFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public HighlightFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HighlightFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHighlightDraw = new HighlightDraw(this);

        mHighlightAnimHolder = new HighlightAnimHolder(this, this);

        mImagePaint = InitAttrs.initPaint();

        InitAttrs.init(context, attrs, mHighlightAnimHolder);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mImagePaint.setXfermode(null);
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), mImagePaint, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);


        mImagePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), mImagePaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mImagePaint);

        mImagePaint.setXfermode(null);
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), mImagePaint, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        getHighlightDraw().onDraw(canvas);
        super.dispatchDraw(canvas);
    }

    @Override
    public View thisView() {
        return this;
    }

    @Override
    public HighlightAnimHolder getHighlightAnimHolder() {
        return mHighlightAnimHolder;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHighlightAnimHolder.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public HighlightDraw getHighlightDraw() {
        return mHighlightDraw;
    }
}
