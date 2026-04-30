package com.bloom.dao;

import com.bloom.model.Product;
import com.bloom.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

/**
 * ProductDAO — all DB operations for Product entity
 */
public class ProductDAO {

    /** Save a new product */
    public boolean save(Product product) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(product);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("ProductDAO.save error: " + e.getMessage());
            return false;
        }
    }

    /** Get all products */
    public List<Product> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Product> products = session.createQuery(
                "FROM Product ORDER BY createdAt DESC", Product.class).list();
            if (products.isEmpty()) {
                seedDefaultProducts();
                return session.createQuery(
                    "FROM Product ORDER BY createdAt DESC", Product.class).list();
            }
            return products;
        }
    }

    /** Get products by category */
    public List<Product> findByCategory(String category) {
        seedDefaultProducts();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Product> q = session.createQuery(
                "FROM Product WHERE category = :cat ORDER BY name", Product.class);
            q.setParameter("cat", category);
            return q.list();
        }
    }

    /** Search by name or description */
    public List<Product> search(String keyword) {
        seedDefaultProducts();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Product> q = session.createQuery(
                "FROM Product WHERE LOWER(name) LIKE :kw OR LOWER(description) LIKE :kw",
                Product.class);
            q.setParameter("kw", "%" + keyword.toLowerCase() + "%");
            return q.list();
        }
    }

    /** Get featured products for homepage */
    public List<Product> findFeatured() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Product> products = session.createQuery(
                "FROM Product WHERE featured = true AND stock > 0", Product.class)
                .setMaxResults(8).list();
            if (products.isEmpty()) {
                seedDefaultProducts();
                return session.createQuery(
                    "FROM Product WHERE featured = true AND stock > 0", Product.class)
                    .setMaxResults(8).list();
            }
            return products;
        }
    }

    /** Get latest products for the New Arrivals filter */
    public List<Product> findRecent(int limit) {
        seedDefaultProducts();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Product WHERE stock > 0 ORDER BY createdAt DESC", Product.class)
                .setMaxResults(limit).list();
        }
    }

    /** Find by ID */
    public Product findById(int productId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Product.class, productId);
        }
    }

    /** Update product (stock, price etc.) */
    public boolean update(Product product) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(product);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    /** Decrease stock after purchase */
    public boolean decreaseStock(int productId, int qty) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Product p = session.get(Product.class, productId);
            if (p == null || p.getStock() < qty) {
                tx.rollback();
                return false;
            }
            p.setStock(p.getStock() - qty);
            session.update(p);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    /** Delete product */
    public boolean delete(int productId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Product p = session.get(Product.class, productId);
            if (p != null) session.delete(p);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    /** Count total products */
    public long countAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return (Long) session.createQuery("SELECT COUNT(p) FROM Product p").uniqueResult();
        }
    }

    /** Get low-stock products (stock < 5) */
    public List<Product> findLowStock() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Product WHERE stock < 5 AND stock > 0 ORDER BY stock", Product.class).list();
        }
    }

    private void seedDefaultProducts() {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = (Long) session.createQuery("SELECT COUNT(p) FROM Product p").uniqueResult();
            if (count != null && count > 0) {
                return;
            }

            tx = session.beginTransaction();
            saveSeed(session, "Monstera Deliciosa", "The classic split-leaf plant. Easy to care for, stunning indoors.", 799.00, 30, "Plants", true);
            saveSeed(session, "Peace Lily", "Elegant white blooms. Thrives in low light. Air-purifying.", 549.00, 25, "Plants", true);
            saveSeed(session, "Snake Plant", "Near-indestructible. Perfect for beginners. Filters air at night.", 449.00, 40, "Plants", true);
            saveSeed(session, "Pothos Golden", "Trailing vines with golden-green leaves. Extremely low maintenance.", 299.00, 50, "Plants", true);
            saveSeed(session, "Areca Palm", "Feathery indoor palm that brightens living rooms and balconies.", 899.00, 18, "Plants", true);
            saveSeed(session, "Calathea Orbifolia", "Large striped leaves with a soft tropical look for shaded rooms.", 749.00, 14, "Plants", true);
            saveSeed(session, "Terracotta Classic Pot", "Handmade unglazed terracotta. Breathable for roots.", 349.00, 60, "Pots", true);
            saveSeed(session, "Hanging Macrame Planter", "Cotton macrame hanger with a compact planter for trailing vines.", 449.00, 32, "Pots", true);
            saveSeed(session, "Self-Watering Pot", "Modern planter with a water reservoir for easy plant care.", 649.00, 24, "Pots", false);
            saveSeed(session, "Organic Liquid Fertiliser", "All-purpose NPK blend. Safe for indoors. 500ml bottle.", 199.00, 80, "Care", true);
            saveSeed(session, "Neem Oil Spray", "Natural pest control. Cold-pressed neem. 250ml.", 149.00, 90, "Care", true);
            saveSeed(session, "Mini Gardening Tool Set", "Small rake, trowel, and pruning snip set for indoor gardening.", 399.00, 28, "Care", true);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("ProductDAO.seedDefaultProducts error: " + e.getMessage());
        }
    }

    private void saveSeed(Session session, String name, String description, double price,
                          int stock, String category, boolean featured) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);
        product.setFeatured(featured);
        session.save(product);
    }
}
