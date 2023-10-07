package io.products.channelProduct.service;

import java.io.IOException;
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
import akka.http.javadsl.model.headers.RawHeader;
import io.products.channelProduct.action.ChannelProductActionImpl;
import io.products.channelProduct.api.ChannelProductApi.ChannelProduct;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductAttribute;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductOption;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductOptionGroup;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariant;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariantGroup;
import kalix.javasdk.action.Action.Effect;
import io.products.shared.utils;

public class ChannelProductService {

  private static final Logger LOG = LoggerFactory.getLogger(ChannelProductService.class);

  public static Empty createChannelProduct(ChannelProduct channelProduct) {

    ActorSystem actorSystem = ActorSystem.create("MyActorSystem");
    // Create an instance of Http using the ActorSystem
    Http http = Http.get(actorSystem);
    LOG.info("starting the actorSystem service");
    // Define the HTTP POST endpoints
    // List<String> postEndpoints =
    // List.of("https://labamap.myshopify.com/admin/api/2023-04/products.json");
    List<String> postEndpoints = List.of("https://asia-southeast2-labamap-ab9a2.cloudfunctions.net/insertDataChannel",
        "https://labamap.myshopify.com/admin/api/2023-04/products.son");

    // "https://example.com/endpoint3");

    // Create a list to store the results
    List<CompletionStage<HttpResponse>> postResults = new ArrayList<>();

    // Begin the transaction
    // Start the HTTP POST calls
    try {
      for (String endpoint : postEndpoints) {
        // Create the HTTP POST request
        LOG.info("transformAttributeToJson " + transformAttributeToJson(channelProduct));
        String accessToken = "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";

        String requestBody = transformAttributeToJson(channelProduct);
        // "{\"product\":{\"title\":\"Burton Custom Freestyle
        // 151\",\"body_html\":\"<strong>Good
        // snowboard!</strong>\",\"vendor\":\"Burton\",\"product_type\":\"Snowboard\",\"status\":\"draft\"}}";

        HttpRequest request = HttpRequest.POST(endpoint)
            .addHeader(RawHeader.create("X-Shopify-Access-Token", accessToken))
            .addHeader(RawHeader.create("Content-Type", "application/json"))
            .withEntity(ContentTypes.APPLICATION_JSON, requestBody);

        // Send the HTTP POST request asynchronously
        CompletionStage<HttpResponse> responseStage = http.singleRequest(request);

        // Store the completion stage in the list
        postResults.add(responseStage);
      }

      // Wait for all the HTTP POST calls to complete
      CompletableFuture<Void> allRequests = CompletableFuture.allOf(
          postResults.toArray(new CompletableFuture[0]));

      // Handle the completion of all requests
      allRequests.thenAccept(ignore -> {
        // Check if any of the requests failed
        boolean anyFailed = postResults.stream()
            .anyMatch(stage -> stage.toCompletableFuture().isCompletedExceptionally());

        if (anyFailed) {
          System.err.println("An error occurred. Rolling back the transaction.");

          // Rollback the transaction if any HTTP POST call fails
          // Perform any necessary rollback logic or operations
        } else {
          System.out.println("All HTTP POST calls succeeded. Committing the transaction.");
          // Perform any additional logic or operations for a successful transaction
        }
      }).toCompletableFuture().join();
    } finally {
      // Cleanup resources, close connections, etc.
    }

    return Empty.getDefaultInstance();

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

      Map<String, Object> result =  parentMap;//new HashMap<>();
      if (variantsGroupList.size() > 0) {
        int deepLevel = 0;
        for (ChannelProductVariant variant : channelProduct.getChannelProductVariantGroupList().get(0)
            .getChannelProductVariantList()) {
          if (!variant.getChnlVrntType().trim().substring(variant.getChnlVrntType().trim().length() - 2).equals("[]")) {
            int lengthOfArray = variant.getChnlVrntName().split("\\.").length;
            deepLevel = lengthOfArray > 0 ? lengthOfArray - 1: 0; 
            break;
          }
        }
        // result = utils.finalMergeMaps(parentMap, utils.mergeMaps(variantsGroupList, deepLevel));
        result = utils.finalMergeMaps(result, utils.mergeMaps(variantsGroupList, deepLevel));
      }
      if (optionsGroupList.size() > 0) {
        int deepLevel = 0;
        for (ChannelProductOption option : channelProduct.getChannelProductOptionGroupList().get(0)
            .getChannelProductOptionList()) {
          if (!option.getChnlOptnType().trim().substring(option.getChnlOptnType().trim().length() - 2).equals("[]")) {
            int lengthOfArray = option.getChnlOptnName().split("\\.").length;
            deepLevel = lengthOfArray > 0 ? lengthOfArray - 1: 0; 
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
      return "";
    }
  }

}
