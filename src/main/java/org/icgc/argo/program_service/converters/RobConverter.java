package org.icgc.argo.program_service.converters;

import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import org.icgc.argo.program_service.model.entity.rob.RobDAO;
import org.icgc.argo.program_service.model.entity.rob.RobEnumDao;
import org.icgc.argo.rob_service.RobCompositeMessage;
import org.icgc.argo.rob_service.RobEnum;
import org.icgc.argo.rob_service.RobEnumValue;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.MappingConstants.ANY_REMAINING;
import static org.mapstruct.MappingConstants.NULL;

@Mapper(
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RobConverter {
  RobConverter INSTANCE = Mappers.getMapper(RobConverter.class);

  /**
   * RobCompositeMessage
   */
  //TODO [rtisma]: Look into making a CustomSPI to automatically ignore merge, clear for each field
  @Mapping(target = "mergeFrom", ignore = true)
  @Mapping(target = "clearField", ignore = true)
  @Mapping(target = "clearOneof", ignore = true)
  @Mapping(target = "mergeId", ignore = true)
  @Mapping(target = "mergeName", ignore = true)
  @Mapping(target = "mergeRobEnum", ignore = true)
  @Mapping(target = "unknownFields", ignore = true)
  @Mapping(target = "mergeUnknownFields", ignore = true)
  @Mapping(target = "allFields", ignore = true)
  RobCompositeMessage convertToCompositeMessage(RobDAO robDAO);

  @InheritInverseConfiguration //This will take the Mapping of isMale from the inverse converter
  RobDAO convertToDAO(RobCompositeMessage robCompositeMessageProto);


  /**
   * RobEnum
   */
  @ValueMapping( source = ANY_REMAINING, target = NULL)
  RobEnumDao convertToEnumDao(RobEnum robEnumProto);

  @ValueMapping(source = NULL, target = "UNDEFINED")
  RobEnum convertToEnumProto(RobEnumDao robEnumDao);

  default RobEnum convertToRobEnum(RobEnumValue v){
    return v.getValue();
  }

  default RobEnumValue convertToRobEnumValue(RobEnum robEnumProto){
    return RobEnumValue.newBuilder().setValue(robEnumProto).build();
  }



  /**
   * Common to All protos
   */

  default String convertToString(StringValue v){
    return v.getValue();
  }

  default int convertToInt(Int32Value v){
    return v.getValue();
  }

  default StringValue convertToStringValue(String v){
    return StringValue.of(v);
  }

  default Int32Value convertToInt32Value(int v){
    return Int32Value.of(v);
  }
}
