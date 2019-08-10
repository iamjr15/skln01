package com.autohub.skln.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.autohub.skln.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-01.
 */
public class BaseFragment extends Fragment {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirebaseFirestore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
    }

    protected FirebaseAuth getFirebaseAuth() {
        return mFirebaseAuth;
    }

    protected FirebaseFirestore getFirebaseStore() {
        return mFirebaseFirestore;
    }

    protected final void showSnackError(String message) {
        if (requireActivity() instanceof BaseActivity) {
            ((BaseActivity) requireActivity()).showSnackError(message);
        }
    }

    protected final void showSnackError(@StringRes int message) {
        if (requireActivity() instanceof BaseActivity) {
            ((BaseActivity) requireActivity()).showSnackError(message);
        }
    }


    protected void showLoading() {
        if (requireActivity() instanceof BaseActivity) {
            ((BaseActivity) requireActivity()).showLoading();
        }
    }

    protected void hideLoading() {
        if (requireActivity() instanceof BaseActivity) {
            ((BaseActivity) requireActivity()).hideLoading();
        }
    }


    protected boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    protected String encrypt(String value) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return BaseActivity.encrypt(value);
    }

}
