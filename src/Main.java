import java.io.*;
import java.util.*;

class Game {
    String name;
    String category;
    int price;
    int quality;

    public Game(String name, String category, int price, int quality) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quality = quality;
    }

    String getName() {
        return name;
    }

    String getCategory() {
        return category;
    }

    int getPrice() {
        return price;
    }

    int getQuality() {
        return quality;
    }

    @Override
    public String toString() {
        return name + "," + category + "," + price + "," + quality;
    }
}

class Dataset {
    private ArrayList<Game> data;
    private String sortedByAttribute;

    public Dataset(ArrayList<Game> data) {
        this.data = data;
        this.sortedByAttribute = "";
    }

    public ArrayList<Game> getGamesByPrice(int price) {
        ArrayList<Game> resultado = new ArrayList<>();
        if ("price".equals(sortedByAttribute)) {
            long startBinaryTime = System.nanoTime();
            int index = Collections.binarySearch(data, new Game("", "", price, 0), Comparator.comparingInt(Game::getPrice));

            if (index >= 0) {
                resultado.add(data.get(index));
                int i = index - 1;
                while (i >= 0 && data.get(i).getPrice() == price) {
                    resultado.add(data.get(i));
                    i--;
                }
                int j = index + 1;
                while (j < data.size() && data.get(j).getPrice() == price) {
                    resultado.add(data.get(j));
                    j++;
                }
            }
            long endBinaryTime = System.nanoTime();
            long binaryTimeMillis = (endBinaryTime - startBinaryTime) / 1000000;
        } else {
            long startLinearTime = System.nanoTime();
            for (Game juego : data) {
                if (juego.getPrice() == price) {
                    resultado.add(juego);
                }
            }
            long endLinearTime = System.nanoTime();
            long linearTimeMillis = (endLinearTime - startLinearTime) / 1000000;
        }

        return resultado;
    }

