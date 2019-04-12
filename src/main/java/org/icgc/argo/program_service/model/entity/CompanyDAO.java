package org.icgc.argo.program_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDAO {
  private int id;
  private String name;
  private Language language;
  private Set<EmployeeDao> employees;
}
