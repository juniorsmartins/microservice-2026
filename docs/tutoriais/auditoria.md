# TUTORIAL

## 1. Teoria

### Fontes:
- https://docs.spring.io/spring-data/jpa/reference/#auditing 
- https://docs.spring.io/spring-data/jpa/reference/auditing.html 
- https://docs.spring.io/spring-data/envers/docs/current/reference/html/#reference 
- https://docs.spring.io/spring-data/jpa/reference/envers/configuration.html 
- https://sunitc.dev/2020/01/21/spring-boot-how-to-add-jpa-hibernate-envers-auditing/ 
- https://www.baeldung.com/database-auditing-jpa 
- https://docs.jboss.org/envers/docs/#configuration 
- https://docs.jboss.org/envers/docs/ 
- https://hibernate.org/orm/envers/ 
- https://medium.com/@rodrigoventuri123/auditoria-com-spring-data-jpa-fbb54c4b443e 
- https://www.baeldung.com/spring-data-javers-audit 

### Introdução: 

```
Em qualquer aplicação, auditoria significa rastrear e registrar cada alteração em todos os objetos de negócio, ou seja, 
rastrear cada operação de inserção, atualização e exclusão. Basicamente, envolve o rastreamento de três coisas.

* Que operação foi realizada?
* Quem fez isso?
* Quando foi feito?

A auditoria nos ajuda a manter registros históricos, o que posteriormente pode nos auxiliar no rastreamento das 
atividades dos usuários.

Embora o JPA Auditing ofereça configuração fácil para auditoria básica, ela não fornece detalhes de todas as 
alterações/atualizações feitas em uma entidade. Por exemplo, uma entidade Cliente pode ter sido modificada 5 vezes. Com 
a auditoria JPA, não há como descobrir o que foi alterado na entidade em cada uma das 5 atualizações. E assim Envers 
entra em cena, fornecendo o histórico de auditoria completo de uma entidade. 
```

Spring Data Jpa Auditing
```
Permite rastrear e registrar automaticamente informações de auditoria, como data de criação, data de modificação e 
usuário que criou ou modificou uma entidade. Isso é especialmente útil em aplicações que exigem conformidade com 
regulamentos ou precisam manter um histórico detalhado das alterações feitas nos dados.
```

Spring Data Envers
```
Envers é um módulo do Hibernate que adiciona recursos de auditoria às entidades JPA.  
```

## 2. Configuração

### Passo-a-passo

Spring Data Jpa Auditing:
1. Criar classe mãe de auditoria;
2. Extender as entidades que precisam de auditoria a partir da classe mãe;
3. Criar classe de configuração para habilitar auditoria. Com bean para converter data;
4. Adicionar campos de auditoria no script de criação das tabelas no PostgreSQL.

Spring Data Envers: 
1. Adicionar dependência no build.gradle;
2. Extender repositorios JPA a partir de RevisionRepository;
3. Habilitar auditoria Envers nas entidades que precisam de auditoria com @Audited e classe pai;
4. Criar script de criação das tabelas de auditoria (opcional, o Envers cria automaticamente, mas não recomendado em produção);
   a. Fiz uso de nomes personalizados para as tabelas e campos (fora do padrão do Envers);
5. Configurar application.yml (necessário para customizar nomes de tabelas e campos);
6. Criar RevisionEntity (importante, especialmente para customizar os nomes);

### Implementação: 

Spring Data Jpa Auditing

1. Classe mãe de auditoria:
```
@MappedSuperclass 
@EntityListeners(AuditingEntityListener.class) 
@Getter
@Setter
public abstract class AbstractAuditingJpa {

    @Column(name = "created_by", updatable = false)
    private String createdBy; 

    @Column(name = "last_modified_by")
    private String lastModifiedBy; 

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private OffsetDateTime createdDate; 

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private OffsetDateTime lastModifiedDate; 
}
```
2. Extender as entidades:
```
@Entity
@Table(name = "customers", indexes = {@Index(name = "idx_customers_email", columnList = "email")})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"email"}, callSuper = false)
@ToString
public final class CustomerJpa extends AbstractAuditingJpa implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, optional = false, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserJpa user;

    @Column(name = "active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active;
}
```
3. Classe de configuração:
```
@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class JpaAuditingConfig {

    @Bean(name = "auditingDateTimeProvider")
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now(ZoneOffset.UTC));
    }
}
```
4. Script (banco PostgreSQL):
```
created_by VARCHAR(50),
created_date TIMESTAMP WITH TIME ZONE NOT NULL,
last_modified_by VARCHAR(50),
last_modified_date TIMESTAMP WITH TIME ZONE,
```

Spring Data Envers: 

1. Dependência no build.gradle;
```
implementation 'org.springframework.data:spring-data-envers:4.0.1'
```
2. Extender repositórios JPA;
```
public interface UserRepository extends JpaRepository<UserJpa, UUID>, RevisionRepository<UserJpa, UUID, Long>  {
}
```
3. Habilitar auditoria Envers nas entidades e classe pai (@Audited);
```
@Audited
@MappedSuperclass 
@EntityListeners(AuditingEntityListener.class) 
@Getter
@Setter
public abstract class AbstractAuditingJpa {

    @Column(name = "created_by", updatable = false)
    private String createdBy; 

    @Column(name = "last_modified_by")
    private String lastModifiedBy; 

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private OffsetDateTime createdDate; 

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private OffsetDateTime lastModifiedDate; 
}

@Audited 
@Entity
@Table(name = "users", indexes = {@Index(name = "idx_users_username", columnList = "username")})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"username"}, callSuper = false)
@ToString
public final class UserJpa extends AbstractAuditingJpa implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleJpa role;

    @Column(name = "active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active;
}
```
4. Scripts auditoria
```
CREATE TABLE IF NOT EXISTS revision_info (
    revision_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    revision_timestamp TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS roles_audit (
    revision_id INTEGER NOT NULL,
    revision_type SMALLINT NOT NULL,

    id UUID NOT NULL,
    name VARCHAR(50) NOT NULL,
    created_by VARCHAR(50),
    created_date TIMESTAMP WITH TIME ZONE,
    last_modified_by VARCHAR(50),
    last_modified_date TIMESTAMP WITH TIME ZONE,

    PRIMARY KEY (revision_id, id),
    CONSTRAINT fk_roles_audit_revision_info FOREIGN KEY (revision_id) REFERENCES revision_info(revision_id)
);
```
5. Configuração application.yml
```
spring:
  jpa: 
    properties:
      org:
        hibernate: 
          envers:
            audit_table_name: revision_info # Nome da tabela de revisões do Envers (padrão REVINFO).
            audit_table_suffix: _audit # Sufixo para tabelas de auditoria do Envers (padrão _aud).
            revision_field_name: revision_id # Nome do campo de revisão.
            revision_type_field_name: revision_type # Nome do campo de tipo de revisão.
```
6. CustomRevisionEntity
```
@Entity
@Table(name = "revision_info")
@RevisionEntity // Indica ao Envers que esta é a classe de controle de revisões
@Getter
@Setter
public class CustomRevisionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @Column(name = "revision_id")
    private Long revisionId;

    @RevisionTimestamp
    @Column(name = "revision_timestamp")
    private Instant revisionTimestamp;
}
```

