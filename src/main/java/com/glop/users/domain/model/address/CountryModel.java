package com.glop.users.domain.model.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CountryModel {
    UUID id;
    String name;
}
