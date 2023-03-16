package com.leventgorgu.rickandmorty.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.leventgorgu.rickandmorty.adapters.CharacterLocationClickListener
import com.leventgorgu.rickandmorty.adapters.CharacterRecyclerAdapter
import com.leventgorgu.rickandmorty.adapters.LocationRecyclerAdapter
import com.leventgorgu.rickandmorty.databinding.FragmentFeedBinding
import com.leventgorgu.rickandmorty.model.character.Character
import com.leventgorgu.rickandmorty.model.character.CharacterItem
import com.leventgorgu.rickandmorty.model.character.Location
import com.leventgorgu.rickandmorty.model.character.Origin
import com.leventgorgu.rickandmorty.viewmodel.FeedViewModel


class FeedFragment : Fragment(),CharacterLocationClickListener {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private val viewModel : FeedViewModel by viewModels()
    private lateinit var locationRecyclerAdapter:LocationRecyclerAdapter
    private lateinit var characterRecyclerAdapter: CharacterRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()

        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationRecyclerAdapter = LocationRecyclerAdapter(listOf(),this)
        binding.locationRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.locationRecyclerView.adapter = locationRecyclerAdapter
        binding.locationRecyclerView.setHasFixedSize(true)

        characterRecyclerAdapter = CharacterRecyclerAdapter()
        binding.characterRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.characterRecyclerView.adapter = characterRecyclerAdapter
        binding.characterRecyclerView.setHasFixedSize(true)

    }

    override fun onResume() {
        super.onResume()
        viewModel.getLocations()
        observeToSubscribe()
    }

    private fun observeToSubscribe(){
        viewModel.location.observe(viewLifecycleOwner, Observer {
            if (it !=null){
                locationRecyclerAdapter.resultList =  it.results
                locationRecyclerAdapter.notifyDataSetChanged()
            }
        })
        viewModel.character.observe(viewLifecycleOwner, Observer {
            if (it !=null){
                characterRecyclerAdapter.setCharacter(it)
                characterRecyclerAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onItemClick(characterIds: String) {
        if (characterIds.contains(","))
            viewModel.getCharacters(characterIds)
        else
            viewModel.getCharacter(characterIds)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}