/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.web.widgets.client.html;

import com.vaadin.shared.communication.SharedState;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class HtmlAttributesExtensionState extends SharedState {

    public static final String DEFAULT_SELECTOR = "<default>";  // query selector that represents the most top element of UI component

    public Map<String, Set<AttributeInfo>> attributes = Collections.emptyMap();
}