-- Ajout de la colonne
ALTER TABLE client
    ADD COLUMN city_id uuid NULL;

ALTER TABLE client ADD CONSTRAINT fk_city FOREIGN KEY (city_id) REFERENCES city(city_id);

-- migration de la colonne
UPDATE client as c set city_id = a.city_id
FROM address as a
WHERE a.address_id = c.address_id;

-- suppression de l'ancienne colonne
ALTER TABLE client
    DROP COLUMN address_id;

