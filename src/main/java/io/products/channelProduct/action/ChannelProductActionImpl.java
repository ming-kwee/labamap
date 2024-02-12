package io.products.channelProduct.action;

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
import com.google.protobuf.Empty;
import java.util.function.Function;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelAttribute;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelOption;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelVariant;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelMetadata;
import io.products.channelProduct.action.ChannelProductActionApi.MetadataGroup;
import io.products.channelProduct.action.ChannelProductActionApi.OptionGroup;
import io.products.channelProduct.action.ChannelProductActionApi.VariantGroup;
import io.products.channelProduct.api.ChannelProductApi.ChannelProduct;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductAttribute;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductHttpResponse;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductOption;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductOptionGroup;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariant;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariantGroup;
import io.products.channelProduct.api.ChannelProductApi.DeleteChannelProductRequest;
import io.products.channelProduct.service.ChannelProductService;
import kalix.javasdk.action.ActionCreationContext;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class ChannelProductActionImpl extends AbstractChannelProductAction {
  private static final Logger LOG = LoggerFactory.getLogger(ChannelProductActionImpl.class);

  public ChannelProductActionImpl(ActionCreationContext creationContext) {
  }

  @Override
  public Effect<Empty> createChannelProduct(ChannelProductActionApi.ChannelProducts actChannelProducts) {

    /* _______________________________________________ */
    // Get Metadata and Set to an List<MetadataGroup>
    /* _______________________________________________ */
    List<MetadataGroup> actChnlMetadataGroupList = actChannelProducts.getChannelProductsList().get(0)
        .getMetadataGroupsList().stream()
        .collect(Collectors.toList());
    Map<String, Object> hashmapMetadata = new HashMap<>();
    for (MetadataGroup metadataGroup : actChnlMetadataGroupList) {
      for (ChannelMetadata metadata : metadataGroup.getChannelMetadataList()) {
        hashmapMetadata.put(metadata.getGrouping() + '.' + metadata.getSubGrouping() + '.' + metadata.getKey(),
            metadata);
      }
    }
    /* _______________________________________________ */


   /* _______________________________________________ */
    // Convert Data From Action To Api ChannelProduct
    /* _______________________________________________ */    
    List<ChannelProduct.Builder> channelProductBuilders = new ArrayList<>();
    for (ChannelProductActionApi.ChannelProduct actChannelProduct : actChannelProducts.getChannelProductsList()) {
      channelProductBuilders.add(convert_FromAction_ToApi_ChannelProduct(actChannelProduct));
    }
    /* _______________________________________________ */


    CompletableFuture<Effect<Empty>> effect;
    if (hashmapMetadata.containsKey("integration.body_content.multi_product")) {
      ChannelMetadata bodyContent = (ChannelMetadata) hashmapMetadata.get("integration.body_content.multi_product");
      boolean isMultiProduct = "true".equals(bodyContent.getValue());

      // Log the appropriate message based on whether it's multi-product or not
      LOG.info(isMultiProduct ? "Create Some Channel Products" : "Create A Channel Product");

      // Call the appropriate method based on the boolean value
      effect = isMultiProduct ? createSomeChannelProducts(channelProductBuilders, hashmapMetadata)
          : createAChannelProduct(channelProductBuilders.get(0), hashmapMetadata);
    } else {
      // If the key is not present, default to creating a single channel product
      effect = createAChannelProduct(channelProductBuilders.get(0), hashmapMetadata);
    }
    /* ------------------------------------------------------------- */
    return effects().asyncEffect(effect.exceptionally(NotEmptyAuth()));
    /* ------------------------------------------------------------- */

  }

  /* --------------------------- */
  // Create Some Channel Products
  /* ___________________________ */
  private CompletableFuture<Effect<Empty>> createSomeChannelProducts(
      List<ChannelProduct.Builder> channelProductBuilders, Map<String, Object> hashmapMetadata) {

    List<CompletableFuture<CompletionStage<Empty>>> futures = new ArrayList<>();
    // Start asynchronous processing for each channelProductBuilder
    for (ChannelProduct.Builder channelProductBuilder : channelProductBuilders) {
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
      // (CompletableFuture<Effect<Empty>>) effects().error(e.getMessage());
      effects().error(e.getMessage());
    }

    // Chain the next step after all asynchronous processing is complete
    return allOf.thenCompose(ignored -> {

      // Process the results of the asynchronous tasks
      List<CompletionStage<Empty>> responses = futures.stream()
          .map(CompletableFuture::join) // Get the result of each CompletableFuture
          .collect(Collectors.toList());

      LOG.info("HITUNG RESPONSE" + responses.size());
      boolean anyException = responses.stream()
          .anyMatch(response -> {
            try {
              response.toCompletableFuture().join();
              return false; //No exception
            } catch (Exception e) {
              // Handle the exception as needed
              System.err.println("Exception in response: " + e.getMessage());
              return true; // Exception encountered
            }
          });




      if (anyException) {
        // kesini jika terjadi error ketika didalam looping service itu terjadi kegagalan. 
        throw new RuntimeException("verifikasi.getDescription()");
      } else {
        // All responses completed successfully
        LOG.info("HITUNG SERVICE " + channelProductBuilders.toArray());


        try {
          ChannelProductHttpResponse verifikasi = ChannelProductService
              .createSomeChannelProductsService(channelProductBuilders, hashmapMetadata);

          if (verifikasi.getStatus().equals("OK")) {
              LOG.info("Status Verifikasi " + verifikasi.getStatus());
              return CompletableFuture.completedFuture(effects().reply(Empty.getDefaultInstance()));
          } else {
              LOG.info("kesini " + channelProductBuilders.get(0).getId());
              return components().channelProduct()
                  .deleteChannelProduct(DeleteChannelProductRequest.newBuilder()
                      .setId(channelProductBuilders.get(0).getId())
                      .build())
                  .execute()
                  .thenApplyAsync(deleteResult -> {
                      throw new RuntimeException(verifikasi.getDescription());
                  })
                  .thenComposeAsync(ex -> {
                      return CompletableFuture.<Effect<Empty>>completedFuture(effects().error(((Throwable) ex).getMessage()));
                  });
          }
      } catch (StatusException ex) {
          LOG.info("kesini StatusException " + ex.getMessage());
          return CompletableFuture.<Effect<Empty>>completedFuture(effects().error(((Throwable) ex).getMessage()));
      }

        // -----------------------
        // try {
        // ChannelProductHttpResponse verifikasi = ChannelProductService
        // .createChannelProduct(channelProductBuilder.build(), hashmapMetadata);

        // if (verifikasi.getStatus() == "OK") {
        // return effects().reply(Empty.getDefaultInstance());

        // } else {

        // /* _________________________ */
        // /* ------------------------- */
        // // Delete Channel Product
        // /* _________________________ */
        // /* ------------------------- */
        // LOG.info("kesini " + channelProductBuilder.getId());
        // CompletionStage<Empty> delete_channel_product = components().channelProduct()
        // .deleteChannelProduct(DeleteChannelProductRequest.newBuilder()
        // .setId(channelProductBuilder.getId())
        // .build())
        // .execute();
        // return effects().error(verifikasi.getDescription());

        // }

        // } catch (StatusException ex) {
        // return effects().error(ex.getMessage());
        // }
        // -----------------------

      }

      // return CompletableFuture.completedFuture(effects().reply(Empty.getDefaultInstance()));
    }).thenApply(Function.identity());
  }



  
  // /* --------------------------- */
  // // Create A Channel Product
  // /* ___________________________ */
  private CompletableFuture<Effect<Empty>> createAChannelProduct(
    ChannelProduct.Builder channelProductBuilder, Map<String, Object> hashmapMetadata) {

    LOG.info("CREATE A CHANNEL PRODUCT");
    CompletionStage<Empty> create_channel_product = components().channelProduct()
        .createChannelProduct(channelProductBuilder.build()).execute();

    return (CompletableFuture<Effect<Empty>>) create_channel_product.thenCompose(result -> {
        try {
            ChannelProductHttpResponse verifikasi = ChannelProductService
                .createAChannelProductService(channelProductBuilder.build(), hashmapMetadata);

            if (verifikasi.getStatus().equals("OK")) {
                LOG.info("Status Verifikasi " + verifikasi.getStatus());
                return CompletableFuture.completedFuture(effects().reply(Empty.getDefaultInstance()));
            } else {
                LOG.info("kesini " + channelProductBuilder.getId());
                return components().channelProduct()
                    .deleteChannelProduct(DeleteChannelProductRequest.newBuilder()
                        .setId(channelProductBuilder.getId())
                        .build())
                    .execute()
                    .thenApplyAsync(deleteResult -> {
                        throw new RuntimeException(verifikasi.getDescription());
                    })
                    .thenComposeAsync(ex -> {
                        return CompletableFuture.<Effect<Empty>>completedFuture(effects().error(((Throwable) ex).getMessage()));
                    });
            }
        } catch (StatusException ex) {
            LOG.info("kesini StatusException " + ex.getMessage());
            // return CompletableFuture.completedFuture(effects().error(ex.getMessage()));
            return CompletableFuture.<Effect<Empty>>completedFuture(effects().error(((Throwable) ex).getMessage()));
            // return CompletableFuture.completedFuture(effects().reply(Empty.getDefaultInstance()));
        }
    }).thenApply(Function.identity());
}

  

  /*
   * -----------------------------------------------------------------------------
   */
  private Function<Throwable, ? extends Effect<Empty>> NotEmptyAuth() {
    /* --- jika kesini artinya ada error saat mencreate product ---- */
    /* ------------------------------------------------------------- */
    LOG.info("jika kesini artinya ada error saat mencreate product " );
    return (e) -> 
    effects().error(e.getMessage(), Status.Code.CANCELLED);
    /* ------------------------------------------------------------- */
  }

  /*
   * -----------------------------------------------------------------------------
   */
  private static ChannelProduct.Builder convert_FromAction_ToApi_ChannelProduct(
      ChannelProductActionApi.ChannelProduct actChannelProduct) {

    ChannelProduct.Builder channelProductBuilder = ChannelProduct.newBuilder();
    FieldDescriptor[] fields = channelProductBuilder.getDescriptorForType().getFields()
        .toArray(new FieldDescriptor[0]);

    /* _________________________ */
    /* ------------------------- */
    // Create Channel Attributes
    /* _________________________ */
    /* ------------------------- */
    List<ChannelAttribute> actChnlAttributeList = actChannelProduct.getChannelAttributesList().stream()
        .collect(Collectors.toList());
    List<ChannelProductAttribute> apiChnlProdAttributeList = new ArrayList<>();
    channelProductBuilder.setId(UUID.randomUUID().toString());
    for (ChannelAttribute actChnlAttribute : actChnlAttributeList) {
      // Create Product common fields
      if (actChnlAttribute.getIsCommon() == true) {
        for (FieldDescriptor field : fields) {
          if (field.getName().equals(actChnlAttribute.getChnlAttrName())) {
            // String propName =
            // utils.replaceAfterUnderscore(actChnlAttribute.getChnlAttrName());
            if ("productId".equals(field.getName())) {
              channelProductBuilder.setProductId(actChnlAttribute.getChnlAttrValue());
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
    List<VariantGroup> actChnlVariantGroupList = actChannelProduct.getVariantGroupsList().stream()
        .collect(Collectors.toList());
    List<ChannelProductVariantGroup> apiChnlProdVariantGroupList = new ArrayList<>();
    for (VariantGroup actChnlVariantGroup : actChnlVariantGroupList) {

      List<ChannelVariant> actChnlVariantList = actChnlVariantGroup.getChannelVariantList().stream()
          .collect(Collectors.toList());
      List<ChannelProductVariant> apiChnlProdVariantList = new ArrayList<>();
      for (ChannelVariant actChnlVariant : actChnlVariantList) {

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
    List<OptionGroup> actChnlOptionGroupList = actChannelProduct.getOptionGroupsList().stream()
        .collect(Collectors.toList());
    List<ChannelProductOptionGroup> apiChnlProdOptionGroupList = new ArrayList<>();
    for (OptionGroup actChnlOptionGroup : actChnlOptionGroupList) {

      List<ChannelOption> actChnlOptionList = actChnlOptionGroup.getChannelOptionList().stream()
          .collect(Collectors.toList());
      List<ChannelProductOption> apiChnlProdOptionList = new ArrayList<>();
      for (ChannelOption actChnlOption : actChnlOptionList) {

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

    return channelProductBuilder;
  }

}