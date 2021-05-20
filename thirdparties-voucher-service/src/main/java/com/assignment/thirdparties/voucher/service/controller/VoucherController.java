package com.assignment.thirdparties.voucher.service.controller;

import com.assignment.thirdparties.voucher.service.model.Voucher;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping(path = "/voucher")
class VoucherController {

    @GetMapping(produces = "application/json")
    public ResponseEntity<Voucher> getVoucher(@RequestParam(name="clientId") String clientId,
                                              @RequestParam(name = "delayInSeconds", required = false, defaultValue = "0") Integer delayInSeconds) throws Exception{
        if(delayInSeconds > 0) {
            java.util.concurrent.TimeUnit.SECONDS.sleep(delayInSeconds);
        }
        String voucherCode = generateNumbers(15, 0, 9);
        Voucher voucher = new Voucher(voucherCode);
        LoggerFactory.getLogger(VoucherController.class).error("sleep2");
        return ResponseEntity.ok(voucher);
    }

    /**
     * Generate voucher code
     * @param length length of code
     * @param min min
     * @param max max
     * @return voucher code
     */
    private String generateNumbers(int length, int min, int max) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        random.ints(length, min, max).forEach(i -> {
            sb.append(i);
        });
        return sb.toString();
    }
}
