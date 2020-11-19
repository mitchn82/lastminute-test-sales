/*
 *  Copyright (C) Michele Nigri - 2020
 *  
 *  All rights reserved
 */
package com.lastiminute.test.sales.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author mitch
 */
@Entity
@Getter @Setter @NoArgsConstructor
public class ShoppingBasket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "basket",
               fetch = FetchType.EAGER,
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Set<ShoppingBasketItem> items = new HashSet<>();
    
    private BigDecimal taxes = BigDecimal.ZERO;
    
    private BigDecimal total = BigDecimal.ZERO;
}
