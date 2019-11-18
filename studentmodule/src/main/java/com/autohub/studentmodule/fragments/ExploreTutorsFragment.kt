package com.autohub.studentmodule.fragments

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
import com.autohub.skln.models.TutorGrades
import com.autohub.skln.models.TutorSubjectsModel
import com.autohub.skln.models.UserModel
import com.autohub.skln.models.tutormodels.TutorData
import com.autohub.skln.utills.AppConstants.*
import com.autohub.skln.utills.CommonUtils
import com.autohub.skln.utills.GlideApp
import com.autohub.skln.utills.GpsUtils
import com.autohub.skln.utills.LocationProvider
import com.autohub.studentmodule.R
import com.autohub.studentmodule.activities.StudentHomeActivity
import com.autohub.studentmodule.activities.TutorFullProfileActivity
import com.autohub.studentmodule.adaptors.CustomArrayAdapter
import com.autohub.studentmodule.adaptors.ExploreAdaptor
import com.autohub.studentmodule.databinding.ExploreTutorFragmentBinding
import com.autohub.studentmodule.models.ExploreFilter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.location.LocationListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Vt Netzwelt
 */
class ExploreTutorsFragment : BaseFragment() {


    private var tutorsList = ArrayList<TutorData>()
    private var mBinding: ExploreTutorFragmentBinding? = null
    private var mCurrentLocation: Location? = null
    private var mGpsUtils: GpsUtils? = null
    private var exploreAdaptor: ExploreAdaptor? = null
    private lateinit var exploreFilter: ExploreFilter
    private var userLatLang: Location? = null
    var grades = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
    var tutorsGradeList: ArrayList<TutorGrades> = ArrayList()
    var tutorsSubjectList: ArrayList<TutorSubjectsModel> = ArrayList()

    fun updateExploreData(user: UserModel, subjectName: String) {
        val studentClass = user.academicInfo!!.selectedClass
        firebaseStore.collection("grades").whereEqualTo("id", studentClass).get().addOnSuccessListener {
            it.forEach {
                mBinding!!.txtgrade.setText("grade ${it.getString("grade")!!}")
                exploreFilter = ExploreFilter(it.getString("grade")!!, subjectName, user.personInfo!!.latitude!!, user.personInfo!!.longitude!!, filterType = ACADMIC_SELECTION_FILTER)
                getTutors(exploreFilter)
            }
        }


    }

    private val tutorsClickListener = ItemClickListener<TutorData> {


        startFullProfileActivityForResult(it)

    }


    private val mLocationListener = LocationListener { location ->
        mCurrentLocation = location
        Log.d(">>>>Location", location.toString())
    }


