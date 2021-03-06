package com.example.threeo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.threeo.data.TimeData
import com.example.threeo.databinding.RowListBinding

//리스트를 관리하는 어뎁터
class AppListAdapter (var items:ArrayList<TimeData>)
    : RecyclerView.Adapter<AppListAdapter.MyViewHolder>(){

    interface OnItemClickListener{
        fun OnItemClick(holder: MyViewHolder, view: View, data: TimeData, position: Int)
    }

    var itemClickListener:OnItemClickListener?= null

    inner class MyViewHolder(val binding: RowListBinding): RecyclerView.ViewHolder(binding.root) {
        init{
            binding.show.setOnClickListener {
                itemClickListener?.OnItemClick(this, it, items[adapterPosition], adapterPosition)
            }
        }
    }

    fun clearData(){
        items.clear()
        notifyDataSetChanged()
    }

    fun addData(data: TimeData){
        items.add(data)
        notifyDataSetChanged()
    }

    fun getData(): ArrayList<TimeData>{
        return items
    }

    fun deDuplication(){
        var distList = items.distinct()
        items.clear()
        items.addAll(distList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = RowListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.img.setImageDrawable(items[position].img)
        holder.binding.appName.text = items[position].appName
        holder.binding.allTime.text = "${items[position].time.toLong()/3600000}시간 ${(items[position].time.toLong()%3600000)/60000}분"
    }
}