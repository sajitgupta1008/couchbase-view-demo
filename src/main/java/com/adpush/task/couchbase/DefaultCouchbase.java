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
        this.clusterManager = CouchbaseCluster.create(config.getNodes());
        this.bucket = bucket().async();
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
    
    private Bucket bucket() {
        clusterManager.authenticate(new PasswordAuthenticator(config.getUserName(), config.getPassword()));
        return clusterManager.openBucket(config.getBucket());
    }
}
