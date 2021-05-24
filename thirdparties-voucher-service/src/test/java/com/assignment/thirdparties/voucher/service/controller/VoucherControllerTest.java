/*
 * VoucherControllerTest.java
 */
package com.assignment.thirdparties.voucher.service.controller;

import com.assignment.thirdparties.voucher.service.model.Voucher;
import com.assignment.thirdparties.voucher.service.service.VoucherService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoucherController.class)
public class VoucherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoucherService service;

    @Test
    public void greetingShouldReturnMessageFromService() throws Exception {
        String mockVoucherCode = "123456789123456";
        Mockito.when(service.buyVoucher(Mockito.anyInt())).thenReturn(new Voucher(mockVoucherCode));
        this.mockMvc.perform(get("/api/3rd/voucher?clientId=testClient"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(mockVoucherCode)));
    }
}
