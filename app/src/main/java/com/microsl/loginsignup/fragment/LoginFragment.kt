package com.microsl.loginsignup.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.microsl.loginsignup.R
import com.microsl.loginsignup.Users
import com.microsl.loginsignup.activity.SettingActivity
import com.microsl.loginsignup.databinding.FragmentLoginBinding
import com.microsl.loginsignup.databinding.LayoutSidebarBinding
import com.microsl.loginsignup.util.AuthRepository
import com.microsl.loginsignup.util.GoogleAuthClient
import com.microsl.loginsignup.util.SmartData
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var sidebarBinding: LayoutSidebarBinding
    private var tagLog = "LoginFragment"
    private lateinit var layoutInflater: LayoutInflater

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var authRepository: AuthRepository
    private lateinit var googleAuthClient: GoogleAuthClient

    // Dark Mode
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var isNightMode: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        // Initialize Firebase Auth and Firebase Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        authRepository = AuthRepository()
        googleAuthClient = GoogleAuthClient(requireActivity())

        // Called the sidebar functionality
        sideBarLayout()

        binding.apply {

            googleCntBtn.setOnClickListener {
                lifecycleScope.launch {
                    val isSignIn = googleAuthClient.signIn(true)
                    if (isSignIn) {
                        val id = auth.currentUser?.uid
                        val name = auth.currentUser?.displayName
                        val email = auth.currentUser?.email
                        val image = auth.currentUser?.photoUrl.toString()

                        Toast.makeText(context, "SignIn Successfully", Toast.LENGTH_SHORT).show()

                        val userInfo = Users(id, name, image, email)
                        // Profile update kora hole o sob change hoa jay ata thik kora lagbe pore ============================= TODO =================================
                        id?.let { it1 -> database.reference.child("Users").child(it1).setValue(userInfo) }
                    } else {
                        Toast.makeText(context, "SignIn Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            btnForgotPassword.setOnClickListener {
                val builder = AlertDialog.Builder(requireActivity())
                val dialogView: View = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null)
                val emailBox = dialogView.findViewById<EditText>(R.id.emailBox)

                builder.setView(dialogView)
                val dialog = builder.create()

                dialogView.findViewById<View>(R.id.btnReset)
                    .setOnClickListener(View.OnClickListener {
                        val userEmail = emailBox.text.toString()
                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                            Toast.makeText( requireActivity(), "Enter your registered email id", Toast.LENGTH_SHORT).show()
                            return@OnClickListener
                        }
                        auth.sendPasswordResetEmail(userEmail)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(requireActivity(),"Check your email",Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()
                                } else {
                                    Toast.makeText(requireActivity(), "Unable to send, failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                    })
                dialogView.findViewById<View>(R.id.btnCancel)
                    .setOnClickListener { dialog.dismiss() }
                if (dialog.window != null) {
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
                }
                dialog.show()
            }

            btnLogin.setOnClickListener {
                val email = loginEmail.text.toString()
                val pass = loginPassword.text.toString()

                Log.d(tagLog, "$email $pass")

                if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (pass.isNotEmpty()) {
                        lifecycleScope.launch {
                            Log.d(tagLog, "lifecycleScope")
                            val isLoginSuccess = authRepository.login(email, pass)
                            if (isLoginSuccess) {
                                val intent = Intent(context, SettingActivity::class.java)
                                startActivity(intent)
                                Toast.makeText(context, "Sign in successfully", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(context, "Sign in Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        loginPassword.error = "Empty fields are not allowed"
                    }
                } else if (email.isEmpty()) {
                    loginEmail.error = "Empty fields are not allowed"
                } else {
                    loginEmail.error = "Please enter correct email"
                }

//                auth.signInWithEmailAndPassword(email, pass)
//                    .addOnSuccessListener {
//                        Toast.makeText(context, "Sign in successfully", Toast.LENGTH_SHORT).show()
//                        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.navHostFragmentContainerView, SignupFragment()).commit()
//                    }.addOnFailureListener {
//                        Toast.makeText(context, "Sign in Failed", Toast.LENGTH_SHORT).show()
//                    }
            }

            btnSignUp.setOnClickListener {
                try {
                    Log.d(tagLog, "Fragment replaced via NavHostController")
                    it.findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
                } catch (e: Exception) {
                    Log.d(tagLog, "Fragment replaced via FragmentTransaction")
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.navHostFragmentContainerView, SignupFragment()).commit()
                }
            }

        }

        return binding.root
    }

    // ----------------------------------------------------------------
    private fun sideBarLayout() {
        // Inflate the sidebar layout
        layoutInflater =
            context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Initialize the sidebar layout binding
        sidebarBinding = LayoutSidebarBinding.inflate(layoutInflater)

        // Add the sidebar layout to the ViewGroup
        binding.sideBar.addView(sidebarBinding.root)

        // Initialize the night mode toggle
        nightModeToggle()

        // Set click listeners for the sidebar buttons
        sidebarBinding.apply {
            // Set Selected Button and deactivate buttons styles
            sideBarBtnLogin.setBackgroundResource(R.drawable.sl_button_selected)
            sideBarBtnSignUp.setBackgroundResource(R.color.sideBar_Bg)
            //sideBarBtnGoogle.setBackgroundResource(R.color.sideBar_Bg)
            //sideBarBtnFacebook.setBackgroundResource(R.color.sideBar_Bg)
            sideBarTitle.text = resources.getString(R.string.login)

            // Set click listeners for the sidebar buttons
            sideBarBtnLogin.setOnClickListener { fragmentSwitch(LoginFragment()) }
            sideBarBtnSignUp.setOnClickListener { fragmentSwitch(SignupFragment()) }
            sideBarBtnGoogle.setOnClickListener {
                lifecycleScope.launch {
                    val isSignIn = googleAuthClient.signIn(false)
                    if (isSignIn) {
                        val id = auth.currentUser?.uid
                        val name = auth.currentUser?.displayName
                        val email = auth.currentUser?.email
                        val image = auth.currentUser?.photoUrl.toString()

                        Toast.makeText(context, "SignIn Successfully", Toast.LENGTH_SHORT).show()

                        val userInfo = Users(id, name, image, email)
                        // Profile update kora hole o sob change hoa jay ata thik kora lagbe pore ============================= TODO =================================
                        id?.let { it1 -> database.reference.child("Users").child(it1).setValue(userInfo) }
                    } else {
                        Toast.makeText(context, "SignIn Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            sideBarBtnFacebook.setOnClickListener {
                lifecycleScope.launch {
                    googleAuthClient.signOut()
                    Toast.makeText(context, "SignOut", Toast.LENGTH_SHORT).show()
                }
            }
            sideBarBtnSettings.setOnClickListener {
                if (isNightMode as Boolean) {
                    editor.putBoolean(SmartData.SWITCH_BUTTON_KEY, true).apply()
                    sidebarBinding.sideBarBtnSettings.setImageResource(R.drawable.sl_moon)
                    sidebarBinding.sideBarBtnSettings.setBackgroundResource(R.drawable.night_mode_thumb_true)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    isNightMode = false
                } else {
                    editor.putBoolean(SmartData.SWITCH_BUTTON_KEY, false).apply()
                    sidebarBinding.sideBarBtnSettings.setImageResource(R.drawable.sl_sun)
                    sidebarBinding.sideBarBtnSettings.setBackgroundResource(R.drawable.night_mode_thumb_false)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    isNightMode = true
                }
            }
        }
    }

    private fun nightModeToggle() {
        // Initialize the binding
        sharedPreferences = context?.getSharedPreferences(SmartData.PREF_KEY, MODE_PRIVATE)!!
        editor = sharedPreferences.edit()

        // Get the current theme mode
        isNightMode = sharedPreferences.getBoolean(SmartData.SWITCH_BUTTON_KEY, false)
        if (isNightMode as Boolean) {
            sidebarBinding.sideBarBtnSettings.setImageResource(R.drawable.sl_moon)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            sidebarBinding.sideBarBtnSettings.setImageResource(R.drawable.sl_sun)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun fragmentSwitch(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.navHostFragmentContainerView, fragment).commit()
    }
}