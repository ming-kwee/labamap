package io.products.channelProduct.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;
import akka.stream.Materializer;
import akka.stream.SystemMaterializer;
import akka.util.ByteString;
import io.grpc.StatusException;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelMetadata;
import io.products.channelProduct.api.ChannelProductApi.ChannelProduct;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductAttribute;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductHttpResponse;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductOption;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductOptionGroup;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariant;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariantGroup;
import io.products.channelProduct.service.authorization.BearerToken;
import io.products.channelProduct.service.authorization.Oauth1_HMACSHA1;
import io.products.shared.utils;

public class Create_CP_MultiExecution {
  private static final Logger LOG = LoggerFactory.getLogger(Create_CP_MultiExecution.class);

  @SuppressWarnings("unchecked")
  public static CompletionStage<HttpResponse> createAChannelProduct_WithSplitVariants(ChannelProduct channelProduct,
      Map<String, Object> hashmapMetadata, Http http, ActorSystem actorSystem)
      throws StatusException {

    LOG.info("CREATE A CHANNEL PRODUCT WITH SPLIT VARIANTS - Create_CP_WithBearerToken");

    List<CompletionStage<HttpResponse>> postResults = new ArrayList<>();
    Map<String, Object> requestBodies = transformAttributeToJson_WithSplitVariants(channelProduct);

    HttpRequest requestForCreatingProduct = initialSetup_HttpRequest_forCreatingProduct(hashmapMetadata);
    String bodyForProduct = (String) requestBodies.get("product");

    // Access the materializer from the actor system
    Materializer materializer = SystemMaterializer.get(actorSystem).materializer();
    return http.singleRequest(requestForCreatingProduct.withEntity(ContentTypes.APPLICATION_JSON, bodyForProduct))
        .thenCompose(response -> {
          // Create an instance of ByteString
          ByteString emptyByteString = ByteString.fromString("");
          // Extract the response payload
          CompletionStage<ByteString> byteStringCompletionStage = response.entity().getDataBytes().runFold(
              emptyByteString, ByteString::concat, materializer);

          // Convert ByteString to String
          return byteStringCompletionStage.thenApply(ByteString::utf8String)
              .thenCompose(payload -> {
                // Log the response payload
                LOG.info("Response Payload: " + payload);

                HttpRequest requestForCreatingVariants = initialSetup_HttpRequest_forCreatingVariants(hashmapMetadata);
                List<String> variantList = (List<String>) requestBodies.get("variants");
                for (String bodyForVariant : variantList) {

                  LOG.info("CREATE A CHANNEL with variants " + bodyForVariant);
                  CompletionStage<HttpResponse> responseStageForVariant = http
                      .singleRequest(
                          requestForCreatingVariants.withEntity(ContentTypes.APPLICATION_JSON, bodyForVariant));
                  postResults.add(responseStageForVariant);
                }

                // Combine all CompletionStage<HttpResponse> into a single CompletableFuture
                CompletableFuture<Void> allRequests = CompletableFuture.allOf(
                    postResults.toArray(new CompletableFuture[0]));

                // When all requests are completed, handle the result
                return allRequests.thenCompose(ignore -> {
                  // Check if any of the requests failed
                  boolean anyFailed = postResults.stream()
                      .anyMatch(stage -> stage.toCompletableFuture().isCompletedExceptionally());

                  if (anyFailed) {
                    System.err.println("An error occurred. Rolling back the transaction.");
                    return CompletableFuture
                        .completedFuture(HttpResponse.create().withStatus(StatusCodes.INTERNAL_SERVER_ERROR));

                  } else {
                    System.out
                        .println("All HTTP POST calls succeeded. Committing the transaction." + postResults.size());
                    return postResults.get(postResults.size() - 1);
                  }
                });

                // return response;
              });
        });
  }

