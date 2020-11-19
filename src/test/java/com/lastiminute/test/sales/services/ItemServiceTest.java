/*
 *  Copyright (C) Michele Nigri - 2020
 *  
 *  All rights reserved
 */
package com.lastiminute.test.sales.services;

import com.lastiminute.test.sales.exceptions.ResourceNotFoundException;
import com.lastiminute.test.sales.models.Item;
import com.lastiminute.test.sales.models.ItemType;
import com.lastiminute.test.sales.repos.ItemRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author mitch
 */
@ExtendWith(SpringExtension.class)
public class ItemServiceTest {

    @SpyBean
    private ItemService itemService;

    @MockBean
    private ItemRepository itemRepo;

    @Test
    public void givenIdShouldReturnItem() {
        long itemId = 1L;
        Item item = new Item();
        item.setId(itemId);
        item.setName("test item");
        item.setPrice(new BigDecimal("1.50"));
        item.setImported(false);
        item.setType(ItemType.BOOK);

        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));
        
        try {
            Item foundItem = itemService.getById(itemId);
            assertThat(foundItem).isEqualTo(item);
        }
        catch (ResourceNotFoundException ex) {
            fail(ex);
        }
    }

    @Test
    public void givenIdShouldNotReturnItem() {
        long itemId = 1L;
        Item item = new Item();
        item.setId(itemId);
        item.setName("test item");
        item.setPrice(new BigDecimal("1.50"));
        item.setImported(false);
        item.setType(ItemType.BOOK);
        
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(ResourceNotFoundException.class, () -> {
            itemService.getById(999L);
        });
    }

    @Test
    public void shouldReturnAllItems() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("test item 1");
        item1.setPrice(new BigDecimal("1.50"));
        item1.setImported(false);
        item1.setType(ItemType.BOOK);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("test item 2");
        item2.setPrice(new BigDecimal("3.50"));
        item2.setImported(true);
        item2.setType(ItemType.FOOD);

        List<Item> items = Arrays.asList(item1, item2);
        when(itemRepo.findAll()).thenReturn(items);

        assertThat(itemService.getAll()).isEqualTo(items);

    }
}
