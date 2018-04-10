package com.nok.baybackendtest.repository;

import com.nok.baybackendtest.entity.MemberTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTypeRepository extends JpaRepository<MemberTypeEntity,Long> {
}
