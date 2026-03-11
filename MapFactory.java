import java.util.*;

public class MapFactory {

    public static <K, V> Map<K, V> createMap(MapType type) {
        switch (type) {
            case HASHMAP:
                return new HashMap<>();
            case TREEMAP:
                return new TreeMap<>();
            case LINKEDHASHMAP:
                return new LinkedHashMap<>();
            default:
                return new HashMap<>();
        }
    }
}

