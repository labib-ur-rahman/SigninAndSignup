package com.microsl.loginsignup.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.microsl.loginsignup.R
import com.microsl.loginsignup.databinding.FragmentLoginBinding
import com.microsl.loginsignup.databinding.LayoutSidebarBinding
import com.microsl.loginsignup.util.SmartData

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var sidebarBinding: LayoutSidebarBinding
    private var tagLog = "LoginFragment"
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
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        // Called the sidebar functionality
        sideBarLayout()

        binding.btnSignUp.setOnClickListener {
            try {
                Log.d(tagLog, "Fragment replaced via NavHostController")
                it.findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
            } catch (e: Exception) {
                Log.d(tagLog, "Fragment replaced via FragmentTransaction")
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.navHostFragmentContainerView, SignupFragment()).commit()
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
            sideBarBtnLogin.setBackgroundResource(R.drawable.sl_button_selected)
            sideBarBtnSignUp.setBackgroundResource(R.color.sideBar_Bg)
            sideBarBtnGoogle.setBackgroundResource(R.color.sideBar_Bg)
            sideBarBtnFacebook.setBackgroundResource(R.color.sideBar_Bg)
            sideBarTitle.text = resources.getString(R.string.login)

            // Set click listeners for the sidebar buttons
            sideBarBtnLogin.setOnClickListener { fragmentSwitch(LoginFragment()) }
            sideBarBtnSignUp.setOnClickListener { fragmentSwitch(SignupFragment()) }
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