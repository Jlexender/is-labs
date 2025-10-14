package org.example.mapper;

import org.example.entity.Dummy;
import org.example.dto.DummyDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DummyMapper {
    DummyMapper INSTANCE = Mappers.getMapper(DummyMapper.class);

    DummyDto toDto(Dummy dummy);
    Dummy toEntity(DummyDto dummyDto);
}