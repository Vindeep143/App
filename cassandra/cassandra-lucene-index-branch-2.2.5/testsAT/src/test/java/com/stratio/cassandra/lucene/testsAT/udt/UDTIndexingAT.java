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
package com.stratio.cassandra.lucene.testsAT.udt;

import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.stratio.cassandra.lucene.testsAT.BaseAT;
import com.stratio.cassandra.lucene.testsAT.util.CassandraUtils;
import com.stratio.cassandra.lucene.testsAT.util.CassandraUtilsSelect;
import com.stratio.cassandra.lucene.testsAT.util.UDT;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

import static com.stratio.cassandra.lucene.builder.Builder.match;
import static com.stratio.cassandra.lucene.builder.Builder.range;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Eduardo Alonso {@literal <eduardoalonso@stratio.com>}
 */

@RunWith(JUnit4.class)
public class UDTIndexingAT extends BaseAT {

    private static CassandraUtils cassandraUtils;

    @BeforeClass
    public static void before() {

        cassandraUtils = CassandraUtils.builder("udt_indexing").build();
        cassandraUtils.createKeyspace();

        String useKeyspaceQuery = "USE " + cassandraUtils.getKeyspace() + " ;";
        UDT geoPointUDT = new UDT("geo_point");
        geoPointUDT.add("latitude", "float");
        geoPointUDT.add("longitude", "float");

        UDT addressUDT = new UDT("address");
        addressUDT.add("street", "text");
        addressUDT.add("city", "text");
        addressUDT.add("zip", "int");
        addressUDT.add("bool", "boolean");
        addressUDT.add("height", "float");
        addressUDT.add("point", "frozen<geo_point>");
        addressUDT.add("zips", "list<int>");
        addressUDT.add("zips_map", "map<int,text>");
        addressUDT.add("zips_set", "set<int>");

        String tableCreationQuery = "CREATE TABLE " +
                                    cassandraUtils.getTable() +
                                    " ( login text PRIMARY KEY, first_name text, last_name text, address frozen<address>, lucene text);";

        cassandraUtils.execute(new SimpleStatement(useKeyspaceQuery));
        cassandraUtils.execute(new SimpleStatement(geoPointUDT.toString()));
        cassandraUtils.execute(new SimpleStatement(addressUDT.toString()));
        cassandraUtils.execute(new SimpleStatement(tableCreationQuery));

        String createIndexQuery = "CREATE CUSTOM INDEX " +
                                  cassandraUtils.getIndexName() +
                                  " ON " +
                                  cassandraUtils.getKeyspace() +
                                  "." +
                                  cassandraUtils.getTable() +
                                  "(lucene) " +
                                  "USING 'com.stratio.cassandra.lucene.Index' " +
                                  "WITH OPTIONS = { " +
                                  "'refresh_seconds' : '1'," +
                                  "'schema' : '{" +
                                  "fields : { " +
                                  "\"address.zips\" : {type:\"integer\"}," +
                                  "\"address.zips_map\" : {type:\"string\"}, " +
                                  "\"address.zips_set\" : {type:\"integer\"}, " +
                                  "\"address.bool\" : {type:\"boolean\"}, " +
                                  "\"address.city\" : {type:\"string\"}, " +
                                  "\"address.point.latitude\" : {type:\"float\"}, " +
                                  "\"address.point.longitude\" : {type:\"float\"}}}'};";

        cassandraUtils.execute(new SimpleStatement(createIndexQuery));

        String insert = "INSERT INTO " +
                        cassandraUtils.getKeyspace() +
                        "." +
                        cassandraUtils.getTable() +
                        "(login, first_name, last_name, address) " +
                        "VALUES (" +
                        "'USER1'," +
                        "'Tom'," +
                        "'Smith'," +
                        "{ " +
                        "street: '1021 West 4th St. #202'," +
                        "city: 'San Francisco'," +
                        "zip: 94110 ," +
                        "bool: true," +
                        "height:5.4 ," +
                        "zips:[ 2,4,6 ]," +
                        "zips_map : " +
                        "{ 1 : '1A'," +
                        "2 : '2A'," +
                        "3 : '3A'}," +
                        "zips_set : {5,7,9}," +
                        "point : {" +
                        "latitude : 1.0," +
                        "longitude : -1.0" +
                        "}  " +
                        "});";

        String insert2 = "INSERT INTO " +
                         cassandraUtils.getKeyspace() +
                         "." +
                         cassandraUtils.getTable() +
                         "(login, first_name, last_name, address) " +
                         "VALUES ('USER2','Tom','Smith'," +
                         "{ " +
                         "street: '1021 West 4th St. #202'," +
                         "city: 'San Francisco'," +
                         "zip: 94110 ," +
                         "bool: false," +
                         "height:5.4 ," +
                         "zips:[ 4,6,8 ]," +
                         "zips_map : " +
                         "{ 1 : '1B'," +
                         "2 : '2B'," +
                         "3 : '3B'}," +
                         "zips_set : {7,9,11}," +
                         "point : {" +
                         "latitude : 2.0," +
                         "longitude : -2.0" +
                         "}  " +
                         "});";

        String insert3 = "INSERT INTO " +
                         cassandraUtils.getKeyspace() +
                         "." +
                         cassandraUtils.getTable() +
                         "(login, first_name, last_name, address) " +
                         "VALUES ('USER3','Tom','Smith'," +
                         "{ " +
                         "street: '1021 West 4th St. #202'," +
                         "city: 'San Francisco'," +
                         "zip: 94110 ," +
                         "bool: true," +
                         "height:5.4 ," +
                         "zips:[ 6,8,10 ]," +
                         "zips_map : " +
                         "{ 1 : '1A'," +
                         "2 : '2A'," +
                         "3 : '3A'}," +
                         "zips_set : {9,11,13}," +
                         "point : {" +
                         "latitude : 3.0," +
                         "longitude : -3.0" +
                         "}  " +
                         "});";

        String insert4 = "INSERT INTO " +
                         cassandraUtils.getKeyspace() +
                         "." +
                         cassandraUtils.getTable() +
                         "(login, first_name, last_name, address) " +
                         "VALUES ('USER4','Tom','Smith'," +
                         "{ " +
                         "street: '1021 West 4th St. #202'," +
                         "city: 'Paris'," +
                         "zip: 94110 ," +
                         "bool: false," +
                         "height:5.4 ," +
                         "zips:[ 8,10,12 ]," +
                         "zips_map : " +
                         "{ 1 : '1B'," +
                         "2 : '2B'," +
                         "3 : '3B'}," +
                         "zips_set : {11,13,15}," +
                         "point : {" +
                         "latitude : 4.0," +
                         "longitude : -4.0" +
                         "}  " +
                         "});";

        String insert5 = "INSERT INTO " +
                         cassandraUtils.getKeyspace() +
                         "." +
                         cassandraUtils.getTable() +
                         "(login, first_name, last_name, address) " +
                         "VALUES ('USER5','Tom','Smith'," +
                         "{ " +
                         "street: '1021 West 4th St. #202'," +
                         "city: 'Paris'," +
                         "zip: 94110 ," +
                         "bool: true," +
                         "height:5.4 ," +
                         "zips:[ 10,12,14]," +
                         "zips_map : " +
                         "{ 1 : '1A'," +
                         "2 : '2A'," +
                         "3 : '3A'}," +
                         "zips_set : {13,15,17}," +
                         "point : {" +
                         "latitude : 5.0," +
                         "longitude : -5.0" +
                         "}  " +
                         "});";

        String insert6 = "INSERT INTO " +
                         cassandraUtils.getKeyspace() +
                         "." +
                         cassandraUtils.getTable() +
                         "(login, first_name, last_name, address) " +
                         "VALUES ('USER6','Tom','Smith'," +
                         "{ " +
                         "street: '1021 West 4th St. #202'," +
                         "city: 'Paris'," +
                         "zip: 94110 ," +
                         "bool: false," +
                         "height:5.4 ," +
                         "zips:[ 12,14,16 ]," +
                         "zips_map : " +
                         "{ 1 : '1B'," +
                         "2 : '2B'," +
                         "3 : '3B'}," +
                         "zips_set : {15,17,19}," +
                         "point : {" +
                         "latitude : 6.0," +
                         "longitude : -6.0" +
                         "}  " +
                         "});";

        String insert7 = "INSERT INTO " +
                         cassandraUtils.getKeyspace() +
                         "." +
                         cassandraUtils.getTable() +
                         "(login, first_name, last_name, address) " +
                         "VALUES ('USER7','Tom','Smith'," +
                         "{ " +
                         "street: '1021 West 4th St. #202'," +
                         "city: 'Paris'," +
                         "zip: 94110 ," +
                         "bool: true," +
                         "height:5.4 ," +
                         "zips:[ 14,16,18 ]," +
                         "zips_map : " +
                         "{ 1 : '1A'," +
                         "2 : '2A'," +
                         "3 : '3A'}," +
                         "zips_set : {17,19,21}," +
                         "point : {" +
                         "latitude : 7.0," +
                         "longitude : -7.0" +
                         "}  " +
                         "});";

        cassandraUtils.execute(new SimpleStatement(insert));
        cassandraUtils.execute(new SimpleStatement(insert2));
        cassandraUtils.execute(new SimpleStatement(insert3));
        cassandraUtils.execute(new SimpleStatement(insert4));
        cassandraUtils.execute(new SimpleStatement(insert5));
        cassandraUtils.execute(new SimpleStatement(insert6));
        cassandraUtils.execute(new SimpleStatement(insert7));
        cassandraUtils.refresh();

    }

