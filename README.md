# TAF (Test Automation Framework)

Note:  It is necessary to replace the drivers in the folders webdrivers based on which operating system(s)
you want to run against as they are just placeholders (text files)

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
