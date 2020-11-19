/*
 *  Copyright (C) Michele Nigri - 2020
 *  
 *  All rights reserved
 */
package com.lastiminute.test.sales.services;

import com.lastiminute.test.sales.SalesApplication;
import com.lastiminute.test.sales.exceptions.ResourceNotFoundException;
import com.lastiminute.test.sales.models.Item;
import com.lastiminute.test.sales.models.ShoppingBasket;
import com.lastiminute.test.sales.models.ShoppingBasketItem;
import java.math.BigDecimal;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author mitch
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SalesApplication.class)
@AutoConfigureTestDatabase
public class ShoppingBasketServiceTest {

    @Autowired
    private ShoppingBasketService basketService;

    @Autowired
    private ItemService itemService;

    @Test
    public void givenId_shouldReturnBasket() throws ResourceNotFoundException {
        long basketId = 1L;

        ShoppingBasket basket = basketService.getById(basketId);
        assertThat(basket.getId()).isEqualTo(basketId);

        Set<ShoppingBasketItem> items = basket.getItems();
        assertThat(items.size()).isEqualTo(3);

        items.stream()
                .map(ShoppingBasketItem::getItem)
                .map(item -> assertThat(item.getName()).isIn("book", "music CD", "chocolate bar"));
    }

    @Test
    public void givenBasket1_shouldReturnTotalTaxRate() throws ResourceNotFoundException {
        ShoppingBasket basket = basketService.getById(1L);
        BigDecimal totalTaxRate = basketService.getTotalTaxRate(basket);
        assertThat(totalTaxRate).isEqualTo(basket.getTaxes()); //new BigDecimal("1.50"));
    }

    @Test
    public void givenBasket1_shouldReturnTotalAmount() throws ResourceNotFoundException {
        ShoppingBasket basket = basketService.getById(1L);
        BigDecimal totalTaxRate = basketService.getTotalAmount(basket);
        assertThat(totalTaxRate).isEqualTo(basket.getTotal()); //new BigDecimal("29.83"));
    }

    @Test
    public void givenBasket2_shouldReturnTotalTaxRate() throws ResourceNotFoundException {
        ShoppingBasket basket = basketService.getById(2L);
        BigDecimal totalTaxRate = basketService.getTotalTaxRate(basket);
        assertThat(totalTaxRate).isEqualTo(basket.getTaxes()); //new BigDecimal("7.65"));
    }

    @Test
    public void givenBasket2_shouldReturnTotalAmount() throws ResourceNotFoundException {
        ShoppingBasket basket = basketService.getById(2L);
        BigDecimal totalTaxRate = basketService.getTotalAmount(basket);
        assertThat(totalTaxRate).isEqualTo(basket.getTotal()); //new BigDecimal("65.15"));
    }

    @Test
    public void givenBasket3_shouldReturnTotalTaxRate() throws ResourceNotFoundException {
        ShoppingBasket basket = basketService.getById(3L);
        BigDecimal totalTaxRate = basketService.getTotalTaxRate(basket);
        assertThat(totalTaxRate).isEqualTo(basket.getTaxes()); //new BigDecimal("6.70"));
    }

    @Test
    public void givenBasket3_shouldReturnTotalAmount() throws ResourceNotFoundException {
        ShoppingBasket basket = basketService.getById(3L);
        BigDecimal totalTaxRate = basketService.getTotalAmount(basket);
        assertThat(totalTaxRate).isEqualTo(basket.getTotal()); //new BigDecimal("74.68"));
    }

    @Test
    public void shouldCreateBasket() throws ResourceNotFoundException {
        ShoppingBasket basket = basketService.update(new ShoppingBasket());

        assertThat(basket).isNotNull();
        assertThat(basket.getItems()).isEmpty();
    }

    @Test
    public void shouldAddToBasket() throws ResourceNotFoundException {
        ShoppingBasket basket = basketService.update(new ShoppingBasket());

        assertThat(basket).isNotNull();
        assertThat(basket.getItems()).isEmpty();

        Item item = itemService.getById(1L);
        basket = basketService.addToBasket(basket.getId(), item.getId());

        assertThat(basket).isNotNull();
        assertThat(basket.getItems().size()).isEqualTo(1);

        basket.getItems().forEach(i -> {
            assertThat(i.getItem()).isEqualTo(item);
            assertThat(i.getQuantity()).isEqualTo(1);
        });
    }

    @Test
    public void shouldUpdateItemOnBasket() throws ResourceNotFoundException {
        ShoppingBasket basket = basketService.update(new ShoppingBasket());
        Item item1 = itemService.getById(1L);
        basket = basketService.addToBasket(basket.getId(), item1.getId());

        assertThat(basket).isNotNull();
        assertThat(basket.getItems().size()).isEqualTo(1);

        basket = basketService.addToBasket(basket.getId(), item1.getId());

        assertThat(basket.getItems().size()).isEqualTo(1);
        basket.getItems().forEach(i -> {
            assertThat(i.getItem()).isEqualTo(item1);
            assertThat(i.getQuantity()).isEqualTo(2);
        });
    }

    @Test
    public void shouldRemoveFromBasket() throws ResourceNotFoundException {
        ShoppingBasket basket = basketService.update(new ShoppingBasket());
        Item item1 = itemService.getById(1L);
        basket = basketService.addToBasket(basket.getId(), item1.getId());
        
        assertThat(basket).isNotNull();
        assertThat(basket.getItems().size()).isEqualTo(1);

        basket = basketService.removeFromBasket(basket.getId(), item1.getId());

        assertThat(basket).isNotNull();
        assertThat(basket.getItems()).isEmpty();
    }

    @Test
    public void shouldUpdateItemOnBasket_2() throws ResourceNotFoundException {
        ShoppingBasket basket = basketService.update(new ShoppingBasket());
        Item item1 = itemService.getById(1L);
        basket = basketService.addToBasket(basket.getId(), item1.getId());
        basket = basketService.addToBasket(basket.getId(), item1.getId());

        assertThat(basket).isNotNull();
        assertThat(basket.getItems().size()).isEqualTo(1);

        basket = basketService.removeFromBasket(basket.getId(), item1.getId());

        basket.getItems().forEach(i -> {
            assertThat(i.getItem()).isEqualTo(item1);
            assertThat(i.getQuantity()).isEqualTo(1);
        });
    }
}
