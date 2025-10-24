import functions.ArrayTabulatedFunction;
import functions.FunctionPoint;
import functions.FunctionPointIndexOutOfBoundsException;
import functions.InappropriateFunctionPointException;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;


public class Main {
    public static void main(String[] args){
        double[] values = {0, 1, 4, 9, 16, 25, 36, 49, 64, 81, 100};
        System.out.println("=== Тестирование ArrayTabulatedFunction ===");
        testFunction(new ArrayTabulatedFunction(0, 10, values));
        
        System.out.println("\n=== Тестирование LinkedListTabulatedFunction ===");
        TabulatedFunction function = new LinkedListTabulatedFunction(0, 10, values);
    
        testFunction(new LinkedListTabulatedFunction(0, 10, values));
        
        System.out.println("\n=== Тестирование исключений ===");
        testExceptions();
    }
    
    public static void testFunction(TabulatedFunction function) {
        System.out.println("Область определения: [" + function.getLeftDomainBorder() + 
                          ", " + function.getRightDomainBorder() + "]");
        System.out.println("Количество точек: " + function.getPointsCount());
        
        // Вывод всех точек
        System.out.println("Точки функции:");
        for (int i = 0; i < function.getPointsCount(); i++) {
            System.out.println("  (" + function.getPointX(i) + ", " + function.getPointY(i) + ")");
        }
        
        // Тестирование вычисления значений
        System.out.println("Значения функции:");
        for (double x = function.getLeftDomainBorder(); x <= function.getRightDomainBorder(); ++x) {
            System.out.println("  f(" + x + ") = " + function.getFunctionValue(x));
        }
        
        // Тестирование модификации
        try {
            function.setPointY(2, 100);
            System.out.println("После изменения Y в точке 2: " + function.getPoint(2));
        } catch (Exception e) {
            System.out.println("Ошибка при изменении точки: " + e.getMessage());
        }
    }
    
    public static void testExceptions() {
        System.out.println("1. Тестирование некорректных границ:");
        try {
            TabulatedFunction func = new ArrayTabulatedFunction(10, 5, 5);
        } catch (IllegalArgumentException e) {
            System.out.println("   Поймано исключение: " + e.getMessage());
        }
        
        System.out.println("2. Тестирование недостаточного количества точек:");
        try {
            TabulatedFunction func = new LinkedListTabulatedFunction(0, 5, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("   Поймано исключение: " + e.getMessage());
        }
        
        System.out.println("3. Тестирование выхода за границы индекса:");
        try {
            TabulatedFunction func = new ArrayTabulatedFunction(0, 5, 3);
            func.getPoint(10);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("   Поймано исключение: " + e.getMessage());
        }
        
        System.out.println("4. Тестирование некорректной координаты X:");
        try {
            TabulatedFunction func = new LinkedListTabulatedFunction(0, 5, 3);
            func.setPointX(1, -1); // Должно быть между 0 и 2.5
        } catch (InappropriateFunctionPointException e) {
            System.out.println("   Поймано исключение: " + e.getMessage());
        }
        
        System.out.println("5. Тестирование дублирования точки:");
        try {
            TabulatedFunction func = new ArrayTabulatedFunction(0, 4, 3);
            func.addPoint(new FunctionPoint(2, 50));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("   Поймано исключение: " + e.getMessage());
        }
        
        System.out.println("6. Тестирование удаления при недостаточном количестве точек:");
        try {
            TabulatedFunction func = new LinkedListTabulatedFunction(0, 3, 3);
            func.deletePoint(0);
            func.deletePoint(0); // Осталась 1 точка - должно бросить исключение
        } catch (IllegalStateException e) {
            System.out.println("   Поймано исключение: " + e.getMessage());
        }
        
        System.out.println("7. Тестирование вычисления вне области определения:");
        TabulatedFunction func = new ArrayTabulatedFunction(0, 5, 3);
        double result = func.getFunctionValue(-10);
        System.out.println("   f(-10) = " + result + " (ожидается NaN)");
        
        System.out.println("8. Тестирование добавления точки с сохранением порядка:");
        try {
            TabulatedFunction testFunc = new LinkedListTabulatedFunction(0, 4, 3);
            System.out.println("   До добавления:");
            for (int i = 0; i < testFunc.getPointsCount(); i++) {
                System.out.println("     (" + testFunc.getPointX(i) + ", " + testFunc.getPointY(i) + ")");
            }
            
            testFunc.addPoint(new FunctionPoint(1.5, 25));
            System.out.println("   После добавления точки (1.5, 25):");
            for (int i = 0; i < testFunc.getPointsCount(); i++) {
                System.out.println("     (" + testFunc.getPointX(i) + ", " + testFunc.getPointY(i) + ")");
            }
        } catch (Exception e) {
            System.out.println("   Ошибка: " + e.getMessage());
        }
    }
}
    
    
    
    
    
    
    
    
    
    
        //     // Создадим простейшую функцию y = x^2 на отрезке [0, 5]
    //     double[] values = {0, 1, 4, 9, 16, 25};

