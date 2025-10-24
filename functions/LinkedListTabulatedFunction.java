package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction{
    
    // ========= Внутренний класс для узла списка =========

    private class FunctionNode {
        private FunctionPoint point; // точка

        // Ссылки на соседние узлы
        private FunctionNode prev;
        private FunctionNode next;

        
        // Конструкторы
        public FunctionNode(FunctionPoint point){
            this.point = point;
            this.prev = null;
            this.next = null;
        }

        public FunctionNode(FunctionPoint point, FunctionNode prev, FunctionNode next){
            this.point = point;
            this.prev = prev;
            this.next = next;
        }

        // Геттеры и сеттеры
        public FunctionPoint getPoint(){
            return point;
        }

        public void setPoint(FunctionPoint point){
            this.point = point;
        }

        public FunctionNode getPrev(){
            return prev;
        }

        public FunctionNode getNext(){
            return next;
        }

        public void setPrev(FunctionNode prev){
            this.prev = prev;
        }

        public void setNext(FunctionNode next){
            this.next = next;
        }
    }

    // ========= Основной класс LinkedListTabulatedFunction ==========
    
    private FunctionNode head; // Голова списка (не хранит данные)
    private int pointsCount; // Кол-во точек
    private FunctionNode lastAccessed; // Последний доступный узел (для оптимизации)
    private int lastIndex; // Индекс последнего доступа

    // Конструкторы
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount){
        // Проверка на область определения
        if (leftX >= rightX){
            throw new IllegalArgumentException("Левая граница (" + leftX + ") должна быть меньше правой (" + rightX + ")");   
        }
        // Проверка на кол-во точек
        if (pointsCount < 2){
            throw new IllegalArgumentException("Кол-во точек должно быть не меньше двух");
        }
        initializeList(leftX, rightX, pointsCount, null);
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values){
        // Проверка на область определения
        if (leftX >= rightX){
            throw new IllegalArgumentException("Левая граница (" + leftX + ") должна быть меньше правой (" + rightX + ")");   
        }
        // Проверка на кол-во точек
        if (values.length < 2){
            throw new IllegalArgumentException("Кол-во точек должно быть не меньше двух");
        }

        initializeList(leftX, rightX, values.length, values);

    }

    // Инициализация списка 1
    public void initializeList(double leftX, double rightX, int count, double[] values){
        // Создаем голову двухсвязного циклического списка
        head = new FunctionNode(null);
        head.setNext(head);
        head.setPrev(head);

        pointsCount = 0;
        lastAccessed = head;
        lastIndex = -1;

        double step = ((rightX - leftX)/(count - 1));
        for(int i = 0; i < count; ++i){
            double x = leftX + i * step;
            double y = (values == null) ? 0 : values[i];
            addNodeToTail().setPoint(new FunctionPoint(x, y)); // Добавляем элементы в конец списка
        }
    }

    // Метод добавления узла в конец списка
    private FunctionNode addNodeToTail(){
        FunctionNode newNode = new FunctionNode(null);
        
        // Вставляем перед головой
        FunctionNode tail = head.getPrev();

        // Связываем элементы списка
        tail.setNext(newNode);
        newNode.setNext(head);
        newNode.setPrev(tail);
        head.setPrev(newNode);
        
        ++pointsCount;
        lastAccessed = newNode;
        lastIndex = pointsCount - 1;

        return newNode;
    }

    // Оптимизированный доступ к узлу по индексу
    private FunctionNode getNodeByIndex(int index){
        // Проверка на номер, выходящий за границы набора точек
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException(index, pointsCount);
        }

        // Оптимизация: начинаем с последнего доступного узла
        FunctionNode current;
        int startIndex;

        if (lastIndex != -1 && Math.abs(index - lastIndex) < Math.abs(index)){
            // Начинаем с последнего доступного узла
            current = lastAccessed;
            startIndex = lastIndex;
        }
        else {
            // Начинаем с головы
            current = head.getNext();
            startIndex = 0;
        }

        // Двигаемся к нужному узлу
        if (index >= startIndex){
            // Двигаемся вперед
            for (int i = startIndex; i < index; ++i){
                current = current.getNext();
            }
        }
        else {
            // Двигаемся назад
            for (int i = startIndex; i > index; --i){
                current = current.getPrev();
            }
        }

        // Сохраняем для след. вызова
        lastAccessed = current;
        lastIndex = index;
        
        return current;
    }

    private FunctionNode addNodeByIndex(int index){
        // Проверка на номер, выходящий за границы набора точек
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException(index, pointsCount);
        }

        // Особый случай добавления в конец
        if (index == pointsCount){
            return addNodeToTail();
        }

        FunctionNode newNode = new FunctionNode(null);

        // Находим узел, перед которым вставляем
        FunctionNode nextNode = getNodeByIndex(index);
        FunctionNode prevNode = nextNode.getPrev();

        // Связываем элементы
        newNode.setNext(nextNode);
        newNode.setPrev(prevNode);
        nextNode.setPrev(newNode);
        prevNode.setNext(newNode); 

        ++pointsCount;
        lastAccessed = newNode;
        lastIndex = index;

        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index){
        // Проверка на номер, выходящий за границы набора точек
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException(index, pointsCount);
        }

        // Проверяем, что после удаления останется минимум 2 точки
        if (pointsCount < 3){
            throw new IllegalStateException("Невозможно удалить точку: количество точек не может быть меньше двух");
        }

        FunctionNode delNode = getNodeByIndex(index);
        FunctionNode prevNode = delNode.getPrev();
        FunctionNode nextNode = delNode.getNext();

        // Удаляем ссылки на удаляемый узел
        prevNode.setNext(nextNode);
        nextNode.setPrev(prevNode);

        // Очищаем ссылки удаляемого узла
        delNode.setPrev(null);
        delNode.setNext(null);

        --pointsCount;

        // Обновляем lastAccesed
        if (lastIndex == index){
            lastAccessed = (index < pointsCount) ? nextNode : head.getNext();
            lastIndex = (index < pointsCount) ? index : 0;
        }
        else if (lastIndex > index){
            --lastIndex;
        }

        return delNode;
    }


    // ========== Методы TabulatedFunction ==========
    
    // Методы получения крайних значений области определения
    public double getLeftDomainBorder(){
        return head.getNext().getPoint().getX();
    }

    public double getRightDomainBorder(){
        return head.getPrev().getPoint().getX();
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        // Простой поиск с начала
        FunctionNode current = head.getNext();
        while (current != head) {
            FunctionNode next = current.getNext();
            if (next == head) {
                // Последний элемент
                if (x == current.getPoint().getX()) {
                    return current.getPoint().getY();
                }
                break;
            }
            
            double x1 = current.getPoint().getX();
            double x2 = next.getPoint().getX();
            
            if (x == x1) return current.getPoint().getY();
            if (x == x2) return next.getPoint().getY();
            if (x >= x1 && x <= x2) {
                double y1 = current.getPoint().getY();
                double y2 = next.getPoint().getY();
                return linearInterpolation(x, x1, x2, y1, y2);
            }
            
            current = next;
        }
        
        return Double.NaN;
}

    // Линейная интерполяция
    private double linearInterpolation(double x, double x1, double x2, double y1, double y2){
        return y1 + (y2 - y1)/(x2 - x1)*(x - x1); // уравнение прямой по двум точкам
    }

    // Метод, возвращающий кол-во точек
    public int getPointsCount(){
        return pointsCount;
    }

    // Метод получения точки по индексу (возвращает копию)
    public FunctionPoint getPoint(int index){
        return new FunctionPoint(getNodeByIndex(index).getPoint());
    }

    // Функция замены указанной точки на переданную
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        
        FunctionNode newNode = getNodeByIndex(index);

        // Проверяем, лежит ли координата x вне интервала
        if (index > 0 && point.getX() <= getNodeByIndex(index - 1).getPoint().getX()){
            throw  new InappropriateFunctionPointException("X координата (" + point.getX() + ") должна быть больше предыдущей точки (" + getPointX(index - 1) + ")");
        }
        if (index < pointsCount - 1 && point.getX() >= getNodeByIndex(index + 1).getPoint().getX()){
             throw new InappropriateFunctionPointException("X координата (" + point.getX() + ") должна быть меньше следующей точки (" + getPointX(index + 1) + ")");
        }
        newNode.setPoint(new FunctionPoint(point));
    }  

    // Метод возвращения абсциссы указанной точки
    public double getPointX(int index){
        return getNodeByIndex(index).getPoint().getX();
    }

    // Метод установки нового значения абсциссы у конкретной точки
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionPoint point = getPoint(index);
        point.setX(x);
        setPoint(index, point);
    }

    // Метод получения Y по индексу
    public double getPointY(int index){
        return getNodeByIndex(index).getPoint().getY();
    }

    // Метод установки Y по индексу
    public void setPointY(int index, double y){
        getNodeByIndex(index).getPoint().setY(y);
    }

    // Метод удаления точки по индексу
    public void deletePoint(int index) {
        deleteNodeByIndex(index);
    }   

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int insertIndex = 0;

        // Оптимизациия: начинаем поиск с lastAccessed если он есть
        if (lastIndex != -1 && lastAccessed != head){
            double lastX = getPointX(lastIndex);
            if (point.getX() > lastX){
                // Ищем вперед от lastIndex
                insertIndex = lastIndex + 1;
                while (insertIndex < pointsCount && point.getX() > getPointX(insertIndex)){
                    ++insertIndex;
                }
            }
            else {
                // Ищем назад от lastIndex
                insertIndex = lastIndex;
                while (insertIndex > 0 && point.getX() < getPointX(insertIndex - 1)) {
                insertIndex--;
                }
            }
        } else {
        // Обычный поиск с начала
        while (insertIndex < pointsCount && point.getX() > getPointX(insertIndex)) {
            insertIndex++;
            }
        }

        // Проверяем на дубликат
        if (insertIndex < pointsCount && point.getX() == getPointX(insertIndex)) {
            throw new InappropriateFunctionPointException("Точка с координатой X = " + point.getX() + " уже существует");
        }

        // Вставляем новую точку
        FunctionNode newNode = addNodeByIndex(insertIndex);
        newNode.setPoint(new FunctionPoint(point));
    }

}





