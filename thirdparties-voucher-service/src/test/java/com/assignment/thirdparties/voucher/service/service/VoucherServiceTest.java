/*
 * VoucherService.java
 */
package com.assignment.thirdparties.voucher.service.service;

import com.assignment.thirdparties.voucher.service.model.Voucher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StopWatch;

@ExtendWith(MockitoExtension.class)
public class VoucherServiceTest {

    @InjectMocks
    private VoucherService service;

    @Test
    public void buyVoucher_whenNoDelay_shouldReturnImmediately() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Voucher result = service.buyVoucher(0);
        stopWatch.stop();
        Assertions.assertTrue(stopWatch.getTotalTimeSeconds() < 2);
        Assertions.assertTrue(!result.getVoucherCode().isEmpty());
    }

    @Test
    public void buyVoucher_whenHasDelay_shouldReturnLongerThanDelayTime() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Voucher result = service.buyVoucher(2);
        stopWatch.stop();
        Assertions.assertTrue(stopWatch.getTotalTimeSeconds() >= 2);
        Assertions.assertTrue(!result.getVoucherCode().isEmpty());
    }
}
