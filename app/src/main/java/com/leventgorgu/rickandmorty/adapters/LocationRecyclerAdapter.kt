package com.leventgorgu.rickandmorty.adapters

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leventgorgu.rickandmorty.R
import com.leventgorgu.rickandmorty.databinding.CharacterRowBinding
import com.leventgorgu.rickandmorty.databinding.LocationRowBinding
import com.leventgorgu.rickandmorty.model.location.Result
import com.leventgorgu.rickandmorty.ui.FeedFragment

class LocationRecyclerAdapter(var resultList:List<Result>,private val listener:CharacterLocationClickListener):RecyclerView.Adapter<LocationRecyclerAdapter.LocationViewHolder>() {

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
            if (selectedRowIndex!=-1){
                val residents = resultList[selectedRowIndex].residents
                val characterIds = StringBuilder()
                for (i in residents.indices) {
                    characterIds.append(residents[i].substringAfterLast("/"))
                    if (i != residents.lastIndex && residents.size != 1) {
                        characterIds.append(",")
                    }
                }
                val characterIdsString = characterIds.toString()
                listener.onItemClick(characterIdsString)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return resultList.size
    }
}