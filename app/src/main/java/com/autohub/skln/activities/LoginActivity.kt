package com.autohub.skln.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.DataBindingUtil
import com.autohub.skln.BaseActivity
import com.autohub.skln.R
import com.autohub.skln.activities.tutor.TutorSignupActivity
import com.autohub.skln.activities.user.SignupStart
import com.autohub.skln.activities.user.StudentHomeActivity
import com.autohub.skln.databinding.ActivityLoginBinding
import com.autohub.skln.tutor.TutorHomeActivity
import com.autohub.skln.utills.ActivityUtils
import com.autohub.skln.utills.AppConstants.TYPE_STUDENT
import com.autohub.skln.utills.AppConstants.TYPE_TUTOR
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.EmailAuthProvider
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

class LoginActivity : BaseActivity() {
    private var mBinding: ActivityLoginBinding? = null
    private val mAccountType = TYPE_STUDENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mBinding!!.callback = this

        mBinding!!.usertype.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    if (checkedId == R.id.radiostudent) {
                        updateUi(true)
                    } else {
                        updateUi(false)
                    }
                })

    }

    private fun updateUi(isStudent: Boolean) {
        if (isStudent) {
            mBinding!!.tvHintemamil.visibility = View.VISIBLE
            mBinding!!.rremail.visibility = View.VISIBLE
            mBinding!!.tvHintloginid.visibility = View.GONE
            mBinding!!.rrloginid.visibility = View.GONE
        } else {
            mBinding!!.tvHintemamil.visibility = View.GONE
            mBinding!!.rremail.visibility = View.GONE
            mBinding!!.tvHintloginid.visibility = View.VISIBLE
            mBinding!!.rrloginid.visibility = View.VISIBLE
        }

    }

    fun login() {
        val email = mBinding!!.edtemail.text
        if (email == null) {
            mBinding!!.edtemail.error = resources.getString(R.string.enter_email)
            mBinding!!.edtemail.requestFocus()
            return
        }

        if (!isValidEmailId(email.toString())) {
            mBinding!!.edtemail.error = resources.getString(R.string.enter_validemail)
            mBinding!!.edtemail.requestFocus()
            return
        }


        val password = getString(mBinding!!.edtPassword.text)
        if (TextUtils.isEmpty(password)) {
            mBinding!!.edtPassword.error = resources.getString(R.string.enter_password)
            mBinding!!.edtPassword.requestFocus()
            // showSnackError(R.string.enter_password)
            return
        }
        showLoading()


        validateUserCredentials()


        /*  var db_root = getString(R.string.db_root_tutors)
          if (TYPE_STUDENT.equals(mAccountType, ignoreCase = true)) {
              db_root = getString(R.string.db_root_students)
          }*/


        /* var db_root = getString(R.string.db_root_students)
         val currentUser = firebaseAuth.currentUser ?: return
         firebaseStore.collection(db_root).document(currentUser.uid).get().addOnCompleteListener(OnCompleteListener { task ->
             if (task.isSuccessful) {
                 val snapshot = task.result
                 if (snapshot == null) {
                     showNeedToRegister()
                     return@OnCompleteListener
                 }
                 val savedPassword = snapshot.getString(KEY_PASSWORD)
                 validateUser(savedPassword)
                 hideLoading()
             } else {
                 showNeedToRegister()
             }
         })*/
    }

    private fun validateUserCredentials() {

        val credential = EmailAuthProvider.getCredential(mBinding!!.edtemail.text.toString().trim(),
                encrypt(mBinding!!.edtPassword.text.toString().trim()))

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener {
                    hideLoading()
                    if (it.isSuccessful) {
                        moveNext()
                    } else {
                        showNeedToRegister()

                    }


                }.addOnFailureListener {
                    showNeedToRegister()

                    hideLoading()

                }


        var db_root = getString(R.string.db_root_students)
        /*     firebaseStore.collection(db_root).whereEqualTo(KEY_EMAIL, mBinding!!.edtemail.text.toString().trim())
                     .whereEqualTo(KEY_PASSWORD, encrypt(mBinding!!.edtPassword.text.toString().trim())).get().addOnCompleteListener {
                         hideLoading()
                         if (it.isSuccessful && !it.result!!.isEmpty) {
                             val snapshot = it.result
                             if (snapshot == null) {
                                 showNeedToRegister()
                                 return@addOnCompleteListener
                             }


                             *//*val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName("")
                                .
                                .build()
*//*

                        *//*   val i = Intent(this@LoginActivity, StudentHomeActivity::class.java)
                           i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                           startActivity(i)
                           finish()*//*

                        // Move user to Home screen
                    } else {
                        showNeedToRegister()
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("", "Error writing document", e)
                    hideLoading()
                    showNeedToRegister()
                }*/


    }

    private fun updatePasswordVisibility(editText: AppCompatEditText) {
        if (editText.transformationMethod is PasswordTransformationMethod) {
            editText.transformationMethod = null
            mBinding!!.txtshowpass.setText(R.string.hide)
        } else {
            editText.transformationMethod = PasswordTransformationMethod()
            mBinding!!.txtshowpass.setText(R.string.show)

        }
        editText.setSelection(editText.length())
    }


    private fun moveNext() {
        val intent: Intent
        if (mAccountType.equals(TYPE_TUTOR, ignoreCase = true)) {
            Toast.makeText(this, "Tutor Verified!", Toast.LENGTH_SHORT).show()
            intent = Intent(this, TutorHomeActivity::class.java)
        } else {
            Toast.makeText(this, "Student Verified!", Toast.LENGTH_SHORT).show()
            intent = Intent(this, StudentHomeActivity::class.java)
        }

        appPreferenceHelper.setSignupComplete(true)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finishAffinity()
    }


    private fun validateUser(savedPass: String?) {
        val pass = getString(mBinding!!.edtPassword.text)
        try {
            if (BaseActivity.encrypt(pass) == savedPass) {
                val intent: Intent
                if (mAccountType.equals(TYPE_TUTOR, ignoreCase = true)) {
                    Toast.makeText(this, "Tutor Verified!", Toast.LENGTH_SHORT).show()
                    intent = Intent(this, TutorHomeActivity::class.java)
                } else {
                    Toast.makeText(this, "Student Verified!", Toast.LENGTH_SHORT).show()
                    intent = Intent(this, StudentHomeActivity::class.java)
                }
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finishAffinity()
            } else if (savedPass == null) {
                showNeedToRegister()
                finishAffinity()
            } else {
                Toast.makeText(this, "Password not matched!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        }

    }

    private fun showNeedToRegister() {
        Snackbar.make(findViewById(android.R.id.content), R.string.wrong_crendentials, Snackbar.LENGTH_LONG)
                /*.setAction("Sign Up") {
                    val intent = Intent(this@LoginActivity, TutorOrStudent::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }*/
                .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
                .show()
    }

    fun signUp() {
        if (mBinding!!.radiostudent.isChecked) {
            ActivityUtils.launchActivity(this@LoginActivity, SignupStart::class.java)
        } else {
            ActivityUtils.launchActivity(this@LoginActivity, TutorSignupActivity::class.java)

        }
    }

    fun updatepasswordVisibility() {
        updatePasswordVisibility(mBinding!!.edtPassword)

    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}
