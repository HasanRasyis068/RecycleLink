package com.example.tes317

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tes317.databinding.ActivityMenuBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Realtime Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Initialize Location Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Set up username
        setUpUserName()

        // Set up location functionality
        setupLocationClickListener()

        // Menu navigation click listeners
        setupMenuClickListeners()
    }

    private fun setUpUserName() {
        // Ambil username dari SharedPreferences
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        var username = sharedPref.getString("username", "Pengguna") ?: "Pengguna" // Ambil dari SharedPreferences

        // Tampilkan username dari SharedPreferences di UI (sebelum mengambil dari Firebase)
        binding.root.findViewById<TextView>(R.id.usernameTextView).text = username

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            // Reference ke lokasi user di Realtime Database
            val userRef = database.child("users").child(user.uid)

            userRef.get()
                .addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        // Ambil username dari Firebase dan perbarui variabel username
                        username = dataSnapshot.child("username").getValue(String::class.java) ?: "Pengguna"

                        // Simpan ke SharedPreferences agar tidak perlu ambil dari Firebase lagi
                        with(sharedPref.edit()) {
                            putString("username", username)
                            apply()
                        }

                        // Update username di UI
                        binding.root.findViewById<TextView>(R.id.usernameTextView).text = username
                    } else {
                        binding.root.findViewById<TextView>(R.id.usernameTextView).text = "Pengguna"
                    }
                }
                .addOnFailureListener {
                    binding.root.findViewById<TextView>(R.id.usernameTextView).text = "Pengguna"
                }
        }
    }


    private fun setupLocationClickListener() {
        binding.tvCurrentLocation.setOnClickListener {
            checkLocationPermissionAndGetLocation()
        }
    }

    private fun checkLocationPermissionAndGetLocation() {
        // Check if location is enabled
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled) {
            // Prompt user to enable GPS
            showEnableGpsDialog()
            return
        }

        // Check location permissions
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // Get location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    // Format and display location
                    val locationText = "Lat: ${it.latitude}, Lng: ${it.longitude}"
                    binding.tvCurrentLocation.text = locationText
                } ?: run {
                    Toast.makeText(this, "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal mendapatkan lokasi", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showEnableGpsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Aktifkan GPS")
            .setMessage("Aplikasi membutuhkan GPS untuk menampilkan lokasi Anda. Aktifkan GPS sekarang?")
            .setPositiveButton("Pengaturan") { _, _ ->
                // Open location settings
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun setupMenuClickListeners() {
        // Existing menu navigation
        binding.cvInput.setOnClickListener {
            val intent = Intent(this@MenuActivity, MainActivity::class.java)
            startActivity(intent)
        }

        binding.cvKategori.setOnClickListener {
            val intent = Intent(this@MenuActivity, JenisSampahActivity::class.java)
            startActivity(intent)
        }

        binding.cvHistory.setOnClickListener {
            // Add navigation to history/saldo activity
            // For now, just a placeholder
            Toast.makeText(this, "Fitur dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get location
                checkLocationPermissionAndGetLocation()
            } else {
                Toast.makeText(this, "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
