package io.products.channelProduct.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Empty;
import kalix.javasdk.action.Action.Effect;

import akka.actor.ActorSystem;
import io.products.channelProduct.api.ChannelProductApi;
import io.products.channelProduct.domain.ChannelProductDomain;
import io.products.product.action.ProductActionImpl;
import io.products.product.domain.ProductDomain;
import kalix.javasdk.valueentity.ValueEntityContext;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/channel_product/api/channel_product_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.
import akka.http.javadsl.model.headers.RawHeader;

public class ChannelProduct extends AbstractChannelProduct {
  private static final Logger LOG = LoggerFactory.getLogger(ProductActionImpl.class);
  @SuppressWarnings("unused")
  private final String entityId;

  public ChannelProduct(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public ChannelProductDomain.ChannelProductState emptyState() {
        return ChannelProductDomain.ChannelProductState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> createChannelProduct(ChannelProductDomain.ChannelProductState currentState,
      ChannelProductApi.ChannelProduct channelProduct) {


        
            ActorSystem actorSystem = ActorSystem.create("MyActorSystem");
    // Create an instance of Http using the ActorSystem
    Http http = Http.get(actorSystem);
    LOG.info("starting the actorSystem service");
    // Define the HTTP POST endpoints
    List<String> postEndpoints = List.of(
        "https://labamap.myshopify.com/admin/api/2023-04/products.jso",
        "https://labamap.myshopify.com/admin/api/2023-0/products.jso");
        // "https://example.com/endpoint3");

    // Create a list to store the results
    List<CompletionStage<HttpResponse>> postResults = new ArrayList<>();

    // Begin the transaction
    // Start the HTTP POST calls
    try {
      for (String endpoint : postEndpoints) {
        // Create the HTTP POST request


        String accessToken = "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";

        String requestBody = "{\"product\":{\"title\":\"Burton Custom Freestyle 151\",\"body_html\":\"<strong>Good snowboard!</strong>\",\"vendor\":\"Burton\",\"product_type\":\"Snowboard\",\"status\":\"draft\"}}";

        HttpRequest request = HttpRequest.POST(endpoint)
                .addHeader(RawHeader.create("X-Shopify-Access-Token", accessToken))
                .addHeader(RawHeader.create("Content-Type", "application/json"))
                .withEntity(ContentTypes.APPLICATION_JSON, requestBody);
        // HttpRequest request = HttpRequest.POST(endpoint);

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


    return effects().reply(Empty.getDefaultInstance());
    // return effects().error("The command handler for `CreateChannelProduct` is not implemented, yet");

        // return reject(currentState, command).orElseGet(() -> handle(state, command));

  }




  // teste(){
        // Create the ActorSystem
    // ActorSystem actorSystem = ActorSystem.create("MyActorSystem");
    // // Create an instance of Http using the ActorSystem
    // Http http = Http.get(actorSystem);
    // LOG.info("starting the actorSystem service");
    // // Define the HTTP POST endpoints
    // List<String> postEndpoints = List.of(
    //     "https://labamap.myshopify.com/admin/api/2023-04/products.json",
    //     "https://labamap.myshopify.com/admin/api/2023-0/products.jso");
    //     // "https://example.com/endpoint3");

    // // Create a list to store the results
    // List<CompletionStage<HttpResponse>> postResults = new ArrayList<>();

    // // Begin the transaction
    // // Start the HTTP POST calls
    // try {
    //   for (String endpoint : postEndpoints) {
    //     // Create the HTTP POST request


    //     String accessToken = "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";

    //     String requestBody = "{\"product\":{\"title\":\"Burton Custom Freestyle 151\",\"body_html\":\"<strong>Good snowboard!</strong>\",\"vendor\":\"Burton\",\"product_type\":\"Snowboard\",\"status\":\"draft\"}}";

    //     HttpRequest request = HttpRequest.POST(endpoint)
    //             .addHeader(RawHeader.create("X-Shopify-Access-Token", accessToken))
    //             .addHeader(RawHeader.create("Content-Type", "application/json"))
    //             .withEntity(ContentTypes.APPLICATION_JSON, requestBody);
    //     // HttpRequest request = HttpRequest.POST(endpoint);

    //     // Send the HTTP POST request asynchronously
    //     CompletionStage<HttpResponse> responseStage = http.singleRequest(request);

    //     // Store the completion stage in the list
    //     postResults.add(responseStage);
    //   }

    //   // Wait for all the HTTP POST calls to complete
    //   CompletableFuture<Void> allRequests = CompletableFuture.allOf(
    //       postResults.toArray(new CompletableFuture[0]));

    //   // Handle the completion of all requests
    //   allRequests.thenAccept(ignore -> {
    //     // Check if any of the requests failed
    //     boolean anyFailed = postResults.stream()
    //         .anyMatch(stage -> stage.toCompletableFuture().isCompletedExceptionally());

    //     if (anyFailed) {
    //       System.err.println("An error occurred. Rolling back the transaction.");

    //       // Rollback the transaction if any HTTP POST call fails
    //       // Perform any necessary rollback logic or operations
    //     } else {
    //       System.out.println("All HTTP POST calls succeeded. Committing the transaction.");
    //       // Perform any additional logic or operations for a successful transaction
    //     }
    //   }).toCompletableFuture().join();
    // } finally {
    //   // Cleanup resources, close connections, etc.
    // }


    // return effects().reply(Empty.getDefaultInstance());
    // // return effects().error("The command handler for `CreateChannelProduct` is not implemented, yet");
  // }
}
