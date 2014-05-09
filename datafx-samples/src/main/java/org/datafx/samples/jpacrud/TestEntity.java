package org.datafx.samples.jpacrud;

import org.datafx.crud.table.ViewColumn;
import org.datafx.util.EntityWithId;

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
