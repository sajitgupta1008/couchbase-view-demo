package com.adpush.task.couchbase;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.List;

public class CouchbaseConfig {
    
    private static final Config CONFIG = ConfigFactory.load();
    
    private final List<String> nodes;
    private final String bucket;
    private final String userName;
    private final String password;
    private final String viewName;
    
    public CouchbaseConfig() {
        this.nodes = CONFIG.getStringList("couchbase.nodes");
        this.bucket = CONFIG.getString("couchbase.bucket");
        this.password = CONFIG.getString("couchbase.password");
        this.viewName = CONFIG.getString("couchbase.view_name");
        this.userName = CONFIG.getString("couchbase.username");
    }
    
    public List<String> getNodes() {
        return nodes;
    }
    
    public String getBucket() {
        return bucket;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getViewName() {
        return viewName;
    }
    
    public String getUserName() {
        return userName;
    }
}
