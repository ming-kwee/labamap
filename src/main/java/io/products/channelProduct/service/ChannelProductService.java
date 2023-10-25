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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Empty;
import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.model.headers.RawHeader;
import io.grpc.Status;
import io.grpc.StatusException;
import io.products.channelProduct.api.ChannelProductApi.ChannelProduct;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductAttribute;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductHttpResponse;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductOption;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductOptionGroup;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariant;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariantGroup;
import io.products.shared.utils;

public class ChannelProductService {
  private static final Logger LOG = LoggerFactory.getLogger(ChannelProductService.class);

  
  public static ChannelProductHttpResponse createChannelProduct(ChannelProduct channelProduct) throws StatusException {
    ActorSystem actorSystem = ActorSystem.create("MyActorSystem");
    Http http = Http.get(actorSystem);
    LOG.info("Starting the actorSystem service");
    String postEndpoint = "https://labamap.myshopify.com/admin/api/2023-04/products.jsn";

    String accessToken = "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";
    String requestBody = transformAttributeToJson(channelProduct);
    LOG.info("transformAttributeToJson " + requestBody);

    HttpRequest request = HttpRequest.POST(postEndpoint)
        .addHeader(RawHeader.create("X-Shopify-Access-Token", accessToken))
        .addHeader(RawHeader.create("Content-Type", "application/json"))
        .withEntity(ContentTypes.APPLICATION_JSON, requestBody);

    CompletionStage<HttpResponse> responseStage = http.singleRequest(request)
        .thenApply(response -> {
          return response;
        })
        .exceptionally(ex -> {
          return HttpResponse.create().withStatus(StatusCodes.INTERNAL_SERVER_ERROR);
        });

    try {

      HttpResponse response = responseStage.toCompletableFuture().get();

      LOG.info("LOH " + response.status().intValue());

      if (response.status().intValue() >= 400) {
        ChannelProductHttpResponse cpHttpResponse = ChannelProductHttpResponse.newBuilder()
            .setStatus("FAIL")
            .setDescription("Gagal simpan")
            .build();
        return cpHttpResponse;
      } else {
        ChannelProductHttpResponse cpHttpResponse = ChannelProductHttpResponse.newBuilder()
            .setStatus("OK")
            .setDescription("Berhasil simpan")
            .build();
        return cpHttpResponse;
      }

    } catch (Exception e) {

      ChannelProductHttpResponse cpHttpResponse = ChannelProductHttpResponse.newBuilder()
          .setStatus("EXCEPTION")
          .setDescription(e.getLocalizedMessage())
          .build();
      LOG.info("StatusCodes Exception LOH");
      return cpHttpResponse;
    }
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

      LOG.info("result " + result);
      String json = objectMapper.writeValueAsString(result);

      return json;
    } catch (JsonProcessingException e) {
      e.printStackTrace(); // Handle the exception as needed
      return e.getMessage();

    }
  }

}

// class FailedResponse {
// private HttpResponse response;
// private Exception exception;

// public FailedResponse(HttpResponse response, Exception exception) {
// this.response = response;
// this.exception = exception;
// }

// public HttpResponse getResponse() {
// return response;
// }

// public Exception getException() {
// return exception;
// }
// }
