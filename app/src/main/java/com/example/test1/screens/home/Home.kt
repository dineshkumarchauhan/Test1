package com.example.test1.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.demo.model.Result
import com.demo.networking.ErrorHandler
import com.example.test1.R
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class Home : Fragment() {
    private val viewModel: HomeVM by viewModels()
    lateinit var recyclerView: RecyclerView
    lateinit var txtMsg: TextView

    var items : ArrayList<Result> = ArrayList()
    var itemMain : ArrayList<Result> ?= ArrayList()
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
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = viewModel.photosAdapter


        viewModel.getProducts() {
            if (this != null) {
                itemMain = this?.results!!
                viewModel.photosAdapter.submitList(itemMain)
                if(itemMain?.isEmpty() == true){
                    txtMsg.visibility = View.VISIBLE
                }else{
                    txtMsg.visibility = View.GONE
                }
            }
            viewModel.photosAdapter.notifyDataSetChanged()
        }


    }


    // Creating menus
    private fun createMenu(menuHost: MenuHost) {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_menu,menu)
                var search = (menu?.findItem(R.id.search)?.actionView as SearchView)
                search.setOnQueryTextListener(object : OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if(newText.toString().isEmpty()){
                            viewModel.photosAdapter.submitList(itemMain)
                            if(itemMain?.isEmpty() == true){
                                txtMsg.visibility = View.VISIBLE
                            }else{
                                txtMsg.visibility = View.GONE
                            }
                        }else{
                            items = itemMain?.filter { s -> s.name.lowercase().contains(newText.toString().lowercase()) } as ArrayList<Result>
                            viewModel.photosAdapter.submitList(items)
                            if(items.isEmpty() == true){
                                txtMsg.visibility = View.VISIBLE
                            }else{
                                txtMsg.visibility = View.GONE
                            }
                        }
                        viewModel.photosAdapter.notifyDataSetChanged()
                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId){
                    R.id.search->{
                        true
                    }
                    else->{
                        false
                    }
                }
            }
        },viewLifecycleOwner, Lifecycle.State.RESUMED)
    }




}