package com.assignment.voucher.service.mapper;

import com.assignment.voucher.service.dto.VoucherDto;
import com.assignment.voucher.service.entity.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper
public interface VoucherMapper {

    @Mappings({
            @Mapping(target="voucherCode", source="entity.voucherCode")
    })
    VoucherDto entityToDto(Voucher entity);

    List<VoucherDto> entitiesToDtos(List<Voucher> entity);
}
