package org.icgc.argo.program_service.converters;

import com.google.protobuf.MessageOrBuilder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    mappingInheritanceStrategy =  MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG
)
public interface ConverterConfig {

//  @Mapping(target = "mergeFrom", ignore = true)
//  @Mapping(target = "clearField", ignore = true)
//  @Mapping(target = "clearOneof", ignore = true)
//  @Mapping(target = "unknownFields", ignore = true)
//  @Mapping(target = "mergeUnknownFields", ignore = true)
//  @Mapping(target = "allFields", ignore = true)
//  GeneratedMessageV3 configForProtoMessage(Object o);

//  @Mapping(target = "mergeFrom", ignore = true)
//  @Mapping(target = "clearField", ignore = true)
//  @Mapping(target = "clearOneof", ignore = true)
//  @Mapping(target = "mergeUnknownFields", ignore = true)
  @Mapping(target = "unknownFields", ignore = true)
  @Mapping(target = "allFields", ignore = true)
  MessageOrBuilder configForProtoMessageOrBuilder(Object o);
//  GeneratedMessageV3 configForProtoMessageOrBuilder(Object o);
  //  MessageOrBuilder configForProtoMessageOrBuilder(Object o);



}
