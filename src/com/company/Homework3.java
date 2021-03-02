package com.company;

import java.util.HashMap;

enum Containers
{
    glass, // стаканчик
    cone   // рожок
}
enum Tastes
{
    chocolate,
    cherry,
    banana
}
enum Toppings
{
    none,
    chocolate,
    nuts,
    fruit;
}

class IceCream
{
    static public HashMap<Containers , Float> contPrice;
    static public HashMap<Tastes , Float> tastePrice;
    static public HashMap<Toppings , Float> topPrice;
    protected Containers container;
    protected Tastes taste;
    protected Toppings topping;
    protected float totalPrice = 0;

    static {
        contPrice = new HashMap<Containers , Float>();
        tastePrice = new HashMap<Tastes , Float>();
        topPrice = new HashMap<Toppings , Float>();

        contPrice.put(Containers.glass, 5F);
        contPrice.put(Containers.cone, 10F);

        tastePrice.put(Tastes.cherry, 5F);
        tastePrice.put(Tastes.banana, 10F);
        tastePrice.put(Tastes.chocolate, 15F);
        
        topPrice.put(Toppings.chocolate, 11F);
        topPrice.put(Toppings.nuts, 13F);
        topPrice.put(Toppings.fruit, 14F);
        topPrice.put(Toppings.none, (float) 0);
    } // инициализируем цены

    public IceCream(Containers _container, Tastes _taste, Toppings _topping)
    {
        container = _container;
        taste = _taste;
        topping = _topping;
        totalPrice = contPrice.get(container) + tastePrice.get(taste) + topPrice.get(topping);
    }

    public float getTotalPrice(){ return totalPrice; }

    @Override
    public String toString() {
        return "IceCream{" +
                "container=" + container +
                ", taste=" + taste +
                ", topping=" + topping +
                ", totalPrice=" + totalPrice +
                '}';
    }
}

public class Homework3 {
    static void run()
    {
        IceCream ice = new IceCream(Containers.cone, Tastes.chocolate, Toppings.nuts);
        System.out.println(ice.toString());
    }
}
