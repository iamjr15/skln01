package com.autohub.skln;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.autohub.skln.databinding.ActivityLoginBinding;
import com.autohub.skln.student.StudentHomeActivity;
import com.autohub.skln.tutor.TutorHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.autohub.skln.utills.AppConstants.KEY_ACCOUNT_TYPE;
import static com.autohub.skln.utills.AppConstants.KEY_PASSWORD;
import static com.autohub.skln.utills.AppConstants.TYPE_STUDENT;
import static com.autohub.skln.utills.AppConstants.TYPE_TUTOR;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding mBinding;
    private String mAccountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mBinding.setCallback(this);
        mAccountType = getIntent().getStringExtra(KEY_ACCOUNT_TYPE);
    }

    public void login() {
        String text = getString(mBinding.edtPassword.getText());
        if (TextUtils.isEmpty(text)) {
            showSnackError(R.string.enter_password);
            return;
        }
        showLoading();
        String db_root = getString(R.string.db_root_tutors);
        if (TYPE_STUDENT.equalsIgnoreCase(mAccountType)) {
            db_root = getString(R.string.db_root_students);
        }
        FirebaseUser currentUser = getFirebaseAuth().getCurrentUser();
        if (currentUser == null) {
            return;
        }
        getFirebaseStore().collection(db_root).document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot == null) {
                        showNeedToRegister();
                        return;
                    }
                    String savedPassword = snapshot.getString(KEY_PASSWORD);
                    validateUser(savedPassword);
                    hideLoading();
                } else {
                    showNeedToRegister();
                }
            }
        });
    }

    private void validateUser(String savedPass) {
        String pass = getString(mBinding.edtPassword.getText());

        try {
            if (encrypt(pass).equals(savedPass)) {
                Intent intent;
                if (mAccountType.equalsIgnoreCase(TYPE_TUTOR)) {
                    Toast.makeText(this, "Tutor Verified!", Toast.LENGTH_SHORT).show();
                    intent = new Intent(this, TutorHomeActivity.class);
                } else {
                    Toast.makeText(this, "Student Verified!", Toast.LENGTH_SHORT).show();
                    intent = new Intent(this, StudentHomeActivity.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finishAffinity();
            } else if (savedPass == null) {
                showNeedToRegister();
                finishAffinity();
            } else {
                Toast.makeText(this, "Password not matched!", Toast.LENGTH_SHORT).show();
            }
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    private void showNeedToRegister() {
        Snackbar.make((findViewById(android.R.id.content)), R.string.not_registered, Snackbar.LENGTH_LONG)
                .setAction("Sign Up", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(LoginActivity.this, TutorOrStudent.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
