/*
 * Copyright (c) 2008-2019 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.table.CubaTableDragSourceExtensionServerRpc;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.v7.ui.Table;

import java.util.ArrayList;
import java.util.List;

public class CubaTableDragSourceExtension<T extends Table> extends DragSourceExtension<T> {

    protected List<Object> transferredItems = new ArrayList<>();

    protected CubaEnhancedTable enhancedTable;

    public CubaTableDragSourceExtension(T target) {
        super(target);

        if (target instanceof CubaEnhancedTable) {
            enhancedTable = (CubaEnhancedTable) target;
        }

        CubaTableDragSourceExtensionServerRpc serverRpc = (CubaTableDragSourceExtensionServerRpc) rowKeys -> {
            transferredItems.clear();

            if (enhancedTable != null) {
                for (String key : rowKeys)
                   transferredItems.add(enhancedTable.getItemByRowKey(key));
            }
        };

        registerRpc(serverRpc);
    }

    public List<Object> getLastTransferredItems() {
        return transferredItems;
    }

    public Object getLastSingleTransferedItem() {
        return transferredItems.isEmpty() ? null : transferredItems.iterator().next();
    }
}
