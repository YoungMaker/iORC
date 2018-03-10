package edu.ycp.cs482.iorc.dummy;

/**
 * Created by Hunter on 3/9/2018.
 */

import com.apollographql.apollo.api.cache.http.HttpCacheRecord;
import com.apollographql.apollo.api.cache.http.HttpCacheRecordEditor;
import com.apollographql.apollo.api.cache.http.HttpCacheStore;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CharacterHttpCacheStore implements HttpCacheStore{
    HttpCacheStore delegate;
    @Override
    public HttpCacheRecord cacheRecord(@Nonnull String cacheKey) throws IOException {
        return delegate.cacheRecord(cacheKey);
    }


    @Override
    public HttpCacheRecordEditor cacheRecordEditor(@Nonnull String cacheKey) throws IOException {
        return delegate.cacheRecordEditor(cacheKey);
    }

    @Override
    public void remove(@Nonnull String cacheKey) throws IOException {
        delegate.remove(cacheKey);
    }

    @Override
    public void delete() throws IOException {
        delegate.delete();
    }
}
