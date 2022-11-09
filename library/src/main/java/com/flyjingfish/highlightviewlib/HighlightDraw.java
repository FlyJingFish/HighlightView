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

    private float startHighlightOffset = 0;
    private float highlightWidth = 0;
    private final Paint mImagePaint;
    private float highlightRotateDegrees = 0;
    private int highlightColor;
    private int highlightEndColor;
    private int mStartDirection = FROM_LEFT;
    private final HighlightDrawView highlightDrawView;


    public HighlightDraw(HighlightDrawView highlightDrawView) {
        this.highlightDrawView = highlightDrawView;
        mImagePaint = new Paint();
        mImagePaint.setColor(Color.BLACK);
        mImagePaint.setAntiAlias(true);
        mImagePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mImagePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    private int getWidth(){
        return highlightDrawView.getView().getWidth();
    }

    private int getHeight(){
        return highlightDrawView.getView().getHeight();
    }

    @SuppressLint("DrawAllocation")
    public void onDraw(Canvas canvas) {
        int hypotenuseLength = (int) Math.sqrt(Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2));
        int left = -(hypotenuseLength - getWidth()) / 2;
        int top = -(hypotenuseLength - getHeight()) / 2;
        int right = getWidth() + (hypotenuseLength - getWidth()) / 2;
        int bottom = getHeight() + (hypotenuseLength - getHeight()) / 2;

        canvas.rotate(highlightRotateDegrees, getWidth() / 2, getHeight() / 2);

        mImagePaint.setXfermode(null);
        canvas.saveLayer(new RectF(left, top, right, bottom), mImagePaint, Canvas.ALL_SAVE_FLAG);
        if (mStartDirection == FROM_TOP || mStartDirection == FROM_BOTTOM) {
            LinearGradient linearGradient =
                    new LinearGradient(0, startHighlightOffset, 0, startHighlightOffset + highlightWidth,
                            new int[]{highlightEndColor, highlightColor, highlightColor, highlightEndColor},
                            new float[]{0, .45f, .55f, 1}, Shader.TileMode.CLAMP);
            mImagePaint.setShader(linearGradient);
            canvas.drawRect(left, startHighlightOffset, right, startHighlightOffset + highlightWidth, mImagePaint);
        } else {
            LinearGradient linearGradient =
                    new LinearGradient(startHighlightOffset, 0, startHighlightOffset + highlightWidth, 0,
                            new int[]{highlightEndColor, highlightColor, highlightColor, highlightEndColor},
                            new float[]{0, .45f, .55f, 1}, Shader.TileMode.CLAMP);
            mImagePaint.setShader(linearGradient);
            canvas.drawRect(startHighlightOffset, top, startHighlightOffset + highlightWidth, bottom, mImagePaint);
        }
        mImagePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.saveLayer(new RectF(left, top, right, bottom), mImagePaint, Canvas.ALL_SAVE_FLAG);
        canvas.rotate(-highlightRotateDegrees, getWidth() / 2, getHeight() / 2);

    }

    public float getHighlightRotateDegrees() {
        return highlightRotateDegrees;
    }

    public void setHighlightRotateDegrees(float highlightRotateDegrees) {
        this.highlightRotateDegrees = highlightRotateDegrees;
    }

    public float getStartHighlightOffset() {
        return startHighlightOffset;
    }

    public void getStartHighlightOffset(float startHighlightOffset) {
        this.startHighlightOffset = startHighlightOffset;
    }

    public float getHighlightWidth() {
        return highlightWidth;
    }

    public void setHighlightWidth(float highlightWidth) {
        this.highlightWidth = highlightWidth;
    }

    @ColorInt
    public int getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(@ColorInt int highlightColor) {
        this.highlightColor = highlightColor;

        float[] resourceColorHsv = new float[3];
        Color.colorToHSV(highlightColor, resourceColorHsv);

        this.highlightEndColor = Color.HSVToColor(1, resourceColorHsv);
    }

    public int getStartDirection() {
        return mStartDirection;
    }

    public void setStartDirection(@HighlightAnimHolder.StartDirection int startDirection) {
        this.mStartDirection = startDirection;
    }
}