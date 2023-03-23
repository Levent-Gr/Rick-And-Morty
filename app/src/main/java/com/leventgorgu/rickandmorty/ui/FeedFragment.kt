package com.leventgorgu.rickandmorty.ui

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.leventgorgu.rickandmorty.R

import com.leventgorgu.rickandmorty.adapters.CharacterRecyclerAdapter
import com.leventgorgu.rickandmorty.utils.CustomScrollListener
import com.leventgorgu.rickandmorty.adapters.LocationRecyclerAdapter
import com.leventgorgu.rickandmorty.databinding.FragmentFeedBinding
import com.leventgorgu.rickandmorty.model.location.Result
import com.leventgorgu.rickandmorty.utils.CharacterLocationListener
import com.leventgorgu.rickandmorty.viewmodel.FeedViewModel


class FeedFragment : Fragment(), CharacterLocationListener {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private val viewModel : FeedViewModel by viewModels()
    private lateinit var locationRecyclerAdapter:LocationRecyclerAdapter
    private lateinit var characterRecyclerAdapter: CharacterRecyclerAdapter
    private lateinit var customScrollListener : CustomScrollListener
    private var pageNumber  = "0"
    private lateinit var linearLayoutManagerForLocationRecyclerView :LinearLayoutManager
    private var fragmentState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.savePageNumber("1")
            viewModel.saveSelectedRow(0)
            fragmentState = true
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val activity = requireActivity() as AppCompatActivity
        val actionBar = activity.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setHomeAsUpIndicator(R.drawable.logo_mini)

        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationRecyclerAdapter = LocationRecyclerAdapter(arrayListOf(),this)
        linearLayoutManagerForLocationRecyclerView = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        binding.locationRecyclerView.layoutManager =linearLayoutManagerForLocationRecyclerView

        binding.locationRecyclerView.adapter = locationRecyclerAdapter
        binding.locationRecyclerView.setHasFixedSize(true)
        locationRecyclerAdapter.fragmentState = fragmentState

        characterRecyclerAdapter = CharacterRecyclerAdapter()
        binding.characterRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.characterRecyclerView.adapter = characterRecyclerAdapter
        binding.characterRecyclerView.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()
        observeToSubscribe()
        if (pageNumber.toInt()!=0 && pageNumber.toInt()<8 && fragmentState)
            viewModel.getLocations(pageNumber)

        customScrollListener = CustomScrollListener(linearLayoutManagerForLocationRecyclerView){
            if (pageNumber.toInt()!=0 && pageNumber.toInt()<8){
                updateLocationData()
            } else{
                locationRecyclerAdapter.isLoading = false
                val lastResult = locationRecyclerAdapter.resultList.last()
                if (lastResult.id==0){
                    locationRecyclerAdapter.resultList.removeLast()
                    locationRecyclerAdapter.notifyDataSetChanged()
                }
                customScrollListener.setIsLoading(true)
            }
        }

        binding.locationRecyclerView.addOnScrollListener(customScrollListener)
    }

    private fun updateLocationData(){
        locationRecyclerAdapter.isLoading = true
        locationRecyclerAdapter.notifyDataSetChanged()
        Handler().postDelayed({
            viewModel.getLocations(pageNumber)
            customScrollListener.setIsLoading(false)
            locationRecyclerAdapter.notifyDataSetChanged()
        }, 2000)

    }

    private fun observeToSubscribe(){
        viewModel.location.observe(viewLifecycleOwner, Observer {
            if (it !=null){
                locationRecyclerAdapter.resultList = ArrayList(it.results)
                if (pageNumber.toInt()!=0 && pageNumber.toInt()<8){
                    val nullResult=Result("","",0,"", listOf(),"","")
                    locationRecyclerAdapter.resultList.add(nullResult)
                }
                val page = it.info.next.substringAfterLast("=")
                if (page != ""){
                    pageNumber=page
                    viewModel.savePageNumber(page)
                }
                locationRecyclerAdapter.notifyDataSetChanged()
            }
        })
        viewModel.character.observe(viewLifecycleOwner, Observer {
            if (it !=null){
                characterRecyclerAdapter.setCharacter(it)
                characterRecyclerAdapter.notifyDataSetChanged()
            }
        })
        viewModel.selectedRow.observe(viewLifecycleOwner, Observer {
            locationRecyclerAdapter.selectedRowIndex = it
            if (characterRecyclerAdapter.itemCount!=0)
                locationRecyclerAdapter.notifyDataSetChanged()
        })
        viewModel.pageNumber.observe(viewLifecycleOwner, Observer {
            if (it != ""){
                pageNumber = it
            }
        })
        viewModel.fragmentState.observe(viewLifecycleOwner, Observer {
            fragmentState = it
        })
    }

    override fun onItemClick(characterIds: String,selectedRowIndex:Int) {
        if (characterIds.contains(","))
            viewModel.getCharacters(characterIds)
        else
            viewModel.getCharacter(characterIds)

        viewModel.saveSelectedRow(selectedRowIndex)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("fragmentState","fragmentState")
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveFragmentState(false)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}