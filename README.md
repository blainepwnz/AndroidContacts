AndroidContacts
===================
----------------------------------
[![GitHub license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/blainepwnz/AndroidContacts/blob/master/LICENSE.txt)
[![Download](https://api.bintray.com/packages/blainepwnz/maven/androidcontacts/images/download.svg) ](https://bintray.com/blainepwnz/maven/androidcontacts/_latestVersion)


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
		implementation 'com.tomash:androidcontacts:1.12.5'
	}
```


Basic Usage
------------------


>The minimum API level supported by this library is API 15.  
>Library is not handling 23+ API permission.

Need to add permission to read contacts in manifest.
```
<uses-permission android:name="android.permission.READ_CONTACTS"/>
<uses-permission android:name="android.permission.WRITE_CONTACTS"/>
```

##### Basic usage
1. Get all contacts from android device
2. Get specific data from contacts
3. Querying inside contacts
4. Save new contacts  

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
* Is favorite

Easiest way to get all data by one time:
```
 new ContactsGetterBuilder(ctx)
            .allFields()
            .buildList();
```
Filter by contacts only with numbers
```
 new ContactsGetterBuilder(ctx)
          .onlyWithPhones()
		  .buildList();          
```


Querying
------------------
Library supports multi querying by contacts,plus you can implement your own filters.

For example , query that gets all contacts with photo and containing sequence "abc" in name.
```
new ContactsGetterBuilder(ctx)
    .onlyWithPhotos()
    .addField(FieldType.EMAILS,FieldType.ADDRESS)
    .withNameLike("abc");
```              
 
 Get contact with specific phone number.
 firstOrNull returns only one Contact if found and null otherwise.
```
new ContactsGetterBuilder(ctx)
    .withPhone("123456789")
    .firstOrNull();   
```

Get contact by local contact id
```
ContactData contactData  = new ContactsGetterBuilder(ctx)
   .addField(FieldType.EMAILS, FieldType.ADDRESS, FieldType.NAME_DATA)
   .getById(123);
```    

Saving new contacts
-------------------

How to save contacts objects using library?

1. Create ContactData object or use yours

	```
    ContactData data = ContactDataFactory.createEmpty();
	```

2. Fill it with data

	```
    data.setCompositeName("Name");
    List<Email> emailList  = new ArrayList<>();
    //creates email with custom label
    emailList.add(new Email("hello@gmail.com","custom label"));
    //creates email with home label
    emailList.add(new Email(context,"home_email@gmail.com",Email.TYPE_HOME));
    //creates email with default label
    emailList.add(new Email(context,"default@gmail.com"));
	```

3. Save data to phone

	```
    //this id is id of newly created contact
    int id = new ContactsSaverBuilder(context)
        .saveContact(data);
	```

####Note
To save list of contacts use:
```
    int[] id = new ContactsSaverBuilder(context)
        .saveContactsList(contactDataList);
```

Using custom Contact object
------------------
##### How to use library with your custom contact class?
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
List<MyAwesomeContactObject> objects  = new ContactsGetterBuilder(ctx)
    .onlyWithPhones()
    .onlyWithPhotos()
    .withEmailLike("@gmail.com")
    .buildList(MyAwesomeContactObject.class);
```

Whats new?
------------------
### 1.12.5
> * Fixed possibility of NPE during equals of contact data

### 1.12.4
> * Added isFavorite to detect and save favorite contacts

### 1.12.3
> * Removed unnecessary properties from manifest

### 1.12.2
> * Fixed issue with phone formatting during searching by number

### 1.12.1
> * Fixed regressing with filters
> * Fixed regression with sorting

### 1.12.0
> * Added account type and account name in ContactData to support saving and getting account names for contacts  

### 1.11.0
> * Added possibility to save bitmap or uri with photos of contact
> * Minor refactor  

### 1.10.0
> * Added contacts saver
> * Fixed bugs for Android api<18
> * Covered contacts saver and getter with tests
> * Reworked WithLabel objects creation

### 1.0.6
> * Improved contacts getter performance

### 1.0.5 
> * Added support of custom Contact objects
> * Added lookup key

