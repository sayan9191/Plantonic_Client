package com.example.myschoolwearclient.ui.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myschoolwearclient.firebaseClasses.OrderItem;
import com.example.myschoolwearclient.repo.CartRepository;
import com.example.myschoolwearclient.repo.OrderRepository;

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
