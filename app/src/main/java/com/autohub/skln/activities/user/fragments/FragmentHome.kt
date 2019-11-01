package com.autohub.skln.activities.user.fragments

//import com.autohub.skln.utills.GlideApp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.autohub.skln.R
import com.autohub.skln.activities.user.adaptors.HobbiesAdaptor
import com.autohub.skln.databinding.FragmentStudentHomeBinding
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.listeners.ItemClickListener
import com.autohub.skln.models.AcadmicsData
import com.autohub.skln.models.User
import com.example.androidapp.ui.home.fragments.country.AcadmicsAdaptor

class FragmentHome : BaseFragment() {
    private var mBinding: FragmentStudentHomeBinding? = null
    private var acadmicsAdaptor: AcadmicsAdaptor? = null
    private var hobbiesAdaptor: HobbiesAdaptor? = null

    private val acadmicClickListener = ItemClickListener<AcadmicsData> { }

    private val hobbiesClickListener = ItemClickListener<AcadmicsData> { }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_student_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentStudentHomeBinding.bind(view)

        mBinding!!.acadmicrecycleview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        acadmicsAdaptor = AcadmicsAdaptor(requireContext(), acadmicClickListener)
        mBinding!!.acadmicrecycleview.adapter = acadmicsAdaptor

        mBinding!!.hobbiesrecycleview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        hobbiesAdaptor = HobbiesAdaptor(requireContext(), hobbiesClickListener)
        mBinding!!.hobbiesrecycleview.adapter = hobbiesAdaptor

        setUpUserInfo()
    }

/*    private fun setupProfile() {
        val ref = FirebaseStorage.getInstance().reference.child("tutor/" +
                firebaseAuth.currentUser!!.uid + ".jpg")
        GlideApp.with(this)
                .load(ref)
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // disable caching of glide
                .skipMemoryCache(true)
                .into(mBinding!!.profilePicture)
    }*/

    private fun setUpUserInfo() {
        firebaseStore.collection(getString(R.string.db_root_students)).document(firebaseAuth.currentUser!!.uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(User::class.java)
                    user!!.id = documentSnapshot.id
                    //                        User user = User.prepareUser(documentSnapshot);
                    mBinding!!.heyUser.text = String.format("Hey, \n%s.", user.firstName)
                    setSubjects(user)
                    setHobbies(user)
                    //                        mUserViewModel.setUser(user);
                }
                .addOnFailureListener { e -> showSnackError(e.message) }
    }

    private fun setSubjects(user: User) {
        acadmicsAdaptor!!.setData(user.acadmics)

    }

    private fun setHobbies(user: User) {
        hobbiesAdaptor!!.setData(user.hobbies)
    }
}
