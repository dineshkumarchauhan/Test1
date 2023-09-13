package com.example.test1.screens.home

import android.Manifest
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test1.MainActivity
import com.example.test1.R
import com.example.test1.photoDB.ErrorHandler
import com.example.test1.screens.adapter.AdapterType
import com.example.test1.screens.adapter.SimpleAdapter
import com.example.test1.utils.phoneGridLandSpan
import com.example.test1.utils.phonePortLandSpan
import com.example.test1.utils.phonePortSpan
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class Home : Fragment() {
    private val viewModel: HomeVM by viewModels()
    lateinit var recyclerView: RecyclerView
    lateinit var txtMsg: TextView

    private var layoutManager: GridLayoutManager? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        txtMsg = view.findViewById(R.id.txtMsg)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        createMenu(menuHost)
        checkPermission()
    }


    private fun createMenu(menuHost: MenuHost) {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_menu,menu)
                var search = (menu?.findItem(R.id.search)?.actionView as SearchView)
                search.setOnQueryTextListener(object : OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        viewModel.getAllPhotos(search.query)
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId){
                    R.id.search->{
                        true
                    } R.id.item_list->{
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


    var requestPermission =
        registerForActivityResult(RequestMultiplePermissions()) { permissions ->
            permissions.forEach { actionMap ->
                when (actionMap.key) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        Manifest.permission.READ_MEDIA_IMAGES
                    else
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    -> {
                        if (actionMap.value) {
                            Log.e("DEBUG", "permission granted")
                            loadData()
                        } else {
                            Log.e("DEBUG", "permission denied")
                            var intent : Intent ? = Intent()
                            intent?.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            val uri = Uri.fromParts("package", requireContext()!!.packageName, null)
                            intent?.setData(uri);
                            resultLauncher.launch(intent)

                        }
                    }
                }
            }
        }

    private fun loadData() {
        if (MainActivity.orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = GridLayoutManager(requireContext(), phonePortLandSpan)
        } else if (MainActivity.orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager = GridLayoutManager(requireContext(), phoneGridLandSpan)
        }

        recyclerView.layoutManager = layoutManager
        viewModel.photosAdapter = SimpleAdapter(layoutManager, AdapterType.Home)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = viewModel.photosAdapter

        viewModel.notesLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ErrorHandler.Success -> {
                    viewModel.photosAdapter.submitList(it.data)
                    txtMsg.visibility = View.GONE
                }
                is ErrorHandler.Error -> {
                    viewModel.photosAdapter.submitList(it.data)
                    txtMsg.text = it.message
                    txtMsg.visibility = View.VISIBLE
                }
                is ErrorHandler.Loading -> {
                    txtMsg.visibility = View.GONE
                }
            }
            viewModel.photosAdapter.notifyDataSetChanged()
        })
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())  { result ->
        checkPermission()
    }

    private fun checkPermission() {
        requestPermission.launch(
            arrayOf(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_IMAGES
            else
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
    }

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