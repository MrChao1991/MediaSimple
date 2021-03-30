package com.cfox.camera.sessionmanager.req;

import android.graphics.Rect;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.MeteringRectangle;

import com.cfox.camera.camera.info.CameraInfoManager;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.utils.EsParams;

public class SessionRequestManager {

    private final RequestCache REQUEST_CACHE = new RequestCache();

    private final CameraInfoManager mCameraHelper;
    private MeteringRectangle[] mFocusArea;
    private MeteringRectangle[] mMeteringArea;
    private int mFlashMode = EsParams.Value.FLASH_STATE.OFF;
    // for reset AE/AF metering area
    private final MeteringRectangle[] mResetRect = new MeteringRectangle[] {
            new MeteringRectangle(0, 0, 0, 0, 0)
    };

    public SessionRequestManager(CameraInfoManager cameraHelper) {
        this.mCameraHelper = cameraHelper;
    }

    public void resetApply() {
        REQUEST_CACHE.reset();
    }

    private <T> void apply(CaptureRequest.Builder builder, CaptureRequest.Key<T> key , T value) {
        REQUEST_CACHE.put(key, value);
        builder.set(key, value);
    }

    public void applyPreviewRequest(CaptureRequest.Builder builder) {
        int afMode = mCameraHelper.getValidAFMode(CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        int antiBMode = mCameraHelper.getValidAntiBandingMode(CaptureRequest.CONTROL_AE_ANTIBANDING_MODE_AUTO);
        apply(builder,CaptureRequest.CONTROL_AF_MODE, afMode);
        apply(builder,CaptureRequest.CONTROL_AE_ANTIBANDING_MODE, antiBMode);
        apply(builder,CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        apply(builder,CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_IDLE);
    }

    public void applyTouch2FocusRequest(CaptureRequest.Builder builder,
                                                MeteringRectangle focus, MeteringRectangle metering) {
        int afMode = mCameraHelper.getValidAFMode(CaptureRequest.CONTROL_AF_MODE_AUTO);
        builder.set(CaptureRequest.CONTROL_AF_MODE, afMode);
        builder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        if (mFocusArea == null) {
            mFocusArea = new MeteringRectangle[] {focus};
        } else {
            mFocusArea[0] = focus;
        }
        if (mMeteringArea == null) {
            mMeteringArea = new MeteringRectangle[] {metering};
        } else {
            mMeteringArea[0] = metering;
        }
        if (mCameraHelper.isMeteringSupport(true)) {
            builder.set(CaptureRequest.CONTROL_AF_REGIONS, mFocusArea);
        }
        if (mCameraHelper.isMeteringSupport(false)) {
            builder.set(CaptureRequest.CONTROL_AE_REGIONS, mMeteringArea);
        }
        builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_IDLE);
    }

    public void getFocusModeRequest(CaptureRequest.Builder builder, int focusMode) {
        int afMode = mCameraHelper.getValidAFMode(focusMode);
        builder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        builder.set(CaptureRequest.CONTROL_AF_MODE, afMode);
        builder.set(CaptureRequest.CONTROL_AF_REGIONS, mResetRect);
        builder.set(CaptureRequest.CONTROL_AE_REGIONS, mResetRect);
        // cancel af trigger
        builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_IDLE);
    }

    public void applyEvRange(CaptureRequest.Builder builder, Integer value) {
        if (value == null) {
            EsLog.w(" Ev value is null");
            return;
        }
        apply(builder, CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION , value);
    }

    public void applyZoomRect(CaptureRequest.Builder builder, Rect zoomRect) {
        if (zoomRect == null) {
            EsLog.w(" zoom Rect is null");
            return;
        }
        apply(builder, CaptureRequest.SCALER_CROP_REGION, zoomRect);
    }

    public void applyFlashRequest(CaptureRequest.Builder builder, Integer value) {
        if (!mCameraHelper.isFlashSupport() || value == null) {
            EsLog.w(" not support flash or value is null");
            return;
        }
        mFlashMode = value;
        switch (value) {
            case EsParams.Value.FLASH_STATE.ON:
                builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
                break;
            case EsParams.Value.FLASH_STATE.OFF:
                builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                break;
            case EsParams.Value.FLASH_STATE.AUTO:
                builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
                break;
            case EsParams.Value.FLASH_STATE.TORCH:
                builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                break;
            default:
                EsLog.e("error value for flash mode");
                break;
        }
        builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_IDLE);
    }

    public void applyAllRequest(CaptureRequest.Builder builder) {
        applyFlashRequest(builder, mFlashMode);
        for (CaptureRequest.Key key : REQUEST_CACHE.getKeySet()) {
            EsLog.d("apply all request key:" + key);
            builder.set(key, REQUEST_CACHE.get(key));
        }
    }

}
