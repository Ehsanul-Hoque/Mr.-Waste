package io.pantheonsite.alphaoptimus369.mrwaste.home_module.models;


public class ProductItem
{

    public long id;
    public String name;
    public double singleItemPriceInDollar;
    public int itemCount;


    public ProductItem(long id, String name, double singleItemPriceInDollar, int itemCount)
    {
        this.id = id;
        this.name = name;
        this.singleItemPriceInDollar = singleItemPriceInDollar;
        this.itemCount = itemCount;
    }

}
