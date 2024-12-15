package com.example.tes317

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BankSampahDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bankSampah: BankSampahEntity)

    @Query("SELECT * FROM bank_sampah")
    fun getAllBankSampah(): LiveData<List<BankSampahEntity>>
}

