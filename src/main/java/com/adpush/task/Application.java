package com.adpush.task;

import com.adpush.task.couchbase.Couchbase;
import com.adpush.task.couchbase.CouchbaseConfig;
import com.adpush.task.handlers.FileHandler;
import com.adpush.task.handlers.ViewsHandler;
import com.adpush.task.utils.DateUtil;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.IOException;

public class Application {
    
    public static void main(String[] args) throws IOException {
        
        Injector injector = Guice.createInjector(new ApplicationModule());
        
        Couchbase couchbase = injector.getInstance(Couchbase.class);
        
        ViewsHandler viewsHandler = new ViewsHandler(couchbase, new CouchbaseConfig());
        
        FileHandler fileHandler = new FileHandler();
        fileHandler.initializeParentDirectory();
        
        viewsHandler.readView()
                .flatMap(row -> row.document())
                .groupBy(doc -> DateUtil.formatDate(doc.content().getLong("dateCreated")), doc -> doc)
                .flatMap(groupObservable -> groupObservable.toList()
                        .map(sameMonthDocs -> fileHandler
                                .createMonthlyReport(groupObservable.getKey(), sameMonthDocs)))
                .toBlocking()
                .forEach(url -> System.out.println("Output file :" + url.toString()));
        
       /* List<AsyncViewRow> viewRows = viewsHandler.readView().toList().toBlocking().single();
     
        
        viewRows.parallelStream()
                .ma
        
                .flatMap(view -> view.document())
                .forEach()
                .forEach(view -> System.out.println("data : " + view.id() + ", " + view.value().toString()));*/
        
        
        couchbase.close();
    }
}
