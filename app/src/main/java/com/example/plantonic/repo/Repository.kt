package com.example.plantonic.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.plantonic.retrofit.models.home.HomePageBannerResponseModel
import com.example.plantonic.firebaseClasses.CategoryItem
import com.example.plantonic.firebaseClasses.PopularProductItem
import com.example.plantonic.firebaseClasses.ProductItem
import androidx.lifecycle.LiveData
import com.example.plantonic.retrofit.models.CommonErrorModel
import com.example.plantonic.utils.constants.DatabaseConstants
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class Repository : BaseRepository() {
    private val _homePageBanners = MutableLiveData<HomePageBannerResponseModel>()
    private val categoryItems = MutableLiveData<ArrayList<CategoryItem>>()
    private val allPopularProducts = MutableLiveData<List<PopularProductItem>>()
    private val hashMap = HashMap<String?, CategoryItem>()
    private val allPopularProductItems = MutableLiveData<ArrayList<ProductItem>>()
    private val productItemsHashMap = HashMap<String?, ProductItem>()

    val categories: LiveData<ArrayList<CategoryItem>>
        get() {
            allCategories
            return categoryItems
        }
    fun allPopularProducts(): MutableLiveData<List<PopularProductItem>>
        {
            getAllPopularProducts()
            return allPopularProducts
        }
    fun popularProductItems(): LiveData<ArrayList<ProductItem>>
        {
            getAllPopularProducts()
            return allPopularProductItems
        }

    val homePageBanners: LiveData<HomePageBannerResponseModel>
        get() {
            allHomePageBanners
            return _homePageBanners
        }

    private val allHomePageBanners: Unit
        get() {
            api.getAllHomePageBanners()
                .enqueue(object : Callback<HomePageBannerResponseModel?> {
                    override fun onResponse(
                        call: Call<HomePageBannerResponseModel?>,
                        response: Response<HomePageBannerResponseModel?>
                    ) {
                        if (response.isSuccessful) {
                            isLoading.postValue(false)
                            errorMessage.postValue("")
                            response.body()?.let {
                                _homePageBanners.postValue(it)
                                Log.d("-------", it.toString())
                            }
                        } else {
                            isLoading.postValue(false)
                            response.errorBody()?.let { errorBody ->
                                errorBody.string().let {
                                    Log.e("Error: ", it)
                                    val errorResponse: CommonErrorModel =
                                        Gson().fromJson(it, CommonErrorModel::class.java)
                                    errorMessage.postValue(errorResponse.detail)
                                    Log.e("Error: ", errorResponse.detail)
                                }
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<HomePageBannerResponseModel?>,
                        t: Throwable
                    ) {
                        Log.d("Request Failed. Error: ", t.message.toString())
                        isLoading.postValue(false)
                        errorMessage.postValue("Something went wrong")
                    }
                })
        }

    //                        Collections.sort(categoryItems, categoryItems.);
    private val allCategories: Unit
        get() {
            DatabaseConstants.getAllCategoriesReference()
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        if (snapshot.exists()) {
                            val item = snapshot.getValue(CategoryItem::class.java)
                            if (item != null) {
                                hashMap[snapshot.key] = item
                                //                        Collections.sort(categoryItems, categoryItems.);
                                categoryItems.postValue(ArrayList(hashMap.values))
                                Log.d("_________", item.categoryName)
                            }
                        }
                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                        if (snapshot.exists()) {
                            val item = snapshot.getValue(CategoryItem::class.java)
                            if (item != null) {
                                hashMap[snapshot.key] = item
                                Log.d("-----------", item.categoryName)
                                categoryItems.postValue(ArrayList(hashMap.values))
                            }
                        }
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        if (hashMap.containsKey(snapshot.key)) {
                            hashMap.remove(snapshot.key)
                            categoryItems.postValue(ArrayList(hashMap.values))
                        }
                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onCancelled(error: DatabaseError) {}
                })
        }
    private val popularHashMap = HashMap<String?, PopularProductItem>()
    private fun getAllPopularProducts() {
        DatabaseConstants.getAllPopularProductsReference()
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        val item = snapshot.getValue(
                            PopularProductItem::class.java
                        )
                        if (item != null) {
                            popularHashMap[snapshot.key] = item
                            getProductFromId(item.getProductId())
                            Log.d("------------------", item.productId)
                            allPopularProducts.postValue(ArrayList(popularHashMap.values))
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        val item = snapshot.getValue(
                            PopularProductItem::class.java
                        )
                        if (item != null) {
                            popularHashMap[snapshot.key] = item
                            getProductFromId(item.getProductId())
                            allPopularProducts.postValue(ArrayList(popularHashMap.values))
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    if (popularHashMap.containsKey(snapshot.key)) {
                        popularHashMap.remove(snapshot.key)
                        allPopularProducts.postValue(ArrayList(popularHashMap.values))
                        if (productItemsHashMap.containsKey(snapshot.key)) {
                            productItemsHashMap.remove(snapshot.key)
                        }
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun getProductFromId(product: String?) {
        DatabaseConstants.getParticularProductReference(product)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val item = snapshot.getValue(ProductItem::class.java)
                        if (item != null) {
                            productItemsHashMap[snapshot.key] = item
                            allPopularProductItems.postValue(ArrayList(productItemsHashMap.values))
                            Log.d("------------------", item.productName)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    companion object {
        private  var repository: Repository? = null
        val instance: Repository?
            get() {
                if (repository == null) {
                    repository = Repository()
                }
                return repository
            }
    }
}