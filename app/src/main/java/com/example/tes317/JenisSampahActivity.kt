package com.example.tes317

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tes317.databinding.ActivityJenisSampahBinding

class JenisSampahActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJenisSampahBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJenisSampahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Access the strings from resources
        val deskripsiJenis = getString(R.string.deskripsi_jenis)
        val sampahAnorganik = getString(R.string.anorganik)
        val sampahOrganik = getString(R.string.organik)

        // Set the text to TextViews or use them as needed
        binding.textViewDeskripsi.text = deskripsiJenis
        binding.textViewAnorganik.text = sampahAnorganik
        binding.textViewOrganik.text = sampahOrganik
    }
}
