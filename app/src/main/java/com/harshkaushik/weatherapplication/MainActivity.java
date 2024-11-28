package com.harshkaushik.weatherapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.harshkaushik.weatherapplication.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding; // Declare binding variable
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "d50b30d05f577a4088924953778fdf6f";
    private static final String TAG = "WeatherApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example: Setting up a UI element using binding
        binding.cityName.setText("Fetching Weather Data...");

        // Fetch weather data
        fetchWeatherData("Jaipur");
        searchCity();
    }

    private void searchCity() {
        // Get the SearchView instance from binding
        SearchView searchView = binding.searchView;

        // Set a listener for query text events
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.isEmpty()) {
                    fetchWeatherData(query); // Fetch weather data for the city
                }
                return true; // Indicate that the query submission has been handled
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true; // Indicate that the query text change has been handled
            }
        });
    }

    private void fetchWeatherData(String cityName) {
        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create API interface
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        // Call the weather API
        Call<WeatherApp> call = apiInterface.getWeather(cityName, "d50b30d05f577a4088924953778fdf6f", "metric");
        call.enqueue(new Callback<WeatherApp>() {
            @Override
            public void onResponse(Call<WeatherApp> call, Response<WeatherApp> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Handle successful response
                    WeatherApp weatherData = response.body();
                    binding.temperature.setText(weatherData.getMain().getTemp() + "°C");
                    binding.cityName.setText(weatherData.getName());
                    binding.weather.setText(weatherData.getWeather().get(0).getDescription());
                    binding.maxTemp.setText("Max: " + weatherData.getMain().getTempMax() + "°C");
                    binding.minTemp.setText("Min: " + weatherData.getMain().getTempMin() + "°C");
                    binding.humidity.setText(weatherData.getMain().getHumidity() + "%");
                    binding.windspeed.setText(weatherData.getWind().getSpeed() + "m/s");
                    binding.conditions.setText(weatherData.getWeather().get(0).getDescription());
                    binding.sunrise.setText(convertUnixToTime(weatherData.getSys().getSunrise()));
                    binding.sunset.setText(convertUnixToTime(weatherData.getSys().getSunset()));
                    binding.sea.setText(weatherData.getMain().getPressure() + "hPa");
                    binding.day.setText(convertUnixToDay(weatherData.getDt()));
                    binding.date.setText(convertUnixToDate(weatherData.getDt()));

//                    binding.visibility.setText(weatherData.getVisibility() + "m");
                    String conditions = weatherData.getWeather().get(0).getDescription();
                    changeImagesAccordingToWeatherCondition(conditions);

                    Log.d("TAG", "City: " + weatherData.getName());
                    Log.d("TAG", "Temperature: " + weatherData.getMain().getTemp() + "°C");
                    Log.d("TAG", "Weather: " + weatherData.getWeather().get(0).getDescription());
                } else {
                    Log.e("TAG", "API Response Error: " + response.message());
                }
            }

            private void changeImagesAccordingToWeatherCondition(String conditions) {
                if (conditions == null || conditions.isEmpty()) {
                    return; // Exit if conditions are null or empty
                }

                // Match the weather condition and set the corresponding background and animation
                switch (conditions.toLowerCase()) {
                    case "clear sky":
                    case "sunny":
                    case "clear":
                        binding.getRoot().setBackgroundResource(R.drawable.hot2_background);
                        binding.lottieAnimationView.setAnimation(R.raw.sunny_animation);
                        break;

                    case "partly cloudy":
                    case "cloudy":
                    case "clouds":
                    case "overcast":
                    case "mist":
                    case "foggy":
                        binding.getRoot().setBackgroundResource(R.drawable.cloud_background);
                        binding.lottieAnimationView.setAnimation(R.raw.cloudy);
                        break;

                    case "light rain":
                    case "drizzle":
                    case "moderate rain":
                    case "rain":
                    case "showers":
                    case "heavy rain":
                    case "thunderstorm":
                        binding.getRoot().setBackgroundResource(R.drawable.rainy_bakground);
                        binding.lottieAnimationView.setAnimation(R.raw.rainy);
                        break;

                    case "light snow":
                    case "moderate snow":
                    case "heavy snow":
                    case "blizzard":
                    case "snow":
                    case "sleet":
                        binding.getRoot().setBackgroundResource(R.drawable.snow_background);
                        binding.lottieAnimationView.setAnimation(R.raw.snow);
                        break;

                    case "haze":
                    case "smoke":
                    case "dust":
                    case "sand":
                    case "ash":
                    case "squall":
                    case "tornado":
                        binding.getRoot().setBackgroundResource(R.drawable.haze_background);
                        binding.lottieAnimationView.setAnimation(R.raw.haze);
                        break;

                    case "windy":
                    case "breeze":
                    case "gale":
                    case "storm":
                        binding.getRoot().setBackgroundResource(R.drawable.wind_background);
                        binding.lottieAnimationView.setAnimation(R.raw.windy);
                        break;

                    case "cold":
                    case "freezing":
                        binding.getRoot().setBackgroundResource(R.drawable.snow_background);
                        binding.lottieAnimationView.setAnimation(R.raw.cold);
                        break;

                    case "hot":
                    case "heatwave":
                        binding.getRoot().setBackgroundResource(R.drawable.hot_background);
                        binding.lottieAnimationView.setAnimation(R.raw.hot);
                        break;

                    default:
                        // Handle unexpected weather conditions
                        binding.getRoot().setBackgroundResource(R.drawable.sunny);
                        binding.lottieAnimationView.setAnimation(R.raw.sunny_animation);
                        break;
                }

                // Play the animation after setting it
                binding.lottieAnimationView.playAnimation();
            }




            @Override
            public void onFailure(Call<WeatherApp> call, Throwable t) {
                // Handle failure
                Log.e("TAG", "API Call Failed: " + t.getMessage());
            }

            private String convertUnixToTime(long timestamp) {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                return sdf.format(new Date(timestamp * 1000)); // Multiply by 1000 to convert seconds to milliseconds
            }

            // Convert timestamp to day and date
            private String convertUnixToDay(long timestamp) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault()); // Full day name
                return sdf.format(new Date(timestamp * 1000));
            }

            private String convertUnixToDate(long timestamp) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()); // e.g., 22 November 2024
                return sdf.format(new Date(timestamp * 1000));
            }
        });
        
    }
}

