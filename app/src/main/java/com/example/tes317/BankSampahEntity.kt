package com.example.tes317

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "bank_sampah")
data class BankSampahEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val alamat: String,
    val hargaLangganan: String,
    val fasilitas: String,
    val gambarUrl: String

) : Serializable