    public ArrayList<Game> getGamesByPriceRange(int lowerPrice, int higherPrice) {
        ArrayList<Game> resultado = new ArrayList<>();
        if ("price".equals(sortedByAttribute)) {
            long startBinaryTime = System.nanoTime();
            int startIndex = 0;
            int low = 0, high = data.size() - 1;
            int firstIndex = -1;

            while(low <= high) {
                int mid = low + (high - low) / 2;
                if (data.get(mid).getPrice() >= lowerPrice) {
                    firstIndex = mid;
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }

            if (firstIndex != -1) {
                for (int i = firstIndex; i < data.size(); i++) {
                    if (data.get(i).getPrice() <= higherPrice) {
                        resultado.add(data.get(i));
                    } else {
                        break;
                    }
                }
            }
            long endBinaryTime = System.nanoTime();
            long binaryTimeMillis = (endBinaryTime - startBinaryTime) / 1000000;
        } else {
            long startLinearTime = System.nanoTime();
            for (Game juego : data) {
                int precio = juego.getPrice();
                if (precio >= lowerPrice && precio <= higherPrice) {
                    resultado.add(juego);
                }
            }
            long endLinearTime = System.nanoTime();
            long linearTimeMillis = (endLinearTime - startLinearTime) / 1000000;
        }

        return resultado;
    }

    public ArrayList<Game> getGamesByCategory(String category) {
        ArrayList<Game> resultado = new ArrayList<>();
        if ("category".equals(sortedByAttribute)) {
            long startBinaryTime = System.nanoTime();
            int index = Collections.binarySearch(data, new Game("", category, 0, 0), Comparator.comparing(Game::getCategory, String.CASE_INSENSITIVE_ORDER));
            if (index >= 0) {
                resultado.add(data.get(index));
                int i = index - 1;
                while (i >= 0 && data.get(i).getCategory().equalsIgnoreCase(category)) {
                    resultado.add(data.get(i--));
                }
                int j = index + 1;
                while (j < data.size() && data.get(j).getCategory().equalsIgnoreCase(category)) {
                    resultado.add(data.get(j++));
                }
            }
            long endBinaryTime = System.nanoTime();
            long binaryTimeMillis = (endBinaryTime - startBinaryTime) / 1000000;
        } else {
            long startLinearTime = System.nanoTime();
            for (Game juego : data) {
                if (juego.getCategory().equalsIgnoreCase(category)) {
                    resultado.add(juego);
                }
            }
            long endLinearTime = System.nanoTime();
            long linearTimeMillis = (endLinearTime - startLinearTime) / 1000000;
        }
        return resultado;
    }

    public ArrayList<Game> getGamesByQuality(int quality) {
        ArrayList<Game> resultado = new ArrayList<>();
        if ("quality".equals(sortedByAttribute)) {
            long startBinaryTime = System.nanoTime();
            int index = Collections.binarySearch(data, new Game("", "", 0, quality), Comparator.comparingInt(Game::getQuality));
            if (index >= 0) {
                resultado.add(data.get(index));
                int i = index - 1;
                while (i >= 0 && data.get(i).getQuality() == quality) {
                    resultado.add(data.get(i--));
                }
                int j = index + 1;
                while (j < data.size() && data.get(j).getQuality() == quality) {
                    resultado.add(data.get(j++));
                }
            }
            long endBinaryTime = System.nanoTime();
            long binaryTimeMillis = (endBinaryTime - startBinaryTime) / 1000000;
        } else {
            long startLinearTime = System.nanoTime();
            for (Game juego : data) {
                if (juego.getQuality() == quality) {
                    resultado.add(juego);
                }
            }
            long endLinearTime = System.nanoTime();
            long linearTimeMillis = (endLinearTime - startLinearTime) / 1000000;
        }
        return resultado;
    }

    public void sortByAlgorithm(String algorithm, String attribute) {
        Comparator<Game> comparator;
        String effectiveAttribute = attribute.toLowerCase();

        switch (effectiveAttribute) {
            case "price":
                comparator = Comparator.comparingInt(Game::getPrice);
                break;
            case "category":
                comparator = Comparator.comparing(Game::getCategory, String.CASE_INSENSITIVE_ORDER);
                break;
            case "quality":
                comparator = Comparator.comparingInt(Game::getQuality);
                break;
            default:
                comparator = Comparator.comparingInt(Game::getPrice);
                effectiveAttribute = "price";
                break;
        }

        long startTime = System.nanoTime();

        switch (algorithm) {
            case "bubbleSort":
                bubbleSort(comparator);
                break;
            case "quickSort":
                quickSort(0, data.size() - 1, comparator);
                break;
            case "mergeSort":
                ArrayList<Game> sortedList = mergeSortRecursive(new ArrayList<>(data), comparator);
                this.data.clear();
                this.data.addAll(sortedList);
                break;
            case "selectionSort":
                selectionSort(comparator);
                break;
            case "insertionSort":
                insertionSort(comparator);
                break;
            case "countingSort":
                if ("quality".equals(effectiveAttribute)) {
                    countingSortByQuality();
                } else {
                    Collections.sort(data, comparator);
                }
                break;
            case "collectionsSort":
                Collections.sort(data, comparator);
                break;
            default:
                Collections.sort(data, comparator);
                break;
        }
        long endTime = System.nanoTime();
        long timeTakenMillis = (endTime - startTime) / 1000000;

        this.sortedByAttribute = effectiveAttribute;
    }

    private void bubbleSort(Comparator<Game> comparator) {
        int n = data.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(data.get(j), data.get(j + 1)) > 0) {
                    Collections.swap(data, j, j + 1);
                }
            }
        }
    }

    private void selectionSort(Comparator<Game> comparator) {
        int n = data.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (comparator.compare(data.get(j), data.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                Collections.swap(data, i, minIndex);
            }
        }
    }

    private void insertionSort(Comparator<Game> comparator) {
        int n = data.size();
        for (int i = 1; i < n; i++) {
            Game key = data.get(i);
            int j = i - 1;
            while (j >= 0 && comparator.compare(data.get(j), key) > 0) {
                data.set(j + 1, data.get(j));
                j--;
            }
            data.set(j + 1, key);
        }
    }

    private void quickSort(int low, int high, Comparator<Game> comp) {
        if (low < high) {
            int pi = partition(low, high, comp);
            quickSort(low, pi - 1, comp);
            quickSort(pi + 1, high, comp);
        }
    }

    private int partition(int low, int high, Comparator<Game> comp) {
        Game pivot = data.get(high);
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (comp.compare(data.get(j), pivot) <= 0) {
                i++;
                Collections.swap(data, i, j);
            }
        }
        Collections.swap(data, i + 1, high);
        return i + 1;
    }

    private ArrayList<Game> mergeSortRecursive(ArrayList<Game> list, Comparator<Game> comparator) {
        if (list.size() <= 1) {
            return list;
        }
        int middle = list.size() / 2;
        ArrayList<Game> left = new ArrayList<>(list.subList(0, middle));
        ArrayList<Game> right = new ArrayList<>(list.subList(middle, list.size()));

        left = mergeSortRecursive(left, comparator);
        right = mergeSortRecursive(right, comparator);

        return merge(left, right, comparator);
    }

    private ArrayList<Game> merge(ArrayList<Game> left, ArrayList<Game> right, Comparator<Game> comparator) {
        ArrayList<Game> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }
        while (i < left.size()) {
            result.add(left.get(i++));
        }
        while (j < right.size()) {
            result.add(right.get(j++));
        }
        return result;
    }

    private void countingSortByQuality() {
        if (data.isEmpty()) {
            return;
        }
        int maxQuality = 100;
        int[] count = new int[maxQuality + 1];

        for (Game game : data) {
            count[game.getQuality()]++;
        }

        for (int i = 1; i <= maxQuality; i++) {
            count[i] += count[i - 1];
        }

        ArrayList<Game> output = new ArrayList<>(Collections.nCopies(data.size(), null));
        for (int i = data.size() - 1; i >= 0; i--) {
            Game currentGame = data.get(i);
            int qualityValue = currentGame.getQuality();
            output.set(count[qualityValue] - 1, currentGame);
            count[qualityValue]--;
        }

        for (int i = 0; i < data.size(); i++) {
            data.set(i, output.get(i));
        }
    }
}

