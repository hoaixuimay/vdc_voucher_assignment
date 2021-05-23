package com.assignment.voucher.service.dto;

public class VoucherWaitResponse {
    private String message;

    public VoucherWaitResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
