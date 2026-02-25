package com.chakray.users.application.mapper;

import com.chakray.users.application.dto.AddressDto;
import com.chakray.users.domain.model.Address;

import java.util.List;
import java.util.stream.Collectors;

public class AddressMapper {
    public static AddressDto toDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setName(address.getName());
        dto.setStreet(address.getStreet());
        dto.setCountryCode(address.getCountryCode());
        return dto;
    }

    public static Address toEntity(AddressDto dto) {
        Address address = new Address();
        address.setId(dto.getId());
        address.setName(dto.getName());
        address.setStreet(dto.getStreet());
        address.setCountryCode(dto.getCountryCode());
        return address;
    }

    public static List<AddressDto> toDtoList(List<Address> addresses) {
        return addresses.stream().map(AddressMapper::toDto).collect(Collectors.toList());
    }

    public static List<Address> toEntityList(List<AddressDto> dtos) {
        return dtos.stream().map(AddressMapper::toEntity).collect(Collectors.toList());
    }

}
