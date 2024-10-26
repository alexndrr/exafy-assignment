INSERT INTO task_priority(name) VALUES
    ('Low'),
    ('Medium'),
    ('High');

INSERT INTO task_status(name, triggers_notifications) VALUES
    ('Pending', false),
    ('In Progress', true),
    ('Completed', false);

INSERT INTO task_category(name) VALUES
    ('Work'),
    ('Personal'),
    ('Others');

INSERT INTO app_user(email) VALUES
    ('aleksandar.developer@protonmail.com'),
    ('test1@test.com');

INSERT INTO task(title, description, due_date, task_category_id, task_priority_id, task_status_id, app_user_id) VALUES
    ('Title 1', '', '2024-10-25T12:00:00.000Z', 1, 1, 2, 1),
    ('Title 2', '', '2024-10-25T11:00:00.000Z', 2, 2, 2, 1),
    ('Title 3', '', '2024-10-26T09:00:00.000Z', 3, 3, 2, 1),
    ('Title 4', '', '2024-10-26T09:00:00.000Z', 3, 3, 2, 2),
    ('Title 5', 'Description about the task', '2024-11-10T09:00:00.000Z', 3, 1, 2, 1),
    ('Title 6', 'This task is important', '2024-11-10T09:00:00.000Z', 3, 3, 2, 1);