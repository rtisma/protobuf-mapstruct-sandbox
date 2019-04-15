package com.roberttisma.sandbox.protobuf_mapstruct.converters;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import com.roberttisma.sandbox.protobuf_mapstruct.company_service.ProtoCompanyComposite;
import com.roberttisma.sandbox.protobuf_mapstruct.company_service.ProtoEmployeeComposite;
import com.roberttisma.sandbox.protobuf_mapstruct.model.entity.CompanyDAO;
import com.roberttisma.sandbox.protobuf_mapstruct.model.entity.EmployeeDao;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper( config = ConverterConfig.class, uses = {LanguageConverter.class})
public interface CompositeConverter {

  /**
   * Employee conversions
   */
  @Mapping(source = "isMale", target = "male")
  EmployeeDao convertToEmployeeDao(ProtoEmployeeComposite employeeComposite);

  // Ignore there proto fields
  // TODO [rtisma]: Look into making a CustomSPI to automatically ignore merge, clear for each proto field
  @Mapping(target = "mergeFrom", ignore = true)
  @Mapping(target = "clearField", ignore = true)
  @Mapping(target = "clearOneof", ignore = true)
  @Mapping(target = "unknownFields", ignore = true)
  @Mapping(target = "mergeUnknownFields", ignore = true)
  @Mapping(target = "allFields", ignore = true)
	@Mapping(target = "mergeId", ignore = true)
	@Mapping(target = "mergeFirstName", ignore = true)
	@Mapping(target = "mergeLastName", ignore = true)
	@Mapping(target = "mergeIsMale", ignore = true)
  @Mapping(target = "mergeIsCool", ignore = true)
  @InheritInverseConfiguration //This will take the Mapping of isMale from the inverse converter
  ProtoEmployeeComposite convertToProtoEmployee(EmployeeDao employeeDao);

  /**
   * Company conversions
   */

  // TODO [rtisma]: Refer to custom SPI for protobufs -->
  //  https://github.com/mapstruct/mapstruct-examples/tree/master/mapstruct-protobuf3/spi-impl
  @Mapping(source = "employeesList", target = "employees")
  CompanyDAO convertToCompanyDao(ProtoCompanyComposite protoCompany);

	@Mapping(target = "mergeFrom", ignore = true)
	@Mapping(target = "clearField", ignore = true)
	@Mapping(target = "clearOneof", ignore = true)
	@Mapping(target = "mergeId", ignore = true)
	@Mapping(target = "mergeName", ignore = true)
	@Mapping(target = "mergeLanguage", ignore = true)
	@Mapping(target = "removeEmployees", ignore = true)
	@Mapping(target = "mergeUnknownFields", ignore = true)
	@Mapping(target = "employeesList", ignore = true)
	@Mapping(target = "employeesOrBuilderList", ignore = true)
	@Mapping(target = "employeesBuilderList", ignore = true)
  ProtoCompanyComposite convertToProtoCompany(CompanyDAO companyDAO);

  /**
   * Common to All protos
   */
  default String convertToString(StringValue v){
    return v.getValue();
  }

  default int convertToInt(Int32Value v){
    return v.getValue();
  }

  default boolean convertToBoolean(BoolValue v){
    return v.getValue();
  }

  default StringValue convertToStringValue(String v){
    return StringValue.of(v);
  }

  default Int32Value convertToInt32Value(int v){
    return Int32Value.of(v);
  }

  default BoolValue convertToBoolValue(boolean v){
    return BoolValue.of(v);
  }

}
