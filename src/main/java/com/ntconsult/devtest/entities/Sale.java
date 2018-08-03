/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntconsult.devtest.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Marcos Freitas Nunes <marcos@cognitivabrasil.com.br>
 */
public class Sale {

    private long id;
    private float amount;
    private String seller;
    private static final Logger LOG = LoggerFactory.getLogger(Sale.class);

    public Sale() {
        amount = 0F;
    }

    /**
     * The array must to have 4 attributes and be in this format: {ignorable, SaleId,[ItemId-ItemQuantity-ItemPrice,
     * ItemId...], SellerName}.
     *
     * Example: {"003","10","[1-10-100,2-30-2.50,3-40-3.10]","Diego"}
     *
     * @param array
     * @return
     */
    public static Sale fromArray(String[] array) {
        Sale sale = new Sale();
        sale.setId(Long.parseLong(array[1]));
        sale.setSeller(array[3]);
        //Removes the first and last character that should be "[" and "]"
        String itemsStr = array[2].substring(1, array[2].length() - 1);

        String[] items = itemsStr.split(",");
        for (String itemStr : items) {
            //split the items and then get a array with attibutes (Item ID-Item Quantity-Item Price).
            String[] itemAttr = itemStr.split("-");
            //get the item's price and add to amount.
            sale.addAmount(Float.parseFloat(itemAttr[2]));
        }
        return sale;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    /**
     * Adds the price to the sale
     *
     * @param price
     */
    public void addAmount(Float price) {
        this.amount += price;
    }

    /**
     * Return the sum of price all itens.
     *
     * @return
     */
    public float getAmount() {
        return this.amount;
    }

}
