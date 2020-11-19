/*
 *  Copyright (C) Michele Nigri - 2020
 *  
 *  All rights reserved
 */
package com.lastiminute.test.sales.repos;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author mitch
 */
@NoRepositoryBean
public interface BaseRepository<T> extends CrudRepository<T, Long> {
    
    @Override
    public List<T> findAll();
}
