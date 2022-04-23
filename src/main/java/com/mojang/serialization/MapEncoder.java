package com.mojang.serialization;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public interface MapEncoder<A> extends Keyable {
   <T> RecordBuilder<T> encode(A var1, DynamicOps<T> var2, RecordBuilder<T> var3);

   default <T> RecordBuilder<T> compressedBuilder(DynamicOps<T> ops) {
      return ops.compressMaps() ? makeCompressedBuilder(ops, this.compressor(ops)) : ops.mapBuilder();
   }

   <T> KeyCompressor<T> compressor(DynamicOps<T> var1);

   default <B> MapEncoder<B> comap(final Function<? super B, ? extends A> function) {
      return new MapEncoder.Implementation<B>() {
         public <T> RecordBuilder<T> encode(B input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
            return MapEncoder.this.encode(function.apply(input), ops, prefix);
         }

         public <T> Stream<T> keys(DynamicOps<T> ops) {
            return MapEncoder.this.keys(ops);
         }

         public String toString() {
            return MapEncoder.this.toString() + "[comapped]";
         }
      };
   }

   default <B> MapEncoder<B> flatComap(final Function<? super B, ? extends DataResult<? extends A>> function) {
      return new MapEncoder.Implementation<B>() {
         public <T> Stream<T> keys(DynamicOps<T> ops) {
            return MapEncoder.this.keys(ops);
         }

         public <T> RecordBuilder<T> encode(B input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
            DataResult<? extends A> aResult = (DataResult)function.apply(input);
            RecordBuilder<T> builder = prefix.withErrorsFrom(aResult);
            return (RecordBuilder)aResult.map((r) -> {
               return MapEncoder.this.encode(r, ops, builder);
            }).result().orElse(builder);
         }

         public String toString() {
            return MapEncoder.this.toString() + "[flatComapped]";
         }
      };
   }

   default Encoder<A> encoder() {
      return new Encoder<A>() {
         public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
            return MapEncoder.this.encode(input, ops, MapEncoder.this.compressedBuilder(ops)).build(prefix);
         }

         public String toString() {
            return MapEncoder.this.toString();
         }
      };
   }

   default MapEncoder<A> withLifecycle(final Lifecycle lifecycle) {
      return new MapEncoder.Implementation<A>() {
         public <T> Stream<T> keys(DynamicOps<T> ops) {
            return MapEncoder.this.keys(ops);
         }

         public <T> RecordBuilder<T> encode(A input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
            return MapEncoder.this.encode(input, ops, prefix).setLifecycle(lifecycle);
         }

         public String toString() {
            return MapEncoder.this.toString();
         }
      };
   }

   static <T> RecordBuilder<T> makeCompressedBuilder(final DynamicOps<T> ops, final KeyCompressor<T> compressor) {
      class CompressedRecordBuilder extends RecordBuilder.AbstractUniversalBuilder<T, List<T>> {
         CompressedRecordBuilder() {
            super(ops);
         }

         protected List<T> initBuilder() {
            List<T> list = new ArrayList(compressor.size());

            for(int i = 0; i < compressor.size(); ++i) {
               list.add(null);
            }

            return list;
         }

         protected List<T> append(T key, T value, List<T> builder) {
            builder.set(compressor.compress(key), value);
            return builder;
         }

         protected DataResult<T> build(List<T> builder, T prefix) {
            return this.ops().mergeToList(prefix, builder);
         }
      }

      return new CompressedRecordBuilder();
   }

   public abstract static class Implementation<A> extends CompressorHolder implements MapEncoder<A> {
   }
}
