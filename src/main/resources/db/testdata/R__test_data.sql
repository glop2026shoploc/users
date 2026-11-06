-- =========================================================
--  Données de test — chargées UNIQUEMENT avec le profil "local"
--  (spring.flyway.locations dans application-local.yml).
--  Script répétable : Flyway le rejoue quand son contenu change,
--  ON CONFLICT DO NOTHING évite les doublons.
--  Ids fixes pour pouvoir les utiliser directement dans Postman / le front.
-- =========================================================

INSERT INTO country (country_id, name)
VALUES ('00000000-0000-0000-0000-000000000001', 'France')
ON CONFLICT DO NOTHING;

INSERT INTO city (city_id, name, zip_code, country_id)
VALUES ('00000000-0000-0000-0000-000000000101', 'Lille', '59000', '00000000-0000-0000-0000-000000000001')
ON CONFLICT DO NOTHING;

INSERT INTO category (category_id, name)
VALUES ('00000000-0000-0000-0000-000000000201', 'Boulangerie')
ON CONFLICT DO NOTHING;
