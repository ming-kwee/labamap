package io.products.channelProduct.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import io.grpc.Status;
import io.grpc.StatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.protobuf.Descriptors.FieldDescriptor;

import java.util.function.Function;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelAttribute_;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelOption_;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelVariant_;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelMetadata_;
import io.products.channelProduct.action.ChannelProductActionApi.MetadataGroup_;
import io.products.channelProduct.action.ChannelProductActionApi.OptionGroup_;
import io.products.channelProduct.action.ChannelProductActionApi.VariantGroup_;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductAttribute;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductHttpResponse;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductOption;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductOptionGroup;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariant;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariantGroup;
import io.products.channelProduct.api.ChannelProductApi.CreateChannelProductCommand;
import io.products.channelProduct.api.ChannelProductApi.DeleteChannelProductCommand;
import io.products.channelProduct.api.ChannelProductApi.UpdateChannelProductCommand;
import io.products.channelProduct.service.ChannelProductService;
import io.products.shared.utils;
import kalix.javasdk.action.ActionCreationContext;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.*;
import com.google.protobuf.util.JsonFormat;


public class ChannelProductActionImpl extends AbstractChannelProductAction {
  private static final Logger LOG = LoggerFactory.getLogger(ChannelProductActionImpl.class);

  public ChannelProductActionImpl(ActionCreationContext creationContext) {
  }

  @Override
  public Effect<Empty> createChannelProduct(ChannelProductActionApi.ChannelProducts_ actChannelProducts) {

    /* _______________________________________________ */
    // Get Metadata and Set to an List<MetadataGroup>
    /* _______________________________________________ */
    List<MetadataGroup_> actChnlMetadataGroupList = actChannelProducts.getChannelProductsList().get(0)
        .getMetadataGroupsList().stream()
        .collect(Collectors.toList());
    Map<String, Object> hashmapMetadata = new HashMap<>();
    for (MetadataGroup_ metadataGroup : actChnlMetadataGroupList) {
      for (ChannelMetadata_ metadata : metadataGroup.getChannelMetadataList()) {
        hashmapMetadata.put(metadata.getGrouping() + '.' + metadata.getSubGrouping() + '.' + metadata.getKey(),
            metadata);
      }
    }
    /* _______________________________________________ */

    /* _______________________________________________ */
    // Convert Data From Action To Api ChannelProduct
    /* _______________________________________________ */
    List<CreateChannelProductCommand.Builder> channelProductBuilders = new ArrayList<>();
    for (ChannelProductActionApi.ChannelProduct_ actChannelProduct : actChannelProducts.getChannelProductsList()) {
      channelProductBuilders
          .add(convert_FromAction_ToApi_ChannelProduct(actChannelProduct, actChannelProducts.getEventId()));
    }
    /* _______________________________________________ */

    CompletableFuture<Effect<Empty>> effect;
    if (hashmapMetadata.containsKey("integration.body_content.create_cp_multi_product")) {
      ChannelMetadata_ bodyContent = (ChannelMetadata_) hashmapMetadata
          .get("integration.body_content.create_cp_multi_product");
      boolean isMultiProduct = "true".equals(bodyContent.getValue());

      // Call the appropriate method based on the boolean value
      effect = !isMultiProduct ? createAChannelProduct(channelProductBuilders.get(0), hashmapMetadata)
          : createSomeChannelProducts(channelProductBuilders, hashmapMetadata);
    } else {
      effect = createSomeChannelProducts(channelProductBuilders, hashmapMetadata);
    }
    /* ------------------------------------------------------------- */
    return effects().asyncEffect(effect.exceptionally(ExceptionHandling()));
    /* ------------------------------------------------------------- */

  }





  /* --------------------------- */
  // Create A Channel Product
  /* ___________________________ */
  private CompletableFuture<Effect<Empty>> createAChannelProduct(
      CreateChannelProductCommand.Builder createChannelProductBuilder, Map<String, Object> hashmapMetadata) {

    LOG.info("CREATE A CHANNEL PRODUCT");
    CompletionStage<Empty> create_channel_product = components().channelProduct()
        .createChannelProduct(createChannelProductBuilder.build()).execute();

    return (CompletableFuture<Effect<Empty>>) create_channel_product.thenApply(x -> {
       return effects().reply(Empty.getDefaultInstance());
    });
  }

  private Function<Throwable, ? extends Effect<Empty>> ExceptionHandling() {
    /* --- jika kesini artinya ada error saat mencreate product ---- */
    /* ------------------------------------------------------------- */
    LOG.info("jika kesini artinya ada error");
    return (e) -> effects().error(e.getMessage(), Status.Code.CANCELLED);
    /* ------------------------------------------------------------- */
  }








