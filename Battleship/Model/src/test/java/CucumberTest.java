import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features= "src/test/resources/featureFiles", publish=true)
public class CucumberTest {

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
