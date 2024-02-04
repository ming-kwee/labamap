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



public class Create_WithObjectCP_WithBearerToken {

  private static final Logger LOG = LoggerFactory.getLogger(Create_WithObjectCP_WithBearerToken.class);



    public static CompletionStage<HttpResponse> createChannelProduct(ChannelProduct channelProduct,
      Map<String, Object> hashmapMetadata, Http http )
      throws StatusException {

        LOG.info("METADATAAA" + hashmapMetadata);
        ChannelMetadata endpoint_Metadata = (ChannelMetadata) hashmapMetadata
            .get("integration.http_request.create_channel_product_endpoint");
        LOG.info("ENDPOINT1 " + endpoint_Metadata.getValue());
        ChannelMetadata accessTokenHeaderName_Metadata = (ChannelMetadata) hashmapMetadata
            .get("integration.http_request.create_channel_product_access_token_header_name");
        LOG.info("HEADER1  " + endpoint_Metadata.getValue());
        ChannelMetadata token_Metadata = (ChannelMetadata) hashmapMetadata
            .get("integration.security.create_channel_product_token_from");
            LOG.info("TOKEN1  " + token_Metadata.getValue());
            ChannelMetadata authorization_Metadata = (ChannelMetadata) hashmapMetadata
            .get("integration.security.create_channel_product_authorization");
            LOG.info("AUTH1  " + authorization_Metadata.getValue());
        // if (endpoint_Metadata.getValue().equals("bearer")) {
        // LOG.info("Starting the actorSystem service");
        String postEndpoint = endpoint_Metadata.getValue(); // "https://labamap.myshopify.com/admin/api/2023-04/products.jsonN";
        LOG.info("ENDPOINT ENDPOINT " + postEndpoint);
        // "https://labamap.myshopify.com/admin/api/2023-04/products.jsn";
    
        String accessToken = "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";
        token_Metadata.getValue(); //
        String requestBody = transformAttributeToJson(channelProduct);
        // LOG.info("transformAttributeToJson " + requestBody);
        LOG.info("HEADER HEADER " + accessTokenHeaderName_Metadata.getValue());
    
        HttpRequest request = HttpRequest.POST(postEndpoint)
            .addHeader(RawHeader.create(accessTokenHeaderName_Metadata.getValue(), accessToken))
            .addHeader(RawHeader.create("Content-Type", "application/json"))
            .withEntity(ContentTypes.APPLICATION_JSON, requestBody);
    
        // return CompletionStage<HttpResponse> responseStage = http.singleRequest(request)
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
