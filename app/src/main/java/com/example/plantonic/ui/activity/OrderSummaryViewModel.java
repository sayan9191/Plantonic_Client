package com.example.plantonic.ui.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.plantonic.firebaseClasses.OrderItem;
import com.example.plantonic.repo.CartRepository;
import com.example.plantonic.repo.OrderRepository;

public class OrderSummaryViewModel extends ViewModel {

    CartRepository cartRepository = new CartRepository();
    OrderRepository orderRepository = new OrderRepository();

    public LiveData[] getAllOrderSummaryItems(String userId){
        cartRepository.getAllCartItems(userId);
        return new LiveData[] {cartRepository.allCartItems, cartRepository.allCartProducts};
    }

    public void placeOrder(OrderItem orderItem){
        orderRepository.updateOrder(orderItem);
    }

    public LiveData<String> getLastPlacedOrderId(){
        return orderRepository.getLastOrder();
    }
}
