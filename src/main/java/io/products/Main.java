package io.products;

import io.products.attribute.domain.Attribute;
import io.products.attribute.view.AttributeViewImpl;
import io.products.masterProduct.action.MasterProductActionImpl;
import io.products.masterProduct.domain.MasterProduct;
import io.products.masterProduct.view.MasterProductViewImpl;
import io.products.channelAttributeMapping.domain.ChannelAttributeMapping;
import io.products.channelAttributeMapping.view.ChannelAttributeMappingViewImpl;
import io.products.channelMapping.domain.ChannelMapping;
import io.products.channelMapping.view.ChannelMappingViewImpl;
import io.products.credential.domain.Credential;
import io.products.credential.view.CredentialByChannelIdAndUserIdViewImpl;
import io.products.credential.view.CredentialViewImpl;
import io.products.metadata.domain.Metadata;
import io.products.metadata.view.MetadataByChannelIdAndTargetViewImpl;
import io.products.metadata.view.MetadataViewImpl;
import io.products.variant.domain.Variant;
import io.products.variant.view.VariantViewImpl;
import kalix.javasdk.Kalix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        ChannelAttributeMapping::new,
        ChannelMapping::new,
        ChannelPlatform::new,
        ChannelProduct::new,
        Credential::new,
        MasterProduct::new,
        Metadata::new,
        Variant::new,
        AttributeViewImpl::new,
        ChannelAttributeMappingViewImpl::new,
        ChannelMappingViewImpl::new,
        ChannelPlatformViewImpl::new,
        ChannelProductActionImpl::new,
        ChannelProductViewImpl::new,
        CredentialByChannelIdAndUserIdViewImpl::new,
        CredentialViewImpl::new,
        MasterProductActionImpl::new,
        MasterProductViewImpl::new,
        MetadataByChannelIdAndTargetViewImpl::new,
        MetadataViewImpl::new,
        VariantViewImpl::new);
  }

  //
  public static void main(String[] args) throws Exception {

    LOG.info("starting the Kalix service");
    createKalix().start();
  }

}