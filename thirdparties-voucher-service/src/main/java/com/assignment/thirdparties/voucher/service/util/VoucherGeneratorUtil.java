/*
 * VoucherGeneratorUtil.java
 */
package com.assignment.thirdparties.voucher.service.util;

import java.util.Random;

public class VoucherGeneratorUtil {

    private static class HOLDER {
        static final VoucherGeneratorUtil INSTANCE = new VoucherGeneratorUtil();
    }

    private VoucherGeneratorUtil() {
        // NOP
    }

    public static VoucherGeneratorUtil getInstance() {
        return HOLDER.INSTANCE;
    }

    /**
     * Generate voucher code
     * @param length length of code
     * @return voucher code
     */
    public String generateNumbers(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        random.ints(length, 0, 9).forEach(i -> {
            sb.append(i);
        });
        return sb.toString();
    }
}
