package com.tomash.androidcontacts.tests

import com.tomash.androidcontacts.BaseTest
import com.tomash.androidcontacts.contactgetter.entity.ContactData
import org.junit.Assert
import org.junit.Test

class EqualsTests : BaseTest() {

    @Test
    @Throws(Exception::class)
    fun equalsNotCrashing() {
        Assert.assertEquals(nullContactData(), nullContactData())
    }

    private fun nullContactData() = object : ContactData() {}.apply {
        accountName = null
        accountType = null
        addressesList = null
        compositeName = null
        emailList = null
        groupList = null
        imAddressesList = null
        nameData = null
        nickName = null
        organization = null
        note = null
        websitesList = null
        updatedPhotoUri = null
        photoUri = null
        lookupKey = null
        updatedBitmap = null
        specialDatesList = null
        sipAddress = null
        relationsList = null
        phoneList = null
    }
}
