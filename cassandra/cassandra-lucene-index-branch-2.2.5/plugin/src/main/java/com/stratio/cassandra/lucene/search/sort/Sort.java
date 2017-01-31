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
package com.stratio.cassandra.lucene.search.sort;

import com.google.common.base.Objects;
import com.google.common.collect.Ordering;
import com.stratio.cassandra.lucene.schema.Schema;
import com.stratio.cassandra.lucene.schema.column.Columns;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * A sorting of fields for a search.
 *
 * @author Andres de la Pena {@literal <adelapena@stratio.com>}
 */
public class Sort implements Iterable<SortField> {

    /** How to sortFields each field. */
    private final List<SortField> sortFields;

    /**
     * Builds a new {@link Sort} for the specified {@link SortField}s.
     *
     * @param sortFields the sort fields
     */
    public Sort(List<SortField> sortFields) {
        this.sortFields = sortFields;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<SortField> iterator() {
        return sortFields.iterator();
    }

    /**
     * Returns the {@link SortField}s to be used.
     *
     * @return the sort fields
     */
    public List<SortField> getSortFields() {
        return sortFields;
    }

    /**
     * Returns the Lucene's {@link org.apache.lucene.search.SortField}s representing this {@link Sort}.
     *
     * @param schema the schema
     * @return the Lucene sort fields
     */
    public List<org.apache.lucene.search.SortField> sortFields(Schema schema) {
        List<org.apache.lucene.search.SortField> fields = new ArrayList<>(sortFields.size());
        for (SortField sortField : sortFields) {
            fields.add(sortField.sortField(schema));
        }
        return fields;
    }

    /**
     * Returns the {@link Columns} {@link Comparator} specified by this {@link Sort}.
     *
     * @param schema the schema
     * @return the equivalent columns comparator
     */
    public Comparator<Columns> comparator(Schema schema) {
        List<Comparator<Columns>> comparators = new ArrayList<>();
        for (com.stratio.cassandra.lucene.search.sort.SortField sortField : getSortFields()) {
            comparators.add(sortField.comparator(schema));
        }
        return Ordering.compound(comparators);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("sortFields", sortFields).toString();
    }
}
