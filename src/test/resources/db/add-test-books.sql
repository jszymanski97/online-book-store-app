DELETE FROM books_categories;
DELETE FROM books;
DELETE FROM categories;

ALTER TABLE books ALTER COLUMN id RESTART WITH 100;
ALTER TABLE categories ALTER COLUMN id RESTART WITH 100;

INSERT INTO categories (id, name, description, is_deleted) VALUES
    (1, 'Fiction', 'Fictional books', '0'),
    (2, 'Fantasy', 'Fantasy books', '0'),
    (3, 'History', 'Historical books', '0');

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted) VALUES
    (1, 'The Hobbit', 'J.R.R. Tolkien', '9781234567890', 10.99, 'A fantasy adventure.', 'hobbit.jpg', false),
    (2, 'War and Peace', 'Leo Tolstoy', '9781234567891', 20.99, 'Historical epic.', 'war_peace.jpg', false),
    (3, 'The Great Gatsby', 'F. Scott Fitzgerald', '9781234567892', 10.99, 'Classic American novel.', 'gatsby.jpg', false);

INSERT INTO books_categories (books_id, categories_id) VALUES
    (1, 2),
    (2, 3),
    (3, 1),
    (3, 2);
