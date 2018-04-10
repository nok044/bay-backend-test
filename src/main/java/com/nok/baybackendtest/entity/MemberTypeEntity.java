package com.nok.baybackendtest.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "bbt_member_type")
public class MemberTypeEntity {

    @Id
    @Column(name = "pk_id", nullable = false, precision = 0)
    private Long pkId;
    @Basic
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    @Basic
    @Column(name = "minimum_salary", nullable = false, precision = 0)
    private Long minimumSalary;

    public Long getPkId() {
        return pkId;
    }

    public void setPkId(Long pkId) {
        this.pkId = pkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMinimumSalary() {
        return minimumSalary;
    }

    public void setMinimumSalary(Long minimumSalary) {
        this.minimumSalary = minimumSalary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberTypeEntity that = (MemberTypeEntity) o;
        return Objects.equals(pkId, that.pkId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(minimumSalary, that.minimumSalary);
    }

    @Override
    public int hashCode() {

        return Objects.hash(pkId, name, minimumSalary);
    }
}
