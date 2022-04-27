package com.mojang.serialization;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public interface MapDecoder<A> extends Keyable {
   <T> DataResult<A> decode(DynamicOps<T> var1, MapLike<T> var2);

   default <T> DataResult<A> compressedDecode(DynamicOps<T> ops, T input) {
      if (ops.compressMaps()) {
         Optional<Consumer<Consumer<T>>> inputList = ops.getList(input).result();
         if (inputList.isEmpty()) {
            return DataResult.error("Input is not a list");
         } else {
            final KeyCompressor<T> compressor = this.compressor(ops);
            final List<T> entries = new ArrayList<>();
            inputList.get().accept(entries::add);
            MapLike<T> map = new MapLike<T>() {
               @Nullable
               public T get(T key) {
                  return entries.get(compressor.compress(key));
               }

               @Nullable
               public T get(String key) {
                  return entries.get(compressor.compress(key));
               }

               public Stream<Pair<T, T>> entries() {
                  return IntStream.range(0, entries.size()).mapToObj((i) -> {
                     return Pair.of(compressor.decompress(i), entries.get(i));
                  }).filter((p) -> {
                     return p.getSecond() != null;
                  });
               }
            };
            return this.decode(ops, map);
         }
      } else {
         return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap((mapx) -> {
            return this.decode(ops, mapx);
         });
      }
   }

   <T> KeyCompressor<T> compressor(DynamicOps<T> var1);

   default Decoder<A> decoder() {
      return new Decoder<A>() {
         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
            return MapDecoder.this.compressedDecode(ops, input).map((r) -> {
               return Pair.of(r, input);
            });
         }

         public String toString() {
            return MapDecoder.this.toString();
         }
      };
   }

   default <B> MapDecoder<B> flatMap(final Function<? super A, ? extends DataResult<? extends B>> function) {
      return new MapDecoder.Implementation<B>() {
         public <T> Stream<T> keys(DynamicOps<T> ops) {
            return MapDecoder.this.keys(ops);
         }

         public <T> DataResult<B> decode(DynamicOps<T> ops, MapLike<T> input) {
            return MapDecoder.this.decode(ops, input).flatMap((b) -> function.apply(b).map(Function.identity()));
         }

         public String toString() {
            return MapDecoder.this + "[flatMapped]";
         }
      };
   }

   default <B> MapDecoder<B> map(final Function<? super A, ? extends B> function) {
      return new MapDecoder.Implementation<B>() {
         public <T> DataResult<B> decode(DynamicOps<T> ops, MapLike<T> input) {
            return MapDecoder.this.decode(ops, input).map(function);
         }

         public <T> Stream<T> keys(DynamicOps<T> ops) {
            return MapDecoder.this.keys(ops);
         }

         public String toString() {
            return MapDecoder.this + "[mapped]";
         }
      };
   }

   default <E> MapDecoder<E> ap(final MapDecoder<Function<? super A, ? extends E>> decoder) {
      return new MapDecoder.Implementation<E>() {
         public <T> DataResult<E> decode(DynamicOps<T> ops, MapLike<T> input) {
            return MapDecoder.this.decode(ops, input).flatMap((f) -> {
               return decoder.decode(ops, input).map((e) -> {
                  return e.apply(f);
               });
            });
         }

         public <T> Stream<T> keys(DynamicOps<T> ops) {
            return Stream.concat(MapDecoder.this.keys(ops), decoder.keys(ops));
         }

         public String toString() {
            return decoder.toString() + " * " + MapDecoder.this;
         }
      };
   }

   default MapDecoder<A> withLifecycle(final Lifecycle lifecycle) {
      return new MapDecoder.Implementation<A>() {
         public <T> Stream<T> keys(DynamicOps<T> ops) {
            return MapDecoder.this.keys(ops);
         }

         public <T> DataResult<A> decode(DynamicOps<T> ops, MapLike<T> input) {
            return MapDecoder.this.decode(ops, input).setLifecycle(lifecycle);
         }

         public String toString() {
            return MapDecoder.this.toString();
         }
      };
   }

   abstract class Implementation<A> extends CompressorHolder implements MapDecoder<A> {
   }
}
