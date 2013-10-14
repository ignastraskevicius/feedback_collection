<!-- 0. If your database is mysql - its enough to complete following steps. If not - customise project for your needs manually (it's NOT sufficient to modify following steps accordingly) -->
<!-- 1. Create database feedback_collection -->
<!-- 2. Create database feedback_collection_test for running integration tests-->
<!-- 3. Create database user 'root' identified by password 'admin' -->
<!-- 4. Grant username 'root' permissions to both databases: feedback_collection and feedback_collection_test -->

<Resource
    name="jdbc/feedback_collectionDataSource"
    url="jdbc:mysql://localhost:3306/feedback_collection?tinyInt1isBit=false&amp;characterEncoding=utf8"
    username="root"
    password="admin"
    driverClassName="com.mysql.jdbc.Driver"
    auth="Container"
    initialSize="5"
    maxActive="100"
    maxIdle="10"
    minEvictableIdleTimeMillis="600000"
    minIdle="0"
    timeBetweenEvictionRunsMillis="60000"
    type="javax.sql.DataSource"
    />

<!-- 5. Copy this <Resource> fragment to $CATALINA_HOME/conf/server.xml and put it inside <GlobalNamingResources> tag -->
<!-- 6. Copy following <ResourceLink> fragment to $CATALINA_HOME/conf/context.xml and put it inside Context tag -->

<ResourceLink
    global="jdbc/feedback_collectionDataSource"
    name="jdbc/feedback_collectionDataSource"
    type="javax.sql.DataSource"
    />

<!-- 7. Copy into $CATALINA_HOME/conf/catalina.properties -->

spring.profiles.active=production

<!-- 8. Put DB drivers jars into $CATALINA_HOME/lib folder. -->
<!-- 9. Restart Tomcat -->
