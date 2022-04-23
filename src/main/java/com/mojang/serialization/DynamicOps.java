package com.mojang.serialization;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.apache.commons.lang3.mutable.MutableObject;

public interface DynamicOps<T> {
   T empty();

   default T emptyMap() {
      return this.createMap(ImmutableMap.of());
   }

   default T emptyList() {
      return this.createList(Stream.empty());
   }

   <U> U convertTo(DynamicOps<U> var1, T var2);

   DataResult<Number> getNumberValue(T var1);

   default Number getNumberValue(T input, Number defaultValue) {
      return this.getNumberValue(input).result().orElse(defaultValue);
   }

   T createNumeric(Number var1);

   default T createByte(byte value) {
      return this.createNumeric(value);
   }

   default T createShort(short value) {
      return this.createNumeric(value);
   }

   default T createInt(int value) {
      return this.createNumeric(value);
   }

   default T createLong(long value) {
      return this.createNumeric(value);
   }

   default T createFloat(float value) {
      return this.createNumeric(value);
   }

   default T createDouble(double value) {
      return this.createNumeric(value);
   }

   default DataResult<Boolean> getBooleanValue(T input) {
      return this.getNumberValue(input).map((number) -> number.byteValue() != 0);
   }

   default T createBoolean(boolean value) {
      return this.createByte((byte)(value ? 1 : 0));
   }

   DataResult<String> getStringValue(T var1);

   T createString(String var1);

   DataResult<T> mergeToList(T var1, T var2);

   default DataResult<T> mergeToList(T list, List<T> values) {
      DataResult<T> result = DataResult.success(list);

      Object value = new Object();
      Object finalValue = value;
      for(Iterator<T> var4 = values.iterator(); var4.hasNext(); result = result.flatMap((r) -> this.mergeToList(r, (T) finalValue))) {
         value = var4.next();
      }

      return result;
   }

   DataResult<T> mergeToMap(T var1, T var2, T var3);

   default DataResult<T> mergeToMap(T map, Map<T, T> values) {
      return this.mergeToMap(map, MapLike.forMap(values, this));
   }

   default DataResult<T> mergeToMap(T map, MapLike<T> values) {
      MutableObject<DataResult<T>> result = new MutableObject(DataResult.success(map));
      values.entries().forEach((entry) -> {
         result.setValue((result.getValue()).flatMap((r) -> this.mergeToMap(r, entry.getFirst(), entry.getSecond())));
      });
      return result.getValue();
   }

   default DataResult<T> mergeToPrimitive(T prefix, T value) {
      return !Objects.equals(prefix, this.empty()) ? DataResult.error("Do not know how to append a primitive value " + value + " to " + prefix, value) : DataResult.success(value);
   }

   DataResult<Stream<Pair<T, T>>> getMapValues(T var1);

   default DataResult<Consumer<BiConsumer<T, T>>> getMapEntries(T input) {
      return this.getMapValues(input).map((s) -> (c) -> s.forEach((p) -> c.accept(p.getFirst(), p.getSecond())));
   }

   T createMap(Stream<Pair<T, T>> var1);

   default DataResult<MapLike<T>> getMap(T input) {
      return this.getMapValues(input).flatMap((s) -> {
         try {
            return DataResult.success(MapLike.forMap(s.collect(Pair.toMap()), this));
         } catch (IllegalStateException var3) {
            return DataResult.error("Error while building map: " + var3.getMessage());
         }
      });
   }

   default T createMap(Map<T, T> map) {
      return this.createMap(map.entrySet().stream().map((e) -> Pair.of(e.getKey(), e.getValue())));
   }

   DataResult<Stream<T>> getStream(T var1);

   default DataResult<Consumer<Consumer<T>>> getList(T input) {
      return this.getStream(input).map((s) -> s::forEach);
   }

   T createList(Stream<T> var1);

