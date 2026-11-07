DROP TABLE IF EXISTS user_roles, user_credentials, role, shopkeepers, users CASCADE;

-- =========================================================
--  Types
-- =========================================================
CREATE TYPE employee_role AS ENUM ('OWNER', 'MANAGER', 'EMPLOYEE');

-- =========================================================
--  Géographie
-- =========================================================
CREATE TABLE country (
                         country_id  uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                         name        varchar(100) NOT NULL UNIQUE
);

CREATE TABLE city (
                      city_id     uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                      name        varchar(150) NOT NULL,
                      zip_code    varchar(10)  NOT NULL,
                      country_id  uuid NOT NULL REFERENCES country (country_id),
                      UNIQUE (country_id, zip_code, name)
);
CREATE INDEX idx_city_country ON city (country_id);

CREATE TABLE address (
                         address_id  uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                         road        varchar(255) NOT NULL,
                         number      varchar(20),            -- texte : "12 bis", "3B"...
                         complement  varchar(255),
                         city_id     uuid NOT NULL REFERENCES city (city_id)
);
CREATE INDEX idx_address_city ON address (city_id);

-- =========================================================
--  Utilisateurs & authentification
-- =========================================================
CREATE TABLE users (
                       user_id     uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                       email       varchar(255) NOT NULL,
                       first_name  varchar(100) NOT NULL,
                       last_name   varchar(100) NOT NULL,
                       created_at  timestamptz  NOT NULL DEFAULT now()
);
-- unicité insensible à la casse
CREATE UNIQUE INDEX uq_users_email ON users (lower(email));

CREATE TABLE credentials (
                             credential_id  uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                             user_id        uuid NOT NULL UNIQUE               -- 1–1
                                 REFERENCES users (user_id) ON DELETE CASCADE,
                             password       varchar(255) NOT NULL,             -- hash bcrypt/argon2, jamais en clair
                             updated_at     timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE admin (
                       admin_id  uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                       user_id   uuid NOT NULL UNIQUE                    -- 1–1 avec users
                           REFERENCES users (user_id) ON DELETE CASCADE,
                       city_id   uuid NOT NULL REFERENCES city (city_id) -- n–1 avec city
);
CREATE INDEX idx_admin_city ON admin (city_id);

CREATE TABLE client (
                        client_id   uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                        user_id     uuid NOT NULL UNIQUE                  -- 1–1 avec users
                            REFERENCES users (user_id) ON DELETE CASCADE,
                        address_id  uuid NOT NULL UNIQUE                  -- 1–1 avec address
                            REFERENCES address (address_id)
);

-- =========================================================
--  Magasins
-- =========================================================
CREATE TABLE category (
                          category_id  uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                          name         varchar(100) NOT NULL UNIQUE
);

CREATE TABLE shop (
                      shop_id       uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                      name          varchar(150) NOT NULL,
                      siret_number  char(14) NOT NULL UNIQUE
                          CHECK (siret_number ~ '^[0-9]{14}$'),
    category_id   uuid REFERENCES category (category_id)          -- 0,1
                  ON DELETE SET NULL,
    address_id    uuid NOT NULL UNIQUE                           -- 1–1
                  REFERENCES address (address_id)
);
CREATE INDEX idx_shop_category ON shop (category_id);

CREATE TABLE employee (
                          employee_id  uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                          user_id      uuid NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
                          shop_id      uuid NOT NULL REFERENCES shop  (shop_id) ON DELETE CASCADE,
                          role         employee_role NOT NULL DEFAULT 'EMPLOYEE',
                          UNIQUE (user_id)            -- 1–1 users/employee comme sur ton schéma
    -- pour autoriser plusieurs magasins par user, remplacer par :
    -- UNIQUE (user_id, shop_id)
);
CREATE INDEX idx_employee_shop ON employee (shop_id);

CREATE TABLE opening_hours (
                               opening_hours_id  uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                               shop_id           uuid NOT NULL REFERENCES shop (shop_id) ON DELETE CASCADE,
                               day_of_week       smallint NOT NULL CHECK (day_of_week BETWEEN 1 AND 7), -- 1 = lundi (ISO)
                               open_time         time NOT NULL,
                               close_time        time NOT NULL,
                               CHECK (close_time > open_time),
                               UNIQUE (shop_id, day_of_week, open_time)
);
CREATE INDEX idx_opening_hours_shop ON opening_hours (shop_id);