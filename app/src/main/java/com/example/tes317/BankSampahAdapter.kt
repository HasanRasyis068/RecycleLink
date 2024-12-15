package com.example.tes317

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tes317.databinding.ItemBanksampahBinding

class BankSampahAdapter(private val list: List<BankSampahEntity>) :
    RecyclerView.Adapter<BankSampahAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemBanksampahBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBanksampahBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bankSampah = list[position]
        holder.binding.tvNama.text = bankSampah.nama
        holder.binding.tvAlamat.text = bankSampah.alamat
        holder.binding.tvHargalangganan.text = bankSampah.hargaLangganan
        holder.binding.tvFasilitas.text = bankSampah.fasilitas

        Glide.with(holder.itemView.context)
            .load(bankSampah.gambarUrl)
            .placeholder(R.drawable.ic_placeholder)  // Gambar placeholder
            .into(holder.binding.ivImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java).apply {
                putExtra("BANK_SAMPAH", bankSampah)
            }
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = list.size
}
