/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.app.accessgroup;

import com.haulmont.cuba.gui.app.security.group.browse.GroupBrowser;
import com.haulmont.cuba.gui.app.security.group.browse.GroupChangeEvent;
import com.haulmont.cuba.gui.app.security.group.browse.UserGroupChangedEvent;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.components.data.TableItems;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.widgets.CubaTable;
import com.haulmont.cuba.web.widgets.CubaTableDragSourceExtension;
import com.haulmont.cuba.web.widgets.CubaTree;
import com.vaadin.shared.ui.grid.DropMode;
import com.vaadin.ui.components.grid.TreeGridDropTarget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class AccessGroupCompanion implements GroupBrowser.Companion {

    @Override
    public void initDragAndDrop(Table<User> usersTable, Tree<Group> groupsTree,
                                Consumer<UserGroupChangedEvent> userGroupChangedHandler,
                                Consumer<GroupChangeEvent> groupChangeEventHandler) {
        CubaTable vTable = usersTable.unwrap(CubaTable.class);

        CubaTableDragSourceExtension<CubaTable> dragTable = new CubaTableDragSourceExtension<>(vTable);

        //noinspection unchecked
        CubaTree<Group> vTree = groupsTree.unwrap(CubaTree.class);
        TreeGridDropTarget<Group> treeGridDropTarget = new TreeGridDropTarget<>(vTree.getCompositionRoot(), DropMode.ON_TOP_OR_BETWEEN);
        treeGridDropTarget.addTreeGridDropListener(event -> {
            if (event.getDragSourceExtension().isPresent()) {
                //noinspection unchecked
                CubaTableDragSourceExtension<CubaTable> sourceExtension =
                        (CubaTableDragSourceExtension<CubaTable>) event.getDragSourceExtension().get();

                List<Object> itemIds = sourceExtension.getLastTransferredItems();
                TableItems<User> tableItems = usersTable.getItems();


                List<User> users = new ArrayList<>();
                for (Object id : itemIds) {
                    users.add(tableItems.getItem(id));
                }

                if (event.getDropTargetRow().isPresent()) {
                    Group group = event.getDropTargetRow().get();
                    userGroupChangedHandler.accept(new UserGroupChangedEvent(groupsTree, users, group));
                }
            }
        });


        /*CubaTree<Group> vTree = groupsTree.unwrap(CubaTree.class);
        // TODO: gg, re-implement
//        vTree.setDragMode(com.vaadin.v7.ui.Tree.TreeDragMode.NODE);
        vTree.setDropHandler(new DropHandler() {
            @Override
            public void drop(DragAndDropEvent dropEvent) {
                DataBoundTransferable transferable = (DataBoundTransferable) dropEvent.getTransferable();

                AbstractSelect.AbstractSelectTargetDetails dropData =
                        ((AbstractSelect.AbstractSelectTargetDetails) dropEvent.getTargetDetails());

                Component sourceComponent = transferable.getSourceComponent();

                Object dropOverId = dropData.getItemIdOver();
                Object itemId = transferable.getItemId();

                List<User> users = null;
                if (sourceComponent instanceof com.vaadin.v7.ui.Table) {
                    users = new ArrayList<>(usersTable.getSelected());
                } else if (sourceComponent instanceof com.vaadin.v7.ui.Tree) {
                    if (itemId == null) {
                        return;
                    }

                    // if we don't drop to itself and don't drop parent to child
                    if (!itemId.equals(dropOverId) && isNotContainDropOver(itemId, dropOverId, vTree)) {

                        Group itemGroup = convertToEntity(vTree.getItem(itemId), Group.class);
                        Group dropOverGroup = convertToEntity(vTree.getItem(dropOverId), Group.class);

                        if (itemGroup != null) {

                            // if we drop to the same parent
                            if ((itemGroup.getParent() != null && dropOverGroup != null)
                                    && (itemGroup.getParent().getId().equals(dropOverGroup.getId()))) {
                                return;
                            }

                            groupChangeEventHandler.accept(new GroupChangeEvent(groupsTree, itemGroup.getId(),
                                    dropOverGroup == null ? null : dropOverGroup.getId()));
                        }
                    }
                }

                if (users == null) {
                    return;
                }

                if (users.isEmpty()) {
                    User user = convertToEntity(vTable.getItem(transferable.getItemId()), User.class);
                    users.add(user);
                }

                final Object targetItemId = dropData.getItemIdOver();
                if (targetItemId == null) {
                    return;
                }
                Group group = convertToEntity(vTree.getItem(targetItemId), Group.class);
                if (group == null) {
                    return;
                }

                userGroupChangedHandler.accept(new UserGroupChangedEvent(groupsTree, users, group));
            }

            @Override
            public AcceptCriterion getAcceptCriterion() {
                return new And(
                        new Not(AbstractSelect.VerticalLocationIs.BOTTOM),
                        new Not(AbstractSelect.VerticalLocationIs.TOP));
            }
        });*/
    }

    protected boolean isNotContainDropOver(Group group, Group dropOver, CubaTree<Group> vTree) {
        if (!vTree.hasChildren(group)) {
            return true;
        }

        return checkAllChildrenRecursively(vTree.getChildren(group), dropOver, vTree);
    }

    protected boolean checkAllChildrenRecursively(Collection<Group> children, Group dropOver, CubaTree<Group> vTree) {
        for (Group group : children) {
            if (group.equals(dropOver)) {
                return false;
            } else if (vTree.hasChildren(group)) {
                if (!checkAllChildrenRecursively(vTree.getChildren(group), dropOver, vTree)) {
                    return false;
                }
            }
        }

        return true;
    }

    /*@Nullable
    protected <T extends Entity> T convertToEntity(@Nullable Item item, Class<T> entityClass) {
        if (!(item instanceof ItemWrapper)) {
            return null;
        }
        Entity entity = ((ItemWrapper) item).getItem();
        if (!entityClass.isAssignableFrom(entity.getClass())) {
            return null;
        }
        //noinspection unchecked
        return (T) entity;
    }*/
}
