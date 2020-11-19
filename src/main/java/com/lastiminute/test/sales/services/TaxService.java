/*
 *  Copyright (C) Michele Nigri - 2020
 *  
 *  All rights reserved
 */
package com.lastiminute.test.sales.services;

import com.lastiminute.test.sales.exceptions.ResourceNotFoundException;
import com.lastiminute.test.sales.models.Item;
import com.lastiminute.test.sales.models.Tax;
import com.lastiminute.test.sales.models.TaxType;
import com.lastiminute.test.sales.repos.TaxRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author mitch
 */
@Service
@CacheConfig(cacheNames = "TaxService")
public class TaxService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaxService.class.getName());

    private TaxRepository repo;

    private final BigDecimal round = new BigDecimal("0.05");
    private final RoundingMode mode = RoundingMode.UP;

    public TaxService(TaxRepository repo) {
        this.repo = repo;
    }

    @Cacheable
    public final Map<TaxType, Tax> getTaxRates() {
        List<Tax> allTaxes = repo.findAll();
        return allTaxes.stream()
                .collect(Collectors.toMap(item -> item.getType(), item -> item));
    }

    public Tax getByType(TaxType type) throws ResourceNotFoundException {
        return repo.findByType(type).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Helper method to getRate tax rate
     *
     * @param item
     * @return rate
     */
    public BigDecimal getRate(Item item) {
        BigDecimal basicTaxRate = new BigDecimal("10");
        try {
            basicTaxRate = getTaxRates().get(TaxType.BASIC).getRate();
        }
        catch (NullPointerException ex) {
            LOGGER.warn("BASIC tax rate not found, unsing default");
        }
        BigDecimal importedTaxRate = new BigDecimal("5");
        try {
            importedTaxRate = getTaxRates().get(TaxType.IMPORTED).getRate();
        }
        catch (NullPointerException ex) {
            LOGGER.warn("IMPORTED tax rate not found, unsing default");
        }

        BigDecimal taxRate = BigDecimal.ZERO;

        switch (item.getType()) {
            case BOOK:
            case FOOD:
            case MEDICAL:
                //no tax rate will be applied
                break;
            case GENERIC:
            default:
                taxRate = taxRate.add(basicTaxRate);
                break;
        }

        //tax rate for imported good with no exception
        if (item.isImported()) {
            taxRate = taxRate.add(importedTaxRate);
        }

        return taxRate;
    }

    /**
     * Get te amount of tax rate given an item
     *
     * @param item
     * @return amount
     */
    public BigDecimal getRateAmount(Item item) {
        BigDecimal rate = this.getRate(item);
        if (rate.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }

        BigDecimal taxRate = new BigDecimal(String.valueOf(rate));

        BigDecimal rateAmount = item.getPrice()
                .multiply(taxRate)
                .divide(new BigDecimal("100"));

        return round(rateAmount, round, mode);
    }

    /**
     * Round the value given a round value and a mode (up or down)
     *
     * @param value
     * @param round
     * @param roundingMode
     * @return rounded value
     */
    public BigDecimal round(BigDecimal value, BigDecimal round, RoundingMode roundingMode) {
        if (round.signum() == 0) {
            return value;
        }
        else {
            return value.divide(round, 0, roundingMode).multiply(round);
        }
    }
}
