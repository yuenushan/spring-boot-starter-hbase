package com.example.cj.springboot.starter.hbase;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.cj.springboot.starter.hbase.template.HbaseTemplate;

/**
 * Created on 2020-06-11
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(HbaseProperties.class)
public class HbaseAutoConfiguration {

    private static final String HBASE_QUORUM = "hbase.zookeeper.quorum";
    private static final String HBASE_ZNODE_PARENT = "zookeeper.znode.parent";

    @Bean
    @ConditionalOnMissingBean(HbaseTemplate.class)
    @ConditionalOnProperty(prefix = "spring.data.hbase", name = {"zNodeParent", "zookeeperQuorum"})
    public HbaseTemplate hbaseTemplate(HbaseProperties hbaseProperties) {
        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        configuration.set(HBASE_QUORUM, hbaseProperties.getZookeeperQuorum());
        configuration.set(HBASE_ZNODE_PARENT, hbaseProperties.getzNodeParent());
        return new HbaseTemplate(configuration);
    }

}
