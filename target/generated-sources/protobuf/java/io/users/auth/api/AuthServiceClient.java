
// Generated by Akka gRPC. DO NOT EDIT.
package io.users.auth.api;

import akka.actor.ClassicActorSystemProvider;
import akka.stream.Materializer;
import akka.stream.SystemMaterializer;

import akka.grpc.internal.*;
import akka.grpc.GrpcClientSettings;
import akka.grpc.javadsl.AkkaGrpcClient;

import io.grpc.MethodDescriptor;

import static io.users.auth.api.AuthService.Serializers.*;

import scala.concurrent.ExecutionContext;

import akka.grpc.AkkaGrpcGenerated;


import akka.grpc.javadsl.SingleResponseRequestBuilder;


@AkkaGrpcGenerated
public abstract class AuthServiceClient extends AuthServiceClientPowerApi implements AuthService, AkkaGrpcClient {
  public static final AuthServiceClient create(GrpcClientSettings settings, ClassicActorSystemProvider sys) {
    return new DefaultAuthServiceClient(settings, sys);
  }

  @AkkaGrpcGenerated
  protected final static class DefaultAuthServiceClient extends AuthServiceClient {

      private final ClientState clientState;
      private final GrpcClientSettings settings;
      private final io.grpc.CallOptions options;
      private final Materializer mat;
      private final ExecutionContext ec;

      private DefaultAuthServiceClient(GrpcClientSettings settings, ClassicActorSystemProvider sys) {
        this.settings = settings;
        this.mat = SystemMaterializer.get(sys).materializer();
        this.ec = sys.classicSystem().dispatcher();
        this.clientState = new ClientState(
          settings,
          akka.event.Logging$.MODULE$.apply(sys.classicSystem(), DefaultAuthServiceClient.class, akka.event.LogSource$.MODULE$.<DefaultAuthServiceClient>fromAnyClass()),
          sys);
        this.options = NettyClientUtils.callOptions(settings);

        sys.classicSystem().getWhenTerminated().whenComplete((v, e) -> close());
      }

  
    
      private final SingleResponseRequestBuilder<io.users.auth.action.AuthActionApi.Auth, com.google.protobuf.Empty> registerRequestBuilder(akka.grpc.internal.InternalChannel channel){
        return new JavaUnaryRequestBuilder<>(registerDescriptor, channel, options, settings, ec);
      }
    
  
    
      private final SingleResponseRequestBuilder<io.users.auth.api.AuthApi.GetLoginRequest, io.users.auth.action.AuthActionApi.Auth> loginRequestBuilder(akka.grpc.internal.InternalChannel channel){
        return new JavaUnaryRequestBuilder<>(loginDescriptor, channel, options, settings, ec);
      }
    
  
    
      private final SingleResponseRequestBuilder<io.users.auth.action.AuthActionApi.Auth, com.google.protobuf.Empty> updateUserRequestBuilder(akka.grpc.internal.InternalChannel channel){
        return new JavaUnaryRequestBuilder<>(updateUserDescriptor, channel, options, settings, ec);
      }
    
  

      

        /**
         * For access to method metadata use the parameterless version of register
         */
        public java.util.concurrent.CompletionStage<com.google.protobuf.Empty> register(io.users.auth.action.AuthActionApi.Auth request) {
          return register().invoke(request);
        }

        /**
         * Lower level "lifted" version of the method, giving access to request metadata etc.
         * prefer register(io.users.auth.action.AuthActionApi.Auth) if possible.
         */
        
          public SingleResponseRequestBuilder<io.users.auth.action.AuthActionApi.Auth, com.google.protobuf.Empty> register()
        
        {
          return registerRequestBuilder(clientState.internalChannel());
        }
      

        /**
         * For access to method metadata use the parameterless version of login
         */
        public java.util.concurrent.CompletionStage<io.users.auth.action.AuthActionApi.Auth> login(io.users.auth.api.AuthApi.GetLoginRequest request) {
          return login().invoke(request);
        }

