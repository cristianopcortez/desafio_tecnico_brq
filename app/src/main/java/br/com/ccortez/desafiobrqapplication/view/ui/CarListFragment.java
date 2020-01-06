package br.com.ccortez.desafiobrqapplication.view.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import br.com.ccortez.desafiobrqapplication.R;
import br.com.ccortez.desafiobrqapplication.database.AppDatabase;
import br.com.ccortez.desafiobrqapplication.databinding.FragmentCarListBinding;
import br.com.ccortez.desafiobrqapplication.di.Injectable;
import br.com.ccortez.desafiobrqapplication.service.CarService;
import br.com.ccortez.desafiobrqapplication.service.model.Car;
import br.com.ccortez.desafiobrqapplication.view.adapter.CarAdapter;
import br.com.ccortez.desafiobrqapplication.view.callback.CarClickCallback;
import br.com.ccortez.desafiobrqapplication.viewmodel.CarListViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class CarListFragment extends Fragment implements Injectable {
    public static final String TAG = "CarListFragment";
    private CarAdapter carAdapter;
    private FragmentCarListBinding binding;
    AppDatabase db;
    public Context mContext = null;
    CarService carService;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_car_list, container, false);

        carAdapter = new CarAdapter(carClickCallback);
        binding.carList.setAdapter(carAdapter);
        binding.setIsLoading(true);
        mContext = Objects.requireNonNull(getActivity()).getApplicationContext();
        db = AppDatabase.getAppDatabase(mContext.getApplicationContext());
        carService = new CarService(mContext);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    ((MainActivity) getActivity()).showCart();
                }
            }
        });

        return (View) binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final CarListViewModel viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(CarListViewModel.class);

        observeViewModel(viewModel);
    }

    private void observeViewModel(CarListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getCarListObservable().observe(this, new Observer<List<Car>>() {
            @Override
            public void onChanged(@Nullable List<Car> cars) {
                if (cars != null) {
                    binding.setIsLoading(false);
                    carAdapter.setCarList(cars);
                }
            }
        });
    }

    private final CarClickCallback carClickCallback = new CarClickCallback() {
        @Override
        public void onClick(Car car) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) getActivity()).show(car);
            }
        }

        @Override
        public void onClickPutInCart(Car car) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                Car carToVerify = db.carDao().getCarById(car.id);
                if (carToVerify == null) {
                    if ((car.preco + carService.getSumFromShopCart()) <= 100000) {
                        car.quantidade = 1;
                        db.carDao().insert(car);
                        Toast.makeText(getActivity().getApplicationContext(), "Adicionado ao carrinho !", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity().getApplicationContext(), "Carrinho passará de 100.000 em compras !", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity().getApplicationContext(), "Carro já no carrinho !", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
