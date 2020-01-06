package br.com.ccortez.desafiobrqapplication.di;

import br.com.ccortez.desafiobrqapplication.viewmodel.CarListViewModel;
import br.com.ccortez.desafiobrqapplication.viewmodel.CarViewModel;

import dagger.Subcomponent;

/**
 * A sub component to create ViewModels. It is called by the
 * {@link br.com.ccortez.desafiobrqapplication.viewmodel.CarViewModelFactory}.
 */
@Subcomponent
public interface ViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        ViewModelSubComponent build();
    }

    CarListViewModel carListViewModel();
    CarViewModel carViewModel();
}
