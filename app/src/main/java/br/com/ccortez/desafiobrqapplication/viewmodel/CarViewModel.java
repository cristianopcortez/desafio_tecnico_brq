package br.com.ccortez.desafiobrqapplication.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import br.com.ccortez.desafiobrqapplication.service.model.Car;
import br.com.ccortez.desafiobrqapplication.service.repository.CarRepository;

import javax.inject.Inject;

public class CarViewModel extends AndroidViewModel {
    private static final String TAG = CarViewModel.class.getName();
    private static final MutableLiveData ABSENT = new MutableLiveData();
    {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    private final LiveData<Car> carObservable;
    private final MutableLiveData<String> carID;

    public ObservableField<Car> car = new ObservableField<>();

    @Inject
    public CarViewModel(@NonNull CarRepository carRepository, @NonNull Application application) {
        super(application);

        this.carID = new MutableLiveData<>();

        carObservable = Transformations.switchMap(carID, input -> {
            if (input.isEmpty()) {
                Log.i(TAG, "CarViewModel carID is absent!!!");
                return ABSENT;
            }

            Log.i(TAG,"CarViewModel carID is " + carID.getValue());

            return carRepository.getCarDetails(carID.getValue());
        });
    }

    public LiveData<Car> getObservableCar() {
        return carObservable;
    }

    public void setCar(Car car) {
        this.car.set(car);
    }

    public void setCarID(String carID) {
        this.carID.setValue(carID);
    }
}
