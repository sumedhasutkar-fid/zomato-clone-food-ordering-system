package com.zomato.app.config;

import com.zomato.app.entity.Category;
import com.zomato.app.entity.Food;
import com.zomato.app.entity.Restaurant;
import com.zomato.app.entity.User;
import com.zomato.app.repository.CategoryRepository;
import com.zomato.app.repository.FoodRepository;
import com.zomato.app.repository.RestaurantRepository;
import com.zomato.app.repository.UserRepository;
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
    private final BCryptPasswordEncoder passwordEncoder;

    public DataLoader(FoodRepository foodRepository,
                      UserRepository userRepository,
                      RestaurantRepository restaurantRepository,
                      CategoryRepository categoryRepository,
                      BCryptPasswordEncoder passwordEncoder) {
        this.foodRepository = foodRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createUserIfMissing("Demo User", "demo@zomato.com", "demo123", AppConstants.CUSTOMER_ROLE);
        createUserIfMissing("Admin User", "admin@zomato.com", "admin123", "ADMIN");

        if (restaurantRepository.count() == 0) {
            restaurantRepository.save(new Restaurant(
                    "Spice Hub Kitchen",
                    "North Indian, Fast Food, Desserts",
                    "MG Road, Pune",
                    "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=1200&q=80",
                    4.5
            ));
        }

        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category("Pizza & Pasta"));
            categoryRepository.save(new Category("Indian Meals"));
            categoryRepository.save(new Category("Snacks"));
            categoryRepository.save(new Category("Drinks & Desserts"));
        }

        if (foodRepository.count() == 0) {
            Long restaurantId = restaurantRepository.findAll().get(0).getId();
            Long pizza = categoryRepository.findAll().get(0).getId();
            Long indian = categoryRepository.findAll().get(1).getId();
            Long snacks = categoryRepository.findAll().get(2).getId();
            Long drinks = categoryRepository.findAll().get(3).getId();

            foodRepository.save(new Food("Margherita Pizza", 249, "https://images.unsplash.com/photo-1604382354936-07c5d9983bd3?auto=format&fit=crop&w=900&q=80", "Classic cheese pizza with basil.", restaurantId, pizza));
            foodRepository.save(new Food("Veg Burger", 129, "https://images.unsplash.com/photo-1550547660-d9450f859349?auto=format&fit=crop&w=900&q=80", "Crispy patty burger with fresh veggies.", restaurantId, snacks));
            foodRepository.save(new Food("Paneer Biryani", 219, "https://images.unsplash.com/photo-1563379091339-03246963d4f6?auto=format&fit=crop&w=900&q=80", "Aromatic biryani with paneer cubes.", restaurantId, indian));
            foodRepository.save(new Food("Masala Dosa", 149, "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?auto=format&fit=crop&w=900&q=80", "Crisp dosa with spiced potato filling.", restaurantId, indian));
            foodRepository.save(new Food("Chocolate Shake", 99, "https://images.unsplash.com/photo-1572490122747-3968b75cc699?auto=format&fit=crop&w=900&q=80", "Cold chocolate shake.", restaurantId, drinks));
            foodRepository.save(new Food("Pasta Alfredo", 199, "https://images.unsplash.com/photo-1645112411341-6c4fd023714a?auto=format&fit=crop&w=900&q=80", "Creamy white sauce pasta.", restaurantId, pizza));
            foodRepository.save(new Food("Grilled Sandwich", 119, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?auto=format&fit=crop&w=900&q=80", "Grilled vegetable cheese sandwich.", restaurantId, snacks));
            foodRepository.save(new Food("Chole Bhature", 179, "https://images.unsplash.com/photo-1626132647523-66f5bf380027?auto=format&fit=crop&w=900&q=80", "Punjabi chole with fluffy bhature.", restaurantId, indian));
            foodRepository.save(new Food("Veg Fried Rice", 169, "https://images.unsplash.com/photo-1603133872878-684f208fb84b?auto=format&fit=crop&w=900&q=80", "Wok tossed rice with vegetables.", restaurantId, indian));
            foodRepository.save(new Food("Steamed Momos", 139, "https://images.unsplash.com/photo-1625220194771-7ebdea0b70b9?auto=format&fit=crop&w=900&q=80", "Soft momos with spicy dip.", restaurantId, snacks));
            foodRepository.save(new Food("Vanilla Ice Cream", 89, "https://images.unsplash.com/photo-1563805042-7684c019e1cb?auto=format&fit=crop&w=900&q=80", "Vanilla scoop dessert.", restaurantId, drinks));
            foodRepository.save(new Food("Cold Coffee", 109, "https://images.unsplash.com/photo-1461023058943-07fcbe16d735?auto=format&fit=crop&w=900&q=80", "Chilled coffee with foam.", restaurantId, drinks));
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
}
