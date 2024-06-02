package io.products.channelProduct.workflow;

import akka.Done;
import com.google.protobuf.Empty;
import io.products.channelProduct.action.ChannelProductCrudActionApi;
import io.products.channelProduct.action.ChannelProductSyncActionApi;
import io.products.channelProduct.domain.ChannelProductDomain;
import io.products.channelProduct.service.ChannelProductRestActionApi;
import kalix.javasdk.workflow.WorkflowContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Workflow Service described in your io/products/channelProduct/workflow/channel_product_workflow.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class ChannelProductWorkflowImpl extends AbstractChannelProductWorkflowImpl {
  private static final Logger LOGGER = LoggerFactory.getLogger(ChannelProductWorkflowImpl.class);
  @SuppressWarnings("unused")
  private final String workflowId;

  public ChannelProductWorkflowImpl(WorkflowContext context) {
    this.workflowId = context.workflowId();
  }

  @Override
  public ChannelProductDomain.ChannelProductState emptyState() {
    return null;
  }

  @Override
  public WorkflowDef<ChannelProductDomain.ChannelProductState> definition() {
    Step createChannelProduct = step("create-channel-product")
            .call(ChannelProductSyncActionApi.ChannelProducts.class, cmd -> {
              LOGGER.info("SONNY create-channel-product");
              return components().channelProductCrudActionImpl().createChannelProduct(cmd);
            }).andThen(ChannelProductSyncActionApi.ChannelProducts.class, cmd -> {
              LOGGER.info("SONNY AFTER create-channel-product");
//              return effects().end();
              return effects().transitionTo("create-rest-channel-product", cmd);
            });

    Step createRestChannelProduct = step("create-rest-channel-product")
            .call(ChannelProductSyncActionApi.ChannelProducts.class, cmd -> {
              LOGGER.info("SONNY create-rest-channel-product");
              return components().channelProductRestActionImpl().createRestChannelProduct(cmd);
            }).andThen(ChannelProductRestActionApi.ChannelProductHttpResponse.class, channelProductHttpResponse -> {
              LOGGER.info("SONNY AFTER create-rest-channel-product");
              return effects().end();
            });

    Step compensateCreateChannelProduct = step("compensate-create-channel-product")
            .asyncCall(() -> {
              LOGGER.info("SONNY compensate-create-channel-product");
              return CompletableFuture.completedStage(Done.getInstance()).thenApply(__ -> Empty.getDefaultInstance());
            })
            .andThen(Empty.class, __ -> effects().end());

    return workflow()
            .defaultStepTimeout(Duration.ofSeconds(15))
            .addStep(createChannelProduct)//, maxRetries(0).failoverTo("compensate-create-channel-product"))
            .addStep(createRestChannelProduct)//, maxRetries(0).failoverTo("compensate-create-channel-product"))
            .addStep(compensateCreateChannelProduct);
  }

  @Override
  public Effect<Empty> start(ChannelProductDomain.ChannelProductState currentState, ChannelProductSyncActionApi.ChannelProducts cmd) {
    if (currentState != null) {
      return effects().error("Channel Product Workflow already started");
    } else {
      LOGGER.info("START WORKFLOW");
      return effects()
              .transitionTo("create-channel-product", cmd)
              .thenReply(Empty.getDefaultInstance());
    }
  }

  @Override
  public Effect<Empty> dummyChannelProductCrud(ChannelProductDomain.ChannelProductState currentState, ChannelProductCrudActionApi.DummyCommandChannelProductCrud dummyCommandChannelProductCrud) {
    return null;
  }
}
