package io.products.channelProduct.service.authorization;

import java.util.Map;

import io.products.channelProduct.action.ChannelProductSyncActionApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.headers.RawHeader;

public class BearerToken {
  private static final Logger LOG = LoggerFactory.getLogger(BearerToken.class);

  public static HttpRequest setup_HttpRequest(String URL, String HttpMethod,  Map<String, Object> hashmapMetadata) {


    LOG.info("HIHI 1");
    String integration_HttpRequest_CreateCpAccessTokenHeaderName = null;
    if (hashmapMetadata.containsKey("integration.http_request.create_cp_access_token_header_name")) {
      ChannelProductSyncActionApi.ChannelMetadata channelMetadata = (ChannelProductSyncActionApi.ChannelMetadata) hashmapMetadata
          .get("integration.http_request.create_cp_access_token_header_name");
      integration_HttpRequest_CreateCpAccessTokenHeaderName = (String) channelMetadata.getValue();
    }
    LOG.info("HIHI 2" + integration_HttpRequest_CreateCpAccessTokenHeaderName);
    String accessToken = "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";
    String postEndpoint = URL;

    LOG.info("HIHI 3" + postEndpoint);
    return HttpRequest.POST(postEndpoint)
        .addHeader(RawHeader.create(integration_HttpRequest_CreateCpAccessTokenHeaderName, accessToken))
        .addHeader(RawHeader.create("Content-Type", "application/json"));
  }


}
