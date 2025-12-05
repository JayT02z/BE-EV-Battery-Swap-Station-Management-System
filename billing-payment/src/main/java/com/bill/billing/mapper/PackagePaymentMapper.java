package com.bill.billing.mapper;

import com.bill.billing.model.DTO.PackagePaymentDTO;
import com.bill.billing.model.entity.PackagePayment;
import com.bill.billing.model.request.PackagePaymentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {DriverMapper.class})
public interface PackagePaymentMapper {

  PackagePaymentDTO toDTO(PackagePayment entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "customerId", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  PackagePayment toEntity(PackagePaymentRequest request);
}
