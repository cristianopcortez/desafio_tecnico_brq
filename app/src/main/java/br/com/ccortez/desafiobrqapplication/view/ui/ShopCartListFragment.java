package br.com.ccortez.desafiobrqapplication.view.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import br.com.ccortez.desafiobrqapplication.R;
import br.com.ccortez.desafiobrqapplication.database.AppDatabase;
import br.com.ccortez.desafiobrqapplication.databinding.FragmentShopCartListBinding;
import br.com.ccortez.desafiobrqapplication.di.Injectable;
import br.com.ccortez.desafiobrqapplication.service.model.Car;
import br.com.ccortez.desafiobrqapplication.view.adapter.ShopCartAdapter;
import br.com.ccortez.desafiobrqapplication.view.callback.CarClickCallback;
import br.com.ccortez.desafiobrqapplication.view.callback.ShopCartClickCallback;
import br.com.ccortez.desafiobrqapplication.viewmodel.CarListViewModel;

public class ShopCartListFragment extends Fragment implements Injectable {
    public static final String TAG = "ShopCartListFragment";
    private ShopCartAdapter shopCartAdapter;
    private FragmentShopCartListBinding binding;
    AppDatabase db;
    public Context mContext = null;
    ActionMode mActionMode;
    androidx.constraintlayout.widget.ConstraintLayout btnFinalizarCompra;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop_cart_list,
                container, false);

        shopCartAdapter = new ShopCartAdapter(shopCartClickCallback);
        binding.carList.setAdapter(shopCartAdapter);
        binding.setIsLoading(true);
        mContext = Objects.requireNonNull(getActivity()).getApplicationContext();
        db = AppDatabase.getAppDatabase(mContext.getApplicationContext());

        mActionMode = Objects.requireNonNull(getActivity()).startActionMode(mActionModeCallback);

        btnFinalizarCompra = binding.btnFinalizarCompra;
        btnFinalizarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //            db.carDao().clear();
                if (getActivity() != null)
                    getActivity().finish();
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
        viewModel.getCarDbListObservable(mContext).observe(this, new Observer<List<Car>>() {
            @Override
            public void onChanged(@Nullable List<Car> cars) {
                if (cars != null) {
                    binding.setIsLoading(false);
                    shopCartAdapter.setCarList(cars);
                }
            }
        });
    }

    private final ShopCartClickCallback shopCartClickCallback = new ShopCartClickCallback() {
        @Override
        public void onClick(Car car) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) getActivity()).show(car);
            }
        }

        @Override
        public void onClickRemoveFromCart(Car car) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                db.carDao().delete(car);
                shopCartAdapter.setCarList(db.carDao().getAllCars());
                shopCartAdapter.notifyDataSetChanged();
                if (shopCartAdapter.getItemCount() == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "Carrinho vazio !", Toast.LENGTH_SHORT).show();
                }
            }
        }

    };

    private android.view.ActionMode.Callback mActionModeCallback = new android.view.ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();

            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            }

            inflater.inflate(R.menu.menu_no_items, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) getActivity()).showHome();
            }
        }
    };

}
