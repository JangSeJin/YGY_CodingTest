package com.hour24.ygy.network.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by interpark on 2016. 3. 7..
 */
public class RetrofitRequest {

    //JSON
    public static <T> T createRetrofitJSONService(final Class<T> classes, final String url) {

        final Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();

        T service = retrofit.create(classes);

        return service;
    }

    //XML
    public static <T> T createRetrofitXMLService(final Class<T> classes, final String url) {

        final Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
                .baseUrl(url)
                .build();

        T service = retrofit.create(classes);

        return service;
    }

    // ScalarsConverterFactory for String
    public static <T> T createRetrofitScalarsService(final Class<T> classes, final String url) {

        final Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(url)
                .build();

        T service = retrofit.create(classes);

        return service;
    }

}
