package com.autohub.skln.tutor.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.autohub.skln.R;
import com.google.firebase.storage.UploadTask;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.autohub.skln.utills.AppConstants.CATEGORY_ACADEMICS;
import static com.autohub.skln.utills.AppConstants.KEY_EXPERIENCE;
import static com.autohub.skln.utills.AppConstants.KEY_OCCUPATION;
import static com.autohub.skln.utills.AppConstants.KEY_PROFILE_PICTURE;

/*This Activity is not in use*/
public class TutorPictureUpload extends BaseActivity {
    private static final String TAG = "TutorPictureUpload";

    @BindView(R.id.tvSelectOccupation)
    TextView tvSelectOccupation;

    @BindView(R.id.tvSelectExperience)
    TextView tvSelectExperience;

    @BindView(R.id.Picture)
    ImageView imageView;

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
        if(TextUtils.isEmpty(mProfileImageUrl)){
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

        getFirebaseStore().collection(getString(R.string.db_root_users)).document(getFirebaseAuth().getCurrentUser().getUid()).set(user, SetOptions.merge())
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
        getImageFromAlbum(0);
    }

    private void getImageFromAlbum(int i) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, i);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (data == null) {
            return;
        }

        String des = "Croped" + ".jpg";

        final Uri imageUri = data.getData();
        setImages(imageUri);
        /*UCrop.of(imageUri, Uri.fromFile(new File(getCacheDir(), des)))
                .withAspectRatio(4, 3)
                .withMaxResultSize(450 , 450)
                .start(this);

*/
        /*if (resultCode == RESULT_OK && reqCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
//            setImages(f, resultUri);


        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
*/

    }

    void setImages(Uri resultUri) {
        Bitmap newImage = null;
        try {
            newImage = getResizedBitmap(getBitmapFromUri(resultUri), 64, 64);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.requestLayout();
        imageView.getLayoutParams().height = 550;
        imageView.getLayoutParams().width = 550;
        imageView.setImageBitmap(newImage);
//      Picasso.get().load(resultUri).fit().centerCrop().into(imageView);
//      addPics.setVisibility(View.GONE);
//      try {
        //threadForProfile.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        final StorageReference picRef = mStorageRef.child("tutor/" +
                getFirebaseAuth().getCurrentUser().getUid() + ".jpg");

        /*UploadTask uploadTask =  picRef.putFile(resultUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return picRef.getDownloadUrl();
            }
        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                mProfileImageUrl = uri.toString();
                Toast.makeText(TutorPictureUpload.this, "Uploading Successful.", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TutorPictureUpload.this, "Uploading Failed.", Toast.LENGTH_LONG).show();
            }
        });*/

        showLoading();
        picRef.putFile(resultUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        hideLoading();
                        Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                        mProfileImageUrl = downloadUrl.toString();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideLoading();
                        Toast.makeText(TutorPictureUpload.this, "Uploading Failed.", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                });
    }

    public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    /*public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }*/

    @OnClick(R.id.tvSelectOccupation)
    public void onOccupationClick(View v) {
        Drawable img = this.getResources().getDrawable(R.drawable.chevron_with_circle_up);
        img.setBounds(0, 0, 120, 120);
        tvSelectOccupation.setCompoundDrawables(null, null, img, null);
        PopupWindow popUp = popupOccupation(v);
        popUp.showAsDropDown(v, 0, 10);
    }

    @OnClick(R.id.tvSelectExperience)
    public void onExpClick(View v) {
        Drawable img = this.getResources().getDrawable(R.drawable.chevron_with_circle_up);
        img.setBounds(0, 0, 120, 120);
        tvSelectExperience.setCompoundDrawables(null, null, img, null);
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
                img.setBounds(0, 0, 120, 120);
                tvSelectOccupation.setCompoundDrawables(null, null, img, null);

            }
        });
        ListView listViewSort = new ListView(this);
        listViewSort.setDivider(null);
        listViewSort.setAdapter(adapter);
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drawable img = getDrawable(R.drawable.chevron_with_circle_down);
                img.setBounds(0, 0, 120, 120);
                tvSelectOccupation.setText(mSelectedOccp = occupations[position]);
                tvSelectOccupation.setCompoundDrawables(null, null, img, null);

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
                img.setBounds(0, 0, 120, 120);
                tvSelectExperience.setText(mSelectedExp = experiences[position]);
                tvSelectExperience.setCompoundDrawables(null, null, img, null);

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
                img.setBounds(0, 0, 120, 120);
                tvSelectExperience.setCompoundDrawables(null, null, img, null);

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
