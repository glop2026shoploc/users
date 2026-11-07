-- Statut de validation d'un commerce par un administrateur
ALTER TABLE shop
    ADD COLUMN status varchar(20) NOT NULL DEFAULT 'WAITING'
        CONSTRAINT chk_shop_status CHECK (status IN ('WAITING', 'VALIDATED', 'REJECTED'));
