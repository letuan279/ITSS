package org.example.entity;

import java.util.List;

public abstract class BaseEntity {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BaseEntity() {
    }

    public BaseEntity(int id) {
        this.id = id;
    }

//    abstract public List<BaseEntity> getAll();
//    abstract public BaseEntity getById(int id);
//    abstract public BaseEntity deleteById(int id);
//    abstract public BaseEntity create(BaseEntity item);
//    abstract public BaseEntity updateById(BaseEntity item);
}
