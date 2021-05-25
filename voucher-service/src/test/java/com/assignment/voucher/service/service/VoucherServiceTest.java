package com.assignment.voucher.service.service;

import com.assignment.voucher.service.dto.VoucherDto;
import com.assignment.voucher.service.dto.VoucherRequest;
import com.assignment.voucher.service.dto.VoucherWaitResponse;
import com.assignment.voucher.service.model.Voucher;
import com.assignment.voucher.service.repository.VoucherRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@ExtendWith(MockitoExtension.class)
public class VoucherServiceTest {

    private final String MOCK_PHONE_NUMBER = "0382138482";
    private final String MOCK_VOUCHER_CODE = "123456789012345";
    private final String MOCK_VOUCHER_CODE_TWO = "223452789012340";

    @Mock private VoucherRepository voucherRepository;
    @Mock private RestTemplateBuilder restTemplateBuilder;
    @Mock private Environment env;
    @Mock private RestTemplate restTemplate;

    private VoucherService voucherService;

    @BeforeEach
    void setUp() {
        voucherService = new VoucherService(voucherRepository, restTemplateBuilder, env);
        ReflectionTestUtils.setField(voucherService, "restTemplate", restTemplate);
    }

    @Test
    public void getVouchers_whenEmptyData_thenReturnEmptyList() {
        Mockito.when(voucherRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(new ArrayList<>());
        List<VoucherDto> result = voucherService.getVouchers(MOCK_PHONE_NUMBER);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void getVouchers_whenHasData_thenReturnListOfData() {
        Mockito.when(voucherRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(mockListVoucherReturn());
        List<VoucherDto> result = voucherService.getVouchers(MOCK_PHONE_NUMBER);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(MOCK_VOUCHER_CODE, result.get(0).getVoucherCode());
        Assertions.assertEquals(MOCK_VOUCHER_CODE_TWO, result.get(1).getVoucherCode());
    }

    @Test
    public void buyVoucher_whenRunQuick_thenReturnVoucherCode() throws ExecutionException, InterruptedException {
        Mockito.when(env.getProperty("requestTimeoutInSeconds")).thenReturn("1");
        Mockito.when(env.getProperty("3rdApiUrl")).thenReturn("http://localhost:8080/api/3rd/voucher");
        Mockito.when(env.getProperty("clientId")).thenReturn("mockClientId");
        Mockito.lenient().when(restTemplateBuilder.build()).thenReturn(restTemplate);
        VoucherDto dto = new VoucherDto();
        dto.setVoucherCode(MOCK_VOUCHER_CODE);
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any(), Mockito.any(Map.class))).thenReturn(dto);

        VoucherRequest voucherRequest = new VoucherRequest();
        voucherRequest.setDelayInSeconds(2);
        voucherRequest.setPhoneNumber(MOCK_PHONE_NUMBER);
        Object result = voucherService.buyVoucher(voucherRequest);
        Assertions.assertTrue(result instanceof VoucherDto);
    }

    @Test
    public void buyVoucher_whenRunLong_thenReturnMessage() throws ExecutionException, InterruptedException {
        Mockito.when(env.getProperty("requestTimeoutInSeconds")).thenReturn("0");
        Mockito.lenient().when(restTemplateBuilder.build()).thenReturn(restTemplate);
        VoucherDto dto = new VoucherDto();
        dto.setVoucherCode(MOCK_VOUCHER_CODE);
        Mockito.lenient().when(restTemplate.getForObject(Mockito.anyString(), Mockito.any(), Mockito.any(Map.class))).thenReturn(dto);

        VoucherRequest voucherRequest = new VoucherRequest();
        voucherRequest.setDelayInSeconds(2);
        voucherRequest.setPhoneNumber(MOCK_PHONE_NUMBER);
        Object result = voucherService.buyVoucher(voucherRequest);
        Assertions.assertTrue(result instanceof VoucherWaitResponse);
    }

    private List<Voucher> mockListVoucherReturn() {
        List<Voucher> result = new ArrayList<>();
        Voucher voucher = new Voucher();
        voucher.setId(1);
        voucher.setPhoneNumber(MOCK_PHONE_NUMBER);
        voucher.setVoucherCode(MOCK_VOUCHER_CODE);
        result.add(voucher);

        Voucher voucher2 = new Voucher();
        voucher2.setId(2);
        voucher2.setPhoneNumber(MOCK_PHONE_NUMBER);
        voucher2.setVoucherCode(MOCK_VOUCHER_CODE_TWO);
        result.add(voucher2);

        return result;
    }
}
