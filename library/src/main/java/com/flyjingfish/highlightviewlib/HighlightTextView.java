package com.flyjingfish.highlightviewlib;

import static com.flyjingfish.highlightviewlib.HighlightAnimHolder.FROM_LEFT;
import static com.flyjingfish.highlightviewlib.HighlightAnimHolder.RESTART;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class HighlightTextView extends AppCompatTextView implements HighlightView {
    private final HighlightFrontTextView mFrontTextView;
    private final HighlightAnimHolder highlightAnimHolder;
    private final AppCompatTextView mTextView;


    public HighlightTextView(@NonNull Context context) {
        this(context, null);
    }

    public HighlightTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HighlightTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFrontTextView = new HighlightFrontTextView(context, attrs, defStyleAttr);
        mTextView = new AppCompatTextView(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HighlightTextView);
        int highlightColor = a.getColor(R.styleable.HighlightTextView_highlight_text_highlightColor,Color.TRANSPARENT);
        long duration = a.getInteger(R.styleable.HighlightTextView_highlight_text_duration,1000);
        int repeatCount = a.getInteger(R.styleable.HighlightTextView_highlight_text_repeatCount,0);
        int repeatMode = a.getInt(R.styleable.HighlightTextView_highlight_text_repeatMode,RESTART);
        float highlightWidth = a.getDimension(R.styleable.HighlightTextView_highlight_text_highlightWidth,10);
        float highlightRotateDegrees = a.getFloat(R.styleable.HighlightTextView_highlight_text_highlightRotateDegrees,30);
        int startDirection = a.getColor(R.styleable.HighlightTextView_highlight_text_startDirection,FROM_LEFT);
        boolean autoStart = a.getBoolean(R.styleable.HighlightTextView_highlight_text_autoStart,false);
        a.recycle();

        highlightAnimHolder = new HighlightAnimHolder(mFrontTextView,this);

        highlightAnimHolder.setDuration(duration);
        highlightAnimHolder.setRepeatCount(repeatCount);
        highlightAnimHolder.setRepeatMode(repeatMode);
        highlightAnimHolder.setHighlightWidth(highlightWidth);
        highlightAnimHolder.setHighlightRotateDegrees(highlightRotateDegrees);
        highlightAnimHolder.setStartDirection(startDirection);
        highlightAnimHolder.setHighlightColor(highlightColor);

        mFrontTextView.setBackground(null);
        mTextView.setBackground(null);
        mFrontTextView.setTextColor(Color.BLACK);
        mTextView.setTextColor(Color.BLACK);
        if (autoStart){
            highlightAnimHolder.startHighlightEffect();
        }

    }

    @Override
    public View thisView() {
        return this;
    }

    public HighlightAnimHolder getHighlightAnimHolder() {
        return highlightAnimHolder;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getPaint().setXfermode(null);
        super.onDraw(canvas);

        getPaint().setXfermode(null);
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(),  canvas.getHeight()),  getPaint(), Canvas.ALL_SAVE_FLAG);
        mTextView.draw(canvas);
        getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(),  canvas.getHeight()),  getPaint(), Canvas.ALL_SAVE_FLAG);
        mFrontTextView.draw(canvas);
    }


    private static class HighlightFrontTextView extends AppCompatTextView implements HighlightDrawView{
        private final HighlightDraw highlightDraw;

        public HighlightFrontTextView(@NonNull Context context) {
            this(context, null);
        }

        public HighlightFrontTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public HighlightFrontTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            highlightDraw = new HighlightDraw(this);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            highlightDraw.onDraw(canvas);
            super.onDraw(canvas);
        }

        @Override
        public View getView() {
            return this;
        }

        @Override
        public HighlightDraw getHighlightDraw() {
            return highlightDraw;
        }
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        if (mFrontTextView != null){
            mFrontTextView.setLayoutParams(params);
        }

        if (mTextView != null){
            mTextView.setLayoutParams(params);
        }
        super.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mFrontTextView.measure(widthMeasureSpec, heightMeasureSpec);
        mTextView.measure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mFrontTextView.layout(left, top, right, bottom);
        mTextView.layout(left, top, right, bottom);
        super.onLayout(changed, left, top, right, bottom);
        highlightAnimHolder.setFinishLayout(true);
        if (highlightAnimHolder.isStartBeforeLayout()){
            highlightAnimHolder.startAnim();
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (mTextView != null){
            mTextView.setText(text, type);
        }
        if (mFrontTextView != null){
            mFrontTextView.setText(text, type);
        }
    }

}
