INSERT INTO users (name, username, email, role_type, password, created_at, updated_at)
VALUES (
    '${ADMIN_NAME}',
    '${ADMIN_USERNAME}',
    '${ADMIN_EMAIL}',
    'a',
    '${ADMIN_PASSWORD}',
    NOW(),
    NOW()
);