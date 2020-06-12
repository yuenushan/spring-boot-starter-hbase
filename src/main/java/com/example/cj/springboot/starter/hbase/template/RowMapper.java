package com.example.cj.springboot.starter.hbase.template;

import org.apache.hadoop.hbase.client.Result;

/**
 * Created on 2020-06-11
 */
public interface RowMapper<T> {
    T mapRow(Result result, int rowNum) throws Exception;
}
