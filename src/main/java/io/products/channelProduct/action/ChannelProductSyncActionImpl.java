package io.products.channelProduct.action;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.products.channelProduct.service.ChannelProductRestActionApi;
import kalix.javasdk.action.ActionCreationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class ChannelProductSyncActionImpl extends AbstractChannelProductSyncAction {

  private static final Logger LOG = LoggerFactory.getLogger(ChannelProductSyncActionImpl.class);

  public ChannelProductSyncActionImpl(ActionCreationContext creationContext) {}

  @Override
  public Effect<Empty> syncChannelProduct(ChannelProductSyncActionApi.ChannelProducts channelProducts) {


    LOG.info("SYNC CHANNEL PRODUCT");
    CompletionStage<Empty> create_channel_product = components().channelProductCrudActionImpl()
            .createChannelProduct(channelProducts).execute();
    CompletionStage<Effect<Empty>> effect = create_channel_product.thenApply(x -> {


      CompletionStage<ChannelProductRestActionApi.ChannelProductHttpResponse> create_rest_channel_product = components().channelProductRestActionImpl()
              .createRestChannelProduct(channelProducts).execute();
      CompletionStage<Effect<Empty>> effect2 = create_rest_channel_product.thenApply(y -> {

        return effects().reply(Empty.getDefaultInstance());
      });

      return effects().asyncEffect(effect2.exceptionally(ExceptionHandling()));
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
