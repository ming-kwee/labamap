package io.products.channel_product.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Empty;

import akka.actor.ActorSystem;
import io.products.channel_product.api.ChannelProductApi;
import io.products.product.action.ProductActionImpl;
import kalix.javasdk.valueentity.ValueEntityContext;
import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/channel_product/api/channel_product_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class ChannelProduct extends AbstractChannelProduct {
  private static final Logger LOG = LoggerFactory.getLogger(ProductActionImpl.class);
  @SuppressWarnings("unused")
  private final String entityId;

  public ChannelProduct(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public ChannelProductDomain.ChannelProductState emptyState() {
    throw new UnsupportedOperationException("Not implemented yet, replace with your empty entity state");
  }

  @Override
  public Effect<Empty> createChannelProduct(ChannelProductDomain.ChannelProductState currentState,
      ChannelProductApi.ChannelProduct channelProduct) {

    // Create the ActorSystem
    ActorSystem actorSystem = ActorSystem.create("MyActorSystem");
    // Create an instance of Http using the ActorSystem
    Http http = Http.get(actorSystem);
    LOG.info("starting the actorSystem service");
    // Define the HTTP POST endpoints
    List<String> postEndpoints = List.of(
        "https://example.com/endpoint1",
        "https://example.com/endpoint2",
        "https://example.com/endpoint3");

    // Create a list to store the results
    List<CompletionStage<HttpResponse>> postResults = new ArrayList<>();

    // Begin the transaction
    // Start the HTTP POST calls
    try {
      for (String endpoint : postEndpoints) {
        // Create the HTTP POST request
        HttpRequest request = HttpRequest.POST(endpoint);

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
    return effects().error("The command handler for `CreateChannelProduct` is not implemented, yet");
  }
}
