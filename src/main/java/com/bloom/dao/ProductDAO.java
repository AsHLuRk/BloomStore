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
            return session.createQuery(
                "FROM Product ORDER BY createdAt DESC", Product.class).list();
        }
    }

    /** Get products by category */
    public List<Product> findByCategory(String category) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Product> q = session.createQuery(
                "FROM Product WHERE category = :cat ORDER BY name", Product.class);
            q.setParameter("cat", category);
            return q.list();
        }
    }

    /** Search by name or description */
    public List<Product> search(String keyword) {
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
            return session.createQuery(
                "FROM Product WHERE featured = true AND stock > 0", Product.class)
                .setMaxResults(8).list();
        }
    }

    /** Get latest products for the New Arrivals filter */
    public List<Product> findRecent(int limit) {
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
}
