package com.autohub.skln;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.autohub.skln.student.StudentHomeActivity;
import com.autohub.skln.tutor.TutorHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    private static final String TAG = "LoginActivity";

    private EditText mPassword;

    private String mAccountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPassword = findViewById(R.id.edtPassword);

        mAccountType = getIntent().getStringExtra(KEY_ACCOUNT_TYPE);

        Button mBtnLogin = findViewById(R.id.btnLogin);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPassword.getText().toString().equals("")) {
                    showSnackError(R.string.enter_password);
                } else {
                    showLoading();

                    String db_root = getString(R.string.db_root_tutors);
                    if (mAccountType.equalsIgnoreCase(TYPE_STUDENT)) {
                        db_root = getString(R.string.db_root_students);
                    }

                    getFirebaseStore().collection(db_root).document(getFirebaseAuth().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot snapshot = task.getResult();
                                String savedPassword = snapshot.getString(KEY_PASSWORD);
                                validateUser(savedPassword);
                                hideLoading();
                            } else {
                                showNeedToRegister();
                            }
                        }
                    });

                }
            }

        });
    }

    private void validateUser(String savedPass) {
        String pass = mPassword.getText().toString();

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
            } else if (savedPass == null) {
                showNeedToRegister();
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
