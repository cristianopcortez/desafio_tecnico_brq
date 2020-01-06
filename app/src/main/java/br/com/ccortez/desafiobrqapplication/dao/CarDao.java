package br.com.ccortez.desafiobrqapplication.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.ccortez.desafiobrqapplication.service.model.Car;

@Dao
public interface CarDao {

    @Insert
    void insert(Car car);

    @Update
    void update(Car car);

    @Delete
    void delete(Car car);

    @Query("DELETE FROM car")
    void clear();

    @Query("SELECT * FROM car")
    List<Car> getAllCars();

    @Query("SELECT * FROM car WHERE id = :id")
    Car getCarById(long id);

    @Query("SELECT SUM(car.preco)FROM car")
    int getSumFromShopCart();

}
