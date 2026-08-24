# WANAIA — Backend Architecture

## Spring Boot Modular Monolith

---

## 1. Project Structure

```
backend/
├── pom.xml
├── Dockerfile
├── src/
│   ├── main/
│   │   ├── java/com/wanaia/
│   │   │   ├── WanaiaApplication.java
│   │   │   │
│   │   │   ├── common/                         # Cross-cutting concerns
│   │   │   │   ├── config/
│   │   │   │   │   ├── SecurityConfig.java
│   │   │   │   │   ├── CorsConfig.java
│   │   │   │   │   ├── CacheConfig.java
│   │   │   │   │   ├── AsyncConfig.java
│   │   │   │   │   ├── JacksonConfig.java
│   │   │   │   │   └── OpenApiConfig.java
│   │   │   │   ├── security/
│   │   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   │   ├── UserPrincipal.java
│   │   │   │   │   └── SecurityUtils.java
│   │   │   │   ├── exception/
│   │   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   │   ├── BusinessException.java
│   │   │   │   │   ├── AuthenticationException.java
│   │   │   │   │   └── ErrorResponse.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── ApiResponse.java
│   │   │   │   │   ├── PagedResponse.java
│   │   │   │   │   └── ErrorDetail.java
│   │   │   │   ├── audit/
│   │   │   │   │   ├── AuditService.java
│   │   │   │   │   ├── AuditLog.java
│   │   │   │   │   └── Auditable.java          # Base entity mixin
│   │   │   │   ├── validation/
│   │   │   │   │   └── ValidationUtils.java
│   │   │   │   └── util/
│   │   │   │       ├── SlugUtils.java
│   │   │   │       └── PaginationUtils.java
│   │   │   │
│   │   │   ├── auth/                            # Authentication module
│   │   │   │   ├── controller/
│   │   │   │   │   └── AuthController.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── request/
│   │   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   │   ├── RegisterRequest.java
│   │   │   │   │   │   ├── RefreshTokenRequest.java
│   │   │   │   │   │   ├── ForgotPasswordRequest.java
│   │   │   │   │   │   └── ResetPasswordRequest.java
│   │   │   │   │   └── response/
│   │   │   │   │       ├── AuthResponse.java
│   │   │   │   │       └── TokenResponse.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── AuthService.java
│   │   │   │   │   └── impl/
│   │   │   │   │       └── AuthServiceImpl.java
│   │   │   │   ├── entity/
│   │   │   │   │   └── RefreshToken.java
│   │   │   │   └── repository/
│   │   │   │       └── RefreshTokenRepository.java
│   │   │   │
│   │   │   ├── user/                            # User module
│   │   │   │   ├── controller/
│   │   │   │   │   └── UserController.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── request/
│   │   │   │   │   │   ├── UpdateProfileRequest.java
│   │   │   │   │   │   └── UpdatePreferencesRequest.java
│   │   │   │   │   └── response/
│   │   │   │   │       ├── UserProfileResponse.java
│   │   │   │   │       └── UserSummaryResponse.java
│   │   │   │   ├── mapper/
│   │   │   │   │   └── UserMapper.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── UserService.java
│   │   │   │   │   └── impl/
│   │   │   │   │       └── UserServiceImpl.java
│   │   │   │   ├── entity/
│   │   │   │   │   ├── User.java
│   │   │   │   │   └── UserPreference.java
│   │   │   │   └── repository/
│   │   │   │       ├── UserRepository.java
│   │   │   │       └── UserPreferenceRepository.java
│   │   │   │
│   │   │   ├── vehicle/                         # Vehicle knowledge module
│   │   │   │   ├── controller/
│   │   │   │   │   ├── BrandController.java
│   │   │   │   │   ├── ModelController.java
│   │   │   │   │   └── VehicleController.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── request/
│   │   │   │   │   └── response/
│   │   │   │   │       ├── BrandResponse.java
│   │   │   │   │       ├── BrandDetailResponse.java
│   │   │   │   │       ├── ModelResponse.java
│   │   │   │   │       ├── ModelDetailResponse.java
│   │   │   │   │       ├── GenerationResponse.java
│   │   │   │   │       ├── TrimResponse.java
│   │   │   │   │       ├── TrimDetailResponse.java
│   │   │   │   │       ├── PowertrainResponse.java
│   │   │   │   │       ├── SpecificationResponse.java
│   │   │   │   │       └── FeatureResponse.java
│   │   │   │   ├── mapper/
│   │   │   │   │   ├── BrandMapper.java
│   │   │   │   │   ├── ModelMapper.java
│   │   │   │   │   └── VehicleMapper.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── BrandService.java
│   │   │   │   │   ├── ModelService.java
│   │   │   │   │   ├── VehicleService.java
│   │   │   │   │   └── impl/
│   │   │   │   ├── entity/
│   │   │   │   │   ├── Brand.java
│   │   │   │   │   ├── Model.java
│   │   │   │   │   ├── Generation.java
│   │   │   │   │   ├── Trim.java
│   │   │   │   │   ├── Powertrain.java
│   │   │   │   │   ├── Engine.java
│   │   │   │   │   ├── ElectricMotor.java
│   │   │   │   │   ├── Battery.java
│   │   │   │   │   ├── Transmission.java
│   │   │   │   │   ├── BodyType.java
│   │   │   │   │   ├── FuelType.java
│   │   │   │   │   ├── Feature.java
│   │   │   │   │   ├── SpecificationCategory.java
│   │   │   │   │   ├── SpecificationKey.java
│   │   │   │   │   └── TrimSpecification.java
│   │   │   │   └── repository/
│   │   │   │       ├── BrandRepository.java
│   │   │   │       ├── ModelRepository.java
│   │   │   │       ├── GenerationRepository.java
│   │   │   │       ├── TrimRepository.java
│   │   │   │       └── ...
│   │   │   │
│   │   │   ├── listing/                         # Marketplace module
│   │   │   │   ├── controller/
│   │   │   │   │   └── ListingController.java
│   │   │   │   ├── dto/
│   │   │   │   ├── mapper/
│   │   │   │   ├── service/
│   │   │   │   ├── entity/
│   │   │   │   │   ├── Listing.java
│   │   │   │   │   ├── ListingImage.java
│   │   │   │   │   └── ListingFeature.java
│   │   │   │   └── repository/
│   │   │   │
│   │   │   ├── search/                          # Search module
│   │   │   │   ├── controller/
│   │   │   │   │   └── SearchController.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── SearchRequest.java
│   │   │   │   │   ├── SearchResponse.java
│   │   │   │   │   └── SearchFacets.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── SearchService.java        # Interface
│   │   │   │   │   ├── PostgresSearchService.java # MVP impl
│   │   │   │   │   └── SearchIndexService.java    # Indexing
│   │   │   │   └── specification/
│   │   │   │       └── ListingSpecification.java  # JPA Specification builder
│   │   │   │
│   │   │   ├── dealer/                          # Dealer module
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   ├── mapper/
│   │   │   │   ├── service/
│   │   │   │   ├── entity/
│   │   │   │   │   ├── Dealer.java
│   │   │   │   │   ├── DealerBrand.java
│   │   │   │   │   └── DealerStaff.java
│   │   │   │   └── repository/
│   │   │   │
│   │   │   ├── comparison/                      # Comparison module
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   ├── service/
│   │   │   │   ├── entity/
│   │   │   │   └── repository/
│   │   │   │
│   │   │   ├── review/                          # Review module
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   ├── service/
│   │   │   │   ├── entity/
│   │   │   │   └── repository/
│   │   │   │
│   │   │   ├── content/                         # Content module
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   ├── service/
│   │   │   │   ├── entity/
│   │   │   │   └── repository/
│   │   │   │
│   │   │   ├── media/                           # Media module
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   │   ├── MediaService.java
│   │   │   │   │   ├── ImageProcessingService.java
│   │   │   │   │   └── StorageService.java
│   │   │   │   └── config/
│   │   │   │       └── StorageConfig.java
│   │   │   │
│   │   │   ├── admin/                           # Admin module
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   └── service/
│   │   │   │
│   │   │   └── ai/                              # AI module (Phase 2)
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── service/
│   │   │       │   ├── AiService.java            # Interface
│   │   │       │   ├── AiProviderAdapter.java    # Abstraction
│   │   │       │   └── impl/
│   │   │       │       └── GeminiAiService.java  # Provider impl
│   │   │       └── config/
│   │   │           └── AiConfig.java
│   │   │
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       ├── db/
│   │       │   └── migration/
│   │       │       ├── V001__create_users_and_auth.sql
│   │       │       ├── V002__create_brands_and_body_types.sql
│   │       │       └── ...
│   │       ├── i18n/
│   │       │   ├── messages.properties
│   │       │   ├── messages_ar.properties
│   │       │   └── messages_fr.properties
│   │       └── openapi/
│   │
│   └── test/
│       └── java/com/wanaia/
│           ├── auth/
│           ├── user/
│           ├── vehicle/
│           ├── listing/
│           ├── search/
│           └── ...
```

