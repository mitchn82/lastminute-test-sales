/*
 *  Copyright (C) Michele Nigri - 2020
 *  
 *  All rights reserved
 */
package com.lastiminute.test.sales.repos;

import com.lastiminute.test.sales.models.Item;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mitch
 */
@Repository
public interface ItemRepository extends BaseRepository<Item> {    
    
}
