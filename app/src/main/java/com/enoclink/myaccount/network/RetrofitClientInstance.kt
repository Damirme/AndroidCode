package com.enoclink.myaccount.network

import com.enoclink.myaccount.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


public class RetrofitClientInstance {

    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL
        private const val CONNECTION_TIME_OUT = 60L
        private const val READ_TIME_OUT = 60L
        private const val WRITE_TIME_OUT = 60L

        private var retrofit: Retrofit? = null

        fun retrofitInstance(): Retrofit {
            if (retrofit == null) {
                val builder = getBuilder()

                retrofit = buildRetrofit(builder)
            }
            return retrofit!!
        }


        private fun getLogInterceptor(): HttpLoggingInterceptor {
            val logging = HttpLoggingInterceptor()
            // set your desired log level
            if (BuildConfig.BUILD_TYPE == "release")
                logging.level = HttpLoggingInterceptor.Level.NONE
            else
                logging.level = HttpLoggingInterceptor.Level.BODY

            return logging
        }

        fun recreateInstance(token: String? = "") {
            val builder = getBuilder(token)
            retrofit = buildRetrofit(builder)

        }

        private fun getBuilder(token: String? = ""): OkHttpClient? {
            var builder = OkHttpClient.Builder().addInterceptor(getLogInterceptor())
                .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)

            if (token != null && token.isNotEmpty()) {
                builder.addInterceptor { chain ->
                    val request = chain
                        .request()
                        .newBuilder()
                        .addHeader("Authorization", token)
                        .build()
                    chain.proceed(request)
                }
            }
            return builder.build()
        }

        private fun buildRetrofit(builder: OkHttpClient?): Retrofit {
            return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(builder)
                .baseUrl(BASE_URL)
                .build()
        }
    }

}