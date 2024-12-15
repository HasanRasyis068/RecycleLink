package com.example.tes317

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.*

class BayarSampahActivity : AppCompatActivity() {

    private lateinit var inputNama: EditText
    private lateinit var inputAlamat: EditText
    private lateinit var spKategori: Spinner
    private lateinit var inputBerat: EditText
    private lateinit var inputHarga: EditText
    private lateinit var btnBayar: Button

    private lateinit var kategoriSampah: Array<String>
    private lateinit var hargaPerKilo: Array<String>

    private var selectedKategori: String = ""
    private var hargaPerKg: Int = 0
    private var berat: Int = 0
    private var totalHarga: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bayar_sampah)

        // Inisialisasi UI
        inputNama = findViewById(R.id.inputNama)
        inputAlamat = findViewById(R.id.inputAlamat)
        spKategori = findViewById(R.id.spKategori)
        inputBerat = findViewById(R.id.inputBerat)
        inputHarga = findViewById(R.id.inputHarga)
        btnBayar = findViewById(R.id.btnBayar)

        // Setup Spinner dan Listener
        setupKategoriSpinner()

        // Listener untuk perubahan berat
        inputBerat.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calculateTotalHarga()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Logika tombol bayar
        btnBayar.setOnClickListener {
            handleBayar()
        }
    }

    private fun setupKategoriSpinner() {
        // Dummy data untuk kategori sampah dan harga
        kategoriSampah = arrayOf("Plastik", "Kertas", "Logam")
        hargaPerKilo = arrayOf("2000", "3000", "5000")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            kategoriSampah
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spKategori.adapter = adapter

        spKategori.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedKategori = kategoriSampah[position]
                hargaPerKg = hargaPerKilo[position].toInt()
                calculateTotalHarga()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun calculateTotalHarga() {
        val beratText = inputBerat.text.toString()
        berat = if (beratText.isNotEmpty() && beratText.toIntOrNull() != null) beratText.toInt() else 0
        totalHarga = hargaPerKg * berat

        // Format total harga ke dalam mata uang rupiah
        val formattedPrice = if (totalHarga > 0) {
            NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(totalHarga)
        } else {
            "Rp 0"
        }
        inputHarga.setText(formattedPrice)
    }

    private fun handleBayar() {
        val nama = inputNama.text.toString()
        val alamat = inputAlamat.text.toString()

        if (nama.isEmpty() || alamat.isEmpty() || berat == 0 || totalHarga == 0) {
            Toast.makeText(this, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            return
        }

        // Tampilkan informasi pembayaran
        Toast.makeText(
            this,
            "Pembayaran berhasil!\nNama: $nama\nKategori: $selectedKategori\nTotal: Rp$totalHarga",
            Toast.LENGTH_LONG
        ).show()

        // Reset form
        resetForm()
    }

    private fun resetForm() {
        inputNama.text.clear()
        inputAlamat.text.clear()
        inputBerat.text.clear()
        inputHarga.text.clear()
        spKategori.setSelection(0)
    }
}
