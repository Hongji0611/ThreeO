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
    private var standardType = 0

    inner class MyViewHolder(val binding: RowListBinding): RecyclerView.ViewHolder(binding.root) {
        init{
            binding.show.setOnClickListener {
                itemClickListener?.OnItemClick(this, it, items[adapterPosition], adapterPosition)
            }
        }
    }

    fun setData(newData: ArrayList<TimeData>){
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    fun setStandardType(_type : Int){
        standardType = _type
        notifyDataSetChanged()
    }

    fun getCustomTime(time: Long):Int{
        val hour = time/3600000
        val min = time/60000 - hour*60
        val totalMin = (hour*60 + min)

        var returnTime = 0

        when(standardType){
            0-> returnTime = (hour / 6).toInt()
            1-> returnTime = (totalMin * 9.6).toInt()
            2-> returnTime = (totalMin / 500).toInt()
            3-> returnTime = (totalMin / 3).toInt()
            4-> returnTime = (hour * 8720).toInt()
            5-> returnTime = (hour / 35).toInt()
            6-> returnTime = (hour / 2).toInt()
            7-> returnTime = (totalMin / 1.3).toInt()
            8-> returnTime = (totalMin * 3.6).toInt()
            9-> returnTime = (totalMin / 30).toInt()
            10-> returnTime = (hour / 2).toInt()
            11-> returnTime = (totalMin * 0.5).toInt()
        }

        return returnTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = RowListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.apply {
            img.setImageDrawable(items[position].img)
            appName.text = items[position].appName

            val hour = items[position].time.toLong()/3600000
            val min = items[position].time.toLong()/60000 - hour*60
            val totalMin = (hour*60 + min)

            when(standardType){
                0-> allTime.text = "${(hour / 6).toInt()}권"
                1-> allTime.text = "${(totalMin * 9.6).toInt()}Kcal"
                2-> allTime.text = "${(totalMin / 500).toInt()}번"
                3-> allTime.text = "${(totalMin / 3).toInt()}곡"
                4-> allTime.text = "${(hour * 8720).toInt()}원"
                5-> allTime.text = "${(hour / 35).toInt()}m"
                6-> allTime.text = "${(hour / 2).toInt()}편"
                7-> allTime.text = "${(totalMin / 1.3).toInt()}개"
                8-> allTime.text = "${(totalMin * 3.6).toInt()}g"
                9-> allTime.text = "${(totalMin / 30).toInt()}번"
                10-> allTime.text = "${(hour / 2).toInt()}주차"
                11-> allTime.text = "${(totalMin * 0.5).toInt()}개"
            }

            if(items[position].isAlarm)
                isAlarmImg.visibility = View.VISIBLE
            else
                isAlarmImg.visibility = View.GONE
        }
    }
}