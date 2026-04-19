<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Bloom · Botanical Lifestyle Store</title>
  <link rel="stylesheet" href="css/bloom.css"/>
</head>
<body>

<!-- ── NAVIGATION ── -->
<nav class="nav">
  <div class="nav-inner">
    <a href="index.jsp" class="nav-logo">Bl<span>oo</span>m</a>
    <ul class="nav-links">
      <li><a href="products.jsp" class="active">Shop</a></li>
      <li><a href="products.jsp?cat=plants">Plants</a></li>
      <li><a href="products.jsp?cat=pots">Pots & Planters</a></li>
      <li><a href="products.jsp?cat=care">Plant Care</a></li>
    </ul>
    <div class="nav-actions">
      <c:choose>
        <c:when test="${not empty sessionScope.user}">
          <a href="orders.jsp" class="btn btn-ghost btn-sm">My Orders</a>
          <a href="LogoutServlet" class="btn btn-outline btn-sm">Sign Out</a>
        </c:when>
        <c:otherwise>
          <a href="login.jsp" class="btn btn-outline btn-sm">Sign In</a>
          <a href="register.jsp" class="btn btn-primary btn-sm">Join</a>
        </c:otherwise>
      </c:choose>
      <a href="cart.jsp" class="cart-icon">
        🛒
        <span class="cart-count">${empty sessionScope.cartCount ? 0 : sessionScope.cartCount}</span>
      </a>
    </div>
  </div>
</nav>

<!-- ── HERO ── -->
<section class="hero">
  <div class="hero-inner">
    <div class="hero-content">
      <div class="hero-eyebrow">New Season Collection</div>
      <h1 class="hero-title">
        Bring <em>nature</em><br/>into your home
      </h1>
      <p class="hero-sub">
        Handpicked botanical plants, artisan pots, and organic care products — curated for modern living.
      </p>
      <div class="hero-actions">
        <a href="products.jsp" class="btn btn-primary btn-lg">Explore Collection</a>
        <a href="products.jsp?cat=new" class="btn btn-outline btn-lg">New Arrivals</a>
      </div>
    </div>
    <div class="hero-visual">
      <div class="hero-card hero-card-main">
        <div class="hero-img-placeholder">🌿</div>
        <div>
          <div class="product-category">Bestseller</div>
          <div class="product-name">Monstera Deliciosa</div>
        </div>
      </div>
      <div class="hero-card hero-card-small">
        <div class="stat-icon">🌱</div>
        <div class="hero-stat">240+</div>
        <div style="font-size:0.78rem;color:var(--muted);margin-top:4px;">Plant varieties</div>
      </div>
    </div>
  </div>
</section>

<!-- ── FEATURED PRODUCTS ── -->
<div class="section">
  <div class="section-header">
    <span class="section-tag">Curated for You</span>
    <h2 class="section-title">Featured <em>Plants</em></h2>
  </div>

  <div class="products-grid">
    <!-- Products loaded dynamically via Servlet; showing placeholders here -->
    <c:forEach var="product" items="${featuredProducts}">
      <div class="product-card">
        <div class="product-img">
          <c:choose>
            <c:when test="${not empty product.imageUrl}">
              <img src="${product.imageUrl}" alt="${product.name}"/>
            </c:when>
            <c:otherwise>🌿</c:otherwise>
          </c:choose>
          <c:if test="${product.stock < 5}">
            <span class="product-badge">Low Stock</span>
          </c:if>
        </div>
        <div class="product-info">
          <div class="product-category">${product.category}</div>
          <div class="product-name">${product.name}</div>
          <p style="font-size:0.82rem;color:var(--muted);margin-bottom:8px;">${product.description}</p>
          <div class="product-footer">
            <div class="product-price">₹${product.price}</div>
            <form action="CartServlet" method="post" style="display:inline">
              <input type="hidden" name="action" value="add"/>
              <input type="hidden" name="productId" value="${product.productId}"/>
              <button type="submit" class="add-to-cart-btn" title="Add to cart">+</button>
            </form>
          </div>
        </div>
      </div>
    </c:forEach>

    <!-- Static placeholders for demo / before Servlet runs -->
    <c:if test="${empty featuredProducts}">
      <c:forEach var="i" begin="1" end="4">
        <div class="product-card">
          <div class="product-img">🌿</div>
          <div class="product-info">
            <div class="product-category">Tropical Plant</div>
            <div class="product-name">Sample Plant ${i}</div>
            <p style="font-size:0.82rem;color:var(--muted);margin-bottom:8px;">A beautiful indoor plant for your home.</p>
            <div class="product-footer">
              <div class="product-price">₹599</div>
              <button class="add-to-cart-btn">+</button>
            </div>
          </div>
        </div>
      </c:forEach>
    </c:if>
  </div>

  <div style="text-align:center;margin-top:40px;">
    <a href="products.jsp" class="btn btn-outline btn-lg">View All Products →</a>
  </div>
