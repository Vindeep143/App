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
package com.stratio.cassandra.lucene.testsAT.varia;

import com.datastax.driver.core.Row;
import com.stratio.cassandra.lucene.testsAT.BaseAT;
import com.stratio.cassandra.lucene.testsAT.util.CassandraUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class AllowFilteringWith1000SimilarRowsAT extends BaseAT {

    private static CassandraUtils cassandraUtils;

    @BeforeClass
    public static void before() {
        cassandraUtils = CassandraUtils.builder("filtering_1000_similar")
                                       .withPartitionKey("integer_1")
                                       .withClusteringKey()
                                       .withColumn("ascii_1", "ascii")
                                       .withColumn("bigint_1", "bigint")
                                       .withColumn("blob_1", "blob")
                                       .withColumn("boolean_1", "boolean")
                                       .withColumn("decimal_1", "decimal")
                                       .withColumn("date_1", "timestamp")
                                       .withColumn("double_1", "double")
                                       .withColumn("float_1", "float")
                                       .withColumn("integer_1", "int")
                                       .withColumn("inet_1", "inet")
                                       .withColumn("text_1", "text")
                                       .withColumn("varchar_1", "varchar")
                                       .withColumn("uuid_1", "uuid")
                                       .withColumn("timeuuid_1", "timeuuid")
                                       .withColumn("list_1", "list<text>")
                                       .withColumn("set_1", "set<text>")
                                       .withColumn("map_1", "map<text,text>")
                                       .withColumn("lucene", "text")
                                       .build()
                                       .createKeyspace()
                                       .createTable()
                                       .createIndex();
        DataHelper.generateCustomInsertions(1000, cassandraUtils);
        cassandraUtils.refresh();
    }

    @AfterClass
    public static void after() {
        cassandraUtils.dropKeyspace();
    }

    @Test
    public void allowFiltering1000rowsTest() {
        List<Row> rows = cassandraUtils.selectAllFromIndexQueryWithFiltering(1000, "double_1", 1D);
        assertEquals("Expected 1000 results!", 1000, rows.size());
    }

    @Test
    public void allowFilteringAndLimit999Test() {
        List<Row> rows = cassandraUtils.selectAllFromIndexQueryWithFiltering(999, "double_1", 1D);
        assertEquals("Expected 999 results!", 999, rows.size());
    }

    @Test
    public void allowFilteringAndLimit1001Test() {
        List<Row> rows = cassandraUtils.selectAllFromIndexQueryWithFiltering(1001, "double_1", 1D);
        assertEquals("Expected 1000 results!", 1000, rows.size());
    }

    @Test
    public void allowFilteringAndLimit99Test() {
        List<Row> rows = cassandraUtils.selectAllFromIndexQueryWithFiltering(99, "double_1", 1D);
        assertEquals("Expected 99 results!", 99, rows.size());
    }

    @Test
    public void allowFilteringAndLimit101Test() {
        List<Row> rows = cassandraUtils.selectAllFromIndexQueryWithFiltering(101, "double_1", 1D);
        assertEquals("Expected 101 results!", 101, rows.size());
    }

    @Test
    public void allowFilteringAndLimit100Test() {
        List<Row> rows = cassandraUtils.selectAllFromIndexQueryWithFiltering(100, "double_1", 1D);
        assertEquals("Expected 100 results!", 100, rows.size());
    }
}