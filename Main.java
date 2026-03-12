import java.util.*;
public class Main {

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);

        Map<String, String> inventory =
                InventoryLoader.loadInventory("inventario.txt");

        System.out.println("\n");
        System.out.println("SELECCIONE LA IMPLEMENTACIÓN");
        System.out.println("1) HashMap");
        System.out.println("2) TreeMap");
        System.out.println("3) LinkedHashMap");
        System.out.println("4) Ejecutar PROFILER (comparar las 3)");
        System.out.print("> ");

        int opt = s.nextInt();
        s.nextLine();

        // Si elige profiler
        if (opt == 4) {
            Profiler.runProfiler(inventory);
            System.out.println("Saliendo...");
            s.close();
            return;
        }

        MapType type;
        switch (opt) {
            case 1: type = MapType.HASHMAP; break;
            case 2: type = MapType.TREEMAP; break;
            case 3: type = MapType.LINKEDHASHMAP; break;
            default:
                System.out.println("Opción inválida → usando HashMap.");
                type = MapType.HASHMAP;
        }

        Map<String, Integer> userCollection = MapFactory.createMap(type);

        int opcion = 0;

        while (opcion != 7) {

            System.out.println("\n======= MENÚ =======");
            System.out.println("1) Agregar producto");
            System.out.println("2) Mostrar categoría de producto");
            System.out.println("3) Mostrar colección del usuario");
            System.out.println("4) Mostrar colección ordenada por categoría");
            System.out.println("5) Mostrar inventario completo");
            System.out.println("6) Mostrar inventario ordenado por categoría");
            System.out.println("7) Salir");
            System.out.print("> ");

            if (!s.hasNextInt()) { s.nextLine(); continue; }

            opcion = s.nextInt();
            s.nextLine();

            switch (opcion) {

                case 1: {
                    System.out.println("Ingrese categoría del producto a agregar:");
                    String category = s.nextLine().trim();

                    if (category.isEmpty() || inventory.values().stream().noneMatch(c -> equalsIgnoreCaseTrim(c, category))) {
                        System.out.println("Categoría no encontrada.");
                        break;
                    }

                    System.out.println("Ingrese producto a agregar:");
                    String p = resolveProductNameInCategory(inventory, s.nextLine(), category);

                    if (p == null) {
                        System.out.println("NO existe en esa categoría.");
                        break;
                    }

                    userCollection.put(p, userCollection.getOrDefault(p, 0) + 1);
                    System.out.println("Agregado: " + p);
                    break;
                }

                case 2: {
                    System.out.println("Ingrese producto:");
                    String p = resolveProductName(inventory, s.nextLine());

                    if (p == null) {
                        System.out.println("NO existe.");
                    } else {
                        System.out.println("Categoría: " + inventory.get(p));
                    }
                    break;
                }

                case 3: {
                    System.out.println("--- Colección usuario ---");
                    Util.printUserCollection(userCollection, inventory);
                    break;
                }

                case 4: {
                    System.out.println("--- Colección ordenada por categoría ---");

                    userCollection.entrySet().stream()
                            .sorted(Comparator
                                    .comparing((Map.Entry<String, Integer> e) -> inventory.get(e.getKey()))
                                    .thenComparing(Map.Entry::getKey))
                            .forEach(e ->
                                    System.out.println(e.getKey() + " | " +
                                            inventory.get(e.getKey()) +
                                            " | Cantidad: " + e.getValue()));
                    break;
                }

                case 5: {
                    System.out.println("--- Inventario completo ---");
                    Util.printInventory(inventory);
                    break;
                }

                case 6: {
                    System.out.println("--- Inventario ordenado por categoría ---");
                    inventory.entrySet().stream()
                            .sorted(Comparator
                                    .comparing(Map.Entry<String, String>::getValue)
                                    .thenComparing(Map.Entry::getKey))
                            .forEach(e ->
                                    System.out.println(e.getKey() + " | " + e.getValue()));
                    break;
                }
            }
        }

        System.out.println("Saliendo...");
    }

    private static String resolveProductName(Map<String, String> inventory, String input) {

        if (input == null) return null;

        String needle = input.trim().toLowerCase();

        for (String k : inventory.keySet())
            if (k.equalsIgnoreCase(input) ||
                k.toLowerCase().equals(needle)) return k;

        for (String k : inventory.keySet())
            if (k.toLowerCase().startsWith(needle)) return k;

        return null;
    }

    private static String resolveProductNameInCategory(Map<String, String> inventory,
                                                       String input,
                                                       String category) {

        if (input == null || category == null) return null;

        String needle = input.trim().toLowerCase();

        for (String k : inventory.keySet()) {
            if (!equalsIgnoreCaseTrim(inventory.get(k), category)) continue;
            if (k.equalsIgnoreCase(input) || k.toLowerCase().equals(needle)) return k;
        }

        for (String k : inventory.keySet()) {
            if (!equalsIgnoreCaseTrim(inventory.get(k), category)) continue;
            if (k.toLowerCase().startsWith(needle)) return k;
        }

        return null;
    }

    private static boolean equalsIgnoreCaseTrim(String a, String b) {
        return a != null && b != null && a.trim().equalsIgnoreCase(b.trim());
    }
}
