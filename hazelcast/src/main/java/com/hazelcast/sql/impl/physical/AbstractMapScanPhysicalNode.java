/*
 * Copyright (c) 2008-2019, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.sql.impl.physical;

import com.hazelcast.internal.serialization.impl.SerializationUtil;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.sql.impl.expression.Expression;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Base class to scan a map.
 */
public abstract class AbstractMapScanPhysicalNode implements PhysicalNode {
    /** Map name. */
    protected String mapName;

    /** Field names. */
    protected List<String> fieldNames;

    /** Projects. */
    protected List<Integer> projects;

    /** Filter. */
    protected Expression<Boolean> filter;

    protected AbstractMapScanPhysicalNode() {
        // No-op.
    }

    protected AbstractMapScanPhysicalNode(
        String mapName,
        List<String> fieldNames,
        List<Integer> projects,
        Expression<Boolean> filter
    ) {
        this.mapName = mapName;
        this.fieldNames = fieldNames;
        this.projects = projects;
        this.filter = filter;
    }

    public String getMapName() {
        return mapName;
    }

    public List<String> getFieldNames() {
        return fieldNames;
    }

    public List<Integer> getProjects() {
        return projects;
    }

    public Expression<Boolean> getFilter() {
        return filter;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(mapName);
        SerializationUtil.writeList(fieldNames, out);
        SerializationUtil.writeList(projects, out);
        out.writeObject(filter);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        mapName = in.readUTF();
        fieldNames = SerializationUtil.readList(in);
        projects = SerializationUtil.readList(in);
        filter = in.readObject();
    }

    @Override
    public int hashCode() {
        return Objects.hash(mapName, fieldNames, projects, filter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractMapScanPhysicalNode that = (AbstractMapScanPhysicalNode) o;

        return mapName.equals(that.mapName)
            && fieldNames.equals(that.fieldNames)
            && projects.equals(that.projects)
            && Objects.equals(filter, that.filter);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{mapName=" + mapName + ", fieldNames=" + fieldNames
            + ", projects=" + projects + ", filter=" + filter + '}';
    }
}