  @SuppressWarnings("unchecked")
  public static CompletionStage<HttpResponse> createSomeChannelProducts_WithSplitVariants(
      List<ChannelProduct.Builder> channelProductBuilders,
      Map<String, Object> hashmapMetadata, Http http)
      throws StatusException, JsonMappingException, JsonProcessingException {
    LOG.info("CREATE SOME CHANNEL PRODUCTs - Create_CP_WithBearerToken");

    List<CompletionStage<HttpResponse>> postResults = new ArrayList<>();
    List<List<String>> Listof_VariantList = new ArrayList<>();

    /* ___________________________________________________ */
    // Arrange multi-channel products into arrays and merge the
    // array with root objects with a properties name if any.
    /* ___________________________________________________ */
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode root = objectMapper.createObjectNode();
    ArrayNode productsArray = objectMapper.createArrayNode();
    for (ChannelProduct.Builder channelProductBuilder : channelProductBuilders) {
      Map<String, Object> requestBodies = transformAttributeToJson_WithSplitVariants(channelProductBuilder.build());
      String bodyForProduct = (String) requestBodies.get("product");
      productsArray.add(objectMapper.readTree(bodyForProduct));

      List<String> variantList = (List<String>) requestBodies.get("variants");
      Listof_VariantList.add(variantList);
      // productsArray.add(objectMapper.readTree(transform_AttributeToJson(channelProductBuilder.build())));
    }

    String bodyForProducts = "";
    if (hashmapMetadata.containsKey("integration.body_content.root_enclosed_property_name")) {
      ChannelMetadata channelMetadata = (ChannelMetadata) hashmapMetadata
          .get("integration.body_content.root_enclosed_property_name");
      root.set(channelMetadata.getValue(), productsArray);
      bodyForProducts = root.toString();
    } else {
      bodyForProducts = productsArray.toString();
    }

    /* ____________________________________________________ */

    HttpRequest request_forCreatingProduct = initialSetup_HttpRequest_forCreatingProduct(hashmapMetadata);
    CompletionStage<HttpResponse> responseStageForProduct = http
        .singleRequest(request_forCreatingProduct.withEntity(ContentTypes.APPLICATION_JSON, bodyForProducts));
    postResults.add(responseStageForProduct);

    HttpRequest requestForCreatingVariants = initialSetup_HttpRequest_forCreatingVariants(hashmapMetadata);

    for (List<String> variantList : Listof_VariantList) {
      for (String bodyForVariant : variantList) {
        CompletionStage<HttpResponse> responseStageForVariant = http
            .singleRequest(requestForCreatingVariants.withEntity(ContentTypes.APPLICATION_JSON, bodyForVariant));
        postResults.add(responseStageForVariant);
      }
    }
    // Combine all CompletionStage<HttpResponse> into a single CompletableFuture
    CompletableFuture<Void> allRequests = CompletableFuture.allOf(
        postResults.toArray(new CompletableFuture[0]));

    // When all requests are completed, handle the result
    return allRequests.thenCompose(ignore -> {
      // Check if any of the requests failed
      boolean anyFailed = postResults.stream()
          .anyMatch(stage -> stage.toCompletableFuture().isCompletedExceptionally());

      if (anyFailed) {
        System.err.println("An error occurred. Rolling back the transaction.");
        return CompletableFuture.completedFuture(HttpResponse.create().withStatus(StatusCodes.INTERNAL_SERVER_ERROR));

      } else {
        System.out.println("All HTTP POST calls succeeded. Committing the transaction.");
        return postResults.get(postResults.size() - 1);
      }
    });

    // HttpRequest request =
    // initialSetup_HttpRequest_forCreatingProduct(hashmapMetadata);

    // return http.singleRequest(request.withEntity(ContentTypes.APPLICATION_JSON,
    // requestBody))
    // .thenApply(response -> {
    // return response;
    // })
    // .exceptionally(ex -> {
    // return HttpResponse.create().withStatus(StatusCodes.INTERNAL_SERVER_ERROR);
    // });
  }

