package com.assignment.voucher.service.repository;

import com.assignment.voucher.service.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    @Query(value="SELECT u FROM Voucher u WHERE u.phoneNumber = ?1")
    List<Voucher> findByPhoneNumber(String phoneNumber);
}