---

## 2. Key Architectural Patterns

### 2.1 Base Entity

```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
```

### 2.2 Soft-Deletable Entity

```java
@MappedSuperclass
public abstract class SoftDeletableEntity extends BaseEntity {

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void softDelete() {
        this.deletedAt = Instant.now();
    }
}
```

### 2.3 API Response Envelope

```java
public record ApiResponse<T>(
    boolean success,
    T data,
    Meta meta,
    Instant timestamp
) {
    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>(true, data, null, Instant.now());
    }

    public static <T> ApiResponse<T> of(T data, Meta meta) {
        return new ApiResponse<>(true, data, meta, Instant.now());
    }

    public record Meta(int page, int pageSize, long totalItems, int totalPages) {}
}
```

### 2.4 DTO Mapping (MapStruct)

```java
@Mapper(componentModel = "spring")
public interface BrandMapper {
    BrandResponse toResponse(Brand entity);
    BrandDetailResponse toDetailResponse(Brand entity);
    List<BrandResponse> toResponseList(List<Brand> entities);
}
```

### 2.5 Global Exception Handling

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
        // 404
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        // 422 with field-level errors
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
        // 403
    }
}
```

### 2.6 Search Abstraction

```java
public interface SearchService {
    PagedResponse<ListingSearchResult> searchListings(SearchRequest request);
    List<String> suggest(String query, int limit);
    SearchFacets getFacets(SearchRequest request);
}

