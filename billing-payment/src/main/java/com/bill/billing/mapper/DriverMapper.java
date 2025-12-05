package com.bill.billing.mapper;

import com.bill.billing.client.service.auth_user_service.model.admin.response.UserResponse;
import com.bill.billing.model.DTO.DriverDTO;
import com.bill.billing.model.event.entity.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DriverMapper {

  @Mapping(target = "id", ignore = true)
  void updateDriverFromUserResponse(UserResponse source, @MappingTarget Driver target);

  @Mapping(target = "birthday", expression = "java(driver.getBirthday() != null ? driver.getBirthday().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE) : null)")
  DriverDTO toDTO(Driver driver);
}
