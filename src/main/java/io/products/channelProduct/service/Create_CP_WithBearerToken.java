package io.products.channelProduct.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.model.headers.RawHeader;
import io.grpc.StatusException;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelMetadata;
import io.products.channelProduct.api.ChannelProductApi.ChannelProduct;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductAttribute;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductOption;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductOptionGroup;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariant;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariantGroup;
import io.products.shared.utils;

public class Create_CP_WithBearerToken {

  private static final Logger LOG = LoggerFactory.getLogger(Create_CP_WithBearerToken.class);

  public static CompletionStage<HttpResponse> createAChannelProduct(ChannelProduct channelProduct,
      Map<String, Object> hashmapMetadata, Http http)
      throws StatusException {

    LOG.info("CREATE A CHANNEL PRODUCT - Create_CP_WithBearerToken");
    String integration_HttpRequest_CreateCpEndpoint = null;
    if (hashmapMetadata.containsKey("integration.http_request.create_cp_endpoint")) {
      ChannelMetadata channelMetadata = (ChannelMetadata) hashmapMetadata
          .get("integration.http_request.create_cp_endpoint");
      integration_HttpRequest_CreateCpEndpoint = (String) channelMetadata.getValue();
    }

    String integration_HttpRequest_CreateCpAccessTokenHeaderName = null;
    if (hashmapMetadata.containsKey("integration.http_request.create_cp_access_token_header_name")) {
      ChannelMetadata channelMetadata = (ChannelMetadata) hashmapMetadata
          .get("integration.http_request.create_cp_access_token_header_name");
      integration_HttpRequest_CreateCpAccessTokenHeaderName = (String) channelMetadata.getValue();
    }

    String accessToken = "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";
    String postEndpoint = integration_HttpRequest_CreateCpEndpoint;
    String requestBody = transformAttributeToJson(channelProduct);

    HttpRequest request = HttpRequest.POST(postEndpoint)
        .addHeader(RawHeader.create(integration_HttpRequest_CreateCpAccessTokenHeaderName, accessToken))
        .addHeader(RawHeader.create("Content-Type", "application/json"))
        .withEntity(ContentTypes.APPLICATION_JSON, requestBody);

    return http.singleRequest(request)
        .thenApply(response -> {
          return response;
        })
        .exceptionally(ex -> {
          return HttpResponse.create().withStatus(StatusCodes.INTERNAL_SERVER_ERROR);
        });

  }




  public static CompletionStage<HttpResponse> createSomeChannelProducts(List<ChannelProduct.Builder> channelProductBuilders,
      Map<String, Object> hashmapMetadata, Http http)
      throws StatusException {

    LOG.info("CREATE SOME CHANNEL PRODUCTs - Create_CP_WithBearerToken");
    String integration_HttpRequest_CreateCpEndpoint = null;
    if (hashmapMetadata.containsKey("integration.http_request.create_cp_endpoint")) {
      ChannelMetadata channelMetadata = (ChannelMetadata) hashmapMetadata
          .get("integration.http_request.create_cp_endpoint");
      integration_HttpRequest_CreateCpEndpoint = (String) channelMetadata.getValue();
    }

    String integration_HttpRequest_CreateCpAccessTokenHeaderName = null;
    if (hashmapMetadata.containsKey("integration.http_request.create_cp_access_token_header_name")) {
      ChannelMetadata channelMetadata = (ChannelMetadata) hashmapMetadata
          .get("integration.http_request.create_cp_access_token_header_name");
      integration_HttpRequest_CreateCpAccessTokenHeaderName = (String) channelMetadata.getValue();
    }

    List<String> requests = new ArrayList<>();
    for (ChannelProduct.Builder channelProductBuilder : channelProductBuilders) {
      requests.add(transformAttributeToJson(channelProductBuilder.build()));
    }
    String requestBody = requests.toString();

    LOG.info("requestBody for some channel products " + requestBody);
    String accessToken = "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";

    String postEndpoint = integration_HttpRequest_CreateCpEndpoint;
    HttpRequest request = HttpRequest.POST(postEndpoint)
        .addHeader(RawHeader.create(integration_HttpRequest_CreateCpAccessTokenHeaderName, accessToken))
        .addHeader(RawHeader.create("Content-Type", "application/json"))
        .withEntity(ContentTypes.APPLICATION_JSON, requestBody);

    return http.singleRequest(request)
        .thenApply(response -> {
          return response;
        })
        .exceptionally(ex -> {
          return HttpResponse.create().withStatus(StatusCodes.INTERNAL_SERVER_ERROR);
        });

  }


  private static String transformAttributeToJson(ChannelProduct channelProduct) {
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

      // LOG.info("result " + result);
      String json = objectMapper.writeValueAsString(result);

      return json;
    } catch (JsonProcessingException e) {
      e.printStackTrace(); // Handle the exception as needed
      return e.getMessage();

    }
  }

}
