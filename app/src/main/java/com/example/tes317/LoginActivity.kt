package com.example.tes317

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tes317.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(android.R.color.transparent)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }

        auth = FirebaseAuth.getInstance()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.apply {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Please enter email and password", Toast.LENGTH_SHORT).show()
                } else {
                    loginUser(email, password)
                }
            }

            tvRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }

            btnPasswordVisibility.setOnClickListener {
                isPasswordVisible = !isPasswordVisible
                togglePasswordVisibility()
            }
        }
    }

    private fun togglePasswordVisibility() {
        binding.apply {
            if (isPasswordVisible) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                btnPasswordVisibility.setImageDrawable(ContextCompat.getDrawable(this@LoginActivity, R.drawable.ic_visibility))
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                btnPasswordVisibility.setImageDrawable(ContextCompat.getDrawable(this@LoginActivity, R.drawable.ic_visibility_off))
            }
            etPassword.setSelection(etPassword.text.length)
        }
    }

    private fun loginUser(email: String, password: String) {
        binding.btnLogin.isEnabled = false
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user?.isEmailVerified == true) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    // Navigate to home screen or dashboard
                    startActivity(Intent(this, MenuActivity::class.java)) // Ganti HomeActivity dengan activity yang sesuai
                    finish()
                } else {
                    Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }
            binding.btnLogin.isEnabled = true
        }
    }
}
