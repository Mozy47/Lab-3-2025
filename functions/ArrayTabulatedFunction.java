package functions;

public class ArrayTabulatedFunction implements TabulatedFunction {
    private FunctionPoint[] points;

    // Конструктор создания объекта табулированной функции по области определения и количеству точек
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount){
        
        // Проверка на область определения
        if (leftX >= rightX){
            throw new IllegalArgumentException("Левая граница (" + leftX + ") должна быть меньше правой (" + rightX + ")");   
        }
        // Проверка на кол-во точек
        if (pointsCount < 2){
            throw new IllegalArgumentException("Кол-во точек должно быть не меньше двух");
        }
        
        this.points = new FunctionPoint[pointsCount]; // Выделяем память для точек функции
        double step = ((rightX - leftX)/(pointsCount - 1)); // -1 -- Для корректности. Пример: левая точка: 1, правая: 5, 
                                                            //  кол-во точек 5, промежуток между ними 4
        for(int i = 0; i < pointsCount; ++i){
            double x = leftX + i * step; // Рассчитываем x, учитывая "шаг"
            this.points[i] = new FunctionPoint(x, 0); // Фиксируем x для каждой точки
        }
    }
    
    // Конструктор создания объекта таб. функции по области определения и массиву значений функции
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values){
        // Проверка на область определения
        if (leftX >= rightX){
            throw new IllegalArgumentException("Левая граница (" + leftX + ") должна быть меньше правой (" + rightX + ")");   
        }
        // Проверка на кол-во точек
        if (values.length < 2){
            throw new IllegalArgumentException("Кол-во точек должно быть не меньше двух");
        }
        
        int len = values.length; // Найдем длину массива и для удобства запишем в отдельную переменную
        this.points = new FunctionPoint[len];
        double step = ((rightX - leftX)/(len - 1));

        for(int i = 0; i < len; ++i){
            double x = leftX + i * step;
            this.points[i] = new FunctionPoint(x, values[i]);
        }    
    }

    
    // Методы получения крайних значений области определения
    public double getLeftDomainBorder(){
        return points[0].getX();
    }

    public double getRightDomainBorder(){
        return points[points.length - 1].getX();
    }

    // Метод нахождения значения функции по аргументу x
    public double getFunctionValue(double x){
        // Проверям, что x находится в области определения
        if (x >= getLeftDomainBorder() && x <= getRightDomainBorder()){
            for (int i = 0; i < points.length - 1; ++i){
                
                // Фиксируем точки для определения нахождения x
                double x1 = points[i].getX(); 
                double x2 = points[i+1].getX();
                
                // Проверяем где находится x
                if (x1 == x) 
                    return points[i].getY();
                if (x2 == x)
                    return points[i+1].getY();
                if (x > x1 && x < x2){
                    return linearInterpolation(x, x1, x2, points[i].getY(), points[i+1].getY());
                }
            }
        }
        
        return Double.NaN;
    }

    // Приватный метод линейной интерполяции
    private double linearInterpolation(double x, double x1, double x2, double y1, double y2){
        return y1 + (y2 - y1)/(x2 - x1)*(x - x1); // уравнение прямой по двум точкам
    }

    // Метод, возвращающий кол-во точек
    public int getPointsCount(){
        return points.length;
    }

    // Метод создания копии точки по индексу
    public FunctionPoint getPoint(int index){
        // Проверка на номер, выходящий за границы набора точек
        if (index < 0 || index >= points.length){
            throw new FunctionPointIndexOutOfBoundsException(index, points.length);
        }

        return new FunctionPoint(points[index]);
    }

    // Функция замены указанной точки на переданную
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        // Проверка на номер, выходящий за границы набора точек
        if (index < 0 || index >= points.length){
            throw new FunctionPointIndexOutOfBoundsException(index, points.length);
        }

        // Проверяем, лежит ли координата x вне интервала
        if (index > 0 && point.getX() <= points[index - 1].getX()){
            throw  new InappropriateFunctionPointException("X координата (" + point.getX() + ") должна быть больше предыдущей точки (" + points[index - 1].getX() + ")");
        }
        if (index < points.length - 1 && point.getX() >= points[index + 1].getX()){
             throw  new InappropriateFunctionPointException("X координата (" + point.getX() + ") должна быть меньше следующей точки (" + points[index - 1].getX() + ")");
        }
        points[index] = new FunctionPoint(point); 
    }  

    // Метод возвращения абсциссы указанной точки
    public double getPointX(int index){
        // Проверка на номер, выходящий за границы набора точек
        if (index < 0 || index >= points.length){
            throw new FunctionPointIndexOutOfBoundsException(index, points.length);
        }
        
        return points[index].getX();
    }

    // Метод установки нового значения абсциссы у конкретной точки
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        // Проверка на номер, выходящий за границы набора точек
        if (index < 0 || index >= points.length){
            throw new FunctionPointIndexOutOfBoundsException(index, points.length);
        }
        
        // Проверяем, лежит ли координата x вне интервала
        if (index > 0 && x <= points[index - 1].getX()){
            throw  new InappropriateFunctionPointException("X координата (" + x + ") должна быть больше предыдущей точки (" + points[index - 1].getX() + ")");
        }
        if (index < points.length - 1 && x >= points[index + 1].getX()){
             throw  new InappropriateFunctionPointException("X координата (" + x + ") должна быть меньше следующей точки (" + points[index - 1].getX() + ")");
        }
        points[index].setX(x);
    }
    
    // Метод возвращения ординаты указанной точки
    public double getPointY(int index){
        // Проверка на номер, выходящий за границы набора точек
        if (index < 0 || index >= points.length){
            throw new FunctionPointIndexOutOfBoundsException(index, points.length);
        }
        
        return points[index].getY();
    }

    // Метод установки значения ординаты у конкретной точки
    public void setPointY(int index, double y){
        // Проверка на номер, выходящий за границы набора точек
        if (index < 0 || index >= points.length){
            throw new FunctionPointIndexOutOfBoundsException(index, points.length);
        }
        
        points[index].setY(y);
    }

    // Метод удаления указанной точки
    public void deletePoint(int index){
        // Проверка на номер, выходящий за границы набора точек
        if (index < 0 || index >= points.length){
            throw new FunctionPointIndexOutOfBoundsException(index, points.length);
        }

        // Проверяем, что после удаления останется минимум 2 точки
        if (points.length < 3){
            throw new IllegalStateException("Невозможно удалить точку: количество точек не может быть меньше двух");
        }


        FunctionPoint[] newPoints = new FunctionPoint[points.length - 1]; // Создаём новый массив на 1 элемент меньше
        System.arraycopy(points, 0, newPoints, 0, index); // Копируем точки до удаляемой
        System.arraycopy(points, index + 1, newPoints, index, points.length - index - 1); // Копируем точки после удаляемой
        
        points = newPoints;
    }

    // Метод добавления указанной точки
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        int insert_index = 0; // индекс, куда встанет новая точка
        while (insert_index < points.length && point.getX() > points[insert_index].getX()){
            insert_index++;
        }

        // Проверяем на дубликат
        if (insert_index < points.length && point.getX() == points[insert_index].getX())
            throw  new InappropriateFunctionPointException("Точка X с координатой (" + point.getX() + ") уже существует");
        
        // Создаем массив на 1 элемент больше
        FunctionPoint[] newPoints = new FunctionPoint[points.length + 1];

        System.arraycopy(points, 0, newPoints, 0, insert_index);
        newPoints[insert_index] = new FunctionPoint(point); // вставляем новую точку
        System.arraycopy(points, insert_index, newPoints, insert_index + 1, points.length - insert_index);

        points = newPoints;
    }

    

}