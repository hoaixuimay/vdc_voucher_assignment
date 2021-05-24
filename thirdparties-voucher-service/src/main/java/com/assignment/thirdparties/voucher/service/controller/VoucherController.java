package com.assignment.thirdparties.voucher.service.controller;

import com.assignment.thirdparties.voucher.service.model.Voucher;
import com.assignment.thirdparties.voucher.service.service.VoucherService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/3rd/voucher")
class VoucherController {

    private final VoucherService service;

    public VoucherController(VoucherService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Voucher> getVoucher(@RequestParam(name="clientId") String clientId,
                                              @RequestParam(name = "delayInSeconds", required = false, defaultValue = "0") Integer delayInSeconds) throws InterruptedException {
        Voucher voucher = service.buyVoucher(delayInSeconds);
        return ResponseEntity.ok(voucher);
    }
}
