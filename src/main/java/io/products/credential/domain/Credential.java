package io.products.credential.domain;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.products.credential.api.CredentialApi;
import io.products.metadataProperty.api.MetadataPropertyApi;
import io.products.metadataProperty.domain.MetadataPropertyDomain;
import kalix.javasdk.valueentity.ValueEntityContext;

import java.util.Optional;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/credential/api/credential_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class Credential extends AbstractCredential {
  @SuppressWarnings("unused")
  private final String entityId;

  public Credential(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public CredentialDomain.CredentialState emptyState() {
    return CredentialDomain.CredentialState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> createCredential(CredentialDomain.CredentialState currentState, CredentialApi.Credential command) {
    CredentialDomain.CredentialState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }

  private Optional<Effect<Empty>> reject(CredentialDomain.CredentialState  currentState, CredentialApi.Credential  command) {
    if (currentState.getChannelId().equals(command.getChannelId()) && currentState.getKey().equals(command.getKey()) && currentState.getUserId().equals(command.getUserId())) {
      return Optional.of(effects().error("Credential is already exists!!", Status.Code.ALREADY_EXISTS));
    } else {
      return Optional.empty();
    }
  }

  private CredentialDomain.CredentialState  convertToDomain(CredentialApi.Credential credential) {
    return CredentialDomain.CredentialState.newBuilder()
            .setChannelId(credential.getChannelId())
            .setKey(credential.getKey())
            .setUserId(credential.getUserId())
            .setValue(credential.getValue())
            .build();
  }

  private Effect<Empty> handle(CredentialDomain.CredentialState state, CredentialApi.Credential command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

  @Override
  public Effect<CredentialApi.Credential> getCredential(CredentialDomain.CredentialState currentState, CredentialApi.GetCredentialRequest getCredentialRequest) {
    return effects().error("The command handler for `GetCredential` is not implemented, yet");
  }

  @Override
  public Effect<Empty> updateCredential(CredentialDomain.CredentialState currentState, CredentialApi.Credential credential) {
    return effects().error("The command handler for `UpdateCredential` is not implemented, yet");
  }

  @Override
  public Effect<Empty> deleteCredential(CredentialDomain.CredentialState currentState, CredentialApi.DeleteCredentialRequest deleteCredentialRequest) {
    return effects().error("The command handler for `DeleteCredential` is not implemented, yet");
  }
}
