import java.util.*;

public class Profiler {

    public static void runProfiler(Map<String, String> inventory) {
        
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║         PROFILING - Tiempo de Ejecución             ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");

        MapType[] types = {MapType.HASHMAP, MapType.TREEMAP, MapType.LINKEDHASHMAP};
        long[] times = new long[3];
        String[] names = {"HashMap", "TreeMap", "LinkedHashMap"};

        for (int i = 0; i < types.length; i++) {
            times[i] = profileMapImplementation(inventory, types[i], names[i]);
        }

        // Mostrar resultados
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                    RESULTADOS                        ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        
        for (int i = 0; i < names.length; i++) {
            System.out.printf("║ %-30s | %15d ns │\n", names[i], times[i]);
        }
        
        System.out.println("╠════════════════════════════════════════════════════════╣");
        
        // Encontrar la más rápida
        long minTime = times[0];
        int fastestIndex = 0;
        for (int i = 1; i < times.length; i++) {
            if (times[i] < minTime) {
                minTime = times[i];
                fastestIndex = i;
            }
        }
        
        System.out.printf("║ ✓ MÁS RÁPIDA: %-33s │\n", names[fastestIndex]);
        System.out.println("╚════════════════════════════════════════════════════════╝\n");

        // Mostrar comparativas
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║              ANÁLISIS COMPARATIVO                    ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        
        for (int i = 0; i < names.length; i++) {
            if (i != fastestIndex) {
                double ratio = (double) times[i] / minTime;
                System.out.printf("║ %s es %.2fx más lenta que %s\n", 
                    names[i], ratio, names[fastestIndex]);
            }
        }
        
        System.out.println("╚════════════════════════════════════════════════════════╝\n");
    }

    private static long profileMapImplementation(Map<String, String> inventory, 
                                                  MapType type, 
                                                  String typeName) {
        
        Map<String, Integer> userCollection = MapFactory.createMap(type);
        
        // Insertar algunos datos
        int insertCount = 50;
        List<String> products = new ArrayList<>(inventory.keySet());
        for (int i = 0; i < Math.min(insertCount, products.size()); i++) {
            String product = products.get(i);
            userCollection.put(product, i + 1);
        }

        // Medir tiempo de visualización (operación principal del programa)
        long startTime = System.nanoTime();
        
        // Ejecutar 100 iteraciones de visualización para obtener tiempos medibles
        for (int iteration = 0; iteration < 100; iteration++) {
            // Operación 1: Mostrar colección (tipo 3)
            userCollection.entrySet().forEach(e -> {
                String ignored = inventory.get(e.getKey());
            });
            
            // Operación 2: Mostrar ordenado por categoría (tipo 4)
            userCollection.entrySet().stream()
                    .sorted(Comparator
                            .comparing((Map.Entry<String, Integer> e) -> inventory.get(e.getKey()))
                            .thenComparing(Map.Entry::getKey))
                    .forEach(e -> {
                        String ignored = inventory.get(e.getKey());
                    });
        }
        
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;

        System.out.printf("✓ %-20s: %,15d ns (%.3f ms)\n", 
            typeName, totalTime, totalTime / 1_000_000.0);

        return totalTime;
    }
}
