package br.com.ccortez.desafiobrqapplication.view.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.ccortez.desafiobrqapplication.R;
import br.com.ccortez.desafiobrqapplication.database.AppDatabase;
import br.com.ccortez.desafiobrqapplication.databinding.FragmentCarDetailsBinding;
import br.com.ccortez.desafiobrqapplication.di.Injectable;
import br.com.ccortez.desafiobrqapplication.service.CarService;
import br.com.ccortez.desafiobrqapplication.service.model.Car;
import br.com.ccortez.desafiobrqapplication.viewmodel.CarViewModel;

import javax.inject.Inject;


public class CarFragment extends Fragment implements Injectable {
    public static final String TAG = "CarFragment";
    private static final String KEY_CAR_ID = "car_id";
    private FragmentCarDetailsBinding binding;
    ActionMode mActionMode;

    AppDatabase db;
    CarService carService;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    List<Pair<String, String>> qtdList;

    private Context mContext = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_car_details, container, false);

        mActionMode = Objects.requireNonNull(getActivity()).startActionMode(mActionModeCallback);
        mContext = Objects.requireNonNull(getActivity()).getApplicationContext();
        db = AppDatabase.getAppDatabase(mContext.getApplicationContext());
        carService = new CarService(mContext);

        // Create and set the adapter for the RecyclerView.
        return (View) binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final CarViewModel viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(CarViewModel.class);

        viewModel.setCarID(getArguments().getString(KEY_CAR_ID));

        binding.setCarViewModel(viewModel);
        binding.setIsLoading(true);

        observeViewModel(viewModel);
    }

    private void observeViewModel(final CarViewModel viewModel) {
        // Observe car data
        viewModel.getObservableCar().observe(this, new Observer<Car>() {
            @Override
            public void onChanged(@Nullable Car car) {
                if (car != null) {
                    binding.setIsLoading(false);
                    viewModel.setCar(car);

                    Picasso.get()
                            .load(car.imagem)
                            .placeholder(R.drawable.ic_car)
                            .error(R.drawable.ic_alert)
                            .into(binding.imageView);

                    binding.btnColocarCarrinho.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "QTD SELECTED: " + binding.spinnerQtd.getSelectedItem());
                            Log.d(TAG, "QTD TOTAL: " + car.quantidade);

                            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                                Car carToVerify = db.carDao().getCarById(car.id);
                                int intCarQty = Integer.valueOf(binding.spinnerQtd.getSelectedItem().toString()).intValue();
                                if (carToVerify == null) {
                                    if (((car.preco * intCarQty) + carService.getSumFromShopCart()) <= 100000) {
                                        car.quantidade = intCarQty;
                                        db.carDao().insert(car);
//                                        for (int i = 0; i < intCarQty; i++) {
//                                            db.carDao().insert(car);
//                                        }
                                        Toast.makeText(getActivity().getApplicationContext(), "Adicionado ao carrinho !", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(getActivity().getApplicationContext(), "Carrinho passará de 100.000 em compras !", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    if (((car.preco * intCarQty) + carService.getSumFromShopCart()) <= 100000) {
                                        if ((carToVerify.quantidade + intCarQty) <= car.quantidade) {
                                            Toast.makeText(getActivity().getApplicationContext(), "Carro já no carrinho ! Atualizando quantidade.", Toast.LENGTH_SHORT).show();
                                            intCarQty = intCarQty + carToVerify.quantidade;
                                            carToVerify.quantidade = intCarQty;
                                            db.carDao().update(carToVerify);
                                        } else {
                                            Toast.makeText(getActivity().getApplicationContext(), "Não há carros disponíveis em estoque para colocar no carrinho!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getActivity().getApplicationContext(), "Carrinho passará de 100.000 em compras !", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        }
                    });

                    addItemsOnSpinner(binding.spinnerQtd, car.quantidade);
                }
            }
        });
    }

    /**
     * Creates car fragment for specific car ID
     */
    public static CarFragment forCar(String carID) {
        CarFragment fragment = new CarFragment();
        Bundle args = new Bundle();

        args.putString(KEY_CAR_ID, carID);
        fragment.setArguments(args);

        return fragment;
    }

    public void addItemsOnSpinner(Spinner spin, int qtd) {

        mContext = Objects.requireNonNull(getActivity()).getApplicationContext();
//        db = AppDatabase.getAppDatabase(mContext.getApplicationContext());

//        spin = view.findViewById(R.id.spinner_gerir);
        List<String> list = new ArrayList<>();
        qtdList = new ArrayList<>();

        for (int i = 1; i < qtd+1; i++) {
            qtdList.add(new Pair<>(String.valueOf(i), String.valueOf(i)));
//            eventsList.add(new Pair<>(lista_enderecos.get(i).getNomeDivulgacao(), lista_enderecos.get(i).getCodigo()));
//            list.add(lista_enderecos.get(i).getNomeDivulgacao());
            list.add(String.valueOf(i));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);
    }

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

            // ver onPositiveClick para ação de cada um

            switch (item.getItemId()) {
//                case R.id.action_delete:
//                    alertDialogHelper.showAlertDialog("", getString(R.string.confirm_delete_evento), getString(R.string.yes), getString(R.string.no), 1, false);
//                    return true;
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
