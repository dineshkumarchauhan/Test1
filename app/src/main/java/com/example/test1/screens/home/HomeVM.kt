package com.example.test1.screens.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test1.MainActivity
import com.example.test1.R
import com.example.test1.di.PhotoRepository
import com.example.test1.photoDB.QueryInterface
import com.example.test1.photoDB.CallHandler
import com.example.test1.photoDB.ErrorHandler
import com.example.test1.models.ItemPhoto
import com.example.test1.screens.adapter.AdapterType
import com.example.test1.screens.adapter.SimpleAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeVM @Inject constructor( private val foodRepository: PhotoRepository): ViewModel() {
        var photosAdapter = SimpleAdapter(null, AdapterType.Home)

    private val _notesLiveData = MutableLiveData<ErrorHandler<List<ItemPhoto>>>()
    val notesLiveData get() = _notesLiveData

    fun getAllPhotos(query: CharSequence) {
        viewModelScope.launch {
            foodRepository.callQuery(
                callHandler = object : CallHandler<List<ItemPhoto>> {
                    override suspend fun sendRequest(queryInterface: QueryInterface) =
                        queryInterface.getQuery(query.toString())
                    override fun success(response: List<ItemPhoto>) {
                        if(response.size != 0){
                            _notesLiveData.postValue(ErrorHandler.Success(response))
                        }else{
                            _notesLiveData.postValue(ErrorHandler.Error(MainActivity.context.get()!!.getString(
                                R.string.no_image_found)))
                        }
                    }
                    override fun error(message: String) {
                        super.error(message)
                        _notesLiveData.postValue(ErrorHandler.Error(MainActivity.context.get()!!.getString(
                            R.string.no_image_found)))
                    }
                    override fun loading() {
                        super.loading()
                        _notesLiveData.postValue(ErrorHandler.Loading())
                    }
                }
            )
        }
    }

}