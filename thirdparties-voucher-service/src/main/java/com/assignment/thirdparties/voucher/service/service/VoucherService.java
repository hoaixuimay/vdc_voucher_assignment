/*
 * VoucherService.java
 */
package com.assignment.thirdparties.voucher.service.service;

import com.assignment.thirdparties.voucher.service.model.Voucher;
import com.assignment.thirdparties.voucher.service.util.VoucherGeneratorUtil;
import org.springframework.stereotype.Service;

@Service
public class VoucherService {

    public Voucher buyVoucher(int delayInSeconds) throws InterruptedException {
        if(delayInSeconds > 0) {
            java.util.concurrent.TimeUnit.SECONDS.sleep(delayInSeconds);
        }
        String voucherCode = VoucherGeneratorUtil.getInstance().generateNumbers(15);
        return new Voucher(voucherCode);
    }
}
