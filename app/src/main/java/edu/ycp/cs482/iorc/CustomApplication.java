package edu.ycp.cs482.iorc;

import android.app.Application;

import edu.ycp.cs482.iorc.Apollo.Query.ApolloQueryCntroller;
import edu.ycp.cs482.iorc.Apollo.Query.QueryControllerProvider;

/**
 * Created by Aaron Walsh on 5/5/2018.
 */

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new QueryControllerProvider(new ApolloQueryCntroller());
    }
}
