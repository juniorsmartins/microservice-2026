# TUTORIAL

## 1. Teoria

### Fontes:

### Introdução: 

Spring Data Jpa Auditing
```
Permite rastrear e registrar automaticamente informações de auditoria, como data de criação, 
data de modificação, usuário que criou ou modificou uma entidade, entre outros. Isso é 
especialmente útil em aplicações que exigem conformidade com regulamentos ou precisam manter 
um histórico detalhado das alterações feitas nos dados.
```

## 2. Configuração

### Passo-a-passo

Spring Data Jpa Auditing:
1. Criar classe mãe de auditoria;
2. Extender as entidades que precisam de auditoria a partir da classe mãe;
3. Criar classe de configuração para habilitar auditoria. Com bean para converter data;
4. Adicionar campos de auditoria no script de criação das tabelas no PostgreSQL.

Spring Data Envers: 
1. 

### Implementação: 

Spring Data Jpa Auditing:

Classe mãe de auditoria
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

Classe de configuração
```
@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class JpaAuditingConfiguration {

    @Bean(name = "auditingDateTimeProvider")
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now(ZoneOffset.UTC));
    }
}
```

Script
```
created_by VARCHAR(50),
created_date TIMESTAMP WITH TIME ZONE NOT NULL,
last_modified_by VARCHAR(50),
last_modified_date TIMESTAMP WITH TIME ZONE,
```

Spring Data Envers: 
```

```

