# Price Query Service

Servicio REST desarrollado con Java 17 y Spring Boot 3 para la prueba técnica de consulta de precios aplicables.

## Tecnologías

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Hibernate
- H2 Database
- Maven
- OpenAPI YAML
- Swagger UI / Springdoc OpenAPI
- JUnit 5
- AssertJ
- Mockito
- REST Assured
- Docker

## Arquitectura

El proyecto sigue arquitectura hexagonal, en combinación con arquitectura limpia y DDD, y se compone de esta forma:

```text
pricesservice
    │   PricesServiceApplication.java
    │   
    ├───application
    │   ├───port
    │   │   ├───in
    │   │   │       GetApplicablePriceUseCase.java
    │   │   │       
    │   │   └───out
    │   │           PriceRepository.java
    │   │           
    │   ├───query
    │   │       GetApplicablePriceQuery.java
    │   │       
    │   ├───result
    │   │       ApplicablePriceResult.java
    │   │       
    │   └───service
    │           GetApplicablePriceService.java
    │           
    ├───domain
    │   ├───error
    │   │       NoApplicablePriceException.java
    │   │       
    │   └───model
    │           ApplicablePrices.java
    │           BrandId.java
    │           DateRange.java
    │           Money.java
    │           Price.java
    │           PriceList.java
    │           Priority.java
    │           ProductId.java
    │           
    └───infrastructure
        ├───adapter
        │   ├───in
        │   │   └───web
        │   │           ErrorResponse.java
        │   │           GlobalExceptionHandler.java
        │   │           PriceController.java
        │   │           PriceResponse.java
        │   │           PriceRestMapper.java
        │   │           
        │   └───out
        │       └───persistence
        │               PriceEntity.java
        │               PriceJpaAdapter.java
        │               PriceJpaRepository.java
        │               PricePersistenceMapper.java
        │               
        └───config
                AdapterConfig.java
                UseCaseConfig.java
```

- `domain`: modelo de dominio puro, sin Spring ni JPA.
- `application`: casos de uso y puertos.
- `infrastructure`: adaptadores REST, persistencia JPA y configuración.

La regla de selección de precio por mayor prioridad se encuentra en el dominio.

### Uso de configuradores para puertos y adaptadores

Se han incluido clases de configuración explícitas:

```
src/main/java/com/test/inditex/pricesservice/infrastructure/config/UseCaseConfig.java
src/main/java/com/test/inditex/pricesservice/infrastructure/config/AdapterConfig.java
```

Estas clases se encargan de instanciar y conectar casos de uso, puertos y adaptadores.

La decisión evita anotar directamente servicios de aplicación o clases de dominio con estereotipos de Spring. 
De esta forma, la capa de aplicación se mantiene más limpia y la infraestructura asume la responsabilidad de componer las dependencias.

## Datos iniciales

La aplicación carga los datos iniciales desde:

```
src/main/resources/data.sql
```

Estos son los registros iniciales insertados en la tabla `PRICES`:

| Brand ID | Start Date | End Date | Price List | Product ID | Priority | Price | Currency |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 1 | 2020-06-14 00:00:00 | 2020-12-31 23:59:59 | 1 | 35455 | 0 | 35.50 | EUR |
| 1 | 2020-06-14 15:00:00 | 2020-06-14 18:30:00 | 2 | 35455 | 1 | 25.45 | EUR |
| 1 | 2020-06-15 00:00:00 | 2020-06-15 11:00:00 | 3 | 35455 | 1 | 30.50 | EUR |
| 1 | 2020-06-15 16:00:00 | 2020-12-31 23:59:59 | 4 | 35455 | 1 | 38.95 | EUR |

Cuando varios precios son aplicables para una misma fecha, producto y marca, se selecciona el de mayor prioridad.

Por ejemplo, para la petición:

```
applicationDate = 2020-06-14T16:00:00
productId = 35455
brandId = 1
```

son aplicables las tarifas `1` y `2`, pero se devuelve la tarifa `2` porque tiene mayor prioridad.

