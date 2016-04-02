/*
 *    Copyright 2016 Dmytro Titov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.autsia.codefreeze;

import com.autsia.codefreeze.impl.CGLIBCodeFreeze;
import com.autsia.codefreeze.model.TestEntity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.*;

import static org.testng.Assert.*;

/**
 * Proof immutability in some common cases
 */
public class CodeFreezeTest {

    private CodeFreeze codeFreeze = new CGLIBCodeFreeze();

    private TestEntity testEntity;

    @BeforeMethod
    public void setUp() throws Exception {
        TestEntity firstLevelEntity = createImmutabilityTestEntity();
        TestEntity secondLevelEntity = createImmutabilityTestEntity();
        TestEntity thirdLevelEntity = createImmutabilityTestEntity();
        TestEntity fourthLevelEntity = createImmutabilityTestEntity();
        firstLevelEntity.setInnerEntity(secondLevelEntity);
        secondLevelEntity.setInnerEntity(thirdLevelEntity);
        thirdLevelEntity.setInnerEntity(fourthLevelEntity);

        testEntity = codeFreeze.freeze(firstLevelEntity);
    }

    private TestEntity createImmutabilityTestEntity() {
        TestEntity testEntity = new TestEntity();
        testEntity.setId(BigInteger.ONE);
        testEntity.setMask(BigInteger.ONE.intValue());
        testEntity.setName(TestEntity.class.getSimpleName());
        List<TestEntity> list = new ArrayList<>();
        list.add(testEntity);
        testEntity.setEntityList(list);
        Set<TestEntity> set = new HashSet<>();
        set.add(testEntity);
        testEntity.setEntitySet(set);
        Map<TestEntity, TestEntity> map = new HashMap<>();
        map.put(testEntity, testEntity);
        testEntity.setEntityMap(map);
        return testEntity;
    }


    // First-level tests

    @Test
    public void testFirstLevelEntityGetBigInteger() throws Exception {
        assertEquals(testEntity.getId(), BigInteger.ONE);
    }

    @Test
    public void testFirstLevelEntityGetString() throws Exception {
        assertEquals(testEntity.getName(), TestEntity.class.getSimpleName());
    }

    @Test
    public void testFirstLevelEntityGetPrimitive() throws Exception {
        assertEquals(testEntity.getMask(), BigInteger.ONE.intValue());
    }

    @Test
    public void testFirstLevelEntityGetReference() throws Exception {
        assertNotNull(testEntity.getInnerEntity());
    }

    @Test
    public void testFirstLevelEntityGetFinalObjectAsObject() throws Exception {
        assertNotNull(testEntity.getFinalObjectAsObject());
    }

    @Test
    public void testFirstLevelEntityGetObjectWithoutParameterlessConstructorAsObject() throws Exception {
        assertNotNull(testEntity.getObjectWithoutParameterlessConstructorAsObject());
    }

    @Test
    public void testFirstLevelEntityGetList() throws Exception {
        assertNotNull(testEntity.getEntityList());
    }

    @Test
    public void testFirstLevelEntityGetSet() throws Exception {
        assertNotNull(testEntity.getEntitySet());
    }

    @Test
    public void testFirstLevelEntityGetMap() throws Exception {
        assertNotNull(testEntity.getEntityMap());
    }

    @Test
    public void testFirstLevelEntityIteration() throws Exception {
        Iterator<TestEntity> listIterator = testEntity.getEntityList().iterator();
        assertNotNull(listIterator.next());

        Iterator<TestEntity> setIterator = testEntity.getEntitySet().iterator();
        assertNotNull(setIterator.next());

        Iterator<Map.Entry<TestEntity, TestEntity>> mapIterator = testEntity.getEntityMap().entrySet().iterator();
        assertNotNull(mapIterator.next());
    }

