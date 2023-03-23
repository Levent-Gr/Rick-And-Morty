package com.leventgorgu.rickandmorty.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leventgorgu.rickandmorty.R
import com.leventgorgu.rickandmorty.databinding.ItemLoadingBinding
import com.leventgorgu.rickandmorty.databinding.LocationRowBinding
import com.leventgorgu.rickandmorty.model.location.Result
import com.leventgorgu.rickandmorty.utils.CharacterLocationListener
import com.leventgorgu.rickandmorty.utils.Util.VIEW_TYPE_LOADING
import com.leventgorgu.rickandmorty.utils.Util.VIEW_TYPE_LOCATION

class LocationRecyclerAdapter(var resultList:ArrayList<Result>,private val listener: CharacterLocationListener):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class LocationViewHolder(val locationRowBinding: LocationRowBinding):RecyclerView.ViewHolder(locationRowBinding.root)

    class LoadingViewHolder(itemLoadingBinding: ItemLoadingBinding) : RecyclerView.ViewHolder(itemLoadingBinding.root)

    var fragmentState= false
    var selectedRowIndex = -1
    var isLoading = false


    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount -1 && isLoading) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_LOCATION
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LOADING -> {
                val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingViewHolder(itemLoadingBinding)
            }
            else -> {
                val binding = LocationRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return LocationViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LocationViewHolder -> {
                holder.locationRowBinding.locationTextView.text = resultList[position].name
                if (selectedRowIndex == holder.adapterPosition) {
                    holder.locationRowBinding.locationRowCardView.setCardBackgroundColor(Color.LTGRAY)
                    if (selectedRowIndex==0&&fragmentState){
                        getCharactersFromLocation(holder)
                        fragmentState = false
                    }
                } else {
                    holder.locationRowBinding.locationRowCardView.setCardBackgroundColor(Color.WHITE)
                }
                holder.itemView.setOnClickListener {
                    getCharactersFromLocation(holder)
                }
            }
        }
    }


    private fun getCharactersFromLocation(holder: RecyclerView.ViewHolder){
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
            listener.onItemClick(characterIdsString,selectedRowIndex)
        }
    }


    override fun getItemCount(): Int {
        return resultList.size
    }
}