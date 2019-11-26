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

package com.hazelcast.sql.impl.exec;

import com.hazelcast.sql.impl.QueryContext;
import com.hazelcast.sql.impl.row.EmptyRowBatch;
import com.hazelcast.sql.impl.row.RowBatch;

/**
 * Abstract executor.
 */
public abstract class AbstractExec implements Exec {
    /** Global query context. */
    protected QueryContext ctx;

    /** ID of the executor. */
    private final int id;

    protected AbstractExec(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public final void setup(QueryContext ctx) {
        this.ctx = ctx;

        setup0(ctx);
    }

    protected void setup0(QueryContext ctx) {
        // No-op.
    }

    @Override
    public final void reset() {
        if (!canReset()) {
            throw new UnsupportedOperationException("Reset is not supported: " + this.getClass().getSimpleName());
        }

        reset0();
    }

    /**
     * Internal reset routine.
     */
    protected void reset0() {
        // No-op.
    }

    @Override
    public final RowBatch currentBatch() {
        RowBatch res = currentBatch0();

        return res != null ? res : EmptyRowBatch.INSTANCE;
    }

    protected abstract RowBatch currentBatch0();
}
