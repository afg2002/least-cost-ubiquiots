package leastcost;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class TransportationProblem {

    // Deklarasi variabel
    double []required;
    double []stock;
    double [][]cost;
    LinkedList<Variable> feasible = new LinkedList<>();

    int stockSize;
    int requiredSize;

    // Konstruktor
    public TransportationProblem(int stockSize, int requiredSize) {
        // Inisialisasi variabel
        this.stockSize = stockSize;
        this.requiredSize = requiredSize;

        stock = new double[stockSize];
        required = new double[requiredSize];
        cost = new double[stockSize][requiredSize];

        for(int i = 0; i < (requiredSize + stockSize - 1); i++)
            feasible.add(new Variable());
    }

    // Metode untuk mengatur nilai stok
    public void setStock(double value, int index) {
        stock[index] = value;
    }

    // Metode untuk mengatur kebutuhan
    public void setRequired(double value, int index) {
        required[index] = value;
    }

    // Metode untuk mengatur biaya transportasi
    public void setCost(double value, int stock, int required) {
        cost[stock][required] = value;
    }

    /**
     * Metode untuk menginisialisasi solusi feasible
     * menggunakan aturan biaya terkecil
     *
     * @return waktu eksekusi dalam detik
     */
    public double leastCostRule() {
        // Menghitung waktu eksekusi metode
        long start = System.nanoTime();

        double min;
        int k = 0; // Penghitung solusi yang memungkinkan

        // isSet bertanggung jawab untuk memberi anotasi pada sel yang telah dialokasikan
        boolean [][]isSet = new boolean[stockSize][requiredSize];
        for (int j = 0; j < requiredSize; j++)
            for (int i = 0;  i < stockSize; i++)
                isSet[i][j] = false;

        int i = 0, j = 0;
        Variable minCost = new Variable();

        // Loop ini bertanggung jawab untuk mencari sel dengan biaya terendah
        while(k < (stockSize + requiredSize - 1)) {
            minCost.setValue(Double.MAX_VALUE);
            // Memilih sel dengan biaya terendah
            for (int m = 0;  m < stockSize; m++)
                for (int n = 0; n < requiredSize; n++)
                    if(!isSet[m][n])
                        if(cost[m][n] < minCost.getValue()) {
                            minCost.setStock(m);
                            minCost.setRequired(n);
                            minCost.setValue(cost[m][n]);
                        }

            i = minCost.getStock();
            j = minCost.getRequired();

            // Mengalokasikan stok dengan cara yang tepat
            min = Math.min(required[j], stock[i]);

            feasible.get(k).setRequired(j);
            feasible.get(k).setStock(i);
            feasible.get(k).setValue(min);
            k++;

            required[j] -= min;
            stock[i] -= min;

            // Mengalokasikan nilai null pada baris/kolom yang dihapus
            if(stock[i] == 0)
                for(int l = 0; l < requiredSize; l++)
                    isSet[i][l] = true;
            else
                for(int l = 0; l < stockSize; l++)
                    isSet[l][j] = true;
        }

        // Mengembalikan waktu eksekusi metode dalam detik
        return (System.nanoTime() - start) * 1.0e-9;
    }

    public double getSolution() {
        double result = 0;
        for(Variable x: feasible) {
            result += x.getValue() * cost[x.getStock()][x.getRequired()];
        }

        return result;
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Masukkan ukuran masalah:");
        int s = scanner.nextInt();
        int r = scanner.nextInt();
        double x;
        TransportationProblem test = new TransportationProblem(s, r);

        System.out.println("Masukkan kapasitas stok:");
        for (int i = 0; i < test.stockSize; i++) {
            x = scanner.nextDouble();
            test.setStock(x, i);
        }

        System.out.println("Masukkan kebutuhan:");
        for (int i = 0; i < test.requiredSize; i++) {
            x = scanner.nextDouble();
            test.setRequired(x, i);
        }

        System.out.println("Masukkan biaya:");
        for (int i = 0; i < test.stockSize; i++) {
            for (int j = 0; j < test.requiredSize; j++) {
                x = scanner.nextDouble();
                test.setCost(x, i, j);
            }
        }

        test.leastCostRule();

        System.out.println("Berikut adalah solusi feasible (dapat dilakukan) penghitungan:");
        for(Variable t: test.feasible) {
            System.out.println(t);
        }

        System.out.println("Fungsi target: " + test.getSolution());
    }
}