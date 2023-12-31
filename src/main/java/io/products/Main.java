package io.products;

import io.products.attribute.action.AttributeActionImpl;
import io.products.attribute.domain.Attribute;
import io.products.attribute.view.AttributeViewImpl;
import io.products.centralProduct.action.CentralProductActionImpl;
import io.products.centralProduct.domain.CentralProduct;
import io.products.centralProduct.view.CentralProductViewImpl;
import io.products.channelAttributeMapping.domain.ChannelAttributeMapping;
import io.products.channelAttributeMapping.view.ChannelAttributeMappingViewImpl;
import io.products.channelMapping.domain.ChannelMapping;
import io.products.channelMapping.view.ChannelMappingViewImpl;
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
import io.products.channelPlatform.domain.ChannelPlatform;
import io.products.channelPlatform.view.ChannelPlatformViewImpl;
import io.products.channelProduct.action.ChannelProductActionImpl;
import io.products.channelProduct.domain.ChannelProduct;
import io.products.channelProduct.view.ChannelProductViewImpl;


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
        Attribute::new,
        CentralProduct::new,
        ChannelAttributeMapping::new,
        ChannelMapping::new,
        ChannelPlatform::new,
        ChannelProduct::new,
        Variant::new,
        AttributeActionImpl::new,
        AttributeViewImpl::new,
        CentralProductActionImpl::new,
        CentralProductViewImpl::new,
        ChannelAttributeMappingViewImpl::new,
        ChannelMappingViewImpl::new,
        ChannelPlatformViewImpl::new,
        ChannelProductActionImpl::new,
        ChannelProductViewImpl::new,
        VariantViewImpl::new);
  }

  //
  public static void main(String[] args) throws Exception {

    LOG.info("starting the Kalix service");
    createKalix().start();
  }

}