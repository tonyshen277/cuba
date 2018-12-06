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

package spec.cuba.core.enhancedsetget

import com.haulmont.chile.core.model.MetaClass
import com.haulmont.cuba.core.entity.StandardEntity
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.testmodel.enhancedsetget.EnhancedSetGetEntity
import com.haulmont.cuba.testmodel.many2many.Many2ManyA
import com.haulmont.cuba.testmodel.many2many.Many2ManyB
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class EnhancedSetGetTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private Metadata metadata = cont.metadata()

    private EnhancedSetGetEntity<String> enhancedSetGetEntity

    void setup() {
        enhancedSetGetEntity = metadata.create(EnhancedSetGetEntity.class)
    }

    def "ID and UUID"() {
        when:
        UUID uuid = enhancedSetGetEntity.getValue("uuid")
        enhancedSetGetEntity.setValue("id", uuid)
        UUID afterId = enhancedSetGetEntity.getValue("id")

        then:
        uuid == afterId && uuid != null
    }

    def "Map"() {
        when:
        Map<String, Integer> map = new HashMap<>()
        map.put("key", 12)
        enhancedSetGetEntity.setValue("map", map)
        Map<String, Integer> afterMap = enhancedSetGetEntity.getValue("map")

        then:
        map == afterMap
    }

    def "Array"() {
        when:
        int[] intArray = new int[1]
        intArray[0] = 12
        enhancedSetGetEntity.setValue("intArray", intArray)
        int[] afterIntArray = enhancedSetGetEntity.getValue("intArray")

        then:
        intArray == afterIntArray
    }

    def "StandardEntity"() {
        when:
        StandardEntity[] standardEntityArray = new StandardEntity[1]
        standardEntityArray[0] = cont.metadata().create(StandardEntity.class)
        enhancedSetGetEntity.setValue("standardEntityArray", standardEntityArray)
        StandardEntity[] afterStandardEntityArray = enhancedSetGetEntity.getValue("standardEntityArray")

        then:
        standardEntityArray == afterStandardEntityArray
    }

    def "Generic field"() {
        when:
        String genericField = "12"
        enhancedSetGetEntity.setValue("genericField", genericField)
        String afterGenericValue = enhancedSetGetEntity.getValue("genericField")

        then:
        genericField == afterGenericValue
    }

    def "Generic map"() {
        when:
        Map<String, Integer> genericMap = new HashMap<>()
        genericMap.put("key", 12)
        enhancedSetGetEntity.setValue("genericMap", genericMap)
        Map<String, Integer> afterGenericMap = enhancedSetGetEntity.getValue("genericMap")

        then:
        genericMap == afterGenericMap
    }

    def "Generic array"() {
        when:
        String[] genericArray = new String[1]
        genericArray[0] = "12"
        enhancedSetGetEntity.setValue("genericArray", genericArray)
        String[] afterGenericArray = enhancedSetGetEntity.getValue("genericArray")

        then:
        genericArray == afterGenericArray
    }

    def "MetaClass"() {
        when:
        MetaClass metaClass = enhancedSetGetEntity.getValue("metaClass")

        then:
        metaClass != null
    }


    def "Many to many"() {
        when:
        Set<Many2ManyB> collectionOfB = new HashSet<>()
        collectionOfB.add(cont.metadata().create(Many2ManyB.class))
        Many2ManyA many2ManyA = cont.metadata().create(Many2ManyA.class)
        many2ManyA.setValue("collectionOfB", collectionOfB)
        Set<Many2ManyB> afterCollectionOfB = many2ManyA.getValue("collectionOfB")

        then:
        collectionOfB == afterCollectionOfB
    }
}
