package org.icgc.argo.program_service.converters;

import lombok.val;
import org.icgc.argo.program_service.Date;
import org.icgc.argo.program_service.MembershipType;
import org.icgc.argo.program_service.Program;
import org.icgc.argo.program_service.model.entity.Program2;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.UUID;

@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    unmappedTargetPolicy = ReportingPolicy.WARN)
public interface Conv {
  Conv COV = Mappers.getMapper(Conv.class);

  Program convertToProtoProgram(org.icgc.argo.program_service.model.entity.Program programDao);

  Program convertToProtoProgram(org.icgc.argo.program_service.model.entity.Program2 programDao);

  @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.NULL)
  Program2.MembershipType2 convertToMembershipType2(MembershipType membershipType);
  org.icgc.argo.program_service.model.entity.Program2  convertToDaoProgram(Program programProto);


  default String convertToString(UUID id){
    return id.toString();
  }

  default UUID convertToUUID(String id){
    return UUID.fromString(id);
  }

  default Instant convertToInstant(java.util.Date date){
    return date.toInstant();
  }

  default LocalDate convertToLocalDate(Date date){
    return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
  }

  default java.util.Date convertToJavaDate(LocalDate localDate){
    return java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  default Date convertToProtoDate(java.util.Date date){
    val i = date.toInstant();
    val month = i.get(ChronoField.MONTH_OF_YEAR);
    val day = i.get(ChronoField.DAY_OF_MONTH);
    val year = i.get(ChronoField.YEAR);
    return Date.newBuilder()
        .setMonth(month)
        .setDay(day)
        .setYear(year)
        .build();
  }

  default Program.Builder createProgramProto(){
    return Program.newBuilder();
  }

}
