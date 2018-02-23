package fr.eql.autom.LibrePlan;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;


//Cas de test Pro_TA_04 : Affichage planning d'un projet
public class ProTa04Test {

	WebDriver driver;
	String navigateur = "chrome";
	String driverSQL = "org.postgresql.Driver";
	String jdbcURL = "jdbc:postgresql://localhost/libreplan";
	String user = "libreplan";
	String password = "libreplan";
	DesiredCapabilities dc;
	

	@Before
	public void setup() throws SQLException, Exception{

		String navo = System.getProperty("browser");
		if(navo==null) {
			dc = DesiredCapabilities.chrome();
			//driver = new ChromeDriver();
			System.out.println("Chrome");
		}
		else if (navo.equals("firefox")) {
			//System.setProperty("webdriver.gecko.driver","C:\\Users\\Formation\\Downloads\\geckodriver-v0.19.1-win64\\geckodriver.exe");
			//driver = new FirefoxDriver();
			dc = DesiredCapabilities.firefox();
			System.out.println("Firefox");
		} else {
			//driver = new ChromeDriver();
			dc = DesiredCapabilities.chrome();
			System.out.println("Default Chrome");
		}
		 
		driver = new RemoteWebDriver(new URL("http://192.168.2.22:4444/wd/hub"), dc);
	
		
	driver.get("http://192.168.2.41:8080/libreplan/common/layout/login.zul");
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		// Insertion d'un jeu de données data 
		/*
		IDatabaseTester databaseTester = new JdbcDatabaseTester(driverSQL,jdbcURL, user, password);
		IDataSet dataSet = databaseTester.getConnection().createDataSet();
		databaseTester.setSetUpOperation(DatabaseOperation.INSERT);
		IDataSet dataInsert = new FlatXmlDataSetBuilder().build(new File("src/test/dataProjet.xml"));
		databaseTester.setDataSet(dataInsert);
		databaseTester.onSetup();
				
		//Vérification de l'insertion
		ITable table2 = dataSet.getTable("order_element");
		IDataSet expected2 = new FlatXmlDataSetBuilder().build(new File("src/test/dataProjet.xml"));
		ITable expectedTable2 = expected2.getTable("order_element");
				
		Assertion.assertEquals(expectedTable2, table2);
		*/
	}	
	
	
	
//Corps du test de création de projet
	@Test
	public void test() throws InterruptedException {
		
		//1- Création de la page de connexion
		LogPage log = PageFactory.initElements(driver, LogPage.class);
		//1- Redirection vers la page d'accueuil grâce à la méthode de connexion issue de la page-objet correspondante avec les identifiants admin.
		ProjectsPlanningPage plan = log.connexion("admin", "admin");
		
		//2- Redirection vers la liste des planning via un click sur l'onglet correspondant.
		ProjectsListPage list = plan.clickProjectslist();
		Thread.sleep(1000);
		
		//3- Accès à la page édition du projet
		ProjectDetailsPage project = list.selectionProject("PROJET_TEST1");
		
		//4- Redirection vers la page de visualisation du planning
		ProjectSchedulingPage schedule = project.clickProjectScheduling();
		Thread.sleep(1000);
		
		//5- Changement de vue vers la vue Année
		schedule.menuDeroulantVue("Year");
		
		//6- Changement de vue vers la vue Trimestre
		schedule.menuDeroulantVue("Quarter");
		
		//7- Changement de vue vers la vue Mois
		schedule.menuDeroulantVue("Month");
		Thread.sleep(1000);
		
		//suppression du projet
		schedule.listeProjet();
		Thread.sleep(1000);

		
		schedule.supprimerProjet();
		Thread.sleep(1000);
		schedule.supprOk();
		
		
	}

	
	
	
	
	//Réinitialisation de la base de données
	/*
	@After
	public void teardown() throws SQLException, Exception{
	
		IDatabaseTester tester = new JdbcDatabaseTester(driverSQL,jdbcURL, user, password);
		IDataSet dataSet = tester.getConnection().createDataSet();
		
		//Vérification de l'insertion
		IDatabaseTester tester = new JdbcDatabaseTester(driverSQL,jdbcURL, user, password);
		IDataSet dataSet = tester.getConnection().createDataSet();
		ITable table = dataSet.getTable("order_element");
		
		IDataSet expected2 = new FlatXmlDataSetBuilder().build(new File("src/test/dataProjet.xml"));
		ITable expectedTable2 = expected2.getTable("order_element");
		
		Assertion.assertEquals(expectedTable2, table);
		
		//Suppresion des données insérées
	
		IDataSet dataSet2 =new FlatXmlDataSetBuilder().build(new File("src/test/dataTask.xml"));
		tester.setTearDownOperation(DatabaseOperation.DELETE);
		tester.setDataSet(dataSet2);
		tester.onTearDown();
		
		//Vérification de la suppresion
		ITable table = dataSet.getTable("order_element");
		IDataSet expected = new FlatXmlDataSetBuilder().build(new File("src/test/dataEmpty.xml"));
		ITable expectedTable = expected.getTable("order_element");
		
		Assertion.assertEquals(expectedTable, table);
	}
	*/
}
