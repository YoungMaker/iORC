package edu.ycp.cs482.iorc.Apollo;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.cache.http.HttpCache;
import com.apollographql.apollo.cache.http.ApolloHttpCache;
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore;
import org.junit.Rule;
import java.io.File;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Hunter on 2/17/2018.
 */


public class MyApolloClient {
    private static final String BASE_URL = "http://10.128.65.75:8081/graphql";
    private static ApolloClient myApolloClient;
    //private static File characterCache = new File();
    private static int cacheSize = 1024*1024;

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

        CharacterHttpCacheStore cacheStore= createCache("/characterCache/");
        HttpLoggingInterceptor loggingInterceptor = createLoggingInterceptor();
        HttpCache httpCache = createHttpCache(cacheStore);

        return ApolloClient.builder()
                .serverUrl(BASE_URL)
                .httpCache(new ApolloHttpCache(cacheStore))
                .okHttpClient(createHttpClient(loggingInterceptor,httpCache))
                .build();
    }

    public static ApolloClient getClassApolloClient(){

        CharacterHttpCacheStore cacheStore= createCache("/classCache/");
        HttpLoggingInterceptor loggingInterceptor = createLoggingInterceptor();
        HttpCache httpCache = createHttpCache(cacheStore);

        return ApolloClient.builder()
                .serverUrl(BASE_URL)
                .httpCache(new ApolloHttpCache(cacheStore))
                .okHttpClient(createHttpClient(loggingInterceptor,httpCache))
                .build();
    }

    public static ApolloClient getRaceApolloClient(){

        CharacterHttpCacheStore cacheStore= createCache("/raceCache/");
        HttpLoggingInterceptor loggingInterceptor = createLoggingInterceptor();
        HttpCache httpCache = createHttpCache(cacheStore);

        return ApolloClient.builder()
                .serverUrl(BASE_URL)
                .httpCache(new ApolloHttpCache(cacheStore))
                .okHttpClient(createHttpClient(loggingInterceptor,httpCache))
                .build();
    }

    public static ApolloClient getVersionSheetApolloClient(){

        CharacterHttpCacheStore cacheStore= createCache("/versionSheetCache/");
        HttpLoggingInterceptor loggingInterceptor = createLoggingInterceptor();
        HttpCache httpCache = createHttpCache(cacheStore);

        return ApolloClient.builder()
                .serverUrl(BASE_URL)
                .httpCache(new ApolloHttpCache(cacheStore))
                .okHttpClient(createHttpClient(loggingInterceptor,httpCache))
                .build();
    }

    private static CharacterHttpCacheStore createCache(String cachePath){
        CharacterHttpCacheStore cacheStore = new CharacterHttpCacheStore();

        //setup race caching in memory
        cacheStore.delegate = new DiskLruHttpCacheStore(inMemoryFileSystem, new File(cachePath), cacheSize);

        return cacheStore;
    }

    private static HttpLoggingInterceptor createLoggingInterceptor(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return loggingInterceptor;
    }

    private static HttpCache createHttpCache(CharacterHttpCacheStore cacheStore){
        return new ApolloHttpCache(cacheStore, null);
    }

    private static OkHttpClient createHttpClient(HttpLoggingInterceptor loggingInterceptor, HttpCache cache){

        return new OkHttpClient.Builder()
                //Network interceptor for http request, cache interceptor for cache
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(cache.interceptor())
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();
    }

}

