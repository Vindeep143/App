/*
 * Copyright (C) 2014 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.cassandra.lucene.testsAT.util;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.Batch;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.stratio.cassandra.lucene.builder.Builder;
import com.stratio.cassandra.lucene.builder.index.Index;
import com.stratio.cassandra.lucene.builder.index.schema.mapping.Mapper;
import com.stratio.cassandra.lucene.builder.search.Search;
import com.stratio.cassandra.lucene.builder.search.condition.Condition;
import com.stratio.cassandra.lucene.builder.search.sort.SortField;
import com.stratio.cassandra.lucene.testsAT.BaseAT;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.stratio.cassandra.lucene.builder.Builder.all;
import static com.stratio.cassandra.lucene.builder.Builder.index;
import static com.stratio.cassandra.lucene.testsAT.util.CassandraConfig.*;

/**
 * @author Andres de la Pena {@literal <adelapena@stratio.com>}
 */
public class CassandraUtils {

    protected static final Logger logger = BaseAT.logger;

    private Session session;

    private final String keyspace;
    private final String table;
    private final String indexName;
    private final String qualifiedTable;
    private final Map<String, String> columns;
    private final Map<String, Mapper> mappers;
    private final List<String> partitionKey;
    private final List<String> clusteringKey;
    private final String indexColumn;

    public static CassandraUtilsBuilder builder(String name) {
        return new CassandraUtilsBuilder(name);
    }

