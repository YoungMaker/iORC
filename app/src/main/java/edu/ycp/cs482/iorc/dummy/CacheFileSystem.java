package edu.ycp.cs482.iorc.dummy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.internal.io.FileSystem;
import okio.Sink;
import okio.Source;

/**
 * Created by Hunter on 3/9/2018.
 */

public class CacheFileSystem implements FileSystem {
    @Override
    public Source source(File file) throws FileNotFoundException {
        return null;
    }

    @Override
    public Sink sink(File file) throws FileNotFoundException {
        return null;
    }

    @Override
    public Sink appendingSink(File file) throws FileNotFoundException {
        return null;
    }

    @Override
    public void delete(File file) throws IOException {

    }

    @Override
    public boolean exists(File file) {
        return false;
    }

    @Override
    public long size(File file) {
        return 0;
    }

    @Override
    public void rename(File from, File to) throws IOException {

    }

    @Override
    public void deleteContents(File directory) throws IOException {

    }
}
