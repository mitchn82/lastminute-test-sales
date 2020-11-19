/*
 *  Copyright (C) Michele Nigri - 2020
 *  
 *  All rights reserved
 */
package com.lastiminute.test.sales.services;

import com.lastiminute.test.sales.exceptions.ResourceNotFoundException;
import com.lastiminute.test.sales.models.Item;
import com.lastiminute.test.sales.models.ShoppingBasket;
import com.lastiminute.test.sales.models.ShoppingBasketItem;
import com.lastiminute.test.sales.repos.ShoppingBasketItemRepository;
import com.lastiminute.test.sales.repos.ShoppingBasketRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mitch
 */
@Service
public class ShoppingBasketService {

    private ShoppingBasketRepository repo;

    private ShoppingBasketItemRepository basketItemRepo;

    private ItemService itemService;

    private TaxService taxService;

    @Autowired
    public ShoppingBasketService(ShoppingBasketRepository repo, ShoppingBasketItemRepository basketItemRepo, TaxService taxService) {
        this.repo = repo;
        this.basketItemRepo = basketItemRepo;
        this.taxService = taxService;
    }

    @Autowired
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }

    public ShoppingBasket getById(Long id) throws ResourceNotFoundException {
        return repo.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public List<ShoppingBasket> getAll() {
        return repo.findAll();
    }

    @Transactional
    public ShoppingBasket update(ShoppingBasket basket) {
        if (!basket.getItems().isEmpty()) {
            basket.setTaxes(this.getTotalTaxRate(basket));
            basket.setTotal(this.getTotalAmount(basket));
        }
        return repo.save(basket);
    }

    @Transactional
    public ShoppingBasket addToBasket(Long basketId, Long itemId) throws ResourceNotFoundException {
        ShoppingBasket basket = getById(basketId);

        ShoppingBasketItem basketItem = basket.getItems().stream()
                .filter(i -> i.getItem().getId().equals(itemId))
                .findFirst().orElse(new ShoppingBasketItem());

        if (basketItem.getItem() != null) {
            basketItem.setQuantity(basketItem.getQuantity() + 1);
        }
        else {
            Item item = itemService.getById(itemId);

            basketItem.setItem(item);
            basketItem.setBasket(basket);
            basketItem.setQuantity(1);
        }

        basket.getItems().add(basketItem);
        return update(basket);
    }

    @Transactional
    public ShoppingBasket removeFromBasket(Long basketId, Long itemId) throws ResourceNotFoundException {
        ShoppingBasket basket = getById(basketId);

        Optional<ShoppingBasketItem> optBasketItem = basket.getItems().stream()
                .filter(i -> i.getItem().getId().equals(itemId))
                .findFirst();

        if (optBasketItem.isPresent()) {
            ShoppingBasketItem basketItem = optBasketItem.get();
            int quantity = basketItem.getQuantity() - 1;
            if (quantity == 0) {
                basket.getItems().remove(basketItem);
            }
            else {
                basketItem.setQuantity(quantity);
            }
            return update(basket);
        }

        return basket;
    }

    public BigDecimal getTotalTaxRate(ShoppingBasket basket) {
        return basket.getItems().stream()
                .map(this::getTaxRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalAmount(ShoppingBasket basket) {
        return basket.getItems().stream()
                .map(item -> this.getPrice(item).add(this.getTaxRate(item)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getTaxRate(ShoppingBasketItem basketItem) {
        Integer quantity = basketItem.getQuantity();
        BigDecimal rate = taxService.getRateAmount(basketItem.getItem());
        return rate.multiply(new BigDecimal(quantity));
    }

    private BigDecimal getPrice(ShoppingBasketItem basketItem) {
        Integer quantity = basketItem.getQuantity();
        BigDecimal price = basketItem.getItem().getPrice();
        return price.multiply(new BigDecimal(quantity));
    }
}
