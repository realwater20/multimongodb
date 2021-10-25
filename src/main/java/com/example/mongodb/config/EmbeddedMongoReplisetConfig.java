package com.example.mongodb.config;

import com.mongodb.BasicDBList;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongoCmdOptionsBuilder;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.bson.Document;
import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.net.UnknownHostException;

@Profile("local")
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore({ MongoAutoConfiguration.class })
@ConditionalOnClass({ MongoClient.class, MongodStarter.class })
@Import({
    EmbeddedMongoAutoConfiguration.class,
    EmbeddedMongoReplisetConfig.DependenciesConfiguration.class
})
public class EmbeddedMongoReplisetConfig {

    public static final int DFLT_PORT_NUMBER = 11046;
    public static final String DFLT_REPLICASET_NAME = "emb";
    public static final int DFLT_STOP_TIMEOUT_MILLIS = 200;

    private Version.Main mFeatureAwareVersion = Version.Main.V4_0;
    private int mPortNumber = DFLT_PORT_NUMBER;
    private String mReplicaSetName = DFLT_REPLICASET_NAME;
    private long mStopTimeoutMillis = DFLT_STOP_TIMEOUT_MILLIS;

    @Bean
    public IMongodConfig mongodConfig() throws UnknownHostException, IOException {
        final IMongodConfig mongodConfig = new MongodConfigBuilder().version(mFeatureAwareVersion)
            .withLaunchArgument("--replSet", mReplicaSetName)
            .stopTimeoutInMillis(mStopTimeoutMillis)
            .cmdOptions(new MongoCmdOptionsBuilder().useNoJournal(false).build())
            .net(new Net(mPortNumber, Network.localhostIsIPv6())).build();
        return mongodConfig;
    }

    /**
     * Initializes a new replica set.
     * Based on code from https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo/issues/257
     */
    class EmbeddedMongoReplicaSetInitialization {

        EmbeddedMongoReplicaSetInitialization() throws Exception {
            MongoClient mongoClient = null;
            try {
                final BasicDBList members = new BasicDBList();
                members.add(new Document("_id", 0).append("host", "localhost:" + mPortNumber));

                final Document replSetConfig = new Document("_id", mReplicaSetName);
                replSetConfig.put("members", members);

                mongoClient = MongoClients.create("mongodb://localhost:"+DFLT_PORT_NUMBER);
                final MongoDatabase adminDatabase = mongoClient.getDatabase("admin");
                adminDatabase.runCommand(new Document("replSetInitiate", replSetConfig));
            }
            finally {
                if (mongoClient != null) {
                    mongoClient.close();
                }
            }
        }
    }

    @Bean
    EmbeddedMongoReplicaSetInitialization embeddedMongoReplicaSetInitialization() throws Exception {
        return new EmbeddedMongoReplicaSetInitialization();
    }

    /**
     * Additional configuration to ensure that the replica set initialization happens after the
     * {@link MongodExecutable} bean is created. That's it - after the database is started.
     */
    @ConditionalOnClass({ MongoClient.class, MongodStarter.class })
    protected static class DependenciesConfiguration
        extends AbstractDependsOnBeanFactoryPostProcessor {

        DependenciesConfiguration() {
            super(EmbeddedMongoReplicaSetInitialization.class, null, MongodExecutable.class);
        }
    }

}