  /* --------------------------- */
  // Create Some Channel Products
  /* ___________________________ */
  private CompletableFuture<Effect<Empty>> createSomeChannelProducts(
          List<CreateChannelProductCommand.Builder> channelProductBuilders, Map<String, Object> hashmapMetadata) {

    List<CompletableFuture<CompletionStage<Empty>>> futures = new ArrayList<>();
    // Start asynchronous processing for each channelProductBuilder
    for (CreateChannelProductCommand.Builder channelProductBuilder : channelProductBuilders) {
      CompletableFuture<CompletionStage<Empty>> future = CompletableFuture.supplyAsync(() -> {
        try {
          // Your asynchronous processing logic goes here
          LOG.info("CREATE SOME CHANNEL PRODUCTS");
          return components().channelProduct().createChannelProduct(channelProductBuilder.build()).execute();
        } catch (Exception e) {
          LOG.info("ERROR when CREATE SOME CHANNEL PRODUCTS " + e.getMessage());
          throw new RuntimeException(e); // Handle exceptions appropriately
        }
      }, Executors.newCachedThreadPool()); // Specify an executor if needed
      futures.add(future);
    }

    // Wait for all asynchronous processing to complete
    CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

    // Wait for all executions to complete
    try {
      allOf.get(); // This will block until all executions are completed
    } catch (InterruptedException | ExecutionException e) {
      effects().error(e.getMessage());
    }

    // Chain the next step after all asynchronous processing is complete
    return allOf.thenCompose(ignored -> {

      // Process the results of the asynchronous tasks
      List<CompletionStage<Empty>> responses = futures.stream()
              .map(CompletableFuture::join) // Get the result of each CompletableFuture
              .collect(Collectors.toList());

      String anyException_WhileCreation = "";
      for (CompletionStage<Empty> response : responses) {
        try {
          response.toCompletableFuture().join();
        } catch (Exception e) {
          anyException_WhileCreation = e.getMessage();
          break; // Exit loop as soon as an exception is encountered
        }
      }

      if (anyException_WhileCreation != "") {
        // jika terjadi error saat looping create channel products.
        return rollbackAllChannelProducts(channelProductBuilders, anyException_WhileCreation);
      } else {
            return CompletableFuture.completedFuture(effects().reply(Empty.getDefaultInstance()));
      }
    });
  }

  private CompletableFuture<Effect<Empty>> rollbackAllChannelProducts(
          List<CreateChannelProductCommand.Builder> channelProductBuilders, String anyException_WhileProcess) {

    List<CompletableFuture<CompletionStage<Empty>>> futures = new ArrayList<>();
    // Start asynchronous soft delete for each channelProductBuilder
    for (CreateChannelProductCommand.Builder channelProductBuilder : channelProductBuilders) {
      CompletableFuture<CompletionStage<Empty>> future = CompletableFuture.supplyAsync(() -> {
        try {
          return components().channelProduct().deleteChannelProduct(DeleteChannelProductCommand.newBuilder()
                          .setId(channelProductBuilder.getId())
                          .build())
                  .execute();
        } catch (Exception e) {
          throw new RuntimeException(e); // Handle exceptions appropriately
        }
      }, Executors.newCachedThreadPool()); // Specify an executor if needed
      futures.add(future);
    }

    // Wait for all asynchronous deletion to complete
    CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

    // Wait for all executions to complete
    try {
      allOf.get(); // This will block until all executions are completed
    } catch (InterruptedException | ExecutionException e) {
      effects().error(e.getMessage());
    }

    // Chain the next step after all asynchronous deletion is complete
    return allOf.thenCompose(ignored -> {

      // Process the results of the asynchronous tasks
      List<CompletionStage<Empty>> responses = futures.stream()
              .map(CompletableFuture::join) // Get the result of each CompletableFuture
              .collect(Collectors.toList());

      String anyException_WhileDeletion = "";
      for (CompletionStage<Empty> response : responses) {
        try {
          response.toCompletableFuture().join();
        } catch (Exception e) {
          anyException_WhileDeletion = e.getMessage();
          break; // Exit loop as soon as an exception is encountered
        }
      }
      String finalExceptionMessage;
      if (anyException_WhileDeletion != "") {
        finalExceptionMessage = anyException_WhileDeletion;
      } else {
        finalExceptionMessage = anyException_WhileProcess;
      }
      ;
      return CompletableFuture.<Effect<Empty>>completedFuture(effects().error(finalExceptionMessage));

    });
  }












