package com.autohub.skln.tutor;

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

public class TutorHomeActivity extends BaseActivity {

    private List<TextView> mTabs = new ArrayList<>();

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_home);
        setStatusBarColor(R.drawable.tutor_home_header);
        mTabs.add(findViewById(R.id.tab_item_home));
        mTabs.add(findViewById(R.id.tab_item_toolbox));
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
                if (position == 0) {
                    setStatusBarColor(R.drawable.tutor_home_header);
                } else if (position == 1 || position == 2) {
                    setStatusBarColor(R.drawable.white_header);
                } else {
                    setStatusBarColor(R.drawable.tutor_profile_header);
                }
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
                Bundle bundle = new Bundle();
                bundle.putString(AppConstants.KEY_TYPE, "tutor");
                FragmentClassRequests fragmentClassRequests = new FragmentClassRequests();
                fragmentClassRequests.setArguments(bundle);
                return fragmentClassRequests;
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
