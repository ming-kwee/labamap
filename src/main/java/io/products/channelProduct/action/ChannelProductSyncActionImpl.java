package io.products.channelProduct.action;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.products.channelProduct.action.ChannelProductRestActionApi;
import kalix.javasdk.action.ActionCreationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class ChannelProductSyncActionImpl extends AbstractChannelProductSyncAction {

    private static final Logger LOG = LoggerFactory.getLogger(ChannelProductSyncActionImpl.class);

    public ChannelProductSyncActionImpl(ActionCreationContext creationContext) {
    }

    @Override
    public Effect<Empty> syncChannelProduct(ChannelProductSyncActionApi.ChannelProducts channelProducts) {
//    Before Implement Workflow
//    CompletionStage<ChannelProductSyncActionApi.ChannelProducts> create_channel_product = components().channelProductCrudActionImpl()
//            .createChannelProduct(channelProducts).execute();
//    CompletionStage<Effect<Empty>> effect = create_channel_product.thenApply(x -> {
//
//
//      CompletionStage<ChannelProductRestActionApi.ChannelProductHttpResponse> create_rest_channel_product = components().channelProductRestActionImpl()
//              .createRestChannelProduct(channelProducts).execute();
//      CompletionStage<Effect<Empty>> effect2 = create_rest_channel_product.thenApply(y -> {
//
//        return effects().reply(Empty.getDefaultInstance());
//      });
//
//      return effects().asyncEffect(effect2.exceptionally(ExceptionHandling()));
//    });

//    After Implement Workflow
        CompletionStage<Empty> start_channel_product_workflow = components().channelProductWorkflowImpl()
                .start(channelProducts).execute();
        CompletionStage<Effect<Empty>> effect = start_channel_product_workflow.thenApply(x -> {
            return effects().reply(Empty.getDefaultInstance());
        });

        return effects().asyncEffect(effect.exceptionally(ExceptionHandling()));
    }


    private Function<Throwable, ? extends Effect<Empty>> ExceptionHandling() {
        /* --- jika kesini artinya ada error saat mencreate product ---- */
        /* ------------------------------------------------------------- */
        LOG.info("jika kesini artinya ada error");
        return (e) -> effects().error(e.getMessage(), Status.Code.CANCELLED);
        /* ------------------------------------------------------------- */
    }

}
