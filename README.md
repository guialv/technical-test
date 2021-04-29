# technical-test
**Prueba Técnica para AppGate**

La prueba técnica se desarrolló usando SpringBoot y la documentación se puede consultar desplegando la aplicación y 
accediendo a la ruta 

```
<HOST>/swagger-ui/
```
Para el almacenamiento de los datos utilizados se utilizó una base de datos
embebida H2 cuya configuración se encuentra en el archivo data.sql.

Para usar el servicio hay que utilizar primeramente el endpoint de inicio de sesión así:

```
GET: /login

Usuario: testuser
Contraseña: ClavePruebas
```

Para agregar operandos para su posterior utilización:


```
POST: /operations/operands

{
    "value": 10
}
```
Para ejecutar una oparación matemática use:

```
GET: /operations/{operation}
```

**Consideraciones de Diseño**

Para asegurar la transaccionalidad de las operaciones matemáticas y evitar colisiones
por acceso concurrente, se utilizaron tokens JWT cuyos identificadores se usaron
para almacenas los operandos que el usuario vaya introduciendo durante la sesión
de manera que al ejecutar una operación, se usen solamente los operandos ingresados 
previamente usando ese identificador de sesión.

Aunque por motivos de tiempo y por ser un ejercicio conceptual los servicios de
inicio de sesión y de lógica de negocio se desarrollaron en el mismo servicio Web,
para asegurar la escalabilidad de la aplicación, separaría los dos servicios
en contenedores Docker individuales de tal manera que se pudiera utilizar 
un balanceador de carga que permita usar más de un contenedor bien sea para el 
inicio de sesión (poco probable) o para los cálculos matemáticos (medianamente
probable).

Por causa del tiempo se hicieron pruebas unitarias únicamente de las clases
que contienen lógica de negocio.

Para llevar el control de auditoría del servicio, se agregó una tabla al modelo
de datos pero no fue posible usar el modelo por causa del tiempo.