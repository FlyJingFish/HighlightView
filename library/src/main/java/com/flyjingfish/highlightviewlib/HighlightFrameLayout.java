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
    private final HighlightAnimHolder mHighlightAnimHolder = new HighlightAnimHolder(this, this);
    private final HighlightDraw mHighlightDraw = new HighlightDraw(this);
    private final Paint mImagePaint = InitAttrs.initPaint();

    private final RectF mRectF = new RectF();
    private final PorterDuffXfermode mSrcInXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    public HighlightFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public HighlightFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HighlightFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitAttrs.init(context, attrs, mHighlightAnimHolder);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mRectF.set(0, 0, getWidth(), getHeight());
        mImagePaint.setXfermode(null);
        canvas.saveLayer(mRectF, mImagePaint, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);


        mImagePaint.setXfermode(mSrcInXfermode);
        canvas.saveLayer(mRectF, mImagePaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawRect(mRectF, mImagePaint);

        mImagePaint.setXfermode(null);
        canvas.saveLayer(mRectF, mImagePaint, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        mHighlightDraw.onDraw(canvas);
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
