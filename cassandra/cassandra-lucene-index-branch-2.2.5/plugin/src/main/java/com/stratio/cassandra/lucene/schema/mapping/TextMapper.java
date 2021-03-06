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
package com.stratio.cassandra.lucene.schema.mapping;

import com.stratio.cassandra.lucene.IndexException;
import org.apache.cassandra.db.marshal.*;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.search.SortField;

/**
 * A {@link Mapper} to map a string, tokenized field.
 *
 * @author Andres de la Pena {@literal <adelapena@stratio.com>}
 */
public class TextMapper extends SingleColumnMapper.SingleFieldMapper<String> {

    /**
     * Builds a new {@link TextMapper} using the specified Lucene {@link org.apache.lucene.analysis.Analyzer}.
     *
     * @param field the name of the field
     * @param column the name of the column to be mapped
     * @param validated if the field must be validated
     * @param analyzer the name of the Lucene {@link org.apache.lucene.analysis.Analyzer} to be used
     */
    public TextMapper(String field, String column, Boolean validated, String analyzer) {
        super(field,
              column,
              false,
              validated,
              analyzer,
              String.class,
              AsciiType.instance,
              BooleanType.instance,
              BytesType.instance,
              ByteType.instance,
              DoubleType.instance,
              FloatType.instance,
              InetAddressType.instance,
              IntegerType.instance,
              Int32Type.instance,
              LongType.instance,
              ShortType.instance,
              TimestampType.instance,
              TimeUUIDType.instance,
              UTF8Type.instance,
              UUIDType.instance);
    }

    /** {@inheritDoc} */
    @Override
    protected String doBase(String name, Object value) {
        return value.toString();
    }

    /** {@inheritDoc} */
    @Override
    public Field indexedField(String name, String value) {
        return new TextField(name, value, STORE);
    }

    /** {@inheritDoc} */
    @Override
    public Field sortedField(String name, String value) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public SortField sortField(String name, boolean reverse) {
        throw new IndexException("Text mapper '%s' does not support sorting because it is analyzed", name);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return toStringHelper(this).add("analyzer", analyzer).toString();
    }
}
