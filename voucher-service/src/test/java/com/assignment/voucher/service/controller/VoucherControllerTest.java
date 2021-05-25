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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
    private final String MOCK_VOUCHER_CODE_TWO = "224456789012345";

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

    @Test
    @WithMockUser(roles = "user")
    public void getVouchers_whenEmpty_thenReturnEmpty() throws Exception {
        Mockito.when(voucherService.getVouchers(MOCK_PHONE_NUMBER)).thenReturn(new ArrayList<>());

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/vouchers")
                .header(HttpHeaders.AUTHORIZATION, "Bearer mockToken")
                .param("phoneNumber", MOCK_PHONE_NUMBER);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    @WithMockUser(roles = "user")
    public void getVouchers_whenHasData_thenReturnListData() throws Exception {
        List<VoucherDto> dtos = new ArrayList<>();
        VoucherDto dto1 = new VoucherDto();
        dto1.setVoucherCode(MOCK_VOUCHER_CODE);
        VoucherDto dto2 = new VoucherDto();
        dto2.setVoucherCode(MOCK_VOUCHER_CODE_TWO);
        dtos.add(dto1);
        dtos.add(dto2);
        Mockito.when(voucherService.getVouchers(MOCK_PHONE_NUMBER)).thenReturn(dtos);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/vouchers")
                .header(HttpHeaders.AUTHORIZATION, "Bearer mockToken")
                .param("phoneNumber", MOCK_PHONE_NUMBER);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(MOCK_VOUCHER_CODE)))
                .andExpect(content().string(containsString(MOCK_VOUCHER_CODE_TWO)));
    }
}
