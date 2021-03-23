package com.cfox.camera.helper.impl;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.view.Surface;

import com.cfox.camera.camera.info.CameraInfo;
import com.cfox.camera.camera.device.session.DeviceSession;
import com.cfox.camera.camera.device.session.DeviceSessionManager;
import com.cfox.camera.camera.info.CameraInfoManager;
import com.cfox.camera.helper.DulVideoSessionHelper;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class DulVideoSessionHelperImpl extends AbsCameraSessionHelper implements DulVideoSessionHelper {

    private final Map<String, CameraInfo> mCameraInfoMap = new HashMap<>(2);
    private final Map<String, CameraInfoManager> mCameraHelperMap = new HashMap<>(2);
    private final Map<String, CaptureRequest.Builder> mPreviewBuilderMap = new HashMap<>(2);
    private final Map<String, DeviceSession> mCameraSessionMap = new HashMap<>(2);
    private final Map<String, CameraCaptureSession.CaptureCallback> mCameraSessionCallbackMap = new HashMap<>(2);
    private final DeviceSessionManager mCameraSessionManager;

    public DulVideoSessionHelperImpl(DeviceSessionManager cameraSessionManager) {
        this.mCameraSessionManager = cameraSessionManager;

    }

    @Override
    public void init() {
        EsLog.d("init .....");
        mCameraSessionManager.closeSession().subscribe();
    }

    @Override
    public Observable<EsResult> cameraStatus() {
        return null;
    }

    @Override
    Observable<EsResult> beforeOpenCamera(EsRequest request) {
        EsLog.d("beforeOpenCamera....");
        return Observable.create(new ObservableOnSubscribe<EsResult>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<EsResult> emitter) throws Exception {
                emitter.onNext(new EsResult());
            }
        });
    }

    @Override
    Observable<EsRequest> applyPreviewPlan(EsRequest request) {
        return null;
    }

    @Override
    public DeviceSession getCameraSession(EsRequest request) {
        String cameraId = request.getString(Es.Key.CAMERA_ID);
        // TODO: 19-12-5 check camera id
        return getCameraSessionForId(cameraId);
    }

    @Override
    public Observable<EsResult> onRepeatingRequest(EsRequest request) {
        String cameraId = request.getString(Es.Key.CAMERA_ID);
        request.put(Es.Key.REQUEST_BUILDER, mPreviewBuilderMap.get(cameraId));
        return getCameraSessionForId(cameraId).onRepeatingRequest(request);
    }

    @Override
    public Observable<EsResult> close(EsRequest request) {
        EsLog.d("close: dul video close camera s");

        return mCameraSessionManager.closeSession().doOnNext(new Consumer<EsResult>() {
            @Override
            public void accept(EsResult esResult) throws Exception {
                mCameraInfoMap.clear();
                mCameraHelperMap.clear();
                mPreviewBuilderMap.clear();
                mCameraSessionMap.clear();
            }
        });
    }


    private DeviceSession getCameraSessionForId(String cameraId) {
        DeviceSession deviceSession;
        if (mCameraSessionMap.containsKey(cameraId)) {
            deviceSession = mCameraSessionMap.get(cameraId);
        } else {
            deviceSession = mCameraSessionManager.createSession();
            mCameraSessionMap.put(cameraId, deviceSession);
        }
        return deviceSession;
    }

//    private CameraInfoManager getCameraHelperForId(String cameraId) {
//        CameraInfoManager cameraHelper;
//        if (mCameraHelperMap.containsKey(cameraId)) {
//            cameraHelper = mCameraHelperMap.get(cameraId);
//        } else {
//            cameraHelper = new DulVideoCameraHelper();
//            cameraHelper.initCameraInfo(getCameraInfoForId(cameraId));
//            mCameraHelperMap.put(cameraId, cameraHelper);
//        }
//
//        return cameraHelper;
//    }

//    private CameraInfo getCameraInfoForId(String cameraId) {
//        CameraInfo cameraInfo;
//        if (mCameraInfoMap.containsKey(cameraId)) {
//            cameraInfo = mCameraInfoMap.get(cameraId);
//        } else {
//            cameraInfo = getCameraInfo(cameraId);
//            mCameraInfoMap.put(cameraId, cameraInfo);
//        }
//
//        return cameraInfo;
//    }
}
