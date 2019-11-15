package com.autohub.tutormodule.ui.dashboard.schedule

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.autohub.tutormodule.R

class ScheduleCalenderAdapter(private val list: List<String>) :
        RecyclerView.Adapter<ScheduleCalenderAdapter.ViewHolder>() {

    var selectedPosition = -1

    override fun onCreateViewHolder(viewGroup: ViewGroup, int: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_calendar_view, viewGroup, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val date = list[position].split(",")

        viewHolder.month.text = date[0]
        viewHolder.date.text = date[1]
        viewHolder.day.text = date[2]

        if (position == selectedPosition) {
            viewHolder.month.setTextColor(Color.WHITE)
            viewHolder.date.setTextColor(Color.WHITE)
            viewHolder.day.setTextColor(Color.WHITE)

            viewHolder.itemView.setBackgroundResource(R.drawable.bg_round_purple)
        } else {

            viewHolder.month.setTextColor(Color.BLACK)
            viewHolder.date.setTextColor(Color.BLACK)
            viewHolder.day.setTextColor(Color.BLACK)

            viewHolder.itemView.setBackgroundResource(R.drawable.white_bg_round_rect)
        }
        viewHolder.itemView.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val month: TextView = itemView.findViewById(R.id.month)
        val date: TextView = itemView.findViewById(R.id.date)
        val day: TextView = itemView.findViewById(R.id.day)
    }
}