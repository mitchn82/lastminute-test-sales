/*
 *  Copyright (C) Michele Nigri - 2020
 *  
 *  All rights reserved
 */
package com.lastiminute.test.sales.controllers;

import com.lastiminute.test.sales.exceptions.ResourceNotFoundException;
import com.lastiminute.test.sales.models.ShoppingBasket;
import com.lastiminute.test.sales.services.ShoppingBasketService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mitch
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/baskets", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ShoppingBasketController {

    private ShoppingBasketService basketService;

    public ShoppingBasketController(ShoppingBasketService basketService) {
        this.basketService = basketService;
    }

    /**
     * Get a basket by id
     * @param id
     * @return
     * @throws ResourceNotFoundException 
     */
    @GetMapping("/{id}")
    public ShoppingBasket getBasket(@PathVariable Long id) throws ResourceNotFoundException {
        return basketService.getById(id);
    }

    /**
     * Create an empty basket
     * @return 
     */
    @PostMapping
    public ShoppingBasket create() {
        return basketService.update(new ShoppingBasket());
    }

    /**
     * Add item to basket. If item already exsist into basket, then increments quantity
     * @param id
     * @param itemId
     * @return
     * @throws ResourceNotFoundException 
     */
    @PostMapping("/{id}/items/{itemId}")
    public ShoppingBasket addItem(@PathVariable Long id, @PathVariable Long itemId) throws ResourceNotFoundException {
        return basketService.addToBasket(id, itemId);
    }

    /**
     * Remove item from basket. If item doesn't exist, simply it does nothing
     * @param id
     * @param itemId
     * @return
     * @throws ResourceNotFoundException 
     */
    @DeleteMapping("/{id}/items/{itemId}")
    public ShoppingBasket removeItem(@PathVariable Long id, @PathVariable Long itemId) throws ResourceNotFoundException {
        return basketService.removeFromBasket(id, itemId);
    }
}
