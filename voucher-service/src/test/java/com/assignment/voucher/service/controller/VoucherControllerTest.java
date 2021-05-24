package com.assignment.voucher.service.controller;

import com.assignment.voucher.service.dto.VoucherDto;
import com.assignment.voucher.service.dto.VoucherRequest;
import com.assignment.voucher.service.service.VoucherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManagerFactory;
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
    private VoucherService voucherService;

    @MockBean
    EntityManagerFactory entityManagerFactory;

    private final String MOCK_PHONE_NUMBER = "0382138482";
    private final String MOCK_VOUCHER_CODE = "123456789012345";

    @Test
    public void buyVoucher_whenReturnVoucher_thenVoucherOutput() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        VoucherRequest requestObject = new VoucherRequest();
        requestObject.setPhoneNumber(MOCK_PHONE_NUMBER);
        requestObject.setDelayInSeconds(0);

        VoucherDto dto = new VoucherDto();
        dto.setVoucherCode(MOCK_VOUCHER_CODE);
        Mockito.when(voucherService.buyVoucher(Mockito.any(VoucherRequest.class))).thenReturn(dto);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/vouchers")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer mockToken")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(requestObject));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(MOCK_VOUCHER_CODE)));


//        String mockVoucherCode = "123456789123456";
//        VoucherRequest request = new VoucherRequest();
//        request.setPhoneNumber(MOCK_PHONE_NUMBER);
//        request.setDelayInSeconds(0);
//        CompletableFuture<Object> mockFuture = new CompletableFuture<Object>();
//        VoucherDto dto = new VoucherDto();
//        dto.setVoucherCode(MOCK_VOUCHER_CODE);
//        mockFuture.complete(ResponseEntity.ok(dto));
//
//        Mockito.when(service.buyVoucher(request)).thenReturn(mockFuture);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        this.mockMvc.perform(post("/api/vouchers")
//                    .header(HttpHeaders.AUTHORIZATION, "Bearer mockToken")
//                    .content(objectMapper.writeValueAsString(request))
//                    .contentType(MediaType.APPLICATION_JSON_VALUE)
//                )
//                .andDo(print()).andExpect(status().isOk())
//                .andExpect(content().string(containsString(MOCK_VOUCHER_CODE)));
    }

    //@Test
    public void buyVoucher_whenReturnMessage_thenMessageOutput() throws Exception {
        String mockVoucherCode = "123456789123456";
        VoucherRequest request = new VoucherRequest();
        request.setPhoneNumber(MOCK_PHONE_NUMBER);
        request.setDelayInSeconds(0);
        CompletableFuture<Object> mockFuture = new CompletableFuture<Object>();
        VoucherDto dto = new VoucherDto();
        dto.setVoucherCode(MOCK_VOUCHER_CODE);
        mockFuture.complete(ResponseEntity.ok(dto));

        Mockito.when(voucherService.buyVoucher(request)).thenReturn(mockFuture);

        this.mockMvc.perform(post("/api/vouchers").header(HttpHeaders.AUTHORIZATION, "Bearer mockToken"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(MOCK_VOUCHER_CODE)));
    }
}
