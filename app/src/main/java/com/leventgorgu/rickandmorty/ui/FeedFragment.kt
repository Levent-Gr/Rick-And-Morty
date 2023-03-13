package com.leventgorgu.rickandmorty.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.leventgorgu.rickandmorty.adapters.LocationRecyclerAdapter
import com.leventgorgu.rickandmorty.databinding.FragmentFeedBinding
import com.leventgorgu.rickandmorty.viewmodel.FeedViewModel


class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private val viewModel : FeedViewModel by viewModels()
    private lateinit var locationRecyclerAdapter:LocationRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationRecyclerAdapter = LocationRecyclerAdapter(listOf())
        binding.locationRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.locationRecyclerView.adapter = locationRecyclerAdapter
        binding.locationRecyclerView.setHasFixedSize(true)
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
    }


}