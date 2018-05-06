package edu.ycp.cs482.iorc.Apollo.Query;

/**
 * Created by Aaron Walsh on 5/5/2018.
 */

public class QueryControllerProvider {

    private IQueryController controller;
    private static QueryControllerProvider _instance;

    public QueryControllerProvider(IQueryController controller){
        this.controller = controller;
        _instance = this;
    }

    public IQueryController getQueryController(){
        return this.controller;
    }

    public static QueryControllerProvider getInstance(){
        return _instance;
    }
}
