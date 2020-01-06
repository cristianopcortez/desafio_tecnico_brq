package br.com.ccortez.desafiobrqapplication.service.repository;

import br.com.ccortez.desafiobrqapplication.BuildConfig;
import br.com.ccortez.desafiobrqapplication.service.model.Car;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {
    String HTTPS_API_GITHUB_URL = BuildConfig.API_BASE_URL;

//    @GET("carros.json")
    @GET(".")
    Call<List<Car>> getCarList();

    //@GET("descricao_carro.json")
    @GET("{id}")
    Call<Car> getCarDetails(@Path("id") String id);
}
