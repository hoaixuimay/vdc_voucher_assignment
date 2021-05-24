package com.assignment.voucher.service.controller;

import com.assignment.voucher.service.dto.VoucherDto;
import com.assignment.voucher.service.dto.VoucherRequest;
import com.assignment.voucher.service.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.*;
import java.util.concurrent.*;

@RestController
@RequestMapping(path = "/api/vouchers")
class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @RolesAllowed("user")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VoucherDto>> getVouchers(@RequestParam(name="phoneNumber") String phoneNumber,
                                                        @RequestHeader String Authorization) {
        List<VoucherDto> result =  voucherService.getVouchers(phoneNumber);
        return ResponseEntity.ok(result);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<Object> buyVoucher(@RequestBody VoucherRequest voucherRequest){
        return voucherService.buyVoucher(voucherRequest);
    }
}
