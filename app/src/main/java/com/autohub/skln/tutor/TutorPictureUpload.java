package com.autohub.skln.tutor;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.autohub.skln.BaseActivity;
import com.autohub.skln.utills.GalleryUtil;
import com.autohub.skln.utills.GlideApp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.autohub.skln.R;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.autohub.skln.utills.AppConstants.CATEGORY_ACADEMICS;
import static com.autohub.skln.utills.AppConstants.KEY_EXPERIENCE;
import static com.autohub.skln.utills.AppConstants.KEY_OCCUPATION;
import static com.autohub.skln.utills.AppConstants.KEY_PROFILE_PICTURE;

public class TutorPictureUpload extends BaseActivity {
    private static final String TAG = "TutorPictureUpload";

    private final int REQUEST_GALLERY_ACTIVITY = 200;
    private final int REQUEST_CROP = 400;

    @BindView(R.id.tvSelectOccupation)
    TextView tvSelectOccupation;

    @BindView(R.id.tvSelectExperience)
    TextView tvSelectExperience;

    @BindView(R.id.profile_picture)
    ImageView ivProfilePicture;

    @BindView(R.id.addProfilePicture)
    RelativeLayout addPics;

    private StorageReference mStorageRef;
    private PopupWindow popupWindowOccp;
    private PopupWindow popupWindowExp;

    private String[] occupations;
    private String[] experiences;
    private String mSelectedOccp = "";
    private String mSelectedExp = "";
    private String mProfileImageUrl = "";

    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_picture_upload);
        ButterKnife.bind(this);

        occupations = getResources().getStringArray(R.array.occupation_arrays);
        experiences = getResources().getStringArray(R.array.experience_arrays);

        tvSelectOccupation.setText(R.string.select_occupation);
        tvSelectExperience.setText(R.string.select_experience);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @OnClick(R.id.btnNext)
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

    @OnClick(R.id.addProfilePicture)
    public void onAddPicture() {
        String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        final int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    return;
                }
                ActivityCompat.requestPermissions(this, galleryPermissions, 0);
            } else {
                Intent gallery_Intent = new Intent(this, GalleryUtil.class);
                startActivityForResult(gallery_Intent, REQUEST_GALLERY_ACTIVITY);
            }
        }
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // If request is cancelled, the result arrays are empty.
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent gallery_Intent = new Intent(this, GalleryUtil.class);
                startActivityForResult(gallery_Intent, REQUEST_GALLERY_ACTIVITY);
            } else {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == REQUEST_GALLERY_ACTIVITY) {
            if(resultCode == RESULT_OK){
                String picturePath = data.getStringExtra("picturePath");
                performCrop(picturePath);
            }
        } else if (reqCode == REQUEST_CROP) {
            if(resultCode == RESULT_OK){
                if (data.getData() != null) {
                    Bitmap bmp;
                    bmp = BitmapFactory.decodeFile(data.getData().getPath());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos); // photo compress before uploading
                    byte[] bytes = baos.toByteArray();

                    //uploading the image
                    ivProfilePicture.requestLayout();
                    ivProfilePicture.getLayoutParams().height = 550;
                    ivProfilePicture.getLayoutParams().width = 550;

                    GlideApp.with(this).load(bmp).into(ivProfilePicture);

                    showLoading();
                    final StorageReference picRef = mStorageRef.child("tutor/" + getFirebaseAuth().getCurrentUser().getUid() + ".jpg");
                    UploadTask uploadTask = picRef.putBytes(bytes);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            hideLoading();
                            mProfileImageUrl = Objects.requireNonNull(taskSnapshot.getUploadSessionUri()).toString();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideLoading();
                            Toast.makeText(TutorPictureUpload.this, "Upload Failed -> " + e, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }
    }

    private void performCrop(String picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);
            cropIntent.setDataAndType(contentUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);
            cropIntent.putExtra("return-data", true);

            startActivityForResult(cropIntent, REQUEST_CROP);

        } catch (ActivityNotFoundException anfe) {
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @OnClick(R.id.tvSelectOccupation)
    public void onOccupationClick(View v) {
        Drawable img = this.getResources().getDrawable(R.drawable.chevron_with_circle_up);
        tvSelectOccupation.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
        PopupWindow popUp = popupOccupation(v);
        popUp.showAsDropDown(v, 0, 10);
    }

    @OnClick(R.id.tvSelectExperience)
    public void onExpClick(View v) {
        Drawable img = this.getResources().getDrawable(R.drawable.chevron_with_circle_up);
        tvSelectExperience.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
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
                tvSelectOccupation.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
            }
        });
        ListView listViewSort = new ListView(this);
        listViewSort.setDivider(null);
        listViewSort.setAdapter(adapter);
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drawable img = getDrawable(R.drawable.chevron_with_circle_down);
                tvSelectOccupation.setText(mSelectedOccp = occupations[position]);
                tvSelectOccupation.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
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
                tvSelectExperience.setText(mSelectedExp = experiences[position]);
                tvSelectExperience.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);

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
                tvSelectExperience.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);

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
