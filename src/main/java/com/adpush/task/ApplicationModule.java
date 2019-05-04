package com.adpush.task;

import com.adpush.task.couchbase.Couchbase;
import com.adpush.task.couchbase.DefaultCouchbase;
import com.google.inject.AbstractModule;

public class ApplicationModule extends AbstractModule {
    
    @Override
    protected void configure() {
        bind(Couchbase.class).to(DefaultCouchbase.class);
    }
}