    @AfterClass
    public static void after() {
        cassandraUtils.dropTable().dropKeyspace();
    }

    private boolean isThisAndOnlyThis(String[] received, String[] expected) {
        if (received.length != expected.length) {
            return false;
        } else {

            for (String i : received) {
                boolean found = false;
                for (String j : expected) {
                    if (i.equals(j)) {
                        found = true;
                    }
                }
                if (!found) {
                    return false;
                }
            }
            return true;
        }
    }

    private void assertEqualsAndOnlyThisString(String[] received, String[] expected) {
        assertEquals("Expected " + expected.length + " results but received: " + received.length,
                     expected.length,
                     received.length);
        assertTrue("Unexpected results!! Expected: " + Arrays.toString(expected) + ",but got: " + received.toString(),
                   isThisAndOnlyThis(received, expected));

    }

    @Test
    public void testUDTInternal() {

        CassandraUtilsSelect select = cassandraUtils.filter(match("address.city", "Paris"));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER4", "USER5", "USER6", "USER7"});

        select = cassandraUtils.filter(match("address.city", "San Francisco"));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER1", "USER2", "USER3"});

        select = cassandraUtils.filter(match("address.bool", true));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER1", "USER3", "USER5", "USER7"});

        select = cassandraUtils.filter(match("address.bool", false));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER2", "USER4", "USER6"});

    }

