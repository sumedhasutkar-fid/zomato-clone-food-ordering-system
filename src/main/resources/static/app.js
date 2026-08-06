const state = {
    token: localStorage.getItem("zomatoToken"),
    refreshToken: localStorage.getItem("zomatoRefreshToken"),
    restaurantId: 1,
    addressId: null,
    latestOrderId: null,
    cart: [],
    coupon: null
};

const authScreen = document.getElementById("authScreen");
const appScreen = document.getElementById("appScreen");
const foodGrid = document.getElementById("foodGrid");
const cartItems = document.getElementById("cartItems");
const cartTotal = document.getElementById("cartTotal");
const authMessage = document.getElementById("authMessage");
const orderMessage = document.getElementById("orderMessage");
const couponMessage = document.getElementById("couponMessage");
const loginStatus = document.getElementById("loginStatus");

document.getElementById("loginTab").addEventListener("click", () => showAuth("login"));
document.getElementById("registerTab").addEventListener("click", () => showAuth("register"));
document.getElementById("loginForm").addEventListener("submit", login);
document.getElementById("registerForm").addEventListener("submit", register);
document.getElementById("logoutBtn").addEventListener("click", logout);
document.getElementById("searchBtn").addEventListener("click", loadFoods);
document.getElementById("searchInput").addEventListener("input", loadSuggestions);
document.getElementById("placeOrder").addEventListener("click", placeOrder);
document.getElementById("payOrder").addEventListener("click", payOrder);
document.getElementById("walletPayOrder").addEventListener("click", walletPayOrder);
document.getElementById("trackOrder").addEventListener("click", trackOrder);
document.getElementById("applyCoupon").addEventListener("click", applyCoupon);
document.getElementById("addressForm").addEventListener("submit", saveAddress);
document.getElementById("reviewForm").addEventListener("submit", saveReview);

function showAuth(type) {
    const isLogin = type === "login";
    document.getElementById("loginForm").classList.toggle("hidden", !isLogin);
    document.getElementById("registerForm").classList.toggle("hidden", isLogin);
    document.getElementById("loginTab").classList.toggle("active", isLogin);
    document.getElementById("registerTab").classList.toggle("active", !isLogin);
    setMessage(authMessage, "");
}

async function register(event) {
    event.preventDefault();
    const payload = {
        name: document.getElementById("registerName").value,
        email: document.getElementById("registerEmail").value,
        password: document.getElementById("registerPassword").value,
        mobile: document.getElementById("registerMobile").value,
        address: document.getElementById("registerAddress").value
    };
    const response = await api("/auth/register", "POST", payload, false);
    setMessage(authMessage, response.message || response, response.message !== undefined || String(response).includes("Successful"));
    if (String(response).includes("Successful")) {
        document.getElementById("loginEmail").value = payload.email;
        document.getElementById("loginPassword").value = payload.password;
        showAuth("login");
    }
}

async function login(event) {
    event.preventDefault();
    const payload = {
        email: document.getElementById("loginEmail").value,
        password: document.getElementById("loginPassword").value
    };
    const data = await api("/auth/login", "POST", payload, false);
    if (!data.token) {
        setMessage(authMessage, data.message || "Login failed", false);
        return;
    }
    state.token = data.token;
    state.refreshToken = data.refreshToken;
    localStorage.setItem("zomatoToken", data.token);
    localStorage.setItem("zomatoRefreshToken", data.refreshToken);
    loginStatus.textContent = `${data.name} (${data.role})`;
    await bootApp();
}

async function bootApp() {
    authScreen.classList.add("hidden");
    appScreen.classList.remove("hidden");
    await loadRestaurants();
    await loadFoods();
    await loadCart();
    await loadOrders();
    await loadWallet();
    await loadReviews();
    await loadFavorites();
    await loadRecommendations();
    await loadNotifications();
}

async function loadRestaurants() {
    const restaurants = await api("/restaurants", "GET", null, false);
    const active = restaurants.find(item => item.id === state.restaurantId) || restaurants[0];
    state.restaurantId = active.id;
    renderRestaurant(active);
    document.getElementById("restaurantList").innerHTML = restaurants.map(restaurant => `
        <button class="restaurant-pill ${restaurant.id === state.restaurantId ? "active-pill" : ""}" type="button" onclick="selectRestaurant(${restaurant.id})">
            ${restaurant.name}
        </button>
    `).join("");
}

async function selectRestaurant(id) {
    state.restaurantId = id;
    await loadRestaurants();
    await loadFoods();
    await loadReviews();
    await loadRecommendations();
}

