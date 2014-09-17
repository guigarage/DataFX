package io.datafx.samples.jpacrud;

import io.datafx.crud.table.ViewColumn;
import io.datafx.crud.util.EntityWithId;

import javax.persistence.*;
import java.util.Date;

@Entity
public class TestEntity implements EntityWithId<Long> {

    @Id
    @GeneratedValue
    @ViewColumn("ID")
    private Long id;

    @Column
    @ViewColumn("Name")
    private String name;

    @Column
    @ViewColumn("Description")
    private String description;

    @Version
    private Date version;


    public TestEntity(String name) {
        this.name = name;
    }

    public TestEntity() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getVersion() {
        return version;
    }

    public void setVersion(Date version) {
        this.version = version;
    }
}
