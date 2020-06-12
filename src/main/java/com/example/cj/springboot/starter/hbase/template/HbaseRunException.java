package com.example.cj.springboot.starter.hbase.template;

/**
 * Created on 2020-06-11
 */
public class HbaseRunException extends RuntimeException {

    public HbaseRunException(Exception cause) {
        super(cause.getMessage(), cause);
    }
}
