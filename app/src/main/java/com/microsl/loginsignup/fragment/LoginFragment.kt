package com.microsl.loginsignup.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.microsl.loginsignup.R
import com.microsl.loginsignup.activity.MainActivity
import com.microsl.loginsignup.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var database: Intent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)



        binding.btnSignUp.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        return binding.root
    }
}