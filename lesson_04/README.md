Системы сборки Java
===================

ant
---
**ant** - утилита для автоматизации процесса сборки программного продукта. Является платформонезависимым аналогом утилиты make, где все команды записываются в XML-формате.
**Императивная** сборка проекта.

Пример **build.xml**:
``` xml 
<?xml version="1.0"?>
<project default="build" basedir=".">
    <property name="name" value="AntBuildJar"/>
    <property name="src.dir" location="${basedir}/src"/>
    <property name="build" location="${basedir}/build"/>
    <property name="build.classes" location="${build}/classes"/>
    <path id="libs.dir">
	<fileset dir="lib" includes="**/*.jar"/>
    </path>
    <!-- Сборка приложения -->
    <target name="build" depends="clean" description="Builds the application">
        <!-- Создание директорий -->
        <mkdir dir="${build.classes}"/>
 
        <!-- Компиляция исходных файлов -->
        <javac srcdir="${src.dir}"
               destdir="${build.classes}"
               debug="false"
               deprecation="true"
               optimize="true" >
            <classpath refid="libs.dir"/>
        </javac>
 
        <!-- Копирование необходимых файлов -->
        <copy todir="${build.classes}">
            <fileset dir="${src.dir}" includes="**/*.*" excludes="**/*.java"/>
        </copy>
 
        <!-- Создание JAR-файла -->
        <jar jarfile="${build}/${name}.jar">
            <fileset dir="${build.classes}"/>
        </jar>
    </target>
 
    <!-- Очистка -->
    <target name="clean" description="Removes all temporary files">
        <!-- Удаление файлов -->
        <delete dir="${build.classes}"/>
    </target>
</project>
```

maven
-----
Фреймворк для автоматизации сборки проектов, специфицированных на XML-языке POM (Project Object Model).
Декларативная сборка проекта.

``` xml
<project>
  <!-- версия модели для POM-ов Maven 2.x всегда 4.0.0 -->
  <modelVersion>4.0.0</modelVersion>
 
  <!-- координаты проекта, то есть набор значений, который
       позволяет однозначно идентифицировать этот проект -->
 
  <groupId>com.mycompany.app</groupId>
  <artifactId>my-app</artifactId>
  <version>1.0</version>
 
  <!-- зависимости от библиотек -->
 
  <dependencies>
    <dependency>
 
      <!-- координаты необходимой библиотеки -->
 
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
 
      <!-- эта библиотека используется только для запуска и компилирования тестов -->
 
      <scope>test</scope>
 
    </dependency>
  </dependencies>
</project>
```

**Структура maven-проекта**
Корневой каталог проекта: файл **pom.xml** и все дальнейшие подкаталоги
* src: все исходные файлы
 * src/main: исходные файлы собственно для продукта
   * src/main/java: Java-исходный текст
   * src/main/resources: другие файлы, которые используются при компиляции или исполнении, например Properties-файлы
 * src/test: исходные файлы, необходимые для организации автоматического тестирования
   * src/test/java: JUnit-тест-задания для автоматического тестирования
* target: все создаваемые в процессе работы Мавена файлы
 * target/classes: компилированные Java-классы

gradle
------
Система автоматической сборки, построенная на принципах Apache Ant и Apache Maven, но предоставляющая DSL на языке Groovy вместо XML.

Внутренние классы. Параметризация. MVC, CRUD, DAO
=================================================

Внутренние классы (статические, нестатические, анонимные)
---------------------------------------------------------

Реализация анонимного маппера. Параметризация. Стирание типов. Ограничения
--------------------------------------------------------------------------

Слои приложения. MVC. CRUD. DAO
-------------------------------



Домашнее задание 
----------------
* Создать интерфейс DAO работы с моделью.
* Реализовать этот интерфейс через коллекции.

Литература: 
-----------
* Структуры данных в картинках.
* Часто-задаваемые-на-собеседованиях-вопросы-по-классам-коллекций.
* Коллекции Java (Java Collections Framework)
* Пакет java.util

