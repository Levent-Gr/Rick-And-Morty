package com.leventgorgu.rickandmorty.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leventgorgu.rickandmorty.R
import com.leventgorgu.rickandmorty.databinding.LocationRowBinding
import com.leventgorgu.rickandmorty.model.location.Result

class LocationRecyclerAdapter(var resultList:List<Result>):RecyclerView.Adapter<LocationRecyclerAdapter.LocationViewHolder>() {

    class LocationViewHolder(val binding: LocationRowBinding):RecyclerView.ViewHolder(binding.root)

    private var selectedRowIndex = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = LocationRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.binding.locationTextView.text = resultList[position].name

        if (selectedRowIndex == holder.adapterPosition) {
            holder.binding.locationRowFrameLayout.setBackgroundResource(R.drawable.border)
        } else {
            holder.binding.locationRowFrameLayout.setBackgroundResource(R.drawable.border_selected)
        }

        holder.itemView.setOnClickListener {
            selectedRowIndex = holder.adapterPosition
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return resultList.size
    }
}