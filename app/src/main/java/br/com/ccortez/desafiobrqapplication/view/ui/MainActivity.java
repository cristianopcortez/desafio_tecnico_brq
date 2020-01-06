package br.com.ccortez.desafiobrqapplication.view.ui;

import android.os.Bundle;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import br.com.ccortez.desafiobrqapplication.R;
import br.com.ccortez.desafiobrqapplication.service.model.Car;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add car list fragment if this is first creation
        if (savedInstanceState == null) {

            CarListFragment fragment = new CarListFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, CarListFragment.TAG).commit();

        }
    }

    public void show(Car car) {
        CarFragment carFragment = CarFragment.forCar(String.valueOf(car.id));

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("car")
                .replace(R.id.fragment_container,
                        carFragment, CarFragment.TAG).commit();
    }

    public void showHome() {
        CarListFragment carListFragment = new CarListFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("carlist")
                .replace(R.id.fragment_container,
                        carListFragment, CarListFragment.TAG).commit();
    }

    public void showCart() {
        ShopCartListFragment shopCartListFragment = new ShopCartListFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("shopcart")
                .replace(R.id.fragment_container,
                        shopCartListFragment, ShopCartListFragment.TAG).commit();
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
