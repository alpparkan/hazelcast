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

package com.hazelcast.sql.impl.physical.io;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.sql.impl.physical.PhysicalNode;
import com.hazelcast.sql.impl.physical.visitor.PhysicalNodeVisitor;
import com.hazelcast.sql.impl.physical.UniInputPhysicalNode;

import java.io.IOException;
import java.util.Objects;

/**
 * Broadcast send node.
 */
public class BroadcastSendPhysicalNode extends UniInputPhysicalNode implements EdgeAwarePhysicalNode {
    /** Edge ID. */
    private int edgeId;

    public BroadcastSendPhysicalNode() {
        // No-op.
    }

    public BroadcastSendPhysicalNode(int id, PhysicalNode upstream, int edgeId) {
        super(id, upstream);

        this.edgeId = edgeId;
    }

    @Override
    public int getEdgeId() {
        return edgeId;
    }

    @Override
    public boolean isSender() {
        return true;
    }

    @Override
    public void visit0(PhysicalNodeVisitor visitor) {
        visitor.onBroadcastSendNode(this);
    }

    @Override
    public void writeData1(ObjectDataOutput out) throws IOException {
        out.writeInt(edgeId);
    }

    @Override
    public void readData1(ObjectDataInput in) throws IOException {
        edgeId = in.readInt();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, edgeId, upstream);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BroadcastSendPhysicalNode that = (BroadcastSendPhysicalNode) o;

        return id == that.id && edgeId == that.edgeId && upstream.equals(that.upstream);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + ", edgeId=" + edgeId + ", upstream=" + upstream + '}';
    }
}
