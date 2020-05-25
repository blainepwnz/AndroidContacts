package com.tomash.androidcontacts.tests

import com.tomash.androidcontacts.BaseTest
import com.tomash.androidcontacts.contactgetter.entity.ContactData
import com.tomash.androidcontacts.contactgetter.entity.PhoneNumber
import com.tomash.androidcontacts.contactgetter.main.Sorting
import com.tomash.androidcontacts.contactgetter.main.contactsGetter.ContactsGetterBuilder
import com.tomash.androidcontacts.contactgetter.main.contactsSaver.ContactsSaverBuilder
import com.tomash.androidcontacts.utils.assertContacts
import com.tomash.androidcontacts.utils.context
import com.tomash.androidcontacts.utils.createRandomContactData
import org.junit.Assert
import org.junit.Test
import java.util.ArrayList

class GetContactsTests : BaseTest() {

    private fun createRandomList(listAction: (List<ContactData>) -> Unit): List<ContactData> {
        val savedData = generateListOfRandomContacts()
        listAction(savedData)
        ContactsSaverBuilder(context)
            .saveContactsList(savedData)
        return savedData
    }

    private fun getList(builderFunc: ContactsGetterBuilder.() -> ContactsGetterBuilder): List<ContactData> {
        return builderFunc(ContactsGetterBuilder(context)
            .allFields())
            .buildList()
    }

    @Test
    @Throws(Exception::class)
    fun correctlyGetsValidContacts() {
        val savedData = createRandomList {}
        savedData.forEachIndexed { index, _ ->
            val randomContact = savedData[index]
            val savedList = getList { withName(randomContact.compositeName) }
            Assert.assertEquals(1, savedList.size)
            assertContacts(randomContact, savedList.first(), index.inc())
        }
    }

    @Test
    @Throws(Exception::class)
    fun queryingByPhoneWorksCorrectly() {
        val savedData = createRandomList {
            it.take(it.size / 2).forEach { it.phoneList.clear() }
        }
        val savedList = getList { onlyWithPhones() }
        Assert.assertEquals((savedData.size / 2).toLong(), savedList.size.toLong())
    }

    @Test
    @Throws(Exception::class)
    fun queryingByPhotoWorksCorrectly() {
        val savedData = createRandomList {
            it.take(it.size / 2).forEach { it.updatedBitmap = null }
        }
        val savedList = getList { onlyWithPhotos() }
        Assert.assertEquals((savedData.size / 2).toLong(), savedList.size.toLong())
    }

    @Test
    @Throws(Exception::class)
    fun comboQueryIsWorking() {
        val savedData = createRandomList {
            it.take(it.size / 2).forEach {
                it.updatedBitmap = null
                it.phoneList.clear()
            }
        }
        val savedList = getList { onlyWithPhotos().onlyWithPhones() }
        Assert.assertEquals((savedData.size / 2).toLong(), savedList.size.toLong())
    }

    @Test
    @Throws(Exception::class)
    fun ascendingNameSortingWorksAsExpected() {
        val savedData = createRandomList {}
        val savedList = getList { setSortOrder(Sorting.BY_DISPLAY_NAME_ASC) }
        savedData.sortedBy { it.compositeName }.forEachIndexed { index, contactData ->
            Assert.assertTrue(savedList[index].compositeName == contactData.compositeName)
        }
    }

    @Test
    fun withPhoneLikeFiltersPhones() {
        //create list with 50 contacts and save them
        createRandomList {
            //removing all random phones
            //adding numbers from example
            it.forEach { it.phoneList.clear() }
            it[0].phoneList.add(PhoneNumber("+631230001234", "1234"))
            it[1].phoneList.add(PhoneNumber("+1 123 000 1234", "1234"))
            it[2].phoneList.add(PhoneNumber("01230001234", "1234"))
            //adding one number that should be filtered
            it[3].phoneList.add(PhoneNumber("001234", "1234"))
        }
        //getting contacts from android
        val savedList = getList { withPhoneLike("1230001234") }
        //asserting that all of them were added
        Assert.assertEquals(3, savedList.size)
    }

    @Test
    fun withPhoneFiltersPhones() {
        //create list with 50 contacts and save them
        createRandomList {
            //removing all random phones
            //adding numbers from example
            it.forEach { it.phoneList.clear() }
            it.first().phoneList.add(PhoneNumber("+631230001234", "1234"))
            it.first().phoneList.add(PhoneNumber("1230001234", "1234"))
            it.first().phoneList.add(PhoneNumber("123000", "1234"))
        }
        //getting contacts from android
        //asserting that all of them were added
        val first = getList { withPhone("1230001234") }
        val second = getList { withPhone("+631230001234") }
        val third = getList { withPhone("123000") }
        //asserting that all lists are the same and containing same numbers
        Assert.assertEquals(1, first.size)
        Assert.assertTrue(first == second && second == third)
    }

    @Test
    @Throws(Exception::class)
    fun descendingNameSortingWorksAsExpected() {
        val savedData = createRandomList {}
        val savedList = getList { setSortOrder(Sorting.BY_DISPLAY_NAME_DESC) }
        savedData.sortedByDescending { it.compositeName }.forEachIndexed { index, contactData ->
            Assert.assertTrue(savedList[index].compositeName == contactData.compositeName)
        }
    }

    @Test
    fun ascendingByIsSortingWorking() {
        createRandomList {}
        getList { setSortOrder(Sorting.BY_ID_ASC) }.zipWithNext().forEach {
            Assert.assertTrue(it.first.contactId < it.second.contactId)
        }
    }

    @Test
    fun descendingByIdSortingIsWorking() {
        createRandomList {}
        getList { setSortOrder(Sorting.BY_ID_DESC) }.zipWithNext().forEach {
            Assert.assertTrue(it.first.contactId > it.second.contactId)
        }
    }

    @Throws(Exception::class)
    private fun generateListOfRandomContacts(): List<ContactData> {
        val dataList = ArrayList<ContactData>()
        for (i in 1..50) {
            dataList.add(createRandomContactData(i))
        }
        return dataList
    }
}
