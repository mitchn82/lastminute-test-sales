/*
 *  Copyright (C) Michele Nigri - 2020
 *  
 *  All rights reserved
 */
package com.lastiminute.test.sales.services;

import com.lastiminute.test.sales.models.Item;
import com.lastiminute.test.sales.models.ItemType;
import com.lastiminute.test.sales.models.Tax;
import com.lastiminute.test.sales.models.TaxType;
import com.lastiminute.test.sales.repos.TaxRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author mitch
 */
@ExtendWith(SpringExtension.class)
public class TaxServiceTest {

    @SpyBean
    private TaxService service;

    @MockBean
    private TaxRepository repo;

    private final Tax basicTax = new Tax(new BigDecimal("10"), TaxType.BASIC);
    private final Tax importedTax = new Tax(new BigDecimal("5"), TaxType.IMPORTED);
    private final List<Tax> taxes = Arrays.asList(basicTax, importedTax);

    @BeforeEach
    public void setupTaxes() {
        when(repo.findAll()).thenReturn(taxes);
    }

    @Test
    public void shouldReturnTaxRatesAsMap() {
        Map<TaxType, Tax> taxRates = service.getTaxRates();

        assertThat(taxRates.get(TaxType.BASIC)).isEqualTo(basicTax);
        assertThat(taxRates.get(TaxType.IMPORTED)).isEqualTo(importedTax);
    }

    @Test
    public void shouldRoundBigDecimal() {
        BigDecimal round = bd("0.05");

        assertThat(bd("1.05")).isEqualTo(service.round(bd("1.03"), round, RoundingMode.UP));
        assertThat(bd("1.10")).isEqualTo(service.round(bd("1.051"), round, RoundingMode.UP));
        assertThat(bd("1.05")).isEqualTo(service.round(bd("1.05"), round, RoundingMode.UP));
        assertThat(bd("1.95")).isEqualTo(service.round(bd("1.900001"), round, RoundingMode.UP));

        assertThat(bd("1.00")).isEqualTo(service.round(bd("1.03"), round, RoundingMode.DOWN));
        assertThat(bd("1.05")).isEqualTo(service.round(bd("1.051"), round, RoundingMode.DOWN));
        assertThat(bd("1.05")).isEqualTo(service.round(bd("1.05"), round, RoundingMode.DOWN));
        assertThat(bd("1.90")).isEqualTo(service.round(bd("1.900001"), round, RoundingMode.DOWN));
    }

    @Test
    public void givenItemShouldReturnTaxRate() {
        Item item = new Item();

        item.setType(ItemType.BOOK);
        item.setImported(false);
        assertThat(service.getRate(item)).isEqualTo(BigDecimal.ZERO);

        item.setType(ItemType.FOOD);
        item.setImported(true);
        assertThat(service.getRate(item)).isEqualTo(bd("5"));

        item.setType(ItemType.GENERIC);
        item.setImported(false);
        assertThat(service.getRate(item)).isEqualTo(bd("10"));

        item.setType(ItemType.GENERIC);
        item.setImported(true);
        assertThat(service.getRate(item)).isEqualTo(bd("15"));
    }

    @Test
    public void shouldReturnRoundedPrice() {
        Item item = new Item();

        item.setPrice(bd("12.49"));
        item.setType(ItemType.BOOK);
        item.setImported(false);
        assertThat(service.getRateAmount(item)).isEqualTo(BigDecimal.ZERO);

        item.setPrice(bd("12.49"));
        item.setType(ItemType.FOOD);
        item.setImported(true);
        assertThat(service.getRateAmount(item)).isEqualTo(bd("0.65"));

        item.setPrice(bd("12.49"));
        item.setType(ItemType.GENERIC);
        item.setImported(false);
        assertThat(service.getRateAmount(item)).isEqualTo(bd("1.25"));

        item.setPrice(bd("12.49"));
        item.setType(ItemType.GENERIC);
        item.setImported(true);
        assertThat(service.getRateAmount(item)).isEqualTo(bd("1.90"));
    }

    @Test
    public void shouldReturnRoundedPrice2() {
        Item item = new Item();

        item.setPrice(bd("12.49"));
        item.setType(ItemType.BOOK);
        item.setImported(false);
        BigDecimal rate1 = service.getRateAmount(item);

        item.setPrice(bd("14.99"));
        item.setType(ItemType.GENERIC);
        item.setImported(false);
        BigDecimal rate2 = service.getRateAmount(item);

        item.setPrice(bd("0.85"));
        item.setType(ItemType.FOOD);
        item.setImported(false);
        BigDecimal rate3 = service.getRateAmount(item);

        assertThat(rate1.add(rate2).add(rate3)).isEqualTo(bd("1.50"));
    }

    private BigDecimal bd(String b) {
        return new BigDecimal(b);
    }

}
