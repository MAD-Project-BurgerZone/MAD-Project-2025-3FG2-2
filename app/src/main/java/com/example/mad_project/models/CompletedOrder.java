package com.example.mad_project.models;

import java.io.Serializable;
import java.util.List;

public class CompletedOrder implements Serializable {
    private List<FoodOrder> orders;
    private double deliveryFee;

    public CompletedOrder(List<FoodOrder> orders, double deliveryFee) {
        this.orders = orders;
        this.deliveryFee = deliveryFee;
    }

    public List<FoodOrder> getOrders() {
        return orders;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }
}
