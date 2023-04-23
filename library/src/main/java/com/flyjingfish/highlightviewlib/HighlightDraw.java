package com.flyjingfish.highlightviewlib;

import static com.flyjingfish.highlightviewlib.HighlightAnimHolder.FROM_BOTTOM;
import static com.flyjingfish.highlightviewlib.HighlightAnimHolder.FROM_LEFT;
import static com.flyjingfish.highlightviewlib.HighlightAnimHolder.FROM_TOP;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;

import androidx.annotation.ColorInt;

class HighlightDraw {

    private float mStartHighlightOffset = 0;
    private float mHighlightWidth = 0;
    private final Paint mImagePaint;
    private float mHighlightRotateDegrees = 0;
    private int mHighlightColor;
    private int mHighlightEndColor;
    private int mStartDirection = FROM_LEFT;
    private final HighlightDrawView mHighlightDrawView;


    protected HighlightDraw(HighlightDrawView highlightDrawView) {
        this.mHighlightDrawView = highlightDrawView;
        mImagePaint = InitAttrs.initPaint();
    }

    private int getWidth() {
        return mHighlightDrawView.thisView().getWidth();
    }

    private int getHeight() {
        return mHighlightDrawView.thisView().getHeight();
    }

    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        int hypotenuseLength = (int) Math.sqrt(Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2));
        int left = -(hypotenuseLength - getWidth()) / 2;
        int top = -(hypotenuseLength - getHeight()) / 2;
        int right = getWidth() + (hypotenuseLength - getWidth()) / 2;
        int bottom = getHeight() + (hypotenuseLength - getHeight()) / 2;

        canvas.rotate(mHighlightRotateDegrees, getWidth() / 2f, getHeight() / 2f);

        mImagePaint.setXfermode(null);
        canvas.saveLayer(new RectF(left, top, right, bottom), mImagePaint, Canvas.ALL_SAVE_FLAG);
        if (mStartDirection == FROM_TOP || mStartDirection == FROM_BOTTOM) {
            LinearGradient linearGradient =
                    new LinearGradient(0, mStartHighlightOffset, 0, mStartHighlightOffset + mHighlightWidth,
                            new int[]{mHighlightEndColor, mHighlightColor, mHighlightColor, mHighlightEndColor},
                            new float[]{0, .45f, .55f, 1}, Shader.TileMode.CLAMP);
            mImagePaint.setShader(linearGradient);
            canvas.drawRect(left, mStartHighlightOffset, right, mStartHighlightOffset + mHighlightWidth, mImagePaint);
        } else {
            LinearGradient linearGradient =
                    new LinearGradient(mStartHighlightOffset, 0, mStartHighlightOffset + mHighlightWidth, 0,
                            new int[]{mHighlightEndColor, mHighlightColor, mHighlightColor, mHighlightEndColor},
                            new float[]{0, .45f, .55f, 1}, Shader.TileMode.CLAMP);
            mImagePaint.setShader(linearGradient);
            canvas.drawRect(mStartHighlightOffset, top, mStartHighlightOffset + mHighlightWidth, bottom, mImagePaint);
        }
        mImagePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.saveLayer(new RectF(left, top, right, bottom), mImagePaint, Canvas.ALL_SAVE_FLAG);
        canvas.rotate(-mHighlightRotateDegrees, getWidth() / 2f, getHeight() / 2f);

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
        return mHighlightColor;
    }

    protected void setHighlightColor(@ColorInt int highlightColor) {
        this.mHighlightColor = highlightColor;

        float[] resourceColorHsv = new float[3];
        Color.colorToHSV(highlightColor, resourceColorHsv);

        this.mHighlightEndColor = Color.HSVToColor(1, resourceColorHsv);
    }

    protected int getStartDirection() {
        return mStartDirection;
    }

    protected void setStartDirection(@HighlightAnimHolder.StartDirection int startDirection) {
        this.mStartDirection = startDirection;
    }
}