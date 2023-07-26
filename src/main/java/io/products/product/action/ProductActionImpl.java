package io.products.product.action;

import com.google.protobuf.Empty;

import io.grpc.Status;
import io.products.channelProduct.api.ChannelProductApi;

import java.util.function.Function;
import kalix.javasdk.DeferredCall;
import kalix.javasdk.action.ActionCreationContext;
import kalix.javasdk.action.Action.Effect;

import java.util.concurrent.CompletionStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Action Service described in your io/products/product/action/product_action.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class ProductActionImpl extends AbstractProductAction {
  private static final Logger LOG = LoggerFactory.getLogger(ProductActionImpl.class);

  public ProductActionImpl(ActionCreationContext creationContext) {
  }

  /**************************************************
   * CREATE
   **************************************************/
  @Override
  public Effect<Empty> createProduct(ProductActionApi.Product product) {
    // components().product().createProduct(null)
    // components().
    CompletionStage<Empty> create_product = components().product()
        .createProduct(io.products.product.api.ProductApi.Product.newBuilder()
            .setId(product.getId())
            .setBodyHtml(product.getBodyHtml())
            .setProductType(product.getProductType())
            .setStatus(product.getStatus())
            .setTitle(product.getTitle())
            .setVendor(product.getVendor())
            .build())
        .execute();
    // return effects().reply(Empty.getDefaultInstance());

    CompletionStage<Effect<Empty>> effect = create_product.thenApply(product_response -> {
      // /* ---- jika melewati ini artinya product sudah dicreate ------- */
      // /* ------------------------------------------------------------- */


      LOG.info("HAHAHA".concat(product.getBodyHtml()));
      // /* -------------------------------------------------------------------- */
      // /* - #2 memanggil service hitChannelapi utk mencreate channel product - */
      // /* -------------------------------------------------------------------- */
      DeferredCall<ChannelProductApi.ChannelProduct, Empty> call = components().channelProduct()
          .createChannelProduct(convertToChannelProductApi(product, product.getId()));

      return effects().forward(call);
      // return effects().reply(Empty.getDefaultInstance());

    });

    /* ------------------------------------------------------------- */
    return effects().asyncEffect(effect.exceptionally(NotEmptyAuth()));
  }

  private ChannelProductApi.ChannelProduct convertToChannelProductApi(ProductActionApi.Product product, String id) {
    return ChannelProductApi.ChannelProduct.newBuilder()
        .setId(id != null ? id : product.getId())
        .setPlatformId("id")
        .setProductId("id")
        .build();
  }

  private Function<Throwable, ? extends Effect<Empty>> NotEmptyAuth() {
    /* --- jika kesini artinya ada error saat mencreate product ---- */
    /* ------------------------------------------------------------- */

    return (e) -> effects().error(
        e.getMessage().split(":")[2],
        Status.Code.valueOf(e.getMessage().split(":")[1].trim().toUpperCase()));

  }






  //   private ChannelProductApi.ChannelProduct convertToChannelProductApi(ProductActionApi.Product product, String id) {
  //   return ChannelProductApi.ChannelProduct.newBuilder()
  //       .setId(id != null ? id : product.getId())
  //       .setPlatformId("id")
  //       .setProductId("id")
  //       .build();
  // }
}