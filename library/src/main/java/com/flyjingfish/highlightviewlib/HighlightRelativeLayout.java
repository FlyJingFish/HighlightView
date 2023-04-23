package com.flyjingfish.highlightviewlib;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HighlightRelativeLayout extends RelativeLayout implements HighlightView, HighlightDrawView, AnimHolder {
    private final HighlightAnimHolder mHighlightAnimHolder;
    private final HighlightDraw mHighlightDraw;
    private final Paint mImagePaint;

    private final RectF mRectF;

    private final PorterDuffXfermode mSrcInXfermode;

    public HighlightRelativeLayout(@NonNull Context context) {
        this(context, null);
    }

    public HighlightRelativeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HighlightRelativeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHighlightDraw = new HighlightDraw(this);

        mHighlightAnimHolder = new HighlightAnimHolder(this, this);

        mImagePaint = InitAttrs.initPaint();

        InitAttrs.init(context, attrs, mHighlightAnimHolder);

        mRectF = new RectF();

        mSrcInXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
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
