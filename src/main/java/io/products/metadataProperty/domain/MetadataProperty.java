package io.products.metadataProperty.domain;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.products.channelPlatform.api.ChannelPlatformApi;
import io.products.channelPlatform.domain.ChannelPlatformDomain;
import io.products.metadataProperty.api.MetadataPropertyApi;
import kalix.javasdk.valueentity.ValueEntityContext;

import java.util.Optional;
import java.util.UUID;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/metadataProperty/api/metadata_property_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class MetadataProperty extends AbstractMetadataProperty {
  @SuppressWarnings("unused")
  private final String entityId;

  public MetadataProperty(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public MetadataPropertyDomain.MetadataPropertyState emptyState() {
    return MetadataPropertyDomain.MetadataPropertyState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> createMetadataProperty(MetadataPropertyDomain.MetadataPropertyState currentState, MetadataPropertyApi.MetadataProperty command) {
    MetadataPropertyDomain.MetadataPropertyState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }

  private Optional<Effect<Empty>> reject(MetadataPropertyDomain.MetadataPropertyState  currentState, MetadataPropertyApi.MetadataProperty  command) {
    if (currentState.getChannelId().equals(command.getChannelId()) && currentState.getKey().equals(command.getKey())) {
      return Optional.of(effects().error("Metadata is already exists!!", Status.Code.ALREADY_EXISTS));
    } else {
      return Optional.empty();
    }
  }

  private MetadataPropertyDomain.MetadataPropertyState  convertToDomain(MetadataPropertyApi.MetadataProperty metadata) {
    return MetadataPropertyDomain.MetadataPropertyState.newBuilder()
            .setChannelId(metadata.getChannelId())
            .setKey(metadata.getKey())
            .setValue(metadata.getValue())
            .setTarget(metadata.getTarget())
            .setGrouping(metadata.getGrouping())
            .setSubGrouping(metadata.getSubGrouping())
            .build();
  }

  private Effect<Empty> handle(MetadataPropertyDomain.MetadataPropertyState state, MetadataPropertyApi.MetadataProperty command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

  @Override
  public Effect<MetadataPropertyApi.MetadataProperty> getMetadataProperty(MetadataPropertyDomain.MetadataPropertyState currentState, MetadataPropertyApi.GetMetadataPropertyRequest getMetadataPropertyRequest) {
    return effects().error("The command handler for `GetMetadataProperty` is not implemented, yet");
  }

  @Override
  public Effect<Empty> updateMetadataProperty(MetadataPropertyDomain.MetadataPropertyState currentState, MetadataPropertyApi.MetadataProperty metadataProperty) {
    return effects().error("The command handler for `UpdateMetadataProperty` is not implemented, yet");
  }

  @Override
  public Effect<Empty> deleteMetadataProperty(MetadataPropertyDomain.MetadataPropertyState currentState, MetadataPropertyApi.DeleteMetadataPropertyRequest deleteMetadataPropertyRequest) {
    return effects().error("The command handler for `DeleteMetadataProperty` is not implemented, yet");
  }
}
