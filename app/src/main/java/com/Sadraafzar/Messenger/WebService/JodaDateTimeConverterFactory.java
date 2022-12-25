package com.Sadraafzar.Messenger.WebService;

/**
 * Created by Administrator on 12/04/2018.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import retrofit2.converter.gson.GsonConverterFactory;

public class JodaDateTimeConverterFactory {

    private static Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateTimeTypeConverter());
        return gsonBuilder.create();
    }

    /**
     * Create an instance using {@code gson} for conversion. The provided {@code gson} instance has a
     * registered TypeAdapter to handle deserializing {@link DateTime}. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static GsonConverterFactory create() {
        return GsonConverterFactory.create(gson());
    }
}