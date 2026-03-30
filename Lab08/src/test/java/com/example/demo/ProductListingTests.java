package com.example.demo;

import com.example.demo.model.CartItem;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductListingTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void searchByKeywordShouldReturnMatchingProducts() throws Exception {
        createProduct("Phone Alpha", 1000, null);
        createProduct("Laptop Beta", 2000, null);
        createProduct("Phone Gamma", 3000, null);

        mockMvc.perform(get("/products")
                        .session(loginAsAdmin())
                        .param("keyword", "phone"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Phone Alpha")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Phone Gamma")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Laptop Beta"))));
    }

    @Test
    void paginationShouldShowFiveProductsPerPage() throws Exception {
        for (int i = 1; i <= 6; i++) {
            createProduct("Product " + i, i * 1000L, null);
        }

        mockMvc.perform(get("/products")
                        .session(loginAsAdmin())
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Product 1")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Product 5")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Product 6"))))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Trang 1 / 2")));

        mockMvc.perform(get("/products")
                        .session(loginAsAdmin())
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Product 6")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Product 1"))))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Trang 2 / 2")));
    }

    @Test
    void sortByPriceShouldRenderAscendingAndDescendingOrder() throws Exception {
        createProduct("Mid", 2000, null);
        createProduct("Cheap", 1000, null);
        createProduct("Expensive", 3000, null);

        String ascending = mockMvc.perform(get("/products")
                        .session(loginAsAdmin())
                        .param("sort", "priceAsc"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(ascending.indexOf("Cheap") < ascending.indexOf("Mid"));
        assertTrue(ascending.indexOf("Mid") < ascending.indexOf("Expensive"));

        String descending = mockMvc.perform(get("/products")
                        .session(loginAsAdmin())
                        .param("sort", "priceDesc"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(descending.indexOf("Expensive") < descending.indexOf("Mid"));
        assertTrue(descending.indexOf("Mid") < descending.indexOf("Cheap"));
    }

    @Test
    void filterByCategoryShouldOnlyShowMatchingProducts() throws Exception {
        Category phoneCategory = createCategory("Phone");
        Category laptopCategory = createCategory("Laptop");

        createProduct("Phone Alpha", 1000, phoneCategory);
        createProduct("Phone Beta", 2000, phoneCategory);
        createProduct("Laptop Gamma", 3000, laptopCategory);

        mockMvc.perform(get("/products")
                        .session(loginAsAdmin())
                        .param("categoryId", String.valueOf(phoneCategory.getId())))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Phone Alpha")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Phone Beta")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Laptop Gamma"))));
    }

    @Test
    void addToCartShouldSaveQuantityIntoSession() throws Exception {
        Product product = createProduct("Phone Alpha", 1000, null);

        MvcResult result = mockMvc.perform(post("/cart/add")
                        .session(loginAsAdmin())
                        .param("productId", String.valueOf(product.getId()))
                        .param("quantity", "3"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        @SuppressWarnings("unchecked")
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute(CartService.CART_SESSION_KEY);

        assertEquals(1, cart.size());
        assertEquals(3, cart.get(product.getId()).getQuantity());
    }

    @Test
    void checkoutShouldCreateOrderAndOrderDetailsAndClearCart() throws Exception {
        Product p1 = createProduct("Phone Alpha", 1000, null);
        Product p2 = createProduct("Laptop Beta", 3000, null);

        MockHttpSession session = loginAsAdmin();

        MvcResult addOne = mockMvc.perform(post("/cart/add")
                        .session(session)
                        .param("productId", String.valueOf(p1.getId()))
                        .param("quantity", "2"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        session = (MockHttpSession) addOne.getRequest().getSession(false);

        MvcResult addTwo = mockMvc.perform(post("/cart/add")
                        .session(session)
                        .param("productId", String.valueOf(p2.getId()))
                        .param("quantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        session = (MockHttpSession) addTwo.getRequest().getSession(false);

        MvcResult checkoutResult = mockMvc.perform(post("/cart/checkout")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        String redirectUrl = checkoutResult.getResponse().getRedirectedUrl();
        assertTrue(redirectUrl != null && redirectUrl.startsWith("/orders/"));

        assertEquals(1, orderRepository.count());
        com.example.demo.model.Order order = orderRepository.findAll().get(0);
        assertEquals(5000L, order.getTotalAmount());
        assertEquals(2L, orderDetailRepository.countByOrderId(order.getId()));

        @SuppressWarnings("unchecked")
        Map<Integer, CartItem> cartAfterCheckout =
                (Map<Integer, CartItem>) checkoutResult.getRequest().getSession(false)
                        .getAttribute(CartService.CART_SESSION_KEY);
        assertTrue(cartAfterCheckout == null || cartAfterCheckout.isEmpty());
    }

    private MockHttpSession loginAsAdmin() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                        .param("username", "admin")
                        .param("password", "admin123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andReturn();

        HttpSession session = result.getRequest().getSession(false);
        return (MockHttpSession) session;
    }

    private Product createProduct(String name, long price, Category category) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCategory(category);
        return productRepository.save(product);
    }

    private Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }
}
