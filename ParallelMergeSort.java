package algorithms;
import java.util.*;

public class ParallelMergeSort extends Thread {
    public static <E> void sort(E[] a, Comparator<? super E> comp, int threads) {
        parallelMergeSort(a, 0, a.length-1, comp, threads);
    }

    /**
     * Сортує діапазон масиву, використовуючи алгоритм сортування злиттям.
     *
     * @param a - масив для сортування
     * @param from перший індексу діапазону для сортування
     * @param to останній індексу діапазону для сортування
     * @param comp компаратор для порівняння елементів масиву
     */
    private static <E> void mergeSort(E[] a, int from, int to,
                                      Comparator<? super E> comp) {
        if (from == to) {
            return;
        }
        if (to - from >0) {
            int mid = (from + to) / 2;

            // Сортування першої та другої половини
            mergeSort(a, from, mid, comp);
            mergeSort(a, mid + 1, to, comp);
            merge(a, from, mid, to, comp);
        }
    }
    /**
     * Бере масив і зливає, сортує його паралельно, в декількох потоках
     *
     * @param <E>
     * @param a - це масив для сортування
     * @param from - це перше значення для сортування
     * @param to - це останнє значення для сортування
     * @param comp - це компаратор, який перевіряє два числа
     * @param availableThreads - це кількість потоків, які використовуються
     */
    private static <E> void parallelMergeSort(E[] a, int from, int to, Comparator<? super E> comp, int availableThreads){
        if (to - from > 0){
            if (availableThreads <=1) {
                mergeSort(a, from, to, comp);
            }
            else {
                int middle = to/2;

                Thread firstHalf = new Thread(){
                    public void run(){
                        parallelMergeSort(a, from, middle, comp, availableThreads - 1);
                    }
                };
                Thread secondHalf = new Thread(){
                    public void run(){
                        parallelMergeSort(a, middle + 1, to, comp, availableThreads - 1);
                    }
                };

                firstHalf.start();
                secondHalf.start();

                try {
                    firstHalf.join();
                    secondHalf.join();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }

                merge(a, from, middle, to, comp);
            }
        }
    }

    /**
     * Об'єднує два сусідні піддіапазони масиву
     *
     * @param a - масив із записами для об’єднання
     * @param from індекс першого елемента першого діапазону
     * @param mid індекс останнього елемента першого діапазону
     * @param to індекс останнього елемента другого діапазону
     * @param comp компаратора для порівняння елементів масиву
     */
    @SuppressWarnings("unchecked")
    private static <E> void merge(E[] a,
                                  int from, int mid, int to, Comparator<? super E> comp) {
        int n = to - from + 1;
        // Розмір діапазону для об’єднання

        // Об’єднання двох половин у тимчасовий масив b
        Object[] b = new Object[n];

        int i1 = from;
        // Наступний елемент для розгляду в першому діапазоні
        int i2 = mid + 1;
        // Наступний елемент для розгляду у другому діапазоні
        int j = 0;
        // Наступна відкрита позиція в b

        // Поки ні i1, ні i2 не дішйли кінця, рухайємося до меншого елемента у b
        while (i1 <= mid && i2 <= to) {
            if (comp.compare(a[i1], a[i2]) < 0) {
                b[j] = a[i1];
                i1++;
            } else {
                b[j] = a[i2];
                i2++;
            }
            j++;
        }

        // Лише один із двох циклів while виконується нижче
        // Скопіювати всі залишки записів першої половини
        while (i1 <= mid) {
            b[j] = a[i1];
            i1++;
            j++;
        }

        // Скопіювати всі залишки записів другої половини
        while (i2 <= to) {
            b[j] = a[i2];
            i2++;
            j++;
        }

        // Копіювати назад з тимчасового масиву
        for (j = 0; j < n; j++) {
            a[from + j] = (E) b[j];
        }
    }
}



