package com.nok.baybackendtest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "bbt_user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bbt_user_pk_id")
    @GenericGenerator(
            name = "seq_bbt_user_pk_id",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "sequence_name", value = "seq_bbt_user_pk_id"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "20"),
                    @org.hibernate.annotations.Parameter(name = "optimizer", value = "hilo")})
    @Column(name = "pk_id", nullable = false, precision = 0)
    private Long pkId;
    @Basic
    @Column(name = "member_type_id", nullable = false, precision = 0)
    @NotNull
    @Min(value = 1)
    private Long memberTypeId;
    @Basic
    @Column(name = "username", nullable = false, length = 200)
    @NotNull
    @Size(min = 1, max = 200)
    private String username;
    @Basic
    @Column(name = "password", nullable = false, length = 80)
    @NotNull
    @Size(min = 1, max = 80)
    @JsonIgnore
    private String password;
    @Basic
    @Column(name = "address", nullable = false, length = 2000)
    @NotNull
    @Size(min = 1, max = 2000)
    private String address;
    @Basic
    @Column(name = "phone", nullable = false, length = 50)
    @NotNull
    @Size(min = 1, max = 50)
    private String phone;
    @Basic
    @Column(name = "ref_code", nullable = false, length = 12)
    @NotNull
    @Size(min = 1, max = 12)
    private String refCode;
    @Basic
    @Column(name = "salary", nullable = false, precision = 0)
    @NotNull
    @Min(value = 0)
    private Long salary;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_type_id", referencedColumnName = "pk_id", insertable = false, updatable = false)
    private MemberTypeEntity memberType;

    public Long getPkId() {
        return pkId;
    }

    public void setPkId(Long pkId) {
        this.pkId = pkId;
    }

    public Long getMemberTypeId() {
        return memberTypeId;
    }

    public void setMemberTypeId(Long memberTypeId) {
        this.memberTypeId = memberTypeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public MemberTypeEntity getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberTypeEntity memberType) {
        this.memberType = memberType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(pkId, that.pkId) &&
                Objects.equals(memberTypeId, that.memberTypeId) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(address, that.address) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(refCode, that.refCode) &&
                Objects.equals(salary, that.salary);
    }

    @Override
    public int hashCode() {

        return Objects.hash(pkId, memberTypeId, username, password, address, phone, refCode, salary);
    }
}
