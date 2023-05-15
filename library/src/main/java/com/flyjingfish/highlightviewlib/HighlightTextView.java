package com.flyjingfish.highlightviewlib;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class HighlightTextView extends AppCompatTextView implements HighlightView, HighlightDrawView, AnimHolder {
    private final HighlightAnimHolder mHighlightAnimHolder = new HighlightAnimHolder(this, this);
    private final RectF mRectF = new RectF();
    private final PorterDuffXfermode mSrcInXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private final HighlightDraw mHighlightDraw = new HighlightDraw(this);

    public HighlightTextView(@NonNull Context context) {
        this(context, null);
    }

    public HighlightTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HighlightTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitAttrs.init(context, attrs, mHighlightAnimHolder);
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
    protected void onDraw(Canvas canvas) {
        mRectF.set(0, 0, getWidth(), getHeight());
        Paint paint = getPaint();
        paint.setXfermode(null);
        canvas.saveLayer(mRectF, paint, Canvas.ALL_SAVE_FLAG);
        super.onDraw(canvas);


        paint.setXfermode(mSrcInXfermode);
        canvas.saveLayer(mRectF, paint, Canvas.ALL_SAVE_FLAG);
        canvas.drawRect(mRectF, paint);

        paint.setXfermode(null);
        canvas.saveLayer(mRectF, paint, Canvas.ALL_SAVE_FLAG);
        super.onDraw(canvas);

        mHighlightDraw.onDraw(canvas);
        super.onDraw(canvas);
    }

    @Override
    public HighlightDraw getHighlightDraw() {
        return mHighlightDraw;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHighlightAnimHolder.onLayout(changed, left, top, right, bottom);
    }

}
