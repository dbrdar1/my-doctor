package ba.unsa.etf.grpc;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.4.0)",
    comments = "Source: SystemEventsService.proto")
public final class SystemEventsServiceGrpc {

  private SystemEventsServiceGrpc() {}

  public static final String SERVICE_NAME = "ba.unsa.etf.grpc.SystemEventsService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<ba.unsa.etf.grpc.ActionRequest,
      ba.unsa.etf.grpc.ActionResponse> METHOD_REGISTRUJ_AKCIJU =
      io.grpc.MethodDescriptor.<ba.unsa.etf.grpc.ActionRequest, ba.unsa.etf.grpc.ActionResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "ba.unsa.etf.grpc.SystemEventsService", "registrujAkciju"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              ba.unsa.etf.grpc.ActionRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              ba.unsa.etf.grpc.ActionResponse.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SystemEventsServiceStub newStub(io.grpc.Channel channel) {
    return new SystemEventsServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SystemEventsServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new SystemEventsServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SystemEventsServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new SystemEventsServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class SystemEventsServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void registrujAkciju(ba.unsa.etf.grpc.ActionRequest request,
        io.grpc.stub.StreamObserver<ba.unsa.etf.grpc.ActionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_REGISTRUJ_AKCIJU, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_REGISTRUJ_AKCIJU,
            asyncUnaryCall(
              new MethodHandlers<
                ba.unsa.etf.grpc.ActionRequest,
                ba.unsa.etf.grpc.ActionResponse>(
                  this, METHODID_REGISTRUJ_AKCIJU)))
          .build();
    }
  }

  /**
   */
  public static final class SystemEventsServiceStub extends io.grpc.stub.AbstractStub<SystemEventsServiceStub> {
    private SystemEventsServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SystemEventsServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SystemEventsServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SystemEventsServiceStub(channel, callOptions);
    }

    /**
     */
    public void registrujAkciju(ba.unsa.etf.grpc.ActionRequest request,
        io.grpc.stub.StreamObserver<ba.unsa.etf.grpc.ActionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_REGISTRUJ_AKCIJU, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class SystemEventsServiceBlockingStub extends io.grpc.stub.AbstractStub<SystemEventsServiceBlockingStub> {
    private SystemEventsServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SystemEventsServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SystemEventsServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SystemEventsServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public ba.unsa.etf.grpc.ActionResponse registrujAkciju(ba.unsa.etf.grpc.ActionRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_REGISTRUJ_AKCIJU, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class SystemEventsServiceFutureStub extends io.grpc.stub.AbstractStub<SystemEventsServiceFutureStub> {
    private SystemEventsServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SystemEventsServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SystemEventsServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SystemEventsServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ba.unsa.etf.grpc.ActionResponse> registrujAkciju(
        ba.unsa.etf.grpc.ActionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_REGISTRUJ_AKCIJU, getCallOptions()), request);
    }
  }

  private static final int METHODID_REGISTRUJ_AKCIJU = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final SystemEventsServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(SystemEventsServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REGISTRUJ_AKCIJU:
          serviceImpl.registrujAkciju((ba.unsa.etf.grpc.ActionRequest) request,
              (io.grpc.stub.StreamObserver<ba.unsa.etf.grpc.ActionResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class SystemEventsServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ba.unsa.etf.grpc.SystemEventsServiceOuterClass.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (SystemEventsServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SystemEventsServiceDescriptorSupplier())
              .addMethod(METHOD_REGISTRUJ_AKCIJU)
              .build();
        }
      }
    }
    return result;
  }
}
