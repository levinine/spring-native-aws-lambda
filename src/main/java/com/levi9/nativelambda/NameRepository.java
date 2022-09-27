package com.levi9.nativelambda;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema;

import java.time.Instant;
import java.util.Optional;

import static software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags.primaryPartitionKey;

@Repository
public class NameRepository {

    DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.create();


    private final TableSchema<FunnyNameEntity> tableSchema =
            StaticTableSchema.builder(FunnyNameEntity.class)
                    .newItemSupplier(FunnyNameEntity::new)
                    .addAttribute(String.class, a -> a.name("id")
                            .getter(FunnyNameEntity::getId)
                            .setter(FunnyNameEntity::setId)
                            .tags(primaryPartitionKey()))
                    .addAttribute(String.class, a -> a.name("name")
                            .getter(FunnyNameEntity::getName)
                            .setter(FunnyNameEntity::setName))
                    .addAttribute(Instant.class, a -> a.name("generatedAt")
                            .getter(FunnyNameEntity::getGeneratedAt)
                            .setter(FunnyNameEntity::setGeneratedAt))

                    .build();
    private final DynamoDbTable<FunnyNameEntity> NAME_TABLE = enhancedClient.table("name", tableSchema);

    public void saveName(FunnyNameEntity name) {
        NAME_TABLE.putItem(name);
    }

    public Optional<FunnyNameEntity> getName(String key) {

        Key key1 = Key.builder()
                .partitionValue(key)
                .build();

        return Optional.ofNullable(NAME_TABLE.getItem(r -> r.key(key1)));
    }
}
