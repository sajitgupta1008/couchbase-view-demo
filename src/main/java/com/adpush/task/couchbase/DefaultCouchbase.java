package com.adpush.task.couchbase;

import com.couchbase.client.java.AsyncBucket;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.auth.PasswordAuthenticator;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DefaultCouchbase implements Couchbase {
    
    private final Cluster clusterManager;
    private final CouchbaseConfig config;
    private final AsyncBucket bucket;
    
    @Inject
    public DefaultCouchbase(CouchbaseConfig config) {
        this.config = config;
        System.out.println("\n\nhash code : "+config.hashCode());
        
        clusterManager = CouchbaseCluster.create(config.getNodes());
        bucket = bucket().async();
    }
    
    private Bucket bucket() {
        clusterManager.authenticate(new PasswordAuthenticator(config.getUserName(), config.getPassword()));
        return clusterManager.openBucket(config.getBucket());  // TODO
        //  return clusterManager.openBucket(config.getBucket(), config.getPassword());
    }
    
    @Override
    public AsyncBucket asyncBucket() {
        return bucket;
    }
    
    @Override
    public void close() {
        bucket.close();
        clusterManager.disconnect();
    }
}
