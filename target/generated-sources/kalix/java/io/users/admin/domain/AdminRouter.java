package io.users.admin.domain;

import com.google.protobuf.Empty;
import io.users.admin.api.AdminApi;
import kalix.javasdk.impl.valueentity.ValueEntityRouter;
import kalix.javasdk.valueentity.CommandContext;
import kalix.javasdk.valueentity.ValueEntity;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * A value entity handler that is the glue between the Protobuf service <code>AdminService</code>
 * and the command handler methods in the <code>Admin</code> class.
 */
public class AdminRouter extends ValueEntityRouter<AdminDomain.AdminState, Admin> {

  public AdminRouter(Admin entity) {
    super(entity);
  }

  @Override
  public ValueEntity.Effect<?> handleCommand(
      String commandName, AdminDomain.AdminState state, Object command, CommandContext context) {
    switch (commandName) {

      case "CreateAdmin":
        return entity().createAdmin(state, (AdminApi.Admin) command);

      case "GetAdmin":
        return entity().getAdmin(state, (AdminApi.GetAdminRequest) command);

      case "UpdateAdmin":
        return entity().updateAdmin(state, (AdminApi.Admin) command);

      default:
        throw new ValueEntityRouter.CommandHandlerNotFound(commandName);
    }
  }
}