  /*
   * -----------------------------------------------------------------------------
   */
  private static CreateChannelProductCommand.Builder convert_FromAction_ToApi_ChannelProduct(
      ChannelProductActionApi.ChannelProduct_ actChannelProduct,
      String eventId) {

    CreateChannelProductCommand.Builder channelProductBuilder = CreateChannelProductCommand.newBuilder();
    FieldDescriptor[] fields = channelProductBuilder.getDescriptorForType().getFields()
        .toArray(new FieldDescriptor[0]);

    /* _________________________ */
    /* ------------------------- */
    // Create Channel Attributes
    /* _________________________ */
    /* ------------------------- */
    List<ChannelAttribute_> actChnlAttributeList = actChannelProduct.getChannelAttributesList().stream()
        .collect(Collectors.toList());
    List<ChannelProductAttribute> apiChnlProdAttributeList = new ArrayList<>();
    channelProductBuilder.setId(UUID.randomUUID().toString());
    for (ChannelAttribute_ actChnlAttribute : actChnlAttributeList) {
      // Create Product common fields
      if (actChnlAttribute.getIsCommon() == true) {
        for (FieldDescriptor field : fields) {

          String propName = utils.replaceAfterUnderscore(actChnlAttribute.getChnlAttrName());
          LOG.info("PROPNAME " + propName + ", " + field.getName() + ", " + actChnlAttribute.getChnlAttrName());

          if (field.getName().equals(propName)) {
            if ("sku".equals(field.getName())) {
              channelProductBuilder.setSku(actChnlAttribute.getChnlAttrValue());
            } else if ("storeId".equals(field.getName())) {
              channelProductBuilder.setStoreId(actChnlAttribute.getChnlAttrValue());
            } else if ("channelId".equals(field.getName())) {
              channelProductBuilder.setChannelId(actChnlAttribute.getChnlAttrValue());
            }
          }
        }
      }
      channelProductBuilder.setEventId(UUID.randomUUID().toString());

      ChannelProductAttribute apiChnlProdAttribute = ChannelProductAttribute.newBuilder()
          .setAttrId(actChnlAttribute.getAttrId())
          .setChnlAttrName(actChnlAttribute.getChnlAttrName())
          .setChnlAttrType(actChnlAttribute.getChnlAttrType())
          .setValue(actChnlAttribute.getChnlAttrValue())
          .setIsCommon(actChnlAttribute.getIsCommon())
          .build();
      apiChnlProdAttributeList.add(apiChnlProdAttribute);
    }
    channelProductBuilder.clearChannelProductAttribute().addAllChannelProductAttribute(apiChnlProdAttributeList);

    /* _________________________ */
    /* ------------------------- */
    // Create Channel Variants
    /* _________________________ */
    /* ------------------------- */
    List<VariantGroup_> actChnlVariantGroupList = actChannelProduct.getVariantGroupsList().stream()
        .collect(Collectors.toList());
    List<ChannelProductVariantGroup> apiChnlProdVariantGroupList = new ArrayList<>();
    for (VariantGroup_ actChnlVariantGroup : actChnlVariantGroupList) {

      List<ChannelVariant_> actChnlVariantList = actChnlVariantGroup.getChannelVariantList().stream()
          .collect(Collectors.toList());
      List<ChannelProductVariant> apiChnlProdVariantList = new ArrayList<>();
      for (ChannelVariant_ actChnlVariant : actChnlVariantList) {

        ChannelProductVariant apiChnlProdVariant = ChannelProductVariant.newBuilder()
            .setVrntId(actChnlVariant.getVrntId())
            .setChnlVrntName(actChnlVariant.getChnlVrntName())
            .setChnlVrntType(actChnlVariant.getChnlVrntType())
            .setValue(actChnlVariant.getChnlVrntValue())
            .build();
        apiChnlProdVariantList.add(apiChnlProdVariant);

      }
      // Create a ChannelProductVariantGroup and set its channelProductVariants field
      ChannelProductVariantGroup apiChnlProdVariantGroup = ChannelProductVariantGroup.newBuilder()
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
    List<OptionGroup_> actChnlOptionGroupList = actChannelProduct.getOptionGroupsList().stream()
        .collect(Collectors.toList());
    List<ChannelProductOptionGroup> apiChnlProdOptionGroupList = new ArrayList<>();
    for (OptionGroup_ actChnlOptionGroup : actChnlOptionGroupList) {

      List<ChannelOption_> actChnlOptionList = actChnlOptionGroup.getChannelOptionList().stream()
          .collect(Collectors.toList());
      List<ChannelProductOption> apiChnlProdOptionList = new ArrayList<>();
      for (ChannelOption_ actChnlOption : actChnlOptionList) {

        ChannelProductOption apiChnlProdOption = ChannelProductOption.newBuilder()
            .setOptnId(actChnlOption.getOptnId())
            .setChnlOptnName(actChnlOption.getChnlOptnName())
            .setChnlOptnType(actChnlOption.getChnlOptnType())
            .setValue(actChnlOption.getChnlOptnValue())
            .build();
        apiChnlProdOptionList.add(apiChnlProdOption);

      }
      // Create a ChannelProductVariantGroup and set its channelProductVariants field
      ChannelProductOptionGroup apiChnlProdOptionGroup = ChannelProductOptionGroup.newBuilder()
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