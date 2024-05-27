//package io.products.channelProduct.action;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.CompletionStage;
//import java.util.stream.Collectors;
//import io.grpc.Status;
//import io.grpc.StatusException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import com.google.protobuf.Descriptors.FieldDescriptor;
//
//import java.util.function.Function;
//import io.products.channelProduct.action.ChannelProductActionApi.ChannelAttribute;
//import io.products.channelProduct.action.ChannelProductActionApi.ChannelOption;
//import io.products.channelProduct.action.ChannelProductActionApi.ChannelVariant;
//import io.products.channelProduct.action.ChannelProductActionApi.ChannelMetadata;
//import io.products.channelProduct.action.ChannelProductActionApi.MetadataGroup;
//import io.products.channelProduct.action.ChannelProductActionApi.OptionGroup;
//import io.products.channelProduct.action.ChannelProductActionApi.VariantGroup;
//import io.products.channelProduct.api.ChannelProductApi.ChannelProductAttribute;
//import io.products.channelProduct.api.ChannelProductApi.ChannelProductHttpResponse;
//import io.products.channelProduct.api.ChannelProductApi.ChannelProductOption;
//import io.products.channelProduct.api.ChannelProductApi.ChannelProductOptionGroup;
//import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariant;
//import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariantGroup;
//import io.products.channelProduct.api.ChannelProductApi.CreateChannelProductCommand;
//import io.products.channelProduct.api.ChannelProductApi.DeleteChannelProductCommand;
//import io.products.channelProduct.api.ChannelProductApi.UpdateChannelProductCommand;
//import io.products.channelProduct.service.ChannelProductService;
//import io.products.shared.utils;
//import kalix.javasdk.action.ActionCreationContext;
//
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Executors;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.protobuf.*;
//import com.google.protobuf.util.JsonFormat;
//
//
//public class ChannelProductActionImpl extends AbstractChannelProductAction {
//    private static final Logger LOG = LoggerFactory.getLogger(ChannelProductActionImpl.class);
//
//    public ChannelProductActionImpl(ActionCreationContext creationContext) {
//    }
//
//    @Override
//    public Effect<Empty> createChannelProduct(ChannelProductActionApi.ChannelProducts actChannelProducts) {
//
//        /* _______________________________________________ */
//        // Get Metadata and Set to an List<MetadataGroup>
//        /* _______________________________________________ */
//        List<MetadataGroup> actChnlMetadataGroupList = actChannelProducts.getChannelProductsList().get(0)
//                .getMetadataGroupsList().stream()
//                .collect(Collectors.toList());
//        Map<String, Object> hashmapMetadata = new HashMap<>();
//        for (MetadataGroup metadataGroup : actChnlMetadataGroupList) {
//            for (ChannelMetadata metadata : metadataGroup.getChannelMetadataList()) {
//                hashmapMetadata.put(metadata.getGrouping() + '.' + metadata.getSubGrouping() + '.' + metadata.getKey(),
//                        metadata);
//            }
//        }
//        /* _______________________________________________ */
//
//        /* _______________________________________________ */
//        // Convert Data From Action To Api ChannelProduct
//        /* _______________________________________________ */
//        List<CreateChannelProductCommand.Builder> channelProductBuilders = new ArrayList<>();
//        for (ChannelProductActionApi.ChannelProduct actChannelProduct : actChannelProducts.getChannelProductsList()) {
//            channelProductBuilders
//                    .add(convert_FromAction_ToApi_ChannelProduct(actChannelProduct, actChannelProducts.getEventId()));
//        }
//        /* _______________________________________________ */
//
//        CompletableFuture<Effect<Empty>> effect;
//        if (hashmapMetadata.containsKey("integration.body_content.create_cp_multi_product")) {
//            ChannelMetadata bodyContent = (ChannelMetadata) hashmapMetadata
//                    .get("integration.body_content.create_cp_multi_product");
//            boolean isMultiProduct = "true".equals(bodyContent.getValue());
//
//            // Call the appropriate method based on the boolean value
//            effect = isMultiProduct ? createSomeChannelProducts(channelProductBuilders, hashmapMetadata)
//                    : createAChannelProduct(channelProductBuilders.get(0), hashmapMetadata);
//        } else {
//            // If the key is not present, default to creating a single channel product
//            effect = createAChannelProduct(channelProductBuilders.get(0), hashmapMetadata);
//        }
//        /* ------------------------------------------------------------- */
//        return effects().asyncEffect(effect.exceptionally(ExceptionHandling()));
//        /* ------------------------------------------------------------- */
//
//    }
//
//    /* --------------------------- */
//    // Create Some Channel Products
//    /* ___________________________ */
//    private CompletableFuture<Effect<Empty>> createSomeChannelProducts(
//            List<CreateChannelProductCommand.Builder> channelProductBuilders, Map<String, Object> hashmapMetadata) {
//
//        List<CompletableFuture<CompletionStage<Empty>>> futures = new ArrayList<>();
//        // Start asynchronous processing for each channelProductBuilder
//        for (CreateChannelProductCommand.Builder channelProductBuilder : channelProductBuilders) {
//            CompletableFuture<CompletionStage<Empty>> future = CompletableFuture.supplyAsync(() -> {
//                try {
//                    // Your asynchronous processing logic goes here
//                    LOG.info("CREATE SOME CHANNEL PRODUCTS");
//                    return components().channelProduct().createChannelProduct(channelProductBuilder.build()).execute();
//                } catch (Exception e) {
//                    LOG.info("ERROR when CREATE SOME CHANNEL PRODUCTS " + e.getMessage());
//                    throw new RuntimeException(e); // Handle exceptions appropriately
//                }
//            }, Executors.newCachedThreadPool()); // Specify an executor if needed
//            futures.add(future);
//        }
//
//        // Wait for all asynchronous processing to complete
//        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
//
//        // Wait for all executions to complete
//        try {
//            allOf.get(); // This will block until all executions are completed
//        } catch (InterruptedException | ExecutionException e) {
//            effects().error(e.getMessage());
//        }
//
//        // Chain the next step after all asynchronous processing is complete
//        return allOf.thenCompose(ignored -> {
//
//            // Process the results of the asynchronous tasks
//            List<CompletionStage<Empty>> responses = futures.stream()
//                    .map(CompletableFuture::join) // Get the result of each CompletableFuture
//                    .collect(Collectors.toList());
//
//            String anyException_WhileCreation = "";
//            for (CompletionStage<Empty> response : responses) {
//                try {
//                    response.toCompletableFuture().join();
//                } catch (Exception e) {
//                    anyException_WhileCreation = e.getMessage();
//                    break; // Exit loop as soon as an exception is encountered
//                }
//            }
//
//            if (anyException_WhileCreation != "") {
//                // jika terjadi error saat looping create channel products.
//                return rollbackAllChannelProducts(channelProductBuilders, anyException_WhileCreation);
//            } else {
//                try {
//                    ChannelProductHttpResponse validasi = ChannelProductService
//                            .createSomeChannelProductsService(channelProductBuilders, hashmapMetadata);
//                    if (validasi.getStatus().equals("OK")) {
//                        return CompletableFuture.completedFuture(effects().reply(Empty.getDefaultInstance()));
//                    } else {
//                        // jika terjadi error saat looping create channel products service.
//                        return rollbackAllChannelProducts(channelProductBuilders, validasi.getDescription());
//                    }
//                } catch (StatusException ex) {
//                    // jika terjadi unhandled error.
//                    return CompletableFuture.<Effect<Empty>>completedFuture(effects().error(((Throwable) ex).getMessage()));
//                }
//
//            }
//        }).thenApply(Function.identity());
//    }
//
//    private CompletableFuture<Effect<Empty>> rollbackAllChannelProducts(
//            List<CreateChannelProductCommand.Builder> channelProductBuilders, String anyException_WhileProcess) {
//
//        List<CompletableFuture<CompletionStage<Empty>>> futures = new ArrayList<>();
//        // Start asynchronous soft delete for each channelProductBuilder
//        for (CreateChannelProductCommand.Builder channelProductBuilder : channelProductBuilders) {
//            CompletableFuture<CompletionStage<Empty>> future = CompletableFuture.supplyAsync(() -> {
//                try {
//                    return components().channelProduct().deleteChannelProduct(DeleteChannelProductCommand.newBuilder()
//                                    .setId(channelProductBuilder.getId())
//                                    .build())
//                            .execute();
//                } catch (Exception e) {
//                    throw new RuntimeException(e); // Handle exceptions appropriately
//                }
//            }, Executors.newCachedThreadPool()); // Specify an executor if needed
//            futures.add(future);
//        }
//
//        // Wait for all asynchronous deletion to complete
//        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
//
//        // Wait for all executions to complete
//        try {
//            allOf.get(); // This will block until all executions are completed
//        } catch (InterruptedException | ExecutionException e) {
//            effects().error(e.getMessage());
//        }
//
//        // Chain the next step after all asynchronous deletion is complete
//        return allOf.thenCompose(ignored -> {
//
//            // Process the results of the asynchronous tasks
//            List<CompletionStage<Empty>> responses = futures.stream()
//                    .map(CompletableFuture::join) // Get the result of each CompletableFuture
//                    .collect(Collectors.toList());
//
//            String anyException_WhileDeletion = "";
//            for (CompletionStage<Empty> response : responses) {
//                try {
//                    response.toCompletableFuture().join();
//                } catch (Exception e) {
//                    anyException_WhileDeletion = e.getMessage();
//                    break; // Exit loop as soon as an exception is encountered
//                }
//            }
//            String finalExceptionMessage;
//            if (anyException_WhileDeletion != "") {
//                finalExceptionMessage = anyException_WhileDeletion;
//            } else {
//                finalExceptionMessage = anyException_WhileProcess;
//            }
//            ;
//            return CompletableFuture.<Effect<Empty>>completedFuture(effects().error(finalExceptionMessage));
//
//        });
//    }
//
//    /* --------------------------- */
//    // Create A Channel Product
//    /* ___________________________ */
//    private CompletableFuture<Effect<Empty>> createAChannelProduct(
//            CreateChannelProductCommand.Builder createChannelProductBuilder, Map<String, Object> hashmapMetadata) {
//
//        LOG.info("CREATE A CHANNEL PRODUCT");
//        CompletionStage<Empty> create_channel_product = components().channelProduct()
//                .createChannelProduct(createChannelProductBuilder.build()).execute();
//
//        return (CompletableFuture<Effect<Empty>>) create_channel_product.thenCompose(createResponse -> {
//            try {
//                ChannelProductHttpResponse validasi = ChannelProductService
//                        .createAChannelProductService(createChannelProductBuilder.build(), hashmapMetadata);
//
//                if (validasi.getStatus().equals("OK")) {
//                    Struct struct = convertNumberFieldsToString(validasi.getDataMap(), "id");
//                    try {
//
//                        // Extract values from the JSON string
//                        ObjectMapper mapper = new ObjectMapper();
//                        JsonNode rootNode = mapper.readTree(JsonFormat.printer().print(struct));
//                        String id = rootNode.path("product").path("id").asText();
//
//                        LOG.info("UPDATE A CHANNEL PRODUCT");
//
//
//                        UpdateChannelProductCommand updateChannelProductCommand = UpdateChannelProductCommand.newBuilder()
//                                .setId(createChannelProductBuilder.getId())
//                                .setSku(createChannelProductBuilder.getSku())
//                                .setStoreId(createChannelProductBuilder.getStoreId())
//                                .setChannelId(createChannelProductBuilder.getChannelId())
//                                .clearChannelProductAttribute()
//                                .addAllChannelProductAttribute(createChannelProductBuilder.getChannelProductAttributeList())
//                                .clearChannelProductVariantGroup()
//                                .addAllChannelProductVariantGroup(createChannelProductBuilder.getChannelProductVariantGroupList())
//                                .clearChannelProductOptionGroup()
//                                .addAllChannelProductOptionGroup(createChannelProductBuilder.getChannelProductOptionGroupList())
//                                .setIsDeleted(createChannelProductBuilder.getIsDeleted())
//                                .setEventId(createChannelProductBuilder.getEventId())
//                                .setProductId(id)
//                                .build();
//
//
//                        CompletionStage<Empty> update_channel_product = components().channelProduct()
//                                .updateChannelProduct(updateChannelProductCommand)
//                                .execute();
//
//                        CompletionStage<Effect<Empty>> updateEffect = update_channel_product.thenApply(updateResponse -> {
//                            return effects().reply(Empty.getDefaultInstance());
//                        });
//                        CompletableFuture<Effect<Empty>> completableFutureUpdateEffect = updateEffect
//                                .exceptionally(ExceptionHandling())
//                                //   .exceptionally(throwable -> {
//                                //     // Handle exception, convert to an error effect
//                                //     return effects().error("Update failed: " + throwable.getMessage());
//                                // })
//                                .toCompletableFuture();
//                        return completableFutureUpdateEffect;
//
//                    } catch (InvalidProtocolBufferException e) {
//                        // return
//                        // CompletableFuture.<Effect<Empty>>completedFuture(effects().error(((Throwable)
//                        // e).getMessage()));
//                        return components().channelProduct()
//                                .deleteChannelProduct(DeleteChannelProductCommand.newBuilder()
//                                        .setId(createChannelProductBuilder.getId())
//                                        .build())
//                                .execute()
//                                .thenApplyAsync(deleteResult -> {
//                                    throw new RuntimeException(((Throwable) e).getMessage());
//                                })
//                                .thenComposeAsync(ex -> {
//                                    return CompletableFuture
//                                            .<Effect<Empty>>completedFuture(effects().error(((Throwable) ex).getMessage()));
//                                });
//                    } catch (JsonMappingException e) {
//                        // return
//                        // CompletableFuture.<Effect<Empty>>completedFuture(effects().error(((Throwable)
//                        // e).getMessage()));
//                        return components().channelProduct()
//                                .deleteChannelProduct(DeleteChannelProductCommand.newBuilder()
//                                        .setId(createChannelProductBuilder.getId())
//                                        .build())
//                                .execute()
//                                .thenApplyAsync(deleteResult -> {
//                                    throw new RuntimeException(((Throwable) e).getMessage());
//                                })
//                                .thenComposeAsync(ex -> {
//                                    return CompletableFuture
//                                            .<Effect<Empty>>completedFuture(effects().error(((Throwable) ex).getMessage()));
//                                });
//                    } catch (JsonProcessingException e) {
//                        // return
//                        // CompletableFuture.<Effect<Empty>>completedFuture(effects().error(((Throwable)
//                        // e).getMessage()));
//                        return components().channelProduct()
//                                .deleteChannelProduct(DeleteChannelProductCommand.newBuilder()
//                                        .setId(createChannelProductBuilder.getId())
//                                        .build())
//                                .execute()
//                                .thenApplyAsync(deleteResult -> {
//                                    throw new RuntimeException(((Throwable) e).getMessage());
//                                })
//                                .thenComposeAsync(ex -> {
//                                    return CompletableFuture
//                                            .<Effect<Empty>>completedFuture(effects().error(((Throwable) ex).getMessage()));
//                                });
//                    }
//
//                } else {
//                    LOG.info("kesini " + createChannelProductBuilder.getId());
//                    return components().channelProduct()
//                            .deleteChannelProduct(DeleteChannelProductCommand.newBuilder()
//                                    .setId(createChannelProductBuilder.getId())
//                                    .build())
//                            .execute()
//                            .thenApplyAsync(deleteResult -> {
//                                throw new RuntimeException(validasi.getDescription());
//                            })
//                            .thenComposeAsync(ex -> {
//                                return CompletableFuture.<Effect<Empty>>completedFuture(effects().error(((Throwable) ex).getMessage()));
//                            });
//                    // return rollbackChannelProduct(createChannelProductBuilder.getId(), "");
//                }
//            } catch (StatusException ex) {
//                LOG.info("kesini StatusException " + ex.getMessage());
//                // return CompletableFuture.completedFuture(effects().error(ex.getMessage()));
//                return CompletableFuture.<Effect<Empty>>completedFuture(effects().error(((Throwable) ex).getMessage()));
//                // return
//                // CompletableFuture.completedFuture(effects().reply(Empty.getDefaultInstance()));
//            }
//        }).thenApply(Function.identity());
//    }
//
//
//    /*
//     * -----------------------------------------------------------------------------
//     */
//    private Function<Throwable, ? extends Effect<Empty>> ExceptionHandling() {
//        /* --- jika kesini artinya ada error saat mencreate product ---- */
//        /* ------------------------------------------------------------- */
//        LOG.info("jika kesini artinya ada error");
//        return (e) -> effects().error(e.getMessage(), Status.Code.CANCELLED);
//        /* ------------------------------------------------------------- */
//    }
//
//    /*
//     * -----------------------------------------------------------------------------
//     */
//    private static CreateChannelProductCommand.Builder convert_FromAction_ToApi_ChannelProduct(
//            ChannelProductActionApi.ChannelProduct actChannelProduct,
//            String eventId) {
//
//        CreateChannelProductCommand.Builder channelProductBuilder = CreateChannelProductCommand.newBuilder();
//        FieldDescriptor[] fields = channelProductBuilder.getDescriptorForType().getFields()
//                .toArray(new FieldDescriptor[0]);
//
//        /* _________________________ */
//        /* ------------------------- */
//        // Create Channel Attributes
//        /* _________________________ */
//        /* ------------------------- */
//        List<ChannelAttribute> actChnlAttributeList = actChannelProduct.getChannelAttributesList().stream()
//                .collect(Collectors.toList());
//        List<ChannelProductAttribute> apiChnlProdAttributeList = new ArrayList<>();
//        channelProductBuilder.setId(UUID.randomUUID().toString());
//        for (ChannelAttribute actChnlAttribute : actChnlAttributeList) {
//            // Create Product common fields
//            if (actChnlAttribute.getIsCommon() == true) {
//                for (FieldDescriptor field : fields) {
//
//                    String propName = utils.replaceAfterUnderscore(actChnlAttribute.getChnlAttrName());
//                    LOG.info("PROPNAME " + propName + ", " + field.getName() + ", " + actChnlAttribute.getChnlAttrName());
//
//                    if (field.getName().equals(propName)) {
//                        if ("sku".equals(field.getName())) {
//                            channelProductBuilder.setSku(actChnlAttribute.getChnlAttrValue());
//                        } else if ("storeId".equals(field.getName())) {
//                            channelProductBuilder.setStoreId(actChnlAttribute.getChnlAttrValue());
//                        } else if ("channelId".equals(field.getName())) {
//                            channelProductBuilder.setChannelId(actChnlAttribute.getChnlAttrValue());
//                        }
//                    }
//                }
//            }
//            channelProductBuilder.setEventId(UUID.randomUUID().toString());
//
//            ChannelProductAttribute apiChnlProdAttribute = ChannelProductAttribute.newBuilder()
//                    .setAttrId(actChnlAttribute.getAttrId())
//                    .setChnlAttrName(actChnlAttribute.getChnlAttrName())
//                    .setChnlAttrType(actChnlAttribute.getChnlAttrType())
//                    .setValue(actChnlAttribute.getChnlAttrValue())
//                    .setIsCommon(actChnlAttribute.getIsCommon())
//                    .build();
//            apiChnlProdAttributeList.add(apiChnlProdAttribute);
//        }
//        channelProductBuilder.clearChannelProductAttribute().addAllChannelProductAttribute(apiChnlProdAttributeList);
//
//        /* _________________________ */
//        /* ------------------------- */
//        // Create Channel Variants
//        /* _________________________ */
//        /* ------------------------- */
//        List<VariantGroup> actChnlVariantGroupList = actChannelProduct.getVariantGroupsList().stream()
//                .collect(Collectors.toList());
//        List<ChannelProductVariantGroup> apiChnlProdVariantGroupList = new ArrayList<>();
//        for (VariantGroup actChnlVariantGroup : actChnlVariantGroupList) {
//
//            List<ChannelVariant> actChnlVariantList = actChnlVariantGroup.getChannelVariantList().stream()
//                    .collect(Collectors.toList());
//            List<ChannelProductVariant> apiChnlProdVariantList = new ArrayList<>();
//            for (ChannelVariant actChnlVariant : actChnlVariantList) {
//
//                ChannelProductVariant apiChnlProdVariant = ChannelProductVariant.newBuilder()
//                        .setVrntId(actChnlVariant.getVrntId())
//                        .setChnlVrntName(actChnlVariant.getChnlVrntName())
//                        .setChnlVrntType(actChnlVariant.getChnlVrntType())
//                        .setValue(actChnlVariant.getChnlVrntValue())
//                        .build();
//                apiChnlProdVariantList.add(apiChnlProdVariant);
//
//            }
//            // Create a ChannelProductVariantGroup and set its channelProductVariants field
//            ChannelProductVariantGroup apiChnlProdVariantGroup = ChannelProductVariantGroup.newBuilder()
//                    .addAllChannelProductVariant(apiChnlProdVariantList)
//                    .build();
//            apiChnlProdVariantGroupList.add(apiChnlProdVariantGroup);
//        }
//
//        channelProductBuilder.clearChannelProductVariantGroup()
//                .addAllChannelProductVariantGroup(apiChnlProdVariantGroupList);
//
//        /* _________________________ */
//        /* ------------------------- */
//        // Create Channel Options
//        /* _________________________ */
//        /* ------------------------- */
//        List<OptionGroup> actChnlOptionGroupList = actChannelProduct.getOptionGroupsList().stream()
//                .collect(Collectors.toList());
//        List<ChannelProductOptionGroup> apiChnlProdOptionGroupList = new ArrayList<>();
//        for (OptionGroup actChnlOptionGroup : actChnlOptionGroupList) {
//
//            List<ChannelOption> actChnlOptionList = actChnlOptionGroup.getChannelOptionList().stream()
//                    .collect(Collectors.toList());
//            List<ChannelProductOption> apiChnlProdOptionList = new ArrayList<>();
//            for (ChannelOption actChnlOption : actChnlOptionList) {
//
//                ChannelProductOption apiChnlProdOption = ChannelProductOption.newBuilder()
//                        .setOptnId(actChnlOption.getOptnId())
//                        .setChnlOptnName(actChnlOption.getChnlOptnName())
//                        .setChnlOptnType(actChnlOption.getChnlOptnType())
//                        .setValue(actChnlOption.getChnlOptnValue())
//                        .build();
//                apiChnlProdOptionList.add(apiChnlProdOption);
//
//            }
//            // Create a ChannelProductVariantGroup and set its channelProductVariants field
//            ChannelProductOptionGroup apiChnlProdOptionGroup = ChannelProductOptionGroup.newBuilder()
//                    .addAllChannelProductOption(apiChnlProdOptionList)
//                    .build();
//            apiChnlProdOptionGroupList.add(apiChnlProdOptionGroup);
//        }
//
//        channelProductBuilder.clearChannelProductOptionGroup()
//                .addAllChannelProductOptionGroup(apiChnlProdOptionGroupList);
//        channelProductBuilder.setEventId(eventId);
//
//        return channelProductBuilder;
//    }
//
//    private static Struct convertNumberFieldsToString(Map<String, Value> fieldsMap, String... fieldsToConvert) {
//        Struct.Builder structBuilder = Struct.newBuilder();
//
//        for (Map.Entry<String, Value> entry : fieldsMap.entrySet()) {
//            String key = entry.getKey();
//            Value value = entry.getValue();
//
//            if (shouldConvert(key, fieldsToConvert) && value.hasNumberValue()) {
//                BigDecimal numberValue = BigDecimal.valueOf(value.getNumberValue());
//                structBuilder.putFields(key, Value.newBuilder().setStringValue(numberValue.toPlainString()).build());
//            } else if (value.hasStructValue()) {
//                structBuilder.putFields(key, Value.newBuilder().setStructValue(
//                        convertNumberFieldsToString(value.getStructValue().getFieldsMap(), fieldsToConvert)).build());
//            } else if (value.hasListValue()) {
//                ListValue.Builder listBuilder = ListValue.newBuilder();
//                for (Value listItem : value.getListValue().getValuesList()) {
//                    if (listItem.hasStructValue()) {
//                        listBuilder.addValues(Value.newBuilder().setStructValue(
//                                convertNumberFieldsToString(listItem.getStructValue().getFieldsMap(), fieldsToConvert)).build());
//                    } else {
//                        listBuilder.addValues(listItem);
//                    }
//                }
//                structBuilder.putFields(key, Value.newBuilder().setListValue(listBuilder.build()).build());
//            } else {
//                structBuilder.putFields(key, value);
//            }
//        }
//
//        return structBuilder.build();
//    }
//
//    private static boolean shouldConvert(String key, String[] fieldsToConvert) {
//        for (String field : fieldsToConvert) {
//            if (key.equals(field)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//}