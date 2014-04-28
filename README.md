fii-admis
=========

Repository for FII school project (CSS)



GUI Application
------------------
- Written in Javascript
- User Interface for the students and for the admin



Custom database
---------------
- Structure:

```
candidates
==========
candidate_id {4 alphanumeric characters}
first_name {max 20 chars}
last_name {max 20 chars}
social_id {13 chars exactly}
gpa_grade {4-5 chars 0.00 - 10.00}
a_test_grade {4-5 chars 0.00 - 10.00}

admission_results
=================

admission_result_id {4 alphanumeric characters}
candidate_id {candidates-foreign-key, 4 alphanumeric characters}
final_grade {4-5 chars 0.00 - 10.00}
status {0(tax-free), 1(tax) or 2(rejected)}

Exemplu de doua inregistrari in tabela 'candidates'
ad4e:sandu:istrate:1900201345644:9.55:8.7
15a9:alexandru:ivan:1900201345644:7.67:5.67
```

- Sample:

```
ad4e:sandu:istrate:1900201345644:9.55:8.7
15a9:alexandru:ivan:1900201345644:7.67:5.67
```



REST Service
------------
- Deployment: The REST service is deployed with [AWS Elastic Beanstalk].

- API Documentation:
http://fii-admis-restservice-dt5dd3kc2v.elasticbeanstalk.com/



Tools and Libraries
-------------------

- Language: Java 7
- [Github for Source Control]
- [AWS Elastic Beanstalk]
- [Apache Maven]
- [Google Guava Libraries]
- [Restlet Framework]



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
