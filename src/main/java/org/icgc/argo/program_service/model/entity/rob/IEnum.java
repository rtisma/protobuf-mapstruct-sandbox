package org.icgc.argo.program_service.model.entity.rob;

public interface IEnum {
  boolean isUndefined();
  boolean isUnrecognized();

  default boolean isDefined(){
    return !isUndefined();
  }

}
