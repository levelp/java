Spring MVC приложения в Intellj IDEA
------------------------------------

* Использование циклов в JSP: <http://localhost:8080/list>
* Таблица умножения: <http://localhost:8080/table?size=22>
 
Настройка Tomcat
----------------

Добавить в раздел **tomcat-users** в **<tomcat>/conf/tomcat-users.xml**:

``` xml
  <role rolename="manager-gui"/>
  <user username="admin" password="123" roles="manager-gui"/> 
```

Запускаем Tomcat: 
* **catalina.bat run** для Windows 
* **catalina.sh run** для Linux/Mac

Открываем: <http://localhost:8080/manager/html>

Вводим логин и пароль: admin / 123

В разделе **Deploy / WAR file to deploy** выбираем наш **war**-файл и нажимаем Deploy.

