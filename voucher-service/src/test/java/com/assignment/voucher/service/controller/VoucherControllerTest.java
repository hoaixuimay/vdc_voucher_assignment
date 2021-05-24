package com.assignment.voucher.service.controller;

import com.assignment.voucher.service.dto.VoucherDto;
import com.assignment.voucher.service.dto.VoucherRequest;
import com.assignment.voucher.service.service.VoucherService;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoucherController.class)
public class VoucherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoucherService service;

    private final String MOCK_PHONE_NUMBER = "0382138482";
    private final String MOCK_VOUCHER_CODE = "123456789012345";

    @Test
    public void greetingShouldReturnMessageFromService() throws Exception {
        String mockVoucherCode = "123456789123456";
        VoucherRequest request = new VoucherRequest();
        request.setPhoneNumber(MOCK_PHONE_NUMBER);
        request.setDelayInSeconds(0);
        CompletableFuture<Object> mockFuture = new CompletableFuture<Object>();
        VoucherDto dto = new VoucherDto();
        dto.setVoucherCode(MOCK_VOUCHER_CODE);
        mockFuture.complete(ResponseEntity.ok(dto));

        Mockito.when(service.buyVoucher(request)).thenReturn(mockFuture);

        this.mockMvc.perform(post("/api/vouchers").header(HttpHeaders.AUTHORIZATION, "Bearer mockToken"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(MOCK_VOUCHER_CODE)));
    }
}
