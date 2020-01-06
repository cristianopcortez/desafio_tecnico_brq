package br.com.ccortez.desafiobrqapplication.service;

import android.content.Context;

import java.util.List;

import br.com.ccortez.desafiobrqapplication.database.AppDatabase;
import br.com.ccortez.desafiobrqapplication.service.model.Car;

public class CarService {

    Context mContext;
    AppDatabase db;

    public CarService(Context mContext) {

        this.mContext = mContext;
        this.db = AppDatabase.getAppDatabase(mContext.getApplicationContext());
    }

    public int getSumFromShopCart() {
        int total = 0;

        List<Car> cars = db.carDao().getAllCars();
        for (int i = 0; i < cars.size(); i++) {
            Car car =  cars.get(i);
            total = total + (car.preco * car.quantidade);
        }

        return total;
    }
}
