package io.products.channelProduct.service;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpResponse;
import io.grpc.StatusException;
import io.products.channelProduct.api.ChannelProductApi.ChannelProduct;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductHttpResponse;


public class ChannelProductService {
  private static final Logger LOG = LoggerFactory.getLogger(ChannelProductService.class);

  public static ChannelProductHttpResponse createChannelProduct(ChannelProduct channelProduct,
  Map<String, Object> hashmapMetadata)
      throws StatusException {
    ActorSystem actorSystem = ActorSystem.create("MyActorSystem");
    Http http = Http.get(actorSystem);

    try {

      HttpResponse response = Create_WithObjectCP_WithBearerToken
      .createChannelProduct(channelProduct, hashmapMetadata, http)
      .toCompletableFuture().get();
      // LOG.info("LOH " + response.status().intValue());
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


}