   default DataResult<ByteBuffer> getByteBuffer(T input) {
      return this.getStream(input).flatMap((stream) -> {
         List<T> list = stream.toList();
         if (!list.stream().allMatch((element) -> this.getNumberValue(element).result().isPresent())) {
            return DataResult.error("Some elements are not bytes: " + input);
         } else {
            ByteBuffer buffer = ByteBuffer.wrap(new byte[list.size()]);

            for(int i = 0; i < list.size(); ++i) {
               buffer.put(i, this.getNumberValue(list.get(i)).result().get().byteValue());
            }

            return DataResult.success(buffer);
         }
      });
   }

   default T createByteList(ByteBuffer input) {
      return this.createList(IntStream.range(0, input.capacity()).mapToObj((i) -> this.createByte(input.get(i))));
   }

   default DataResult<IntStream> getIntStream(T input) {
      return this.getStream(input).flatMap((stream) -> {
         List<T> list = stream.toList();
         return list.stream().allMatch((element) -> this.getNumberValue(element).result().isPresent()) ? DataResult.success(list.stream().mapToInt((element) -> (this.getNumberValue(element).result().get()).intValue())) : DataResult.error("Some elements are not ints: " + input);
      });
   }

   default T createIntList(IntStream input) {
      return this.createList(input.mapToObj(this::createInt));
   }

   default DataResult<LongStream> getLongStream(T input) {
      return this.getStream(input).flatMap((stream) -> {
         List<T> list = stream.toList();
         return list.stream().allMatch((element) -> this.getNumberValue(element).result().isPresent()) ? DataResult.success(list.stream().mapToLong((element) -> (this.getNumberValue(element).result().get()).longValue())) : DataResult.error("Some elements are not longs: " + input);
      });
   }

   default T createLongList(LongStream input) {
      return this.createList(input.mapToObj(this::createLong));
   }

   T remove(T var1, String var2);

   default boolean compressMaps() {
      return false;
   }

   default DataResult<T> get(T input, String key) {
      return this.getGeneric(input, this.createString(key));
   }

   default DataResult<T> getGeneric(T input, T key) {
      return this.getMap(input).flatMap((map) -> Optional.ofNullable(map.get(key)).map(DataResult::success).orElseGet(() -> DataResult.error("No element " + key + " in the map " + input)));
   }

   default T set(T input, String key, T value) {
      return this.mergeToMap(input, this.createString(key), value).result().orElse(input);
   }

   default T update(T input, String key, Function<T, T> function) {
      return this.get(input, key).map((value) -> this.set(input, key, function.apply(value))).result().orElse(input);
   }

   default T updateGeneric(T input, T key, Function<T, T> function) {
      return this.getGeneric(input, key).flatMap((value) -> this.mergeToMap(input, key, function.apply(value))).result().orElse(input);
   }

   default ListBuilder<T> listBuilder() {
      return new ListBuilder.Builder(this);
   }

   default RecordBuilder<T> mapBuilder() {
      return new RecordBuilder.MapBuilder(this);
   }

   default <E> Function<E, DataResult<T>> withEncoder(Encoder<E> encoder) {
      return (e) -> encoder.encodeStart(this, e);
   }

   default <E> Function<T, DataResult<Pair<E, T>>> withDecoder(Decoder<E> decoder) {
      return (t) -> decoder.decode(this, t);
   }

   default <E> Function<T, DataResult<E>> withParser(Decoder<E> decoder) {
      return (t) -> decoder.parse(this, t);
   }

   default <U> U convertList(DynamicOps<U> outOps, T input) {
      return outOps.createList((this.getStream(input).result().orElse(Stream.empty())).map((e) -> this.convertTo(outOps, e)));
   }

   default <U> U convertMap(DynamicOps<U> outOps, T input) {
      return outOps.createMap((this.getMapValues(input).result().orElse(Stream.empty())).map((e) -> Pair.of(this.convertTo(outOps, e.getFirst()), this.convertTo(outOps, e.getSecond()))));
   }
}
