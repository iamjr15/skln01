package com.autohub.skln.student;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.autohub.skln.R;
import com.autohub.skln.databinding.StudentEditProfileBinding;
import com.autohub.skln.fragment.BaseFragment;
import com.autohub.skln.models.User;
import com.autohub.skln.utills.AppConstants;
import com.autohub.skln.utills.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static com.autohub.skln.utills.AppConstants.KEY_FIRST_NAME;
import static com.autohub.skln.utills.AppConstants.KEY_LAST_NAME;
import static com.autohub.skln.utills.AppConstants.KEY_PASSWORD;
import static com.autohub.skln.utills.AppConstants.KEY_PHONE_NUMBER;
import static com.autohub.skln.utills.AppConstants.KEY_STDT_HOBBIES;

public class EditProfileFragment extends BaseFragment {
    private StudentEditProfileBinding mBinding;

    private User user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.student_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = StudentEditProfileBinding.bind(view);
        mBinding.showHidePassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBinding.showHidePassword.getText().toString().equalsIgnoreCase("show")) {
                    mBinding.password.setTransformationMethod(null);
                    mBinding.showHidePassword.setText("hide");
                } else {
                    mBinding.password.setTransformationMethod(new PasswordTransformationMethod());
                    mBinding.showHidePassword.setText("show");
                }
            }
        });
        setUpUserInfo();
        mBinding.updateProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                makeSaveRequest();
            }
        });
        mBinding.favoriteSubjCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showSub(false);
            }
        });
        mBinding.leastFavuSubjCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showSub(true);
            }
        });
        mBinding.favHobbyCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showHobby();
            }
        });
        mBinding.grade.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showGrade();
            }
        });
    }

    public void showHobby() {
        final List<String> items = new ArrayList<>();
        items.add(AppConstants.HOBBY_DANCE);
        items.add(AppConstants.HOBBY_DRUM);
        items.add(AppConstants.HOBBY_GUITAR);
        items.add(AppConstants.HOBBY_KEYBOARD);
        items.add(AppConstants.HOBBY_MARTIAL);
        items.add(AppConstants.HOBBY_PAINT);
        String[] namesArr = items.toArray(new String[items.size()]);
        boolean[] booleans = new boolean[items.size()];
        List<String> selectedItems = new ArrayList<>();
        if (user.hobbiesToPursue != null && !user.hobbiesToPursue.isEmpty()) {
            selectedItems = Arrays.asList(user.hobbiesToPursue.split("\\s*,\\s*"));
        }
        final List<String> selectedHobbies = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            if (items.contains(selectedItems.get(i))) {
                booleans[items.indexOf(selectedItems.get(i))] = true;
                selectedHobbies.add(selectedItems.get(i));
            }
        }


        new AlertDialog.Builder(requireActivity())
                .setTitle("Choose your favourite hobby")
                .setMultiChoiceItems(namesArr, booleans, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            selectedHobbies.add(items.get(i));
                        } else {
                            selectedHobbies.remove(items.get(i));
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        String selectedHobbyString = "";
                        for (int i = 0; i < selectedHobbies.size(); i++) {
                            if (i == selectedHobbies.size() - 1) {
                                selectedHobbyString += selectedHobbies.get(i);
                            } else {
                                selectedHobbyString += selectedHobbies.get(i) + ",";
                            }
                        }
                        mBinding.favHobby.setText(selectedHobbyString);
                        user.hobbiesToPursue = selectedHobbyString;
                        // Do something useful withe the position of the selected radio button
                    }
                })
                .show();
    }

    public void showGrade() {
        final String[] namesArr = new String[12];
        int indexSelected = -1;
        for (int i = 0; i < 12; i++) {
            namesArr[i] = (i + 1) + CommonUtils.getClassSuffix((i + 1)) + " Grade";
            if (user.studentClass.equalsIgnoreCase(String.valueOf(i + 1))) {
                indexSelected = i;
            }
        }

        new AlertDialog.Builder(requireActivity())
                .setSingleChoiceItems(namesArr, indexSelected, null)
                .setTitle("Select Current Grade")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        if (selectedPosition < 0) {
                            selectedPosition = 0;
                        }
                        mBinding.grade.setText(namesArr[selectedPosition]);
                        user.studentClass = String.valueOf(selectedPosition + 1);
                    }
                })
                .show();
    }

    public void showSub(final boolean isLeastFav) {
        final List<String> items = new ArrayList<>();
        items.add(AppConstants.SUBJECT_SCIENCE);
        items.add(AppConstants.SUBJECT_COMPUTER_SCIENCE);
        items.add(AppConstants.SUBJECT_ACCOUNTANCY);
        items.add(AppConstants.SUBJECT_BIOLOGY);
        items.add(AppConstants.SUBJECT_BUSINESS);
        items.add(AppConstants.SUBJECT_SOCIAL_STUDIES);
        items.add(AppConstants.SUBJECT_CHEMISTRY);
        items.add(AppConstants.SUBJECT_ECONOMICS);
        items.add(AppConstants.SUBJECT_LANGUAGES);
        items.add(AppConstants.SUBJECT_PHYSICS);
        items.add(AppConstants.SUBJECT_MATHS);
        items.add(AppConstants.SUBJECT_ENGLISH);
        String[] namesArr = items.toArray(new String[items.size()]);
        boolean[] booleans = new boolean[items.size()];
        List<String> selectedItems = new ArrayList<>();
        if (user.favoriteClasses != null && !user.leastFavoriteClasses.isEmpty() && !isLeastFav) {
//            if (!isLeastFav) {
            selectedItems = Arrays.asList(user.favoriteClasses.split("\\s*,\\s*"));
//            } else {

//            }
        }
        if (user.leastFavoriteClasses != null && !user.leastFavoriteClasses.isEmpty() && isLeastFav) {
//            if (!isLeastFav) {
            selectedItems = Arrays.asList(user.leastFavoriteClasses.split("\\s*,\\s*"));
//            } else {

//            }
        }


        final List<String> selectedSub = new ArrayList<>();

        for (int i = 0; i < selectedItems.size(); i++) {
            if (items.contains(selectedItems.get(i))) {
                booleans[items.indexOf(selectedItems.get(i))] = true;
                selectedSub.add(selectedItems.get(i));
            }
        }


        String title = "";
        if (!isLeastFav) {
            title = "Choose Favourite Subject";
        } else {
            title = "Choose Least Favourite Subject";
        }
        new AlertDialog.Builder(requireActivity())
                .setMultiChoiceItems(namesArr, booleans,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                if (b) {
                                    selectedSub.add(items.get(i));
                                } else {
                                    selectedSub.remove(items.get(i));
                                }
                            }
                        })
                .setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        String selectedSubString = "";
                        for (int i = 0; i < selectedSub.size(); i++) {
                            if (i == selectedSub.size() - 1) {
                                selectedSubString += selectedSub.get(i);
                            } else {
                                selectedSubString += selectedSub.get(i) + ",";
                            }
                        }
                        if (isLeastFav) {
                            user.leastFavoriteClasses = selectedSubString;
                            mBinding.leastFavuSubj.setText(selectedSubString);
                        } else {
                            user.favoriteClasses = selectedSubString;
                            mBinding.favoriteSubj.setText(selectedSubString);
                        }
                    }
                })
                .show();
    }

    private void setUpUserInfo() {
        getFirebaseStore().collection(getString(R.string.db_root_students)).document(getFirebaseAuth().getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        EditProfileFragment.this.user = user;
                        user.id = documentSnapshot.getId();
                        mBinding.edtFirstName.setText(user.firstName);
                        mBinding.edtLastName.setText(user.lastName);
                        mBinding.etPhoneNumber.setText(user.phoneNumber);
                        mBinding.favHobby.setText(user.hobbiesToPursue);
                        mBinding.favoriteSubj.setText(user.favoriteClasses);
                        mBinding.leastFavuSubj.setText(user.leastFavoriteClasses);
                        mBinding.grade.setText(CommonUtils.getGrade(Integer.parseInt(user.studentClass.trim())));
                        mBinding.codePicker.setClickable(false);
                        mBinding.codePicker.setFocusable(false);
                        mBinding.codePicker.setEnabled(false);
                        mBinding.codePicker.registerCarrierNumberEditText(mBinding.etPhoneNumber);
                        mBinding.codePicker.setFullNumber(user.phoneNumber);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showSnackError(e.getMessage());
                    }
                });
    }

    private void makeSaveRequest() {
        showLoading();
        final Map<String, Object> user = new HashMap<>();
        user.put(KEY_FIRST_NAME, mBinding.edtFirstName.getText().toString());
        user.put(KEY_LAST_NAME, mBinding.edtLastName.getText().toString());
       // user.put(KEY_PASSWORD, getEncryptedPassword());
        user.put(KEY_PHONE_NUMBER, mBinding.codePicker.getFullNumberWithPlus());
        user.put(AppConstants.KEY_STDT_LEAST_FAV_CLASSES, mBinding.leastFavuSubj.getText().toString());
        user.put(AppConstants.KEY_STDT_FAVORITE_CLASSES, mBinding.favoriteSubj.getText().toString());
        user.put(AppConstants.KEY_STDT_CLASS, mBinding.grade.getText().toString());
        user.put(KEY_STDT_HOBBIES, mBinding.favHobby.getText().toString());
        String dbRoot = getString(R.string.db_root_students);
        getFirebaseStore().collection(dbRoot).document(getFirebaseAuth()
                .getCurrentUser().getUid()).set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        Toast.makeText(requireContext(),
                                "Profile Updated.", Toast.LENGTH_SHORT).show();
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

    @NonNull
    private String getEncryptedPassword() {
        try {
            return encrypt(mBinding.password.getText().toString());
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
        return mBinding.password.getText().toString();
    }

}