function renderRestaurant(restaurant) {
    document.getElementById("restaurantHero").innerHTML = `
        <img src="${restaurant.imageUrl}" alt="${restaurant.name}">
        <div>
            <p class="eyebrow">${restaurant.cuisine}</p>
            <h2>${restaurant.name}</h2>
            <p>${restaurant.location} | Rating ${restaurant.rating}</p>
            <p>Timing: ${restaurant.openingTime || "10:00"} - ${restaurant.closingTime || "23:00"} | ${restaurant.open ? "Open" : "Closed"}</p>
        </div>
    `;
}

async function loadFoods() {
    const query = document.getElementById("searchInput").value;
    const sortBy = document.getElementById("sortSelect").value;
    const data = await api(`/foods/search?restaurantId=${state.restaurantId}&query=${encodeURIComponent(query)}&page=0&size=24&sortBy=${sortBy}`, "GET", null, false);
    const foods = data.content || data;

    foodGrid.innerHTML = foods.map(food => {
        const cartItem = state.cart.find(item => item.foodId === food.id);
        const qty = cartItem ? cartItem.quantity : 0;
        return `
            <article class="food-card">
                <img src="${food.imageUrl}" alt="${food.name}">
                <div class="food-body">
                    <div class="food-title-row">
                        <h3>${food.name}</h3>
                        <span class="price">Rs ${food.price}</span>
                    </div>
                    <p>${food.description || ""}</p>
                    <p>Stock: ${food.stockQuantity}</p>
                    <div class="qty-row">
                        <button type="button" onclick="decreaseCart(${food.id})">-</button>
                        <strong>${qty}</strong>
                        <button type="button" onclick="addToCart(${food.id})">+</button>
                        <button class="wish-btn" type="button" onclick="toggleFavorite(${food.id})">Wishlist</button>
                    </div>
                </div>
            </article>
        `;
    }).join("");
}

async function loadSuggestions() {
    const query = document.getElementById("searchInput").value;
    if (!query) {
        document.getElementById("suggestionsList").innerHTML = "";
        return;
    }
    const suggestions = await api(`/search/suggestions?query=${encodeURIComponent(query)}`, "GET", null, false);
    document.getElementById("suggestionsList").innerHTML = suggestions.map(item => `<button type="button" onclick="useSuggestion('${item}')">${item}</button>`).join("");
}

function useSuggestion(value) {
    document.getElementById("searchInput").value = value;
    loadFoods();
}

async function addToCart(foodId) {
    await api("/cart", "POST", { foodId, quantity: 1 });
    await loadCart();
    await loadFoods();
}

async function decreaseCart(foodId) {
    await api(`/cart/${foodId}`, "DELETE");
    await loadCart();
    await loadFoods();
}

async function loadCart() {
    const items = await api("/cart");
    state.cart = Array.isArray(items) ? items : [];
    if (!state.cart.length) {
        cartItems.innerHTML = "<p>Your cart is empty.</p>";
        cartTotal.textContent = "Rs 0";
        return;
    }
    cartItems.innerHTML = state.cart.map(item => `
        <div class="cart-item">
            <div><strong>${item.foodName}</strong><span>Qty ${item.quantity} x Rs ${item.price}</span></div>
            <strong>Rs ${item.quantity * item.price}</strong>
        </div>
    `).join("");
    cartTotal.textContent = `Rs ${cartAmount()}`;
}

function cartAmount() {
    return state.cart.reduce((sum, item) => sum + item.price * item.quantity, 0);
}

async function applyCoupon() {
    const code = document.getElementById("couponCode").value;
    const response = await api("/coupons/apply", "POST", { code, amount: cartAmount() });
    if (response.finalAmount !== undefined) {
        state.coupon = response;
        setMessage(couponMessage, `${response.code} applied. Final amount Rs ${response.finalAmount}`, true);
        return;
    }
    setMessage(couponMessage, response.message || "Coupon failed", false);
}

async function loadWallet() {
    const wallet = await api("/wallet");
    document.getElementById("walletBalance").textContent = `Wallet: Rs ${wallet.balance || 0}`;
}

async function saveAddress(event) {
    event.preventDefault();
    const address = await api("/addresses", "POST", {
        label: document.getElementById("addressLabel").value,
        line1: document.getElementById("addressLine").value,
        city: document.getElementById("addressCity").value,
        pincode: document.getElementById("addressPincode").value
    });
    state.addressId = address.id;
    setMessage(orderMessage, "Address saved.", true);
}

