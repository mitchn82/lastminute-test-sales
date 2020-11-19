/*
 *  Copyright (C) Michele Nigri - 2020
 *  
 *  All rights reserved
 */
package com.lastiminute.test.sales.controllers;

import com.jayway.jsonpath.JsonPath;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author mitch
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingBasketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenId_shouldReturnBasket() throws Exception {
        this.mockMvc.perform(get("/baskets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items", hasSize(3)));
    }

    @Test
    public void shouldCreateBasket() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/baskets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        int id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        this.mockMvc.perform(get("/baskets/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    public void givenId_shouldAddToBasket() throws Exception {
        int itemId = 1;
        this.mockMvc.perform(post("/baskets/2/items/" + itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[?(@.item.id == " + itemId + ")].quantity").value(1));
    }

    @Test
    public void givenId_shouldModifyBasket() throws Exception {
        int itemId = 4;
        this.mockMvc.perform(post("/baskets/2/items/" + itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[?(@.item.id == " + itemId + ")].quantity").value(2));
    }

    @Test
    public void givenId_shouldRemoveFromBasket() throws Exception {
        int itemId = 6;
        this.mockMvc.perform(delete("/baskets/3/items/" + itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[?(@.item.id == " + itemId + ")]").doesNotExist());
    }
}
