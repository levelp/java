// Исключения в Java двух видов:
// * Наследники от класса **Exception**
//  надо указывать .
// * Наследники от класса **RuntimeException**
//  не надо указывать throws.

/**
 * Любое значение X
 */
public class AnyXException extends RuntimeException {
}
