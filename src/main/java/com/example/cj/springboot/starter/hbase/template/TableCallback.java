package com.example.cj.springboot.starter.hbase.template;

import org.apache.hadoop.hbase.client.Table;

/**
 * Created on 2020-06-11
 */
public interface TableCallback<T> {
    T doInTable(Table table) throws Throwable;
}
