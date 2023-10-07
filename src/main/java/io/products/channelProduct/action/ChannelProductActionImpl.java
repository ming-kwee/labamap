package io.products.channelProduct.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import io.grpc.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Empty;

import java.util.function.Function;
import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.headers.RawHeader;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelAttribute;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelOption;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelVariant;
import io.products.channelProduct.action.ChannelProductActionApi.OptionGroup;
import io.products.channelProduct.action.ChannelProductActionApi.VariantGroup;
import io.products.channelProduct.api.ChannelProductApi;
import io.products.channelProduct.api.ChannelProductApi.ChannelProduct;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductAttribute;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductOption;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductOptionGroup;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariant;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariantGroup;
import io.products.channelProduct.service.ChannelProductService;
import io.products.product.api.ProductApi;
import kalix.javasdk.DeferredCall;
import kalix.javasdk.action.Action.Effect;
import kalix.javasdk.action.ActionCreationContext;
import scala.collection.View.Iterate;
import scala.util.parsing.json.JSON;
import io.products.shared.utils;


// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.node.ArrayNode;
// import com.fasterxml.jackson.databind.node.ObjectNode;

public class ChannelProductActionImpl extends AbstractChannelProductAction {
  private static final Logger LOG = LoggerFactory.getLogger(ChannelProductActionImpl.class);

  public ChannelProductActionImpl(ActionCreationContext creationContext) {
  }

