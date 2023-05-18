package com.flyjingfish.highlightviewlib;

import static com.flyjingfish.highlightviewlib.HighlightAnimHolder.FROM_BOTTOM;
import static com.flyjingfish.highlightviewlib.HighlightAnimHolder.FROM_LEFT;
import static com.flyjingfish.highlightviewlib.HighlightAnimHolder.FROM_TOP;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;

import androidx.annotation.ColorInt;

import java.util.ArrayList;
import java.util.List;

class HighlightDraw {

    private final RectF mRectF = new RectF();
    private final PorterDuffXfermode mDstInXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private final float[] mGradientPosition = new float[]{0, .45f, .55f, 1};
    private final int[] mGradientColors = new int[4];
    private float mStartHighlightOffset = 0;
    private float mHighlightWidth = 0;
    private final Paint mImagePaint = InitAttrs.initPaint();
    private float mHighlightRotateDegrees = 0;
    private ColorStateList mHighlightColor;
    private int mCurHighlightColor;
    private int mStartDirection = FROM_LEFT;
    private final HighlightDrawView mHighlightDrawView;


    protected HighlightDraw(HighlightDrawView highlightDrawView) {
        this.mHighlightDrawView = highlightDrawView;
    }

    private int getWidth() {
        return mHighlightDrawView.thisView().getWidth();
    }

    private int getHeight() {
        return mHighlightDrawView.thisView().getHeight();
    }

    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        int hypotenuseLength = (int) Math.sqrt(Math.pow(viewWidth, 2) + Math.pow(viewHeight, 2));
        int left = -(hypotenuseLength - viewWidth) / 2;
        int top = -(hypotenuseLength - viewHeight) / 2;
        int right = viewWidth + (hypotenuseLength - viewWidth) / 2;
        int bottom = viewHeight + (hypotenuseLength - viewHeight) / 2;

        canvas.rotate(mHighlightRotateDegrees, viewWidth / 2f, viewHeight / 2f);

        mImagePaint.setXfermode(null);
        mRectF.set(left, top, right, bottom);
        canvas.saveLayer(mRectF, mImagePaint, Canvas.ALL_SAVE_FLAG);
        if (mStartDirection == FROM_TOP || mStartDirection == FROM_BOTTOM) {
            LinearGradient linearGradient =
                    new LinearGradient(0, mStartHighlightOffset, 0, mStartHighlightOffset + mHighlightWidth,
                            mGradientColors, mGradientPosition, Shader.TileMode.CLAMP);
            mImagePaint.setShader(linearGradient);
            canvas.drawRect(left, mStartHighlightOffset, right, mStartHighlightOffset + mHighlightWidth, mImagePaint);
        } else {
            LinearGradient linearGradient =
                    new LinearGradient(mStartHighlightOffset, 0, mStartHighlightOffset + mHighlightWidth, 0,
                            mGradientColors, mGradientPosition, Shader.TileMode.CLAMP);
            mImagePaint.setShader(linearGradient);
            canvas.drawRect(mStartHighlightOffset, top, mStartHighlightOffset + mHighlightWidth, bottom, mImagePaint);
        }
        mImagePaint.setXfermode(mDstInXfermode);
        canvas.saveLayer(mRectF, mImagePaint, Canvas.ALL_SAVE_FLAG);
        canvas.rotate(-mHighlightRotateDegrees, viewWidth / 2f, viewHeight / 2f);

    }

    protected float getHighlightRotateDegrees() {
        return mHighlightRotateDegrees;
    }

    protected void setHighlightRotateDegrees(float highlightRotateDegrees) {
        this.mHighlightRotateDegrees = highlightRotateDegrees;
    }

    protected float setStartHighlightOffset() {
        return mStartHighlightOffset;
    }

    protected void setStartHighlightOffset(float startHighlightOffset) {
        this.mStartHighlightOffset = startHighlightOffset;
    }

    protected float getHighlightWidth() {
        return mHighlightWidth;
    }

    protected void setHighlightWidth(float highlightWidth) {
        this.mHighlightWidth = highlightWidth;
    }

    @ColorInt
    protected int getHighlightColor() {
        return mCurHighlightColor;
    }

    protected ColorStateList getHighlightColors() {
        return mHighlightColor;
    }

    protected void setHighlightColor(ColorStateList highlightColor) {
        this.mHighlightColor = highlightColor;
        setGradientColors();
    }

    protected void setHighlightColor(@ColorInt int highlightColor) {
        setHighlightColor(ColorStateList.valueOf(highlightColor));
    }

    void setGradientColors(){
        if (mHighlightColor == null){
            return;
        }
        boolean inval = false;
        final int[] drawableState = mHighlightDrawView.thisView().getDrawableState();
        int highlightColor = mHighlightColor.getColorForState(drawableState, 0);
        if (highlightColor != mCurHighlightColor) {
            mCurHighlightColor = highlightColor;
            float[] resourceColorHsv = new float[3];
            Color.colorToHSV(highlightColor, resourceColorHsv);

            int mHighlightEndColor = Color.HSVToColor(1, resourceColorHsv);

            mGradientColors[0] = mHighlightEndColor;
            mGradientColors[1] = highlightColor;
            mGradientColors[2] = highlightColor;
            mGradientColors[3] = mHighlightEndColor;
            inval = true;
        }
        if (inval){
            mHighlightDrawView.thisView().invalidate();
        }
    }

    protected int getStartDirection() {
        return mStartDirection;
    }

    protected void setStartDirection(@HighlightAnimHolder.StartDirection int startDirection) {
        this.mStartDirection = startDirection;
    }
}