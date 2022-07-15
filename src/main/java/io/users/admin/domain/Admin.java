package io.users.admin.domain;

import com.google.protobuf.Empty;
import io.users.admin.api.AdminApi;
import kalix.javasdk.valueentity.ValueEntityContext;
import java.util.Optional;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/users/admin/api/admin_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class Admin extends AbstractAdmin {
  @SuppressWarnings("unused")
  private final String entityId;

  public Admin(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public AdminDomain.AdminState emptyState() {
    return AdminDomain.AdminState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> createAdmin(AdminDomain.AdminState currentState, AdminApi.Admin command) {
    AdminDomain.AdminState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }

  private Optional<Effect<Empty>> reject(AdminDomain.AdminState currentState, AdminApi.Admin command) {
    if (currentState.getEmail().equals(command.getEmail())) {
      return Optional.of(effects().error("Email is already exists!"));
    }
    return Optional.empty();
  }

  private Effect<Empty> handle(AdminDomain.AdminState state, AdminApi.Admin command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

  private AdminDomain.AdminState convertToDomain(AdminApi.Admin admin) {
    return AdminDomain.AdminState.newBuilder()
        .setId(admin.getId())
        .setEmail(admin.getEmail())
        .setPassword(admin.getPassword())
        .setFirstName(admin.getFirstName())
        .setLastName(admin.getLastName())
        .setRole(admin.getRole())
        .build();
  }

  @Override
  public Effect<AdminApi.Admin> getAdmin(AdminDomain.AdminState currentState,
      AdminApi.GetAdminRequest command) {
    if (currentState.getEmail().equals(command.getEmail())) {
      return effects().reply(convertToApi(currentState));
    } else {
      return effects().error("Admin " + command.getEmail() + " has not been created.");
    }
  }

  private AdminApi.Admin convertToApi(AdminDomain.AdminState state) {
    return AdminApi.Admin.newBuilder()
        .setId(state.getId())
        .setEmail(state.getEmail())
        // .setPassword(state.getPassword())
        .setFirstName(state.getFirstName())
        .setLastName(state.getLastName())
        .setRole(state.getRole())
        .build();
  }
}
