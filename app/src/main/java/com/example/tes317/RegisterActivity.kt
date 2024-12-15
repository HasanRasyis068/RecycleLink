package com.example.tes317

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tes317.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.apply {
            btnRegister.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()
                val username = etUsername.text.toString().trim() // Menambahkan username (jika ada)

                if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                    Toast.makeText(this@RegisterActivity, "Please enter all fields", Toast.LENGTH_SHORT).show()
                } else {
                    registerUser(email, password, username)
                }
            }

            tvLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }
    }

    private fun registerUser(email: String, password: String, username: String) {
        binding.btnRegister.isEnabled = false
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.sendEmailVerification()

                // Menyimpan data pengguna ke Realtime Database
                val userId = user?.uid
                if (userId != null) {
                    val database = FirebaseDatabase.getInstance("https://project-belens-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    val userRef = database.getReference("users").child(userId)

                    // Menyimpan data pengguna seperti username, email
                    val userData = User(username, email)
                    userRef.setValue(userData).addOnCompleteListener { dbTask ->
                        if (dbTask.isSuccessful) {
                            val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                            val editor = sharedPref.edit()
                            editor.putString("userId", userId)
                            editor.putString("username", username)
                            editor.putString("email", email)
                            editor.apply()

                            Toast.makeText(this, "Registration successful, please verify your email", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
            }
            binding.btnRegister.isEnabled = true
        }
    }
}

// Model class untuk User
data class User(val username: String, val email: String)
