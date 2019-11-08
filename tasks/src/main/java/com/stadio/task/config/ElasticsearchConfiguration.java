package com.stadio.task.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.net.InetAddress;

//import org.elasticsearch.transport.client.PreBuiltTransportClient;

@Configuration
public class ElasticsearchConfiguration {
    //public class ElasticsearchConfiguration  implements FactoryBean<TransportClient>, InitializingBean, DisposableBean{
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchConfiguration.class);

    /**
     *  this is config for version 5.x
     */

//    @Value("${spring.data.elasticsearch.cluster-nodes}")
//    private String clusterNodes;
//
//    private TransportClient transportClient;
//    private PreBuiltTransportClient preBuiltTransportClient;
//
//    @Override
//    public void destroy() throws Exception {
//        try {
//            logger.info("Closing elasticSearch client");
//            if (transportClient != null) {
//                transportClient.close();
//            }
//        } catch (final Exception e) {
//            logger.error("Error closing ElasticSearch client: ", e);
//        }
//    }
//
//    @Override
//    public TransportClient getObject() throws Exception {
//        return transportClient;
//    }
//
//    @Override
//    public Class<TransportClient> getObjectType() {
//        return TransportClient.class;
//    }
//
//    @Override
//    public boolean isSingleton() {
//        return false;
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        buildClient();
//    }
//
//    protected void buildClient() {
//        try {
//            preBuiltTransportClient = new PreBuiltTransportClient(settings());
//
//            String InetSocket[] = clusterNodes.split(":");
//            String address = InetSocket[0];
//            Integer port = Integer.valueOf(InetSocket[1]);
//            transportClient = preBuiltTransportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(address), port));
//
//        } catch (UnknownHostException e) {
//            logger.error(e.getMessage());
//        }
//    }
//
//    private Settings settings() {
////		Settings settings = Settings.builder().put("cluster.name", clusterName).put("client.transport.sniff", true).build();
//        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
//        return settings;
//    }

    /**
     * this is config for version 2.x
     */

    @Bean
    public NodeBuilder nodeBuilder() {
        return new NodeBuilder();
    }

    @Value("${elasticsearch.host}")
    private String EsHost;

    @Value("${elasticsearch.port}")
    private int EsPort;

    @Value("${elasticsearch.clustername}")
    private String EsClusterName;

    @Bean
    public Client client() throws Exception {

        Settings esSettings = Settings.settingsBuilder()
                .put("cluster.name", EsClusterName)
                .build();

        return TransportClient.builder()
                .settings(esSettings)
                .build()
                .addTransportAddress(
                        new InetSocketTransportAddress(InetAddress.getByName(EsHost), EsPort));
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
        return new ElasticsearchTemplate(client());
    }
}
