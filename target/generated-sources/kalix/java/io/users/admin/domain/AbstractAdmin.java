package io.users.admin.domain;

import com.google.protobuf.Empty;
import io.users.Components;
import io.users.ComponentsImpl;
import io.users.admin.api.AdminApi;
import kalix.javasdk.valueentity.ValueEntity;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public abstract class AbstractAdmin extends ValueEntity<AdminDomain.AdminState> {

  protected final Components components() {
    return new ComponentsImpl(commandContext());
  }

  public abstract Effect<Empty> createAdmin(AdminDomain.AdminState currentState, AdminApi.Admin admin);

  public abstract Effect<AdminApi.Admin> getAdmin(AdminDomain.AdminState currentState, AdminApi.GetAdminRequest getAdminRequest);

  public abstract Effect<Empty> updateAdmin(AdminDomain.AdminState currentState, AdminApi.Admin admin);

}