    //     TabulatedFunction f = new TabulatedFunction(0.0, 5.0, values);
        
    //     System.out.println("ФУНКЦИЯ x^2");
    //     functionInfo(f);
    //     testFunctionValue(f);
    //     testDeletePoint(f);
    //     testAddPoint(f);
    //     testPointModification(f); 
    // }

    // public static void functionInfo(TabulatedFunction f){
    //     System.out.println("Область определения: [" + f.getLeftDomainBorder() + ", " + f.getRightDomainBorder() + "]");
    //     System.out.println("Количество точек: " + f.getPointsCount());
        
    //     System.out.println("Точки функции:");
    //     for (int i = 0; i < f.getPointsCount(); i++) {
    //         System.out.println("[" + i + "] " + f.getPointX(i) + " " + f.getPointY(i));
    //     }
    // }

    // public static void testFunctionValue(TabulatedFunction f){
    //     double[] testPointsX = {-1, 0, 0.5, 1, 1.45, 1.6, 5, 6};

    //     for (double x: testPointsX){
    //         double y = f.getFunctionValue(x);
    //         if (Double.isNaN(y))
    //             System.out.println("Значение функции не определено (аргумент x, мне области определения функции)");
    //         else
    //             System.out.println("f(x) = " + y);

    //     }
    // }

    // // Тестирование удаления точки
    // public static void testDeletePoint(TabulatedFunction f) {
    //     System.out.println("Количество точек до удаления: " + f.getPointsCount());
        
    //     // Удаляем точку с индексом 2
    //     f.deletePoint(2);
    //     System.out.println("Количество точек после удаления: " + f.getPointsCount());
        
    //     System.out.println("Все точки после удаления:");
    //     for (int i = 0; i < f.getPointsCount(); i++) {
    //         System.out.println("[" + i + "] " + f.getPointX(i) + " " + f.getPointY(i));
    //     }
    // }

    // // Тестирование добавления точки
    // public static void testAddPoint(TabulatedFunction f) {
    //     System.out.println("Количество точек до добавления: " + f.getPointsCount());
        
    //     // Добавляем точку в середину
    //     f.addPoint(new FunctionPoint(2.5, 6.25));
    //     System.out.println("Количество точек после добавления точки (2.5; 6.25): " + f.getPointsCount());
        
    //     // Добавляеми точку с существующим X (не должна добавиться)
    //     f.addPoint(new FunctionPoint(3.0, 100.0));
    //     System.out.println("Количество точек после попытки добавить точку с существующим X = 3.0: " + f.getPointsCount());
        
    //     System.out.println("Все точки после добавления:");
    //     for (int i = 0; i < f.getPointsCount(); i++) {
    //         System.out.println("[" + i + "] " + f.getPointX(i) + " " + f.getPointY(i));
    //     }
    // }

    // // Тестирование изменения точек
    // public static void testPointModification(TabulatedFunction f) {
    //     System.out.println("До изменения точки с индексом 2:");
    //     System.out.println("[" + 2 + "] " + f.getPointX(2) + " " + f.getPointY(2));
        
    //     // изменяем Y точки
    //     f.setPointY(2, 10.0);
    //     System.out.println("После изменения Y точки 2 на 10.0:");
    //      System.out.println("[" + 2 + "] " + f.getPointX(2) + " " + f.getPointY(2));
        
    //     // Изменяем X точки (неуспешно - нарушит порядок)
    //     System.out.println("Пытаемся изменить X точки 2 на 5.0 (не должно измениться):");
    //     f.setPointX(2, 5.0); // Не изменится, т.к. 5.0 > следующей точки
    //      System.out.println("[" + 2 + "] " + f.getPointX(2) + " " + f.getPointY(2));
        
    //     // Изменяем X точки на допустимое значение
    //     f.setPointX(2, 2.2);
    //     System.out.println("После изменения X точки 2 на 2.2 (успешно):");
    //     System.out.println("[" + 2 + "] " + f.getPointX(2) + " " + f.getPointY(2));

    //     FunctionPoint newPoint = new FunctionPoint(2.2, 5.0);
    //     f.setPoint(2, newPoint);
    //     System.out.println("После setPoint (заменяем указанную точку на переданную):");
    //     System.out.println("[" + 2 + "] " + f.getPointX(2) + " " + f.getPointY(2));
    // }

