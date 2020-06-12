package com.example.cj.springboot.starter.hbase;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created on 2020-06-11
 */
@ConfigurationProperties(prefix = "spring.data.hbase")
public class HbaseProperties {
    private String zNodeParent;
    private String zookeeperQuorum;

    public String getzNodeParent() {
        return zNodeParent;
    }

    public void setzNodeParent(String zNodeParent) {
        this.zNodeParent = zNodeParent;
    }

    public String getZookeeperQuorum() {
        return zookeeperQuorum;
    }

    public void setZookeeperQuorum(String zookeeperQuorum) {
        this.zookeeperQuorum = zookeeperQuorum;
    }
}
