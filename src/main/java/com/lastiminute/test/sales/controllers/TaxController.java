/*
 *  Copyright (C) Michele Nigri - 2020
 *  
 *  All rights reserved
 */
package com.lastiminute.test.sales.controllers;

import com.lastiminute.test.sales.models.Tax;
import com.lastiminute.test.sales.models.TaxType;
import com.lastiminute.test.sales.services.TaxService;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mitch
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/taxes", produces = {MediaType.APPLICATION_JSON_VALUE})
public class TaxController {

    private TaxService taxService;

    public TaxController(TaxService taxService) {
        this.taxService = taxService;
    }

    /**
     * Get all taxes
     * @return 
     */
    @GetMapping
    public Map<TaxType, Tax> getAllTaxes() {
        return taxService.getTaxRates();
    }
        
}
