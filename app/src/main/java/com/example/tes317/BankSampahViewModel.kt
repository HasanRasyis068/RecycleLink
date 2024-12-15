package com.example.tes317

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BankSampahViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).bankSampahDao()
    val allBankSampah: LiveData<List<BankSampahEntity>> = dao.getAllBankSampah()

    fun insert(bankSampah: BankSampahEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(bankSampah)
        }
    }
}
