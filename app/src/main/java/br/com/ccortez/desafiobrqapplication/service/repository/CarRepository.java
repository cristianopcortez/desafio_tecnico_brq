package br.com.ccortez.desafiobrqapplication.service.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import br.com.ccortez.desafiobrqapplication.database.AppDatabase;
import br.com.ccortez.desafiobrqapplication.service.model.Car;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class CarRepository {
    private GitHubService gitHubService;

    AppDatabase db;
    public Context mContext = null;

    private static final String TAG = CarRepository.class.getSimpleName();

    @Inject
    public CarRepository(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    public LiveData<List<Car>> getCarList() {
        final MutableLiveData<List<Car>> data = new MutableLiveData<>();

        gitHubService.getCarList().enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {

                Log.d(TAG, "carList: " + response.body());

                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {

                Log.e(TAG, "error carList: ", t);

                // TODO better error handling in part #2 ...
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<Car> getCarDetails(String carID) {
        final MutableLiveData<Car> data = new MutableLiveData<>();

        gitHubService.getCarDetails(carID).enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                simulateDelay();
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                // TODO better error handling in part #2 ...
                data.setValue(null);
            }
        });

        return data;
    }

    private void simulateDelay() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LiveData<List<Car>> getCarDbList(Context mContext) {
        final MutableLiveData<List<Car>> data = new MutableLiveData<>();

        if (mContext != null) {
            db = AppDatabase.getAppDatabase(mContext.getApplicationContext());
            data.setValue(db.carDao().getAllCars());
        }

        return data;
    }
}
