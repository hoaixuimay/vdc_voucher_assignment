/*
 * VoucherGeneratorUtilTest.java
 */
package com.assignment.thirdparties.voucher.service.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VoucherGeneratorUtilTest {
    private VoucherGeneratorUtil voucherGeneratorUtil = VoucherGeneratorUtil.getInstance();

    @Test
    public void testGenerateNumbers() {
        String result = voucherGeneratorUtil.generateNumbers(10);
        Assertions.assertTrue(result.matches("\\d{10}"));
    }
}
