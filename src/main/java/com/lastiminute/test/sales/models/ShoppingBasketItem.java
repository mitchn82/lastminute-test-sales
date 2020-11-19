/*
 *  Copyright (C) Michele Nigri - 2020
 *  
 *  All rights reserved
 */
package com.lastiminute.test.sales.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author mitch
 */
@Entity
@Getter @Setter @NoArgsConstructor
public class ShoppingBasketItem implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "basket_id", nullable = false)
    private ShoppingBasket basket;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item Item;
    
    private Integer quantity;
}
