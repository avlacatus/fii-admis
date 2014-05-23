fii-admis
=========

Repository for FII school project (CSS) .

Phase 1
------------------

This is an application consisting of 3 main modules:



GUI Application
------------------
- User Interface for the students (http://fii-admis-restservice-dt5dd3kc2v.elasticbeanstalk.com/index.html) and for the admin (http://fii-admis-restservice-dt5dd3kc2v.elasticbeanstalk.com/admin.html)
- The student can visualise the list of candidates and add himself by filling a form with the following:
    - First name
    - Last name
    - Social id (in Romanian: CNP), exactly 13 digits
    - GPA grade (in Romanian: media din timpul anilor de studiu), 1.00 - 10.00
    - A-Test grade (in Romanian: media de la bacalaureat), 1.00-10.00
- After the admission results are published, the student can only visualize them
- The admin can
    - visualize the list of candidates
    - edit their details
    - delete a candidate
    - start computation of the admission results and publish them.
- The results can be exported via PDF.

The final admission grade is a computed as the average of the GPA Grade and the A-Test grade.
The final admission status is as follows:
- final grade >= 9 => TAX_FREE
- final grade >= 7 && final grade < 9 => TAX
- final grade < 7 => REJECTED



REST Service
------------
 Deployment: The REST service is deployed with [AWS Elastic Beanstalk].

 API Documentation:
http://fii-admis-restservice-dt5dd3kc2v.elasticbeanstalk.com/apidoc.html

 The request input parameters and the response are in the json format.

Custom database
---------------
- Structure - two text files representing the tables. Each record is a line with colon separated fields:

```
candidates
==========
candidate_id {4 alphanumeric characters}
first_name
last_name
social_id {13 chars exactly}
gpa_grade {1.00 - 10.00}
a_test_grade {1.00 - 10.00}

admission_results
=================

admission_result_id {4 alphanumeric characters}
candidate_id {candidates-foreign-key, 4 alphanumeric characters}
final_grade {0.00 - 10.00}
status {0(tax-free), 1(tax) or 2(rejected)}

Sample from 'candidates':
zPyN:Maria:Rotaru:2900908785634:6.9:6.9
BjSY:Georgiana:Marin:2910417104043:8.53:7.49

Sample from 'admission_results':
l0zh:zPyN:6.9:2
E8F8:BjSY:8.01:1

```

The results can be visualized via the web interface, the web service, in the json format, or in the PDF format, by clicking on a link (http://fii-admis-restservice-dt5dd3kc2v.elasticbeanstalk.com/admission_results.pdf).


Phase 2
-------------------

Phase 3
-------------------


Tools and Libraries
-------------------
- HTML 5, CSS 3
- [jQuery]
- [Twitter Bootstrap]
- Java 7
- [Restlet Framework]
- [Google Guava Libraries]
- [Github for Source Control]
- [AWS Elastic Beanstalk]
- [Apache Maven]




Team
----
- Daneliuc Ana-Maria
- Lăcătuș Alexandra
- Popoveniuc Mirela
- Popovici Adrian



[AWS Elastic Beanstalk]: http://aws.amazon.com/elasticbeanstalk/
[Eclipse IDE for Java EE Developers]: https://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/keplersr2
[JetBrains IntelliJ IDEA]: http://www.jetbrains.com/idea/
[AWS Toolkit for Eclipse]: http://aws.amazon.com/eclipse/
[Github plugin for Eclipse]: http://eclipse.github.com/
[Github for Source Control]:https://github.com
[Apache Maven]: http://maven.apache.org/
[Google Guava Libraries]: https://code.google.com/p/guava-libraries/
[Restlet Framework]: http://restlet.org/
[jQuery]: https://jquery.com/
[Twitter Bootstrap]: http://getbootstrap.com/
