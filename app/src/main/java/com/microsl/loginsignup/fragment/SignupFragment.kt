package com.microsl.loginsignup.fragment

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.microsl.loginsignup.R
import com.microsl.loginsignup.Users
import com.microsl.loginsignup.databinding.ActivityMainBinding
import com.microsl.loginsignup.databinding.FragmentSignupBinding
import com.microsl.loginsignup.databinding.LayoutSidebarBinding
import com.microsl.loginsignup.util.SmartData

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var tagLog = "SignupFragment"
    private lateinit var sidebarBinding: LayoutSidebarBinding
    private lateinit var layoutInflater: LayoutInflater

    // Dark Mode
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var isNightMode: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        // Called the sidebar functionality
        sideBarLayout()

        // Initialize Firebase Auth and Firebase Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.signupButton.setOnClickListener {
            val user = binding.signupName.text.toString().trim()
            val email = binding.signupEmail.text.toString().trim()
            val pass = binding.signupPassword.text.toString().trim()

            //val userUid = currentUser.uid
            val rootRef = FirebaseDatabase.getInstance().getReference()
            val usersRef = rootRef.child("users")

            if (user.isEmpty()) binding.signupName.error = "Name cannot be empty"
            if (email.isEmpty()) binding.signupEmail.error = "Email cannot be empty"
            if (pass.isEmpty()) binding.signupPassword.error = "Password cannot be empty"
            else {
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val userInfo = Users(
                            binding.signupName.text.toString(),
                            binding.signupEmail.text.toString(),
                            binding.signupPassword.text.toString()
                        )
                        val id = task.result.user!!.uid
                        database.reference.child("Users").child(id).setValue(userInfo)

                        Toast.makeText(context,"SignUp Successful",Toast.LENGTH_SHORT).show()
                        it.findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                    } else {
                        Toast.makeText(context, "SignUp Failed ${task.exception!!.message}",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val bind= ActivityMainBinding.inflate(inflater, container, false)
            try {
                Log.d(tagLog, "Fragment replaced via NavHostController")
                it.findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            } catch (e: Exception) {
                Log.d(tagLog, "Fragment replaced via FragmentTransaction ${e.message}")
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.navHostFragmentContainerView, LoginFragment()).commit()
            }
        }

        return binding.root
    }

    // ----------------------------------------------------------------
    private fun sideBarLayout(){
        // Inflate the sidebar layout
        layoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Initialize the sidebar layout binding
        sidebarBinding = LayoutSidebarBinding.inflate(layoutInflater)

        // Add the sidebar layout to the ViewGroup
        binding.sideBar.addView(sidebarBinding.root)

        // Initialize the night mode toggle
        nightModeToggle()

        // Set click listeners for the sidebar buttons
        sidebarBinding.apply {
            // Set Selected Button and deactivate buttons styles
            sideBarBtnSignUp.setBackgroundResource(R.drawable.sl_button_selected)
            sideBarBtnLogin.setBackgroundResource(R.color.sideBar_Bg)
            sideBarBtnGoogle.setBackgroundResource(R.color.sideBar_Bg)
            sideBarBtnFacebook.setBackgroundResource(R.color.sideBar_Bg)
            sideBarTitle.text = resources.getString(R.string.signup)

            // Set click listeners for the sidebar buttons
            sideBarBtnLogin.setOnClickListener { fragmentSwitch(LoginFragment()) }
            sideBarBtnSignUp.setOnClickListener { fragmentSwitch(SignupFragment()) }
            sideBarBtnSettings.setOnClickListener {
                if (isNightMode as Boolean) {
                    editor.putBoolean(SmartData.SWITCH_BUTTON_KEY, true).apply()
                    sideBarBtnSettings.setImageResource(R.drawable.sl_moon)
                    sideBarBtnSettings.setBackgroundResource(R.drawable.night_mode_thumb_true)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    isNightMode = false
                } else {
                    editor.putBoolean(SmartData.SWITCH_BUTTON_KEY, false).apply()
                    sideBarBtnSettings.setImageResource(R.drawable.sl_sun)
                    sideBarBtnSettings.setBackgroundResource(R.drawable.night_mode_thumb_false)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    isNightMode = true
                }
            }
        }
    }
    private fun nightModeToggle(){
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
    private fun fragmentSwitch(fragment: Fragment){
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.navHostFragmentContainerView, fragment).commit()
    }
}
