package io.products.channelProduct.service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lazada.lazop.util.ApiException;

import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;
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
import io.products.channelProduct.service.authorization.Oauth2_SHA256;
import io.products.shared.utils;

public class Create_CP_SingleExecution {
  private static final Logger LOG = LoggerFactory.getLogger(Create_CP_SingleExecution.class);

  public static CompletionStage<HttpResponse> createAChannelProduct(ChannelProduct channelProduct,
      Map<String, Object> hashmapMetadata, Http http)
      throws StatusException, ApiException, JsonMappingException, JsonProcessingException, InvalidKeyException, NoSuchAlgorithmException, InterruptedException {
    // LOG.info("CREATE A CHANNEL PRODUCT - Create_CP_WithBearerToken");
    HttpRequest request = initialSetup_HttpRequest(hashmapMetadata);

    String requestBody = transform_AttributeToJson(channelProduct);
    return http.singleRequest(request.withEntity(ContentTypes.APPLICATION_JSON, requestBody))
        .thenApply(response -> {

          LOG.info("HALORESPONSE " + response);
          return response;
        })
        .exceptionally(ex -> {
          return HttpResponse.create().withStatus(StatusCodes.INTERNAL_SERVER_ERROR);
        });
  }

  public static CompletionStage<HttpResponse> createSomeChannelProducts(
      List<ChannelProduct.Builder> channelProductBuilders,
      Map<String, Object> hashmapMetadata, Http http)
      throws StatusException, JsonMappingException, JsonProcessingException, ApiException, InvalidKeyException, NoSuchAlgorithmException, InterruptedException {

    LOG.info("CREATE SOME CHANNEL PRODUCTs - Create_CP_WithBearerToken");
    /* ___________________________________________________ */
    // Arrange multi-channel products into arrays and merge the
    // array with root objects with a properties name if any.
    /* ___________________________________________________ */
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode root = objectMapper.createObjectNode();
    ArrayNode productsArray = objectMapper.createArrayNode();
    for (ChannelProduct.Builder channelProductBuilder : channelProductBuilders) {
      productsArray.add(objectMapper.readTree(transform_AttributeToJson(channelProductBuilder.build())));
    }
    String requestBody = "";
    if (hashmapMetadata.containsKey("integration.body_content.root_enclosed_property_name")) {
      ChannelMetadata channelMetadata = (ChannelMetadata) hashmapMetadata
          .get("integration.body_content.root_enclosed_property_name");
      root.set(channelMetadata.getValue(), productsArray);
      requestBody = root.toString();
    } else {
      requestBody = productsArray.toString();
    }
    /* ____________________________________________________ */
    // String accessToken = "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";
    HttpRequest request = initialSetup_HttpRequest(hashmapMetadata);

    return http.singleRequest(request.withEntity(ContentTypes.APPLICATION_JSON, requestBody))
        .thenApply(response -> {
          return response;
        })
        .exceptionally(ex -> {
          return HttpResponse.create().withStatus(StatusCodes.INTERNAL_SERVER_ERROR);
        });
  }

  // ---------------------------------------------------------------------------------------------------------------
  private static String transform_AttributeToJson(ChannelProduct channelProduct) {
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
        result = utils.finalMergeMaps(result, utils.mergeMaps(variantsGroupList, deepLevel));
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

      String json = objectMapper.writeValueAsString(result);

      return json;
    } catch (JsonProcessingException e) {
      e.printStackTrace(); // Handle the exception as needed
      return e.getMessage();

    }
  }
  // ---------------------------------------------------------------------------------------------------------------


  private static HttpRequest initialSetup_HttpRequest(Map<String, Object> hashmapMetadata) throws ApiException, JsonMappingException, JsonProcessingException, InvalidKeyException, NoSuchAlgorithmException, InterruptedException {


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
        case "oauth2":
          try {
            request = Oauth2_SHA256.setup_HttpRequest(integration_HttpRequest_CreateCpEndpoint, "POST", hashmapMetadata);
          } catch (UnsupportedEncodingException e) {
            System.out.println("ELOLL: " + e.getMessage());
            e.printStackTrace();
          }
        break;
        default:
          break;
      }

    return request;
  }

}
