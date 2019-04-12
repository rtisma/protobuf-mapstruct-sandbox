package com.roberttisma.sandbox.protobuf_mapstruct.converters;

import com.roberttisma.sandbox.protobuf_mapstruct.company_service.ProtoCompanyOO;
import com.roberttisma.sandbox.protobuf_mapstruct.company_service.ProtoEmployeeOO;
import com.roberttisma.sandbox.protobuf_mapstruct.model.entity.CompanyDAO;
import com.roberttisma.sandbox.protobuf_mapstruct.model.entity.EmployeeDao;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Currently no easy way of converting OneOf values while using the default presence checking strategies.
 * If the presence is not checked properly, the field will be set via its setter, and therefore setting the OneOf flag for that field, even if its the default value.
 * Cannot use
 *
 */
@Mapper(config = ConverterConfig.class, uses = {LanguageConverter.class})
public interface OOConverter {

  /**
   * Employee conversions
   */
  @Mapping(source = "isMale", target = "male")
  EmployeeDao convertToEmployeeDao(ProtoEmployeeOO protoEmployee);

  @Mapping(target = "mergeFrom", ignore = true)
  @Mapping(target = "clearField", ignore = true)
  @Mapping(target = "clearOneof", ignore = true)
  @Mapping(target = "mergeUnknownFields", ignore = true)
  @Mapping(target = "firstNameBytes", ignore = true)
  @Mapping(target = "lastNameBytes", ignore = true)
  @InheritInverseConfiguration
  ProtoEmployeeOO convertToProtoEmployee(EmployeeDao employeeDao);

  @Mapping(source = "employeesList", target = "employees")
  CompanyDAO convertToCompanyDao(ProtoCompanyOO companyOO);

  // TODO [rtisma]: Using Custom SPI, create a custom null checking strategy for OneOf fields
  default boolean isUndefined(ProtoEmployeeOO protoEmployeeOO){
     return protoEmployeeOO.getOptionalIdCase() ==
         ProtoEmployeeOO.OptionalIdCase.OPTIONALID_NOT_SET;
  }

  /**
   * Company conversions
   */


}
