package id.ac.stiki.doleno.scholarix

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET

// build connection to the baseurl
private val retrofit =
    Retrofit.Builder().baseUrl("https://restcountries.com/v3.1/").addConverterFactory(
        GsonConverterFactory.create()
    ).build()

val countryService = retrofit.create(CountryService::class.java)

interface CountryService {
    @GET("all")
    suspend fun getCountries(): CountriesResponse

}