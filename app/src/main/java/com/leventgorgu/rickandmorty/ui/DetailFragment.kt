package com.leventgorgu.rickandmorty.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.leventgorgu.rickandmorty.viewmodel.DetailViewModel

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

        (activity as AppCompatActivity).supportActionBar?.show()

        val activity = activity as AppCompatActivity?
        val actionBar: ActionBar? = activity!!.supportActionBar
        actionBar!!.title = characterName

        val titleId = resources.getIdentifier("action_bar_title", "id", "android")
        val titleTextView = activity?.findViewById<TextView>(titleId)
        titleTextView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

        actionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            setDisplayHomeAsUpEnabled(true)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCharacter(characterId)
        observeToSubscribe()
    }

    private fun observeToSubscribe(){
        viewModel.character.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                updateUI(it)
            }
        })
    }


    private fun updateUI(character: Character){

        val options = RequestOptions()
            .placeholder(placeHolderProgressBar(binding.root.context))
            .error(R.mipmap.ic_launcher_round)

        Glide.with(binding.root.context)
            .setDefaultRequestOptions(options)
            .load(character[0].image)
            .into(binding.characterImageView)

        binding.characterStatusTextView.text = character[0].status
        binding.characterSpecyTextView.text = character[0].species
        binding.characterGenderTextView.text = character[0].gender
        binding.characterOriginTextView.text = character[0].origin.name
        binding.characterLocationTextView.text = character[0].location.name

        var episodeString = ""
        val episodes = character[0].episode
        for (i in episodes.indices){
            episodeString += episodes[i].substringAfterLast("/")
            if (i != episodes.lastIndex) {
                episodeString += ","
            }
        }
        binding.characterEpisodesTextView.text = episodeString

        binding.characterCreatedTextView.text = character[0].created

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