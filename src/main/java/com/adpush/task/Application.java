package com.adpush.task;

import com.adpush.task.handlers.FileHandler;
import com.adpush.task.handlers.ViewsHandler;
import com.adpush.task.utils.DateUtil;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.IOException;

public class Application {
    
    public static void main(String[] args) throws IOException {
        
        Injector injector = Guice.createInjector(new ApplicationModule());
        
        ViewsHandler viewsHandler = injector.getInstance(ViewsHandler.class);
        FileHandler fileHandler = injector.getInstance(FileHandler.class);
        
        fileHandler.initializeParentDirectory();
        
        viewsHandler.createAllDocumentsView()
                .flatMap(ignored -> viewsHandler.readAllDocumentsView())
                .flatMap(row -> row.document())
                .groupBy(doc -> DateUtil.formatDate(doc.content().getLong("dateCreated")), doc -> doc)
                .flatMap(groupObservable -> groupObservable.toList()
                        .map(sameMonthDocs -> fileHandler
                                .createMonthlyReport(groupObservable.getKey(), sameMonthDocs)))
                .toBlocking()
                .forEach(url -> System.out.println("\nOutput file :" + url.toString()));
        
        viewsHandler.closeConnection();
    }
}
