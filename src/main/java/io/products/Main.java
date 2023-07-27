package io.products;

import io.products.channelProductVariant.domain.ChannelProductVariant;
import io.products.channelProductVariant.view.ChannelProductVariantViewImpl;
import io.products.variant.domain.Variant;
import io.products.variant.view.VariantViewImpl;
import kalix.javasdk.Kalix;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import io.products.productAttribute.domain.ProductAttribute;
import io.products.productAttribute.view.ProductAttributeViewImpl;
import io.products.productAttributeValue.action.ProductAttributeValueActionImpl;
import io.products.productAttributeValue.domain.ProductAttributeValue;
import io.products.productAttributeValue.view.ProductAttributeValueView;
import io.products.productAttributeValue.view.ProductAttributeValueViewImpl;
import io.products.channelPlatform.domain.ChannelPlatform;
import io.products.channelPlatform.view.ChannelPlatformViewImpl;
import io.products.channelProduct.domain.ChannelProduct;
import io.products.product.action.ProductActionImpl;
import io.products.product.domain.Product;
import io.products.product.view.ProductViewImpl;
import io.products.channelProductAttribute.domain.ChannelProductAttribute;
import io.products.channelProductAttribute.view.ChannelProductAttributeViewImpl;
public final class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static Kalix createKalix() {
    // The KalixFactory automatically registers any generated Actions, Views or
    // Entities,
    // and is kept up-to-date with any changes in your protobuf definitions.
    // If you prefer, you may remove this and manually register these components in
    // a
    // `new Kalix()` instance.
    return KalixFactory.withComponents(
        ChannelPlatform::new,
        ChannelProduct::new,
        ChannelProductAttribute::new,
        ChannelProductVariant::new,
        Product::new,
        ProductAttribute::new,
        ProductAttributeValue::new,
        Variant::new,
        ChannelPlatformViewImpl::new,
        ChannelProductAttributeViewImpl::new,
        ChannelProductVariantViewImpl::new,
        ProductActionImpl::new,
        ProductAttributeValueActionImpl::new,
        ProductAttributeValueViewImpl::new,
        ProductAttributeViewImpl::new,
        ProductViewImpl::new,
        VariantViewImpl::new
    );
  }

  //
  public static void main(String[] args) throws Exception {

    LOG.info("starting the Kalix service");
    createKalix().start();
  }

}