package com.roberttisma.sandbox.protobuf_mapstruct;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import com.roberttisma.sandbox.protobuf_mapstruct.company_service.ProtoCompanyOO;
import com.roberttisma.sandbox.protobuf_mapstruct.company_service.ProtoEmployeeComposite;
import com.roberttisma.sandbox.protobuf_mapstruct.company_service.ProtoEmployeeOO;
import com.roberttisma.sandbox.protobuf_mapstruct.converters.CompositeConverter;
import com.roberttisma.sandbox.protobuf_mapstruct.converters.CompositeConverterImpl;
import com.roberttisma.sandbox.protobuf_mapstruct.converters.ConverterConfig;
import com.roberttisma.sandbox.protobuf_mapstruct.converters.LanguageConverterImpl;
import com.roberttisma.sandbox.protobuf_mapstruct.converters.OOConverter;
import com.roberttisma.sandbox.protobuf_mapstruct.converters.OOConverterImpl;
import com.roberttisma.sandbox.protobuf_mapstruct.converters.RegularConverter;
import com.roberttisma.sandbox.protobuf_mapstruct.converters.RegularConverterImpl;
import com.roberttisma.sandbox.protobuf_mapstruct.model.entity.EmployeeDao;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
    OOConverterImpl.class,
    LanguageConverterImpl.class,
    ConverterConfig.class,
    CompositeConverterImpl.class,
    RegularConverterImpl.class
})
public class MapstuctPOCTest {

  @Autowired
  private OOConverter ooConverter;

  @Autowired
  private CompositeConverter compositeConverter;

  @Autowired
  private RegularConverter regularConverter;

  @Test
  public void OneOfProtoEmployee_to_JavaEmployee_issues(){
    /**
     * Demonstrates the default values for primatives and strings.
     * since the setter methods were not called, the fields are correctly showing as undefined
     */
    val emptyDto = ProtoEmployeeOO.newBuilder().build();
    assertThat(emptyDto.getId()).isEqualTo(0);
    assertThat(emptyDto.getIsMale()).isEqualTo(false);
    assertThat(emptyDto.getFirstName()).isEqualTo("");
    assertThat(isIdDefined(emptyDto)).isFalse();
    assertThat(isMaleDefined(emptyDto)).isFalse();
    assertThat(isFirstNameDefined(emptyDto)).isFalse();

    /**
     * After conversion to a java DAO, there is no way of knowing is the field was actually set by the proto DTO :(
     */
    val shouldBeEmptyDao = ooConverter.convertToEmployeeDao(emptyDto);
    assertThat(shouldBeEmptyDao.getId()).isEqualTo(0);
    assertThat(shouldBeEmptyDao.isMale()).isEqualTo(false);
    assertThat(shouldBeEmptyDao.getFirstName()).isEqualTo("");

    /**
     * After setting the proto DTO with DEFAULT values, is correctly shows the fields as defined
     */
    val setDto = ProtoEmployeeOO.newBuilder().setId(0).setIsMale(false).setFirstName("").build();
    assertThat(isIdDefined(setDto)).isTrue();
    assertThat(isMaleDefined(setDto)).isTrue();
    assertThat(isFirstNameDefined(setDto)).isTrue();

    /**
     * Once the above proto DTO with fields defined with default values is converted to a java DAO,
     * there is no way of knowing if the fields were actually set to the default values or if they are not set at all
     */
    val setDao = ooConverter.convertToEmployeeDao(setDto);
    assertThat(setDao.getId()).isEqualTo(0);
    assertThat(setDao.isMale()).isEqualTo(false);
    assertThat(setDao.getFirstName()).isEqualTo("");
  }


  @Test
  public void EmployeeDao_to_OneOfProtoEmployee_issues(){

    /**
     * As expected, the presence for primative field, id, can be detected when the corresponding setter is not called
     * Like wise for the others
     */
    val emptyDto = ProtoEmployeeOO.newBuilder().build();
    assertThat(isIdDefined(emptyDto)).isFalse();
    assertThat(isMaleDefined(emptyDto)).isFalse();
    assertThat(isFirstNameDefined(emptyDto)).isFalse();

    /**
     The converter will call the setId, setIsMade method, since the daos values for those setters a
     NOT nullable (i.e int and bool cannot be null), the setters are called.
     This means, even though the DAO has undefined fields, the DTO will show them as defined :(
     However, since the DAOs string field IS NULLABLE, a null check (and hence a presence check) can be made
     before calling the DTOs setter. This is why the firstName field of the DTO is detected to be undefined,
     while the others are falsely defined.
     */
    val emptyDato = EmployeeDao.builder().build();
    val shouldBeEmptyDto = ooConverter.convertToProtoEmployee(emptyDato);
    assertThat(isIdDefined(shouldBeEmptyDto)).isTrue();
    assertThat(isMaleDefined(shouldBeEmptyDto)).isTrue();
    assertThat(isFirstNameDefined(shouldBeEmptyDto)).isFalse();

    /**
     * In this example, we actually set the id field.
     * Notice that the results are the same as the previous experiement.
     * This means that when Java DAOs are converted to Proto DTOs,
     * the DTOs fields corresponding to the undefined java DAOs primative (i.e non objects) fields
     * will falsely show as defined.
     */
    val setDAO = EmployeeDao.builder().id(234).build();
    val expectedEmptyDto = ooConverter.convertToProtoEmployee(setDAO);
    assertThat(isIdDefined(expectedEmptyDto)).isTrue();
    assertThat(isMaleDefined(expectedEmptyDto)).isTrue();
    assertThat(isFirstNameDefined(expectedEmptyDto)).isFalse();
  }

