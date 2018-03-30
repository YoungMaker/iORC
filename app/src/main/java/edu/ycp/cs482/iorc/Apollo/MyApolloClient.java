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
    private static final String BASE_URL = "http://iorc-api.mooo.com:8081/graphql";
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

        CharacterHttpCacheStore characterCacheStore = new CharacterHttpCacheStore();

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

        CharacterHttpCacheStore classCacheStore = new CharacterHttpCacheStore();

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

        ApolloClient classApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .httpCache(new ApolloHttpCache(classCacheStore))
                .okHttpClient(okHttpClient)
                .build();

        return classApolloClient;
    }

    public static ApolloClient getRaceApolloClient(){

        CharacterHttpCacheStore raceCacheStore = new CharacterHttpCacheStore();

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

        ApolloClient raceApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .httpCache(new ApolloHttpCache(raceCacheStore))
                .okHttpClient(okHttpClient)
                .build();

        return raceApolloClient;
    }

    public static ApolloClient getAlignmentApolloClient(){

        CharacterHttpCacheStore alignmentCacheStore = new CharacterHttpCacheStore();

        //setup race caching in memory
        alignmentCacheStore.delegate = new DiskLruHttpCacheStore(inMemoryFileSystem, new File("/alignmentCache/"), cacheSize);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        HttpCache cache = new ApolloHttpCache(alignmentCacheStore, null);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //Network interceptor for http request, cache interceptor for cache
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(cache.interceptor())
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();

        ApolloClient alignmentApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .httpCache(new ApolloHttpCache(alignmentCacheStore))
                .okHttpClient(okHttpClient)
                .build();

        return alignmentApolloClient;
    }

    public static ApolloClient getReligionApolloClient(){

        CharacterHttpCacheStore religionCacheStore = new CharacterHttpCacheStore();

        //setup race caching in memory
        religionCacheStore.delegate = new DiskLruHttpCacheStore(inMemoryFileSystem, new File("/religionCache/"), cacheSize);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        HttpCache cache = new ApolloHttpCache(religionCacheStore, null);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //Network interceptor for http request, cache interceptor for cache
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(cache.interceptor())
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();

        ApolloClient religionApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .httpCache(new ApolloHttpCache(religionCacheStore))
                .okHttpClient(okHttpClient)
                .build();

        return religionApolloClient;
    }

    public static ApolloClient getItemApolloClient(){

        CharacterHttpCacheStore itemCacheStore = new CharacterHttpCacheStore();

        //setup race caching in memory
        itemCacheStore.delegate = new DiskLruHttpCacheStore(inMemoryFileSystem, new File("/itemCache/"), cacheSize);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        HttpCache cache = new ApolloHttpCache(itemCacheStore, null);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //Network interceptor for http request, cache interceptor for cache
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(cache.interceptor())
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();

        ApolloClient itemApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .httpCache(new ApolloHttpCache(itemCacheStore))
                .okHttpClient(okHttpClient)
                .build();

        return itemApolloClient;
    }

    public static ApolloClient getSkillApolloClient(){

        CharacterHttpCacheStore skillCacheStore = new CharacterHttpCacheStore();

        //setup skills
        skillCacheStore.delegate = new DiskLruHttpCacheStore(inMemoryFileSystem, new File("/skillCache/"), cacheSize);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        HttpCache cache = new ApolloHttpCache(skillCacheStore, null);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //Network interceptor for http request, cache interceptor for cache
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(cache.interceptor())
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();

        ApolloClient skillApolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .httpCache(new ApolloHttpCache(skillCacheStore))
                .okHttpClient(okHttpClient)
                .build();

        return skillApolloClient;
    }

}

