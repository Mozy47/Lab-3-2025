import functions.ArrayTabulatedFunction;
import functions.FunctionPoint;
import functions.FunctionPointIndexOutOfBoundsException;
import functions.InappropriateFunctionPointException;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;

public class Main {
    public static void main(String[] args) throws InappropriateFunctionPointException  {
        double[] values = {0, 1, 4, 9, 16, 25, 36, 49, 64, 81, 100};
        System.out.println("=== Тестирование ArrayTabulatedFunction ===");
        testFunction(new ArrayTabulatedFunction(0, 10, values));
        
        System.out.println("\n=== Тестирование LinkedListTabulatedFunction ===");
        TabulatedFunction function = new LinkedListTabulatedFunction(0, 10, values);
    
        testFunction(new LinkedListTabulatedFunction(0, 10, values));
        
        System.out.println("\n=== Тестирование исключений ===");
        testExceptions();
    }
    
    public static void testFunction(TabulatedFunction function) throws InappropriateFunctionPointException {
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
        for (double x = function.getLeftDomainBorder(); x <= function.getRightDomainBorder(); x = x + 1.2) {
            System.out.println("  f(" + x + ") = " + function.getFunctionValue(x));
        }
        System.out.println("-------------------------------------------------------");
        // Тестирование модификации
        try {
            function.setPointY(2, 100);
            System.out.println("После изменения Y в точке 2: " + function.getPoint(2));
        } catch (Exception e) {
            System.out.println("Ошибка при изменении точки: " + e.getMessage());
        }
        System.out.println("-------------------------------------------------------");
        // Тестирование удаления точки
        System.out.println("Количество точек до удаления: " + function.getPointsCount());
        // Удаляем точку с индексом 2
        function.deletePoint(2);
        System.out.println("Количество точек после удаления: " + function.getPointsCount());
        
        System.out.println("Все точки после удаления:");
        for (int i = 0; i < function.getPointsCount(); i++) {
            System.out.println("[" + i + "] " + function.getPointX(i) + " " + function.getPointY(i));
        }
        System.out.println("-------------------------------------------------------");

        //Тестирование добавления точки
        System.out.println("Количество точек до добавления: " + function.getPointsCount());
        
        // Добавляем точку в середину
        try {
        function.addPoint(new FunctionPoint(2.5, 6.25));
        System.out.println("Количество точек после добавления точки (2.5; 6.25): " + function.getPointsCount());
        
        // Добавляеми точку с существующим X (не должна добавиться)
        function.addPoint(new FunctionPoint(3.0, 100.0));
        System.out.println("Количество точек после попытки добавить точку с существующим X = 3.0: " + function.getPointsCount());
        
        System.out.println("Все точки после добавления:");
        for (int i = 0; i < function.getPointsCount(); i++) {
            System.out.println("[" + i + "] " + function.getPointX(i) + " " + function.getPointY(i));
        }
        } catch (InappropriateFunctionPointException e) {
        System.out.println("Поймано исключение: " + e.getMessage());  
        }
        System.out.println("-------------------------------------------------------");
        // Тестирование изменения точек
        System.out.println("До изменения точки с индексом 2:");
        System.out.println("[" + 2 + "] " + function.getPointX(2) + " " + function.getPointY(2));
        
        // изменяем Y точки
        function.setPointY(2, 10.0);
        System.out.println("После изменения Y точки 2 на 10.0:");
        System.out.println("[" + 2 + "] " + function.getPointX(2) + " " + function.getPointY(2));
        System.out.println("-------------------------------------------------------");
        // Изменяем X точки (неуспешно - нарушит порядок)
        try {
        System.out.println("Пытаемся изменить X точки 2 на 5.0 (не должно измениться):");
        function.setPointX(2, 5.0); // Не изменится, т.к. 5.0 > следующей точки
        System.out.println("[" + 2 + "] " + function.getPointX(2) + " " + function.getPointY(2));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Поймано исключение: " + e.getMessage());  
        }
        System.out.println("-------------------------------------------------------");
        // Изменяем X точки на допустимое значение
        function.setPointX(2, 2.2);
        System.out.println("После изменения X точки 2 на 2.2 (успешно):");
        System.out.println("[" + 2 + "] " + function.getPointX(2) + " " + function.getPointY(2));
        System.out.println("-------------------------------------------------------");

        FunctionPoint newPoint = new FunctionPoint(2.2, 5.0);
        function.setPoint(2, newPoint);
        System.out.println("После setPoint (заменяем указанную точку на переданную):");
        System.out.println("[" + 2 + "] " + function.getPointX(2) + " " + function.getPointY(2));
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
    
    
    
    
    
    


//     // Тестирование изменения точек
//     public static void testPointModification(TabulatedFunction f) {
//         System.out.println("До изменения точки с индексом 2:");
//         System.out.println("[" + 2 + "] " + f.getPointX(2) + " " + f.getPointY(2));
        
//         // изменяем Y точки
//         f.setPointY(2, 10.0);
//         System.out.println("После изменения Y точки 2 на 10.0:");
//          System.out.println("[" + 2 + "] " + f.getPointX(2) + " " + f.getPointY(2));
        
//         // Изменяем X точки (неуспешно - нарушит порядок)
//         System.out.println("Пытаемся изменить X точки 2 на 5.0 (не должно измениться):");
//         f.setPointX(2, 5.0); // Не изменится, т.к. 5.0 > следующей точки
//          System.out.println("[" + 2 + "] " + f.getPointX(2) + " " + f.getPointY(2));
        
//         // Изменяем X точки на допустимое значение
//         f.setPointX(2, 2.2);
//         System.out.println("После изменения X точки 2 на 2.2 (успешно):");
//         System.out.println("[" + 2 + "] " + f.getPointX(2) + " " + f.getPointY(2));

//         FunctionPoint newPoint = new FunctionPoint(2.2, 5.0);
//         f.setPoint(2, newPoint);
//         System.out.println("После setPoint (заменяем указанную точку на переданную):");
//         System.out.println("[" + 2 + "] " + f.getPointX(2) + " " + f.getPointY(2));
//     }
// }

