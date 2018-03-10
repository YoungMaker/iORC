package edu.ycp.cs482.iorc.dummy;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.cache.http.HttpCacheRecord;
import com.apollographql.apollo.api.cache.http.HttpCacheRecordEditor;
import com.apollographql.apollo.api.cache.http.HttpCacheStore;
import com.apollographql.apollo.cache.http.ApolloHttpCache;
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore;

import org.junit.Rule;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import okhttp3.OkHttpClient;
import okhttp3.internal.io.*;
import okhttp3.internal.io.FileSystem;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Hunter on 2/17/2018.
 */

public class MyApolloClient {
    private static final String BASE_URL = "http://10.64.12.31:8081/graphql";
    private static ApolloClient myApolloClient;
    //private static File characterCache = new File();
    private static int characterCacheSize = 1024*1024;
    private static CharacterHttpCacheStore characterCacheStore;
    @Rule
    public static InMemoryFileSystem inMemoryFileSystem = new InMemoryFileSystem();

    public static ApolloClient getMyApolloClient(){

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        myApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();

        return myApolloClient;
    }

    public static ApolloClient getCharacterApolloClient(){

        characterCacheStore = new CharacterHttpCacheStore();

        //TODO properly implement caching
        //characterCacheStore.delegate = new DiskLruHttpCacheStore(inMemoryFileSystem, new File("/cache/"), characterCacheSize);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //Network interceptor or interceptor???
                .addNetworkInterceptor(loggingInterceptor)
                //.addInterceptor(new TrackingInterceptor())
                //.addInterceptor(cache.interceptor())
                .build();

        ApolloClient characterApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .httpCache(new ApolloHttpCache(characterCacheStore))
                .okHttpClient(okHttpClient)
                .build();

        return characterApolloClient;
    }
}

