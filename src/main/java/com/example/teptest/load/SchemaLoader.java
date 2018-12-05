package com.example.teptest.load;

import org.axonframework.eventhandling.tokenstore.jdbc.PostgresTokenTableFactory;
import org.axonframework.eventhandling.tokenstore.jdbc.TokenSchema;
import org.axonframework.eventsourcing.eventstore.jdbc.EventSchema;
import org.axonframework.eventsourcing.eventstore.jdbc.PostgresEventTableFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@Profile("jdbc & load")
public class SchemaLoader {

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Autowired
    public void createSchema(DataSource dataSource) {
        if("create-drop".equals(ddlAuto)) {
            try (Connection connection = dataSource.getConnection()) {
                EventSchema eventSchema = EventSchema.builder().build();
                TokenSchema tokenSchema = TokenSchema.builder().build();

                connection.prepareStatement("DROP TABLE IF EXISTS " + eventSchema.domainEventTable()).execute();
                connection.prepareStatement("DROP TABLE IF EXISTS " + tokenSchema.tokenTable()).execute();

                PostgresEventTableFactory.INSTANCE.createDomainEventTable(connection, eventSchema).execute();
                new PostgresTokenTableFactory().createTable(connection, tokenSchema).execute();
            } catch (SQLException ex) {
                throw new RuntimeException("Couldn't create schema", ex);
            }
        }
    }

}
