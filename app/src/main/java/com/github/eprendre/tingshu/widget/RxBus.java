//package com.github.eprendre.tingshu.widget;
//
//import io.reactivex.Flowable;
//import io.reactivex.processors.FlowableProcessor;
//import io.reactivex.processors.PublishProcessor;
//import kotlin.Metadata;
//import kotlin.jvm.internal.Intrinsics;
//import org.jetbrains.annotations.NotNull;
//
//@Metadata(d1 = {"\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bÇ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0006\u001a\u00020\u0007J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0001J\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\fJ \u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\r0\f\"\u0004\b\u0000\u0010\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\r0\u000fR\u001c\u0010\u0003\u001a\u0010\u0012\f\u0012\n \u0005*\u0004\u0018\u00010\u00010\u00010\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, k = 1, mv = {1, 6, 0}, xi = 48)
//public final class RxBus {
//    @NotNull
//    public static final RxBus INSTANCE = new RxBus();
//    @NotNull
//    private static final FlowableProcessor<Object> bus;
//
//    static {
//        FlowableProcessor<Object> serialized = PublishProcessor.create().toSerialized();
//        Intrinsics.checkNotNullExpressionValue(serialized, "create<Any>().toSerialized()");
//        bus = serialized;
//    }
//
//    private RxBus() {
//    }
//
//    public final boolean hasSubscribers() {
//        return bus.hasSubscribers();
//    }
//
//    public final void post(@NotNull Object any) {
//        Intrinsics.checkNotNullParameter(any, "any");
//        bus.onNext(any);
//    }
//
//    @NotNull
//    public final Flowable<Object> toFlowable() {
//        return bus;
//    }
//
//    @NotNull
//    public final <T> Flowable<T> toFlowable(@NotNull Class<T> tClass) {
//        Intrinsics.checkNotNullParameter(tClass, "tClass");
//        Flowable<T> flowable = bus.ofType(tClass);
//        Intrinsics.checkNotNullExpressionValue(flowable, "bus.ofType(tClass)");
//        return flowable;
//    }
//}
