/*
 * VoucherService.java
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.mapstruct.factory.Mappers;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VoucherService {
    private VoucherMapper voucherMapper = Mappers.getMapper(VoucherMapper.class);
    private RestTemplate restTemplate;
    private VoucherRepository voucherRepository;
    private Environment env;

    @Autowired
    public VoucherService(VoucherRepository voucherRepository, RestTemplateBuilder restTemplateBuilder, Environment env){
        this.restTemplate=restTemplateBuilder.build();
        this.voucherRepository = voucherRepository;
        this.env = env;
    }

    /**
     * Get all voucher by phone number
     * @param phoneNumber phone number
     * @return list of voucher
     */
    public List<VoucherDto> getVouchers(String phoneNumber) {
        List<Voucher> result = voucherRepository.findByPhoneNumber(phoneNumber);
        return voucherMapper.entitiesToDtos(result);
    }

    /**
     * Buy voucher
     * @param voucherRequest voucher request
     * @return Future object
     */
    public Object buyVoucher(VoucherRequest voucherRequest) throws ExecutionException, InterruptedException {

        CompletableFuture<VoucherWaitResponse> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(Long.valueOf(env.getProperty("requestTimeoutInSeconds")));
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return new VoucherWaitResponse("The request is " +
                    "being processed within 30 seconds. You will receive a sms message when finish");
        });

        CompletableFuture<VoucherDto> future2 = CompletableFuture.supplyAsync(() -> {
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

            return voucherDto;
        });
        CompletableFuture<Object> anyOfFuture = CompletableFuture.anyOf(future1, future2);
        Object result = anyOfFuture.get();
        if(result instanceof VoucherWaitResponse) {
            future2.thenAcceptAsync(res -> {
                // TODO: integrate with SMS message provider. e.g. Twilio
                LoggerFactory.getLogger(VoucherService.class).info("[SMS Message] - Send sms message to phone number " + res.getVoucherCode());
            });
        }
        return result;
    }
}
