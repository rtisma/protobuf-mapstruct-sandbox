package org.icgc.argo.program_service.converters;

import org.icgc.argo.company_service.ProtoCompanyRegular;
import org.icgc.argo.company_service.ProtoEmployeeRegular;
import org.icgc.argo.program_service.model.entity.CompanyDAO;
import org.icgc.argo.program_service.model.entity.EmployeeDao;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper( config = ConverterConfig.class, uses = {LanguageConverter.class})
public interface RegularConverter {

  /**
   * Employee conversions
   */
  @Mapping(source = "isMale", target = "male")
  EmployeeDao convertToEmployeeDao(ProtoEmployeeRegular protoEmployee);

  // Ignore there proto fields
  // TODO [rtisma]: Look into making a CustomSPI to automatically ignore merge, clear for each proto field
  @Mapping(target = "mergeFrom", ignore = true)
  @Mapping(target = "clearField", ignore = true)
  @Mapping(target = "clearOneof", ignore = true)
  @Mapping(target = "unknownFields", ignore = true)
  @Mapping(target = "mergeUnknownFields", ignore = true)
  @Mapping(target = "allFields", ignore = true)
  @Mapping(target = "firstNameBytes", ignore = true)
  @Mapping(target = "lastNameBytes", ignore = true)
  @InheritInverseConfiguration //This will take the Mapping of isMale from the inverse converter
  ProtoEmployeeRegular convertToProtoEmployee(EmployeeDao employeeDao);

  Set<EmployeeDao> convertToSetOfEmployeeDao(List<ProtoEmployeeRegular> protoEmployees);

  /**
   * Company conversions
   */

  // TODO [rtisma]: Refer to custom SPI for protobufs -->
  //  https://github.com/mapstruct/mapstruct-examples/tree/master/mapstruct-protobuf3/spi-impl
  @Mapping(source = "employeesList", target = "employees")
  CompanyDAO convertToCompanyDao(ProtoCompanyRegular protoCompany);

  // Inherited from ProtobufConverterConfig.class
//  @Mapping(target = "unknownFields", ignore = true)
//  @Mapping(target = "allFields", ignore = true)

  // Mappings specific to protobuf messages
  @Mapping(target = "mergeFrom", ignore = true)
  @Mapping(target = "clearField", ignore = true)
  @Mapping(target = "clearOneof", ignore = true)
  @Mapping(target = "mergeUnknownFields", ignore = true)
	@Mapping(target = "nameBytes", ignore = true)
	@Mapping(target = "languageValue", ignore = true)
	@Mapping(target = "removeEmployees", ignore = true)
	@Mapping(target = "employeesList", ignore = true)
	@Mapping(target = "employeesOrBuilderList", ignore = true)
	@Mapping(target = "employeesBuilderList", ignore = true)
  ProtoCompanyRegular convertToProtoCompany(CompanyDAO companyDAO);

}
