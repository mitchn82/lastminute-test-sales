/*
 *  Copyright (C) Michele Nigri - 2020
 *  
 *  All rights reserved
 */
package com.lastiminute.test.sales.repos;

import com.lastiminute.test.sales.models.Tax;
import com.lastiminute.test.sales.models.TaxType;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mitch
 */
@Repository
public interface TaxRepository extends BaseRepository<Tax> {    
    
    public Optional<Tax> findByType(TaxType type);
}
