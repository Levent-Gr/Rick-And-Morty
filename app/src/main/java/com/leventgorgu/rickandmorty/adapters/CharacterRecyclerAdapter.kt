package com.leventgorgu.rickandmorty.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.leventgorgu.rickandmorty.R
import com.leventgorgu.rickandmorty.databinding.CharacterRightRowBinding
import com.leventgorgu.rickandmorty.databinding.CharacterRowBinding
import com.leventgorgu.rickandmorty.model.character.Character
import com.leventgorgu.rickandmorty.model.character.CharacterItem
import com.leventgorgu.rickandmorty.ui.FeedFragment
import com.leventgorgu.rickandmorty.ui.FeedFragmentDirections
import com.leventgorgu.rickandmorty.utils.Util.VIEW_TYPE_LEFT
import com.leventgorgu.rickandmorty.utils.Util.VIEW_TYPE_RIGHT



class CharacterRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class CharacterViewRightHolder(val characterViewRightBinding: CharacterRightRowBinding):RecyclerView.ViewHolder(characterViewRightBinding.root)

    class CharacterViewLeftHolder(val characterViewLeftBinding: CharacterRowBinding):RecyclerView.ViewHolder(characterViewLeftBinding.root)

    private var character : Character = Character()

    override fun getItemViewType(position: Int): Int {
        return if (position %2==0) {
            VIEW_TYPE_LEFT
        } else {
            VIEW_TYPE_RIGHT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LEFT -> {
                val characterBinding = CharacterRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return CharacterViewLeftHolder(characterBinding)
            }
            else -> {
                val characterRightBinding = CharacterRightRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return CharacterViewRightHolder(characterRightBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CharacterViewLeftHolder -> bindCharacter(holder, position)
            is CharacterViewRightHolder -> bindCharacter(holder, position)
        }
        holder.itemView.setOnClickListener {
            navigateDetailFragment(position,holder)
        }
    }

    private fun bindCharacter(holder: RecyclerView.ViewHolder, position: Int) {
        if (character[position].id==0){
            holder.itemView.visibility = View.GONE
        }else {
            holder.itemView.visibility = View.VISIBLE

            val options = RequestOptions()
                .placeholder(placeHolderProgressBar(holder.itemView.context))
                .error(R.mipmap.ic_launcher_round)

            Glide.with(holder.itemView.context)
                .setDefaultRequestOptions(options)
                .load(character[position].image)
                .circleCrop()
                .into(holder.itemView.findViewById(R.id.characterImageView))

            val genderImageView = when (holder) {
                is CharacterViewLeftHolder -> holder.characterViewLeftBinding.characterGenderImageView
                is CharacterViewRightHolder -> holder.characterViewRightBinding.characterGenderImageView
                else -> null
            }

            if (character[position].gender!=null){
                genderImageView?.let {
                    it.setImageResource(
                        when (character[position].gender) {
                            "Female" -> R.drawable.female
                            "Male" -> R.drawable.male
                            "Genderless" -> R.drawable.genderless
                            else -> R.drawable.unknown
                        }
                    )
                }
            }


            holder.itemView.findViewById<TextView>(R.id.characterNameTextView).text = character[position].name
        }
    }

    private fun navigateDetailFragment(position:Int,holder:RecyclerView.ViewHolder) {
        val characterId = character[position].id.toString()
        val characterName = character[position].name
        val action = FeedFragmentDirections.actionFeedFragmentToDetailFragment(characterId,characterName)
        Navigation.findNavController(holder.itemView).navigate(action)
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