class GenerateData {
    private static final String[] palabras = {"Dragon", "Empire", "Quest", "Galaxy", "Legends", "Warrior", "Fantasy", "Kingdom", "Chronicles", "Heroes"};
    private static final String[] categorias = {"Acción", "Aventura", "Estrategia", "RPG", "Deportes", "Simulación", "Puzzle", "Carreras"};

    public static ArrayList<Game> generateRandomGames(int numGames) {
        ArrayList<Game> games = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numGames; i++) {
            String name = palabras[random.nextInt(palabras.length)] + " " + palabras[random.nextInt(palabras.length)];
            String category = categorias[random.nextInt(categorias.length)];
            int price = random.nextInt(70001);
            int quality = random.nextInt(101);
            games.add(new Game(name, category, price, quality));
        }
        return games;
    }

    public static void saveInFiles(ArrayList<Game> games, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("name,category,price,quality");
            for (Game g : games) {
                writer.println(g.toString());
            }
            System.out.println("Archivo '" + filename + "' guardado correctamente con " + games.size() + " juegos.");
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo '" + filename + "': " + e.getMessage());
        }
    }
}

class CSVLoader {
    public static ArrayList<Game> cargarDesdeCSV(String nombreArchivo) {
        ArrayList<Game> juegos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            br.readLine();
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 4) {
                    try {
                        String name = partes[0];
                        String category = partes[1];
                        int price = Integer.parseInt(partes[2]);
                        int quality = Integer.parseInt(partes[3]);
                        juegos.add(new Game(name, category, price, quality));
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato numérico en línea: " + linea + " (" + e.getMessage() + ")");
                    }
                } else {
                    System.err.println("Línea con formato incorrecto (esperados 4 campos): " + linea);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado: " + nombreArchivo);
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV '" + nombreArchivo + "': " + e.getMessage());
        }
        System.out.println("Cargados " + juegos.size() + " juegos desde '" + nombreArchivo + "'.");
        return juegos;
    }
}

class MainBenchmarks {
    public static long medirTiempoOrdenamiento(Dataset dataset, String algoritmo, String atributo) {
        long inicio = System.currentTimeMillis();
        dataset.sortByAlgorithm(algoritmo, atributo);
        long fin = System.currentTimeMillis();
        return fin - inicio;
    }

    public static long medirTiempoBusqueda(Runnable metodoBusqueda) {
        long inicio = System.currentTimeMillis();
        metodoBusqueda.run();
        long fin = System.currentTimeMillis();
        return fin - inicio;
    }

