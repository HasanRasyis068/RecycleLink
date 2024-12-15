package com.example.tes317

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tes317.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: BankSampahViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val dummyData = listOf(
            BankSampahEntity(1, "Bank Sampah Gemah Ripah", "Alamat: Jl. Urip Sumoharjo Dk, Jl. Badegan No.RT 12, Bejen, Bantul, Kec. Bantul, Kabupaten Bantul, Daerah Istimewa Yogyakarta 55711", "Bulanan: Rp. 25.000, Tahunan: Rp. 250.000", "Angkut Cepat", "https://storage.googleapis.com/banksampah/gemah_ripah.png"),
            BankSampahEntity(2, "Bank Sampah Surolaras", "Alamat: Jl. Suronatan No.Blok NG-2/51, Notoprajan, Ngampilan, Kota Yogyakarta, Daerah Istimewa Yogyakarta 55262", "Bulanan: Rp. 20.000, Tahunan: Rp. 220.000", "Angkut & Sortir", "https://storage.googleapis.com/banksampah/surolaras.png"),
            BankSampahEntity(3, "Bank Sampah Bedeng Berseri", "Alamat: Jl. Bumijo Kulon No.I, Bumijo, Kec. Jetis, Kota Yogyakarta, Daerah Istimewa Yogyakarta 55231", "Bulanan: Rp. 15.000, Tahunan: Rp. 150.000", "Angkut Cepat & Sortir", "https://storage.googleapis.com/banksampah/bank_sampah3.png"),
            BankSampahEntity(4, "Bank Sampah Simul 5", "Alamat: Jl. Sidomulyo No.345, Bener, Kec. Tegalrejo, Kota Yogyakarta, Daerah Istimewa Yogyakarta 55243", "Bulanan: Rp. 20.000, Tahunan: Rp. 200.000", "Angkut Cepat & Sortir", "https://storage.googleapis.com/banksampah/simul5.png")
        )

        val adapter = BankSampahAdapter(dummyData)
        binding.rvBankSampah.layoutManager = LinearLayoutManager(this)
        binding.rvBankSampah.adapter = adapter

        // Menambahkan data ke database jika ingin persist
        dummyData.forEach {
            viewModel.insert(it)
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        // Daftar lokasi bank sampah (latitude, longitude, dan nama)
        val locations = listOf(
            LatLng(-7.89353,110.3252617) to "Bank Sampah Gemah Ripah",
            LatLng(-7.8027437,110.2072919) to "Bank Sampah Surolaras",
            LatLng(-7.7845715,110.3549812) to "Bank Sampah Bedeng Berseri",
            LatLng(-7.7791859,110.3533394) to "Bank Sampah Simul 5"
        )

        // Menambahkan marker untuk setiap lokasi dalam daftar
        for (location in locations) {
            googleMap.addMarker(MarkerOptions().position(location.first).title(location.second))
        }

        // Menyusun peta untuk menampilkan semua lokasi dengan zoom yang sesuai
        val bounds = LatLngBounds.Builder()
        for (location in locations) {
            bounds.include(location.first)
        }
        val padding = 100 // Padding peta
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds.build(), padding)
        googleMap.moveCamera(cameraUpdate)
    }

}
