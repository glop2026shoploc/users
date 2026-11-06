-- Consentement RGPD à l'utilisation des données, obligatoire à l'inscription
ALTER TABLE users
    ADD COLUMN data_usage_consent boolean NOT NULL DEFAULT false;
