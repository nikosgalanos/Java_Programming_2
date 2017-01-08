# WebMasters

**WebMasters** is a group created by 10 students of the Department of Management Science and Technology  
of the Athens University of Economics and Business for the purposes of the group-project of the 3rd semester  
course "Java Programming 2".

## Team Goal

Our team's objective was to build a web crawler written in Java for crawling an acceptable part of the Web  
so as to provide all the required data that a search engine needs to do its job.

## Program Description

### How to use it

Our crawler is fully written in Java (version 7+) and it's output is a local database on Microsoft SQL Server.  
In order to execute the program you just need to use the created .jar file by giving the following command in the cmd
java -jar Beta_1.3.jar http://www.startingurl.com  
where starting url is the first website you want to crawl. You also need to have already created a table with two columns in a local database using the Microsoft SQL Server and a folder in your computer in which all the downloaded html pages will be saved.

### Short Program Description

Our web crawler consists of the following 6 classes:
* LinksEx
* PrefixSuffixCheck
* DownloadHtml
* DataBaseConn
* Crawl
* Demo

**LinksEx** is a class which creates a connection to a website in order to extract all the links leading to the same or another website.  
As this program is only made for university purposes not all of the found urls are appropriate and useful.

**PrefixSuffixCheck** does exactly the pre-described check in each url found. It checks if it's prefix is in the appropriate form  
(http://www.urlfound.com) and if it's not it fixes it using regular expressions. It also checks it's suffix because some url types such as .jpeg or .ico are not usefull for the purposes of this project so it rejects them.

**DownloadHtml** is a class which creates some sub-folders into your already created folder, giving them the name of the website that it's html pages will be saved. For example all the html pages coming from the website www.aueb.gr will be saved in a folder named "aueb". It then downloads the html page of all the approved and fixed urls that our crawler has already visited.

**DataBaseConn** is using an SQL driver in order to make a jdbc connection between our Java program and Microsoft SQL Server. It reads a HashMap in which all the visited urls are saved as keys and the computer paths in which the downloaded html page is saved, are saved as values, and then it inserts these data in the pre-created SQL table by making an "insert" statement.

**Crawl** is the "heart" of our program. It coordinates all of the pre-described functions by properly creating objects of the previous classes and it also handles everything that can go wrong so as the program will not crash at any point. 

**Demo** class is nothing more than the class that contains the main method which makes the program run by creating an object of the _Crawl_ class.

## Future Work

For the time being there isn't a plan to improve or expand our crawler as we believe it serves the purposes of our course. If our professor believes that there is some further work to be done or that our program has perspective, then we might continue this project by his leading.

## Contributors

1. Βαλεριάνος Ιωάννης
2. Γαλανός Νικόλαος (team leader)
3. Εξαρχάκος Παναγιώτης
4. Ιωαννίδη Χρυσαυγή - Μαρία
5. Κασελούρης Αθανάσιος
6. Μαλανδράκης Ευάγγελος
7. Μάρκου Ασημένια
8. Μπάρτζα Μαρία
9. Νταλάρης Φίλιππος
10. Χιώτης Νικόλαος

## Acknowledgements

Special thanks to our professor **Diomidis Spinellis** for the opportunity to work in a closer to reality project,using certain programming tools and understanding how to co-operate in a group consisted of a large number of members. We would also like to thank our professor's assistants **Kechagia Maria** and **Georgiou Stefanos** for supporting us whenever we were in need of some help or directions.
Special thanks also to every member of our team for contributing their time and programming skills in order to achieve what in the beginning looked almost impossible.

