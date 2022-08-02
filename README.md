AndroidContacts
===================
----------------------------------
[![GitHub license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/blainepwnz/AndroidContacts/blob/master/LICENSE.txt)
[![](https://jitpack.io/v/blainepwnz/AndroidContacts.svg)](https://jitpack.io/#blainepwnz/AndroidContacts)


Gradle Dependency
---------------------------
**Step 1.** Add it in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
    }
}
```
**Step 2.** Add the dependency
```
dependencies {
	implementation("com.github.blainepwnz:AndroidContacts:1.14.0")
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
5. Delete contacts
5. Managing blocked contacts

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

Querying
------------------

Example: query that gets all contacts with photo and containing sequence "abc" in name.
```
new ContactsGetterBuilder(ctx)
    .onlyWithPhotos()
    .addField(FieldType.EMAILS, FieldType.ADDRESS)
    .withNameLike("abc");
```

Saving new contacts
-------------------

```
ContactData data = ContactDataFactory.createEmpty();
...Fill it with data
// id of newly created contact
int id = new ContactsSaverBuilder(context)
    .saveContact(data);
```

Deleting contacts
-------------------

```
val contactData = /* get contact data from somewhere */
val deleter = ContactsDeleter(context)
//usual delete with no need of callbacks
deleter.deleteContact(contactData)
//full range of callbacks, implement any you need
deleter.deleteContact(contactData) {
    onCompleted { }
    onFailure { }
    onResult { }
    doFinally { }
}
```

#### Additional info
[More documentation](https://github.com/blainepwnz/AndroidContacts/wiki/Documentation)

#### Changelog
[More documentation](https://github.com/blainepwnz/AndroidContacts/blob/master/CHANGELOG.md)
