/*
 *  Copyright (C) Michele Nigri - 2020
 *  
 *  All rights reserved
 */
package com.lastiminute.test.sales.services;

import com.lastiminute.test.sales.exceptions.ResourceNotFoundException;
import com.lastiminute.test.sales.models.Item;
import com.lastiminute.test.sales.repos.ItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author mitch
 */
@Service
public class ItemService {

    private ItemRepository repo;

    public ItemService(ItemRepository repo) {
        this.repo = repo;
    }

    public Item getById(Long id) throws ResourceNotFoundException {
        return repo.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public List<Item> getAll() {
        return repo.findAll();
    }
}
