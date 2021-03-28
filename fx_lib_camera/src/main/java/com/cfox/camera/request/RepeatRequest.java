package com.cfox.camera.request;


import android.util.Pair;

public class RepeatRequest {
    private final Integer mEv;
    private final Float mZoomSize;
    private final Integer mFlashState;
    private final Pair<Float, Float> mAfTouchXY;
    public RepeatRequest(Builder builder) {
        mEv = builder.mEv;
        mZoomSize = builder.mZoomSize;
        mFlashState = builder.mFlashState;
        mAfTouchXY = builder.mAfTouchXY;
    }

    public Float getZoomSize() {
        return mZoomSize;
    }

    public Integer getEv() {
        return mEv;
    }

    public Integer getFlashState() {
        return mFlashState;
    }

    public Pair<Float, Float> getAfTouchXY() {
        return mAfTouchXY;
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Integer mFlashState;
        private Integer mEv;
        private Float mZoomSize;
        private Pair<Float, Float> mAfTouchXY;


        public Builder setFlash(int flashState){
            mFlashState = flashState;
            return this;
        }

        public Builder setEv(int value) {
            mEv = value;
            return this;
        }

        public Builder setZoom(float value) {
            mZoomSize = value;
            return this;
        }
        public Builder setAfTouchXY(float x, float y) {
            mAfTouchXY = new Pair<>(x, y);
            return this;
        }

        public RepeatRequest builder() {
            return new RepeatRequest(this);
        }
    }
}
