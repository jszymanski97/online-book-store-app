DELETE FROM cart_items;
DELETE FROM shopping_carts;
DELETE FROM users;
DELETE FROM books_categories;
DELETE FROM books;
DELETE FROM categories;

ALTER TABLE users ALTER COLUMN id RESTART WITH 100;
ALTER TABLE shopping_carts ALTER COLUMN id RESTART WITH 100;
ALTER TABLE cart_items ALTER COLUMN id RESTART WITH 100;
ALTER TABLE books ALTER COLUMN id RESTART WITH 100;
ALTER TABLE categories ALTER COLUMN id RESTART WITH 100;

INSERT INTO users (id, email, password, first_name, last_name, shipping_address) VALUES
                                                                                     (1, 'user1@example.com', 'password123', 'John', 'Doe', '123 Main St'),
                                                                                     (2, 'user2@example.com', 'password123', 'Jane', 'Smith', '456 Elm St'),
                                                                                     (3, 'user3@example.com', 'password123', 'Alice', 'Johnson', '789 Oak St');

INSERT INTO categories (id, name, description, is_deleted) VALUES
                                                               (1, 'Fiction', 'Fictional books', false),
                                                               (2, 'Fantasy', 'Fantasy books', false),
                                                               (3, 'History', 'Historical books', false);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted) VALUES
                                                                                             (1, 'The Hobbit', 'J.R.R. Tolkien', '9781234567890', 10.99, 'A fantasy adventure.', 'hobbit.jpg', false),
                                                                                             (2, 'War and Peace', 'Leo Tolstoy', '9781234567891', 20.99, 'Historical epic.', 'war_peace.jpg', false),
                                                                                             (3, 'The Great Gatsby', 'F. Scott Fitzgerald', '9781234567892', 10.99, 'Classic American novel.', 'gatsby.jpg', false);

INSERT INTO books_categories (books_id, categories_id) VALUES
                                                           (1, 2),
                                                           (2, 3),
                                                           (3, 1),
                                                           (3, 2);

INSERT INTO shopping_carts (id, user_id) VALUES
                                             (1, 1),
                                             (2, 2),
                                             (3, 3);

INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity) VALUES
                                                                     (1, 1, 1, 2),
                                                                     (2, 1, 2, 1),
                                                                     (3, 2, 3, 1),
                                                                     (4, 2, 1, 1),
                                                                     (5, 3, 2, 1);
