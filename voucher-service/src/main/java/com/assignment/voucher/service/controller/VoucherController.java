package com.assignment.voucher.service.controller;

import com.assignment.voucher.service.dto.VoucherDto;
import com.assignment.voucher.service.entity.Voucher;
import com.assignment.voucher.service.mapper.VoucherMapper;
import com.assignment.voucher.service.repository.VoucherRepository;
import org.mapstruct.factory.Mappers;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping(path = "/api/vouchers")
class VoucherController {

    @Autowired
    private VoucherRepository voucherRepository;
    private VoucherMapper voucherMapper = Mappers.getMapper(VoucherMapper.class);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VoucherDto>> getVouchers(@RequestParam(name="phoneNumber") String phoneNumber) {
        List<Voucher> result = voucherRepository.findByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(voucherMapper.entitiesToDtos(result));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity buyVoucher(@RequestParam(name="phoneNumber") String phoneNumber){
        Voucher result = new Voucher();
        return ResponseEntity.ok(voucherMapper.entityToDto(result));
    }
}