  /**
   * All the same primative and string conversion issues still exist with this,
   * except now theres a list. I probably should have started with company instead of employee
   */
  @Test
  public void OneOfProtoCompany_to_CompanyDao_issues(){
    /**
     * For lists, it is easy to see if they contain anything (presence check)
     */
    val emptyProto = ProtoCompanyOO.newBuilder().build();
    assertThat(emptyProto.getEmployeesList()).isEmpty();

    /**
     * Fortunately, using the OneOf method you list presence is properly converted
     */
    val shouldByEmptyCompanyPojo = ooConverter.convertToCompanyDao(emptyProto);
    assertThat(shouldByEmptyCompanyPojo.getEmployees()).isEmpty();

    val setProto = ProtoCompanyOO.newBuilder().addEmployees(ProtoEmployeeOO.newBuilder().build()).build();
    assertThat(setProto.getEmployeesList()).hasSize(1);
    val setPojo = ooConverter.convertToCompanyDao(setProto);
    assertThat(setPojo.getEmployees()).hasSize(1);
  }

  @Test
  public void EmployeeDao_to_CompositeProtoEmployee_issues(){
    /**
     * Using the composite mapper, the presence checking is correct, and so null fields are NOT set.
     * Since the DAO contains primatives such as int and boolean, the corresponding setter on the proto will be called
     */
    val emptyDato = EmployeeDao.builder().build();
    val shouldBeEmptyDto = compositeConverter.convertToProtoEmployee(emptyDato);
    assertThat(shouldBeEmptyDto.hasId()).isTrue();
    assertThat(shouldBeEmptyDto.hasIsMale()).isTrue();
    assertThat(shouldBeEmptyDto.hasFirstName()).isFalse();

    /**
     * By defining the firstname in the DAO, the corresponding setter in the proto is called, making it present
     */
    val setDAO = EmployeeDao.builder().firstName("John").build();
    val expectedEmptyDto = compositeConverter.convertToProtoEmployee(setDAO);
    assertThat(expectedEmptyDto.hasId()).isTrue();
    assertThat(expectedEmptyDto.hasIsMale()).isTrue();
    assertThat(expectedEmptyDto.hasFirstName()).isTrue();
  }

  @Test
  public void CompositeProtoEmployee_to_EmployeeDAO_issues(){
    /**
     * Since all the fields are objects, the proto object has `has` presence checking generated for each field.
     * As expected, a composite employee with no fields set should have all their `has` methods returning false,
     * however their value will not neccessarily be null
     */
    val emptyDto = ProtoEmployeeComposite.newBuilder().build();
    assertThat(emptyDto.hasId()).isFalse();
    assertThat(emptyDto.hasIsMale()).isFalse();
    assertThat(emptyDto.hasIsCool()).isFalse();
    assertThat(emptyDto.hasFirstName()).isFalse();

    /**
     * The one downside to the composite approach, is needing to call the `getValue()` method in addition to calling the getter method.
     */
    assertThat(emptyDto.getId().getValue()).isEqualTo(0);
    assertThat(emptyDto.getIsMale().getValue()).isFalse();
    assertThat(emptyDto.getFirstName().getValue()).isEqualTo("");
    assertThat(emptyDto.getIsCool().getValue()).isFalse();

    /**
     * Since the emptyDTO did not call any setters, the compositeConverter will construct a DAO with null values for Object references, and default values for primatives (like int and boolean)
     */
    val emptyDao = compositeConverter.convertToEmployeeDao(emptyDto);
    assertThat(emptyDao.getId()).isEqualTo(0);
    assertThat(emptyDao.isMale()).isFalse();
    assertThat(emptyDao.getIsCool()).isNull();
    assertThat(emptyDao.getFirstName()).isNull();

    /**
     * Create a DTO with values defined, that coinincidetly are default values.
     */
    val setDto = ProtoEmployeeComposite.newBuilder()
        .setIsMale(BoolValue.of(false))
        .setIsCool(BoolValue.of(false))
        .setId(Int32Value.of(0))
        .setFirstName(StringValue.of(""))
        .build();

    /**
     * Although default value were explicitly set in the previous step, the compositeConverter is able to detect object references that were explictly set, which is why they are not null. This is the main benefit with using the composite approach.
     */
    val setDao = compositeConverter.convertToEmployeeDao(setDto);
    assertThat(setDao.getId()).isEqualTo(0); // Same as emptyDao
    assertThat(setDao.isMale()).isFalse(); // Same as emptyDao
    assertThat(setDao.getFirstName()).isEqualTo(""); // NOT NULL - presence detected, and non-default value
    assertThat(setDao.getIsCool()).isFalse(); // NOT NULL - presence detected, and non-default value
  }


  private static boolean isFirstNameDefined(ProtoEmployeeOO e){
    return e.getOptionalFirstNameCase() != ProtoEmployeeOO.OptionalFirstNameCase.OPTIONALFIRSTNAME_NOT_SET;
  }

  private static boolean isIdDefined(ProtoEmployeeOO e){
    return e.getOptionalIdCase() != ProtoEmployeeOO.OptionalIdCase.OPTIONALID_NOT_SET;
  }

  private static boolean isMaleDefined(ProtoEmployeeOO e){
    return e.getOptionalIsMaleCase() != ProtoEmployeeOO.OptionalIsMaleCase.OPTIONALISMALE_NOT_SET;
  }

}
