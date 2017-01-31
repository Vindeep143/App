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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Eduardo Alonso {@literal <eduardoalonso@stratio.com>}
 */
public class UDT {
    private Map<String, String> map;
    private String name;

    public UDT(String name) {
        map = new HashMap<>();
        this.name = name;
    }

    public UDT add(String name, String type) {
        map.put(name, type);
        return this;
    }

    public String toString(String keyspace) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TYPE ");
        if (keyspace != null) {
            sb.append(keyspace).append(".");
        }
        sb.append(name);
        sb.append(" ( ");
        Set<String> set = this.map.keySet();
        Iterator<String> iterator = set.iterator();
        for (int i = 0; i < set.size(); i++) {
            String key = iterator.next();
            sb.append(key);
            sb.append(" ");
            sb.append(map.get(key));
            if (i < (set.size() - 1)) {
                sb.append(", ");
            }
        }
        sb.append(");");
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString(null);
    }
}
