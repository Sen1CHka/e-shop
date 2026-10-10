package eshop.configuration;

import eshop.domain.*;
import eshop.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fills an empty database with demo data so the shop can be tried out right after the first start.
 */
@Component
@ConditionalOnProperty(name = "app.seed-data", havingValue = "true")
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    private final Map<String, Product> products = new LinkedHashMap<>();
    private int productAge;

    public DataSeeder(UserRepository userRepository, CategoryRepository categoryRepository,
                      ProductRepository productRepository, ReviewRepository reviewRepository,
                      AddressRepository addressRepository, OrderRepository orderRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.addressRepository = addressRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        user("admin", "Shop Admin", "admin@eshop.cz", "admin123", Role.ADMIN);
        User jana = user("jana", "Jana Novakova", "jana@example.com", "jana123", Role.USER);
        User petr = user("petr", "Petr Svoboda", "petr@example.com", "petr123", Role.USER);

        Category laptops = category("Laptops", "Notebooks for work, study and play", "💻");
        Category phones = category("Phones", "Smartphones and tablets", "📱");
        Category audio = category("Audio", "Headphones, speakers and microphones", "🎧");
        Category gaming = category("Gaming", "Consoles, controllers and gaming gear", "🎮");
        Category smartHome = category("Smart home", "Lights, sensors and assistants", "🏠");
        Category accessories = category("Accessories", "Cables, chargers, bags and more", "🔌");

        product(laptops, "AeroBook 14", "Lumen", "24990", 12,
                "Light 14\" ultrabook with an aluminium body, 16 GB RAM, 512 GB SSD and up to 15 hours of battery life. Weighs just 1.2 kg.");
        product(laptops, "AeroBook 16 Pro", "Lumen", "42990", 5,
                "16\" workstation with a 120 Hz mini-LED display, 32 GB RAM and 1 TB SSD. Built for editing video and compiling code.");
        product(laptops, "StudyMate 15", "Kestrel", "11490", 30,
                "Affordable 15.6\" notebook for school and office work. 8 GB RAM, 256 GB SSD, full-size keyboard with numeric pad.");
        product(laptops, "Raptor G17", "Kestrel", "38990", 3,
                "17\" gaming laptop with a dedicated graphics card, 144 Hz display and RGB backlit keyboard.");
        product(laptops, "FlexPad 13 2-in-1", "Lumen", "19990", 0,
                "Convertible 13\" touchscreen notebook with a 360° hinge and a stylus in the box.");

        product(phones, "Nova X5", "Orbit", "17990", 20,
                "6.4\" OLED display, triple camera with optical stabilisation, 256 GB storage and 5G.");
        product(phones, "Nova X5 Lite", "Orbit", "8990", 42,
                "The essentials of Nova X5 at half the price. 6.5\" display, 128 GB storage, two-day battery.");
        product(phones, "Pebble Mini", "Tamo", "5490", 15,
                "Compact 5.4\" phone that fits every pocket. Simple, fast and with a headphone jack.");
        product(phones, "Orbit Tab 11", "Orbit", "9990", 8,
                "11\" tablet with a 2K display and four speakers. Great for films, notes and drawing.");

        product(audio, "QuietWave 700", "Sonora", "6490", 18,
                "Over-ear wireless headphones with active noise cancelling and 35 hours of playback.");
        product(audio, "Buds Air 3", "Sonora", "2290", 60,
                "True wireless earbuds with a charging case, IPX4 sweat resistance and clear call microphones.");
        product(audio, "BoomBox Go", "Sonora", "1890", 25,
                "Waterproof portable Bluetooth speaker. 12 hours of music, floats in water.");
        product(audio, "StudioCast USB", "Tamo", "3290", 4,
                "USB condenser microphone for podcasts, streaming and video calls. Plug and play.");

        product(gaming, "PlayCube 5", "Kestrel", "13990", 7,
                "Home console with 4K gaming, 1 TB storage and one wireless controller included.");
        product(gaming, "PlayCube Controller", "Kestrel", "1690", 35,
                "Wireless controller with haptic feedback and a rechargeable battery. Available in black.");
        product(gaming, "Viper Pro Mouse", "Tamo", "1290", 22,
                "Ultra-light 59 g gaming mouse with a 26 000 DPI sensor and six programmable buttons.");
        product(gaming, "MechaBoard TKL", "Tamo", "2490", 2,
                "Tenkeyless mechanical keyboard with hot-swappable switches and per-key RGB lighting.");

        product(smartHome, "GlowBulb Color", "Lumen", "399", 120,
                "Smart E27 LED bulb with 16 million colours. Works over Wi-Fi without a hub.");
        product(smartHome, "HomeHub Mini", "Orbit", "1490", 16,
                "Small smart speaker with a voice assistant that controls lights, plugs and thermostats.");
        product(smartHome, "SafeCam 2K", "Orbit", "1790", 9,
                "Indoor security camera with night vision, motion alerts and two-way audio.");
        product(smartHome, "SmartPlug Duo", "Lumen", "549", 75,
                "Pack of two smart plugs with energy monitoring and schedules.");

        product(accessories, "PowerBrick 65W", "Tamo", "890", 50,
                "Compact GaN charger with two USB-C ports. Charges a laptop and a phone at once.");
        product(accessories, "USB-C Cable 2 m", "Tamo", "249", 200,
                "Braided USB-C to USB-C cable, 100 W power delivery, 2 metres.");
        product(accessories, "CityPack 15", "Kestrel", "1390", 14,
                "Water-repellent backpack with a padded pocket for a 15\" laptop.");
        product(accessories, "PowerBank 20000", "Tamo", "990", 1,
                "20 000 mAh power bank with fast charging and a built-in display.");

        review("AeroBook 14", jana, 5, "Light, quiet and the battery really lasts a whole day of lectures.");
        review("AeroBook 14", petr, 4, "Great laptop, I only miss an HDMI port.");
        review("Nova X5", petr, 5, "Excellent camera, especially at night.");
        review("QuietWave 700", jana, 5, "The noise cancelling on the tram is a game changer.");
        review("Buds Air 3", petr, 3, "Good sound for the price, but the case scratches easily.");
        review("PlayCube 5", jana, 4, "Fast and silent. The storage fills up quickly though.");
        review("GlowBulb Color", petr, 4, "Easy setup, colours are vivid.");
        review("USB-C Cable 2 m", jana, 5, "Sturdy cable, charges my laptop without problems.");

        address(jana, "Jana Novakova", "Technicka 2", "Praha 6", "160 00", "+420 601 111 222", true);
        address(jana, "Jana Novakova (work)", "Karlovo namesti 13", "Praha 2", "121 35", "+420 601 111 222", false);
        address(petr, "Petr Svoboda", "Kolejni 550/2", "Praha 6", "160 00", "+420 602 333 444", true);

        order(jana, OrderState.DELIVERED, 21, DeliveryMethod.COURIER, PaymentMethod.CARD,
                Map.of("AeroBook 14", 1, "USB-C Cable 2 m", 2));
        order(jana, OrderState.SHIPPED, 3, DeliveryMethod.PARCEL_LOCKER, PaymentMethod.CARD,
                Map.of("QuietWave 700", 1));
        order(jana, OrderState.NEW, 0, DeliveryMethod.PICKUP, PaymentMethod.CASH_ON_DELIVERY,
                Map.of("PlayCube 5", 1, "PlayCube Controller", 1));
        order(petr, OrderState.PROCESSING, 1, DeliveryMethod.COURIER, PaymentMethod.BANK_TRANSFER,
                Map.of("Nova X5", 1, "Buds Air 3", 1, "PowerBrick 65W", 1));
        order(petr, OrderState.CANCELLED, 10, DeliveryMethod.COURIER, PaymentMethod.CARD,
                Map.of("GlowBulb Color", 4));

        log.info("Demo data created. Sign in as admin/admin123 (administrator) or jana/jana123 (customer).");
    }

    private User user(String username, String realName, String email, String password, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setRealName(realName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }

    private Category category(String name, String description, String icon) {
        String slug = name.toLowerCase().replace(' ', '-');
        return categoryRepository.save(new Category(name, slug, description, icon));
    }

    private void product(Category category, String name, String brand, String price, int stock, String description) {
        Product product = new Product();
        product.setCategory(category);
        product.setName(name);
        product.setBrand(brand);
        product.setPrice(new BigDecimal(price));
        product.setAvailableAmount(stock);
        product.setDescription(description);
        // Spread creation dates so that sorting by "newest" gives a stable, meaningful order
        product.setCreatedAt(LocalDateTime.now().minusDays(productAge++).truncatedTo(ChronoUnit.SECONDS));
        products.put(name, productRepository.save(product));
    }

    private void review(String productName, User user, int rating, String comment) {
        reviewRepository.save(new Review(products.get(productName), user, rating, comment));
    }

    private void address(User user, String fullName, String street, String city, String zipCode, String phone,
                         boolean defaultAddress) {
        Address address = new Address();
        address.setUser(user);
        address.setFullName(fullName);
        address.setStreet(street);
        address.setCity(city);
        address.setZipCode(zipCode);
        address.setCountry("Czech Republic");
        address.setPhone(phone);
        address.setDefaultAddress(defaultAddress);
        addressRepository.save(address);
    }

    private void order(User user, OrderState state, int daysAgo, DeliveryMethod delivery, PaymentMethod payment,
                       Map<String, Integer> items) {
        Order order = new Order();
        order.setUser(user);
        order.setState(state);
        order.setDate(LocalDateTime.now().minusDays(daysAgo).truncatedTo(ChronoUnit.SECONDS));
        order.setDeliveryMethod(delivery);
        order.setPaymentMethod(payment);
        order.setShipping(new ShippingAddress(user.getRealName(), "Technicka 2", "Praha 6", "160 00",
                "Czech Republic", "+420 601 111 222"));
        items.forEach((name, quantity) -> order.addItem(products.get(name), quantity));
        order.calculateTotalPrice();
        orderRepository.save(order);
    }
}