    public static void benchmarkOrdenamiento() {
        int[] tamanhos = {100, 10000, 1000000};
        String[] algoritmosBase = {"bubbleSort", "insertionSort", "selectionSort", "mergeSort", "quickSort", "collectionsSort"};
        String[] algoritmoCountingSort = {"countingSort"};
        String[] atributos = {"price", "category", "quality"};

        for (String atributo : atributos) {
            System.out.println("\n=== Benchmarks de Ordenamiento por: " + atributo.toUpperCase() + " ===");
            System.out.printf("%-20s | %-15s | %-20s%n", "Algoritmo", "Tamaño Dataset", "Tiempo Promedio (ms)");
            System.out.println(String.join("", Collections.nCopies(60, "-")));

            for (int tamanho : tamanhos) {
                ArrayList<Game> baseDatasetList = CSVLoader.cargarDesdeCSV("dataset_" + tamanho + ".csv");
                if (baseDatasetList.isEmpty() && tamanho > 0) {
                    System.out.println("No se pudo cargar dataset_" + tamanho + ".csv. Saltando benchmarks para este tamaño.");
                    continue;
                }

                String[] algoritmosAProbar = algoritmosBase;
                if ("quality".equals(atributo)) {
                    algoritmosAProbar = new String[algoritmosBase.length + 1];
                    System.arraycopy(algoritmosBase, 0, algoritmosAProbar, 0, algoritmosBase.length);
                    algoritmosAProbar[algoritmosBase.length] = "countingSort";
                }

                for (String algoritmo : algoritmosAProbar) {
                    if (tamanho == 1000000 && (algoritmo.equals("bubbleSort") || algoritmo.equals("insertionSort") || algoritmo.equals("selectionSort"))) {
                        System.out.printf("%-20s | %,-15d | %-20s%n", algoritmo, tamanho, "omitido (demasiado lento)");
                        continue;
                    }

                    long tiempoTotalEjecucion = 0;
                    boolean superoTiempoMax = false;
                    int ejecucionesValidas = 0;

                    for (int i = 0; i < 3; i++) {
                        ArrayList<Game> copiaParaOrdenar = new ArrayList<>(baseDatasetList);
                        Dataset datasetDePrueba = new Dataset(copiaParaOrdenar);

                        long tiempoEjecucionActual = medirTiempoOrdenamiento(datasetDePrueba, algoritmo, atributo);

                        if (tiempoEjecucionActual > 300000) {
                            superoTiempoMax = true;
                            break;
                        }
                        tiempoTotalEjecucion += tiempoEjecucionActual;
                        ejecucionesValidas++;
                    }

                    if (superoTiempoMax) {
                        System.out.printf("%-20s | %,-15d | %-20s%n", algoritmo, tamanho, ">300000 ms");
                    } else if (ejecucionesValidas > 0) {
                        System.out.printf("%-20s | %,-15d | %,-20d%n", algoritmo, tamanho, (tiempoTotalEjecucion / ejecucionesValidas));
                    } else {
                        System.out.printf("%-20s | %,-15d | %-20s%n", algoritmo, tamanho, "error/no ejecutado");
                    }
                }
                System.out.println(String.join("", Collections.nCopies(60, "-")));
            }
        }
    }

