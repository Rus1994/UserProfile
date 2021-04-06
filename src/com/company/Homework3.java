package com.company;

import java.util.HashMap;

enum Containers {
    GLASS, // стаканчик
    CONE   // рожок
}

enum Tastes {
    CHOCOLATE,
    CHERRY,
    BANANA
}

enum Toppings {
    NONE,
    CHOCOLATE,
    NUTS,
    FRUIT;
}

enum Drinks {
    WATER,
    TEA,
    MILK,
    JUICE
}

class IceCream {
    protected HashMap<Containers, Float> contPrice;
    protected HashMap<Tastes, Float> tastePrice;
    protected HashMap<Toppings, Float> topPrice;
    protected Containers container;
    protected Tastes taste;
    protected Toppings topping;
    protected float basePrice;
    protected float totalPrice;

    public IceCream() {
        contPrice = new HashMap<Containers, Float>();
        tastePrice = new HashMap<Tastes, Float>();
        topPrice = new HashMap<Toppings, Float>();

        contPrice.put(Containers.GLASS, 5F);
        contPrice.put(Containers.CONE, 10F);

        tastePrice.put(Tastes.CHERRY, 5F);
        tastePrice.put(Tastes.BANANA, 10F);
        tastePrice.put(Tastes.CHOCOLATE, 15F);

        topPrice.put(Toppings.CHOCOLATE, 11F);
        topPrice.put(Toppings.NUTS, 13F);
        topPrice.put(Toppings.FRUIT, 14F);
        topPrice.put(Toppings.NONE, (float) 0);
    } // дефолтный конструктор

    public IceCream(Containers container, Tastes taste, Toppings topping) {
        this.container = container;
        this.taste = taste;
        this.topping = topping;
        this.basePrice = contPrice.get(container) + tastePrice.get(taste);
        this.totalPrice = basePrice + topPrice.get(topping);
    }

    public String showBasePrice() {
        return "Базовая цена = " + basePrice + " руб\n";
    }

    public String showToppingsPrice() {
        return "Топпинг " + topping + " " + topPrice.get(topping) + " руб\n";
    }

    public String showTotalPrice() {
        return "Итоговая цена = " + totalPrice + " руб\n";
    }

    public String showResult() {
        return showBasePrice() + showToppingsPrice() + showTotalPrice();
    }

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

class Surprise extends IceCream {
    protected Toppings topping2;

    public Surprise() {
        super();
        container = Containers.values()[(int) (Math.random() * Containers.values().length)];
        taste = Tastes.values()[(int) (Math.random() * Tastes.values().length)];
        topping = Toppings.values()[(int) (Math.random() * Toppings.values().length)];
        topping2 = Toppings.values()[(int) (Math.random() * Toppings.values().length)];
        basePrice = contPrice.get(container) + tastePrice.get(taste);
        totalPrice = basePrice + topPrice.get(topping) + topPrice.get(topping2);
    }

    public String showToppingsPrice() {
        return "Топпинг 1 " + topping + " " + topPrice.get(topping) + " руб\n" +
                "Топпинг 2 " + topping2 + " " + topPrice.get(topping2) + " руб\n";
    }

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

class Extra extends IceCream {
    protected HashMap<Drinks, Float> drinkPrice;
    Drinks drink;

    public Extra(Containers container, Tastes taste, Drinks drink) {
        super(container, taste, Toppings.NONE);

        drinkPrice = new HashMap<Drinks, Float>();
        drinkPrice.put(Drinks.WATER, 5F);
        drinkPrice.put(Drinks.TEA, 8F);
        drinkPrice.put(Drinks.MILK, 12F);
        drinkPrice.put(Drinks.JUICE, 15F);

        this.drink = drink;
        this.basePrice += drinkPrice.get(drink);
        this.totalPrice = basePrice;
    }

    public String showDrinkPrice() {
        return "Напиток " + drink + " " + drinkPrice.get(drink) + " руб\n";
    }

    public String showResult() {
        return showDrinkPrice() + super.showResult();
    }

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

    static void run() {
        System.out.println("Домашнее задание 3 - Классы мороженного без пользовательского меню");

        IceCream ice = new IceCream(Containers.CONE, Tastes.CHOCOLATE, Toppings.NUTS);
        System.out.println(ice.showResult());
        Surprise ice2 = new Surprise();
        System.out.println(ice2.showResult());
        Extra ice3 = new Extra(Containers.CONE, Tastes.CHOCOLATE, Drinks.JUICE);
        System.out.println(ice3.showResult());
    }
}
