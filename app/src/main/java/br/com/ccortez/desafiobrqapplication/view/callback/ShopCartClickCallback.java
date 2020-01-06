package br.com.ccortez.desafiobrqapplication.view.callback;

import br.com.ccortez.desafiobrqapplication.service.model.Car;

public interface ShopCartClickCallback {
    void onClick(Car car);
    void onClickRemoveFromCart(Car car);
}
