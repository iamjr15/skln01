package com.autohub.skln.activities.user.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.autohub.skln.R
import com.autohub.skln.activities.user.adaptors.CustomArrayAdapter
import com.autohub.skln.activities.user.adaptors.ExploreAdaptor
import com.autohub.skln.databinding.ExploreTutorFragmentBinding
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.fragment.FullProfileTutorFragment
import com.autohub.skln.listeners.ItemClickListener
import com.autohub.skln.models.User
import com.autohub.skln.utills.ActivityUtils
import com.autohub.skln.utills.AppConstants
import com.autohub.skln.utills.GpsUtils
import com.autohub.skln.utills.LocationProvider
import com.google.android.gms.location.LocationListener
import kotlinx.android.synthetic.main.explore_tutor_fragment.*
import java.util.*


class ExploreTutorsFragment : BaseFragment() {


    private var mBinding: ExploreTutorFragmentBinding? = null
    private var mCurrentLocation: Location? = null
    private var mGpsUtils: GpsUtils? = null
    private var exploreAdaptor: ExploreAdaptor? = null

    var grades = arrayOf(
            "1",
            "2",
            "3",
            "4",
            "5"
    )


    private val tutorsClickListener = ItemClickListener<User> {

        var bundle = Bundle()
        bundle.putParcelable(AppConstants.KEY_DATA, it);
        ActivityUtils.launchFragment(requireContext(), FullProfileTutorFragment::class.java.name, bundle)
    }

    private val mLocationListener = LocationListener { location ->
        mCurrentLocation = location
        Log.d(">>>>Location", location.toString())

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGpsUtils = GpsUtils(requireActivity())
        if (checkGooglePlayServices() && isLocationPermissionGranted) {
            Log.d(">>>>Location", "Oncreate")
            LocationProvider.getInstance().start(requireContext(), mLocationListener)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.explore_tutor_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = ExploreTutorFragmentBinding.bind(view)
        if (!isLocationPermissionGranted) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 2321)
        }


        mBinding!!.turorsrecycleview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        exploreAdaptor = ExploreAdaptor(requireContext(), tutorsClickListener)
        mBinding!!.turorsrecycleview.adapter = exploreAdaptor

        var customAdapter = CustomArrayAdapter(requireContext(), grades)
        mBinding!!.spinner!!.setAdapter(customAdapter)

        /* mBinding!!.spinner!!.setOnItemClickListener( { adapterView, view, i, l ->

             mBinding!!.txtgrade.setText("grade${grades[i]}")

         })*/


        mBinding!!.spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                mBinding!!.txtgrade.setText("grade ${grades[position]}")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        mBinding!!.txtgrade.setOnClickListener {
            mBinding!!.spinner!!.performClick()

        }

        getTutors()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisible && isResumed) {
            if (!mGpsUtils!!.isGpsEnabled) {
                Log.d(">>>>Location", "onResume 1")
                mGpsUtils!!.turnGPSOn { Log.d(">>>>Location", "OonResume 2") }
            } else {
                Log.d(">>>>Location", "Enabled")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocationProvider.getInstance().stopLocationUpdates()
    }

    private fun getTutors() {
        firebaseStore.collection(getString(R.string.db_root_tutors)).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val usersList = ArrayList<User>()
                for (document in task.result!!) {
                    val user = document.toObject(User::class.java)
                    usersList.add(user)
                    Log.d(">>>Explore", "Data Is " + user.firstName + " , " + user.gender)
                }

                exploreAdaptor!!.setData(usersList)
                //  mBinding.viewPager.setAdapter(new ExplorePagerAdapter(ExploreTutorsFragment.this, users));
            } else {
                Log.d(">>>Explore", "Error getting documents: ", task.exception)
            }
        }.addOnFailureListener { e -> Log.e(">>>Explore", "Error getting documents: ", e) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == BaseFragment.REQUEST_CODE_RECOVER_PLAY_SERVICES) {
            if (resultCode == Activity.RESULT_OK) {
                LocationProvider.getInstance().start(requireContext(), mLocationListener)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(requireContext(), "Google Play Services must be installed.",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2321) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkGooglePlayServices() && isLocationPermissionGranted) {
                    LocationProvider.getInstance().start(requireContext(), mLocationListener)
                }
            }
        }
    }

    /*   private static class ExplorePagerAdapter extends PagerAdapter {
        private final List<User> mData = new ArrayList<>();
        private LayoutInflater mInflater;
        private ExploreTutorsFragment mTutorsFragment;

        private ExplorePagerAdapter(ExploreTutorsFragment fragment, List<User> data) {
            mData.addAll(data);
            mTutorsFragment = fragment;
            mInflater = LayoutInflater.from(mTutorsFragment.requireActivity());
        }

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = (User) v.getTag();
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppConstants.KEY_DATA, user);
                ActivityUtils.launchFragment(v.getContext(), FullProfileTutorFragment.class.getName(), bundle);
            }
        };

        @Override
        public int getCount() {
            return mData.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ItemExploreTeacherBinding binding = ItemExploreTeacherBinding.inflate(mInflater, container, false);
            User user = mData.get(position);
            binding.setModel(new UserViewModel(user));
            binding.viewMore.setTag(user);
            binding.viewMore.setOnClickListener(mOnClickListener);
            if (!TextUtils.isEmpty(user.pictureUrl)) {
                StorageReference pathReference1 = FirebaseStorage.getInstance().getReference().child(user.pictureUrl);
                GlideApp.with(mInflater.getContext())
                        .load(pathReference1)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fallback(R.drawable.default_pic)
                        .into(binding.profilePicture);
            }
            List<String> subjects = user.getSubjectsToTeachAsArray();
            for (String subject : subjects) {
                ItemSubjectBinding subjectsBinding = ItemSubjectBinding.inflate(mInflater, binding.subjects, false);
                subjectsBinding.setText(subject.trim());
                binding.subjects.addView(subjectsBinding.getRoot());
            }
            container.addView(binding.getRoot());
            Location toLocation = new Location(LocationManager.GPS_PROVIDER);
            toLocation.setLatitude(user.latitude);
            toLocation.setLongitude(user.longitude);
            binding.distance.setText(LocationProvider.getInstance().getDistance(mTutorsFragment.mCurrentLocation, toLocation));
            return binding.getRoot();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }*/
}
