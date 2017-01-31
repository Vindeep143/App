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
package com.stratio.cassandra.lucene.builder.search.sort;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * A geo spatial distance search sort.
 *
 * @author Eduardo Alonso {@literal <eduardoalonso@stratio.com>}
 */
public class GeoDistanceSortField extends SortField {

    /** The name of mapper to use to calculate distance. */
    @JsonProperty("field")
    private final String field;

    /** The longitude of the center point to sort by distance to it. */
    @JsonProperty("longitude")
    private final double longitude;

    /** The latitude of the center point to sort by distance to it. */
    @JsonProperty("latitude")
    private final double latitude;

    /**
     * Creates a new {@link GeoDistanceSortField} for the specified field and reverse option.
     *
     * @param field the name of the field to be used for sort
     * @param longitude the longitude in degrees of the reference point
     * @param latitude the latitude in degrees of the reference point
     */
    @JsonCreator
    public GeoDistanceSortField(@JsonProperty("field") String field,
                                @JsonProperty("longitude") double longitude,
                                @JsonProperty("latitude") double latitude) {
        this.field = field;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
