package io.products.channelProduct.service.authorization;

import java.util.Map;
import org.apache.http.auth.AUTH;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.headers.RawHeader;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class Oauth1_HMACSHA1 {
  private static final Logger LOG = LoggerFactory.getLogger(Oauth1_HMACSHA1.class);


  public static HttpRequest setup_HttpRequest(String URL, String HttpMethod, Map<String, Object> hashmapMetadata) {

    String postEndpoint = URL;
    // OAuth 1.0 credentials
    String consumerKey = "ck_175b1e990d83b0cfdd7ffac7f1073081269329e1";
    String consumerSecret = "cs_1d39574ec96e6f67d4980a5799c392168a6876d7";

    OAuthConsumer oAuthConsumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
    HttpUriRequest request = new HttpPost(postEndpoint);
    try {
      oAuthConsumer.sign(request);
    } catch (OAuthMessageSignerException | OAuthExpectationFailedException | OAuthCommunicationException e) {
      e.printStackTrace();
    }

    String authHeader = request.getFirstHeader(AUTH.WWW_AUTH_RESP).getValue();

    LOG.info("authHeader " + authHeader);

    return HttpRequest.POST(postEndpoint)
        .addHeader(RawHeader.create("Authorization", authHeader))
        .addHeader(RawHeader.create("Content-Type", "application/json"));
  }

}
