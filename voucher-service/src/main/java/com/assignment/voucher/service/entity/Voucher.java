package com.assignment.voucher.service.entity;

import javax.persistence.*;

@Entity
@Table(name = "voucher")
public class Voucher {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "voucher_code", nullable = false)
    private String voucherCode;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }
}