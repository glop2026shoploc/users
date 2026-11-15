-- Schéma cible de la base users.
-- Les magasins (shop, address, category, opening_hours) et les produits sont dans la base catalogue.

-- =========================================================
--  Types
-- =========================================================
CREATE TYPE employee_role AS ENUM ('OWNER', 'MANAGER', 'EMPLOYEE');

-- =========================================================
--  Géographie
-- =========================================================
CREATE TABLE country
(
    country_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name       varchar(100) NOT NULL UNIQUE
);

CREATE TABLE city
(
    city_id    uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name       varchar(150) NOT NULL,
    zip_code   varchar(10)  NOT NULL,
    country_id uuid         NOT NULL REFERENCES country (country_id),
    UNIQUE (country_id, zip_code, name)
);
CREATE INDEX idx_city_country ON city (country_id);

-- =========================================================
--  Utilisateurs & authentification
-- =========================================================
CREATE TABLE users
(
    user_id            uuid PRIMARY KEY      DEFAULT gen_random_uuid(),
    email              varchar(255) NOT NULL,
    first_name         varchar(100) NOT NULL,
    last_name          varchar(100) NOT NULL,
    created_at         timestamptz  NOT NULL DEFAULT now(),
    data_usage_consent boolean      NOT NULL DEFAULT false
);
-- unicité insensible à la casse
CREATE UNIQUE INDEX uq_users_email ON users (lower(email));

CREATE TABLE credentials
(
    credential_id uuid PRIMARY KEY      DEFAULT gen_random_uuid(),
    user_id       uuid         NOT NULL UNIQUE -- 1–1
        REFERENCES users (user_id) ON DELETE CASCADE,
    password      varchar(255) NOT NULL,
    updated_at    timestamptz  NOT NULL DEFAULT now()
);

-- Spécialisations de users (overlapping : un user peut être admin, client et employé)
CREATE TABLE admin
(
    admin_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id  uuid NOT NULL UNIQUE                    -- 1–1 avec users
        REFERENCES users (user_id) ON DELETE CASCADE,
    city_id  uuid NOT NULL REFERENCES city (city_id) -- n–1 avec city
);
CREATE INDEX idx_admin_city ON admin (city_id);

CREATE TABLE client
(
    client_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id   uuid NOT NULL UNIQUE -- 1–1 avec users
        REFERENCES users (user_id) ON DELETE CASCADE,
    city_id   uuid REFERENCES city (city_id) -- n–1 avec city
);

CREATE TABLE employee
(
    employee_id uuid PRIMARY KEY       DEFAULT gen_random_uuid(),
    user_id     uuid          NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    shop_id     uuid          NOT NULL, -- magasin du service catalogue (autre base : pas de FK)
    role        employee_role NOT NULL DEFAULT 'EMPLOYEE',
    UNIQUE (user_id)
);
CREATE INDEX idx_employee_shop ON employee (shop_id);
