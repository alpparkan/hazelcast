/*
 * Copyright (c) 2008-2020, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.sql.impl.explain;

import com.hazelcast.sql.impl.QueryMetadata;
import com.hazelcast.sql.impl.row.HeapRow;
import com.hazelcast.sql.impl.row.Row;
import com.hazelcast.sql.impl.type.QueryDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Query plan descriptor.
 */
public final class QueryExplain {
    public static final QueryMetadata EXPLAIN_METADATA;

    static {
        List<QueryDataType> explainColumnTypes = new ArrayList<>(1);

        explainColumnTypes.add(QueryDataType.VARCHAR);

        EXPLAIN_METADATA = new QueryMetadata(explainColumnTypes);
    }

    /** Original SQL. */
    private final String sql;

    /** Elements.  */
    private final List<QueryExplainElement> elements;

    public QueryExplain(String sql, List<QueryExplainElement> elements) {
        this.sql = sql;
        this.elements = elements;
    }

    public List<Row> asRows() {
        List<Row> rows = new ArrayList<>(elements.size());

        for (QueryExplainElement element : elements) {
            String elementString = elementAsString(element);

            HeapRow row = new HeapRow(1);
            row.set(0, elementString);

            rows.add(row);
        }

        return rows;
    }

    public String getSql() {
        return sql;
    }

    public List<QueryExplainElement> getElements() {
        return elements;
    }

    private static String elementAsString(QueryExplainElement element) {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < element.getLevel(); i++) {
            res.append("  ");
        }

        res.append(element.getExplain());

        return res.toString();
    }
}