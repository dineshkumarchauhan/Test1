package com.example.test1.screens.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.genericAdapter.GenericAdapter
import com.demo.model.ItemProducts
import com.demo.model.Result
import com.demo.networking.ApiInterface
import com.demo.networking.CallHandler
import com.demo.networking.ErrorHandler
import com.demo.networking.Repository
import com.example.test1.databinding.ListItemBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import retrofit2.Response

@HiltViewModel
class HomeVM @Inject constructor( private val foodRepository: Repository): ViewModel() {

    val photosAdapter = object : GenericAdapter<ListItemBinding, Result>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = ListItemBinding.inflate(inflater, parent, false)

        override fun onBindHolder(binding: ListItemBinding, dataClass: Result, position: Int) {
            binding.txtTitle.text = dataClass.name
            binding.txtDesc.text = dataClass.vicinity

            Picasso.get().load(if (dataClass.photos.size > 0)
                dataClass.photos[0].imgUrl else ""
            ).into(binding!!.ivIcon)

            binding.root.setOnClickListener {
            }
        }
    }



    fun getProducts(callBack: ItemProducts.() -> Unit){
        viewModelScope.launch {
            foodRepository.callApi(
                callHandler = object : CallHandler<Response<ItemProducts>> {
                    override suspend fun sendRequest(apiInterface: ApiInterface) =
                        apiInterface.getProducts()

                    override fun success(response: Response<ItemProducts>) {
                        if (response.isSuccessful){
                            callBack(response.body()!!)
                        }
                    }

                    override fun error(message: String) {
                        super.error(message)
                    }

                    override fun loading() {
                        super.loading()
                    }
                }
            )
        }


    }


}