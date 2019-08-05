package com.autohub.skln.student;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.autohub.skln.BaseActivity;
import com.autohub.skln.R;
import com.autohub.skln.fragment.FragmentClassRequests;
import com.autohub.skln.fragment.FragmentHome;
import com.autohub.skln.fragment.FragmentProfile;
import com.autohub.skln.fragment.FragmentToolbox;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.autohub.skln.utills.AppConstants.KEY_FIRST_NAME;

public class StudentHomeActivity extends BaseActivity {

    private List<TextView> mTabs = new ArrayList<>();

    private ViewPager mViewPager;

    TextView tvHey;
    CircleImageView ivPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        tvHey = findViewById(R.id.hey_user);
        ivPicture = findViewById(R.id.iv_picture);

        getFirebaseStore().collection(getString(R.string.db_root_students)).document(getFirebaseAuth().getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String firstName = documentSnapshot.getString(KEY_FIRST_NAME);
                        tvHey.setText(String.format("Hey, \n%s.", firstName));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showSnackError(e.getMessage());
                    }
                });

        mTabs.add((TextView) findViewById(R.id.tab_item_home));
        mTabs.add((TextView) findViewById(R.id.tab_item_toolbox));
        mTabs.add((TextView) findViewById(R.id.tab_item_cls_request));
        mTabs.add((TextView) findViewById(R.id.tab_item_profile));

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(sectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < 4; i++) {
                    if (position == i) {
                        mTabs.get(i).setTextColor(getResources().getColor(R.color.black));
                        setTextViewDrawableColor(mTabs.get(i), R.color.black);
                    } else {
                        mTabs.get(i).setTextColor(getResources().getColor(R.color.light_grey));
                        setTextViewDrawableColor(mTabs.get(i), R.color.light_grey);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        for (final TextView tab : mTabs) {
            tab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(mTabs.indexOf(tab));
                }
            });
        }
    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new FragmentHome();
            } else if (position == 1) {
                return new FragmentToolbox();
            } else if (position == 2) {
                return new FragmentClassRequests();
            } else if (position == 3) {
                return new FragmentProfile();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

}
