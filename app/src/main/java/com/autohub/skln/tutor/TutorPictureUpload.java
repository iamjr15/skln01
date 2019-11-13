package com.autohub.skln.tutor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.autohub.skln.BaseActivity;
import com.autohub.skln.CropActivity;
import com.autohub.skln.R;
import com.autohub.skln.databinding.ActivityTutorPictureUploadBinding;
import com.autohub.skln.utills.AppConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.autohub.skln.utills.AppConstants.CATEGORY_ACADEMICS;
import static com.autohub.skln.utills.AppConstants.KEY_EXPERIENCE;
import static com.autohub.skln.utills.AppConstants.KEY_OCCUPATION;
import static com.autohub.skln.utills.AppConstants.KEY_PROFILE_PICTURE;

public class TutorPictureUpload extends BaseActivity {
    private static final String TAG = "TutorPictureUpload";

    //    private final int REQUEST_GALLERY_ACTIVITY = 200;
    private final int REQUEST_CROP = 400;

    private StorageReference mStorageRef;
    private PopupWindow popupWindowOccp;
    private PopupWindow popupWindowExp;

    private String[] occupations;
    private String[] experiences;
    private String mSelectedOccp = "";
    private String mSelectedExp = "";
    private String mProfileImageUrl = "";

    private int width;

    private ActivityTutorPictureUploadBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_picture_upload);
        mBinding.setCallback(this);

        occupations = getResources().getStringArray(R.array.occupation_arrays);
        experiences = getResources().getStringArray(R.array.experience_arrays);

        mBinding.tvSelectOccupation.setText(R.string.select_occupation);
        mBinding.tvSelectExperience.setText(R.string.select_experience);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void onNextClick() {
        if (TextUtils.isEmpty(mProfileImageUrl)) {
            Toast.makeText(this, R.string.profile_image, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mSelectedOccp)) {
            showSnackError(R.string.warning_choose_occp);
            return;
        }

        if (TextUtils.isEmpty(mSelectedExp)) {
            showSnackError(R.string.warning_choose_exp);
            return;
        }

        showLoading();
        Map<String, Object> user = new HashMap<>();
        user.put(KEY_PROFILE_PICTURE, mProfileImageUrl);
        user.put(KEY_OCCUPATION, mSelectedOccp);
        user.put(KEY_EXPERIENCE, mSelectedExp);

        getFirebaseStore().collection(getString(R.string.db_root_tutors)).document(getFirebaseAuth().getCurrentUser().getUid()).set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        if (getAppPreferenceHelper().getTutorCategory().equals(CATEGORY_ACADEMICS))
                            startActivity(new Intent(TutorPictureUpload.this, TutorTargetBoardSelect.class));
                        else
                            startActivity(new Intent(TutorPictureUpload.this, TutorBiodata.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideLoading();
                        showSnackError(e.getMessage());
                    }
                });
    }

    public void onAddPicture() {
        String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!checkIfAlreadyhavePermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return;
            }
            ActivityCompat.requestPermissions(this, galleryPermissions, 0);
        } else {
            PickSetup setup = new PickSetup();
            setup.setCancelText("Close");
            PickImageDialog.build(setup, new IPickResult() {
                @Override
                public void onPickResult(PickResult pickResult) {
                    if (pickResult.getError() == null) {
                        Uri uri = pickResult.getUri();
                        mBinding.profilePicture.setImageURI(uri);
                        mBinding.profilePicture.setTag(pickResult.getPath());

                        File file = new File(pickResult.getPath());
                        Intent intent = new Intent(TutorPictureUpload.this, CropActivity.class);
                        intent.putExtra(AppConstants.KEY_URI, Uri.fromFile(file));
                        startActivityForResult(intent, 1122);
                    }
                }
            }).show(this);
        }
    }

    public boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // TODO: 2019-07-31 handle permissions properly
//        if (requestCode == 0) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Intent gallery_Intent = new Intent(this, GalleryUtil.class);
//                startActivityForResult(gallery_Intent, REQUEST_GALLERY_ACTIVITY);
//            } else {
//                android.os.Process.killProcess(android.os.Process.myPid());
//            }
//        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == 1122 && resultCode == RESULT_OK && data != null) {
            Uri croppedUri = data.getParcelableExtra("_cropped_uri_");
            Uri originalUri = data.getParcelableExtra("_original_uri_");
            Log.d(">>>RegisterAcRes", croppedUri.toString() + " , " + originalUri.toString());
            mBinding.profilePicture.setImageURI(croppedUri);
            mBinding.profilePicture.setTag(croppedUri.getPath());
            uploadImage(croppedUri);
//            ivProfilePicture.setTag(R.id.uri, croppedUri);
        }
