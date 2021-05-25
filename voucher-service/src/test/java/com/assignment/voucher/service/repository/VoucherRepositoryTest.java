/*
 * VoucherRepositoryTest.java
 */
package com.assignment.voucher.service.repository;

import com.assignment.voucher.service.model.Voucher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
public class VoucherRepositoryTest {

    private final String MOCK_VOUCHER_NUMBER = "123456789123456";
    private final String MOCK_VOUCHER_NUMBER_TWO = "423456789123451";
    private final String MOCK_VOUCHER_NUMBER_THREE = "223456789123453";

    private final String MOCK_PHONE_NUMBER = "0382138482";
    private final String MOCK_PHONE_NUMBER_TWO = "0382138483";

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @AfterEach
    public void tearDown() {
        testEntityManager.clear();
    }

    @Test
    public void findByPhoneNumber_whenNoData_thenReturnEmpty() {
        List<Voucher> vouchers = voucherRepository.findByPhoneNumber(MOCK_PHONE_NUMBER);
        Assertions.assertTrue(CollectionUtils.isEmpty(vouchers));
    }

    @Test
    public void findByPhoneNumber_whenHasData_thenReturnCorrectly() {
        Voucher voucher = new Voucher();
        //voucher.setId(1);
        voucher.setPhoneNumber(MOCK_PHONE_NUMBER);
        voucher.setVoucherCode(MOCK_VOUCHER_NUMBER);
        testEntityManager.persist(voucher);

        Voucher voucher2 = new Voucher();
        //voucher2.setId(2);
        voucher2.setPhoneNumber(MOCK_PHONE_NUMBER);
        voucher2.setVoucherCode(MOCK_VOUCHER_NUMBER_TWO);
        testEntityManager.persist(voucher2);

        Voucher voucher3 = new Voucher();
        //voucher3.setId(3);
        voucher3.setPhoneNumber(MOCK_PHONE_NUMBER_TWO);
        voucher3.setVoucherCode(MOCK_VOUCHER_NUMBER_THREE);
        testEntityManager.persist(voucher3);

        List<Voucher> vouchers = voucherRepository.findByPhoneNumber(MOCK_PHONE_NUMBER);
        Assertions.assertEquals(2, vouchers.size());
        assertThat(vouchers.get(0).getVoucherCode(), anyOf(is(MOCK_VOUCHER_NUMBER), is(MOCK_VOUCHER_NUMBER_TWO)));
        assertThat(vouchers.get(0).getId(), anyOf(is(1L), is(2L)));
        assertThat(vouchers.get(0).getPhoneNumber(), is(MOCK_PHONE_NUMBER));
        assertThat(vouchers.get(1).getVoucherCode(), anyOf(is(MOCK_VOUCHER_NUMBER), is(MOCK_VOUCHER_NUMBER_TWO)));
        assertThat(vouchers.get(1).getId(), anyOf(is(1L), is(2L)));
        assertThat(vouchers.get(1).getPhoneNumber(), is(MOCK_PHONE_NUMBER));

        vouchers = voucherRepository.findByPhoneNumber(MOCK_PHONE_NUMBER_TWO);
        Assertions.assertEquals(1, vouchers.size());
        assertThat(vouchers.get(0).getVoucherCode(), is(MOCK_VOUCHER_NUMBER_THREE));
        assertThat(vouchers.get(0).getId(), is(3L));
        assertThat(vouchers.get(0).getPhoneNumber(), is(MOCK_PHONE_NUMBER_TWO));
    }
}