    public CassandraUtils(String keyspace,
                          String table,
                          String indexName,
                          String indexColumn,
                          Map<String, String> columns,
                          Map<String, Mapper> mappers,
                          List<String> partitionKey,
                          List<String> clusteringKey) {

        session = CassandraConnection.session;

        this.keyspace = keyspace;
        this.table = table;
        this.indexName = indexName;
        this.columns = columns;
        this.mappers = mappers;
        this.partitionKey = partitionKey;
        this.clusteringKey = clusteringKey;
        this.indexColumn = indexColumn;
        qualifiedTable = keyspace + "." + table;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public String getTable() {
        return table;
    }

    public String getQualifiedTable() {
        return qualifiedTable;
    }

    public String getIndexName() {
        return indexName;
    }

    public String getIndexColumn() {
        return indexColumn;
    }

    public ResultSet execute(Statement statement) {
        return CassandraConnection.execute(statement);
    }

    public ResultSet execute(int fetchSize, String query) {
        if (!query.endsWith(";")) {
            query += ";";
        }
        return execute(new SimpleStatement(query).setFetchSize(fetchSize));
    }

    public ResultSet execute(String query, Object... args) {
        return execute(FETCH, String.format(query, args));
    }

    public ResultSet execute(int fetchSize, String query, Object... args) {
        return execute(fetchSize, String.format(query, args));
    }

    ResultSet execute(StringBuilder query) {
        return execute(query.toString());
    }

    public CassandraUtils waitForIndexing() {

        // Waiting for the custom index to be refreshed
        logger.debug("Waiting for the index to be created...");
        try {
            TimeUnit.SECONDS.sleep(WAIT_FOR_INDEXING);
        } catch (InterruptedException e) {
            logger.error("Interruption caught during a Thread.sleep; index might be unstable");
        }
        logger.debug("Index ready to rock!");

        return this;
    }

    public CassandraUtils refresh() {
        session.execute(QueryBuilder.select()
                                    .from(keyspace, table)
                                    .where(eq(indexColumn, Builder.search().refresh(true).build()))
                                    .setConsistencyLevel(ConsistencyLevel.ALL));
        return this;
    }

    public CassandraUtils createKeyspace() {
        execute(new StringBuilder().append("CREATE KEYSPACE ")
                                   .append(keyspace)
                                   .append(" with replication = ")
                                   .append("{'class' : 'SimpleStrategy', 'replication_factor' : '")
                                   .append(REPLICATION)
                                   .append("' };"));
        return this;
    }

    public CassandraUtils dropTable() {
        execute("DROP TABLE " + qualifiedTable);
        return this;
    }

    public CassandraUtils dropKeyspace() {
        execute("DROP KEYSPACE " + keyspace + " ;");
        return this;
    }

    public CassandraUtils createTable() {
        StringBuilder sb = new StringBuilder().append("CREATE TABLE ").append(qualifiedTable).append(" (");
        for (String s : columns.keySet()) {
            sb.append(s).append(" ").append(columns.get(s)).append(", ");
        }
        sb.append("PRIMARY KEY ((");
        for (int i = 0; i < partitionKey.size(); i++) {
            sb.append(partitionKey.get(i));
            sb.append(i == partitionKey.size() - 1 ? ")" : ",");
        }
        for (String s : clusteringKey) {
            sb.append(", ").append(s);
        }
        sb.append("))");
        execute(sb);
        return this;
    }

    public CassandraUtils truncateTable() {
        execute(new StringBuilder().append("TRUNCATE ").append(qualifiedTable));
        return this;
    }

    public CassandraUtils createIndex() {
        if (!columns.containsKey(COLUMN)) {
            execute("ALTER TABLE %s ADD %s text;", qualifiedTable, COLUMN);
        }
        Index index = index(keyspace, table, indexColumn).name(indexName)
                                                         .refreshSeconds(REFRESH)
                                                         .indexingThreads(THREADS);
        for (Map.Entry<String, Mapper> entry : mappers.entrySet()) {
            index.mapper(entry.getKey(), entry.getValue());
        }
        execute(index.build());
        return this;
    }

    public CassandraUtils createUDT(UDT udt) {
        execute(udt.toString(keyspace));
        return this;
    }

    public CassandraUtils dropIndex() {
        execute(new StringBuilder().append("DROP INDEX ").append(keyspace).append(".").append(indexName).append(";"));
        return this;
    }

    public List<Row> selectAllFromIndexQueryWithFiltering(int limit, String name, Object value) {
        Search search = Builder.search().query(all()).refresh(true);
        return execute(QueryBuilder.select()
                                   .from(keyspace, table)
                                   .where(eq(indexColumn, search.build()))
                                   .and(eq(name, value))
                                   .limit(limit)
                                   .allowFiltering()).all();
    }

    @SafeVarargs
    public final CassandraUtils insert(Map<String, String>... paramss) {

        Batch batch = QueryBuilder.unloggedBatch();
        for (Map<String, String> params : paramss) {
            String columns = "";
            String values = "";
            for (String s : params.keySet()) {
                if (!s.equals(indexColumn)) {
                    columns += s + ",";
                    values = values + params.get(s) + ",";
                }
            }
            columns = columns.substring(0, columns.length() - 1);
            values = values.substring(0, values.length() - 1);

            batch.add(new SimpleStatement(new StringBuilder().append("INSERT INTO ")
                                                             .append(qualifiedTable)
                                                             .append(" (")
                                                             .append(columns)
                                                             .append(") VALUES (")
                                                             .append(values)
                                                             .append(");")
                                                             .toString()));
        }
        execute(batch);
        return this;
    }

    public CassandraUtils insert(String[] names, Object[] values) {
        execute(QueryBuilder.insertInto(keyspace, table).values(names, values));
        return this;
    }

    public CassandraUtilsDelete delete(String... names) {
        return new CassandraUtilsDelete(this, names);
    }

    public CassandraUtilsUpdate update() {
        return new CassandraUtilsUpdate(this);
    }

    public CassandraUtilsSelect select() {
        return new CassandraUtilsSelect(this);
    }

    public CassandraUtilsSelect searchAll() {
        return select().search().filter(all());
    }

    public CassandraUtilsSelect query(Condition query) {
        return select().query(query);
    }

    public CassandraUtilsSelect filter(Condition filter) {
        return select().filter(filter);
    }

    public CassandraUtilsSelect sort(SortField... sort) {
        return select().sort(sort);
    }

}
