package com.wanaia.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

public class ArchUnitBoundaryTest {

    private static JavaClasses classes;

    @BeforeAll
    static void setUp() {
        classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.wanaia");
    }

    @Test
    @DisplayName("Controllers must not directly access Spring Data Repositories")
    void controllersMustNotAccessRepositoriesDirectly() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..controller..")
            .should().dependOnClassesThat().resideInAPackage("..repository..");

        rule.check(classes);
    }

    @Test
    @DisplayName("Entities must reside exclusively in model packages")
    void entitiesMustResideInModelPackages() {
        ArchRule rule = classes()
            .that().areAnnotatedWith(jakarta.persistence.Entity.class)
            .should().resideInAPackage("..model..");

        rule.check(classes);
    }

    @Test
    @DisplayName("Repositories must reside in repository packages and be interfaces")
    void repositoriesMustBeInterfacesInRepositoryPackages() {
        ArchRule rule = classes()
            .that().areAnnotatedWith(org.springframework.stereotype.Repository.class)
            .should().beInterfaces()
            .andShould().resideInAPackage("..repository..");

        rule.check(classes);
    }

    @Test
    @DisplayName("Mobility domain must not access User or Market repositories directly")
    void mobilityDomainMustNotAccessExternalRepositories() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..domain.mobility..")
            .should().dependOnClassesThat().resideInAnyPackage(
                "..domain.user.repository..",
                "..domain.market.repository..",
                "..domain.decision.repository..",
                "..domain.profile.repository.."
            );

        rule.check(classes);
    }

    @Test
    @DisplayName("Decision domain must not directly access Mobility or Market repositories")
    void decisionDomainMustNotAccessExternalRepositories() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..domain.decision..")
            .should().dependOnClassesThat().resideInAnyPackage(
                "..domain.mobility.repository..",
                "..domain.market.repository..",
                "..domain.user.repository.."
            );

        rule.check(classes);
    }
}
