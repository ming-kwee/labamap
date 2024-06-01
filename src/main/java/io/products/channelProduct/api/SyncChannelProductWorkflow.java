package io.products.channelProduct.api;

import com.google.protobuf.Empty;
import io.products.channelProduct.action.ChannelProductSyncActionApi;
import io.products.channelProduct.domain.ChannelProductDomain;
import kalix.javasdk.workflow.WorkflowContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Workflow Service described in your io/products/channelProduct/api/sync_channel_product_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class SyncChannelProductWorkflow extends AbstractSyncChannelProductWorkflow {
  private static final Logger LOGGER = LoggerFactory.getLogger(SyncChannelProductWorkflow.class);
  @SuppressWarnings("unused")
  private final String workflowId;

  public SyncChannelProductWorkflow(WorkflowContext context) {
    this.workflowId = context.workflowId();
  }

  @Override
  public ChannelProductDomain.ChannelProductState emptyState() {
    return null;
  }

  @Override
  public WorkflowDef<ChannelProductDomain.ChannelProductState> definition() {
    Step createSyncChannelProduct = step("create-sync-channel-product")
            .call(ChannelProductSyncActionApi.ChannelProducts.class, cmd -> {
              LOGGER.info("SONNY create-sync-channel-product");
              return components().channelProductSyncActionImpl().syncChannelProduct(cmd);
            }).andThen(Empty.class, __ -> {
              LOGGER.info("SONNY AFTER create-sync-channel-product");
              return effects().end();
//              return effects().transitionTo("update-sync-channel-product-id", updateSyncChannelProductCommand);
            });
//
//    Step updateSyncChannelProductId = step("update-sync-channel-product-id")
//            .call(SyncChannelProductApi.UpdateSyncChannelProductCommand.class, cmd -> {
//              LOGGER.info("SONNY update-sync-channel-product-id");
//              return components().syncChannelProduct().updateChannelProduct(cmd);
//            }).andThen(Empty.class, __ -> {
//              return effects().end();
//            });

    return workflow()
            .addStep(createSyncChannelProduct);
//            .addStep(updateSyncChannelProductId);
  }

  @Override
  public Effect<Empty> start(ChannelProductDomain.ChannelProductState currentState, ChannelProductSyncActionApi.ChannelProducts cmd) {
    if (currentState != null) {
      return effects().error("transfer already started");
    } else {
      LOGGER.info("START WORKFLOW");
      return effects()
              .transitionTo("create-sync-channel-product", cmd)
              .thenReply(Empty.getDefaultInstance());
    }
  }
}
