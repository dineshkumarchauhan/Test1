package com.example.test1.screens.detail

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.MainActivity
import com.example.test1.R
import com.example.test1.models.ItemPhoto
import com.example.test1.screens.adapter.AdapterType
import com.example.test1.screens.adapter.SimpleAdapter
import com.example.test1.utils.phoneGridLandSpan
import com.example.test1.utils.phonePortLandSpan
import com.example.test1.utils.phonePortSpan
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Detail : Fragment() {
    lateinit var recyclerView: RecyclerView
    private val viewModel: DetailVM by viewModels()
    private var layoutManager: GridLayoutManager? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.detail, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        createMenu(menuHost)
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("data", ItemPhoto::class.java)
        } else {
            arguments?.getParcelable<ItemPhoto>("data")
        }

        if (MainActivity.orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = GridLayoutManager(requireContext(), phonePortLandSpan)
        } else if (MainActivity.orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager = GridLayoutManager(requireContext(), phoneGridLandSpan)
        }
        recyclerView.layoutManager = layoutManager
        viewModel.photosAdapter = SimpleAdapter(layoutManager, AdapterType.Detail)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = viewModel.photosAdapter

        data?.let { mainArr ->
            viewModel.photosAdapter.submitList(data.photoResult)
            viewModel.photosAdapter.notifyDataSetChanged()
        }
    }




    // Creating menus
    private fun createMenu(menuHost: MenuHost) {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.detail_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId){
                   R.id.item_list->{
                        if (layoutManager?.spanCount == phonePortSpan) {
                            if (MainActivity.orientation == Configuration.ORIENTATION_PORTRAIT){
                                layoutManager?.spanCount = phonePortLandSpan
                            } else if (MainActivity.orientation == Configuration.ORIENTATION_LANDSCAPE){
                                layoutManager?.spanCount = phoneGridLandSpan
                            }
                            menuItem.title = "list"
                        } else {
                            layoutManager?.spanCount = phonePortSpan
                            menuItem.title = "grid"
                        }
                       viewModel.photosAdapter.notifyDataSetChanged()
                        true
                    }
                    else->{
                        false
                    }
                }
            }
        },viewLifecycleOwner, Lifecycle.State.RESUMED)
    }



    // used for orientation changes
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        MainActivity.orientation = getResources().getConfiguration().orientation
        if (layoutManager?.spanCount == phonePortSpan) {
            layoutManager?.spanCount = phonePortSpan
        } else {
            if (MainActivity.orientation == Configuration.ORIENTATION_PORTRAIT){
                layoutManager?.spanCount = phonePortLandSpan
            } else if (MainActivity.orientation == Configuration.ORIENTATION_LANDSCAPE){
                layoutManager?.spanCount = phoneGridLandSpan
            } else{
                layoutManager?.spanCount = phonePortSpan
            }
        }
        viewModel.photosAdapter.notifyDataSetChanged()
    }
}