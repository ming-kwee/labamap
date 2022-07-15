
// Generated by Akka gRPC. DO NOT EDIT.
package io.users.doctor.api;

import akka.actor.ClassicActorSystemProvider;
import akka.stream.Materializer;
import akka.stream.SystemMaterializer;

import akka.grpc.internal.*;
import akka.grpc.GrpcClientSettings;
import akka.grpc.javadsl.AkkaGrpcClient;

import io.grpc.MethodDescriptor;

import static io.users.doctor.api.DoctorService.Serializers.*;

import scala.concurrent.ExecutionContext;

import akka.grpc.AkkaGrpcGenerated;


import akka.grpc.javadsl.SingleResponseRequestBuilder;


@AkkaGrpcGenerated
public abstract class DoctorServiceClient extends DoctorServiceClientPowerApi implements DoctorService, AkkaGrpcClient {
  public static final DoctorServiceClient create(GrpcClientSettings settings, ClassicActorSystemProvider sys) {
    return new DefaultDoctorServiceClient(settings, sys);
  }

  @AkkaGrpcGenerated
  protected final static class DefaultDoctorServiceClient extends DoctorServiceClient {

      private final ClientState clientState;
      private final GrpcClientSettings settings;
      private final io.grpc.CallOptions options;
      private final Materializer mat;
      private final ExecutionContext ec;

      private DefaultDoctorServiceClient(GrpcClientSettings settings, ClassicActorSystemProvider sys) {
        this.settings = settings;
        this.mat = SystemMaterializer.get(sys).materializer();
        this.ec = sys.classicSystem().dispatcher();
        this.clientState = new ClientState(
          settings,
          akka.event.Logging$.MODULE$.apply(sys.classicSystem(), DefaultDoctorServiceClient.class, akka.event.LogSource$.MODULE$.<DefaultDoctorServiceClient>fromAnyClass()),
          sys);
        this.options = NettyClientUtils.callOptions(settings);

        sys.classicSystem().getWhenTerminated().whenComplete((v, e) -> close());
      }

  
    
      private final SingleResponseRequestBuilder<io.users.doctor.api.DoctorApi.Doctor, com.google.protobuf.Empty> createDoctorRequestBuilder(akka.grpc.internal.InternalChannel channel){
        return new JavaUnaryRequestBuilder<>(createDoctorDescriptor, channel, options, settings, ec);
      }
    
  
    
      private final SingleResponseRequestBuilder<io.users.doctor.api.DoctorApi.GetDoctorRequest, io.users.doctor.api.DoctorApi.Doctor> getDoctorRequestBuilder(akka.grpc.internal.InternalChannel channel){
        return new JavaUnaryRequestBuilder<>(getDoctorDescriptor, channel, options, settings, ec);
      }
    
  

      

        /**
         * For access to method metadata use the parameterless version of createDoctor
         */
        public java.util.concurrent.CompletionStage<com.google.protobuf.Empty> createDoctor(io.users.doctor.api.DoctorApi.Doctor request) {
          return createDoctor().invoke(request);
        }

        /**
         * Lower level "lifted" version of the method, giving access to request metadata etc.
         * prefer createDoctor(io.users.doctor.api.DoctorApi.Doctor) if possible.
         */
        
          public SingleResponseRequestBuilder<io.users.doctor.api.DoctorApi.Doctor, com.google.protobuf.Empty> createDoctor()
        
        {
          return createDoctorRequestBuilder(clientState.internalChannel());
        }
      

        /**
         * For access to method metadata use the parameterless version of getDoctor
         */
        public java.util.concurrent.CompletionStage<io.users.doctor.api.DoctorApi.Doctor> getDoctor(io.users.doctor.api.DoctorApi.GetDoctorRequest request) {
          return getDoctor().invoke(request);
        }

        /**
         * Lower level "lifted" version of the method, giving access to request metadata etc.
         * prefer getDoctor(io.users.doctor.api.DoctorApi.GetDoctorRequest) if possible.
         */
        
          public SingleResponseRequestBuilder<io.users.doctor.api.DoctorApi.GetDoctorRequest, io.users.doctor.api.DoctorApi.Doctor> getDoctor()
        
        {
          return getDoctorRequestBuilder(clientState.internalChannel());
        }
      

      
        private static MethodDescriptor<io.users.doctor.api.DoctorApi.Doctor, com.google.protobuf.Empty> createDoctorDescriptor =
          MethodDescriptor.<io.users.doctor.api.DoctorApi.Doctor, com.google.protobuf.Empty>newBuilder()
            .setType(
   MethodDescriptor.MethodType.UNARY 
  
  
  
)
            .setFullMethodName(MethodDescriptor.generateFullMethodName("io.users.doctor.api.DoctorService", "CreateDoctor"))
            .setRequestMarshaller(new ProtoMarshaller<io.users.doctor.api.DoctorApi.Doctor>(DoctorSerializer))
            .setResponseMarshaller(new ProtoMarshaller<com.google.protobuf.Empty>(EmptySerializer))
            .setSampledToLocalTracing(true)
            .build();
        
        private static MethodDescriptor<io.users.doctor.api.DoctorApi.GetDoctorRequest, io.users.doctor.api.DoctorApi.Doctor> getDoctorDescriptor =
          MethodDescriptor.<io.users.doctor.api.DoctorApi.GetDoctorRequest, io.users.doctor.api.DoctorApi.Doctor>newBuilder()
            .setType(
   MethodDescriptor.MethodType.UNARY 
  
  
  
)
            .setFullMethodName(MethodDescriptor.generateFullMethodName("io.users.doctor.api.DoctorService", "GetDoctor"))
            .setRequestMarshaller(new ProtoMarshaller<io.users.doctor.api.DoctorApi.GetDoctorRequest>(GetDoctorRequestSerializer))
            .setResponseMarshaller(new ProtoMarshaller<io.users.doctor.api.DoctorApi.Doctor>(DoctorSerializer))
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



