package com.example.plantonic.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.plantonic.firebaseClasses.OrderItem
import com.example.plantonic.firebaseClasses.UserOrderItem
import com.example.plantonic.utils.constants.DatabaseConstants.*
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class OrderRepository {

    private val cartRepository = CartRepository()

    private val _allOrderItems: MutableLiveData<List<OrderItem>> = MutableLiveData()
    val allOrderItems: LiveData<List<OrderItem>> = _allOrderItems
    private val allOrdersMap: HashMap<String, OrderItem> = HashMap()
    private val allUserOrdersMap: HashMap<String, UserOrderItem> = HashMap()

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
            }
    }
}