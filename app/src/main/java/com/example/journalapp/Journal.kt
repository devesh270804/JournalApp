package com.example.journalapp

import com.google.firebase.Timestamp

data class Journal(
    var title: String = "",
    var thoughts: String = "",
    var imageUrl: String = "",
    var userId: String = "",
    var timeAdded: Timestamp = Timestamp.now(),
    var username: String = ""
)


//data class Journal(
//    var title: String? = null,
//    var thoughts: String? = null,
//    var imageUrl: String? = null,
//    var userId: String? = null,
//    var timeAdded: Timestamp? = null,
//    var username: String? = null
//)