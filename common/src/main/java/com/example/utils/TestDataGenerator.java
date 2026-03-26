package com.example.utils;

import com.example.models.api.Pet;
import com.example.models.unit.*;

import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;

public class TestDataGenerator {

    //массив имён
    private static final String[] FIRST_NAMES = {"Иван", "Пётр", "Алексей", "Дмитрий", "Сергей", "Анна", "Мария", "Елена"};
    //массив фамилий
    private static final String[] LAST_NAMES = {"Иванов", "Петров", "Сидоров", "Смирнов", "Кузнецов"};
    //названия товаров
    private static final String[] PRODUCT_NAMES = {"Ноутбук", "Смартфон", "Планшет", "Монитор", "Клавиатура", "Мышь", "Наушники"};
    //категории
    private static final String[] CATEGORIES = {"Электроника", "Компьютеры", "Аксессуары", "Бытовая техника"};
    //генератор случайных чисел
    private static final Random RANDOM = new Random();

    //
    public static int randomInt(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    public static String randomEmail() {
        return "user" + randomInt(100, 999) + "@test.com";
    }

    public static String randomPhone() {
        return "+7" + (randomInt(1000000000, 999999999));
    }

    // Генератор пользователя
    public static User generateUser() {
        User user = new User();
        user.setId(0); // новый пользователь

        String firstName = FIRST_NAMES[RANDOM.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)];

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(randomEmail());
        user.setPassword("password123");
        user.setPhone(randomPhone());
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return user;
    }

    // Генератор списка пользователя
    public static List<User> generateUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(generateUser());
        }
        return users;
    }

    // Генератор продукта
    public static Product generateProduct() {
        Product product = new Product();
        product.setId(0);

        String name = PRODUCT_NAMES[RANDOM.nextInt(PRODUCT_NAMES.length)];
        product.setName(name + " " + randomInt(100, 999));
        product.setDescription("Описание товара " + name);

        BigDecimal price = BigDecimal.valueOf(randomInt(500, 5000));
        product.setPrice(price);

        String category = CATEGORIES[RANDOM.nextInt(CATEGORIES.length)];
        product.setCategory(category);

        product.setStockQuantity(randomInt(0, 50));
        product.setAvailable(product.getStockQuantity() > 0);
        product.setImageUrl("https://example.com/img/" + randomInt(1, 100) + ".jpg");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        return product;
    }

    // Позиция заказа
    public static OrderItem generateOrderItem(int productId) {
        int quantity = randomInt(1, 5);
        BigDecimal price = BigDecimal.valueOf(randomInt(100, 1000));
        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(quantity));

        return new OrderItem(
                productId,
                "Товар " + productId,
                quantity,
                price
        );
        // totalPrice посчитается в конструкторе или через calculateTotal()
    }

    // Генерация заказа
    public static Order generateOrder(int userId) {
        List<OrderItem> items = new ArrayList<>();
        // Добавим 1-3 случайных товара
        int itemCount = randomInt(1, 3);
        for (int i = 0; i < itemCount; i++) {
            items.add(generateOrderItem(randomInt(1, 10)));
        }

        // Посчитаем общую сумму
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            total = total.add(item.getTotalPrice());
        }

        return new Order(
                userId,
                items,
                total,
                OrderStatus.NEW,
                randomInt(0, 1) == 0 ? "CARD" : "CASH",
                "ул. Тестовая, д. " + randomInt(1, 100)
        );
    }

   // Генерация Пет
    public static Map<String, Object> generatePet(String name, String status) {
        Map<String, Object> pet = new HashMap<>();
        pet.put("id", 0); // 0 = сервер назначит сам

        Map<String, Object> category = new HashMap<>();
        category.put("id", 1);
        category.put("name", "dogs");
        pet.put("category", category);

        pet.put("name", name);
        pet.put("photoUrls", Arrays.asList("https://example.com/" + name + ".jpg"));

        Map<String, Object> tag = new HashMap<>();
        tag.put("id", 1);
        tag.put("name", "friendly");
        pet.put("tags", Arrays.asList(tag));

        pet.put("status", status);
        return pet;
    }

    public static Map<String, Object> generateRandomPet() {
        String[] names = {"Бобик", "Шарик", "Мурзик", "Барсик", "Рекс"};
        String[] statuses = {"available", "pending", "sold"};
        String name = names[RANDOM.nextInt(names.length)] + RANDOM.nextInt(1000);
        String status = statuses[RANDOM.nextInt(statuses.length)];
        return generatePet(name, status);
    }

    // Генерация заказа
    public static Map<String, Object> generateOrder(long petId) {
        Map<String, Object> order = new HashMap<>();
        order.put("id", 0);
        order.put("petId", petId);
        order.put("quantity", RANDOM.nextInt(5) + 1);
        order.put("shipDate", LocalDateTime.now().toString());
        order.put("status", "placed");
        order.put("complete", RANDOM.nextBoolean());
        return order;
    }

    // Генерация пользователя
    public static Map<String, Object> generateUser(String username) {
        Map<String, Object> user = new HashMap<>();
        user.put("id", 0);
        user.put("username", username);
        user.put("firstName", "John");
        user.put("lastName", "Doe");
        user.put("email", username + "@example.com");
        user.put("password", "secret123");
        user.put("phone", "1234567890");
        user.put("userStatus", 0);
        return user;
    }

    public static Map<String, Object> generateRandomUser() {
        String username = "user_" + System.currentTimeMillis();
        return generateUser(username);
    }

    public static Pet generateRandomPetAsObject() {
        Pet pet = new Pet();

        pet.setId(0); // 0 = сервер сам назначит

        Map<String, Object> category = new HashMap<>();
        category.put("id", 1);
        category.put("name", "dogs");
        pet.setCategory(category);

        pet.setName("Бобик" + RANDOM.nextInt(1000));
        pet.setPhotoUrls(Arrays.asList("https://example.com/dog.jpg"));

        Map<String, Object> tag = new HashMap<>();
        tag.put("id", 1);
        tag.put("name", "friendly");
        pet.setTags(Arrays.asList(tag));

        pet.setStatus("available");

        return pet;
    }

    public static User generateRandomUserObject() {
        User user = new User();
        user.setId(0);
        user.setUsername("user" + System.currentTimeMillis());  // ← добавили
        user.setEmail(user.getUsername() + "@example.com");
        user.setPassword("secret123");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("1234567890");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
}
