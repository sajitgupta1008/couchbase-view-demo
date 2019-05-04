package com.adpush.task.handlers;

import com.adpush.task.couchbase.Couchbase;
import com.adpush.task.couchbase.CouchbaseConfig;
import com.couchbase.client.core.CouchbaseException;
import com.couchbase.client.java.error.DesignDocumentAlreadyExistsException;
import com.couchbase.client.java.view.AsyncViewResult;
import com.couchbase.client.java.view.AsyncViewRow;
import com.couchbase.client.java.view.DefaultView;
import com.couchbase.client.java.view.DesignDocument;
import com.couchbase.client.java.view.View;
import com.couchbase.client.java.view.ViewQuery;
import rx.Observable;

import javax.inject.Inject;

import static java.util.Collections.singletonList;

public class ViewsHandler {
    
    private static final String MAPPING_FUNCTION = "function (doc, meta) { if(doc.dateCreated){ emit(meta.id, doc); }}";
    private static final String DESIGN_DOCUMENT_NAME = "document";
    
    private final Couchbase couchbase;
    private final CouchbaseConfig config;
    
    @Inject
    public ViewsHandler(Couchbase couchbase, CouchbaseConfig config) {
        this.couchbase = couchbase;
        this.config = config;
        
        this.createAllDocumentsView().toBlocking().subscribe();
    }
    
    private Observable<DesignDocument> createAllDocumentsView() {
        
        View view = DefaultView.create(config.getViewName(), MAPPING_FUNCTION);
        DesignDocument designDocument = DesignDocument.create(DESIGN_DOCUMENT_NAME, singletonList(view));
        
        return couchbase.asyncBucket()
                .bucketManager()
                .flatMap(manager -> manager.insertDesignDocument(designDocument))
                .onErrorResumeNext(throwable -> {
                    
                    if (throwable instanceof DesignDocumentAlreadyExistsException) {
                        return Observable.just(designDocument);
                    }
                    
                    return Observable.error(new CouchbaseException(throwable));
                });
    }
    
    public Observable<AsyncViewRow> readView() {
        
        return couchbase.asyncBucket()
                .query(ViewQuery.from(DESIGN_DOCUMENT_NAME, config.getViewName()))
                .flatMap(AsyncViewResult::rows)
                .doOnError(error -> System.out.println("some error occured : " + error));
    }
}
