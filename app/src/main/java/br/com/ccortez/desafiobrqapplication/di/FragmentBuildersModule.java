package br.com.ccortez.desafiobrqapplication.di;

import br.com.ccortez.desafiobrqapplication.view.ui.CarFragment;
import br.com.ccortez.desafiobrqapplication.view.ui.CarListFragment;

import br.com.ccortez.desafiobrqapplication.view.ui.ShopCartListFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract CarFragment contributeCarFragment();

    @ContributesAndroidInjector
    abstract CarListFragment contributeCarListFragment();

    @ContributesAndroidInjector
    abstract ShopCartListFragment contributeShopCartListFragment();

}
