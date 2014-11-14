import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Тестирование стека:
 * push(value) - поместить значение на вершину стека
 * pop() - вернуть значение вершины стека и удалить
 * его из стека
 * size() - текущий размер стека
 */
public class StackTest {

    /**
     * Создаём экземпляр стека, добавляем
     * элементы и проверяем размер
     */
    @Test
    public void addElementAndCheckSize() {
        MyStack<Integer> myStack = new MyStack<Integer>();
        assertEquals("Сначала стек пуст",
                0, myStack.size());

        myStack.push(10);
        assertEquals("В стеке один элемент",
                1, myStack.size());

        myStack.push(20);
        assertEquals(2, myStack.size());

        myStack.push(344);
        assertEquals(3, myStack.size());

        myStack.push(-53);
        assertEquals(4, myStack.size());
    }

    /**
     * Последовательность операций push / pop
     */
    @Test
    public void pushPopSequence() {
        MyStack<Integer> myStack = new MyStack<Integer>();
        myStack.push(10);
        assertEquals(10, myStack.pop().intValue());

        myStack.push(23);
        myStack.push(333);
        assertEquals(333, myStack.pop().intValue());
        assertEquals(23, myStack.pop().intValue());
    }

    @Test
    public void pushPopSequenceStrings() {
        MyStack<String> myStack = new MyStack<String>();
        myStack.push("AA");
        assertEquals("AA", myStack.pop());

        myStack.push("BB");
        myStack.push("CC");
        assertEquals("CC", myStack.pop());
        assertEquals("BB", myStack.pop());
    }
}
