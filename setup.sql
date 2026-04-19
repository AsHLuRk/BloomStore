-- ═══════════════════════════════════════════════════════════
-- BLOOM STORE — MySQL Setup Script
-- Run this ONCE before starting the app.
-- ═══════════════════════════════════════════════════════════

-- 1. Create and select database
CREATE DATABASE IF NOT EXISTS bloom_store;
USE bloom_store;

-- 2. Tables are auto-created by Hibernate (hbm2ddl.auto=update)
--    Run this file just for seed data after first startup.

-- ── SEED: Users ─────────────────────────────────────────────
-- Password for all seed users = "bloom123" (SHA-256 hashed)
INSERT IGNORE INTO users (first_name, last_name, email, password, role) VALUES
('Admin', 'Bloom',  'admin@bloom.com',  'SHA256_HASH_OF_bloom123', 'ADMIN'),
('Arjun', 'Sharma', 'arjun@example.com','SHA256_HASH_OF_bloom123', 'USER'),
('Priya', 'Gupta',  'priya@example.com','SHA256_HASH_OF_bloom123', 'USER');

-- ── SEED: Products ──────────────────────────────────────────
INSERT IGNORE INTO products (name, description, price, stock, category, is_featured) VALUES
-- Plants
('Monstera Deliciosa',   'The classic split-leaf plant. Easy to care for, stunning indoors.', 799.00, 30, 'Plants', 1),
('Peace Lily',           'Elegant white blooms. Thrives in low light. Air-purifying.',         549.00, 25, 'Plants', 1),
('Snake Plant',          'Near-indestructible. Perfect for beginners. Filters air at night.',   449.00, 40, 'Plants', 1),
('Pothos Golden',        'Trailing vines with golden-green leaves. Extremely low maintenance.',  299.00, 50, 'Plants', 1),
('Rubber Plant',         'Bold, glossy dark leaves. A statement piece for any room.',           699.00, 20, 'Plants', 0),
('Fiddle Leaf Fig',      'Dramatic, architectural leaves loved by interior designers.',         999.00,  8, 'Plants', 1),
('ZZ Plant',             'Glossy leaves, drought-tolerant. Virtually indestructible.',          599.00, 35, 'Plants', 0),
('Bird of Paradise',     'Tropical showstopper with enormous paddle-shaped leaves.',           1299.00,  5, 'Plants', 0),

-- Pots
('Terracotta Classic Pot',  'Handmade unglazed terracotta. Breathable for roots.',             349.00, 60, 'Pots', 1),
('Ceramic Matte White',     'Minimal matte white glaze. Drainage hole included.',              499.00, 45, 'Pots', 0),
('Woven Rattan Basket',     'Natural rattan basket planter. Fits 6-inch pots.',                399.00, 30, 'Pots', 1),
('Cement Cylinder Pot',     'Industrial-style concrete finish. Heavy and sturdy.',             599.00, 20, 'Pots', 0),

-- Care
('Organic Liquid Fertiliser', 'All-purpose NPK blend. Safe for indoors. 500ml bottle.',        199.00, 80, 'Care', 1),
('Well-Draining Potting Mix', 'Perlite-enriched soil. Excellent drainage for tropicals.',      299.00, 60, 'Care', 0),
('Neem Oil Spray',            'Natural pest control. Cold-pressed neem. 250ml.',               149.00, 90, 'Care', 1),
('Pebble Tray Set',           'Humidity tray with decorative pebbles. Set of 2.',              249.00, 40, 'Care', 0);

-- ── VERIFICATION ────────────────────────────────────────────
SELECT 'Users:'    AS '', COUNT(*) FROM users;
SELECT 'Products:' AS '', COUNT(*) FROM products;
