# feed-reader
This project supports a simple feed service. The service supports the following operations.
- Create a feed
- Add article to a feed
- Subscribe/Unsubscribe a user to a feed
- Get all feeds a subscriber is following
- Get all articles from the set of feeds a subscriber is following

## How to run the service
### Building the project
From the root directory of the project, just run the following command `mvn clean install`. This will build a war named `feed-reader.war` in the target directory.

### Setting up the database
The service uses a mysql database to store the information about feeds, articles, users, subscription. The database configuration is stored in this [file](https://github.com/abhijeetcsekgp/feed-reader/blob/master/src/main/resources/config.properties). You will need to modify this file as per your mysql installation.

You will also need to create the database `feed` in your mysql database and run this sql [script](https://github.com/abhijeetcsekgp/feed-reader/blob/master/src/main/resources/sql/create_table.sql) to create the required tables in the database.

### Deploying the webapp
You will need a tomcat installation to host the webapp. Once you have installed tomcat, you can just copy the generated war to the webapps directory in the tomcat installation directory. You may have to restart tomcat if the war is not automatically extracted.

The service is now ready to use.

## APIs
The following apis are available in the service.
- POST /feed-reader/rest/feeds
  Content-Type: application/json
  Accept: application/json

  Creates a new feed. For e.g. 
  ```curl -H "Content-Type: application/json" -H "Accept: application/json" -X POST -d '{"name": "Sports", "description":"News from the sports world"}' http://localhost:8080/feed-reader/rest/feeds```
  
- POST /feed-reader/rest/feeds/{feedId}/add-article
  Accept: application/json
  Content-Type: application/json
  
  Adds an article to a feed. For e.g.
  ```curl -H "Content-Type: application/json" -H "Accept: application/json" -X POST -d '{"title":"ICC Champions Trophy", "url":"www.google.com"}' http://localhost:8080/feed-reader/rest/feeds/1/add-article```
  
- POST /feed-reader/rest/users/{emailId}/subscribe/{feedId}
  Accept: application/json
  Content-Type: application/json
  
  Subscribes a user to a feed. For e.g.
  ```curl -H "Accept: application/json" -X POST http://localhost:8080/feed-reader/rest/users/abhijeet.kumar@xyz.com/subscribe/1```
  
- DELETE /feed-reader/rest/users/{emailId}/unsubscribe/{feedId}
  Accept: application/json

  Unsubscribes a user from a feed. For e.g.
  ```curl -H "Accept: application/json" -X DELETE http://localhost:8080/feed-reader/rest/users/abhijeet.kumar@xyz.com/unsubscribe/1```
  
- GET /feed-reader/rest/users/{emailId}/subscription
  Accept: application/json

  Returns all feeds that a subscriber is following. For e.g.
  ```curl -H "Accept: application/json" -X GET http://localhost:8080/feed-reader/rest/users/abhijeet.kumar@xyz.com/subscription```
- GET /feed-reader/rest/users/{emailId}/articles
  Accept: application/json
  
  Returns all articles from the feeds a subscriber is following. For e.g.
  ```curl -H "Accept: application/json" -X GET http://localhost:8080/feed-reader/rest/users/abhijeet.kumar@xyz.com/articles```

## Project Information
The project uses jersey to provide the REST Apis. It also uses spring to provide the JAX-RS components which is used by jersey. The project uses mysql database to persist information, though it can be easily configured to use any other relational database.

### Tables
- feed (feed entity)
- article (article entity)
- _user (user entity)
- subscription (captures user and the feed subscribed by the user)
- feed_article (captures an article and the feed it belongs to)

### APIs explained
- POST /feed-reader/rest/feeds

  It expects a json with the name and description of the feed to be created. The name of the feed should be unique. The service does not allow two feeds with the same name.
  
  Payload sample: {"name": "Sports", "description":"News from the sports world"}
  
- POST /feed-reader/rest/feeds/{feedId}/add-article

  It expects a json with the title and url of the article. Currently there is no restriction on the title and url of the article. A new article is created everytime this api is called. The absence of the restriction on the title is to allow two articles from different sources with same title to exist within the same feed. The absence of restriction on url allows an 'actual' article identified by a url to exist within two feeds. (Sometimes an article can belong to more than one category). The article is added to the feed indicated in the path of the api.
  
  Payload sample: {"title":"ICC Champions Trophy", "url":"www.google.com"}
  
- POST /feed-reader/rest/users/{emailId}/subscribe/{feedId}

  It checks if a user with the passed emailId already exists in the system. It the user exists the user is subscribed to the feed. If not, a new user is created first and then subscribed to the feed.
  
- DELETE /feed-reader/rest/users/{emailId}/unsubscribe/{feedId}

  It checks if the user identified by the emailId exists in the system. If it does, the user is unsubscribed from the feed by deleting the entry corresponding to the user and feed.
  
- GET /feed-reader/rest/users/{emailId}/subscription
  
  It returns the feeds subscribed by the user as a json array.

- GET /feed-reader/rest/users/{emailId}/articles

  It returns the articles belonging to the feeds subscribed by the user as a json array.
  

