package com.assignment.voucher.service.controller;

import com.assignment.voucher.service.dto.VoucherDto;
import com.assignment.voucher.service.dto.VoucherRequest;
import com.assignment.voucher.service.dto.VoucherWaitResponse;
import com.assignment.voucher.service.entity.Voucher;
import com.assignment.voucher.service.mapper.VoucherMapper;
import com.assignment.voucher.service.repository.VoucherRepository;
import org.mapstruct.factory.Mappers;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.*;

@RestController
@RequestMapping(path = "/api/vouchers")
class VoucherController {

    private VoucherRepository voucherRepository;
    private VoucherMapper voucherMapper = Mappers.getMapper(VoucherMapper.class);
    private RestTemplate restTemplate;

    private Set<String> willSendSMSMessageConcurrentSet = ConcurrentHashMap.newKeySet();

    @Autowired
    private Environment env;

    @Autowired
    public VoucherController(RestTemplateBuilder restTemplateBuilder, VoucherRepository voucherRepository){
        this.restTemplate=restTemplateBuilder.build();
        this.voucherRepository= voucherRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VoucherDto>> getVouchers(@RequestParam(name="phoneNumber") String phoneNumber) {
        List<Voucher> result = voucherRepository.findByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(voucherMapper.entitiesToDtos(result));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<Object> buyVoucher(@RequestBody VoucherRequest voucherRequest){

        String promiseSendSmsMessageId = UUID.randomUUID().toString();
        CompletableFuture<ResponseEntity> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            willSendSMSMessageConcurrentSet.add(promiseSendSmsMessageId);
            return ResponseEntity.ok(new VoucherWaitResponse("The request is " +
                    "being processed within 30 seconds. You will receive a sms message when finish"));
        });

        CompletableFuture<ResponseEntity> future2 = CompletableFuture.supplyAsync(() -> {
            final String uri = env.getProperty("3rdApiUrl") + "?clientId={clientId}&delayInSeconds={delayInSeconds}";
            Map<String, String> params = new HashMap<>();
            params.put("clientId", env.getProperty("clientId"));
            params.put("delayInSeconds", voucherRequest.getDelayInSeconds() != null ?
                    voucherRequest.getDelayInSeconds().toString() : "0");
            VoucherDto voucherDto = restTemplate.getForObject(uri, VoucherDto.class, params);

            // insert to db
            Voucher voucher = new Voucher();
            voucher.setPhoneNumber(voucherRequest.getPhoneNumber());
            voucher.setVoucherCode(voucherDto.getVoucherCode());
            voucherRepository.save(voucher);


            if(willSendSMSMessageConcurrentSet.contains(promiseSendSmsMessageId)){
                // send sms message if have flag
                LoggerFactory.getLogger(VoucherController.class).info("[SMS Message] - Send sms message to phone number "+ voucherRequest.getPhoneNumber());

                // then remove from concurrentSet
                willSendSMSMessageConcurrentSet.remove(promiseSendSmsMessageId);
            }
            return ResponseEntity.ok(voucherDto);
        });

        return CompletableFuture.anyOf(future1, future2);
    }
}
