package com.autohub.skln.student;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.autohub.skln.BaseActivity;
import com.autohub.skln.R;
import com.autohub.skln.fragment.FragmentClassRequests;
import com.autohub.skln.fragment.FragmentProfile;
import com.autohub.skln.fragment.FragmentToolbox;
import com.autohub.skln.utills.AppConstants;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class StudentHomeActivity extends BaseActivity {

    private List<TextView> mTabs = new ArrayList<>();
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        setStatusBarColor(R.drawable.white_header);
        mTabs.add(findViewById(R.id.tab_item_home));
        mTabs.add(findViewById(R.id.tab_item_toolbox));
        mTabs.add(findViewById(R.id.tab_item_explore));
        mTabs.add(findViewById(R.id.tab_item_cls_request));
        mTabs.add(findViewById(R.id.tab_item_profile));

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(sectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 4) {
                    setStatusBarColor(R.drawable.tutor_profile_header);

                } else {
                    setStatusBarColor(R.drawable.white_header);
                }
                for (int i = 0; i < mTabs.size(); i++) {
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
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new FragmentHome();
            } else if (position == 1) {
                return new FragmentToolbox();
            } else if (position == 2) {
                return new ExploreTutorsFragment();
            } else if (position == 3) {
                Bundle bundle = new Bundle();
                bundle.putString(AppConstants.KEY_TYPE, "student");
                FragmentClassRequests fragmentClassRequests = new FragmentClassRequests();
                fragmentClassRequests.setArguments(bundle);
                return fragmentClassRequests;
            } else {
                FragmentProfile fragmentProfile = new FragmentProfile();
                Bundle bundle = new Bundle();
                bundle.putString(AppConstants.KEY_TYPE, "student");
                fragmentProfile.setArguments(bundle);
                return fragmentProfile;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
