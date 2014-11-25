Date, Calendar, DateFormat (Format). Joda-Time
----------------------------------------------


Реализация DAO через сериализацию
---------------------------------


JMM. Потоки выполнения. Synchronize, final, volatile
----------------------------------------------------

*  FileStorage: сделать JUnit тесты DAO.


Домашнее задание
---------------- 
* Сохранение в файл и загрузка объекта из файла
** object.txt ** 
```
MyClass
str: Строчка
i: 23
d: 1.2
b: true
```
* Дополнить в FileStorage реализацию методов save/load.
* Дополнить реализацию FileStorage методами delete, update, getList.
* Загрузить и настроить Tomcat.
**tomcat\conf\tomcat-users.xml**

``` xml
  <role rolename="manager-gui"/>
  <user username="admin" password="123" roles="manager-gui"/>
```
* Изучить примеры к Tomcat
  * tomcat\webapps\examples\jsp\jsp2\el\composite.jsp - путь к примеру в папке.
  * http://localhost:8080/examples/jsp/jsp2/el/composite.jsp - путь к примеру в браузере.

Литература 
----------
* JMM
* Java Concurrency in Practice - http://www.amazon.com/Java-Concurrency-Practice-Brian-Goetz/dp/0321349601
* JDK concurrent package
* Обзор java.util.concurrent.
* Синхронизация потоков
