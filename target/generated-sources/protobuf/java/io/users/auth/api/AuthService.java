
// Generated by Akka gRPC. DO NOT EDIT.
package io.users.auth.api;

import akka.grpc.ProtobufSerializer;
import akka.grpc.javadsl.GoogleProtobufSerializer;

import akka.grpc.AkkaGrpcGenerated;


public interface AuthService {
  
  
  java.util.concurrent.CompletionStage<com.google.protobuf.Empty> register(io.users.auth.action.AuthActionApi.Auth in);
  
  
  java.util.concurrent.CompletionStage<io.users.auth.action.AuthActionApi.Auth> login(io.users.auth.api.AuthApi.GetLoginRequest in);
  
  
  java.util.concurrent.CompletionStage<com.google.protobuf.Empty> updateUser(io.users.auth.action.AuthActionApi.Auth in);
  

  static String name = "io.users.auth.api.AuthService";
  static akka.grpc.ServiceDescription description = new akka.grpc.internal.ServiceDescriptionImpl(name, AuthApi.getDescriptor());

  @AkkaGrpcGenerated
  public static class Serializers {
    
      public static ProtobufSerializer<io.users.auth.action.AuthActionApi.Auth> AuthSerializer = new GoogleProtobufSerializer<>(io.users.auth.action.AuthActionApi.Auth.parser());
    
      public static ProtobufSerializer<io.users.auth.api.AuthApi.GetLoginRequest> GetLoginRequestSerializer = new GoogleProtobufSerializer<>(io.users.auth.api.AuthApi.GetLoginRequest.parser());
    
      public static ProtobufSerializer<com.google.protobuf.Empty> EmptySerializer = new GoogleProtobufSerializer<>(com.google.protobuf.Empty.parser());
    
  }
}