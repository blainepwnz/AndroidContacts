AndroidContactProvider
===================
----------------------------------
[![GitHub license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/blainepwnz/AndroidContacts/blob/master/LICENSE.txt)


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
		compile 'com.tomash:androidcontacts:1.0.3'
	}
```

---


 Basic Usage
------------------


>The minimum API level supported by this library is API 15.

Need to add permisson to read contacts in manifest.
```
<uses-permission android:name="android.permission.READ_CONTACTS"/>
```

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
---
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
 
 Get contact with specific phonenumber

        new ContactsGetter.Builder(ctx)
            .withPhone("123456789")
            .firstOrNull();   

firstOrNull returns only one Contact if found and null otherwise.
