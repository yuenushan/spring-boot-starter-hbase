package com.example.cj.springboot.starter.hbase.template;

import java.util.Map;

/**
 * Created on 2020-06-11
 */
public interface HbaseOperations {
    <T> T execute(String tableName, TableCallback<T> action);
    <T> T get(String tableName, String rowName, RowMapper<T> mapper);
    <T> T get(String tableName, String rowName, String familyName, RowMapper<T> mapper);
    <T> T get(String tableName, String rowName, String familyName, String qualifier, RowMapper<T> mapper);
    void put(String tableName, String rowName, String familyName, String qualifier, byte[] data);
    void put(String tableName, String rowName, String familyName, Map<String, byte[]> qualifierDataMap);
    void put(String tableName, String rowName, String familyName, Map<String, byte[]> qualifierDataMap, long timestamp);
    void delete(String tableName, String rowName);
}
