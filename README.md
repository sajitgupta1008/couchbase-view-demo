# couchbase-view-demo

To run the project, at the root level of  the project, do : 

1. First export your couchbase configuration.

```$xslt
export BUCKET="your_bucket_name"
export DATABASE_USERNAME="your_username"
export DATABASE_PASSWORD="your_password"
export COUCHBASE_CONTACT_POINT_ONE=""  //if couchbase runs on localhost, no need to set it.
```

2. Type `mvn clean compile`.

3. Type `mvn exec:java -Dexec.mainClass="com.adpush.task.Application"


The project assumes that there are documents already in couchbase bucket that contain the field `dateCreated` with epoch time in milliseconds. For eg : 
```
{
  "dateCreated": 1525522989000,
  ...
  ...
}
```

The output tar.gz archive will be created outside the project in a folder named `reports`.
`