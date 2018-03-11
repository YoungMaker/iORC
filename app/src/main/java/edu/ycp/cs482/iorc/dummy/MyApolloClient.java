package edu.ycp.cs482.iorc.dummy;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.cache.http.HttpCache;
import com.apollographql.apollo.api.cache.http.HttpCacheRecord;
import com.apollographql.apollo.api.cache.http.HttpCacheRecordEditor;
import com.apollographql.apollo.api.cache.http.HttpCacheStore;
import com.apollographql.apollo.cache.http.ApolloHttpCache;
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore;

import org.junit.Rule;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
    private static int cacheSize = 1024*1024;
    private static CharacterHttpCacheStore characterCacheStore;
    private static CharacterHttpCacheStore classCacheStore;
    private static CharacterHttpCacheStore raceCacheStore;

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

        //setup character
        characterCacheStore.delegate = new DiskLruHttpCacheStore(inMemoryFileSystem, new File("/characterCache/"), cacheSize);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        HttpCache cache = new ApolloHttpCache(characterCacheStore, null);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //Network interceptor for http request, cache interceptor for cache
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(cache.interceptor())
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();

        ApolloClient characterApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .httpCache(new ApolloHttpCache(characterCacheStore))
                .okHttpClient(okHttpClient)
                .build();

        return characterApolloClient;
    }

    public static ApolloClient getClassApolloClient(){

        classCacheStore = new CharacterHttpCacheStore();

        //setup class caching in memory
        classCacheStore.delegate = new DiskLruHttpCacheStore(inMemoryFileSystem, new File("/classCache/"), cacheSize);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        HttpCache cache = new ApolloHttpCache(classCacheStore, null);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //Network interceptor for http request, cache interceptor for cache
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(cache.interceptor())
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();

        ApolloClient characterApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .httpCache(new ApolloHttpCache(classCacheStore))
                .okHttpClient(okHttpClient)
                .build();

        return characterApolloClient;
    }

    public static ApolloClient getRaceApolloClient(){

        raceCacheStore = new CharacterHttpCacheStore();

        //setup race caching in memory
        raceCacheStore.delegate = new DiskLruHttpCacheStore(inMemoryFileSystem, new File("/raceCache/"), cacheSize);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        HttpCache cache = new ApolloHttpCache(raceCacheStore, null);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //Network interceptor for http request, cache interceptor for cache
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(cache.interceptor())
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();

        ApolloClient characterApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .httpCache(new ApolloHttpCache(raceCacheStore))
                .okHttpClient(okHttpClient)
                .build();

        return characterApolloClient;
    }
}

