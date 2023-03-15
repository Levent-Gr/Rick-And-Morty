package com.leventgorgu.rickandmorty.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.leventgorgu.rickandmorty.R
import com.leventgorgu.rickandmorty.databinding.CharacterRowBinding
import com.leventgorgu.rickandmorty.model.character.Character
import com.leventgorgu.rickandmorty.model.character.CharacterItem

class CharacterRecyclerAdapter : RecyclerView.Adapter<CharacterRecyclerAdapter.CharacterViewHolder>() {

    class CharacterViewHolder(val binding: CharacterRowBinding):RecyclerView.ViewHolder(binding.root)

    private var character : Character = Character()
    var rowPositions = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val characterBinding = CharacterRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CharacterViewHolder(characterBinding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {

        val options = RequestOptions()
            .placeholder(placeHolderProgressBar(holder.itemView.context))
            .error(R.mipmap.ic_launcher_round)

        Glide.with(holder.itemView.context)
            .setDefaultRequestOptions(options)
            .load(character[position].image)
            .into(holder.binding.characterImageView)


        holder.binding.characterNameTextView.text = character[position].name
        if (character[position].gender=="Female")
            holder.binding.characterGenderImageView.setImageResource(R.drawable.female)
        else if(character[position].gender=="Male")
            holder.binding.characterGenderImageView.setImageResource(R.drawable.male)
        else if(character[position].gender=="Genderless")
            holder.binding.characterGenderImageView.setImageResource(R.drawable.genderless)
        else if (character[position].gender=="unknown")
            holder.binding.characterGenderImageView.setImageResource(R.drawable.unknown)



        val characterImageViewParams = holder.binding.characterImageView.layoutParams as ConstraintLayout.LayoutParams
        val characterNameTextViewParams = holder.binding.characterNameTextView.layoutParams as ConstraintLayout.LayoutParams
        if (rowPositions==0) {
            characterImageViewParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            characterImageViewParams.horizontalBias = 1F

            characterNameTextViewParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            characterNameTextViewParams.horizontalBias = 0F

            rowPositions =1
        } else if (rowPositions==1) {
            characterImageViewParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            characterImageViewParams.horizontalBias = 0F

            characterNameTextViewParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            characterNameTextViewParams.horizontalBias = 1F

            rowPositions = 0
        }

        holder.binding.characterImageView.layoutParams = characterImageViewParams
        holder.binding.characterNameTextView.layoutParams = characterNameTextViewParams
    }

    override fun getItemCount(): Int {
        return character.size
    }

    fun setCharacter(characterItem: ArrayList<CharacterItem>){
        character.clear()
        character.addAll(characterItem)
    }

    private fun placeHolderProgressBar(context: Context):CircularProgressDrawable{
        return CircularProgressDrawable(context).apply {
            strokeWidth = 8f
            centerRadius = 40f
            start()
        }
    }
}