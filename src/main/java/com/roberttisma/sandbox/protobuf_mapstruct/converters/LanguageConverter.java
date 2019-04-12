package com.roberttisma.sandbox.protobuf_mapstruct.converters;

import com.roberttisma.sandbox.protobuf_mapstruct.company_service.ProtoLanguage;
import com.roberttisma.sandbox.protobuf_mapstruct.company_service.ProtoLanguageValue;
import com.roberttisma.sandbox.protobuf_mapstruct.model.entity.Language;
import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;

import static org.mapstruct.MappingConstants.ANY_REMAINING;
import static org.mapstruct.MappingConstants.NULL;

@Mapper(config = ConverterConfig.class)
public interface LanguageConverter {
  /**
   * Language conversions
   */
  @ValueMapping( source = ANY_REMAINING, target = NULL)
  Language convertToLanguage(ProtoLanguage protoLanguage);

  ProtoLanguage convertToProtoLanguage(Language language);

  default ProtoLanguage convertToProtoLanguage(ProtoLanguageValue language){
    return language.getValue();
  }

  default ProtoLanguageValue convertToProtoLanguageValue(ProtoLanguage language){
    return ProtoLanguageValue.newBuilder().setValue(language).build();
  }

}
