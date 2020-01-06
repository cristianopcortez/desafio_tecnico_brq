package br.com.ccortez.desafiobrqapplication.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import br.com.ccortez.desafiobrqapplication.service.model.Car;
import br.com.ccortez.desafiobrqapplication.service.repository.CarRepository;

import java.util.List;

import javax.inject.Inject;

public class CarListViewModel extends AndroidViewModel {
    private final LiveData<List<Car>> carListObservable;
    private final LiveData<List<Car>> carDbListObservable;

    @Inject
    public CarListViewModel(@NonNull CarRepository carRepository, @NonNull Application application) {
        super(application);

        // If any transformation is needed, this can be simply done by Transformations class ...
        carListObservable = carRepository.getCarList();
        carDbListObservable = carRepository.getCarDbList(application.getApplicationContext());
    }

    /**
     * Expose the LiveData Cars query so the UI can observe it.
     */
    public LiveData<List<Car>> getCarListObservable() {
        return carListObservable;
    }

    public LiveData<List<Car>> getCarDbListObservable(Context mContext) {
        return carDbListObservable;
    }
}