//        else if (reqCode == REQUEST_GALLERY_ACTIVITY) {
//            if (resultCode == RESULT_OK) {
//                String picturePath = data.getStringExtra("picturePath");
//                performCrop(picturePath);
//            }
//        } else if (reqCode == REQUEST_CROP) {
//            if (resultCode == RESULT_OK) {
//                if (data.getData() != null) {
//                    Bitmap bmp;
//                    bmp = BitmapFactory.decodeFile(data.getData().getPath());
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos); // photo compress before uploading
//                    byte[] bytes = baos.toByteArray();
//                    GlideApp.with(this).load(bmp).into(mBinding.profilePicture);
//
//                    showLoading();
//                    final StorageReference picRef = mStorageRef.child("tutor/" + getFirebaseAuth().getCurrentUser().getUid() + ".jpg");
//                    UploadTask uploadTask = picRef.putBytes(bytes);
//                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            hideLoading();
//                            mProfileImageUrl = Objects.requireNonNull(taskSnapshot.getUploadSessionUri()).toString();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            hideLoading();
//                            Toast.makeText(TutorPictureUpload.this, "Upload Failed -> " + e, Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//            }
//        }
    }


//    private void performCrop(String picUri) {
//        try {
//            Intent cropIntent = new Intent("com.android.camera.action.CROP");
//            File f = new File(picUri);
//            Uri contentUri = Uri.fromFile(f);
//            cropIntent.setDataAndType(contentUri, "image/*");
//            cropIntent.putExtra("crop", "true");
//            cropIntent.putExtra("aspectX", 1);
//            cropIntent.putExtra("aspectY", 1);
//            cropIntent.putExtra("outputX", 280);
//            cropIntent.putExtra("outputY", 280);
//            cropIntent.putExtra("return-data", true);
//
//            startActivityForResult(cropIntent, REQUEST_CROP);
//
//        } catch (ActivityNotFoundException anfe) {
//            String errorMessage = "your device doesn't support the crop action!";
//            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }

    private void uploadImage(Uri uri) {
        showLoading();
        File file = new File(uri.getPath());
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
            final String pathString = "tutor/" + getFirebaseAuth().getCurrentUser().getUid() + ".jpg";
            final StorageReference picRef = mStorageRef.child(pathString);
            UploadTask uploadTask = picRef.putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    hideLoading();
                    mProfileImageUrl = pathString;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideLoading();
                    Toast.makeText(TutorPictureUpload.this, "Upload Failed -> " + e, Toast.LENGTH_LONG).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            hideLoading();
        } catch (IOException e) {
            e.printStackTrace();
            hideLoading();
        }
    }

    public void onOccupationClick(View v) {
        Drawable img = this.getResources().getDrawable(R.drawable.chevron_with_circle_up);
        mBinding.tvSelectOccupation.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
        PopupWindow popUp = popupOccupation(v);
        popUp.showAsDropDown(v, 0, 10);
    }

    public void onExpClick(View v) {
        Drawable img = this.getResources().getDrawable(R.drawable.chevron_with_circle_up);
        mBinding.tvSelectExperience.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
        PopupWindow popUp = popupWindowExp();
        popUp.showAsDropDown(v, 0, 10);
    }

    private PopupWindow popupOccupation(View view) {
        popupWindowOccp = new PopupWindow(this);
        popupWindowOccp.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.reactangle_cert));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_items,
                getResources().getStringArray(R.array.occupation_arrays));
        popupWindowOccp.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDismiss() {
                Drawable img = getDrawable(R.drawable.chevron_with_circle_down);
                mBinding.tvSelectOccupation.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
            }
        });
        ListView listViewSort = new ListView(this);
        listViewSort.setDivider(null);
        listViewSort.setAdapter(adapter);
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drawable img = getDrawable(R.drawable.chevron_with_circle_down);
                mBinding.tvSelectOccupation.setText(mSelectedOccp = occupations[position]);
                mBinding.tvSelectOccupation.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                if (popupWindowOccp != null) {
                    popupWindowOccp.dismiss();
                }
            }
        });
        setPopWidth(popupWindowOccp);
        popupWindowOccp.setFocusable(true);
        popupWindowOccp.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindowOccp.setContentView(listViewSort);

        return popupWindowOccp;
    }

    private PopupWindow popupWindowExp() {
        popupWindowExp = new PopupWindow(this);
        popupWindowExp.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.reactangle_cert));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_items,
                getResources().getStringArray(R.array.experience_arrays));
        ListView listViewSort = new ListView(this);
        listViewSort.setDivider(null);
        listViewSort.setAdapter(adapter);
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drawable img = getDrawable(R.drawable.chevron_with_circle_down);
                mBinding.tvSelectExperience.setText(mSelectedExp = experiences[position]);
                mBinding.tvSelectExperience.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);

                if (popupWindowExp != null) {
                    popupWindowExp.dismiss();
                }
            }
        });
        setPopWidth(popupWindowExp);
        popupWindowExp.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDismiss() {
                Drawable img = getDrawable(R.drawable.chevron_with_circle_down);
                mBinding.tvSelectExperience.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);

            }
        });
        popupWindowExp.setFocusable(true);
        popupWindowExp.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindowExp.setContentView(listViewSort);

        return popupWindowExp;
    }

    // Set Pop up width according to resolution
    private void setPopWidth(PopupWindow popupWindow) {
        if (width == 1440) {
            popupWindow.setWidth(width - 200);
        } else if (width == 1080) {
            popupWindow.setWidth(width - 150);
        } else if (width == 720) {
            popupWindow.setWidth(width - 100);
        } else {
            popupWindow.setWidth(width - 100);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
