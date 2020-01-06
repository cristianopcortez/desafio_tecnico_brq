package br.com.ccortez.desafiobrqapplication.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import br.com.ccortez.desafiobrqapplication.R;
import br.com.ccortez.desafiobrqapplication.databinding.CarListItemBinding;
import br.com.ccortez.desafiobrqapplication.databinding.CarListItemClBinding;
import br.com.ccortez.desafiobrqapplication.service.model.Car;
import br.com.ccortez.desafiobrqapplication.view.callback.CarClickCallback;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private static final String TAG = CarAdapter.class.getSimpleName();

    List<? extends Car> carList;

    @Nullable
    private final CarClickCallback carClickCallback;

    public CarAdapter(@Nullable CarClickCallback carClickCallback) {
        this.carClickCallback = carClickCallback;
    }

    public void setCarList(final List<? extends Car> carList) {
        if (this.carList == null) {
            this.carList = carList;
            notifyItemRangeInserted(0, carList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return CarAdapter.this.carList.size();
                }

                @Override
                public int getNewListSize() {
                    return carList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return CarAdapter.this.carList.get(oldItemPosition).id ==
                            carList.get(newItemPosition).id;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Car car = carList.get(newItemPosition);
                    Car old = carList.get(oldItemPosition);
                    return car.id == old.id;
                }
            });
            this.carList = carList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CarListItemClBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        // R.layout.car_list_item,
                        R.layout.car_list_item_cl,
                        parent, false);

        binding.setCallback(carClickCallback);

        return new CarViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {
        holder.binding.setCar(carList.get(position));

        // Log.d(TAG, "img: "+carList.get(position).imagem);
        Log.d(TAG, "car: "+carList.get(position));

        Picasso.get()
                .load(carList.get(position).imagem)
                .placeholder(R.drawable.ic_car)
                .error(R.drawable.ic_alert)
                .into(holder.carImage);

        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return carList == null ? 0 : carList.size();
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {

        final CarListItemClBinding binding;
        final ImageView carImage;

        public CarViewHolder(CarListItemClBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            carImage = binding.carImage;
        }
    }
}
