package com.cfox.camera.surface;

import android.util.Size;
import android.view.Surface;

import com.cfox.camera.utils.FxRequest;

import java.util.List;

import io.reactivex.Observable;

public interface ISurfaceHelper {
    Surface getSurface();
    Observable<FxRequest> isAvailable();
    List<Surface> getSurfaces();
    void addSurface(Surface surface);
    Class getSurfaceClass();
    void setAspectRatio(Size size);
}
