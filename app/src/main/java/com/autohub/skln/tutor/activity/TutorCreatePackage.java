package com.autohub.skln.tutor.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.autohub.skln.BaseActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.autohub.skln.R;
import com.autohub.skln.utills.AppConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TutorCreatePackage extends BaseActivity {
    private static final String TAG = "TutorCreatePackage";

    private String chargesType = "";
    private String chargeDoubt = "";
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> arrayList1 = new ArrayList<>();
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_create_package);
        ButterKnife.bind(this);

        mStorage = FirebaseStorage.getInstance().getReference();
    }

    @OnClick(R.id.tvClassType)
    public void onClassTypeClick() {
        /*if(tvMonthly.getCurrentTextColor() == Color.parseColor("#ffffff")){
            arrayList.remove(tvMonthly.getText().toString());
            Log.d(TAG, "onMonthlyClick: "+ arrayList);
            tvMonthly.setBackgroundResource(R.drawable.reactangle_cert);
            tvMonthly.setTextColor(Color.parseColor("#ff707070"));
        }else {
            arrayList.add(tvMonthly.getText().toString());
            Log.d(TAG, "onMonthlyClick: "+ arrayList);
            tvMonthly.setBackgroundResource(R.drawable.reactangle_cert_black);
            tvMonthly.setTextColor(Color.parseColor("#ffffff"));
        }*/
    }

    @OnClick(R.id.tvClassNumber)
    public void onClassNumberClick(){
        /*if(rlBundleInfo.getVisibility() == View.VISIBLE){
            rlBundleInfo.setVisibility(View.GONE);
        }else {
            rlBundleInfo.setVisibility(View.VISIBLE);
        }*/
    }

    @OnClick(R.id.tvClassFreq)
    public void onClassFreqClick() {
//        chargesType = tvWeekly.getText().toString();
        /*if(tvWeekly.getCurrentTextColor() == Color.parseColor("#ffffff")){
            arrayList.remove(tvWeekly.getText().toString());
            Log.d(TAG, "tvWeekly: "+ arrayList);
            tvWeekly.setBackgroundResource(R.drawable.reactangle_cert);
            tvWeekly.setTextColor(Color.parseColor("#ff707070"));
        }else {
            arrayList.add(tvWeekly.getText().toString());
            Log.d(TAG, "tvWeekly: "+ arrayList);
            tvWeekly.setBackgroundResource(R.drawable.reactangle_cert_black);
            tvWeekly.setTextColor(Color.parseColor("#ffffff"));
        }*/

    }

    @OnClick(R.id.btnNext)
    public void onNextClick() {
        for (int i = 0; i < arrayList1.size(); i++) {
            chargeDoubt = chargeDoubt + arrayList1.get(i);
            Log.d(TAG, "onNextClick: " + chargeDoubt );
        }

        for (int i = 0; i < arrayList.size(); i++) {
            chargesType = chargesType + arrayList.get(i);
            Log.d(TAG, "onNextClick: " + chargesType );
        }

        if (chargesType.equals("")) {
            Snackbar snackbar;
            snackbar = Snackbar.make((findViewById(android.R.id.content)), getString(R.string.select_chargestype), Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            view.setBackgroundColor(Color.parseColor("#ba0505"));
            textView.setTextColor(Color.WHITE);
            snackbar.show();
            return;
        }
        if (chargeDoubt.equals("")) {
            Snackbar snackbar;
            snackbar = Snackbar.make((findViewById(android.R.id.content)), getString(R.string.select_chargesdoubt), Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            view.setBackgroundColor(Color.parseColor("#ba0505"));
            textView.setTextColor(Color.WHITE);
            snackbar.show();
            return;
        }
        Intent intent = new Intent(this, TutorHomeActivity.class);
        startActivity(intent);
        sendData();
    }

    // Sending data to Firebase FireStore
    private void sendData() {

        mStorage = mStorage.child("users/profiles/proof_document/" + getFirebaseAuth().getCurrentUser().getUid() + "Original");
        UploadTask uploadTask =  mStorage.putFile(Uri.parse(getAppPreferenceHelper().getProofImage()));
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Snackbar snackbar;
                    snackbar = Snackbar.make((findViewById(android.R.id.content)), "Error in image loading", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    view.setBackgroundColor(Color.parseColor("#ba0505"));
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return mStorage.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.d(TAG, "onComplete: "+ downloadUri.toString());
                    mStorage = FirebaseStorage.getInstance().getReference();
                } else {
                    // Handle failures
                    // ...
                }
            }

        });



        showLoading();
        Map<String, Object> user = new HashMap<>();
        user.put(AppConstants.KEY_FIRST_NAME, getAppPreferenceHelper().getUserFirstName());
        user.put(AppConstants.KEY_LAST_NAME, getAppPreferenceHelper().getUserLastName());
        user.put(AppConstants.PROFILE_IMAGE, getAppPreferenceHelper().getProfileImage());
        user.put(AppConstants.CERTIFICATE_IMAGE, getAppPreferenceHelper().getCertificatesImage());
        user.put(AppConstants.PROOF_DOCUMENT_IMAGE, getAppPreferenceHelper().getProofImage());
        user.put(AppConstants.KEY_PHONE_NUMBER, getAppPreferenceHelper().getUserPhone());
        user.put(AppConstants.KEY_PASSWORD, getAppPreferenceHelper().getUserPassword());
        user.put(AppConstants.KEY_CATEGORY, getAppPreferenceHelper().getTutorCategory());
        user.put(AppConstants.KEY_CLASSES, getAppPreferenceHelper().getClasses());
        user.put(AppConstants.KEY_SUBJECTS, getAppPreferenceHelper().getSubjects());
        user.put(AppConstants.KEY_OCCUPATION, getAppPreferenceHelper().getOccupation());
        user.put(AppConstants.KEY_EXPERIENCE, getAppPreferenceHelper().getExperince());
        user.put(AppConstants.KEY_QUALIFICATION, getAppPreferenceHelper().getQualification());
        user.put(AppConstants.KEY_AREA_QUALIFICATION, getAppPreferenceHelper().getArea());
//        user.put(AppConstants.KEY_UNIVERSITY, getAppPreferenceHelper().getUniversity());
//        user.put(AppConstants.KEY_SPECIALISATION, getAppPreferenceHelper().getSpecialisation());
//        user.put(AppConstants.KEY_QUALIFICATION_YEAR, getAppPreferenceHelper().getYear());
        user.put(AppConstants.KEY_BOARD, getAppPreferenceHelper().getboard());
//        user.put(AppConstants.KEY_ACADEMIC_EXPERINCE, getAppPreferenceHelper().getexperinceLevel());
//        user.put(AppConstants.KEY_HOURS_AVAILABLE, getAppPreferenceHelper().getworkingHour());
        user.put(AppConstants.KEY_BIODATA, getAppPreferenceHelper().getOverview());
        user.put(AppConstants.KEY_CLASSES_PLACE, getAppPreferenceHelper().getClassesPlace());
        user.put(AppConstants.KEY_CHARGES_TYPE, chargesType);
        user.put(AppConstants.KEY_CHARGES_DOUBT, chargeDoubt);

        getFirebaseStore().collection("users").document(getAppPreferenceHelper().getUserId()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        Toast.makeText(TutorCreatePackage.this, R.string.sign_up_message, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(TutorCreatePackage.this, TutorOrStudent.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideLoading();
                        Toast.makeText(TutorCreatePackage.this, R.string.error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
