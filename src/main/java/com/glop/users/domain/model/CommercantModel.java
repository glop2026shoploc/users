package com.glop.users.domain.model;

import lombok.Data;

@Data
public class CommercantModel {

    String raisonSociale;
    String categorie;
    String siret;
    String adressePostale;
    String email;
    String password;
    String encodedPassword;
    CommercantStatus statut;
}
