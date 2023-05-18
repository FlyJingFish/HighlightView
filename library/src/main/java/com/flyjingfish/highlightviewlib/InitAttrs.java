package com.flyjingfish.highlightviewlib;

import static com.flyjingfish.highlightviewlib.HighlightAnimHolder.FROM_LEFT;
import static com.flyjingfish.highlightviewlib.HighlightAnimHolder.RESTART;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.savedstate.SavedStateRegistryOwner;

class InitAttrs {
    static void init(@NonNull Context context, @Nullable AttributeSet attrs, @NonNull HighlightAnimHolder highlightAnimHolder) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HighlightAnimView);
        ColorStateList highlightColor = a.getColorStateList(R.styleable.HighlightAnimView_highlight_view_highlightColor);
        long duration = a.getInteger(R.styleable.HighlightAnimView_highlight_view_duration, 1000);
        int repeatCount = a.getInteger(R.styleable.HighlightAnimView_highlight_view_repeatCount, 0);
        int repeatMode = a.getInt(R.styleable.HighlightAnimView_highlight_view_repeatMode, RESTART);
        float highlightWidth = a.getDimension(R.styleable.HighlightAnimView_highlight_view_highlightWidth, 10);
        float highlightRotateDegrees = a.getFloat(R.styleable.HighlightAnimView_highlight_view_highlightRotateDegrees, 0);
        int startDirection = a.getColor(R.styleable.HighlightAnimView_highlight_view_startDirection, FROM_LEFT);
        boolean autoStart = a.getBoolean(R.styleable.HighlightAnimView_highlight_view_autoStart, false);
        a.recycle();
        if (highlightColor == null){
            highlightColor = ColorStateList.valueOf(Color.WHITE);
        }

        highlightAnimHolder.setDuration(duration);
        highlightAnimHolder.setRepeatCount(repeatCount);
        highlightAnimHolder.setRepeatMode(repeatMode);
        highlightAnimHolder.setHighlightWidth(highlightWidth);
        highlightAnimHolder.setHighlightRotateDegrees(highlightRotateDegrees);
        highlightAnimHolder.setStartDirection(startDirection);
        highlightAnimHolder.setHighlightColor(highlightColor);

        if (autoStart) {
            highlightAnimHolder.startHighlightEffect();
        }
    }

    static Paint initPaint() {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        return paint;
    }

    static EnsureFragmentX ensureInFragmentX(@Nullable View view) {
        if (view == null) {
            return new EnsureFragmentX(null,false);
        } else if (view.getParent() instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (isInFragment(viewGroup)) {
                Object object1 = viewGroup.getTag(R.id.view_tree_lifecycle_owner);
                return new EnsureFragmentX((LifecycleOwner) object1,true);
            } else {
                return ensureInFragmentX(viewGroup);
            }
        } else {
            return new EnsureFragmentX(null,false);
        }
    }

    private static boolean isInFragment(View view){
        Object object1 = view.getTag(R.id.view_tree_lifecycle_owner);
        Object object2 = view.getTag(R.id.view_tree_view_model_store_owner);
        Object object3 = view.getTag(R.id.view_tree_saved_state_registry_owner);
        return object1 instanceof LifecycleOwner && object2 instanceof ViewModelStoreOwner && object3 instanceof SavedStateRegistryOwner && !(object1 instanceof Activity);
    }
}
