package io.products.channelProduct.action;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.products.shared.utils;
import kalix.javasdk.action.ActionCreationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Action Service described in your io/products/channelProduct/action/channel_product_sync_action.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class ChannelProductSyncActionImpl extends AbstractChannelProductSyncAction {

  private static final Logger LOG = LoggerFactory.getLogger(ChannelProductSyncActionImpl.class);

  public ChannelProductSyncActionImpl(ActionCreationContext creationContext) {}

  @Override
  public Effect<Empty> syncChannelProduct(ChannelProductSyncActionApi.ChannelProducts channelProducts) {


    LOG.info("CREATE CHANNEL PRODUCT");
    ChannelProductActionApi.ChannelProducts_ channelProducts_ = ChannelProductSyncActionImpl.convert_FromSyncAction_ToAction_ChannelProduct(channelProducts);

    CompletionStage<Empty> create_channel_product = components().channelProductActionImpl()
            .createChannelProduct(channelProducts_).execute();

    CompletionStage<Effect<Empty>> effect = create_channel_product.thenApply(x -> {
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


  public static ChannelProductActionApi.ChannelProducts_ convert_FromSyncAction_ToAction_ChannelProduct(ChannelProductSyncActionApi.ChannelProducts channelProducts) {
    ChannelProductActionApi.ChannelProducts_.Builder builder = ChannelProductActionApi.ChannelProducts_.newBuilder();

    // Set eventId
    builder.setEventId(channelProducts.getEventId());

    // Convert and set channelProducts
    List<ChannelProductActionApi.ChannelProduct_> channelProducts_ = channelProducts.getChannelProductsList().stream()
            .map(ChannelProductSyncActionImpl::convertSyncChannelProduct)
            .collect(Collectors.toList());
    builder.addAllChannelProducts(channelProducts_);

    return builder.build();
  }



  private static ChannelProductActionApi.ChannelProduct_ convertSyncChannelProduct(ChannelProductSyncActionApi.ChannelProduct channelProduct) {
    ChannelProductActionApi.ChannelProduct_.Builder builder = ChannelProductActionApi.ChannelProduct_.newBuilder();

    // Convert and set channelAttributes
    List<ChannelProductActionApi.ChannelAttribute_> channelAttributes = channelProduct.getChannelAttributesList().stream()
            .map(ChannelProductSyncActionImpl::convertSyncChannelAttribute)
            .collect(Collectors.toList());
    builder.addAllChannelAttributes(channelAttributes);

    // Convert and set variantGroups
    List<ChannelProductActionApi.VariantGroup_> variantGroups = channelProduct.getVariantGroupsList().stream()
            .map(ChannelProductSyncActionImpl::convertSyncVariantGroup)
            .collect(Collectors.toList());
    builder.addAllVariantGroups(variantGroups);

    // Convert and set optionGroups
    List<ChannelProductActionApi.OptionGroup_> optionGroups = channelProduct.getOptionGroupsList().stream()
            .map(ChannelProductSyncActionImpl::convertSyncOptionGroup)
            .collect(Collectors.toList());
    builder.addAllOptionGroups(optionGroups);

    // Convert and set metadataGroups
    List<ChannelProductActionApi.MetadataGroup_> metadataGroups = channelProduct.getMetadataGroupsList().stream()
            .map(ChannelProductSyncActionImpl::convertSyncMetadataGroup)
            .collect(Collectors.toList());
    builder.addAllMetadataGroups(metadataGroups);

    return builder.build();
  }


  private static ChannelProductActionApi.ChannelAttribute_ convertSyncChannelAttribute(ChannelProductSyncActionApi.ChannelAttribute channelAttribute) {
    return ChannelProductActionApi.ChannelAttribute_.newBuilder()
            .setAttrId(channelAttribute.getAttrId())
            .setChnlAttrName(channelAttribute.getChnlAttrName())
            .setChnlAttrValue(channelAttribute.getChnlAttrValue())
            .setChnlAttrType(channelAttribute.getChnlAttrType())
            .setIsCommon(channelAttribute.getIsCommon())
            .build();
  }


  private static ChannelProductActionApi.VariantGroup_ convertSyncVariantGroup(ChannelProductSyncActionApi.VariantGroup variantGroup) {
    ChannelProductActionApi.VariantGroup_.Builder builder = ChannelProductActionApi.VariantGroup_.newBuilder();

    // Convert and set channelVariant
    List<ChannelProductActionApi.ChannelVariant_> channelVariants = variantGroup.getChannelVariantList().stream()
            .map(ChannelProductSyncActionImpl::convertSyncChannelVariant)
            .collect(Collectors.toList());
    builder.addAllChannelVariant(channelVariants);

    return builder.build();
  }



  private static ChannelProductActionApi.ChannelVariant_ convertSyncChannelVariant(ChannelProductSyncActionApi.ChannelVariant channelVariant) {
    return ChannelProductActionApi.ChannelVariant_.newBuilder()
            .setVrntId(channelVariant.getVrntId())
            .setChnlVrntName(channelVariant.getChnlVrntName())
            .setChnlVrntValue(channelVariant.getChnlVrntValue())
            .setChnlVrntType(channelVariant.getChnlVrntType())
            .build();
  }


  private static ChannelProductActionApi.OptionGroup_ convertSyncOptionGroup(ChannelProductSyncActionApi.OptionGroup optionGroup) {
    ChannelProductActionApi.OptionGroup_.Builder builder = ChannelProductActionApi.OptionGroup_.newBuilder();

    // Convert and set channelOption
    List<ChannelProductActionApi.ChannelOption_> channelOptions = optionGroup.getChannelOptionList().stream()
            .map(ChannelProductSyncActionImpl::convertSyncChannelOption)
            .collect(Collectors.toList());
    builder.addAllChannelOption(channelOptions);

    return builder.build();
  }

  private static ChannelProductActionApi.ChannelOption_ convertSyncChannelOption(ChannelProductSyncActionApi.ChannelOption channelOption) {
    return ChannelProductActionApi.ChannelOption_.newBuilder()
            .setOptnId(channelOption.getOptnId())
            .setChnlOptnName(channelOption.getChnlOptnName())
            .setChnlOptnValue(channelOption.getChnlOptnValue())
            .setChnlOptnType(channelOption.getChnlOptnType())
            .build();
  }

  private static ChannelProductActionApi.MetadataGroup_ convertSyncMetadataGroup(ChannelProductSyncActionApi.MetadataGroup metadataGroup) {
    ChannelProductActionApi.MetadataGroup_.Builder builder = ChannelProductActionApi.MetadataGroup_.newBuilder();

    // Convert and set channelMetadata
    List<ChannelProductActionApi.ChannelMetadata_> channelMetadata = metadataGroup.getChannelMetadataList().stream()
            .map(ChannelProductSyncActionImpl::convertSyncChannelMetadata)
            .collect(Collectors.toList());
    builder.addAllChannelMetadata(channelMetadata);

    return builder.build();
  }

  private static ChannelProductActionApi.ChannelMetadata_ convertSyncChannelMetadata(ChannelProductSyncActionApi.ChannelMetadata channelMetadata) {
    return ChannelProductActionApi.ChannelMetadata_.newBuilder()
            .setChannelId(channelMetadata.getChannelId())
            .setKey(channelMetadata.getKey())
            .setValue(channelMetadata.getValue())
            .setTarget(channelMetadata.getTarget())
            .setGrouping(channelMetadata.getGrouping())
            .setSubGrouping(channelMetadata.getSubGrouping())
            .build();
  }

}
