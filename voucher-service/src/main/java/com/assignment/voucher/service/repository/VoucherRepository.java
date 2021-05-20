package com.assignment.voucher.service.repository;

import com.assignment.voucher.service.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    @Query("SELECT t FROM voucher t WHERE t.phone_number = ?1")
    List<Voucher> findByPhoneNumber(String phoneNumber);
}
