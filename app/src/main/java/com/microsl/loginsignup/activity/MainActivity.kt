package com.microsl.loginsignup.activity

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.microsl.loginsignup.R
import com.microsl.loginsignup.databinding.ActivityMainBinding
import com.microsl.loginsignup.fragment.LoginFragment
import com.microsl.loginsignup.fragment.SignupFragment
import com.microsl.loginsignup.util.SmartData

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var isNightMode: Boolean? = null


    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the binding
        sharedPreferences = getSharedPreferences(SmartData.PREF_KEY, MODE_PRIVATE)
        editor = sharedPreferences.edit()

        // Get the current theme mode
        isNightMode = sharedPreferences.getBoolean(SmartData.SWITCH_BUTTON_KEY, false)
        if (isNightMode as Boolean) {
            binding.sideBarBtnSettings.setImageResource(R.drawable.sl_moon)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            binding.sideBarBtnSettings.setImageResource(R.drawable.sl_sun)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Set the default fragment
        fragmentSwitch(LoginFragment(), binding.sideBarBtnLogin)

        binding.sideBarBtnLogin.setOnClickListener {
            fragmentSwitch(LoginFragment(), binding.sideBarBtnLogin)
        }
        binding.sideBarBtnSignUp.setOnClickListener {
            fragmentSwitch(SignupFragment(), binding.sideBarBtnSignUp)
        }
        binding.sideBarBtnSettings.setOnClickListener {
            if (isNightMode as Boolean) {
                editor.putBoolean(SmartData.SWITCH_BUTTON_KEY, true).apply()
                binding.sideBarBtnSettings.setImageResource(R.drawable.sl_moon)
                binding.sideBarBtnSettings.setBackgroundResource(R.drawable.night_mode_thumb_true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                isNightMode = !this.isNightMode!!
            } else {
                editor.putBoolean(SmartData.SWITCH_BUTTON_KEY, false).apply()
                binding.sideBarBtnSettings.setImageResource(R.drawable.sl_sun)
                binding.sideBarBtnSettings.setBackgroundResource(R.drawable.night_mode_thumb_false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                isNightMode = !this.isNightMode!!
            }
        }

    }

    // ----------------------------------------------------------------
    private fun fragmentSwitch(fragment: Fragment, activeBtn: View){
        supportFragmentManager.beginTransaction().replace(R.id.navHostFragmentContainerView, fragment).commit()
        activeBtn.setBackgroundResource(R.drawable.sl_button_selected)
        when (activeBtn) {
            binding.sideBarBtnLogin -> {
                binding.sideBarBtnSignUp.setBackgroundResource(R.color.sideBar_Bg)
                binding.sideBarBtnGoogle.setBackgroundResource(R.color.sideBar_Bg)
                binding.sideBarBtnFacebook.setBackgroundResource(R.color.sideBar_Bg)
                binding.sideBarTitle.text = resources.getString(R.string.login)
            }
            binding.sideBarBtnSignUp -> {
                binding.sideBarBtnLogin.setBackgroundResource(R.color.sideBar_Bg)
                binding.sideBarBtnGoogle.setBackgroundResource(R.color.sideBar_Bg)
                binding.sideBarBtnFacebook.setBackgroundResource(R.color.sideBar_Bg)
                binding.sideBarTitle.text = resources.getString(R.string.signup)
            }
            else -> {
                binding.sideBarBtnLogin.setBackgroundResource(R.color.sideBar_Bg)
                binding.sideBarBtnSignUp.setBackgroundResource(R.color.sideBar_Bg)
                binding.sideBarBtnGoogle.setBackgroundResource(R.color.sideBar_Bg)
                binding.sideBarBtnFacebook.setBackgroundResource(R.color.sideBar_Bg)
                binding.sideBarTitle.text = resources.getString(R.string.labib)
            }
        }
    }
}