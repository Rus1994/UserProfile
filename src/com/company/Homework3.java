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
enum Drinks
{
    water,
    tea,
    milk,
    juice
}

class IceCream
{
    static public HashMap<Containers , Float> contPrice;
    static public HashMap<Tastes , Float> tastePrice;
    static public HashMap<Toppings , Float> topPrice;
    protected Containers container;
    protected Tastes taste;
    protected Toppings topping;
    protected float basePrice;
    protected float totalPrice;

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

    public IceCream(){} // дефолтный конструктор
    public IceCream(Containers _container, Tastes _taste, Toppings _topping)
    {
        container = _container;
        taste = _taste;
        topping = _topping;
        basePrice = contPrice.get(container) + tastePrice.get(taste);
        totalPrice = basePrice + topPrice.get(topping);
    }
    public String showBasePrice(){ return "Базовая цена = " + basePrice + " руб\n"; }
    public String showToppingsPrice(){ return "Топпинг " + topping + " " + topPrice.get(topping) + " руб\n"; }
    public String showTotalPrice(){ return "Итоговая цена = " + totalPrice + " руб\n"; }
    public String showResult(){return showBasePrice() + showToppingsPrice() + showTotalPrice();}

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

class Surprise extends IceCream
{
    Toppings topping2;
    public Surprise()
    {
        super();
        container = Containers.values()[(int)(Math.random()*Containers.values().length)];
        taste = Tastes.values()[(int)(Math.random()*Tastes.values().length)];
        topping = Toppings.values()[(int)(Math.random()*Toppings.values().length)];
        topping2 = Toppings.values()[(int)(Math.random()*Toppings.values().length)];
        basePrice = contPrice.get(container) + tastePrice.get(taste);
        totalPrice = basePrice + topPrice.get(topping) + topPrice.get(topping2);
    }
    public String showToppingsPrice(){ return "Топпинг 1 " + topping + " " + topPrice.get(topping) + " руб\n" +
            "Топпинг 2 " + topping2 + " " + topPrice.get(topping2) + " руб\n"; }
    @Override
    public String toString() {
        return "Surprise{" +
                "container=" + container +
                ", taste=" + taste +
                ", topping=" + topping +
                ", topping2=" + topping2 +
                ", totalPrice=" + totalPrice +
                '}';
    }
}

class Extra extends IceCream
{
    static public HashMap<Drinks , Float> drinkPrice;
    Drinks drink;

    static{
        drinkPrice = new HashMap<Drinks , Float>();
        drinkPrice.put(Drinks.water, 5F);
        drinkPrice.put(Drinks.tea, 8F);
        drinkPrice.put(Drinks.milk, 12F);
        drinkPrice.put(Drinks.juice, 15F);
    }
    public Extra(Containers _container, Tastes _taste, Drinks _drink)
    {
        super(_container, _taste, Toppings.none);
        drink = _drink;
        basePrice += drinkPrice.get(drink);
        totalPrice = basePrice;
    }
    public String showDrinkPrice(){return "Напиток " + drink + " " + drinkPrice.get(drink) + " руб\n";}
    public String showResult(){return showDrinkPrice() + super.showResult();}
    @Override
    public String toString() {
        return "Extra{" +
                "container=" + container +
                ", taste=" + taste +
                ", topping=" + topping +
                ", drink=" + drink +
                ", totalPrice=" + totalPrice +
                '}';
    }
}

public class Homework3 {

    static void run()
    {
        System.out.println("Домашнее задание 3 - Классы мороженного без пользовательского меню");

        IceCream ice = new IceCream(Containers.cone, Tastes.chocolate, Toppings.nuts);
        System.out.println(ice.showResult());
        Surprise ice2 = new Surprise();
        System.out.println(ice2.showResult());
        Extra ice3 = new Extra(Containers.cone, Tastes.chocolate, Drinks.juice);
        System.out.println(ice3.showResult());
    }
}
