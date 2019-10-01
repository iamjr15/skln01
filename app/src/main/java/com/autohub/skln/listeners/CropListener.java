package com.autohub.skln.listeners;

import android.net.Uri;

import java.io.File;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-07-31.
 */
public interface CropListener {
    void onCropped(File file, Uri originalUri);
}
