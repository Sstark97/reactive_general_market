# Spring Webflux

# Estructuras
## Mono
Es un tipo de Publisher en Reactor que representa un único valor (0 o 1). Es adecuado para manejar operaciones asíncronas que producen un solo resultado, como leer un valor de una base de datos o una operación HTTP que devuelve un único objeto.

### Métodos más usados:

- **just**: Crea un Mono que emite el valor proporcionado.
```java
Mono<String> mono = Mono.just("Hello, World!");
```

- **empty**: Crea un Mono que completa sin emitir ningún valor.
```java
Mono<String> mono = Mono.empty();
````

- **error**: Crea un Mono que emite un error.
```java
Mono<String> mono = Mono.error(new RuntimeException("Error occurred"));
```

- **fromCallable**: Crea un Mono a partir de una llamada que devuelve un valor.
```java
Mono<String> mono = Mono.fromCallable(() -> "Callable Result");
```

- **flatMap**: Transforma el valor emitido por el Mono en otro Mono.
```java
Mono<String> mono = Mono.just("hello")
    .flatMap(value -> Mono.just(value.toUpperCase())); // Result: "HELLO"
````

- **map**: Transforma el valor emitido por el Mono.
```java
Mono<String> mono = Mono.just("hello")
    .map(String::toUpperCase); // Result: "HELLO"
````

- **then**: Ejecuta otra operación después de que este Mono se complete.
```java
Mono<Void> mono = Mono.just("Done")
    .then(Mono.fromRunnable(() -> System.out.println("Next Operation")));
```

- **onErrorResume**: Proporciona un Mono alternativo si ocurre un error.
```java
Mono<String> mono = Mono.error(new RuntimeException("Error occurred"))
    .onErrorResume(e -> Mono.just("Recovered from error"));
```

- **doOnNext**: Ejecuta una acción secundaria cuando se emite un valor.
```java
Mono<String> mono = Mono.just("hello")
    .doOnNext(value -> System.out.println("Value: " + value));
```

## Flux
Es un tipo de Publisher en Reactor que representa una secuencia de 0 a N elementos. Es adecuado para manejar operaciones asíncronas que producen múltiples resultados, como leer una lista de valores de una base de datos o una operación HTTP que devuelve una colección de objetos.

### Métodos más usados:

- **just**: Crea un Flux que emite una secuencia de valores proporcionados.
```java
Flux<String> flux = Flux.just("Hello", "World");
```

- **empty**: Crea un Flux que completa sin emitir ningún valor.
```java
Flux<String> flux = Flux.empty();
```

- **error**: Crea un Flux que emite un error.
```java
Flux<String> flux = Flux.error(new RuntimeException("Error occurred"));
```

- **fromIterable**: Crea un Flux a partir de una colección.
```java
Flux<String> flux = Flux.fromIterable(Arrays.asList("Alpha", "Beta", "Gamma"));
```

- **range**: Crea un Flux que emite una secuencia de enteros en un rango especificado.
```java
Flux<Integer> flux = Flux.range(1, 5); // Emite 1, 2, 3, 4, 5
```

- **flatMap**: Transforma cada valor emitido por el Flux en otro Flux y concatena los resultados.
```java
Flux<String> flux = Flux.just("alpha", "beta")
    .flatMap(value -> Flux.fromArray(value.split("")));
``` 

- **map**: Transforma cada valor emitido por el Flux.
```java
Flux<String> flux = Flux.just("alpha", "beta")
    .map(String::toUpperCase); // Result: "ALPHA", "BETA"
```

- **filter**: Filtra los valores emitidos por el Flux según un predicado.
```java
Flux<Integer> flux = Flux.range(1, 10)
    .filter(value -> value % 2 == 0); // Emite solo números pares
```

- **collectList**: Acumula todos los valores emitidos por el Flux en una lista y los emite como un Mono.
```java
Mono<List<String>> mono = Flux.just("one", "two", "three")
    .collectList();
```

- **onErrorResume**: Proporciona un Flux alternativo si ocurre un error.
```java
Flux<String> flux = Flux.error(new RuntimeException("Error occurred"))
    .onErrorResume(e -> Flux.just("Recovered", "From", "Error"));
```

- **doOnNext**: Ejecuta una acción secundaria cuando se emite un valor.
```java
Flux<String> flux = Flux.just("one", "two", "three")
    .doOnNext(value -> System.out.println("Value: " + value));
```

Estos métodos son fundamentales para trabajar con flujos reactivos en Spring WebFlux, proporcionando una amplia gama de operaciones para manipular, transformar y gestionar secuencias de datos asíncronos.

## Functional Endpoints
Con Webflux podemos crear endpoints de manera funcional, para ello necesitamos 3 elementos:
- Caso de uso: El servicio o caso de uso será aquel que realice la acción que queramos.
- Handler: El handler sera el elemento intermedio que interactuará con el use case y el router, para gestionar la petición y devolverla en formato de request
- Router: Es la capa más externa, la que se encarga de servir las rutas de la api y responder.

*Use case*
```java
@Service
public class CreateProduct {
  private final ProductRepository productRepository;

  public CreateProduct(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Mono<Product> execute(CreatedProductDto productDto) {
    if(productDto.name() == null) {
      return Mono.error(new IllegalArgumentException("Required parameters are missing."));
    }
    return productRepository.save(productDto.toDomain());
  }
}
```

*Handler*
```java
@Component
public class ProductHandler {

  private final CreateProduct createProduct;
  private final FindPaginatedProducts findPaginatedProducts;

  public ProductHandler(CreateProduct createProduct, FindPaginatedProducts findPaginatedProducts) {
    this.createProduct = createProduct;
    this.findPaginatedProducts = findPaginatedProducts;
  }

  public Mono<ServerResponse> createProduct(ServerRequest request) {
    return request.bodyToMono(CreatedProductDto.class)
        .flatMap(createProduct::execute)
        .flatMap(createdProduct -> ServerResponse.ok().bodyValue(createdProduct))
        .onErrorResume(error -> ServerResponse.badRequest().bodyValue(error.getMessage()));
  }
}
```

La potencia de los elementos funcionales nos permite gestionar los errores de una manerá más simple y entendible.

*Router*
```java
@Configuration
public class ProductRouter {
  private final ProductHandler productHandler;

  public ProductRouter(ProductHandler productHandler) {
    this.productHandler = productHandler;
  }

  @Bean
  RouterFunction<ServerResponse> productRoutes() {
    return RouterFunctions.route(RequestPredicates.POST("products/create"), productHandler::createProduct)
        .andRoute(RequestPredicates.GET("products/all"), productHandler::findAllProductsPaginated);
  }
}
```