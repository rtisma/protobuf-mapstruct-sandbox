package com.roberttisma.sandbox.protobuf_mapstruct.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDao {
  private int id;
  private String firstName;
  private String lastName;
  private boolean male;
  private Boolean isCool;
}
