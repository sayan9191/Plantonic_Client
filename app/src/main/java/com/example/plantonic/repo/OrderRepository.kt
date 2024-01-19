package com.example.plantonic.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.plantonic.firebaseClasses.OrderItem
import com.example.plantonic.firebaseClasses.UserOrderItem
import com.example.plantonic.retrofit.models.CommonErrorModel
import com.example.plantonic.retrofit.models.order.GetOrdersResponseModel
import com.example.plantonic.retrofit.models.order.PlaceOrderRequestModel
import com.example.plantonic.retrofit.models.order.PlaceOrderResponseModel
import com.example.plantonic.retrofit.models.track.TrackOrderRequestModel
import com.example.plantonic.retrofit.models.track.TrackOrderResponseModel
import com.example.plantonic.utils.constants.DatabaseConstants.*
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepository : BaseRepository() {

    private val cartRepository = CartRepository()

    private val _lastOrder : MutableLiveData<String> = MutableLiveData();
//    val lastPlacedOrderId : LiveData<String> = _lastOrder
//get() {
//        return orderRepository.lastOrder
//    }

    private val _allOrderItems: MutableLiveData<List<OrderItem>> = MutableLiveData()
    val allOrderItems: LiveData<List<OrderItem>> = _allOrderItems

    private val allOrdersMap: HashMap<String, OrderItem> = HashMap()
    private val allUserOrdersMap: HashMap<String, UserOrderItem> = HashMap()


    private val _placeOrderResponse : MutableLiveData<PlaceOrderResponseModel> = MutableLiveData()
    val placeOrderResponse : LiveData<PlaceOrderResponseModel> = _placeOrderResponse

    private val _myOrdersResponseModel : MutableLiveData<GetOrdersResponseModel> = MutableLiveData()
    val myOrdersResponseModel : LiveData<GetOrdersResponseModel> = _myOrdersResponseModel

    val trackOrderResponse : MutableLiveData<TrackOrderResponseModel> = MutableLiveData()

    fun getMyOrders(page: Int){
        isLoading.postValue(true)
        api.getOrders("Bearer " + localStorage.token, page).enqueue( object: Callback<GetOrdersResponseModel>{
            override fun onResponse(
                call: Call<GetOrdersResponseModel>,
                response: Response<GetOrdersResponseModel>
            ) {
                if (response.isSuccessful) {
                    isLoading.postValue(false)
                    errorMessage.postValue("")
                    response.body()?.let {
                        _myOrdersResponseModel.postValue(it)
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

            override fun onFailure(call: Call<GetOrdersResponseModel>, t: Throwable) {
                Log.d("Request Failed. Error: ", t.message.toString())
                isLoading.postValue(false)
                errorMessage.postValue("Something went wrong")
            }

        })
    }

    fun trackOrder(orderId: Int) {
        isLoading.postValue(true)
        api.trackOrder("Bearer " + localStorage.token, TrackOrderRequestModel(orderId)).enqueue( object: Callback<TrackOrderResponseModel>{
            override fun onResponse(
                call: Call<TrackOrderResponseModel>,
                response: Response<TrackOrderResponseModel>
            ) {
                if (response.isSuccessful) {
                    isLoading.postValue(false)
                    errorMessage.postValue("")
                    response.body()?.let {
                        trackOrderResponse.postValue(it)
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

            override fun onFailure(call: Call<TrackOrderResponseModel>, t: Throwable) {
                Log.d("Request Failed. Error: ", t.message.toString())
                isLoading.postValue(false)
                errorMessage.postValue("Something went wrong")
            }

        })
    }

    fun getAllUserOrders(userId: String) {
        getAllUserOrdersReference(userId).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    val item = snapshot.getValue(UserOrderItem::class.java)
                    if (item != null) {
                        allUserOrdersMap[snapshot.key.toString()] = item
                        getSpecificOrderDetails(item.orderId)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    val item = snapshot.getValue(UserOrderItem::class.java)
                    if (item != null) {
                        allUserOrdersMap[snapshot.key.toString()] = item
                        getSpecificOrderDetails(item.orderId)
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (allUserOrdersMap.containsKey(snapshot.key.toString())) {
                    allUserOrdersMap.remove(snapshot.key.toString())
                }
                if (allOrdersMap.containsKey(snapshot.key.toString())) {
                    allOrdersMap.remove(snapshot.key.toString())
                    _allOrderItems.postValue(ArrayList(allOrdersMap.values))
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getSpecificOrderDetails(orderId: String) {
        getSpecificOrderReference(orderId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val item = snapshot.getValue(OrderItem::class.java)
                    if (item != null) {
                        allOrdersMap[snapshot.key.toString()] = item
                        _allOrderItems.postValue(ArrayList(allOrdersMap.values))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    fun placeOrder(all_orders: List<OrderItem>){
        isLoading.postValue(true)
        api.placeOrder("Bearer " + localStorage.token, PlaceOrderRequestModel(all_orders)).enqueue(object : Callback<PlaceOrderResponseModel>{
            override fun onResponse(
                call: Call<PlaceOrderResponseModel>,
                response: Response<PlaceOrderResponseModel>
            ) {
                if (response.isSuccessful) {
                    isLoading.postValue(false)
                    errorMessage.postValue("")
                    response.body()?.let {
                        _placeOrderResponse.postValue(it)
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

            override fun onFailure(call: Call<PlaceOrderResponseModel>, t: Throwable) {
                Log.d("Request Failed. Error: ", t.message.toString())
                isLoading.postValue(false)
                errorMessage.postValue("Something went wrong")
            }

        })
    }


    fun updateOrder(orderItem: OrderItem) {
        getSpecificOrderReference(orderItem.orderId)
            .setValue(orderItem).addOnSuccessListener {
                updateMerchantOrder(
                    UserOrderItem(
                        orderItem.orderId,
                        orderItem.productId,
                        orderItem.customerId,
                        orderItem.merchantId
                    )
                )
            }
    }

    private fun updateMerchantOrder(orderItem: UserOrderItem) {
        getSpecificMerchantOrderReference(orderItem.merchantId, orderItem.orderId)
            .setValue(orderItem).addOnSuccessListener {
                updateUserOrder(orderItem)
            }
    }

    private fun updateUserOrder(userOrderItem: UserOrderItem) {
        getSpecificUserOrderReference(userOrderItem.customerId, userOrderItem.orderId)
            .setValue(userOrderItem).addOnSuccessListener {
                cartRepository.removeProductFromCart(
                    userOrderItem.customerId,
                    userOrderItem.productId
                )
                _lastOrder.postValue(userOrderItem.productId)
            }
    }
}