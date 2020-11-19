/*
 *  Copyright (C) Michele Nigri - 2020
 *  
 *  All rights reserved
 */
package com.lastiminute.test.sales.controllers;

import com.lastiminute.test.sales.exceptions.ResourceNotFoundException;
import com.lastiminute.test.sales.models.Item;
import com.lastiminute.test.sales.services.ItemService;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mitch
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/items", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * Get all items
     *
     * @return
     */
    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAll();
    }

    /**
     * Get an item by id
     *
     * @param id
     * @return
     * @throws ResourceNotFoundException
     */
    @GetMapping("/{id}")
    public Item getItem(@PathVariable Long id) throws ResourceNotFoundException {
        return itemService.getById(id);
    }

}
