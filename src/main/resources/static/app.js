const state = {
    token: localStorage.getItem("zomatoToken"),
    refreshToken: localStorage.getItem("zomatoRefreshToken"),
    restaurantId: 1,
    addressId: null,
    latestOrderId: null
};

const authScreen = document.getElementById("authScreen");
const appScreen = document.getElementById("appScreen");
const foodGrid = document.getElementById("foodGrid");
const cartItems = document.getElementById("cartItems");
const cartTotal = document.getElementById("cartTotal");
const authMessage = document.getElementById("authMessage");
const orderMessage = document.getElementById("orderMessage");
const loginStatus = document.getElementById("loginStatus");

document.getElementById("loginTab").addEventListener("click", () => showAuth("login"));
document.getElementById("registerTab").addEventListener("click", () => showAuth("register"));
document.getElementById("loginForm").addEventListener("submit", login);
document.getElementById("registerForm").addEventListener("submit", register);
document.getElementById("logoutBtn").addEventListener("click", logout);
document.getElementById("searchBtn").addEventListener("click", loadFoods);
document.getElementById("placeOrder").addEventListener("click", placeOrder);
document.getElementById("payOrder").addEventListener("click", payOrder);
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
    await loadRestaurant();
    await loadFoods();
    await loadCart();
    await loadOrders();
    await loadReviews();
}

async function loadRestaurant() {
    const restaurants = await api("/restaurants", "GET", null, false);
    const restaurant = restaurants[0];
    state.restaurantId = restaurant.id;
    document.getElementById("restaurantHero").innerHTML = `
        <img src="${restaurant.imageUrl}" alt="${restaurant.name}">
        <div>
            <p class="eyebrow">${restaurant.cuisine}</p>
            <h2>${restaurant.name}</h2>
            <p>${restaurant.location} | Rating ${restaurant.rating}</p>
        </div>
    `;
}

async function loadFoods() {
    const query = document.getElementById("searchInput").value;
    const sortBy = document.getElementById("sortSelect").value;
    const data = await api(`/foods/search?restaurantId=${state.restaurantId}&query=${encodeURIComponent(query)}&page=0&size=12&sortBy=${sortBy}`, "GET", null, false);
    const foods = data.content || data;

    foodGrid.innerHTML = foods.map(food => `
        <article class="food-card">
            <img src="${food.imageUrl}" alt="${food.name}">
            <div class="food-body">
                <div class="food-title-row">
                    <h3>${food.name}</h3>
                    <span class="price">Rs ${food.price}</span>
                </div>
                <p>${food.description || ""}</p>
                <button class="food-action" type="button" onclick="addToCart(${food.id})">Add</button>
            </div>
        </article>
    `).join("");
}

async function addToCart(foodId) {
    await api("/cart", "POST", { foodId, quantity: 1 });
    await loadCart();
}

async function loadCart() {
    const items = await api("/cart");
    if (!items.length) {
        cartItems.innerHTML = "<p>Your cart is empty.</p>";
        cartTotal.textContent = "Rs 0";
        return;
    }

    cartItems.innerHTML = items.map(item => `
        <div class="cart-item">
            <div><strong>${item.foodName}</strong><span>Qty ${item.quantity} x Rs ${item.price}</span></div>
            <strong>Rs ${item.quantity * item.price}</strong>
        </div>
    `).join("");
    cartTotal.textContent = `Rs ${items.reduce((sum, item) => sum + item.price * item.quantity, 0)}`;
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
    state.latestOrderId = order.id;
    document.getElementById("payOrder").disabled = false;
    setMessage(orderMessage, `Order #${order.id} placed. Pay now.`, true);
    await loadCart();
    await loadOrders();
}

async function payOrder() {
    const payment = await api(`/payments/mock/${state.latestOrderId}`, "POST", {});
    setMessage(orderMessage, `Payment success: ${payment.transactionId}`, true);
    document.getElementById("payOrder").disabled = true;
    await loadOrders();
}

async function loadOrders() {
    const orders = await api("/orders");
    document.getElementById("ordersList").innerHTML = orders.length
        ? orders.map(order => `<p>Order #${order.id} | Rs ${order.totalAmount} | ${order.status} | ${order.paymentStatus}</p>`).join("")
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
