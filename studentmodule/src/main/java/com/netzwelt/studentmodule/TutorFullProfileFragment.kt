package com.netzwelt.studentmodule


import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.models.Request
import com.autohub.skln.models.User
import com.autohub.skln.models.UserViewModel
import com.autohub.skln.utills.AppConstants
import com.autohub.skln.utills.CommonUtils
import com.autohub.skln.utills.GlideApp
import com.autohub.skln.utills.LocationProvider
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.location.LocationListener
import com.google.firebase.storage.FirebaseStorage
import com.netzwelt.studentmodule.databinding.FragmentTutorFullProfileBinding


/**
 * A simple [Fragment] subclass.
 */
class TutorFullProfileFragment : BaseFragment() {
    var readMore: Boolean = false

    private var mUserViewModel: UserViewModel? = null
    private var mBinding: FragmentTutorFullProfileBinding? = null
    private var mCurrentUser: User? = null
    /* private val mLocationListener = LocationListener { location ->
         mCurrentLocation = location

       *//*  if (mUserViewModel != null && mBinding != null) {
            mBinding!!.txtdistence.setText(String.format("%.2f", CommonUtils.distance(mCurrentLocation!!.latitude, mCurrentLocation!!.longitude,

                    mUserViewModel!!.user.latitude.toDouble(), mUserViewModel!!.user.longitude.toDouble())) + " Km")

        }*//*


        Log.d(">>>>Location", location.toString())
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        requireNotNull(arguments) { "Arguments can not be null" }
        val user = arguments!!.getParcelable<User>(AppConstants.KEY_DATA)
        mUserViewModel = UserViewModel(user)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutor_full_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentTutorFullProfileBinding.bind(view)
        mBinding!!.model = mUserViewModel
        mBinding!!.callback = this
        setUpView()
        getCurrentUser()

    }


    fun onReadMoreClick() {
        /* if (readMore) {
             makeTextViewResizable(mBinding!!.bio, 2, "\"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s", true)
             readMore = false
         } else {
             readMore = true

             makeTextViewResizable(mBinding!!.bio, -1, "\"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s", true)

         }*/
    }

    override fun onDestroy() {
        super.onDestroy()


    }

    private fun getCurrentUser() {
        firebaseStore.collection(getString(com.autohub.skln.R.string.db_root_students)).document(firebaseAuth.currentUser!!.uid).get()
                .addOnSuccessListener { documentSnapshot -> mCurrentUser = documentSnapshot.toObject(User::class.java) }
                .addOnFailureListener { e -> showSnackError(e.message) }
    }

    private fun setUpView() {
        addSubjectRadioButtons(mUserViewModel!!.user.subjectsToTeach.split(","))
        mUserViewModel!!.user.classType?.split(",")?.let { addClassTypeRadioButtons(it) }

        mBinding!!.txtdistence.setText(mUserViewModel!!.user.distance)


        if (mUserViewModel!!.user.pictureUrl != null) {
            val pathReference1 = FirebaseStorage.getInstance().reference.child(mUserViewModel!!.user.pictureUrl)
            GlideApp.with(requireContext())
                    .load(pathReference1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fallback(com.autohub.skln.R.drawable.default_pic)
                    .into(mBinding!!.profilePicture)
        }


    }

    private fun makeRequest(subject: String) {
        showLoading()
        val studentId = firebaseAuth.currentUser!!.uid
        val tutorId = mUserViewModel!!.userId
        val request = Request(studentId, tutorId, subject, mUserViewModel!!.firstName, mCurrentUser!!.firstName, mCurrentUser!!.studentClass, mUserViewModel!!.classType)
        val dbRoot = getString(com.autohub.skln.R.string.db_root_requests)
        firebaseStore.collection(dbRoot).add(request).addOnSuccessListener { documentReference ->
            Log.d(">>>>Request", "DocumentSnapshot added with ID: " + documentReference.id)
            hideLoading()
        }.addOnFailureListener { hideLoading() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(com.autohub.skln.R.menu.menu_favorite, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            requireActivity().finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun addSubjectRadioButtons(subjects: List<String>) {
        if (subjects.size == 0) {
            val rdbtn = RadioButton(requireContext())
            rdbtn.id = View.generateViewId()
            //rdbtn.text = "Radio " + rdbtn.id
            rdbtn.text = mUserViewModel!!.user.subjectsToTeach
            mBinding!!.classtyperadio.addView(rdbtn)
        }

        for (i in 0 until subjects.size) {
            val rdbtn = RadioButton(requireContext())
            rdbtn.id = View.generateViewId()
            //rdbtn.text = "Radio " + rdbtn.id
            rdbtn.text = subjects[i]
            mBinding!!.subjectradio.addView(rdbtn)
        }
    }

    fun addClassTypeRadioButtons(classtype: List<String>) {
        if (classtype.size == 0) {
            val rdbtn = RadioButton(requireContext())
            rdbtn.id = View.generateViewId()
            //rdbtn.text = "Radio " + rdbtn.id
            rdbtn.text = mUserViewModel!!.user.classType
            mBinding!!.classtyperadio.addView(rdbtn)
        }

        for (i in 0 until classtype.size) {
            val rdbtn = RadioButton(requireContext())
            rdbtn.id = View.generateViewId()
            //rdbtn.text = "Radio " + rdbtn.id
            rdbtn.text = classtype[i]
            mBinding!!.classtyperadio.addView(rdbtn)
        }
    }

    fun onBackClick() {
        requireActivity().finish()
    }

    fun onRequestClick() {

        if (mBinding!!.subjectradio.checkedRadioButtonId == -1) {

        } else {

            /* if (mUserViewModel!!.user.subjectsToTeach.split(',').size == 0) {
                 println("==================== Selected subject" + mUserViewModel!!.user.subjectsToTeach)

                 makeRequest(mUserViewModel!!.user.subjectsToTeach)

             } else {
                 println("==================== Selected subject" + mUserViewModel!!.user.subjectsToTeach.split(',')[mBinding!!.subjectradio.checkedRadioButtonId - 1])

                 makeRequest(mUserViewModel!!.user.subjectsToTeach.split(',')[mBinding!!.subjectradio.checkedRadioButtonId - 1])
             }*/


        }


        /*   ChooserDialogFragment.newInstance("Choose Subject", mUserViewModel!!.subjectsToTeachAsArray, object : DialogFragmentButtonPressedListener<String>() {
               override fun onButtonPressed(values: String) {

               }
           }).show(childFragmentManager, "Choose Subject")*/
    }


}

