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
package com.stratio.cassandra.lucene.service;

import com.google.common.base.Objects;
import org.apache.cassandra.db.DecoratedKey;
import org.apache.cassandra.db.composites.CellName;

/**
 * Class representing the primary key of a logical CQL row.
 *
 * @author Andres de la Pena {@literal <adelapena@stratio.com>}
 */
public class RowKey {

    /** The partition key. */
    private final DecoratedKey partitionKey;

    /** The clustering key. */
    private final CellName clusteringKey;

    /**
     * Builds a new row key.
     *
     * @param partitionKey The partition key.
     * @param clusteringKey The clustering key.
     */
    public RowKey(DecoratedKey partitionKey, CellName clusteringKey) {
        this.partitionKey = partitionKey;
        this.clusteringKey = clusteringKey;
    }

    /**
     * Returns the partition key.
     *
     * @return The partition key.
     */
    public DecoratedKey getPartitionKey() {
        return partitionKey;
    }

    /**
     * Returns the clustering key.
     *
     * @return The clustering key.
     */
    public CellName getClusteringKey() {
        return clusteringKey;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .add("partitionKey", partitionKey)
                      .add("clusteringKey", clusteringKey)
                      .toString();
    }
}
