package com.example.threeo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.threeo.R
import com.example.threeo.data.DetailData
import com.example.threeo.data.TimeData
import com.example.threeo.databinding.ColListBinding
import com.example.threeo.databinding.RowListBinding

class IfListAdapter (var items:ArrayList<DetailData>)
    : RecyclerView.Adapter<IfListAdapter.MyViewHolder>(){

    interface OnItemClickListener{
        fun OnItemClick(holder: MyViewHolder, view: View, data: DetailData, position: Int)
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

    fun settingPushBtn(position: Int){
        for(list in items){
            list.isPush = false
        }
        items[position].isPush = true
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(items[position].isPush)
            holder.binding.container.setBackgroundResource(R.drawable.push_box2)
        else
            holder.binding.container.setBackgroundResource(R.drawable.fill_box3)
        holder.binding.ifImg.setImageResource(items[position].img)
    }
}