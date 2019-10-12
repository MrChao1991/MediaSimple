package com.cfox.camera.controller;

import android.content.Context;

import com.cfox.camera.model.CameraModule;
import com.cfox.camera.surface.ISurfaceHelper;

public class FxVideoController extends AbsController {
    public FxVideoController(Context context, ISurfaceHelper surfaceHelper) {
        super(context, surfaceHelper, CameraModule.ModuleFlag.MODULE_VIDEO);
    }
}
