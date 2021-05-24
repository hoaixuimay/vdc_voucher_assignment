package com.assignment.voucher.service.service;

import com.assignment.voucher.service.dto.VoucherDto;
import com.assignment.voucher.service.model.Voucher;
import com.assignment.voucher.service.repository.VoucherRepository;
import org.junit.Before;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class VoucherServiceTest {

    private final String MOCK_PHONE_NUMBER = "0382138482";
    private final String MOCK_VOUCHER_CODE = "123456789012345";
    private final String MOCK_VOUCHER_CODE_TWO = "223452789012340";

    @Mock private VoucherRepository voucherRepository;
    @Mock private RestTemplateBuilder restTemplateBuilder;
    @Mock private Environment env;

    VoucherService voucherService;

    @Before
    public void setup() {
        voucherService = new VoucherService(voucherRepository, restTemplateBuilder, env);
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