    @Test
    public void testFirstLevelGetterWithInnerGetter() throws Exception {
        assertNotNull(testEntity.getThirdLevelEntity());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testFirstLevelEntitySetBigInteger() throws Exception {
        testEntity.setId(BigInteger.ONE);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testFirstLevelEntitySetString() throws Exception {
        testEntity.setName(TestEntity.class.getSimpleName());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testFirstLevelEntitySetPrimitive() throws Exception {
        testEntity.setMask(BigInteger.ONE.intValue());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testFirstLevelEntitySetReference() throws Exception {
        testEntity.setInnerEntity(testEntity);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testFirstLevelEntitySetList() throws Exception {
        testEntity.setEntityList(new ArrayList<>());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testFirstLevelEntitySetSet() throws Exception {
        testEntity.setEntitySet(new HashSet<>());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testFirstLevelEntitySetMap() throws Exception {
        testEntity.setEntityMap(new HashMap<>());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testFirstLevelEntityListFieldModification() throws Exception {
        testEntity.getEntityList().add(new TestEntity());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testFirstLevelEntitySetFieldModification() throws Exception {
        testEntity.getEntitySet().add(new TestEntity());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testFirstLevelEntityMapFieldModification() throws Exception {
        testEntity.getEntityMap().put(new TestEntity(), new TestEntity());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testFirstLevelEntityListRemoval() throws Exception {
        Iterator<TestEntity> iterator = testEntity.getEntityList().iterator();
        iterator.remove();
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testFirstLevelEntitySetRemoval() throws Exception {
        Iterator<TestEntity> iterator = testEntity.getEntitySet().iterator();
        iterator.remove();
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testFirstLevelEntityMapRemoval() throws Exception {
        Iterator<Map.Entry<TestEntity, TestEntity>> iterator = testEntity.getEntityMap().entrySet().iterator();
        iterator.remove();
    }

    // Second-level tests

    @Test
    public void testSecondLevelEntityGetBigInteger() throws Exception {
        assertEquals(testEntity.getInnerEntity().getId(), BigInteger.ONE);
    }

    @Test
    public void testSecondLevelEntityGetString() throws Exception {
        assertEquals(testEntity.getInnerEntity().getName(), TestEntity.class.getSimpleName());
    }

    @Test
    public void testSecondLevelEntityGetPrimitive() throws Exception {
        assertEquals(testEntity.getInnerEntity().getMask(), BigInteger.ONE.intValue());
    }

    @Test
    public void testSecondLevelEntityGetReference() throws Exception {
        assertNotNull(testEntity.getInnerEntity().getInnerEntity());
    }

    @Test
    public void testSecondLevelEntityGetFinalObjectAsObject() throws Exception {
        assertNotNull(testEntity.getInnerEntity().getFinalObjectAsObject());
    }

    @Test
    public void testSecondLevelEntityGetObjectWithoutParameterlessConstructorAsObject() throws Exception {
        assertNotNull(testEntity.getInnerEntity().getObjectWithoutParameterlessConstructorAsObject());
    }

    @Test
    public void testSecondLevelEntityGetList() throws Exception {
        assertNotNull(testEntity.getInnerEntity().getEntityList());
    }

    @Test
    public void testSecondLevelEntityGetSet() throws Exception {
        assertNotNull(testEntity.getInnerEntity().getEntitySet());
    }

    @Test
    public void testSecondLevelEntityGetMap() throws Exception {
        assertNotNull(testEntity.getInnerEntity().getEntityMap());
    }

    @Test
    public void testSecondLevelEntityIteration() throws Exception {
        Iterator<TestEntity> listIterator = testEntity.getInnerEntity().getEntityList().iterator();
        assertNotNull(listIterator.next());

        Iterator<TestEntity> setIterator = testEntity.getInnerEntity().getEntitySet().iterator();
        assertNotNull(setIterator.next());

        Iterator<Map.Entry<TestEntity, TestEntity>> mapIterator = testEntity.getInnerEntity().getEntityMap().entrySet().iterator();
        assertNotNull(mapIterator.next());
    }

    @Test
    public void testSecondLevelGetterWithInnerGetter() throws Exception {
        assertNotNull(testEntity.getInnerEntity().getThirdLevelEntity());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSecondLevelEntitySetBigInteger() throws Exception {
        testEntity.getInnerEntity().setId(BigInteger.ONE);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSecondLevelEntitySetString() throws Exception {
        testEntity.getInnerEntity().setName(TestEntity.class.getSimpleName());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSecondLevelEntitySetPrimitive() throws Exception {
        testEntity.getInnerEntity().setMask(BigInteger.ONE.intValue());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSecondLevelEntitySetReference() throws Exception {
        testEntity.getInnerEntity().setInnerEntity(testEntity);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSecondLevelEntitySetList() throws Exception {
        testEntity.getInnerEntity().setEntityList(new ArrayList<>());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSecondLevelEntitySetSet() throws Exception {
        testEntity.getInnerEntity().setEntitySet(new HashSet<>());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSecondLevelEntitySetMap() throws Exception {
        testEntity.getInnerEntity().setEntityMap(new HashMap<>());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSecondLevelEntityListFieldModification() throws Exception {
        testEntity.getInnerEntity().getEntityList().add(new TestEntity());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSecondLevelEntitySetFieldModification() throws Exception {
        testEntity.getInnerEntity().getEntitySet().add(new TestEntity());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSecondLevelEntityMapFieldModification() throws Exception {
        testEntity.getInnerEntity().getEntityMap().put(new TestEntity(), new TestEntity());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSecondLevelEntityListRemoval() throws Exception {
        Iterator<TestEntity> iterator = testEntity.getInnerEntity().getEntityList().iterator();
        iterator.remove();
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSecondLevelEntitySetRemoval() throws Exception {
        Iterator<TestEntity> iterator = testEntity.getInnerEntity().getEntitySet().iterator();
        iterator.remove();
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testSecondLevelEntityMapRemoval() throws Exception {
        Iterator<Map.Entry<TestEntity, TestEntity>> iterator = testEntity.getInnerEntity().getEntityMap().entrySet().iterator();
        iterator.remove();
    }

    // Other tests

    @Test
    public void testEquals() throws Exception {
        assertTrue(testEntity.equals(testEntity.getInnerEntity()));
    }

}