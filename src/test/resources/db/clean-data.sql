DELETE FROM books_categories;
DELETE FROM cart_items;
DELETE FROM books;
DELETE FROM categories;
ALTER TABLE books ALTER COLUMN id RESTART WITH 1;
