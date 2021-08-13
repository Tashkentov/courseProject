package algorithms;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
            runSort();
        }

    /**
     * Запускає вкладений цикл тестів, які викликають ParallelMergeSorter та
     * потім перевіряє масив, щоб забезпечити правильне сортування
     */
    public static void runSort() {
            int SIZE = 1000,   // початкова довжина масиву для сортування
                    ROUNDS = 15,
                    availableThreads = (Runtime.getRuntime().availableProcessors())*2;

            Integer[] a;

            Comparator<Integer> comp = new Comparator<Integer>() {
                public int compare(Integer d1, Integer d2) {
                    return d1.compareTo(d2);
                }
            };

            System.out.printf("\nMax number of threads == %d\n\n", availableThreads);
            for (int i = 1; i <= availableThreads; i*=2) {
                if (i == 1) {
                    System.out.printf("%d Thread:\n", i);
                }
                else {
                    System.out.printf("%d Threads:\n", i);
                }
                for (int j = 0, k = SIZE; j < ROUNDS; ++j, k*=2) {
                    a = createRandomArray(k);
                    // запускаємо алгоритм і час, протягом якого сортуються масиви
                    long startTime = System.currentTimeMillis();
                    ParallelMergeSort.sort(a, comp, availableThreads);
                    long endTime = System.currentTimeMillis();

                    if (!isSorted(a, comp)) {
                        throw new RuntimeException("not sorted afterward: " + Arrays.toString(a));
                    }

                    System.out.printf("%10d elements  =>  %6d ms \n", k, endTime - startTime);
                }
                System.out.print("\n");
            }
        }

    /**
     * Повертає значення true, якщо даний масив розміщено в порядку сортування за зростанням.
     *
     * @param a - масив для вивчення
     * @param comp компаратора для порівняння елементів масиву
     * @return true, якщо даний масив відсортовано, false - не відсортовано
     */
    public static <E> boolean isSorted(E[] a, Comparator<? super E> comp) {
            for (int i = 0; i < a.length - 1; i++) {
                if (comp.compare(a[i], a[i + 1]) > 0) {
                    System.out.println(a[i] + " > " + a[i + 1]);
                    return false;
                }
            }
            return true;
        }

    // Випадкова перестановка елементів даного масиву.
        public static <E> void shuffle(E[] a) {
            for (int i = 0; i < a.length; i++) {
                // перемістити елемент i до випадкового індексу в [i .. length-1]
                int randomIndex = (int) (Math.random() * a.length - i);
                swap(a, i, i + randomIndex);
            }
        }

    // Поміняє місцями значення за двома даними індексами в даному масиві.
        public static final <E> void swap(E[] a, int i, int j) {
            if (i != j) {
                E temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }

    // Створює масив заданої довжини, заповнює його випадковим чином і повертає його.
        public static Integer[] createRandomArray(int length) {
            Integer[] a = new Integer[length];
            Random rand = new Random(System.currentTimeMillis());
            for (int i = 0; i < a.length; i++) {
                a[i] = rand.nextInt(1000000);
            }
            return a;
        }
    }
