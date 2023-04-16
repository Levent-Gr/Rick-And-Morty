package com.leventgorgu.rickandmorty.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.leventgorgu.rickandmorty.R
import com.leventgorgu.rickandmorty.databinding.FragmentDetailBinding
import com.leventgorgu.rickandmorty.model.character.Character
import com.leventgorgu.rickandmorty.model.character.CharacterItem
import com.leventgorgu.rickandmorty.utils.Status
import com.leventgorgu.rickandmorty.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private var characterId = ""
    private var characterName = ""
    private val viewModel : DetailViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            characterId = DetailFragmentArgs.fromBundle(it).characterId
            characterName = DetailFragmentArgs.fromBundle(it).characterName
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as AppCompatActivity
        val actionBar: ActionBar? = activity.supportActionBar
        actionBar!!.title = characterName
        actionBar?.setDisplayShowTitleEnabled(true)
        activity.supportActionBar!!.show()

        val titleId = resources.getIdentifier("action_bar_title", "id", "android")
        val titleTextView = activity.findViewById<TextView>(titleId)
        titleTextView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

        actionBar.apply {
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCharacter(characterId)
        observeToSubscribe()
    }

    private fun observeToSubscribe(){
        viewModel.character.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS->{
                    updateUI(it.data!!)
                }
                Status.ERROR->{
                    binding.characterProgressBar!!.visibility = View.VISIBLE
                    Toast.makeText(requireContext(),it.message ?: "Error", Toast.LENGTH_LONG).show()
                }
                Status.LOADING->{
                    binding.characterProgressBar!!.visibility = View.VISIBLE
                }
            }
        })
    }


    private fun updateUI(characterItem: CharacterItem){

        binding.characterProgressBar!!.visibility = View.INVISIBLE

        val options = RequestOptions()
            .placeholder(placeHolderProgressBar(binding.root.context))
            .error(R.mipmap.ic_launcher_round)


        Glide.with(binding.root.context)
            .setDefaultRequestOptions(options)
            .load(characterItem.image)
            .into(binding.characterImageView)

        binding.characterStatusTextView.text = characterItem.status
        binding.characterSpecyTextView.text = characterItem.species
        binding.characterGenderTextView.text = characterItem.gender
        binding.characterOriginTextView.text = characterItem.origin.name
        binding.characterLocationTextView.text = characterItem.location.name

        var episodeString = ""
        val episodes = characterItem.episode
        for (i in episodes.indices){
            episodeString += episodes[i].substringAfterLast("/")
            if (i != episodes.lastIndex) {
                episodeString += ","
            }
        }
        binding.characterEpisodesTextView.text = episodeString
        binding.characterCreatedTextView.text = characterItem.created
    }

    private fun placeHolderProgressBar(context: Context): CircularProgressDrawable {
        return CircularProgressDrawable(context).apply {
            strokeWidth = 8f
            centerRadius = 40f
            start()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}