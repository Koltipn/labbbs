INSERT INTO category (name) VALUES ('Dogs'), ('Cats'), ('Birds');

INSERT INTO pet (name, category_id, status) VALUES
    ('Rex', 1, 'AVAILABLE'),
    ('Whiskers', 2, 'PENDING'),
    ('Tweety', 3, 'SOLD');

INSERT INTO tag (name, pet_id) VALUES
    ('Friendly', 1),
    ('Playful', 1),
    ('Lazy', 2),
    ('Sings', 3);