//package com.harshkaushik.weatherapplication;
//
//import retrofit2.Call;
//import retrofit2.http.GET;
//import retrofit2.http.Query;
//
//public interface ApiInterface {
////    @GET("weather")
////    Call<WeatherApp> getCurrentWeatherData(@Query("lat") String lat,
////                                           @Query("lon") String lon,
////                                           @Query("APPID") String appid,
////                                            @Query("q") String city,
////                                            @Query("units") String units);
////    Call<WeatherApp> getWeather(@Query("q") String city, @Query("appid") String appid, @Query("units") String units);
//    @GET("weather")
//    Call<WeatherResponse> getWeather(
//            @Query("q") String city,
//            @Query("appid") String apiKey,
//            @Query("units") String units
//    );
//}


package com.harshkaushik.weatherapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("weather")
    Call<WeatherApp> getWeather(
            @Query("q") String cityName,
            @Query("appid") String apiKey,
            @Query("units") String units
    );
}