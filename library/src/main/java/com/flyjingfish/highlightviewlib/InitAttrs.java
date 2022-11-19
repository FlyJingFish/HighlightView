package com.flyjingfish.highlightviewlib;

import static com.flyjingfish.highlightviewlib.HighlightAnimHolder.FROM_LEFT;
import static com.flyjingfish.highlightviewlib.HighlightAnimHolder.RESTART;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class InitAttrs {
    static void init(@NonNull Context context, @Nullable AttributeSet attrs,@NonNull HighlightAnimHolder highlightAnimHolder){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HighlightAnimView);
        int highlightColor = a.getColor(R.styleable.HighlightAnimView_highlight_view_highlightColor, Color.TRANSPARENT);
        long duration = a.getInteger(R.styleable.HighlightAnimView_highlight_view_duration,1000);
        int repeatCount = a.getInteger(R.styleable.HighlightAnimView_highlight_view_repeatCount,0);
        int repeatMode = a.getInt(R.styleable.HighlightAnimView_highlight_view_repeatMode,RESTART);
        float highlightWidth = a.getDimension(R.styleable.HighlightAnimView_highlight_view_highlightWidth,10);
        float highlightRotateDegrees = a.getFloat(R.styleable.HighlightAnimView_highlight_view_highlightRotateDegrees,30);
        int startDirection = a.getColor(R.styleable.HighlightAnimView_highlight_view_startDirection,FROM_LEFT);
        boolean autoStart = a.getBoolean(R.styleable.HighlightAnimView_highlight_view_autoStart,false);
        a.recycle();


        highlightAnimHolder.setDuration(duration);
        highlightAnimHolder.setRepeatCount(repeatCount);
        highlightAnimHolder.setRepeatMode(repeatMode);
        highlightAnimHolder.setHighlightWidth(highlightWidth);
        highlightAnimHolder.setHighlightRotateDegrees(highlightRotateDegrees);
        highlightAnimHolder.setStartDirection(startDirection);
        highlightAnimHolder.setHighlightColor(highlightColor);

        if (autoStart){
            highlightAnimHolder.startHighlightEffect();
        }
    }

    static Paint initPaint(){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        return paint;
    }
}
