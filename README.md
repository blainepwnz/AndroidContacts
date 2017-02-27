AndroidContacts
===================
----------------------------------
[![GitHub license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/blainepwnz/AndroidContacts/blob/master/LICENSE.txt)
[ ![Download](https://api.bintray.com/packages/blainepwnz/maven/androidcontacts/images/download.svg) ](https://bintray.com/blainepwnz/maven/androidcontacts/_latestVersion)


Gradle Dependency
---------------------------
**Step 1.** Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			jcenter()
    	}
	}
```
**Step 2.** Add the dependency
```
	dependencies {
		compile 'com.tomash:androidcontacts:1.0.5'
	}
```




 Basic Usage
------------------


>The minimum API level supported by this library is API 15.

Need to add permisson to read contacts in manifest.
```
<uses-permission android:name="android.permission.READ_CONTACTS"/>
```

##### Basic usage
1. Get all contacts from android device
2. Get specific data from contacts
3. Querying inside contacts
#### Type of fields you can get

* Phones
* Emails
* Addresses
* Groups
* Websites
* Nickname
* Note
* SIP
* Name
* Events
* Relations
* Photo
* Instant Messenger Addresses
* Lookup key

Easiest way to get all data by one time:
```
 new ContactsGetter.Builder(ctx)
            .allFields()
            .buildList();
```
Filter by contacts only with numbers
```
 new ContactsGetter.Builder(ctx)
          .onlyWithPhones()
		  .buildList();          
```


Querying
------------------
Library supports multi quering by contacts,plus you can implement your own filters.

For example , query that gets all contacts with photo and containing sequence "abc" in name.
```
new ContactsGetter.Builder(ctx)
    .onlyWithPhotos()
    .addField(FieldType.EMAILS,FieldType.ADDRESS)
    .withNameLike("abc");
```              
 
 Get contact with specific phone number.
 firstOrNull returns only one Contact if found and null otherwise.
```
new ContactsGetter.Builder(ctx)
    .withPhone("123456789")
    .firstOrNull();   
```

Get contact by local contact id
```
ContactData contactData  = new ContactsGetter.Builder(ctx)
   .addField(FieldType.EMAILS,FieldType.ADDRESS,FieldType.NAME_DATA)
   .getById(123);
```    



Using custom Contact object
------------------
#####How to use library with your custom contact class?
1. Inherit from ContactData
2. Add empty constructor
3. That's all!

```
public class MyAwesomeContactObject extends ContactData {
    private String mySuperField;

    //Important to add empty constructor
    public MyAwesomeContactObject() {
    }

    //getters , setters , any logic you wish
}
```
##### How to use it after?
```
List<MyAwesomeContactObject> objects  = new ContactsGetter.Builder(ctx)
    .onlyWithPhones()
    .onlyWithPhotos()
    .withEmailLike("@gmail.com")
    .buildList(MyAwesomeContactObject.class);
```

Whats new?
------------------
### 1.0.5 
> * Added support of custom Contact objects
> * Added lookup key

