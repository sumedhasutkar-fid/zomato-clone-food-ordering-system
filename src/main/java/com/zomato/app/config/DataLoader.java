package com.zomato.app.config;

import com.zomato.app.entity.*;
import com.zomato.app.repository.*;
import com.zomato.app.util.AppConstants;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final CouponRepository couponRepository;
    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final WalletRepository walletRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataLoader(FoodRepository foodRepository,
                      UserRepository userRepository,
                      RestaurantRepository restaurantRepository,
                      CategoryRepository categoryRepository,
                      CouponRepository couponRepository,
                      DeliveryPartnerRepository deliveryPartnerRepository,
                      InventoryItemRepository inventoryItemRepository,
                      WalletRepository walletRepository,
                      BCryptPasswordEncoder passwordEncoder) {
        this.foodRepository = foodRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
        this.couponRepository = couponRepository;
        this.deliveryPartnerRepository = deliveryPartnerRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createUserIfMissing("Demo User", "demo@zomato.com", "demo123", AppConstants.CUSTOMER_ROLE);
        createUserIfMissing("Admin User", "admin@zomato.com", "admin123", "ADMIN");
        createUserIfMissing("Delivery Boy", "delivery@zomato.com", "delivery123", "DELIVERY");

        if (restaurantRepository.findByName("Spice Hub Kitchen").isEmpty()) {
            Restaurant spice = restaurantRepository.save(new Restaurant(
                    "Spice Hub Kitchen",
                    "North Indian, Fast Food, Desserts",
                    "MG Road, Pune",
                    "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=1200&q=80",
                    4.5
            ));
            spice.setOpeningTime("10:00");
            spice.setClosingTime("23:00");
            restaurantRepository.save(spice);
        }

        if (restaurantRepository.findByName("Punjabi Thali House").isEmpty()) {
            Restaurant punjabi = restaurantRepository.save(new Restaurant(
                    "Punjabi Thali House",
                    "Punjabi, Thali, North Indian",
                    "FC Road, Pune",
                    "https://images.unsplash.com/photo-1585937421612-70a008356fbe?auto=format&fit=crop&w=1200&q=80",
                    4.6
            ));
            punjabi.setOpeningTime("11:00");
            punjabi.setClosingTime("22:30");
            restaurantRepository.save(punjabi);
        }

        Long pizza = category("Pizza & Pasta").getId();
        Long indian = category("Indian Meals").getId();
        Long snacks = category("Snacks").getId();
        Long drinks = category("Drinks & Desserts").getId();
        Long thali = category("Thali & Punjabi").getId();

        Long restaurantId = restaurantRepository.findByName("Spice Hub Kitchen").orElseThrow().getId();
        Long punjabiRestaurantId = restaurantRepository.findByName("Punjabi Thali House").orElseThrow().getId();

        createFoodIfMissing("Margherita Pizza", 249, "https://images.unsplash.com/photo-1604382354936-07c5d9983bd3?auto=format&fit=crop&w=900&q=80", "Classic cheese pizza with basil.", restaurantId, pizza);
        createFoodIfMissing("Veg Burger", 129, "https://images.unsplash.com/photo-1550547660-d9450f859349?auto=format&fit=crop&w=900&q=80", "Crispy patty burger with fresh veggies.", restaurantId, snacks);
        createFoodIfMissing("Paneer Biryani", 219, "https://images.unsplash.com/photo-1563379091339-03246963d4f6?auto=format&fit=crop&w=900&q=80", "Aromatic biryani with paneer cubes.", restaurantId, indian);
        createFoodIfMissing("Masala Dosa", 149, "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?auto=format&fit=crop&w=900&q=80", "Crisp dosa with spiced potato filling.", restaurantId, indian);
        createFoodIfMissing("Chocolate Shake", 99, "https://images.unsplash.com/photo-1572490122747-3968b75cc699?auto=format&fit=crop&w=900&q=80", "Cold chocolate shake.", restaurantId, drinks);
        createFoodIfMissing("Pasta Alfredo", 199, "https://images.unsplash.com/photo-1645112411341-6c4fd023714a?auto=format&fit=crop&w=900&q=80", "Creamy white sauce pasta.", restaurantId, pizza);
        createFoodIfMissing("Grilled Sandwich", 119, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?auto=format&fit=crop&w=900&q=80", "Grilled vegetable cheese sandwich.", restaurantId, snacks);
        createFoodIfMissing("Chole Bhature", 179, "https://images.unsplash.com/photo-1626132647523-66f5bf380027?auto=format&fit=crop&w=900&q=80", "Punjabi chole with fluffy bhature.", restaurantId, indian);
        createFoodIfMissing("Veg Fried Rice", 169, "https://images.unsplash.com/photo-1603133872878-684f208fb84b?auto=format&fit=crop&w=900&q=80", "Wok tossed rice with vegetables.", restaurantId, indian);
        createFoodIfMissing("Steamed Momos", 139, "https://images.unsplash.com/photo-1625220194771-7ebdea0b70b9?auto=format&fit=crop&w=900&q=80", "Soft momos with spicy dip.", restaurantId, snacks);
        createFoodIfMissing("Vanilla Ice Cream", 89, "https://images.unsplash.com/photo-1563805042-7684c019e1cb?auto=format&fit=crop&w=900&q=80", "Vanilla scoop dessert.", restaurantId, drinks);
        createFoodIfMissing("Cold Coffee", 109, "https://images.unsplash.com/photo-1461023058943-07fcbe16d735?auto=format&fit=crop&w=900&q=80", "Chilled coffee with foam.", restaurantId, drinks);
        createFoodIfMissing("Punjabi Special Thali", 299, "https://images.unsplash.com/photo-1546833999-b9f581a1996d?auto=format&fit=crop&w=900&q=80", "Dal makhani, paneer, roti, rice and sweet.", punjabiRestaurantId, thali);
        createFoodIfMissing("Amritsari Kulcha Combo", 229, "https://images.unsplash.com/photo-1601050690597-df0568f70950?auto=format&fit=crop&w=900&q=80", "Stuffed kulcha with chole and chutney.", punjabiRestaurantId, thali);
        createFoodIfMissing("Sarson Saag Makki Roti", 249, "https://images.unsplash.com/photo-1585937421612-70a008356fbe?auto=format&fit=crop&w=900&q=80", "Classic Punjabi saag and makki roti.", punjabiRestaurantId, thali);
        createFoodIfMissing("Paneer Butter Masala", 219, "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?auto=format&fit=crop&w=900&q=80", "Creamy paneer curry.", punjabiRestaurantId, thali);
        createFoodIfMissing("Rajma Chawal Bowl", 179, "https://images.unsplash.com/photo-1585937421612-70a008356fbe?auto=format&fit=crop&w=900&q=80", "Rajma curry with steamed rice.", punjabiRestaurantId, thali);
        createFoodIfMissing("Lassi Patiala", 99, "https://images.unsplash.com/photo-1626074353765-517a681e40be?auto=format&fit=crop&w=900&q=80", "Sweet Punjabi lassi.", punjabiRestaurantId, drinks);

        if (couponRepository.count() == 0) {
            couponRepository.save(new Coupon("WELCOME20", 20));
            couponRepository.save(new Coupon("THALI10", 10));
        }

        if (deliveryPartnerRepository.count() == 0) {
            deliveryPartnerRepository.save(new DeliveryPartner("Rahul Rider", "9000000001"));
            deliveryPartnerRepository.save(new DeliveryPartner("Amit Delivery", "9000000002"));
        }

        if (walletRepository.findByUserEmail("demo@zomato.com").isEmpty()) {
            walletRepository.save(new Wallet("demo@zomato.com", 500));
        }

        if (inventoryItemRepository.count() == 0) {
            foodRepository.findAll().forEach(food -> inventoryItemRepository.save(new InventoryItem(food.getId(), food.getName(), 50)));
        }
    }

    private void createUserIfMissing(String name, String email, String password, String role) {
        if (userRepository.existsByEmail(email)) {
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setMobile("9999999999");
        user.setAddress("Practice address");
        user.setRole(role);
        userRepository.save(user);
    }

    private Category category(String name) {
        return categoryRepository.findByName(name)
                .orElseGet(() -> categoryRepository.save(new Category(name)));
    }

    private void createFoodIfMissing(String name, double price, String imageUrl, String description, Long restaurantId, Long categoryId) {
        if (foodRepository.findByNameAndRestaurantId(name, restaurantId).isPresent()) {
            return;
        }
        Food food = foodRepository.save(new Food(name, price, imageUrl, description, restaurantId, categoryId));
        inventoryItemRepository.save(new InventoryItem(food.getId(), food.getName(), 50));
    }
}
