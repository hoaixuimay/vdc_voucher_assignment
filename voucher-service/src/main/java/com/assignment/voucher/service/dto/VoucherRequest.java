package com.assignment.voucher.service.dto;

public class VoucherRequest {
    private String phoneNumber;
    private Integer delayInSeconds;

    public Integer getDelayInSeconds() {
        return delayInSeconds;
    }

    public void setDelayInSeconds(Integer delayInSeconds) {
        this.delayInSeconds = delayInSeconds;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
