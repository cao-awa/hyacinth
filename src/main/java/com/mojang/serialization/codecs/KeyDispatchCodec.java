package com.mojang.serialization.codecs;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.util.function.Function;
import java.util.stream.Stream;

public class KeyDispatchCodec<K, V> extends MapCodec<V> {
   private final String typeKey;
   private final Codec<K> keyCodec;
   private final String valueKey;
   private final Function<? super V, ? extends DataResult<? extends K>> type;
   private final Function<? super K, ? extends DataResult<? extends Decoder<? extends V>>> decoder;
   private final Function<? super V, ? extends DataResult<? extends Encoder<V>>> encoder;
   private final boolean assumeMap;

   public static <K, V> KeyDispatchCodec<K, V> unsafe(String typeKey, Codec<K> keyCodec, Function<? super V, ? extends DataResult<? extends K>> type, Function<? super K, ? extends DataResult<? extends Decoder<? extends V>>> decoder, Function<? super V, ? extends DataResult<? extends Encoder<V>>> encoder) {
      return new KeyDispatchCodec<>(typeKey, keyCodec, type, decoder, encoder, true);
   }

   protected KeyDispatchCodec(String typeKey, Codec<K> keyCodec, Function<? super V, ? extends DataResult<? extends K>> type, Function<? super K, ? extends DataResult<? extends Decoder<? extends V>>> decoder, Function<? super V, ? extends DataResult<? extends Encoder<V>>> encoder, boolean assumeMap) {
      this.valueKey = "value";
      this.typeKey = typeKey;
      this.keyCodec = keyCodec;
      this.type = type;
      this.decoder = decoder;
      this.encoder = encoder;
      this.assumeMap = assumeMap;
   }

   public KeyDispatchCodec(String typeKey, Codec<K> keyCodec, Function<? super V, ? extends DataResult<? extends K>> type, Function<? super K, ? extends DataResult<? extends Codec<? extends V>>> codec) {
      this(typeKey, keyCodec, type, codec, (v) -> getCodec(type, codec, v), false);
   }

   public <T> DataResult<V> decode(DynamicOps<T> ops, MapLike<T> input) {
      T elementName = input.get(this.typeKey);
      return elementName == null ? DataResult.error("Input does not contain a key [" + this.typeKey + "]: " + input) : this.keyCodec.decode(ops, elementName).flatMap((type) -> {
         DataResult<? extends Decoder<? extends V>> elementDecoder = this.decoder.apply(type.getFirst());
         return elementDecoder.flatMap((c) -> {
            if (ops.compressMaps()) {
               T value = input.get(ops.createString("value"));
               return value == null ? DataResult.error("Input does not have a \"value\" entry: " + input) : c.parse(ops, value).map(Function.identity());
            } else if (c instanceof MapCodec.MapCodecCodec) {
               return ((MapCodec.MapCodecCodec)c).codec().decode(ops, input).map(Function.identity());
            } else {
               return this.assumeMap ? c.decode(ops, ops.createMap(input.entries())).map(Pair::getFirst) : c.decode(ops, input.get("value")).map(Pair::getFirst);
            }
         });
      });
   }

   public <T> RecordBuilder<T> encode(V input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
      DataResult<? extends Encoder<V>> elementEncoder = this.encoder.apply(input);
      RecordBuilder<T> builder = prefix.withErrorsFrom(elementEncoder);
      if (elementEncoder.result().isEmpty()) {
         return builder;
      } else {
         Encoder<V> c = elementEncoder.result().get();
         if (ops.compressMaps()) {
            return prefix.add(this.typeKey, this.type.apply(input).flatMap((t) -> this.keyCodec.encodeStart(ops, t))).add("value", c.encodeStart(ops, input));
         } else if (c instanceof MapCodec.MapCodecCodec) {
            return ((MapCodec.MapCodecCodec)c).codec().encode(input, ops, prefix).add(this.typeKey, this.type.apply(input).flatMap((t) -> this.keyCodec.encodeStart(ops, t)));
         } else {
            T typeString = ops.createString(this.typeKey);
            DataResult<T> result = c.encodeStart(ops, input);
            if (this.assumeMap) {
               ops.getClass();
               DataResult<MapLike<T>> element = result.flatMap(ops::getMap);
               return element.map((map) -> {
                  prefix.add(typeString, this.type.apply(input).flatMap((t) -> this.keyCodec.encodeStart(ops, t)));
                  map.entries().forEach((pair) -> {
                     if (!pair.getFirst().equals(typeString)) {
                        prefix.add(pair.getFirst(), pair.getSecond());
                     }

                  });
                  return prefix;
               }).result().orElseGet(() -> prefix.withErrorsFrom(element));
            } else {
               prefix.add(typeString, this.type.apply(input).flatMap((t) -> this.keyCodec.encodeStart(ops, t)));
               prefix.add("value", result);
               return prefix;
            }
         }
      }
   }

   public <T> Stream<T> keys(DynamicOps<T> ops) {
      Stream<String> var10000 = Stream.of(this.typeKey, "value");
      ops.getClass();
      return var10000.map(ops::createString);
   }

   private static <K, V> DataResult<? extends Encoder<V>> getCodec(Function<? super V, ? extends DataResult<? extends K>> type, Function<? super K, ? extends DataResult<? extends Encoder<? extends V>>> encoder, V input) {
      return type.apply(input).flatMap((k) -> ((DataResult)encoder.apply(k)).map(Function.identity())).map((c) -> c);
   }

   public String toString() {
      return "KeyDispatchCodec[" + this.keyCodec.toString() + " " + this.type + " " + this.decoder + "]";
   }
}
