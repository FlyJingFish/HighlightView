package com.flyjingfish.highlightviewlib;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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

public class HighlightImageView extends AppCompatImageView implements HighlightView, AnimHolder {
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
        mHighlightAnimHolder = new HighlightAnimHolder(mFrontImageView, this);
        mFrontImageView.setBackground(null);

        InitAttrs.init(context, attrs, mHighlightAnimHolder);
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

    @Override
    public HighlightAnimHolder getHighlightAnimHolder() {
        return mHighlightAnimHolder;
    }

    private static class HighlightFrontImageView extends AppCompatImageView implements HighlightDrawView {
        private final HighlightDraw mHighlightDraw;

        public HighlightFrontImageView(@NonNull Context context) {
            this(context, null);
        }

        public HighlightFrontImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public HighlightFrontImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        if (mFrontImageView != null) {
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
        mHighlightAnimHolder.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (mFrontImageView != null) {
            mFrontImageView.setScaleType(scaleType);
        }
        super.setScaleType(scaleType);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if (mFrontImageView != null) {
            mFrontImageView.setImageBitmap(bm);
        }
        super.setImageBitmap(bm);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        if (mFrontImageView != null) {
            mFrontImageView.setImageDrawable(drawable);
        }
        super.setImageDrawable(drawable);
    }

    @Override
    public void setImageResource(int resId) {
        if (mFrontImageView != null) {
            mFrontImageView.setImageResource(resId);
        }
        super.setImageResource(resId);
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        if (mFrontImageView != null) {
            mFrontImageView.setImageURI(uri);
        }
        super.setImageURI(uri);
    }

    @Override
    public void setImageAlpha(int alpha) {
        if (mFrontImageView != null) {
            mFrontImageView.setImageAlpha(alpha);
        }
        super.setImageAlpha(alpha);
    }

    @Override
    public void setImageLevel(int level) {
        if (mFrontImageView != null) {
            mFrontImageView.setImageLevel(level);
        }
        super.setImageLevel(level);
    }

    @Override
    public void setImageState(int[] state, boolean merge) {
        if (mFrontImageView != null) {
            mFrontImageView.setImageState(state, merge);
        }
        super.setImageState(state, merge);
    }

    @Override
    public void setImageTintList(@Nullable ColorStateList tint) {
        if (mFrontImageView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFrontImageView.setImageTintList(tint);
        }
        super.setImageTintList(tint);
    }

    @Override
    public void setImageTintMode(@Nullable PorterDuff.Mode tintMode) {
        if (mFrontImageView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFrontImageView.setImageTintMode(tintMode);
        }
        super.setImageTintMode(tintMode);
    }

    @Override
    public void setImageMatrix(Matrix matrix) {
        if (mFrontImageView != null) {
            mFrontImageView.setImageMatrix(matrix);
        }
        super.setImageMatrix(matrix);
    }
}
