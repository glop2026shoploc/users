-- =========================================================
--  Données de test — chargées UNIQUEMENT avec le profil "local"
--  (spring.flyway.locations dans application-local.yml).
--  Script répétable : Flyway le rejoue quand son contenu change,
--  ON CONFLICT DO NOTHING évite les doublons.
--  Ids fixes pour pouvoir les utiliser directement dans Postman / le front.
-- =========================================================

INSERT INTO country (country_id, name)
VALUES ('00000000-0000-0000-0000-000000000001', 'Pays Imaginaire')
ON CONFLICT DO NOTHING;

INSERT INTO city (city_id, name, zip_code, country_id)
VALUES ('00000000-0000-0000-0000-000000000101', 'Ville Imaginaire', '99250', '00000000-0000-0000-0000-000000000001')
ON CONFLICT DO NOTHING;

-- ---------------------------------------------------------
--  Lille : un client et trois commerçants (magasins dans les données de test de catalogue,
--  mêmes shop_id). Mot de passe de tous ces comptes : Motdepasse1!
--    client@lille.fr       client de Lille
--    meert@test.fr         Meert
--    nottinghill@test.fr   Notting Hill Coffee
--    furet@test.fr         Le Furet du Nord
-- ---------------------------------------------------------

INSERT INTO country (country_id, name)
VALUES ('00000000-0000-0000-0000-000000000002', 'France')
ON CONFLICT DO NOTHING;

INSERT INTO city (city_id, name, zip_code, country_id)
VALUES ('00000000-0000-0000-0000-000000000102', 'Lille', '59000', '00000000-0000-0000-0000-000000000002')
ON CONFLICT DO NOTHING;

INSERT INTO users (user_id, email, first_name, last_name, data_usage_consent)
VALUES ('00000000-0000-0000-0000-000000000501', 'client@lille.fr', 'Julie', 'Lille', true),
       ('00000000-0000-0000-0000-000000000502', 'meert@test.fr', 'Meert', 'Commerçant', true),
       ('00000000-0000-0000-0000-000000000503', 'nottinghill@test.fr', 'Notting', 'Commerçant', true),
       ('00000000-0000-0000-0000-000000000504', 'furet@test.fr', 'Furet', 'Commerçant', true)
ON CONFLICT DO NOTHING;

-- Hash BCrypt de "Motdepasse1!" (même encodeur que l'application)
INSERT INTO credentials (credential_id, user_id, password)
VALUES ('00000000-0000-0000-0000-000000000801', '00000000-0000-0000-0000-000000000501', '$2a$10$U07sEl5M880II7HnvtGkXuK7CBpRayzkC1hzFjRh70mBeR8Xo8chy'),
       ('00000000-0000-0000-0000-000000000802', '00000000-0000-0000-0000-000000000502', '$2a$10$U07sEl5M880II7HnvtGkXuK7CBpRayzkC1hzFjRh70mBeR8Xo8chy'),
       ('00000000-0000-0000-0000-000000000803', '00000000-0000-0000-0000-000000000503', '$2a$10$U07sEl5M880II7HnvtGkXuK7CBpRayzkC1hzFjRh70mBeR8Xo8chy'),
       ('00000000-0000-0000-0000-000000000804', '00000000-0000-0000-0000-000000000504', '$2a$10$U07sEl5M880II7HnvtGkXuK7CBpRayzkC1hzFjRh70mBeR8Xo8chy')
ON CONFLICT DO NOTHING;

INSERT INTO client (client_id, user_id, city_id)
VALUES ('00000000-0000-0000-0000-000000000601', '00000000-0000-0000-0000-000000000501', '00000000-0000-0000-0000-000000000102')
ON CONFLICT DO NOTHING;

-- Propriétaires des magasins de Lille (shop_id = magasins des données de test de catalogue)
INSERT INTO employee (employee_id, user_id, shop_id, role)
VALUES ('00000000-0000-0000-0000-000000000701', '00000000-0000-0000-0000-000000000502', '00000000-0000-0000-0000-000000000411', 'OWNER'),
       ('00000000-0000-0000-0000-000000000702', '00000000-0000-0000-0000-000000000503', '00000000-0000-0000-0000-000000000412', 'OWNER'),
       ('00000000-0000-0000-0000-000000000703', '00000000-0000-0000-0000-000000000504', '00000000-0000-0000-0000-000000000413', 'OWNER')
ON CONFLICT DO NOTHING;