        /**
         * Lower level "lifted" version of the method, giving access to request metadata etc.
         * prefer login(io.users.auth.api.AuthApi.GetLoginRequest) if possible.
         */
        
          public SingleResponseRequestBuilder<io.users.auth.api.AuthApi.GetLoginRequest, io.users.auth.action.AuthActionApi.Auth> login()
        
        {
          return loginRequestBuilder(clientState.internalChannel());
        }
      

        /**
         * For access to method metadata use the parameterless version of updateUser
         */
        public java.util.concurrent.CompletionStage<com.google.protobuf.Empty> updateUser(io.users.auth.action.AuthActionApi.Auth request) {
          return updateUser().invoke(request);
        }

        /**
         * Lower level "lifted" version of the method, giving access to request metadata etc.
         * prefer updateUser(io.users.auth.action.AuthActionApi.Auth) if possible.
         */
        
          public SingleResponseRequestBuilder<io.users.auth.action.AuthActionApi.Auth, com.google.protobuf.Empty> updateUser()
        
        {
          return updateUserRequestBuilder(clientState.internalChannel());
        }
      

      
        private static MethodDescriptor<io.users.auth.action.AuthActionApi.Auth, com.google.protobuf.Empty> registerDescriptor =
          MethodDescriptor.<io.users.auth.action.AuthActionApi.Auth, com.google.protobuf.Empty>newBuilder()
            .setType(
   MethodDescriptor.MethodType.UNARY 
  
  
  
)
            .setFullMethodName(MethodDescriptor.generateFullMethodName("io.users.auth.api.AuthService", "Register"))
            .setRequestMarshaller(new ProtoMarshaller<io.users.auth.action.AuthActionApi.Auth>(AuthSerializer))
            .setResponseMarshaller(new ProtoMarshaller<com.google.protobuf.Empty>(EmptySerializer))
            .setSampledToLocalTracing(true)
            .build();
        
        private static MethodDescriptor<io.users.auth.api.AuthApi.GetLoginRequest, io.users.auth.action.AuthActionApi.Auth> loginDescriptor =
          MethodDescriptor.<io.users.auth.api.AuthApi.GetLoginRequest, io.users.auth.action.AuthActionApi.Auth>newBuilder()
            .setType(
   MethodDescriptor.MethodType.UNARY 
  
  
  
)
            .setFullMethodName(MethodDescriptor.generateFullMethodName("io.users.auth.api.AuthService", "Login"))
            .setRequestMarshaller(new ProtoMarshaller<io.users.auth.api.AuthApi.GetLoginRequest>(GetLoginRequestSerializer))
            .setResponseMarshaller(new ProtoMarshaller<io.users.auth.action.AuthActionApi.Auth>(AuthSerializer))
            .setSampledToLocalTracing(true)
            .build();
        
        private static MethodDescriptor<io.users.auth.action.AuthActionApi.Auth, com.google.protobuf.Empty> updateUserDescriptor =
          MethodDescriptor.<io.users.auth.action.AuthActionApi.Auth, com.google.protobuf.Empty>newBuilder()
            .setType(
   MethodDescriptor.MethodType.UNARY 
  
  
  
)
            .setFullMethodName(MethodDescriptor.generateFullMethodName("io.users.auth.api.AuthService", "UpdateUser"))
            .setRequestMarshaller(new ProtoMarshaller<io.users.auth.action.AuthActionApi.Auth>(AuthSerializer))
            .setResponseMarshaller(new ProtoMarshaller<com.google.protobuf.Empty>(EmptySerializer))
            .setSampledToLocalTracing(true)
            .build();
        

      /**
       * Initiates a shutdown in which preexisting and new calls are cancelled.
       */
      public java.util.concurrent.CompletionStage<akka.Done> close() {
        return clientState.closeCS() ;
      }

     /**
      * Returns a CompletionState that completes successfully when shutdown via close()
      * or exceptionally if a connection can not be established after maxConnectionAttempts.
      */
      public java.util.concurrent.CompletionStage<akka.Done> closed() {
        return clientState.closedCS();
      }
  }

}


