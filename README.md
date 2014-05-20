![ScreenShot](http://www.javafxdata.org/img/datafx-logo.png)
What you’ve stumbled upon here is a project that intends to make retrieving, massaging, populating, viewing, and editing data in JavaFX UI controls easier. It’s all that boring kludge work you have to do between getting user requirements and delivering a rich user experience.

### Overview ###

The road between a JavaFX Application and a back-end system contains different areas. We hope that this projects helps developers crossing at least some of these areas:

* Firstly, by providing various data source adapters to ensure convenience around populating JavaFX controls such as ListView, TreeView, TableView and your custom controls or layout components. This is achieved by abstracting away the implementation details surrounding data source retrieval and massaging, such that data can be rapidly loaded and seen on screen. We also provide convenience around features such as sorting, filtering, on-demand loading and write-back support for data that changed locally.
* Secondly, by providing various control cell factories to ensure convenience around viewing and editing data in JavaFX controls such as ListView, TreeView and TableView. We do this by abstracting away the implementation details for a number of common JavaFX cell factories such that they can be easily installed and used in your user interface.
* Thirdly, by providing a framework that handles flow and contexts and that integrates very well with FXML. This part helps you maintening state between different user actions, and managing the possible flows in a client application.