  @Override
  public Effect<Empty> createChannelProduct(ChannelProductActionApi.ChannelProduct actChannelProduct) {
    ChannelProduct.Builder channelProductBuilder = ChannelProduct.newBuilder();
    FieldDescriptor[] fields = channelProductBuilder.getDescriptorForType().getFields().toArray(new FieldDescriptor[0]);

    /* ------------------------- */
    // Create Channel Attributes
    /* ------------------------- */
    List<ChannelAttribute> actChnlAttributeList = actChannelProduct.getChannelAttributesList().stream()
        .collect(Collectors.toList());
    List<ChannelProductAttribute> apiChnlProdAttributeList = new ArrayList<>();
    for (ChannelAttribute actChnlAttribute : actChnlAttributeList) {

      // Create Product common fields
      if (actChnlAttribute.getIsCommon() == true) {
        for (FieldDescriptor field : fields) {
          if (field.getName().equals(actChnlAttribute.getChnlAttrName())) {
            String propName = utils.replaceAfterUnderscore(actChnlAttribute.getChnlAttrName());
            if ("id".equals(field.getName())) {
              channelProductBuilder.setId(actChnlAttribute.getChnlAttrValue());
            } else if ("product_id".equals(field.getName())) {
              channelProductBuilder.setProductId(actChnlAttribute.getChnlAttrValue());
            } else if ("channel_id".equals(field.getName())) {
              channelProductBuilder.setChannelId(actChnlAttribute.getChnlAttrValue());
            }
          }
        }
      }

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

    /* ------------------------- */
    // Create Channel Variants
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

    /* ------------------------- */
    // Create Channel Options
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

    channelProductBuilder.clearChannelProductOptionGroup().addAllChannelProductOptionGroup(apiChnlProdOptionGroupList);

    /* ------------------------- */
    // Create Channel Product
    /* ------------------------- */
    CompletionStage<Empty> create_channel_product = components().channelProduct()
        .createChannelProduct(channelProductBuilder.build()).execute();
    CompletionStage<Effect<Empty>> effect = create_channel_product.thenApply(x -> {

      return effects().reply(ChannelProductService.createChannelProduct(channelProductBuilder.build()));

    });
    /* ------------------------------------------------------------- */
    return effects().asyncEffect(effect.exceptionally(NotEmptyAuth()));
    /* ------------------------------------------------------------- */
    // ===================================================================

  }

  private Function<Throwable, ? extends Effect<Empty>> NotEmptyAuth() {
    /* --- jika kesini artinya ada error saat mencreate product ---- */
    /* ------------------------------------------------------------- */
    return (e) -> effects().error(
        e.getMessage(),
        Status.Code.CANCELLED);
  }

  // private Effect<Empty> outboundChannelProduct(ChannelProduct channelProduct) {

  //   ActorSystem actorSystem = ActorSystem.create("MyActorSystem");
  //   // Create an instance of Http using the ActorSystem
  //   Http http = Http.get(actorSystem);
  //   LOG.info("starting the actorSystem service");
  //   // Define the HTTP POST endpoints
  //   // List<String> postEndpoints =
  //   // List.of("https://labamap.myshopify.com/admin/api/2023-04/products.json");
  //   List<String> postEndpoints = List.of("https://asia-southeast2-labamap-ab9a2.cloudfunctions.net/insertDataChannel",
  //       "https://labamap.myshopify.com/admin/api/2023-04/products.json");

  //   // "https://example.com/endpoint3");

  //   // Create a list to store the results
  //   List<CompletionStage<HttpResponse>> postResults = new ArrayList<>();

  //   // Begin the transaction
  //   // Start the HTTP POST calls
  //   try {
  //     for (String endpoint : postEndpoints) {
  //       // Create the HTTP POST request
  //       LOG.info("transformAttributeToJson " + transformAttributeToJson(channelProduct));
  //       String accessToken = "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";

  //       String requestBody = transformAttributeToJson(channelProduct);
  //       // "{\"product\":{\"title\":\"Burton Custom Freestyle
  //       // 151\",\"body_html\":\"<strong>Good
  //       // snowboard!</strong>\",\"vendor\":\"Burton\",\"product_type\":\"Snowboard\",\"status\":\"draft\"}}";

  //       HttpRequest request = HttpRequest.POST(endpoint)
  //           .addHeader(RawHeader.create("X-Shopify-Access-Token", accessToken))
  //           .addHeader(RawHeader.create("Content-Type", "application/json"))
  //           .withEntity(ContentTypes.APPLICATION_JSON, requestBody);

  //       // Send the HTTP POST request asynchronously
  //       CompletionStage<HttpResponse> responseStage = http.singleRequest(request);

  //       // Store the completion stage in the list
  //       postResults.add(responseStage);
  //     }

  //     // Wait for all the HTTP POST calls to complete
  //     CompletableFuture<Void> allRequests = CompletableFuture.allOf(
  //         postResults.toArray(new CompletableFuture[0]));

  //     // Handle the completion of all requests
  //     allRequests.thenAccept(ignore -> {
  //       // Check if any of the requests failed
  //       boolean anyFailed = postResults.stream()
  //           .anyMatch(stage -> stage.toCompletableFuture().isCompletedExceptionally());

  //       if (anyFailed) {
  //         System.err.println("An error occurred. Rolling back the transaction.");

  //         // Rollback the transaction if any HTTP POST call fails
  //         // Perform any necessary rollback logic or operations
  //       } else {
  //         System.out.println("All HTTP POST calls succeeded. Committing the transaction.");
  //         // Perform any additional logic or operations for a successful transaction
  //       }
  //     }).toCompletableFuture().join();
  //   } finally {
  //     // Cleanup resources, close connections, etc.
  //   }

  //   return effects().reply(Empty.getDefaultInstance());

  // }

  // private static String replaceAfterUnderscore(String input) {
  //   StringBuilder result = new StringBuilder();
  //   boolean convertNext = false;
  //   for (char c : input.toCharArray()) {
  //     if (c == '_') {
  //       convertNext = true;
  //     } else if (convertNext) {
  //       result.append(Character.toUpperCase(c));
  //       convertNext = false;
  //     } else {
  //       result.append(c);
  //     }
  //   }
  //   return result.toString() + "_";
  // }

  // private static String transformAttributeToJson(ChannelProduct channelProduct) {
  //   ObjectMapper objectMapper = new ObjectMapper();

  //   try {

  //     // Create a map to store the attributes dynamically
  //     Map<String, Object> parentMap = new HashMap<>();
  //     Map<String, Object> mergedMap = new HashMap<>();

  //     // Convert attributes to a map and add to the parent map
  //     for (ChannelProductAttribute attribute : channelProduct.getChannelProductAttributeList()) {
  //       addAttributeToParentMap(parentMap, attribute.getChnlAttrName(), attribute.getValue(),
  //           attribute.getChnlAttrType());
  //     }

  //     List<Map<String, Object>> variantsGroupList = new ArrayList<>();
  //     for (ChannelProductVariantGroup variantGroup : channelProduct.getChannelProductVariantGroupList()) {
  //       Map<String, Object> groupMap = new HashMap<>();
  //       for (ChannelProductVariant variant : variantGroup.getChannelProductVariantList()) {
  //         groupMap = addVariantToGroupMap(groupMap, variant.getChnlVrntName(), variant.getValue(),
  //             variant.getChnlVrntType());
  //       }
  //       variantsGroupList.add(groupMap);
  //     }

  //     List<Map<String, Object>> optionsGroupList = new ArrayList<>();
  //     for (ChannelProductOptionGroup optionGroup : channelProduct.getChannelProductOptionGroupList()) {
  //       Map<String, Object> groupMap = new HashMap<>();
  //       for (ChannelProductOption option : optionGroup.getChannelProductOptionList()) {
  //         groupMap = addVariantToGroupMap(groupMap, option.getChnlOptnName(), option.getValue(),
  //             option.getChnlOptnType());
  //       }
  //       optionsGroupList.add(groupMap);
  //     }


  //     Map<String, Object> result = new HashMap<>();

  //     LOG.info("variantsGroupList " + variantsGroupList);
  //     LOG.info("optionsGroupList " + optionsGroupList);

  //     result = finalMergeMaps(parentMap, mergeMaps(variantsGroupList));
  //     result = finalMergeMaps(result, mergeMaps(optionsGroupList));

  //     String json = objectMapper.writeValueAsString(result);

  //     return json;
  //   } catch (JsonProcessingException e) {
  //     e.printStackTrace(); // Handle the exception as needed
  //     return "";
  //   }
  // }

  // private static void addAttributeToParentMap(Map<String, Object> parentMap, String attribute, String value,
  //     String type) {
  //   String[] attributeParts = attribute.split("\\.");
  //   Map<String, Object> currentMap = parentMap;

  //   for (int i = 0; i < attributeParts.length - 1; i++) {
  //     currentMap.putIfAbsent(attributeParts[i], new HashMap<>());
  //     currentMap = (Map<String, Object>) currentMap.get(attributeParts[i]);
  //   }
  //   currentMap.put(attributeParts[attributeParts.length - 1], typeConverter(value, type));
  // }

  // private static int countNestedLevels(Map<String, Object> map, int currentLevel) {
  //   int maxLevel = currentLevel;
  //   for (Map.Entry<String, Object> entry : map.entrySet()) {
  //     Object value = entry.getValue();
  //     if (value instanceof Map) {
  //       int childLevel = countNestedLevels((Map<String, Object>) value, currentLevel + 1);
  //       maxLevel = Math.max(maxLevel, childLevel);
  //     }
  //   }
  //   return maxLevel;
  // }

  // private static void collectKeys(Map<String, Object> map, List<String> allKeys) {
  //   for (Map.Entry<String, Object> entry : map.entrySet()) {
  //     String key = entry.getKey();
  //     Object value = entry.getValue();

  //     // Add the current key to the list
  //     allKeys.add(key);

  //     if (value instanceof Map) {
  //       // Recursively traverse nested maps
  //       collectKeys((Map<String, Object>) value, allKeys);
  //     }
  //   }
  // }

  // public static Map<String, Object> mergeMaps(List<Map<String, Object>> listOfMaps) {
  //   Map<String, Object> mergedMap = new HashMap<>();
  //   List<Map<String, Object>> mergedVariants = new ArrayList<>();

  //   List<String> allKeys = new ArrayList<>();
  //   int nofLvl = 0;
  //   for (Map<String, Object> map : listOfMaps) {
  //     collectKeys(map, allKeys);
  //     nofLvl = countNestedLevels(map, 0);

  //     Map<String, Object> currentMap = map;

  //     for (int i = 0; i < nofLvl; i++) {
  //       currentMap = (Map<String, Object>) currentMap.get(allKeys.get(i));
  //     }
  //     LOG.info("currentMap " + currentMap);

  //     if (currentMap instanceof Map) {
  //       mergedVariants.add((Map<String, Object>) currentMap);
  //     } else if (currentMap instanceof List) {
  //       mergedVariants.addAll((List<Map<String, Object>>) currentMap);
  //     }
  //   }

  //   // Initialize a temporary map
  //   Map<String, Object> currentMap = mergedMap;

  //   if (nofLvl == 0) {
  //     currentMap.put("", mergedVariants);
  //   } else if (nofLvl == 1) {
  //     currentMap.put(allKeys.get(0), mergedVariants);
  //   } else {
  //     // Iterate through the keys to create nested maps
  //     for (int i = 0; i < nofLvl - 1; i++) {
  //       currentMap.put(allKeys.get(i), new HashMap<>());
  //       currentMap = (Map<String, Object>) currentMap.get(allKeys.get(i));
  //       LOG.info("mergedMap MERGE MAP " + allKeys.get(i) + " -> " + mergedMap);
  //     }

  //     // Finally, put the data at the leaf level
  //     currentMap.put(allKeys.get(nofLvl - 1), mergedVariants);
  //   }

  //   LOG.info("PARENT MERGE MAP" + mergedMap);
  //   return mergedMap;
  // }

  // private static Map<String, Object> addVariantToGroupMap(Map<String, Object> groupMap, String variant, String value,
  //     String type) {
  //   String[] variantParts = variant.split("\\.");
  //   Map<String, Object> currentMap = groupMap;

  //   for (int i = 0; i < variantParts.length - 1; i++) {
  //     currentMap.putIfAbsent(variantParts[i], new HashMap<>());
  //     currentMap = (Map<String, Object>) currentMap.get(variantParts[i]);
  //   }
  //   currentMap.put(variantParts[variantParts.length - 1], typeConverter(value, type));

  //   return groupMap; // Return the modified groupMap
  // }

  // private static Map<String, Object> finalMergeMaps(Map<String, Object> map1, Map<String, Object> map2) {
  //   Map<String, Object> mergedMap = new HashMap<>(map1);

  //   for (Map.Entry<String, Object> entry : map2.entrySet()) {
  //     String key = entry.getKey();
  //     Object value = entry.getValue();

  //     if (mergedMap.containsKey(key)) {
  //       Object existingValue = mergedMap.get(key);

  //       if (existingValue instanceof Map && value instanceof Map) {
  //         // If both values are maps, recursively merge them
  //         Map<String, Object> mergedValue = finalMergeMaps((Map<String, Object>) existingValue,
  //             (Map<String, Object>) value);
  //         mergedMap.put(key, mergedValue);
  //       } else if (existingValue instanceof Map) {
  //         // If existing value is a map and new value is not, keep the existing map
  //       } else if (value instanceof Map) {
  //         // If new value is a map and existing value is not, replace with the new map
  //         mergedMap.put(key, value);
  //       } else {
  //         // If both values are not maps, replace with the new value
  //         mergedMap.put(key, value);
  //       }
  //     } else {
  //       // If the key doesn't exist in the merged map, add it
  //       mergedMap.put(key, value);
  //     }
  //   }

  //   return mergedMap;
  // }

  // public static Object typeConverter(String value, String type) {
  //   if ("string".equalsIgnoreCase(type)) {
  //     return value;
  //   } else if ("int".equalsIgnoreCase(type)) {
  //     try {
  //       return Integer.parseInt(value);
  //     } catch (NumberFormatException e) {
  //       // Handle the case where the value is not a valid integer
  //       return 0; // You can return a default value or handle the error as needed
  //     }
  //   } else if ("boolean".equalsIgnoreCase(type)) {
  //     return Boolean.parseBoolean(value);
  //   } else if ("float".equalsIgnoreCase(type)) {
  //     try {
  //       return Float.parseFloat(value);
  //     } catch (NumberFormatException e) {
  //       // Handle the case where the value is not a valid float
  //       return 0.0f; // You can return a default value or handle the error as needed
  //     }
  //   } else if ("object".equalsIgnoreCase(type)) {
  //     // Parse the value as an Object (you might need custom logic for this)
  //     return parseObject(value);
  //   } else if ("object[]".equalsIgnoreCase(type)) {
  //     // Parse the value as an array of Objects (you might need custom logic for this)
  //     return parseObjectArray(value);
  //   } else if ("string[]".equalsIgnoreCase(type)) {
  //     // Parse the value as an array of Objects (you might need custom logic for this)
  //     return parseObjectArray(value);
  //   } else {
  //     // Handle unsupported types or throw an exception
  //     throw new IllegalArgumentException("Unsupported attribute type: " + type);
  //   }
  // }

  // // Custom method to parse an Object
  // private static Object parseObject(String value) {
  //   // Implement your custom logic to parse the value as an Object
  //   // For example, you can deserialize a JSON string into an Object
  //   // Here's a simplified example using JSON:
  //   try {
  //     ObjectMapper objectMapper = new ObjectMapper();
  //     return objectMapper.readValue(value, Object.class);
  //   } catch (IOException e) {
  //     // Handle parsing errors
  //     return null; // Or handle the error as needed
  //   }
  // }

  // // Custom method to parse an array of Objects
  // private static Object[] parseObjectArray(String value) {
  //   // Implement your custom logic to parse the value as an array of Objects
  //   // For example, you can deserialize a JSON array into an Object[] array
  //   // Here's a simplified example using JSON:
  //   try {
  //     ObjectMapper objectMapper = new ObjectMapper();
  //     return objectMapper.readValue(value, Object[].class);
  //   } catch (IOException e) {
  //     // Handle parsing errors
  //     return null; // Or handle the error as needed
  //   }
  // }


}