</div>

<!-- ── WHY BLOOM ── -->
<div style="background:var(--parchment);padding:60px 0;margin-top:20px;">
  <div class="section" style="padding-top:0;padding-bottom:0;">
    <div style="display:grid;grid-template-columns:repeat(3,1fr);gap:40px;text-align:center;">
      <div>
        <div style="font-size:2.5rem;margin-bottom:14px;">🌱</div>
        <div style="font-family:var(--font-serif);font-size:1.3rem;color:var(--ink);margin-bottom:8px;">Sustainably Sourced</div>
        <div style="font-size:0.85rem;color:var(--muted);">Every plant is ethically grown and responsibly packaged.</div>
      </div>
      <div>
        <div style="font-size:2.5rem;margin-bottom:14px;">🚚</div>
        <div style="font-family:var(--font-serif);font-size:1.3rem;color:var(--ink);margin-bottom:8px;">Safe Delivery</div>
        <div style="font-size:0.85rem;color:var(--muted);">Plants delivered in 3–5 days in custom eco packaging.</div>
      </div>
      <div>
        <div style="font-size:2.5rem;margin-bottom:14px;">💳</div>
        <div style="font-family:var(--font-serif);font-size:1.3rem;color:var(--ink);margin-bottom:8px;">Secure Payments</div>
        <div style="font-size:0.85rem;color:var(--muted);">Powered by distributed RMI payment processing.</div>
      </div>
    </div>
  </div>
</div>

<!-- ── FOOTER ── -->
<footer class="footer">
  <div class="footer-inner">
    <div>
      <div class="footer-brand">Bl<span>oo</span>m</div>
      <p class="footer-desc">A curated botanical lifestyle store bringing nature into modern homes since 2024.</p>
    </div>
    <div>
      <div class="footer-heading">Shop</div>
      <ul class="footer-links">
        <li><a href="products.jsp?cat=plants">Plants</a></li>
        <li><a href="products.jsp?cat=pots">Pots & Planters</a></li>
        <li><a href="products.jsp?cat=care">Plant Care</a></li>
      </ul>
    </div>
    <div>
      <div class="footer-heading">Account</div>
      <ul class="footer-links">
        <li><a href="register.jsp">Register</a></li>
        <li><a href="login.jsp">Sign In</a></li>
        <li><a href="orders.jsp">My Orders</a></li>
      </ul>
    </div>
    <div>
      <div class="footer-heading">Info</div>
      <ul class="footer-links">
        <li><a href="#">About Us</a></li>
        <li><a href="#">Shipping Policy</a></li>
        <li><a href="#">Contact</a></li>
      </ul>
    </div>
  </div>
  <div class="footer-bottom">
    <span>© 2026 Bloom Store. Advanced Java Mini Project.</span>
    <span>JSP · Servlets · JavaBeans · Hibernate · RMI</span>
  </div>
</footer>

</body>
</html>
