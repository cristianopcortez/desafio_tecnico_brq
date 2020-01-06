package br.com.ccortez.desafiobrqapplication.view.callback;

import br.com.ccortez.desafiobrqapplication.service.model.Car;

public interface CarClickCallback {
    void onClick(Car car);
    void onClickPutInCart(Car car);
}