    @Test(expected = InvalidQueryException.class)
    public void testUDTInternalThatFails() {

        CassandraUtilsSelect select = cassandraUtils.filter(match("address.point", "Paris"));
        select.count();
        assertTrue("Selecting a type that is no matched must return an Exception", true);
    }

    @Test
    public void testUDTList() {

        CassandraUtilsSelect select = cassandraUtils.filter(match("address.zips", 10));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER3", "USER4", "USER5"});

        select = cassandraUtils.filter(match("address.zips", 12));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER4", "USER5", "USER6"});

        select = cassandraUtils.filter(match("address.zips", 14));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER5", "USER6", "USER7"});

        select = cassandraUtils.filter(match("address.zips", 15));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{});

        select = cassandraUtils.filter(match("address.zips", 16));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER6", "USER7"});

        select = cassandraUtils.filter(match("address.zips", 18));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER7"});
    }

    @Test
    public void testUDTMap() {
        CassandraUtilsSelect select = cassandraUtils.filter(match("address.zips_map$1", "1A")).refresh(true);
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER1", "USER3", "USER5", "USER7"});

        select = cassandraUtils.filter(match("address.zips_map$1", "1B"));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER2", "USER4", "USER6"});

        select = cassandraUtils.filter(match("address.zips_map$2", "2A"));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER1", "USER3", "USER5", "USER7"});

        select = cassandraUtils.filter(match("address.zips_map$2", "2B"));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER2", "USER4", "USER6"});

        select = cassandraUtils.filter(match("address.zips_map$3", "3A"));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER1", "USER3", "USER5", "USER7"});

        select = cassandraUtils.filter(match("address.zips_map$3", "3B"));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER2", "USER4", "USER6"});
    }

    @Test
    public void testUDTMapThatFails() {

        CassandraUtilsSelect select = cassandraUtils.filter(match("address.zips_map", 1));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{});
    }

    @Test
    public void testUDTSet() {
        CassandraUtilsSelect select = cassandraUtils.filter(match("address.zips_set", 5));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER1"});

        select = cassandraUtils.filter(match("address.zips_set", 7));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER1", "USER2"});

        select = cassandraUtils.filter(match("address.zips_set", 9));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER1", "USER2", "USER3"});

        select = cassandraUtils.filter(match("address.zips_set", 11));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER2", "USER3", "USER4"});

        select = cassandraUtils.filter(match("address.zips_set", 12));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{});

        select = cassandraUtils.filter(match("address.zips_set", 13));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER3", "USER4", "USER5"});

        select = cassandraUtils.filter(match("address.zips_set", 14));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{});

        select = cassandraUtils.filter(match("address.zips_set", 15));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER4", "USER5", "USER6"});

        select = cassandraUtils.filter(match("address.zips_set", 17));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER5", "USER6", "USER7"});

        select = cassandraUtils.filter(match("address.zips_set", 19));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER6", "USER7"});

        select = cassandraUtils.filter(match("address.zips_set", 20));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{});

        select = cassandraUtils.filter(match("address.zips_set", 21));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER7"});
    }

    @Test
    public void testUDTOverUDT() {
        CassandraUtilsSelect select = cassandraUtils.filter(match("address.point.latitude", 1.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER1"});

        select = cassandraUtils.filter(match("address.point.latitude", 2.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER2"});

        select = cassandraUtils.filter(match("address.point.latitude", 3.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER3"});

        select = cassandraUtils.filter(match("address.point.latitude", 4.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER4"});

        select = cassandraUtils.filter(match("address.point.latitude", 5.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER5"});

        select = cassandraUtils.filter(match("address.point.latitude", 6.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER6"});

        select = cassandraUtils.filter(match("address.point.latitude", 7.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER7"});

        select = cassandraUtils.filter(match("address.point.longitude", -1.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER1"});

        select = cassandraUtils.filter(match("address.point.longitude", -2.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER2"});

        select = cassandraUtils.filter(match("address.point.longitude", -3.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER3"});

        select = cassandraUtils.filter(match("address.point.longitude", -4.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER4"});

        select = cassandraUtils.filter(match("address.point.longitude", -5.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER5"});

        select = cassandraUtils.filter(match("address.point.longitude", -6.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER6"});

        select = cassandraUtils.filter(match("address.point.longitude", -7.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER7"});

        select = cassandraUtils.filter(range("address.point.latitude").lower(1.0)
                                                                      .upper(3.0)
                                                                      .includeLower(true)
                                                                      .includeUpper(true));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER1", "USER2", "USER3"});

        select = cassandraUtils.filter(range("address.point.latitude").lower(2.0)
                                                                      .upper(5.0)
                                                                      .includeLower(true)
                                                                      .includeUpper(true));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER2", "USER3", "USER4", "USER5"});

        select = cassandraUtils.filter(range("address.point.latitude").lower(1.0)
                                                                      .upper(7.0)
                                                                      .includeLower(true)
                                                                      .includeUpper(true));
        assertEqualsAndOnlyThisString(select.stringColumn("login"),
                                      new String[]{"USER1", "USER2", "USER3", "USER4", "USER5", "USER6", "USER7"});

        select = cassandraUtils.filter(range("address.point.longitude").lower(-3.0).upper(-1.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER2"});

        select = cassandraUtils.filter(range("address.point.longitude").lower(-5.0).upper(-2.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER3", "USER4"});

        select = cassandraUtils.filter(range("address.point.longitude").lower(-7.0).upper(-1.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"),
                                      new String[]{"USER2", "USER3", "USER4", "USER5", "USER6"});

        select = cassandraUtils.filter(range("address.point.latitude").lower(1.0)
                                                                      .upper(3.0)
                                                                      .includeLower(true)
                                                                      .includeUpper(true));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER1", "USER2", "USER3"});

        select = cassandraUtils.filter(range("address.point.latitude").lower(2.0)
                                                                      .upper(5.0)
                                                                      .includeLower(true)
                                                                      .includeUpper(true));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER2", "USER3", "USER4", "USER5"});

        select = cassandraUtils.filter(range("address.point.latitude").lower(1.0)
                                                                      .upper(7.0)
                                                                      .includeLower(true)
                                                                      .includeUpper(true));
        assertEqualsAndOnlyThisString(select.stringColumn("login"),
                                      new String[]{"USER1", "USER2", "USER3", "USER4", "USER5", "USER6", "USER7"});

        select = cassandraUtils.filter(range("address.point.longitude").lower(-3.0).upper(-1.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER2"});

        select = cassandraUtils.filter(range("address.point.longitude").lower(-5.0).upper(-2.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER3", "USER4"});

        select = cassandraUtils.filter(range("address.point.longitude").lower(-7.0).upper(-1.0));
        assertEqualsAndOnlyThisString(select.stringColumn("login"),
                                      new String[]{"USER2", "USER3", "USER4", "USER5", "USER6"});
    }

    @Test(expected = InvalidQueryException.class)
    public void testUDTOverUDTThatFails() {
        cassandraUtils.filter(range("address.point.non-existent").lower(-1.0).upper(-3.0)).get();
        assertTrue("Selecting a non-existent type inside udt inside udt must return an Exception", true);
    }

    @Test
    public void testNonCompleteUDT() {

        String insert = "INSERT INTO " +
                        cassandraUtils.getKeyspace() +
                        "." +
                        cassandraUtils.getTable() +
                        "(login, first_name, last_name, address) VALUES (" +
                        "'USER10'," +
                        "'Tom'," +
                        "'Smith',{" +
                        "city: 'Madrid'});";

        cassandraUtils.execute(new SimpleStatement(insert));
        cassandraUtils.refresh();

        CassandraUtilsSelect select = cassandraUtils.filter(match("address.city", "Madrid"));
        assertEqualsAndOnlyThisString(select.stringColumn("login"), new String[]{"USER10"});
    }

}
