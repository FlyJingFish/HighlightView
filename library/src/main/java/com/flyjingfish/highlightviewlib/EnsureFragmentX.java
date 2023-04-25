package com.flyjingfish.highlightviewlib;

import androidx.lifecycle.LifecycleOwner;

class EnsureFragmentX{
    LifecycleOwner lifecycleOwner;
    boolean isInFragmentX;

    public EnsureFragmentX(LifecycleOwner lifecycleOwner, boolean isInFragmentX) {
        this.lifecycleOwner = lifecycleOwner;
        this.isInFragmentX = isInFragmentX;
    }

    @Override
    public String toString() {
        return "EnsureFragmentX{" +
                "lifecycleOwner=" + lifecycleOwner +
                ", isInFragmentX=" + isInFragmentX +
                '}';
    }
}
