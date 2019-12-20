package com.cfox.camera.camera.session.helper;

import android.util.Range;
import android.util.Size;

import com.cfox.camera.camera.ICameraInfo;

public interface ICameraHelper {

    void initCameraInfo(ICameraInfo cameraInfo);
    //    IBuilderPack getBuilderPack();
    int createPreviewTemplate();
    boolean isAutoFocusSupported();
    boolean isRawSupported();
    boolean isLegacyLocked();
    int getSensorOrientation();
    int getValidAFMode(int targetMode);
    int getValidAntiBandingMode(int targetMode);
    boolean isMeteringSupport(boolean focusArea);
    float getMinimumDistance();
    boolean isFlashSupport();
    boolean canTriggerAf();
    Size[] getPictureSize(int format);
    Size[] getPreviewSize(Class klass);
    Range<Integer> getEvRange();

    IBuilderHelper getBuilderHelper();
}