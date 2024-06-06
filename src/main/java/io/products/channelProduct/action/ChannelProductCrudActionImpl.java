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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.protobuf.Descriptors.FieldDescriptor;

import java.util.function.Function;

import io.products.channelProduct.api.ChannelProductApi.ChannelProductAttribute;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductOption;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductOptionGroup;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariant;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariantGroup;
import io.products.channelProduct.api.ChannelProductApi.CreateChannelProductCommand;
import io.products.channelProduct.api.ChannelProductApi.DeleteChannelProductCommand;
import io.products.shared.utils;
import kalix.javasdk.action.ActionCreationContext;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import io.products.channelProduct.action.ChannelProductSyncAction;
import com.google.protobuf.*;


public class ChannelProductCrudActionImpl extends AbstractChannelProductCrudAction {
    private static final Logger LOG = LoggerFactory.getLogger(ChannelProductCrudActionImpl.class);

    public ChannelProductCrudActionImpl(ActionCreationContext creationContext) {
    }

    @Override
    public Effect<ChannelProductSyncActionApi.ChannelProducts> createChannelProduct(ChannelProductSyncActionApi.ChannelProducts syncChannelProducts) {

        /* _______________________________________________ */
        // Get Metadata and Set to List<MetadataGroup>
        /* _______________________________________________ */
        List<ChannelProductSyncActionApi.MetadataGroup> syncChnlMetadataGroupList = syncChannelProducts.getChannelProductsList().get(0)
                .getMetadataGroupsList().stream()
                .collect(Collectors.toList());
        Map<String, Object> hashmapMetadata = new HashMap<>();
        for (ChannelProductSyncActionApi.MetadataGroup metadataGroup : syncChnlMetadataGroupList) {
            for (ChannelProductSyncActionApi.ChannelMetadata metadata : metadataGroup.getChannelMetadataList()) {
                hashmapMetadata.put(metadata.getGrouping() + '.' + metadata.getSubGrouping() + '.' + metadata.getKey(),
                        metadata);
            }
        }
        /* _______________________________________________ */



        /* _______________________________________________ */
        // Convert Data From Action To Api ChannelProduct
        /* _______________________________________________ */
        List<CreateChannelProductCommand.Builder> channelProductBuilders = new ArrayList<>();
        for (ChannelProductSyncActionApi.ChannelProduct syncChannelProduct : syncChannelProducts.getChannelProductsList()) {
            channelProductBuilders
                    .add(convert_FromAction_ToApi_ChannelProduct(syncChannelProduct, syncChannelProducts.getEventId()));
        }
        /* _______________________________________________ */




        /* _______________________________________________ */
        // Conditioning to create A channel or Some Channel
        /* _______________________________________________ */
        CompletableFuture<Effect<ChannelProductSyncActionApi.ChannelProducts>> effect;
        if (hashmapMetadata.containsKey("integration.body_content.create_cp_multi_product")) {
            ChannelProductSyncActionApi.ChannelMetadata bodyContent = (ChannelProductSyncActionApi.ChannelMetadata) hashmapMetadata
                    .get("integration.body_content.create_cp_multi_product");
            boolean isMultiProduct = "true".equals(bodyContent.getValue());

            // Call the appropriate method based on the boolean value
            effect = !isMultiProduct ? createAChannelProduct(channelProductBuilders.get(0), hashmapMetadata, syncChannelProducts)
                    : createSomeChannelProducts(channelProductBuilders, hashmapMetadata, syncChannelProducts);
        } else {
            effect = createSomeChannelProducts(channelProductBuilders, hashmapMetadata, syncChannelProducts);
        }

        /* ------------------------------------------------------------- */
        return effects().asyncEffect(effect.exceptionally(ExceptionHandling()));
        /* ------------------------------------------------------------- */
    }


    /* --------------------------- */
    // Create A Channel Product
    /* ___________________________ */
    private CompletableFuture<Effect<ChannelProductSyncActionApi.ChannelProducts>> createAChannelProduct(
            CreateChannelProductCommand.Builder createChannelProductBuilder, Map<String, Object> hashmapMetadata, ChannelProductSyncActionApi.ChannelProducts syncChannelProducts) {

        LOG.info("CREATE A CHANNEL PRODUCT");
        CompletionStage<Empty> create_channel_product = components().channelProduct()
                .createChannelProduct(createChannelProductBuilder.build()).execute();

        return (CompletableFuture<Effect<ChannelProductSyncActionApi.ChannelProducts>>) create_channel_product.thenApply(x -> {
            return effects().reply(syncChannelProducts);
        });
    }

