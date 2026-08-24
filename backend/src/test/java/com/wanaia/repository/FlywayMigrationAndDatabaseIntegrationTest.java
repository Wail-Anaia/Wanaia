package com.wanaia.repository;

import com.wanaia.domain.market.model.Market;
import com.wanaia.domain.market.repository.MarketRepository;
import com.wanaia.domain.mobility.model.MobilityCategory;
import com.wanaia.domain.mobility.model.MobilityProduct;
import com.wanaia.domain.mobility.repository.MobilityCategoryRepository;
import com.wanaia.domain.mobility.repository.MobilityProductRepository;
import com.wanaia.domain.provenance.model.DataSource;
import com.wanaia.domain.provenance.repository.DataSourceRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureEmbeddedDatabase(type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES)
@ActiveProfiles("test")
public class FlywayMigrationAndDatabaseIntegrationTest {

    @Autowired
    private MobilityCategoryRepository categoryRepository;

    @Autowired
    private MobilityProductRepository productRepository;

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private DataSourceRepository dataSourceRepository;

    @Test
    @DisplayName("Verify Flyway migrations successfully initialize PostgreSQL schema and load seed data")
    void shouldExecuteFlywayMigrationsAndLoadSeedData() {
        // Verify Categories
        List<MobilityCategory> categories = categoryRepository.findAll();
        assertFalse(categories.isEmpty(), "Mobility categories must be seeded");
        assertTrue(categories.stream().anyMatch(c -> c.getCode().equals("CAR")));
        assertTrue(categories.stream().anyMatch(c -> c.getCode().equals("MOTORCYCLE")));

        // Verify Products
        List<MobilityProduct> products = productRepository.findAll();
        assertFalse(products.isEmpty(), "Mobility products must be seeded");
        assertTrue(products.stream().anyMatch(p -> p.getSlug().equals("2-5-hybrid-awd-i")));

        // Verify Markets
        List<Market> markets = marketRepository.findAll();
        assertFalse(markets.isEmpty(), "Sovereign markets must be seeded");
        assertTrue(markets.stream().anyMatch(m -> m.getCode().equals("MAR")));
        assertTrue(markets.stream().anyMatch(m -> m.getCode().equals("FRA")));

        // Verify Provenance Data Sources
        List<DataSource> sources = dataSourceRepository.findAll();
        assertFalse(sources.isEmpty(), "Data sources must be seeded");
        assertTrue(sources.stream().anyMatch(s -> s.getCode().equals("OEM_TOYOTA_MA")));
    }
}
