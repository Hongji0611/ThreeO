package com.example.threeo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.threeo.data.TimeData
import com.example.threeo.databinding.ColListBinding
import com.example.threeo.databinding.RowListBinding

class IfListAdapter (var items:ArrayList<String>)
    : RecyclerView.Adapter<IfListAdapter.MyViewHolder>(){

    interface OnItemClickListener{
        fun OnItemClick(holder: MyViewHolder, view: View, data: String, position: Int)
    }

    var itemClickListener:OnItemClickListener?= null

    inner class MyViewHolder(val binding: ColListBinding): RecyclerView.ViewHolder(binding.root) {
        init{
            binding.root.setOnClickListener {
                itemClickListener?.OnItemClick(this, it, items[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ColListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.ifBtn.text = items[position]
    }
}