    private fun startFullProfileActivityForResult(user: TutorData) {
        val intent = Intent(context, TutorFullProfileActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(KEY_DATA, user)
        bundle.putString("fromwhere", "searchfrg")
        intent.putExtras(bundle)
        activity!!.startActivityForResult(intent, StudentHomeActivity.TUROR_REQUEST)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGpsUtils = GpsUtils(requireActivity())

        fetchTutorSubjects()
        if (checkGooglePlayServices() && isLocationPermissionGranted) {
            Log.d(">>>>Location", "Oncreate")
            LocationProvider.getInstance().start(requireContext(), mLocationListener)
        }
    }

    private fun setupProfile() {
        val ref = FirebaseStorage.getInstance().reference.child("student/" +
                firebaseAuth.currentUser!!.uid + ".jpg")
        GlideApp.with(this)
                .load(ref)
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // disable caching of glide
                .skipMemoryCache(true)
                .placeholder(com.autohub.skln.R.drawable.default_pic)

                .into(mBinding!!.profilePicture)
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
            val placeFields = listOf(Place.Field.ADDRESS,
                    Place.Field.LAT_LNG
            )

            val intent = Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, placeFields)
                    .build(requireContext())
            activity!!.startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)


        }


        mBinding!!.turorsrecycleview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        exploreAdaptor = ExploreAdaptor(requireContext(), tutorsClickListener)
        mBinding!!.turorsrecycleview.adapter = exploreAdaptor

        val customAdapter = CustomArrayAdapter(requireContext(), grades)
        with(mBinding!!.spinner!!) {
            adapter = customAdapter
        }



        mBinding!!.spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                mBinding!!.txtgrade.setText("grade ${grades[position]}")
                exploreFilter = ExploreFilter(grades[position], "", 0.0, 0.0, filterType = GREAD_SELECTION_FILTER)
                if (tutorsSubjectList.isNotEmpty()) {
                    getTutors(exploreFilter)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        mBinding!!.txtgrade.setOnClickListener {
            mBinding!!.spinner!!.performClick()

        }



        setupProfile()

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
                tutorsList = ArrayList()
                /*
                * Fetch All TUtors List
                * */
                for (document in task.result!!) {
                    Log.e("document", document.toString())
                    val user = document.toObject(TutorData::class.java)
                    val geopoints = ((document.data.get("location") as HashMap<*, *>).get("geopoint")) as GeoPoint
                    user.location!!.latitude = geopoints.latitude
                    user.location!!.longitude = geopoints.longitude


                    var subjects: StringBuilder = StringBuilder()
                    var grades: StringBuilder = StringBuilder()
                    /*
                    * Add tutors Subjects
                    * */
                    for (i in tutorsSubjectList) {
                        if (i.teacherId.equals(user.id)) {
                            subjects.append("," + (context as StudentHomeActivity).subjectDataList
                                    .get((context as StudentHomeActivity).subjectDataList.map { it.id }.indexOf(i.subjectId)).name)
                        }
                    }
                    /*
                     * Add tutors Grades
                     * */
                    for (i in tutorsGradeList) {
                        if (i.teacherId.equals(user.id)) {
                            grades.append("," + (context as StudentHomeActivity).gradesDataList
                                    .get((context as StudentHomeActivity).gradesDataList.map { it.id }.indexOf(i.gradeId)).grade)
                        }
                    }



                    user.subjectsToTeach = subjects.toString().removeRange(0..0)
                    user.classToTeach = grades.toString().removeRange(0..0)


                    /*
                    * Add Distance from Student Location
                    * */
                    var studentdata = (context as StudentHomeActivity).user

                    if (studentdata != null) {
                        user.distance = String.format("%.2f", CommonUtils.distance(studentdata.personInfo!!.latitude!!,
                                studentdata.personInfo!!.longitude!!,
                                user.location!!.latitude!!, user.location!!.longitude!!))
                                .replace(",", ".").toDouble()

                    }

                    /*
                    * Filter Tutors data
                    * */
                    if (checkFilterfoData(exploreFilter, user)) {
                        tutorsList.add(user)
                    }
                }

                val newList = tutorsList.sortedBy { it.distance }


                exploreAdaptor!!.setData(newList, mCurrentLocation)

                if (tutorsList.size > 0) {
                    mBinding!!.rrempty.visibility = View.GONE

                } else {
                    mBinding!!.rrempty.visibility = View.VISIBLE

                }

            } else {
                Log.d(">>>Explore", "Error getting documents: ", task.exception)
            }
        }.addOnFailureListener { e -> Log.e(">>>Explore", "Error getting documents: ", e) }
    }

    private fun checkFilterfoData(exploreFilter: ExploreFilter, tutorData: TutorData): Boolean {

        if (exploreFilter.filterType == ALL_SELECTION_FILTER) {
            return true
        } else if (exploreFilter.filterType == ACADMIC_SELECTION_FILTER) {
            /*
            * Showing tutors with in 50km range of student and with student class and subjects
            * */
            if (tutorData.classToTeach!!.split(",").contains(exploreFilter.studentClass) &&
                    tutorData.subjectsToTeach!!.contains(exploreFilter.subjectName, true)
                    && tutorData.distance!! < 50) {
                return true
            }
        } else {
            if (tutorData.classToTeach!!.split(",").contains(exploreFilter.studentClass)) {
                return true
            }
        }

        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_RECOVER_PLAY_SERVICES) {
            if (resultCode == RESULT_OK) {
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
                var status = Autocomplete.getStatusFromIntent(data!!)                //  this.onError(status)
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2321) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkGooglePlayServices() && isLocationPermissionGranted) {
                    LocationProvider.getInstance().start(requireContext(), mLocationListener)
                }
            }
        }
    }

    companion object {
        const val AUTOCOMPLETE_REQUEST_CODE = 1
    }

    fun fetchTutorSubjects() {
        tutorsGradeList = ArrayList()


        firebaseStore.collection("tutorGrades").get().addOnCompleteListener {

            if (it.isSuccessful) {
                for (document in it.result!!) {
                    val user = document.toObject(TutorGrades::class.java)
                    tutorsGradeList.add(user)

                }
                fetchTutorGrades()

            }
        }
    }

    fun fetchTutorGrades() {
        tutorsSubjectList = ArrayList()
        firebaseStore.collection("tutorSubjects").get().addOnCompleteListener {

            if (it.isSuccessful) {
                for (document in it.result!!) {
                    val user = document.toObject(TutorSubjectsModel::class.java)
                    tutorsSubjectList.add(user)
                }

                showExploreTutors()

            }
        }
    }


    fun showExploreTutors() {

        var user = (context as StudentHomeActivity).user!!

        val location = Location(LocationManager.GPS_PROVIDER)
        location.latitude = user.personInfo!!.latitude!!
        location.longitude = user.personInfo!!.longitude!!

        userLatLang = location

        LocationProvider.getInstance().getAddressFromLocation(requireContext(), location) { address ->
            Log.d(">>>>LocationAddress", "Address is :$address")
            mBinding!!.textlocation.text = address
        }

        if (this.arguments != null) {

            updateExploreData(user, this.arguments!!.getString("data_key", ""))

        } else {
            exploreFilter = ExploreFilter("all", "", 0.0, 0.0, filterType = ALL_SELECTION_FILTER)
            getTutors(exploreFilter)

        }


    }


}