async function placeOrder() {
    if (!state.addressId) {
        await saveAddress(new Event("submit"));
    }
    const order = await api("/orders", "POST", { addressId: state.addressId });
    if (!order.id) {
        setMessage(orderMessage, order.message || "Order failed", false);
        return;
    }
    state.latestOrderId = order.id;
    document.getElementById("payOrder").disabled = false;
    document.getElementById("walletPayOrder").disabled = false;
    document.getElementById("trackOrder").disabled = false;
    setMessage(orderMessage, `Order #${order.id} placed. Pay now.`, true);
    await loadCart();
    await loadFoods();
    await loadOrders();
    await loadNotifications();
}

async function payOrder() {
    const payment = await api(`/payments/mock/${state.latestOrderId}`, "POST", {});
    setMessage(orderMessage, `Mock payment success: ${payment.transactionId}`, true);
    document.getElementById("payOrder").disabled = true;
    await loadOrders();
}

async function walletPayOrder() {
    const payment = await api(`/payments/wallet/${state.latestOrderId}`, "POST", {});
    if (!payment.transactionId) {
        setMessage(orderMessage, payment.message || "Wallet payment failed", false);
        return;
    }
    setMessage(orderMessage, `Wallet payment success: ${payment.transactionId}`, true);
    document.getElementById("walletPayOrder").disabled = true;
    await loadWallet();
    await loadOrders();
}

async function trackOrder() {
    const tracking = await api(`/tracking/${state.latestOrderId}`);
    document.getElementById("trackingBox").innerHTML = `
        <strong>${tracking.status}</strong>
        <p>${tracking.trackingStatus}</p>
        <p>${tracking.distanceKm} km away</p>
    `;
}

async function loadOrders() {
    const orders = await api("/orders");
    document.getElementById("ordersList").innerHTML = orders.length
        ? orders.map(order => `<p>Order #${order.id} | Rs ${order.totalAmount} | ${order.status} | ${order.paymentStatus} | ${order.trackingStatus}</p>`).join("")
        : "<p>No orders yet.</p>";
}

async function saveReview(event) {
    event.preventDefault();
    await api("/reviews", "POST", {
        restaurantId: state.restaurantId,
        rating: Number(document.getElementById("ratingInput").value),
        comment: document.getElementById("reviewComment").value
    });
    document.getElementById("reviewComment").value = "";
    await loadReviews();
}

async function loadReviews() {
    const reviews = await api(`/reviews?restaurantId=${state.restaurantId}`, "GET", null, false);
    document.getElementById("reviewsList").innerHTML = reviews.length
        ? reviews.map(review => `<p>${review.rating} stars - ${review.comment}</p>`).join("")
        : "<p>No reviews yet.</p>";
}

async function toggleFavorite(foodId) {
    await api(`/favorites/${foodId}`, "POST", {});
    await loadFavorites();
}

async function loadFavorites() {
    const favorites = await api("/favorites");
    document.getElementById("favoritesList").innerHTML = favorites.length
        ? favorites.map(item => `<p>${item.foodName}</p>`).join("")
        : "<p>No wishlist items.</p>";
}

async function loadRecommendations() {
    const foods = await api(`/search/recommendations?restaurantId=${state.restaurantId}`, "GET", null, false);
    document.getElementById("recommendationsList").innerHTML = foods.length
        ? foods.map(food => `<p>${food.name} - Rs ${food.price}</p>`).join("")
        : "<p>No recommendations.</p>";
}

async function loadNotifications() {
    const notifications = await api("/notifications");
    document.getElementById("notificationsList").innerHTML = notifications.length
        ? notifications.slice(0, 5).map(item => `<p>${item.message}</p>`).join("")
        : "<p>No notifications.</p>";
}

async function api(url, method = "GET", body = null, auth = true) {
    const headers = { "Content-Type": "application/json" };
    if (auth && state.token) {
        headers.Authorization = `Bearer ${state.token}`;
    }

    const response = await fetch(url, {
        method,
        headers,
        body: body ? JSON.stringify(body) : null
    });

    const contentType = response.headers.get("Content-Type") || "";
    const data = contentType.includes("application/json") ? await response.json() : await response.text();
    if (!response.ok) {
        return { message: data.message || data || "Request failed" };
    }
    return data;
}

function logout() {
    localStorage.removeItem("zomatoToken");
    localStorage.removeItem("zomatoRefreshToken");
    location.reload();
}

function setMessage(element, text, success) {
    element.textContent = text;
    element.classList.toggle("success", Boolean(success));
    element.classList.toggle("error", text && !success);
}

if (state.token) {
    bootApp();
}
