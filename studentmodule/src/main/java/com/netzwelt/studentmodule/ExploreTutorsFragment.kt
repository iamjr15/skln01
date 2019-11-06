package com.netzwelt.studentmodule

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.listeners.ItemClickListener
import com.autohub.skln.models.ExploreFilter
import com.autohub.skln.models.User
import com.autohub.skln.utills.*
import com.autohub.skln.utills.AppConstants.*
import com.google.android.gms.location.LocationListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.netzwelt.studentmodule.adaptors.CustomArrayAdapter
import com.netzwelt.studentmodule.adaptors.ExploreAdaptor
import com.netzwelt.studentmodule.databinding.ExploreTutorFragmentBinding
import java.util.*


class ExploreTutorsFragment : BaseFragment() {


    private var tutorsList = ArrayList<User>()
    private var mBinding: ExploreTutorFragmentBinding? = null
    private var mCurrentLocation: Location? = null
    private var mGpsUtils: GpsUtils? = null
    private var exploreAdaptor: ExploreAdaptor? = null
    private lateinit var exploreFilter: ExploreFilter

    var grades = arrayOf(
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
            "11",
            "12"
    )

    var AUTOCOMPLETE_REQUEST_CODE = 1
    fun updateExploreData(user: User, subjectName: String) {

        var location = Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(user.latitude.toDouble())
        location.setLongitude(user.longitude.toDouble())

        LocationProvider.getInstance().getAddressFromLocation(requireContext(), location) { address ->
            Log.d(">>>>LocationAddress", "Address is :$address")
            mBinding!!.textlocation.setText(address)
        }


        mBinding!!.txtgrade.setText("grade ${user.studentClass}")
        exploreFilter = ExploreFilter(user.studentClass, subjectName, user.latitude, user.longitude, filterType = ACADMIC_SELECTION_FILTER)
        getTutors(exploreFilter)
    }

    private val tutorsClickListener = ItemClickListener<User> {

        var bundle = Bundle()
        bundle.putParcelable(KEY_DATA, it);
        ActivityUtils.launchFragment(requireContext(), TutorFullProfileFragment::class.java.name, bundle)
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
        mBinding!!.changeaddress.setOnClickListener {


            if (!Places.isInitialized()) {
                Places.initialize(this.activity!!.application, getString(R.string.google_key))
            }
            val placeFields = Arrays.asList(Place.Field.ADDRESS,
                    Place.Field.LAT_LNG
            )

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, placeFields)
                    .build(requireContext())
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)


        }





        mBinding!!.turorsrecycleview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        exploreAdaptor = ExploreAdaptor(requireContext(), tutorsClickListener)
        mBinding!!.turorsrecycleview.adapter = exploreAdaptor

        var customAdapter = CustomArrayAdapter(requireContext(), grades)
        mBinding!!.spinner!!.setAdapter(customAdapter)



        mBinding!!.spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                mBinding!!.txtgrade.setText("grade ${grades[position]}")
                exploreFilter = ExploreFilter(grades[position], "", 0.toFloat(), 0.toFloat(), filterType = GREAD_SELECTION_FILTER)
                getTutors(exploreFilter)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        mBinding!!.txtgrade.setOnClickListener {
            mBinding!!.spinner!!.performClick()

        }
        exploreFilter = ExploreFilter("all", "", 0.toFloat(), 0.toFloat(), filterType = ALL_SELECTION_FILTER)

        getTutors(exploreFilter)
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

    private fun getTutors(exploreFilter: ExploreFilter) {
        firebaseStore.collection(getString(R.string.db_root_tutors)).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                tutorsList = ArrayList<User>()
                for (document in task.result!!) {
                    val user = document.toObject(User::class.java)
                    if (checkFilterfoData(exploreFilter, user)) {
                        tutorsList.add(user)

                    }
                    Log.d(">>>Explore", "Data Is " + user.firstName + " , " + user.gender)
                }

                if (tutorsList.size > 0) {
                    mBinding!!.rrempty.visibility = View.GONE

                } else {
                    mBinding!!.rrempty.visibility = View.VISIBLE

                }

                exploreAdaptor!!.setData(tutorsList, mCurrentLocation)
                //  mBinding.viewPager.setAdapter(new ExplorePagerAdapter(ExploreTutorsFragment.this, users));
            } else {
                Log.d(">>>Explore", "Error getting documents: ", task.exception)
            }
        }.addOnFailureListener { e -> Log.e(">>>Explore", "Error getting documents: ", e) }
    }

    private fun checkFilterfoData(exploreFilter: ExploreFilter, tutorData: User): Boolean {

        if (exploreFilter.filterType.equals(ALL_SELECTION_FILTER)) {
            return true
        } else if (exploreFilter.filterType.equals(ACADMIC_SELECTION_FILTER)) {
            if (tutorData.classesToTeach.split(",").contains(exploreFilter.studentClass) &&
                    tutorData.subjectsToTeach.contains(exploreFilter.subjectName)
                    && CommonUtils.distance(tutorData.latitude.toDouble(),
                            tutorData.longitude.toDouble(),
                            exploreFilter.latitude.toDouble(),
                            exploreFilter.longitude.toDouble()) < 5) {
                return true
            }
        } else {
            if (tutorData.classesToTeach.split(",").contains(exploreFilter.studentClass)) {
                return true
            }
        }

        return false
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
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                var place = Autocomplete.getPlaceFromIntent(data!!)              // this.onPlaceSelected(place);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                var status = Autocomplete.getStatusFromIntent(data!!);                //  this.onError(status)
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


}
