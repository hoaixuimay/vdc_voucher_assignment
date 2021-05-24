package com.assignment.thirdparties.voucher.service.controller;

import com.assignment.thirdparties.voucher.service.model.Voucher;
import com.assignment.thirdparties.voucher.service.util.VoucherGeneratorUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/3rd/voucher")
class VoucherController {

    @GetMapping(produces = "application/json")
    public ResponseEntity<Voucher> getVoucher(@RequestParam(name="clientId") String clientId,
                                              @RequestParam(name = "delayInSeconds", required = false, defaultValue = "0") Integer delayInSeconds) throws Exception{
        if(delayInSeconds > 0) {
            java.util.concurrent.TimeUnit.SECONDS.sleep(delayInSeconds);
        }
        String voucherCode = VoucherGeneratorUtil.getInstance().generateNumbers(15);
        Voucher voucher = new Voucher(voucherCode);
        return ResponseEntity.ok(voucher);
    }
}
