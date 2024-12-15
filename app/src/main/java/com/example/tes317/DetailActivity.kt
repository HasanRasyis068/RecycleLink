package com.example.tes317

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tes317.databinding.ActivityDetailBinding
import java.util.*

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    // SharedPreferences untuk menyimpan status langganan
    private val sharedPrefs by lazy {
        getSharedPreferences("BANK_SAMPAH_PREFS", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menerima data dari Intent
        val bankSampah = intent.getSerializableExtra("BANK_SAMPAH") as BankSampahEntity

        // Cek status langganan dan update UI
        val isSubscribed = sharedPrefs.getBoolean(bankSampah.nama, false)
        val subscriptionType = sharedPrefs.getString("${bankSampah.nama}_type", "")
        updateSubscriptionStatus(isSubscribed, subscriptionType)

        // Menampilkan data di layout
        binding.apply {
            tvNama.text = bankSampah.nama
            tvAlamat.text = bankSampah.alamat
            tvHargalangganan.text = bankSampah.hargaLangganan
            tvFasilitas.text = bankSampah.fasilitas
            tvHargaSampah.text = "Harga Sampah per kg: Rp. 5.000"

            Glide.with(this@DetailActivity)
                .load(bankSampah.gambarUrl)
                .placeholder(R.drawable.ic_placeholder)
                .into(ivImage)

            // Tombol Bayar Sampah
            btnBayarSampah.setOnClickListener {
                val intent = Intent(this@DetailActivity, BayarSampahActivity::class.java)
                startActivity(intent)
            }

            // Tombol Langganan Bulanan
            btnLanggananBulanan.setOnClickListener {
                handleSubscription(bankSampah, "Bulanan")
            }

            // Tombol Langganan Tahunan
            btnLanggananTahunan.setOnClickListener {
                handleSubscription(bankSampah, "Tahunan")
            }
        }
    }

    // Fungsi untuk menangani langganan
    private fun handleSubscription(bankSampah: BankSampahEntity, type: String) {
        val currentType = sharedPrefs.getString("${bankSampah.nama}_type", "")
        val lastSubscribed = sharedPrefs.getLong("${bankSampah.nama}_timestamp", 0L)
        val currentTime = System.currentTimeMillis()

        // Jika sudah langganan dan memilih jenis yang berbeda
        if (currentType != null && currentType != type && currentTime - lastSubscribed < 30 * 24 * 60 * 60 * 1000) {
            // Jika langganan belum 30 hari
            AlertDialog.Builder(this).apply {
                setTitle("Ganti Langganan")
                setMessage("Anda sudah berlangganan $currentType. Apakah Anda yakin ingin mengganti langganan ke $type?")
                setPositiveButton("Ya") { _, _ ->
                    updateSubscription(bankSampah, type)
                }
                setNegativeButton("Tidak", null)
            }.show()
        } else {
            // Jika belum berlangganan atau sudah lebih dari 30 hari
            updateSubscription(bankSampah, type)
        }
    }

    // Fungsi untuk memperbarui status langganan
    private fun updateSubscription(bankSampah: BankSampahEntity, type: String) {
        sharedPrefs.edit()
            .putBoolean(bankSampah.nama, true)
            .putString("${bankSampah.nama}_type", type)
            .putLong("${bankSampah.nama}_timestamp", System.currentTimeMillis())
            .apply()

        // Update UI tombol
        updateSubscriptionStatus(true, type)
        Toast.makeText(this, "Berhasil berlangganan $type di ${bankSampah.nama}!", Toast.LENGTH_SHORT).show()
    }

    // Fungsi untuk mengupdate status tombol Langganan
    private fun updateSubscriptionStatus(isSubscribed: Boolean, type: String?) {
        binding.apply {
            if (isSubscribed) {
                if (type == "Bulanan") {
                    // Tombol Bulanan berubah menjadi abu-abu dan teks "Sudah Langganan"
                    btnLanggananBulanan.text = "Sudah Langganan Bulanan"
                    btnLanggananBulanan.setBackgroundColor(getColor(R.color.button_disabled_gray))
                    btnLanggananBulanan.isEnabled = false

                    // Tombol Tahunan tetap tidak terpengaruh
                    btnLanggananTahunan.setBackgroundColor(getColor(R.color.purple_700))
                    btnLanggananTahunan.isEnabled = true
                } else {
                    // Tombol Tahunan berubah menjadi abu-abu dan teks "Sudah Langganan"
                    btnLanggananTahunan.text = "Sudah Langganan Tahunan"
                    btnLanggananTahunan.setBackgroundColor(getColor(R.color.button_disabled_gray))
                    btnLanggananTahunan.isEnabled = false

                    // Tombol Bulanan tetap tidak terpengaruh
                    btnLanggananBulanan.setBackgroundColor(getColor(R.color.purple_700))
                    btnLanggananBulanan.isEnabled = true
                }
            } else {
                // Reset warna tombol jika belum berlangganan
                btnLanggananBulanan.setBackgroundColor(getColor(R.color.purple_700))
                btnLanggananTahunan.setBackgroundColor(getColor(R.color.purple_700))

                // Reset teks tombol
                btnLanggananBulanan.text = "Langganan Bulanan"
                btnLanggananTahunan.text = "Langganan Tahunan"

                // Tombol bisa diklik
                btnLanggananBulanan.isEnabled = true
                btnLanggananTahunan.isEnabled = true
            }
        }
    }
}
