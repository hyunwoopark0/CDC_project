package org.example.cdcproject.repository;

import org.example.cdcproject.dao.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository  extends JpaRepository<Customer, Long> {
    @Modifying
    @Query("DELETE FROM Customer WHERE firstName = :condition1 AND lastName = :condition2")
    void deleteByConditions(@Param("condition1") String condition1, @Param("condition2") String condition2);
}
