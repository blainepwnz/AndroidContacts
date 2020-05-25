package com.tomash.androidcontacts.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import com.tomash.androidcontacts.contactgetter.entity.Address
import com.tomash.androidcontacts.contactgetter.entity.ContactData
import com.tomash.androidcontacts.contactgetter.entity.Email
import com.tomash.androidcontacts.contactgetter.entity.IMAddress
import com.tomash.androidcontacts.contactgetter.entity.Organization
import com.tomash.androidcontacts.contactgetter.entity.PhoneNumber
import com.tomash.androidcontacts.contactgetter.entity.Relation
import com.tomash.androidcontacts.contactgetter.entity.SpecialDate
import com.tomash.androidcontacts.contactgetter.interfaces.WithLabel
import com.tomash.androidcontacts.contactgetter.main.ContactDataFactory
import com.tomash.androidcontacts.contactgetter.main.contactsGetter.ContactsGetterBuilder
import com.tomash.androidcontacts.contactgetter.main.contactsSaver.ContactsSaverBuilder
import org.assertj.core.api.Assertions
import org.junit.Assert
import java.security.SecureRandom

lateinit var context: Context
private val secureRandom = SecureRandom()

@Throws(Exception::class)
fun <T : WithLabel?> generateRandomWithLabel(clazz: Class<T>): T {
    val mainData = randomString(8)
    return if (secureRandom.nextBoolean()) {
        val labelName = randomString(4)
        clazz.getConstructor(String::class.java, String::class.java).newInstance(mainData, labelName)
    } else {
        val labelId = secureRandom.nextInt(3) + 1
        return clazz.getConstructor(Context::class.java,
            String::class.java,
            Int::class.javaPrimitiveType)
            .newInstance(context, mainData, labelId)
    }
}

fun createRandomContactData(bitmapSize: Int = 1): ContactData {
    val contactData = ContactDataFactory.createEmpty()
    contactData.updatedBitmap = Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.ARGB_8888)
    contactData.compositeName = randomString()
    contactData.nickName = randomString()
    contactData.sipAddress = randomString()
    contactData.accountType = randomString()
    contactData.accountName = randomString()
    contactData.organization = Organization(randomString(), randomString())
    contactData.note = randomString()
    contactData.phoneList.add(generateRandomWithLabel(PhoneNumber::class.java))
    contactData.phoneList.add(generateRandomWithLabel(PhoneNumber::class.java))
    contactData.phoneList.add(generateRandomWithLabel(PhoneNumber::class.java))
    contactData.addressesList.add(generateRandomWithLabel(Address::class.java))
    contactData.addressesList.add(generateRandomWithLabel(Address::class.java))
    contactData.addressesList.add(generateRandomWithLabel(Address::class.java))
    contactData.emailList.add(generateRandomWithLabel(Email::class.java))
    contactData.emailList.add(generateRandomWithLabel(Email::class.java))
    contactData.emailList.add(generateRandomWithLabel(Email::class.java))
    contactData.relationsList.add(generateRandomWithLabel(Relation::class.java))
    contactData.relationsList.add(generateRandomWithLabel(Relation::class.java))
    contactData.relationsList.add(generateRandomWithLabel(Relation::class.java))
    contactData.specialDatesList.add(generateRandomWithLabel(SpecialDate::class.java))
    contactData.specialDatesList.add(generateRandomWithLabel(SpecialDate::class.java))
    contactData.specialDatesList.add(generateRandomWithLabel(SpecialDate::class.java))
    contactData.imAddressesList.add(generateRandomWithLabel(IMAddress::class.java))
    contactData.imAddressesList.add(generateRandomWithLabel(IMAddress::class.java))
    contactData.imAddressesList.add(generateRandomWithLabel(IMAddress::class.java))
    contactData.websitesList.add(randomString())
    contactData.websitesList.add(randomString())
    contactData.websitesList.add(randomString())
    return contactData
}

fun mockContactDataList(size: Int = 50,
    init: ContactData.() -> Unit = {}) =
    MutableList(size) { ContactDataFactory.createEmpty().apply(init) }

fun clearAllContacts() {
    //another method for deleting contacts not to intersect with code from library
    val cr = context.contentResolver
    val cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
        null, null, null, null)
    while (cur!!.moveToNext()) {
        try {
            val lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
            val uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey)
            cr.delete(uri, null, null)
        } catch (e: java.lang.Exception) {
        }
    }
}

fun assertContacts(userContact: ContactData, contactFromDb: ContactData, bitmapSize: Int) {
    val saved = getBitmapFromContactData(contactFromDb)
    Assert.assertTrue(saved.height == bitmapSize && saved.width == bitmapSize)
    userContact.compositeName isEqual contactFromDb.compositeName
    userContact.note isEqual contactFromDb.note
    userContact.nickName isEqual contactFromDb.nickName
    userContact.sipAddress isEqual contactFromDb.sipAddress
    userContact.organization isEqual contactFromDb.organization
    userContact.accountType isEqual contactFromDb.accountType
    userContact.accountName isEqual contactFromDb.accountName
    Assertions.assertThat(userContact.emailList).containsExactlyInAnyOrderElementsOf(contactFromDb.emailList)
    Assertions.assertThat(userContact.phoneList).containsExactlyInAnyOrderElementsOf(contactFromDb.phoneList)
    Assertions.assertThat(userContact.addressesList).containsExactlyInAnyOrderElementsOf(contactFromDb.addressesList)
    Assertions.assertThat(userContact.websitesList).containsExactlyInAnyOrderElementsOf(contactFromDb.websitesList)
    Assertions.assertThat(userContact.imAddressesList).containsExactlyInAnyOrderElementsOf(contactFromDb.imAddressesList)
    Assertions.assertThat(userContact.specialDatesList).containsExactlyInAnyOrderElementsOf(contactFromDb.specialDatesList)
    Assertions.assertThat(userContact.relationsList).containsExactlyInAnyOrderElementsOf(contactFromDb.relationsList)
}

infix fun <T> T.isEqual(target: T) =
    Assert.assertEquals(this, target)

fun getAllContacts() = ContactsGetterBuilder(context)
    .allFields()
    .buildList()

fun List<ContactData>.saveAll() = ContactsSaverBuilder(context)
    .saveContactsList(this)

fun getBitmapFromContactData(contactFromDb: ContactData): Bitmap {
    return MediaStore.Images.Media.getBitmap(context.contentResolver, contactFromDb.photoUri)
}
