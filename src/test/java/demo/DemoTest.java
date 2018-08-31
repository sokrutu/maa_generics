package demo;

import de.monticore.ModelingLanguageFamily;
import de.monticore.io.paths.ModelPath;
import de.monticore.symboltable.GlobalScope;
import de.monticore.templateclassgenerator.Modelfinder;
import de.se_rwth.commons.logging.Log;
import montiarc._ast.ASTComponent;
import montiarc._cocos.MontiArcCoCoChecker;
import montiarc._symboltable.ComponentSymbol;
import montiarc._symboltable.MontiArcLanguage;
import montiarc._symboltable.MontiArcLanguageFamily;
import montiarc.cocos.*;
import montiarc.helper.JavaHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DemoTest {

    @Before
    public void setup() {
        Log.init();
        Log.getFindings().clear();
        Log.enableFailQuick(false);
    }

    @Test
    public void test() {
        String defaultTypesPath = "src/test/resources/defaultTypes";
        String modelsPath = "src/test/resources/models";

        // Globales Scope
        ModelingLanguageFamily fam = new MontiArcLanguageFamily();
        final ModelPath mp = new ModelPath(Paths.get(modelsPath), Paths.get(defaultTypesPath));
        GlobalScope scope = new GlobalScope(mp, fam);
        JavaHelper.addJavaPrimitiveTypes(scope);

        // Alle Komponenten
        List<ComponentSymbol> components = Modelfinder
                .getModelsInModelPath(Paths.get(modelsPath).toFile(), MontiArcLanguage.FILE_ENDING)
                .stream()
                .map(fqModelName -> scope.<ComponentSymbol>resolve(fqModelName, ComponentSymbol.KIND))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        Assert.assertEquals(1, components.size());

        // CoCos checken
        components
                .stream()
                .forEach(c -> {
                    MontiArcCoCoChecker checker = MontiArcCoCos.createChecker();
                    // Dieser Test ist nicht in den Default-CoCos. Und er failt immer noch.
                    checker.addCoCo(new AutomatonGuardIsNotBoolean());
                    checker.checkAll((ASTComponent) c.getAstNode().get());
                });

        // Übersicht aller Default-CoCos
        /*new MontiArcCoCoChecker()
                .addCoCo(new PortUsage())
                .addCoCo(new SubComponentsConnected())
                .addCoCo(new PackageLowerCase())
                .addCoCo(new NamesCorrectlyCapitalized())
                .addCoCo(new DefaultParametersHaveCorrectOrder())
                .addCoCo(new DefaultParametersCorrectlyAssigned())
                .addCoCo(new ComponentWithTypeParametersHasInstance())
                .addCoCo(new CircularInheritance())
                .addCoCo(new IOAssignmentCallFollowsMethodCall())
                .addCoCo(new SubcomponentGenericTypesCorrectlyAssigned())
                .addCoCo(new TypeParameterNamesUnique())
                .addCoCo(new TopLevelComponentHasNoInstanceName())
                .addCoCo(new ConnectorEndPointIsCorrectlyQualified())
                .addCoCo(new InPortUniqueSender())
                .addCoCo(new ImportsValid())
                .addCoCo(new SubcomponentReferenceCycle())
                .addCoCo(new ReferencedSubComponentExists())
                .addCoCo(new PortNamesAreNotJavaKeywords())
                .addCoCo(new InputPortChangedInCompute())
                .addCoCo(new UsedPortsAndVariablesExist())
                .addCoCo(new MultipleBehaviorImplementation())
                .addCoCo(new InitBlockOnlyOnEmbeddedAJava())
                .addCoCo(new AtMostOneInitBlock())
                .addCoCo(new ImplementationInNonAtomicComponent())
                .addCoCo(new NamesCorrectlyCapitalized())
                .addCoCo(new AutomatonHasNoState())
                .addCoCo(new AutomatonHasNoInitialState())
                .addCoCo(new MultipleAssignmentsSameIdentifier())
                .addCoCo(new AutomatonOutputInExpression())
                .addCoCo(new AutomatonNoAssignmentToIncomingPort())
                .addCoCo(new AutomatonReactionWithAlternatives())
                .addCoCo(new AutomatonReactionWithAlternatives())
                .addCoCo(new UseOfForbiddenExpression())
                .addCoCo(new UseOfForbiddenExpression())
                .addCoCo(new NamesCorrectlyCapitalized())
                .addCoCo(new ConnectorSourceAndTargetComponentDiffer())
                .addCoCo(new ConnectorSourceAndTargetExistAndFit())
                .addCoCo(new ImportsAreUnique())
                .addCoCo(new AutomatonDeclaredInitialStateDoesNotExist())
                .addCoCo(new UseOfUndeclaredState())
                .addCoCo(new UseOfUndeclaredField())
                .addCoCo(new UseOfUndeclaredField())
                .addCoCo(new SubcomponentGenericTypesCorrectlyAssigned())
                .addCoCo(new ConfigurationParametersCorrectlyInherited())
                .addCoCo(new InnerComponentNotExtendsDefiningComponent())
                .addCoCo(new AutomatonNoDataAssignedToVariable())
                .addCoCo(new AutomatonInitialDeclaredMultipleTimes())
                .addCoCo(new AutomatonStateDefinedMultipleTimes())
                .addCoCo(new UseOfValueLists())
                .addCoCo(new IdentifiersAreUnique());*/

        Assert.assertTrue(Log.getFindings().toString(), Log.getFindings().isEmpty());
    }

}
