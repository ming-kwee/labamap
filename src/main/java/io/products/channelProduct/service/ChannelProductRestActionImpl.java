package io.products.channelProduct.service;

import com.google.protobuf.Descriptors;
import io.grpc.StatusException;
import io.products.channelProduct.action.ChannelProductSyncActionApi;
import io.products.channelProduct.api.ChannelProductApi;
import io.products.channelProduct.service.AbstractChannelProductRestAction;
import io.products.channelProduct.service.ChannelProductRestActionApi;
import io.products.shared.utils;
import kalix.javasdk.action.ActionCreationContext;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ChannelProductRestActionImpl extends AbstractChannelProductRestAction {

  private static final Logger LOG = LoggerFactory.getLogger(ChannelProductRestActionImpl.class);

  public ChannelProductRestActionImpl(ActionCreationContext creationContext) {}

  @Override
  public Effect<ChannelProductRestActionApi.ChannelProductHttpResponse> createRestChannelProduct(ChannelProductSyncActionApi.ChannelProducts syncChannelProducts) {
    /* _______________________________________________ */
    // Get Metadata and Set to an List<MetadataGroup>
    /* _______________________________________________ */
    List<ChannelProductSyncActionApi.MetadataGroup> syncChnlMetadataGroupList = syncChannelProducts.getChannelProductsList().get(0)
            .getMetadataGroupsList().stream()
            .toList();
    Map<String, Object> hashmapMetadata = new HashMap<>();
    for (ChannelProductSyncActionApi.MetadataGroup metadataGroup : syncChnlMetadataGroupList) {
      for (ChannelProductSyncActionApi.ChannelMetadata metadata : metadataGroup.getChannelMetadataList()) {
        hashmapMetadata.put(metadata.getGrouping() + '.' + metadata.getSubGrouping() + '.' + metadata.getKey(),
                metadata);
      }
    }
    /* _______________________________________________ */

    LOG.info("CREATE REST CHANNEL PRODUCT ");
            /* _________________________________________________________ */
    // Convert Data From Sync Action To Api ChannelProduct
    /* _________________________________________________________ */
    List<ChannelProductApi.CreateChannelProductCommand.Builder> channelProductBuilders = new ArrayList<>();
    for (ChannelProductSyncActionApi.ChannelProduct syncChannelProduct : syncChannelProducts.getChannelProductsList()) {
      channelProductBuilders
              .add(convert_FromSyncAction_ToApi_ChannelProduct(syncChannelProduct, syncChannelProducts.getEventId()));
    }
    ChannelProductRestActionApi.ChannelProductHttpResponse.Builder createRestResultBuilder =
            ChannelProductRestActionApi.ChannelProductHttpResponse.newBuilder();

    try {
      ChannelProductRestActionApi.ChannelProductHttpResponse createRestResult = ChannelProductService
              .createAChannelProductService(channelProductBuilders.get(0).build(), hashmapMetadata);

      createRestResultBuilder
              .setDescription(createRestResult.getDescription())
              .setStatus(createRestResult.getStatus())
              .putAllData(createRestResult.getDataMap());

    } catch (StatusException e) {
      throw new RuntimeException(e);
    }

    return effects().reply(createRestResultBuilder.build());
//    return createRestResult;
  }



  private static ChannelProductApi.CreateChannelProductCommand.Builder convert_FromSyncAction_ToApi_ChannelProduct(
          ChannelProductSyncActionApi.ChannelProduct syncChannelProduct,
          String eventId) {

    ChannelProductApi.CreateChannelProductCommand.Builder channelProductBuilder = ChannelProductApi.CreateChannelProductCommand.newBuilder();
    Descriptors.FieldDescriptor[] fields = channelProductBuilder.getDescriptorForType().getFields()
            .toArray(new Descriptors.FieldDescriptor[0]);

    /* _________________________ */
    /* ------------------------- */
    // Create Channel Attributes
    /* _________________________ */
    /* ------------------------- */
    List<ChannelProductSyncActionApi.ChannelAttribute> syncChnlAttributeList = syncChannelProduct.getChannelAttributesList().stream()
            .toList();
    List<ChannelProductApi.ChannelProductAttribute> apiChnlProdAttributeList = new ArrayList<>();
    channelProductBuilder.setId(UUID.randomUUID().toString());
    for (ChannelProductSyncActionApi.ChannelAttribute syncChnlAttribute : syncChnlAttributeList) {
      // Create Product common fields
      if (syncChnlAttribute.getIsCommon()) {
        for (Descriptors.FieldDescriptor field : fields) {

          String propName = utils.replaceAfterUnderscore(syncChnlAttribute.getChnlAttrName());

          if (field.getName().equals(propName)) {
            if ("sku".equals(field.getName())) {
              channelProductBuilder.setSku(syncChnlAttribute.getChnlAttrValue());
            } else if ("storeId".equals(field.getName())) {
              channelProductBuilder.setStoreId(syncChnlAttribute.getChnlAttrValue());
            } else if ("channelId".equals(field.getName())) {
              channelProductBuilder.setChannelId(syncChnlAttribute.getChnlAttrValue());
            }
          }
        }
      }
      channelProductBuilder.setEventId(UUID.randomUUID().toString());

      ChannelProductApi.ChannelProductAttribute apiChnlProdAttribute = ChannelProductApi.ChannelProductAttribute.newBuilder()
              .setAttrId(syncChnlAttribute.getAttrId())
              .setChnlAttrName(syncChnlAttribute.getChnlAttrName())
              .setChnlAttrType(syncChnlAttribute.getChnlAttrType())
              .setValue(syncChnlAttribute.getChnlAttrValue())
              .setIsCommon(syncChnlAttribute.getIsCommon())
              .build();
      apiChnlProdAttributeList.add(apiChnlProdAttribute);
    }
    channelProductBuilder.clearChannelProductAttribute().addAllChannelProductAttribute(apiChnlProdAttributeList);

    /* _________________________ */
    /* ------------------------- */
    // Create Channel Variants
    /* _________________________ */
    /* ------------------------- */
    List<ChannelProductSyncActionApi.VariantGroup> syncChnlVariantGroupList = syncChannelProduct.getVariantGroupsList().stream()
            .toList();
    List<ChannelProductApi.ChannelProductVariantGroup> apiChnlProdVariantGroupList = new ArrayList<>();
    for (ChannelProductSyncActionApi.VariantGroup syncChnlVariantGroup : syncChnlVariantGroupList) {

      List<ChannelProductSyncActionApi.ChannelVariant> syncChnlVariantList = syncChnlVariantGroup.getChannelVariantList().stream()
              .toList();
      List<ChannelProductApi.ChannelProductVariant> apiChnlProdVariantList = new ArrayList<>();
      for (ChannelProductSyncActionApi.ChannelVariant syncChnlVariant : syncChnlVariantList) {

        ChannelProductApi.ChannelProductVariant apiChnlProdVariant = ChannelProductApi.ChannelProductVariant.newBuilder()
                .setVrntId(syncChnlVariant.getVrntId())
                .setChnlVrntName(syncChnlVariant.getChnlVrntName())
                .setChnlVrntType(syncChnlVariant.getChnlVrntType())
                .setValue(syncChnlVariant.getChnlVrntValue())
                .build();
        apiChnlProdVariantList.add(apiChnlProdVariant);

      }
      // Create a ChannelProductVariantGroup and set its channelProductVariants field
      ChannelProductApi.ChannelProductVariantGroup apiChnlProdVariantGroup = ChannelProductApi.ChannelProductVariantGroup.newBuilder()
              .addAllChannelProductVariant(apiChnlProdVariantList)
              .build();
      apiChnlProdVariantGroupList.add(apiChnlProdVariantGroup);
    }

    channelProductBuilder.clearChannelProductVariantGroup()
            .addAllChannelProductVariantGroup(apiChnlProdVariantGroupList);

    /* _________________________ */
    /* ------------------------- */
    // Create Channel Options
    /* _________________________ */
    /* ------------------------- */
    List<ChannelProductSyncActionApi.OptionGroup> syncChnlOptionGroupList = syncChannelProduct.getOptionGroupsList().stream()
            .toList();
    List<ChannelProductApi.ChannelProductOptionGroup> apiChnlProdOptionGroupList = new ArrayList<>();
    for (ChannelProductSyncActionApi.OptionGroup syncChnlOptionGroup : syncChnlOptionGroupList) {

      List<ChannelProductSyncActionApi.ChannelOption> syncChnlOptionList = syncChnlOptionGroup.getChannelOptionList().stream()
              .toList();
      List<ChannelProductApi.ChannelProductOption> apiChnlProdOptionList = new ArrayList<>();
      for (ChannelProductSyncActionApi.ChannelOption syncChnlOption : syncChnlOptionList) {

        ChannelProductApi.ChannelProductOption apiChnlProdOption = ChannelProductApi.ChannelProductOption.newBuilder()
                .setOptnId(syncChnlOption.getOptnId())
                .setChnlOptnName(syncChnlOption.getChnlOptnName())
                .setChnlOptnType(syncChnlOption.getChnlOptnType())
                .setValue(syncChnlOption.getChnlOptnValue())
                .build();
        apiChnlProdOptionList.add(apiChnlProdOption);

      }
      // Create a ChannelProductVariantGroup and set its channelProductVariants field
      ChannelProductApi.ChannelProductOptionGroup apiChnlProdOptionGroup = ChannelProductApi.ChannelProductOptionGroup.newBuilder()
              .addAllChannelProductOption(apiChnlProdOptionList)
              .build();
      apiChnlProdOptionGroupList.add(apiChnlProdOptionGroup);
    }

    channelProductBuilder.clearChannelProductOptionGroup()
            .addAllChannelProductOptionGroup(apiChnlProdOptionGroupList);
    channelProductBuilder.setEventId(eventId);

    return channelProductBuilder;
  }

}
