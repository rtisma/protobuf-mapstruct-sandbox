package org.icgc.argo.program_service.model.entity.rob;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RobDAO {
  private int id;
  private String name;
  private RobEnumDao robEnum;

}
