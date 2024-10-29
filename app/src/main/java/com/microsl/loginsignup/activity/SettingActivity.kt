package com.microsl.loginsignup.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.microsl.loginsignup.R
import com.microsl.loginsignup.databinding.ActivitySettingBinding
import com.microsl.loginsignup.util.SmartData

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
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
        updateUI(sharedPreferences)

        // Set the switch listener
        binding.switchButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                editor.putBoolean(SmartData.SWITCH_BUTTON_KEY, true).apply()
                updateUI(sharedPreferences)
                Toast.makeText(this@SettingActivity, "Night Mode On", Toast.LENGTH_SHORT).show()
            } else {
                editor.putBoolean(SmartData.SWITCH_BUTTON_KEY, false).apply()
                updateUI(sharedPreferences)
                Toast.makeText(this@SettingActivity, "Day Mode On", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(sharedPreferences: SharedPreferences) {
        binding.switchButton.apply {
            // Get the current theme mode
            isChecked = sharedPreferences.getBoolean(SmartData.SWITCH_BUTTON_KEY, false)
            if (isChecked) {
                binding.switchButton.isChecked = isChecked
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                binding.switchButton.isChecked = isChecked
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}