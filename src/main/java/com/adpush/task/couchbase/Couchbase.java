package com.adpush.task.couchbase;

import com.couchbase.client.java.AsyncBucket;

public interface Couchbase {
    
    AsyncBucket asyncBucket();
    
    void close();
    
}
