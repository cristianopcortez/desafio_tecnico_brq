package br.com.ccortez.desafiobrqapplication.di;

import br.com.ccortez.desafiobrqapplication.service.repository.BackEndService;
import br.com.ccortez.desafiobrqapplication.viewmodel.CarViewModelFactory;

import javax.inject.Singleton;

import androidx.lifecycle.ViewModelProvider;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(subcomponents = ViewModelSubComponent.class)
class AppModule {
    @Singleton @Provides
    BackEndService provideBackEndService() {
        return new Retrofit.Builder()
                .baseUrl(BackEndService.HTTP_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BackEndService.class);
    }

    @Singleton
    @Provides
    ViewModelProvider.Factory provideViewModelFactory(
            ViewModelSubComponent.Builder viewModelSubComponent) {

        return new CarViewModelFactory(viewModelSubComponent.build());
    }

}
