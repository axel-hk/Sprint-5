package ru.sber.streams




// 1. Используя withIndex() посчитать сумму элементов листа, индекс которых кратен 3. (нулевой индекс тоже входит)
fun getSumWithIndexDivisibleByThree(list: List<Long>): Long {

    return list.withIndex()
        .filter { it-> it.index%3==0 }
        .map { it->it.value }
        .reduce { acc, l-> acc+l   }
}

// 2. Используя функцию generateSequence() создать последовательность, возвращающую числа Фибоначчи.
fun generateFibonacciSequence(): Sequence<Int> {

    return  generateSequence(Pair(0, 1)) { Pair(it.second, it.first + it.second)}.map{it.first}

}

// 3. Получить города, в которых есть покупатели.
fun Shop.getCustomersCities(): Set<City> = customers.map { it -> it.city }.toSet()

// 4. Получить все когда-либо заказанные продукты.
fun Shop.allOrderedProducts(): Set<Product> = customers.asSequence()
                                            .map { it->it.orders }
                                            .flatten()
                                            .asSequence()
                                            .map{it->it.products}
                                            .flatten()
                                            .toSet()




// 5. Получить покупателя, который сделал больше всего заказов.
fun Shop.getCustomerWithMaximumNumberOfOrders(): Customer? = customers.asSequence().maxByOrNull { it -> it.orders.size  }

// 6. Получить самый дорогой продукт, когда-либо приобртенный покупателем.
fun Customer.getMostExpensiveProduct(): Product? = orders.asSequence()
                                                    .map { it->it.products }
                                                    .flatten()
                                                    .maxByOrNull { it.price }

// 7. Получить соответствие в мапе: город - количество заказанных и доставленных продуктов в данный город.
fun Shop.getNumberOfDeliveredProductByCity(): Map<City, Int> = customers
        .groupBy(keySelector = { it.city }, valueTransform = { it.orders
        .filter { o -> o.isDelivered }
        .map { o -> o.products }
        .flatten()
        .size})
        .mapValues { list -> list.value
        .reduce { acc, el -> acc + el } }

// 8. Получить соответствие в мапе: город - самый популярный продукт в городе.
fun Shop.getMostPopularProductInCity(): Map<City, Product> = customers
        .groupBy(keySelector = { it.city }, valueTransform = { it.orders
        .map { o -> o.products }
        .flatten() })
        .mapValues { l -> l.value
        .flatten()
        .groupingBy { it }
        .eachCount()
        .maxByOrNull { it.value }?.key!! }

// 9. Получить набор товаров, которые заказывали все покупатели.
fun Shop.getProductsOrderedByAll(): Set<Product> =  customers
        .groupBy(keySelector = { it.name }, valueTransform = { it.orders
        .map { o -> o.products }
        .flatten()
        .toSet() })
        .values.flatten()
        .reduce{ acc, item -> item.intersect(acc) }



