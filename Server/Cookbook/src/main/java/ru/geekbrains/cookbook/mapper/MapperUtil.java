package ru.geekbrains.cookbook.mapper;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapperUtil {
    public static <K extends Comparable<? super K>, V> List<V> mapToList(Map<K, ? extends V> map){
        return map.entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public static <V> Map<Integer, V> listToMap(List<? extends V> list){
        Map<Integer, V> map = new HashMap<>();
        int i = 1;
        for(V v: list)
            map.put(i++, v);
        return map;
    }
}
