package com.assignment.voucher.service.controller;

import com.assignment.voucher.service.dto.VoucherDto;
import com.assignment.voucher.service.dto.VoucherRequest;
import com.assignment.voucher.service.dto.VoucherWaitResponse;
import com.assignment.voucher.service.service.VoucherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoucherController.class)
@AutoConfigureMockMvc(addFilters = false)
public class VoucherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoucherService voucherService;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final String MOCK_PHONE_NUMBER = "0382138482";
    private final String MOCK_VOUCHER_CODE = "123456789012345";

    @Test
    public void buyVoucher_whenReturnVoucher_thenVoucherOutput() throws Exception {
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
    }

    @Test
    public void buyVoucher_whenReturnMessage_thenMessageOutput() throws Exception {
        VoucherRequest requestObject = new VoucherRequest();
        requestObject.setPhoneNumber(MOCK_PHONE_NUMBER);
        requestObject.setDelayInSeconds(0);
        final String testMessage = "test message";
        VoucherWaitResponse dto = new VoucherWaitResponse(testMessage);
        Mockito.when(voucherService.buyVoucher(Mockito.any(VoucherRequest.class))).thenReturn(dto);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/vouchers")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer mockToken")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(requestObject));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testMessage)));
    }
}
