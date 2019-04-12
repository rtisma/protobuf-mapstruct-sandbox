import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.icgc.argo.company_service.ProtoCompanyOO;
import org.icgc.argo.company_service.ProtoEmployeeOO;
import org.icgc.argo.program_service.Program;
import org.icgc.argo.program_service.converters.Conv;
import org.icgc.argo.program_service.converters.ConverterConfig;
import org.icgc.argo.program_service.converters.LanguageConverterImpl;
import org.icgc.argo.program_service.converters.OOConverter;
import org.icgc.argo.program_service.converters.OOConverterImpl;
import org.icgc.argo.program_service.model.entity.EmployeeDao;
import org.icgc.argo.rob_service.RobBespokeMessage;
import org.icgc.argo.rob_service.RobEnum;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { OOConverterImpl.class, LanguageConverterImpl.class, ConverterConfig.class })
public class MapstuctPOCTest {

  @Autowired
  private OOConverter mapper;

  @Test
  public void fromProto_enum_default_defined(){
    val proto = Program.newBuilder().setId(UUID.randomUUID().toString()).build();
    val dd = Conv.COV.convertToDaoProgram(proto).getMembershipType();

    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> Conv.COV.convertToDaoProgram(proto))
        .withMessageContaining("Can't get the number of an unknown enum value");

  }

  /**
   * Empty RobBespokeMessage
   */
  @Test
  public void testRob(){
    val empty = RobBespokeMessage.newBuilder().build();
    assertThat(empty.getId()).isEqualTo(0);
    assertThat(empty.getName()).isEqualTo("");
    assertThat(empty.getRobEnum()).isEqualTo(RobEnum.UNDEFINED);
    assertThat(RobEnum.UNDEFINED.getNumber()).isEqualTo(0);

    // convert To dao

    log.info("sdfsdf");

  }

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
    val shouldBeEmptyDao = mapper.convertToEmployeeDao(emptyDto);
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
    val setDao = mapper.convertToEmployeeDao(setDto);
    assertThat(setDao.getId()).isEqualTo(0);
    assertThat(setDao.isMale()).isEqualTo(false);
    assertThat(setDao.getFirstName()).isEqualTo("");
  }


  @Test
  public void JavaEmployee_to_OneOfProtoEmployee_issues(){

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
    val shouldBeEmptyDto = mapper.convertToProtoEmployee(emptyDato);
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
    val expectedEmptyDto = mapper.convertToProtoEmployee(setDAO);
    assertThat(isIdDefined(expectedEmptyDto)).isTrue();
    assertThat(isMaleDefined(expectedEmptyDto)).isTrue();
    assertThat(isFirstNameDefined(expectedEmptyDto)).isFalse();
  }

  /**
   * All the same primative and string conversion issues still exist with this,
   * except now theres a list. I probably should have started with company instead of employee
   */
  @Test
  public void OneOfProtoCompany_to_CompanyPojo_issues(){
    /**
     * For lists, it is easy to see if they contain anything (presence check)
     */
    val emptyProto = ProtoCompanyOO.newBuilder().build();
    assertThat(emptyProto.getEmployeesList()).isEmpty();

    /**
     * Fortunately, using the OneOf method you list presence is properly converted
     */
    val shouldByEmptyCompanyPojo = mapper.convertToCompanyDao(emptyProto);
    assertThat(shouldByEmptyCompanyPojo.getEmployees()).isEmpty();

    val setProto = ProtoCompanyOO.newBuilder().addEmployees(ProtoEmployeeOO.newBuilder().build()).build();
    assertThat(setProto.getEmployeesList()).hasSize(1);
    val setPojo = mapper.convertToCompanyDao(setProto);
    assertThat(setPojo.getEmployees()).hasSize(1);
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
