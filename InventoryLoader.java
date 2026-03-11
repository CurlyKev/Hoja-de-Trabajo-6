import java.io.*;
import java.util.*;

public class InventoryLoader {

    public static Map<String, String> loadInventory(String fileName) {

        List<String> lines = readAll(fileName);
        boolean looksLikePairs = lines.stream()
                .map(String::trim)
                .filter(l -> !l.isEmpty())
                .filter(l -> l.endsWith("\\"))
                .count() >= Math.max(1, lines.size() / 20);

        Map<String, String> inventory = new LinkedHashMap<>();

        if (looksLikePairs) parsePairs(lines, inventory);
        else parseBlocks(lines, inventory);

        return inventory;
    }

    private static List<String> readAll(String fileName) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) lines.add(line);

        } catch (Exception e) {
            System.out.println("Error al leer inventario: " + e.getMessage());
        }
        return lines;
    }

    private static void parsePairs(List<String> lines, Map<String, String> inventory) {

        String pendingCategory = null;

        for (int i = 0; i < lines.size(); i++) {

            String raw = lines.get(i);
            if (raw == null) continue;

            String line = raw.trim();
            if (line.isEmpty()) continue;

            if (line.endsWith("\\")) {
                pendingCategory = line.replaceAll("\\s*\\\\\\s*$", "").trim();
                continue;
            }

            if (pendingCategory != null) {

                String product = line.trim();

                if (equalsIgnoreCaseTrim(pendingCategory, "Categoría") &&
                    equalsIgnoreCaseTrim(product, "Producto")) {

                    pendingCategory = null;
                    continue;
                }

                addEntry(inventory, product, pendingCategory);
                pendingCategory = null;
            }
        }
    }

    private static void parseBlocks(List<String> lines, Map<String, String> inventory) {
        String category = null;

        for (String raw : lines) {
            if (raw == null) continue;

            String line = raw.trim();
            if (line.isEmpty()) continue;

            if (!raw.startsWith(" ") && !raw.startsWith("-") && !raw.startsWith("*")) {
                category = line;
            } else if (category != null) {
                String product = line.replaceFirst("^[-*]\\s*", "").trim();
                addEntry(inventory, product, category);
            }
        }
    }

    private static void addEntry(Map<String, String> inventory, String product, String category) {

        if (product == null || category == null) return;

        String p = product.trim();
        String c = category.trim();

        if (p.isEmpty() || c.isEmpty()) return;

        if (inventory.containsKey(p)) {
            String prev = inventory.get(p);
            if (!prev.equals(c)) {
                System.out.println("AVISO: Producto repetido: " + p + " estaba en " + prev +
                                   " — se ignora nueva categoría: " + c);
            }
            return;
        }
        inventory.put(p, c);
    }

    private static boolean equalsIgnoreCaseTrim(String a, String b) {
        return a != null && b != null && a.trim().equalsIgnoreCase(b.trim());
    }
}
