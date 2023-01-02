package com.example.plantonic.repo

import androidx.lifecycle.MutableLiveData
import com.example.plantonic.firebaseClasses.CategoryProductItem
import com.example.plantonic.firebaseClasses.ProductItem
import com.example.plantonic.utils.constants.DatabaseConstants
import com.example.plantonic.utils.constants.DatabaseConstants.getParticularCategoryItems
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CategoriesRepository {

    val categoriesItems : MutableLiveData<List<ProductItem>> = MutableLiveData()

    val isLoading : MutableLiveData<Boolean> = MutableLiveData()
    val status : MutableLiveData<String> = MutableLiveData()

    private val categoriesMap: HashMap<String, ProductItem> = HashMap()

    fun getCategoryProductItems(categoryId: String){
        isLoading.postValue(true)

        getParticularCategoryItems(categoryId).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    getAllCategoryProductItems(categoryId)
                }else{
                    isLoading.postValue(false)
                    status.postValue("Coming soon")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading.postValue(false)
                status.postValue("Something went wrong")
            }
        })


    }


    private fun getAllCategoryProductItems(categoryId: String){
        getParticularCategoryItems(categoryId).addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()){
                    val categoryProductItem = snapshot.getValue(CategoryProductItem::class.java)
                    if (categoryProductItem != null){
                        addProductFromId(categoryProductItem.productId)
                    }
                }else{
                    isLoading.postValue(false)
                    status.postValue("Coming soon")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()){
                    val categoryProductItem = snapshot.getValue(CategoryProductItem::class.java)
                    if (categoryProductItem != null){
                        addProductFromId(categoryProductItem.productId)
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (categoriesMap.containsKey(snapshot.key)){
                    categoriesMap.remove(snapshot.key)
                    categoriesItems.postValue(categoriesMap.values.toList())

                    if (categoriesMap.size == 0) {
                        isLoading.postValue(false)
                        status.postValue("Coming soon")
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading.postValue(false)
                status.postValue("Something went wrong")
            }
        })
    }


    private fun addProductFromId(product: String?) {
        DatabaseConstants.getParticularProductReference(product)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val item = snapshot.getValue(ProductItem::class.java)
                        if (item != null) {
                            categoriesMap[snapshot.key.toString()] = item
                            categoriesItems.postValue(categoriesMap.values.toList())
                            isLoading.postValue(false)
                            status.postValue("")
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    status.postValue("Something went wrong")
                }
            })
    }
}