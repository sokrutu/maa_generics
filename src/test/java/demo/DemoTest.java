package demo;

import de.monticore.ModelingLanguageFamily;
import de.monticore.automaton.ioautomaton.JavaHelper;
import de.monticore.io.paths.ModelPath;
import de.monticore.lang.montiarc.montiarc._ast.ASTComponent;
import de.monticore.lang.montiarc.montiarc._symboltable.ComponentSymbol;
import de.monticore.lang.montiarc.montiarcautomaton._cocos.MontiArcAutomatonCoCoChecker;
import de.monticore.lang.montiarc.montiarcautomaton._symboltable.MontiArcAutomatonLanguage;
import de.monticore.lang.montiarc.montiarcautomaton._symboltable.MontiArcAutomatonLanguageFamily;
import de.monticore.lang.montiarc.montiarcautomaton.cocos.MontiArcAutomatonCocos;
import de.monticore.symboltable.GlobalScope;
import de.monticore.templateclassgenerator.Modelfinder;
import de.se_rwth.commons.logging.Log;
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
        Log.getFindings().clear();
        Log.enableFailQuick(false);
    }

    @Test
    public void test() {
        String defaultTypesPath = "src/test/resources/defaultTypes";
        String modelsPath = "src/test/resources/models";

        // Globales Scope
        ModelingLanguageFamily fam = new MontiArcAutomatonLanguageFamily();
        final ModelPath mp = new ModelPath(Paths.get(modelsPath), Paths.get(defaultTypesPath));
        GlobalScope scope = new GlobalScope(mp, fam);
        JavaHelper.addJavaPrimitiveTypes(scope);

        // Alle Komponenten
        List<ComponentSymbol> components = Modelfinder
                .getModelsInModelPath(Paths.get(modelsPath).toFile(), MontiArcAutomatonLanguage.FILE_ENDING)
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
                    MontiArcAutomatonCoCoChecker checker = MontiArcAutomatonCocos.createChecker();
                    checker.checkAll((ASTComponent) c.getAstNode().get());
                });

        Assert.assertTrue(Log.getFindings().toString(), Log.getFindings().isEmpty());
    }

}
