package io.products.channelPlatform.domain;

import java.util.Optional;
import java.util.UUID;

import com.google.protobuf.Empty;

import io.grpc.Status;
import io.products.channelPlatform.api.ChannelPlatformApi;
import kalix.javasdk.valueentity.ValueEntityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import kalix.javasdk.action.Action.Effect;

public class ChannelPlatform extends AbstractChannelPlatform {
  @SuppressWarnings("unused")
  private final String entityId;

  public ChannelPlatform(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public ChannelPlatformDomain.ChannelPlatformState emptyState() {
    return ChannelPlatformDomain.ChannelPlatformState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> createChannelPlatform(ChannelPlatformDomain.ChannelPlatformState currentState, io.products.channelPlatform.api.ChannelPlatformApi.ChannelPlatform command) {
    ChannelPlatformDomain.ChannelPlatformState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }


  private Optional<Effect<Empty>> reject(ChannelPlatformDomain.ChannelPlatformState  currentState, ChannelPlatformApi.ChannelPlatform  command) {

    // if (currentState.getChannelId().equals(command.getChannelId())) {
    //   return Optional.of(effects().error("Channel is already exists!!", Status.Code.NOT_FOUND));

    // } else {
      return Optional.empty();
    // }
  }


  private ChannelPlatformDomain.ChannelPlatformState  convertToDomain(ChannelPlatformApi.ChannelPlatform  channel) {
    return ChannelPlatformDomain.ChannelPlatformState .newBuilder()
        .setChannelId(UUID.randomUUID().toString())
        .setChannelName(channel.getChannelName())
        .setChannelEndpoint(channel.getChannelEndpoint())
        .build();
  }

  private Effect<Empty> handle(ChannelPlatformDomain.ChannelPlatformState state, ChannelPlatformApi.ChannelPlatform command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }








  @Override
  public Effect<ChannelPlatformApi.ChannelPlatform> getChannelPlatform(ChannelPlatformDomain.ChannelPlatformState currentState, ChannelPlatformApi.GetChannelPlatformRequest getChannelPlatformRequest) {
    return effects().error("The command handler for `GetChannelPlatform` is not implemented, yet");
  }

  @Override
  public Effect<Empty> updateChannelPlatform(ChannelPlatformDomain.ChannelPlatformState currentState, ChannelPlatformApi.ChannelPlatform channelPlatform) {
    return effects().error("The command handler for `UpdateChannelPlatform` is not implemented, yet");
  }

  @Override
  public Effect<Empty> deleteChannelPlatform(ChannelPlatformDomain.ChannelPlatformState currentState, ChannelPlatformApi.ChannelPlatformRequest deleteChannelPlatformRequest) {
    return effects().error("The command handler for `DeleteChannelPlatform` is not implemented, yet");
  }
}
