package com.cfox.camera.camera.session.impl;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.MeteringRectangle;

import com.cfox.camera.camera.session.IRequestBuilderManager;
import com.cfox.camera.camera.session.helper.ICameraHelper;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;

public class RequestBuilderManager implements IRequestBuilderManager {

    private ICameraHelper mCameraHelper;
    private MeteringRectangle[] mFocusArea;
    private MeteringRectangle[] mMeteringArea;
    // for reset AE/AF metering area
    private MeteringRectangle[] mResetRect = new MeteringRectangle[] {
            new MeteringRectangle(0, 0, 0, 0, 0)
    };

    public RequestBuilderManager(ICameraHelper cameraHelper) {
        this.mCameraHelper = cameraHelper;
    }


    public void getPreviewRequest(CaptureRequest.Builder builder) {
        int afMode = mCameraHelper.getValidAFMode(CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        int antiBMode = mCameraHelper.getValidAntiBandingMode(CaptureRequest.CONTROL_AE_ANTIBANDING_MODE_AUTO);
        builder.set(CaptureRequest.CONTROL_AF_MODE, afMode);
        builder.set(CaptureRequest.CONTROL_AE_ANTIBANDING_MODE, antiBMode);
        builder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_IDLE);
    }

    public void getTouch2FocusRequest(CaptureRequest.Builder builder,
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

    public void getStillPictureRequest(CaptureRequest.Builder builder, int rotation) {
        builder.set(CaptureRequest.JPEG_ORIENTATION, rotation);
    }

    public void getFocusDistanceRequest(CaptureRequest.Builder builder, float distance) {
        int afMode = mCameraHelper.getValidAFMode(CaptureRequest.CONTROL_AF_MODE_OFF);
        // preview
        builder.set(CaptureRequest.CONTROL_AF_MODE, afMode);
        float miniDistance = mCameraHelper.getMinimumDistance();
        if (miniDistance > 0) {
            builder.set(CaptureRequest.LENS_FOCUS_DISTANCE, miniDistance * distance);
        }
    }

    public void getFlashRequest(CaptureRequest.Builder builder, int value) {
        if (!mCameraHelper.isFlashSupport()) {
            EsLog.w(" not support flash");
            return ;
        }
        switch (value) {
            case Es.FLASH_TYPE.ON:
                builder.set(CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
                break;
            case Es.FLASH_TYPE.OFF:
                builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                break;
            case Es.FLASH_TYPE.AUTO:
                builder.set(CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
                break;
            case Es.FLASH_TYPE.TORCH:
                builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                break;
            default:
                EsLog.e("error value for flash mode");
                break;
        }
        builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_IDLE);
    }

    public void applyFlashRequest(CaptureRequest.Builder builder, int value) {
        if (!mCameraHelper.isFlashSupport()) {
            EsLog.w(" not support flash");
            return ;
        }
        switch (value) {
            case Es.FLASH_TYPE.ON:
                builder.set(CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
                break;
            case Es.FLASH_TYPE.OFF:
                builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                break;
            case Es.FLASH_TYPE.AUTO:
                builder.set(CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
                break;
            case Es.FLASH_TYPE.TORCH:
                builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                break;
            default:
                EsLog.e("error value for flash mode");
                break;
        }
    }

    private CaptureRequest.Builder getRequestBuilder(EsRequest request) {

        CaptureRequest.Builder builder = (CaptureRequest.Builder) request.getObj(Es.Key.REQUEST_BUILDER);
        if (builder == null) {

        }

        return builder;

    }



}