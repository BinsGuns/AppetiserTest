# AppetiserTestApp

Main functionality : 

Show list of items obtained from an iTunes Search API  and show a detailed view for each item


Flow :

1) Initially when application start it shows search bar with empty list.

2) After getting data from API of Itunes I am using Realm database to save data, I choose Realm Database as it is open source and I love open source technology and I am quite familiar with realm because I already used this on some of my projects.

3) When the user left the app I am saving the latest data queried from the network and when the app restores back, I am getting the latest local copy and added a label date below the search bar for reference.

4) Everytime it will query from the API of Itunes it will replace the latest data saved from Realm Database

Libraries/Dependency Used :

-Retrofit Networking Http Call (From Square Team)
https://square.github.io/retrofit/

-Glide Image Caching library
https://github.com/bumptech/glide

-RxJava and RxAndroid
https://github.com/ReactiveX/RxAndroid


Enjoy using my test application!
