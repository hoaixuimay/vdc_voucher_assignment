package com.assignment.thirdparties.voucher.service.model;

public class Voucher {

    private String voucherCode;

    public Voucher(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }
}
