package functions;

public class FunctionPoint {
    // Переменные для хранения координат точкии
    private double x;
    private double y;

    // Конструктор создания объекта с заданными координатами
    public FunctionPoint(double x, double y){
        this.x = x; // this обращается к полю текущего объекта
        this.y = y;
    }

    // Конструктор копирования существующей точки
    public FunctionPoint(FunctionPoint point){
        this.x = point.x;
        this.y = point.y;
    }
    
    // Конструктор создания точки с координатами (0 , 0)
    public FunctionPoint(){
        this.x = 0;
        this.y = 0;
    }

    // Геттеры
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }

    // Сеттеры
    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }
 
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
