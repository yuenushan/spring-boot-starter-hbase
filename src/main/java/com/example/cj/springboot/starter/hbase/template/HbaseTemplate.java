package com.example.cj.springboot.starter.hbase.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created on 2020-06-11
 */
public class HbaseTemplate implements HbaseOperations {

    private Connection connection;

    /**
     * 读取配置目录中的hbase-default.xml或hbase-site.xml。后期可以优化去掉，改成读几个配置属性
     */
    public HbaseTemplate() {
        this.connection = createConnection();
    }

    public HbaseTemplate(Configuration conf) {
        try {
            this.connection = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            throw new HbaseRunException(e);
        }
    }

    @Override
    public <T> T execute(String tableName, TableCallback<T> action) {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            return action.doInTable(table);
        } catch (Throwable th) {
            if (th instanceof Error) {
                throw (Error) th;
            }
            if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            }
            throw new HbaseRunException((Exception) th);
        }
    }

    private Connection createConnection() {
        try {
            return ConnectionFactory.createConnection();
        } catch (IOException e) {
            throw new HbaseRunException(e);
        }
    }

    @Override
    public <T> T get(String tableName, String rowName, RowMapper<T> mapper) {
        return get(tableName, rowName, null, null, mapper);
    }

    @Override
    public <T> T get(String tableName, String rowName, String familyName, RowMapper<T> mapper) {
        return get(tableName, rowName, familyName, null, mapper);
    }

    @Override
    public <T> T get(String tableName, String rowName, String familyName, String qualifier,
            RowMapper<T> mapper) {
        return execute(tableName, table -> {
            Get get = new Get(Bytes.toBytes(rowName));
            if (familyName != null) {
                byte[] family = Bytes.toBytes(familyName);
                if (qualifier != null) {
                    get.addColumn(family, Bytes.toBytes(qualifier));
                } else {
                    get.addFamily(family);
                }
            }
            Result result = table.get(get);
            return mapper.mapRow(result, 0);
        });
    }

    @Override
    public void put(String tableName, String rowName, String familyName, String qualifier, byte[] data) {
        execute(tableName, table -> {
            Put put = new Put(Bytes.toBytes(rowName));
            put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(qualifier), data);
            table.put(put);
            return null;
        });
    }

    @Override
    public void put(String tableName, String rowName, String familyName, Map<String, byte[]> qualifierDataMap) {
        put(tableName, rowName, familyName, qualifierDataMap, Long.MAX_VALUE);
    }

    @Override
    public void put(String tableName, String rowName, String familyName, Map<String, byte[]> qualifierDataMap,
            long timestamp) {
        if (qualifierDataMap == null || qualifierDataMap.isEmpty()) {
            return;
        }
        byte[] cf = Bytes.toBytes(familyName);
        execute(tableName, table -> {
            List<Put> puts = new ArrayList<>();
            for (Entry<String, byte[]> entry : qualifierDataMap.entrySet()) {
                Put put = new Put(Bytes.toBytes(rowName), timestamp);
                put.addColumn(cf, Bytes.toBytes(entry.getKey()), entry.getValue());
                puts.add(put);
            }
            table.put(puts);
            return null;
        });
    }

    @Override
    public void delete(String tableName, String rowName) {
        execute(tableName, htable -> {
            Delete delete = new Delete(Bytes.toBytes(rowName));
            htable.delete(delete);
            return null;
        });
    }
}
