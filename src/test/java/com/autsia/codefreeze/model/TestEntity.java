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

package com.autsia.codefreeze.model;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestEntity extends SuperTestEntity {

    private BigInteger id;
    private int mask;
    private String name;
    private TestEntity innerEntity;
    private Map<TestEntity, TestEntity> entityMap;
    private List<TestEntity> entityList;
    private Set<TestEntity> entitySet;

    public TestEntity() {
        this.setSuperId(BigInteger.ONE);
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestEntity getInnerEntity() {
        return innerEntity;
    }

    public void setInnerEntity(TestEntity innerEntity) {
        this.innerEntity = innerEntity;
    }

    public Map<TestEntity, TestEntity> getEntityMap() {
        return entityMap;
    }

    public void setEntityMap(Map<TestEntity, TestEntity> entityMap) {
        this.entityMap = entityMap;
    }

    public List<TestEntity> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<TestEntity> entityList) {
        this.entityList = entityList;
    }

    public Set<TestEntity> getEntitySet() {
        return entitySet;
    }

    public void setEntitySet(Set<TestEntity> entitySet) {
        this.entitySet = entitySet;
    }

    public TestEntity getThirdLevelEntity() {
        return innerEntity.getInnerEntity();
    }

    public Object getFinalObjectAsObject() {
        return "";
    }

    public Object getObjectWithoutParameterlessConstructorAsObject() {
        return BigInteger.ONE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestEntity that = (TestEntity) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public String toString() {
        return "TestEntity{" +
                "id=" + id +
                ", mask=" + mask +
                ", name='" + name + '\'' +
                ", innerEntity=" + innerEntity +
                ", entityMap=" + entityMap +
                ", entityList=" + entityList +
                ", entitySet=" + entitySet +
                '}';
    }


}