## Ejecutar en local

```bash
mvn spring-boot:run
```

## Ejecutar tests

Para lanzar toda la suite de tests, se ha de lanzar el siguiente comando.

```bash
mvn test
```

Si solo se pretende ejecutar los tests E2E, ejecutamos el mismo comando añadiendo lo siguiente.

```bash
mvn test -Dtest=PriceApiE2ETest
```
Los tests se desglosan de la siguiente manera:

| Tipo de test                         | Objetivo |
|--------------------------------------| --- |
| Tests unitarios de dominio           | Validan reglas de negocio e invariantes sin Spring |
| Tests unitarios de aplicación        | Validan la orquestación del caso de uso con puertos mockeados |
| Tests unitarios de mapper            | Validan transformaciones entre capas |
| Tests unitarios de controlador       | Validan el adaptador REST |
| Tests de integración de persistencia | Validan el adaptador JPA contra H2 |
| Tests E2E                            | Validan el flujo HTTP completo con la aplicación levantada |

Los tests E2E cubren los cinco casos pedidos en la prueba técnica:

| Caso | Application Date | Product ID | Brand ID | Expected Price List | Expected Price |
| --- | --- | --- | --- | --- | --- |
| 1 | 2020-06-14T10:00:00 | 35455 | 1 | 1 | 35.50 |
| 2 | 2020-06-14T16:00:00 | 35455 | 1 | 2 | 25.45 |
| 3 | 2020-06-14T21:00:00 | 35455 | 1 | 1 | 35.50 |
| 4 | 2020-06-15T10:00:00 | 35455 | 1 | 3 | 30.50 |
| 5 | 2020-06-16T21:00:00 | 35455 | 1 | 4 | 38.95 |

Además, se han añadido escenarios de error como:

- Precio no encontrado.
- Formato inválido de fecha.
- Parámetros obligatorios ausentes.

### SQL específico para los tests e2e

Se incluye un fichero SQL específico para los tests E2E.

```
src/test/resources/test-e2e/prices.sql
```

## Ejecutar con Docker

```bash
docker build -t prices-service .
docker run -p 8080:8080 prices-service
```

Dado que se usa la base de datos en memoria H2, no existen dependencias externas que deban orquestarse como PostgreSQL
o Kafka. Es por ello que se incluye un Dockerfile pero no un docker-compose.yml.

## Swagger UI

```
http://localhost:8080/swagger-ui.html
```

## OpenAPI YAML

El contrato de la API se encuentra en:

```
src/main/resources/static/openapi.yaml
```

Con el servicio levantado, se puede consultar la especificación en:

```
http://localhost:8080/openapi.yaml
```
### API-First "ligero"

El YAML revela la documentación formal del contrato expuesto por el servicio, si bien no se han incorporado herramientas
generadoras de código a partir del mismo (p.e open api generator), a fin de desarrollar una solución propia para la prueba
y reducir el uso de dependencias. Es por ello por lo que esta solución se considera un API First ligero.

## H2 Console

```
http://localhost:8080/h2-console
```

JDBC URL:

```
jdbc:h2:mem:pricesdb
```

User:

```
sa
```

Password vacío.

## Colección Postman

Se incluye una colección de Postman con las peticiones propuestas en la prueba para importarse directamente 
en la herramienta.

## Líneas futuras

Como líneas futuras más próximas, no incluidas con la intención de mantener la sencillez de la prueba, destacaría las siguientes:

- Mejoras en el modelo de los datos. BrandId, ProductId y PriceList se almacenan directamente en la tabla PRICES. Sin embargo,
  en un escenario real estos campos podrían modelarse como claves foráneas hacia otras tablas BRANDS, PRODUCTS, PRICE_LISTS...
- Sustitución de PostgreSQL por H2 u otra base de datos relacional persistente.
- Incluir más casos de uso: crear un nuevo precio, actualizar precio existente, eliminación o desactivación, consulta de histórico...