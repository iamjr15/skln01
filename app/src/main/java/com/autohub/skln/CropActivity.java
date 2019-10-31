package com.autohub.skln;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.autohub.skln.listeners.CropListener;
import com.autohub.skln.utills.AppConstants;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-07-17.
 */
public class CropActivity extends BaseActivity {
    private CropImageView mCropImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle("Crop Image");
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        Uri mUri = getIntent().getParcelableExtra(AppConstants.KEY_URI);
        mCropImageView = findViewById(R.id.cropImageView);
        mCropImageView.setAspectRatio(1, 1);
        mCropImageView.setAutoZoomEnabled(true);
        mCropImageView.setImageUriAsync(mUri);
        mCropImageView.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
                returnResult(result);
            }
        });
    }

    private void returnResult(CropImageView.CropResult result) {
        if (result.getError() != null) {
            result.getError().printStackTrace();
            Log.e(">>>RegisterAcCropError", result.getError().getMessage());
            return;
        }
        if (result.getUri() == null) {
            Bitmap mImage = mCropImageView.getCropShape() == CropImageView.CropShape.OVAL
                    ? CropImage.toOvalBitmap(result.getBitmap())
                    : result.getBitmap();
            Log.e(">>>RegisterAcCropError", (mImage == null) + "");
            File file = new File(result.getOriginalUri().getPath());
            Log.d(">>>RegisterAcCropError", file.getParent());
            new FileFromBitmap(mImage, file, new CropListener() {

                @Override
                public void onCropped(File file, Uri originalUri) {
                    Intent intent = new Intent();
                    intent.putExtra("_cropped_uri_", Uri.fromFile(file));
                    intent.putExtra("_original_uri_", originalUri);
                    Log.d(">>>RegisterAcCropDone", file.getPath() + " , " + originalUri.getPath());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }).execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crop_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.crop) {
            mCropImageView.getCroppedImageAsync();
            return true;
        } else if (item.getItemId() == R.id.rotate_right) {
            mCropImageView.rotateImage(90);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class FileFromBitmap extends AsyncTask<Void, Void, File> {

        private Bitmap bitmap;
        private File mOriginalFile;
        private CropListener mCropListener;

        FileFromBitmap(Bitmap bitmap, File directory, CropListener cropListener) {
            this.bitmap = bitmap;
            this.mOriginalFile = directory;
            mCropListener = cropListener;
        }

        @Override
        protected File doInBackground(Void... params) {
            File file = new File(mOriginalFile.getParent(), "isporty_cropped.jpg");
            try {
                if (file.exists()) {
                    file.delete();
                }
                OutputStream os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
                return file;
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
            }
            return mOriginalFile;
        }


        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            Log.d(">>>RegisterAcCroped", file.getAbsolutePath());
            mCropListener.onCropped(file, Uri.fromFile(mOriginalFile));
        }
    }
}
