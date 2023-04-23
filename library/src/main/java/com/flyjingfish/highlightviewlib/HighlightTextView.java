package com.flyjingfish.highlightviewlib;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.LayoutDirection;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.text.TextUtilsCompat;

import java.util.Locale;

public class HighlightTextView extends AppCompatTextView implements HighlightView, AnimHolder {
    private final HighlightFrontTextView mFrontTextView;
    private final HighlightAnimHolder mHighlightAnimHolder;
    private final boolean isRtl;
    private final RectF mRectF = new RectF();
    private final PorterDuffXfermode mSrcInXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    public HighlightTextView(@NonNull Context context) {
        this(context, null);
    }

    public HighlightTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HighlightTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        isRtl = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == LayoutDirection.RTL;
        mFrontTextView = new HighlightFrontTextView(context, attrs, defStyleAttr);
        mHighlightAnimHolder = new HighlightAnimHolder(mFrontTextView, this);

        mFrontTextView.setBackground(null);
        mFrontTextView.setTextColor(Color.BLACK);
        initCompoundDrawables();

        mFrontTextView.setCompoundDrawablePadding(getCompoundDrawablePadding());

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
        mFrontTextView.draw(canvas);
    }


    private static class HighlightFrontTextView extends AppCompatTextView implements HighlightDrawView {
        private final HighlightDraw mHighlightDraw;

        public HighlightFrontTextView(@NonNull Context context) {
            this(context, null);
        }

        public HighlightFrontTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public HighlightFrontTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            mHighlightDraw = new HighlightDraw(this);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            mHighlightDraw.onDraw(canvas);
            super.onDraw(canvas);
        }

        @Override
        public View thisView() {
            return this;
        }

        @Override
        public HighlightDraw getHighlightDraw() {
            return mHighlightDraw;
        }
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        if (mFrontTextView != null) {
            mFrontTextView.setLayoutParams(params);
        }

        super.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mFrontTextView.measure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mFrontTextView.layout(left, top, right, bottom);
        super.onLayout(changed, left, top, right, bottom);
        mHighlightAnimHolder.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (mFrontTextView != null) {
            mFrontTextView.setText(text, type);
        }
        super.setText(text, type);
    }

    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        initCompoundDrawables();
    }

    @Override
    public void setCompoundDrawablesRelative(@Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        super.setCompoundDrawablesRelative(start, top, end, bottom);
        initCompoundDrawables();
    }

    @Override
    public void setCompoundDrawablePadding(int pad) {
        super.setCompoundDrawablePadding(pad);
        if (mFrontTextView != null) {
            mFrontTextView.setCompoundDrawablePadding(pad);
        }
    }

    private void initCompoundDrawables() {
        if (mFrontTextView == null) {
            return;
        }
        Drawable[] drawablesRelative = getCompoundDrawablesRelative();

        Drawable[] drawables = getCompoundDrawables();

        Drawable drawableLeft;
        Drawable drawableRight;
        Drawable drawableTop = null;
        Drawable drawableBottom = null;
        if (isRtl) {
            if (drawablesRelative[0] != null || drawablesRelative[2] != null) {
                drawableLeft = drawablesRelative[2];
                drawableRight = drawablesRelative[0];
            } else {
                drawableLeft = drawables[0];
                drawableRight = drawables[2];
            }

        } else {
            if (drawablesRelative[0] != null || drawablesRelative[2] != null) {
                drawableLeft = drawablesRelative[0];
                drawableRight = drawablesRelative[2];
            } else {
                drawableLeft = drawables[0];
                drawableRight = drawables[2];
            }

        }

        if (drawablesRelative[1] != null) {
            drawableTop = drawablesRelative[1];
        } else if (drawables[1] != null) {
            drawableTop = drawables[1];
        }

        if (drawablesRelative[3] != null) {
            drawableBottom = drawablesRelative[3];
        } else if (drawables[3] != null) {
            drawableBottom = drawables[3];
        }

        mFrontTextView.setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

}
