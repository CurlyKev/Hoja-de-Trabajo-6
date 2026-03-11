import java.util.Map;

public class Util {

    public static void printInventory(Map<String, String> inventory) {
        inventory.forEach((prod, cat) ->
                System.out.println("Producto: " + prod + " | Categoría: " + cat));
    }

    public static void printUserCollection(Map<String, Integer> userCount,
                                           Map<String, String> inventory) {

        userCount.forEach((prod, qty) -> {
            String cat = inventory.get(prod);
            System.out.println(prod + " | Categoría: " + cat + " | Cantidad: " + qty);
        });
    }
}