    public static void benchmarkBusqueda() {
        ArrayList<Game> baseDatasetList = CSVLoader.cargarDesdeCSV("dataset_1000000.csv");
        if (baseDatasetList.isEmpty()) {
            System.out.println("No se pudo cargar dataset_1000000.csv para benchmark de búsqueda.");
            return;
        }

        System.out.println("\n=== Benchmarks de Búsqueda (Dataset: 1,000,000 elementos) ===");
        System.out.printf("%-25s | %-15s | %-20s%n", "Método", "Tipo Búsqueda", "Tiempo (ms)");
        System.out.println(String.join("", Collections.nCopies(65, "-")));

        Dataset dsLinearPrice = new Dataset(new ArrayList<>(baseDatasetList));
        Dataset dsBinaryPrice = new Dataset(new ArrayList<>(baseDatasetList));
        dsBinaryPrice.sortByAlgorithm("quickSort", "price");

        long t1 = medirTiempoBusqueda(() -> dsLinearPrice.getGamesByPrice(35000));
        System.out.printf("%-25s | %-15s | %,-20d%n", "getGamesByPrice", "Lineal", t1);
        long t2 = medirTiempoBusqueda(() -> dsBinaryPrice.getGamesByPrice(35000));
        System.out.printf("%-25s | %-15s | %,-20d%n", "getGamesByPrice", "Binaria", t2);

        Dataset dsLinearPriceRange = new Dataset(new ArrayList<>(baseDatasetList));
        Dataset dsBinaryPriceRange = new Dataset(new ArrayList<>(baseDatasetList));
        dsBinaryPriceRange.sortByAlgorithm("quickSort", "price");

        long t3 = medirTiempoBusqueda(() -> dsLinearPriceRange.getGamesByPriceRange(10000, 20000));
        System.out.printf("%-25s | %-15s | %,-20d%n", "getGamesByPriceRange", "Lineal", t3);
        long t4 = medirTiempoBusqueda(() -> dsBinaryPriceRange.getGamesByPriceRange(10000, 20000));
        System.out.printf("%-25s | %-15s | %,-20d%n", "getGamesByPriceRange", "Binaria", t4);

        Dataset dsLinearCategory = new Dataset(new ArrayList<>(baseDatasetList));
        Dataset dsBinaryCategory = new Dataset(new ArrayList<>(baseDatasetList));
        dsBinaryCategory.sortByAlgorithm("quickSort", "category");

        long t5 = medirTiempoBusqueda(() -> dsLinearCategory.getGamesByCategory("Aventura"));
        System.out.printf("%-25s | %-15s | %,-20d%n", "getGamesByCategory", "Lineal", t5);
        long t6 = medirTiempoBusqueda(() -> dsBinaryCategory.getGamesByCategory("Aventura"));
        System.out.printf("%-25s | %-15s | %,-20d%n", "getGamesByCategory", "Binaria", t6);

        Dataset dsLinearQuality = new Dataset(new ArrayList<>(baseDatasetList));
        Dataset dsBinaryQuality = new Dataset(new ArrayList<>(baseDatasetList));
        dsBinaryQuality.sortByAlgorithm("quickSort", "quality");

        long t7 = medirTiempoBusqueda(() -> dsLinearQuality.getGamesByQuality(75));
        System.out.printf("%-25s | %-15s | %,-20d%n", "getGamesByQuality", "Lineal", t7);
        long t8 = medirTiempoBusqueda(() -> dsBinaryQuality.getGamesByQuality(75));
        System.out.printf("%-25s | %-15s | %,-20d%n", "getGamesByQuality", "Binaria", t8);
        System.out.println(String.join("", Collections.nCopies(65, "-")));
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Fase de Generación de Datos ---");
        ArrayList<Game> juegos100 = GenerateData.generateRandomGames(100);
        GenerateData.saveInFiles(juegos100, "dataset_100.csv");

        ArrayList<Game> juegos10000 = GenerateData.generateRandomGames(10000);
        GenerateData.saveInFiles(juegos10000, "dataset_10000.csv");

        ArrayList<Game> juegos1000000 = GenerateData.generateRandomGames(1000000);
        GenerateData.saveInFiles(juegos1000000, "dataset_1000000.csv");

        System.out.println("\n--- Fase de Pruebas de Funcionamiento Básico (Main) ---");
        ArrayList<Game> gamesParaPrueba = CSVLoader.cargarDesdeCSV("dataset_100.csv");
        if (gamesParaPrueba.isEmpty()) {
            System.out.println("⚠️ No se pudieron cargar juegos para la prueba básica en Main. Saliendo.");
            return;
        }

        Dataset datasetPrueba = new Dataset(gamesParaPrueba);

        System.out.println("\nOrdenando dataset de prueba por 'price' usando 'quickSort'...");
        datasetPrueba.sortByAlgorithm("quickSort", "price");

        int precioBuscado = gamesParaPrueba.get(0).getPrice();
        System.out.println("\nBuscando juegos con precio $" + precioBuscado + ":");
        ArrayList<Game> resultadoPrecio = datasetPrueba.getGamesByPrice(precioBuscado);
        for (Game juego : resultadoPrecio) {
            System.out.println("- " + juego.getName() + " | $" + juego.getPrice());
        }
        if (resultadoPrecio.isEmpty()){
            System.out.println(" (Ninguno encontrado)");
        }

        String categoriaBuscada = "Aventura";
        System.out.println("\nBuscando juegos de categoría '" + categoriaBuscada + "' (búsqueda lineal o binaria según estado previo):");
        ArrayList<Game> resultadoCategoria = datasetPrueba.getGamesByCategory(categoriaBuscada);
        for (Game juego : resultadoCategoria) {
            System.out.println("- " + juego.getName() + " | Cat: " + juego.getCategory());
        }
        if (resultadoCategoria.isEmpty()){
            System.out.println(" (Ninguno encontrado)");
        }

        System.out.println("\n--- Fase de Benchmarking ---");
        MainBenchmarks.benchmarkOrdenamiento();
        MainBenchmarks.benchmarkBusqueda();

        System.out.println("\n--- Fin del programa ---");
    }
}