// MVP implementation using PostgreSQL
@Service
@Profile("!elasticsearch")
public class PostgresSearchService implements SearchService {
    // Uses JPA Specifications + FTS
}

// Future implementation
@Service
@Profile("elasticsearch")
public class ElasticsearchSearchService implements SearchService {
    // Uses OpenSearch/Elasticsearch
}
```

### 2.7 AI Provider Abstraction

```java
public interface AiProvider {
    AiChatResponse chat(AiChatRequest request, AiContext context);
    AiRecommendation recommend(AiRecommendationRequest request);
}

@Service
public class AiService {
    private final AiProvider provider;
    private final VehicleService vehicleService;
    private final SearchService searchService;

    // Uses structured vehicle data, never lets AI invent facts
    public AiChatResponse processChat(AiChatRequest request) {
        // 1. Parse intent
        // 2. Fetch relevant structured data
        // 3. Build context with real data
        // 4. Generate response grounded in facts
        return provider.chat(enrichedRequest, vehicleContext);
    }
}
```

---

## 3. Security Implementation

### 3.1 JWT Flow

```java
@Component
public class JwtTokenProvider {
    // Access tokens: 15 minutes
    // Refresh tokens: 30 days
    // Algorithm: HS512
    // Claims: userId, role, permissions

    public String generateAccessToken(UserPrincipal user) { ... }
    public String generateRefreshToken(UserPrincipal user) { ... }
    public Long getUserIdFromToken(String token) { ... }
    public boolean validateToken(String token) { ... }
}
```

### 3.2 Security Filter Chain

```java
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        return http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(jwtAuthEntryPoint)
                .accessDeniedHandler(accessDeniedHandler))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers(GET, "/api/v1/brands/**").permitAll()
                .requestMatchers(GET, "/api/v1/models/**").permitAll()
                .requestMatchers(GET, "/api/v1/vehicles/**").permitAll()
                .requestMatchers(GET, "/api/v1/search/**").permitAll()
                .requestMatchers(GET, "/api/v1/listings/**").permitAll()
                .requestMatchers(GET, "/api/v1/dealers/**").permitAll()
                .requestMatchers(GET, "/api/v1/content/**").permitAll()
                // Authenticated endpoints
                .requestMatchers("/api/v1/garage/**").authenticated()
                .requestMatchers("/api/v1/reviews/**").authenticated()
                .requestMatchers(POST, "/api/v1/listings/**").authenticated()
                // Admin endpoints
                .requestMatchers("/api/v1/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                // Default
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
```

---

## 4. Configuration Strategy

### application.yml (base)

```yaml
spring:
  application:
    name: wanaia-api
  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: validate  # Flyway manages schema
    properties:
      hibernate:
        default_schema: public
        jdbc:
          batch_size: 50
  flyway:
    enabled: true
    locations: classpath:db/migration
  jackson:
    default-property-inclusion: non_null
    serialization:
      write-dates-as-timestamps: false

server:
  port: 8080
  servlet:
    context-path: /
```

### application-dev.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/wanaia_dev
    username: wanaia
    password: ${DB_PASSWORD}
  jpa:
    show-sql: true

logging:
  level:
    com.wanaia: DEBUG
    org.hibernate.SQL: DEBUG
```

### application-prod.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5

logging:
  level:
    com.wanaia: INFO
    org.hibernate.SQL: WARN
```

---

## 5. Testing Architecture

### 5.1 Test Structure

```
test/
├── java/com/wanaia/
│   ├── auth/
│   │   ├── AuthControllerTest.java          # @WebMvcTest
│   │   ├── AuthServiceTest.java             # @ExtendWith(MockitoExtension)
│   │   └── AuthIntegrationTest.java         # @SpringBootTest
│   ├── vehicle/
│   │   ├── BrandControllerTest.java
│   │   ├── BrandServiceTest.java
│   │   ├── BrandRepositoryTest.java         # @DataJpaTest
│   │   └── VehicleIntegrationTest.java
│   ├── listing/
│   │   ├── ListingControllerTest.java
│   │   └── ListingServiceTest.java
│   └── common/
│       ├── TestDataFactory.java             # Test data builders
│       └── AbstractIntegrationTest.java     # Base for integration tests
```

### 5.2 Test Strategy

| Layer | Framework | Scope |
|-------|-----------|-------|
| Unit | JUnit 5 + Mockito | Service logic |
| Repository | @DataJpaTest + Testcontainers | JPA queries |
| Controller | @WebMvcTest + MockMvc | REST API |
| Integration | @SpringBootTest + Testcontainers | End-to-end |
| Security | @WithMockUser | Auth/RBAC |

### 5.3 Testcontainers for PostgreSQL

```java
@Testcontainers
@SpringBootTest
public abstract class AbstractIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
        .withDatabaseName("wanaia_test")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
```

---

## 6. Build & Dependencies (Maven)

### Key Dependencies

| Dependency | Purpose |
|-----------|---------|
| spring-boot-starter-web | REST API |
| spring-boot-starter-data-jpa | JPA + Hibernate |
| spring-boot-starter-security | Authentication |
| spring-boot-starter-validation | Bean Validation |
| spring-boot-starter-cache | Caching |
| spring-boot-starter-actuator | Observability |
| springdoc-openapi-starter-webmvc-ui | OpenAPI/Swagger |
| flyway-core + flyway-database-postgresql | Migrations |
| postgresql | JDBC driver |
| jjwt-api + jjwt-impl | JWT handling |
| mapstruct | DTO mapping |
| lombok | Boilerplate reduction |
| testcontainers | Integration testing |
| spring-boot-starter-test | Testing |

---

*The backend is designed as a clean, modular monolith with clear domain boundaries, strong security, and a path toward selective microservice extraction.*
