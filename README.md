# TAF (Test Automation Framework)

**Notes:**

1)  It is necessary to replace the drivers in the folders webdrivers based on which operating system(s)
you want to run against as they are just placeholders (text files)

2)  It is necessary to manually install the Oracle (and/or SQL Server) driver.  Also, modification of the OracleInstance (and/or SqlServerInstance) class will be required.

3)  It is necessary to replace the DLL files if using SQL Server with integrated security as they are just placeholders (text files)

## Basic overview of the core classes that are mainly used

### RunTests
This class is used to run the suites from Jenkins, Local or standalone.

### TestProperties
This class holds all the test properties that can be configured such as browser to use, element time out, etc.

### TestNGBase
Every test should extend this class.  It handles basic functionality such as taking screenshots on failure, installation of the drivers & closing the driver.

### TestContext
This class provides context specific objects to the user such as the driver, default aliases, etc.  This class is not instantiated by the user.  The TestNGBase handles initialization and provides a method get the context when necessary.

### DomainObject
This class mainly handles the marshalling to XML & unmarshalling from XML back to an object.  In general, you would create a custom domain object that extends this class with all your page objects for the test.

### PageObjectV2
This is an enhanced version of the PageObject that handles the JavascriptException thrown by geckodriver sometimes instead of the StaleElementReferenceException.  Also, it has an initialization method to handle dynamic locators.  Methods that end in V2 have been enhanced in some way, the naming convention is used to allow the use of the older version 1 methods if desired.

The method setElementValueV2 is the most used here as it sets the components value and validates using the component specific validation method.

## Basic overview of the utility classes that are mainly used

### Utils
Various methods for common tasks.  Mainly used to get WebDriverWait for ExpectedConditions

### ExpectedConditionsUtil
This class has custom ExpectedConditions and any new additions to the ExpectedConditions that are later than our current version.

### Helper
Class for general helper methods.  Mainly used for logging to the console.  Also, used in conjunction with AssertAggregator.

### AssertAggregator
Class to accumulate assert failures to throw a single assertion later.  The main use case is performing multiple assertions on an object in which you want all assertions executed always.

### AssertsUtil
This class holds useful Matcher methods for assertions.

### JsUtils
This class provides utility methods to work with JavaScript

### Conditional
This class is used to find the first condition that matches a list of possible conditions. An example of
 this would be if 2 possible outcomes of an action are a button becomes ready or a button is removed. You
 want to handle each condition differently as such you need to detect which condition occurred. This class
 will allow you to determine this.

### Failsafe
This class is used to retry specific steps instead of the entire test.

### Accessibility
This class is for testing page accessibility.  The page objects should expose a method to test the accessibility that uses PageObjectV2.performAccessibilityTest

### WebDriverTypeEnum
This class handles initialization of drivers.  In general, this class is not directly used.  It would only be necessary to use this class when multiple windows need to be opened at the same time.

### URLUtils
Utilities class to work with URIBuilder.  It is mainly makes constructing the resource path for API requests easier.

### CryptoUtils
This class is used when encryption and/or decryption is required.

### MySqlInstance
Class to work with a MYSQL database

### OracleInstance
Class to work with a Oracle database

### SqlServerInstance
Class to work with a SQL Server database