    private Function<Throwable, ? extends Effect<ChannelProductSyncActionApi.ChannelProducts>> ExceptionHandling() {
        /* --- jika kesini artinya ada error saat mencreate product ---- */
        /* ------------------------------------------------------------- */
        LOG.info("jika kesini artinya ada error");
        return (e) -> effects().error(e.getMessage(), Status.Code.CANCELLED);
        /* ------------------------------------------------------------- */
    }


    /* --------------------------- */
    // Create Some Channel Products
    /* ___________________________ */
    private CompletableFuture<Effect<ChannelProductSyncActionApi.ChannelProducts>> createSomeChannelProducts(
            List<CreateChannelProductCommand.Builder> channelProductBuilders, Map<String, Object> hashmapMetadata, ChannelProductSyncActionApi.ChannelProducts syncChannelProducts) {

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
                return CompletableFuture.completedFuture(effects().reply(syncChannelProducts));
            }
        });
    }

    private CompletableFuture<Effect<ChannelProductSyncActionApi.ChannelProducts>> rollbackAllChannelProducts(
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
            return CompletableFuture.<Effect<ChannelProductSyncActionApi.ChannelProducts>>completedFuture(effects().error(finalExceptionMessage));

        });
    }


    /*
     * -----------------------------------------------------------------------------
     */
    private static CreateChannelProductCommand.Builder convert_FromAction_ToApi_ChannelProduct(
            ChannelProductSyncActionApi.ChannelProduct syncChannelProduct,
            String eventId) {

        CreateChannelProductCommand.Builder channelProductBuilder = CreateChannelProductCommand.newBuilder();
        FieldDescriptor[] fields = channelProductBuilder.getDescriptorForType().getFields()
                .toArray(new FieldDescriptor[0]);

        /* _________________________ */
        /* ------------------------- */
        // Create Channel Attributes
        /* _________________________ */
        /* ------------------------- */
        List<ChannelProductSyncActionApi.ChannelAttribute> syncChnlAttributeList = syncChannelProduct.getChannelAttributesList().stream()
                .collect(Collectors.toList());
        List<ChannelProductAttribute> apiChnlProdAttributeList = new ArrayList<>();
        channelProductBuilder.setId(UUID.randomUUID().toString());
        for (ChannelProductSyncActionApi.ChannelAttribute syncChnlAttribute : syncChnlAttributeList) {
            // Create Product common fields
            if (syncChnlAttribute.getIsCommon() == true) {
                for (FieldDescriptor field : fields) {

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

            ChannelProductAttribute apiChnlProdAttribute = ChannelProductAttribute.newBuilder()
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
                .collect(Collectors.toList());
        List<ChannelProductVariantGroup> apiChnlProdVariantGroupList = new ArrayList<>();
        for (ChannelProductSyncActionApi.VariantGroup syncChnlVariantGroup : syncChnlVariantGroupList) {

            List<ChannelProductSyncActionApi.ChannelVariant> syncChnlVariantList = syncChnlVariantGroup.getChannelVariantList().stream()
                    .collect(Collectors.toList());
            List<ChannelProductVariant> apiChnlProdVariantList = new ArrayList<>();
            for (ChannelProductSyncActionApi.ChannelVariant syncChnlVariant : syncChnlVariantList) {

                ChannelProductVariant apiChnlProdVariant = ChannelProductVariant.newBuilder()
                        .setVrntId(syncChnlVariant.getVrntId())
                        .setChnlVrntName(syncChnlVariant.getChnlVrntName())
                        .setChnlVrntType(syncChnlVariant.getChnlVrntType())
                        .setValue(syncChnlVariant.getChnlVrntValue())
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
        List<ChannelProductSyncActionApi.OptionGroup> syncChnlOptionGroupList = syncChannelProduct.getOptionGroupsList().stream()
                .collect(Collectors.toList());
        List<ChannelProductOptionGroup> apiChnlProdOptionGroupList = new ArrayList<>();
        for (ChannelProductSyncActionApi.OptionGroup syncChnlOptionGroup : syncChnlOptionGroupList) {

            List<ChannelProductSyncActionApi.ChannelOption> syncChnlOptionList = syncChnlOptionGroup.getChannelOptionList().stream()
                    .collect(Collectors.toList());
            List<ChannelProductOption> apiChnlProdOptionList = new ArrayList<>();
            for (ChannelProductSyncActionApi.ChannelOption syncChnlOption : syncChnlOptionList) {

                ChannelProductOption apiChnlProdOption = ChannelProductOption.newBuilder()
                        .setOptnId(syncChnlOption.getOptnId())
                        .setChnlOptnName(syncChnlOption.getChnlOptnName())
                        .setChnlOptnType(syncChnlOption.getChnlOptnType())
                        .setValue(syncChnlOption.getChnlOptnValue())
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