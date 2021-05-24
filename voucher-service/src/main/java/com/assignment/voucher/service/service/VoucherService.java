/*
 * VoucherService.java
 *
 * Copyright by Axon Ivy (Lucerne), all rights reserved.
 */
package com.assignment.voucher.service.service;

import com.assignment.voucher.service.dto.VoucherDto;
import com.assignment.voucher.service.dto.VoucherRequest;
import com.assignment.voucher.service.dto.VoucherWaitResponse;
import com.assignment.voucher.service.mapper.VoucherMapper;
import com.assignment.voucher.service.model.Voucher;
import com.assignment.voucher.service.repository.VoucherRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.mapstruct.factory.Mappers;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VoucherService {
    private VoucherRepository voucherRepository;
    private VoucherMapper voucherMapper = Mappers.getMapper(VoucherMapper.class);
    private RestTemplate restTemplate;

    private Set<String> willSendSMSMessageConcurrentSet = ConcurrentHashMap.newKeySet();

    @Autowired
    private Environment env;

    @Autowired
    public VoucherService(RestTemplateBuilder restTemplateBuilder, VoucherRepository voucherRepository){
        this.restTemplate=restTemplateBuilder.build();
        this.voucherRepository= voucherRepository;
    }

    /**
     * Get all voucher by phone number
     * @param phoneNumber phone number
     * @return list of voucher
     */
    public ResponseEntity<List<VoucherDto>> getVouchers(String phoneNumber) {
        List<Voucher> result = voucherRepository.findByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(voucherMapper.entitiesToDtos(result));
    }

    /**
     * Buy voucher
     * @param voucherRequest voucher request
     * @return Future object
     */
    public CompletableFuture<Object> buyVoucher(VoucherRequest voucherRequest){

        String promiseSendSmsMessageId = UUID.randomUUID().toString();
        CompletableFuture<ResponseEntity> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(Long.valueOf(env.getProperty("requestTimeoutInSeconds")));
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            willSendSMSMessageConcurrentSet.add(promiseSendSmsMessageId);
            VoucherDto dto = new VoucherDto();
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


            if(future1.isDone() && willSendSMSMessageConcurrentSet.contains(promiseSendSmsMessageId)){
                // send sms message if have flag
                LoggerFactory.getLogger(VoucherService.class).info("[SMS Message] - Send sms message to phone number "+ voucherRequest.getPhoneNumber());

                // then remove from concurrentSet
                willSendSMSMessageConcurrentSet.remove(promiseSendSmsMessageId);
            }
            return ResponseEntity.ok(voucherDto);
        });

        return CompletableFuture.anyOf(future1, future2);
    }
}
