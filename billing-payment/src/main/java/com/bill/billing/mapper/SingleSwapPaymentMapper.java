package com.bill.billing.mapper;

import com.bill.billing.model.DTO.SingleSwapPaymentDTO;
import com.bill.billing.model.entity.SingleSwapPayment;
import com.bill.billing.model.request.SingleSwapPaymentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {DriverMapper.class})
public interface SingleSwapPaymentMapper {

  @Mapping(target = "customerId", source = "customerId")
  SingleSwapPaymentDTO toDTO(SingleSwapPayment entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "customerId", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  SingleSwapPayment toEntity(SingleSwapPaymentRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "customerId", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  void updateEntityFromRequest(SingleSwapPaymentRequest request, @MappingTarget SingleSwapPayment entity);
}