  // ---------------------------------------------------------------------------------------------------------------
  @SuppressWarnings("unchecked")
  private static Map<String, Object> transformAttributeToJson_WithSplitVariants(ChannelProduct channelProduct) {

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      // Create a map to store the attributes dynamically
      Map<String, Object> parentMap = new HashMap<>();

      // Convert attributes to a map and add to the parent map
      for (ChannelProductAttribute attribute : channelProduct.getChannelProductAttributeList()) {
        utils.addAttribute_toMap(parentMap, attribute.getChnlAttrName(), attribute.getValue(),
            attribute.getChnlAttrType());
      }

      List<Map<String, Object>> variantsGroupList = new ArrayList<>();
      for (ChannelProductVariantGroup variantGroup : channelProduct.getChannelProductVariantGroupList()) {
        Map<String, Object> groupMap = new HashMap<>();
        for (ChannelProductVariant variant : variantGroup.getChannelProductVariantList()) {
          groupMap = utils.addVariantOrOption_toGroupMap(groupMap, variant.getChnlVrntName(), variant.getValue(),
              variant.getChnlVrntType());
        }
        variantsGroupList.add(groupMap);
      }

      List<Map<String, Object>> optionsGroupList = new ArrayList<>();
      for (ChannelProductOptionGroup optionGroup : channelProduct.getChannelProductOptionGroupList()) {
        Map<String, Object> groupMap = new HashMap<>();
        for (ChannelProductOption option : optionGroup.getChannelProductOptionList()) {
          groupMap = utils.addVariantOrOption_toGroupMap(groupMap, option.getChnlOptnName(), option.getValue(),
              option.getChnlOptnType());
        }
        optionsGroupList.add(groupMap);
      }

      List<String> requestBodyForVariants = new ArrayList<String>();
      Map<String, Object> result = parentMap;// new HashMap<>();
      if (variantsGroupList.size() > 0) {
        int deepLevel = 0;
        for (ChannelProductVariant variant : channelProduct.getChannelProductVariantGroupList().get(0)
            .getChannelProductVariantList()) {
          if (!variant.getChnlVrntType().trim().substring(variant.getChnlVrntType().trim().length() - 2).equals("[]")) {
            int lengthOfArray = variant.getChnlVrntName().split("\\.").length;
            deepLevel = lengthOfArray > 0 ? lengthOfArray - 1 : 0;
            break;
          }
        }
        Map<String, Object> temp = utils.mergeMaps(variantsGroupList, deepLevel);
        Map<String, Object> product = (Map<String, Object>) temp.get("product");
        List<Map<String, Object>> variants = (List<Map<String, Object>>) product.get("variants");
        for (Map<String, Object> variant : variants) {
          requestBodyForVariants.add(objectMapper.writeValueAsString(variant));
        }

      }
      if (optionsGroupList.size() > 0) {
        int deepLevel = 0;
        for (ChannelProductOption option : channelProduct.getChannelProductOptionGroupList().get(0)
            .getChannelProductOptionList()) {
          if (!option.getChnlOptnType().trim().substring(option.getChnlOptnType().trim().length() - 2).equals("[]")) {
            int lengthOfArray = option.getChnlOptnName().split("\\.").length;
            deepLevel = lengthOfArray > 0 ? lengthOfArray - 1 : 0;
            break;
          }
        }
        result = utils.finalMergeMaps(result, utils.mergeMaps(optionsGroupList, deepLevel));
      }
      String requestBodyForProduct = objectMapper.writeValueAsString(result);

      Map<String, Object> requestBodies = new HashMap<>();
      requestBodies.put("product", requestBodyForProduct);
      requestBodies.put("variants", requestBodyForVariants);
      return requestBodies;

    } catch (JsonProcessingException e) {
      e.printStackTrace(); // Handle the exception as needed
      // return e.getMessage();
      return null;
    }
  }
  // ---------------------------------------------------------------------------------------------------------------

  private static HttpRequest initialSetup_HttpRequest_forCreatingProduct(Map<String, Object> hashmapMetadata) {

    /* -------- setting up authorization metadata ------- */
    String integration_Security_CreateCpTypeOfAuthorization = null;
    if (hashmapMetadata.containsKey("integration.security.create_cp_type_of_authorization")) {
      ChannelMetadata channelMetadata = (ChannelMetadata) hashmapMetadata
          .get("integration.security.create_cp_type_of_authorization");
      integration_Security_CreateCpTypeOfAuthorization = (String) channelMetadata.getValue();
    } else {
      ChannelProductHttpResponse cpHttpResponse = ChannelProductHttpResponse.newBuilder()
          .setStatus("FAIL")
          .setDescription("type of authorization has not been set")
          .build();
      throw new RuntimeException(cpHttpResponse.getDescription());
    }

    String integration_HttpRequest_CreateCpEndpoint = null;
    if (hashmapMetadata.containsKey("integration.http_request.create_cp_endpoint")) {
      ChannelMetadata channelMetadata = (ChannelMetadata) hashmapMetadata
          .get("integration.http_request.create_cp_endpoint");
      integration_HttpRequest_CreateCpEndpoint = (String) channelMetadata.getValue();
    }

    /* --- the entry to the service world based on type of authorization --- */
    HttpRequest request = null;
    switch (integration_Security_CreateCpTypeOfAuthorization) {
      case "bearer":
        request = BearerToken.setup_HttpRequest(integration_HttpRequest_CreateCpEndpoint, "POST", hashmapMetadata);
        break;
      case "oauth1":
        request = Oauth1_HMACSHA1.setup_HttpRequest(integration_HttpRequest_CreateCpEndpoint, "POST", hashmapMetadata);
        break;
      default:
        break;
    }

    return request;

  }

  private static HttpRequest initialSetup_HttpRequest_forCreatingVariants(Map<String, Object> hashmapMetadata) {

    /* -------- setting up authorization metadata ------- */
    String integration_Security_CreateCpTypeOfAuthorization = null;
    if (hashmapMetadata.containsKey("integration.security.create_cp_type_of_authorization")) {
      ChannelMetadata channelMetadata = (ChannelMetadata) hashmapMetadata
          .get("integration.security.create_cp_type_of_authorization");
      integration_Security_CreateCpTypeOfAuthorization = (String) channelMetadata.getValue();
    } else {
      ChannelProductHttpResponse cpHttpResponse = ChannelProductHttpResponse.newBuilder()
          .setStatus("FAIL")
          .setDescription("type of authorization has not been set")
          .build();
      throw new RuntimeException(cpHttpResponse.getDescription());
    }

    String integration_HttpRequest_CreateCpVariantEndpoint = null;
    if (hashmapMetadata.containsKey("integration.http_request.create_cp_variant_endpoint")) {
      ChannelMetadata channelMetadata = (ChannelMetadata) hashmapMetadata
          .get("integration.http_request.create_cp_variant_endpoint");
      integration_HttpRequest_CreateCpVariantEndpoint = (String) channelMetadata.getValue();
    }

    /* --- the entry to the service world based on type of authorization --- */
    HttpRequest request = null;
    switch (integration_Security_CreateCpTypeOfAuthorization) {
      case "bearer":
        request = BearerToken.setup_HttpRequest(integration_HttpRequest_CreateCpVariantEndpoint, "POST",
            hashmapMetadata);
        break;
      case "oauth1":
        request = Oauth1_HMACSHA1.setup_HttpRequest(integration_HttpRequest_CreateCpVariantEndpoint, "POST",
            hashmapMetadata);
        break;
      default:
        break;
    }

    return request;

  }

}
