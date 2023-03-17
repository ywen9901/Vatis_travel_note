package com.example.vatis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vatis.fragments.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ShareActivity : AppCompatActivity() {
    companion object {
        lateinit var planRef: DocumentReference

        lateinit var shareOptionsFragment: ShareOptionsFragment

        lateinit var shareIgOptionsFragment: ShareIgOptionsFragment
        lateinit var shareIgTemplateFragment: ShareIgTemplateFragment
//        lateinit var shareIgOutputFragment: ShareIgOutputFragment

        lateinit var shareJournalOptionsFragment: ShareJournalOptionsFragment
        lateinit var shareJournalTemplateFragment: ShareJournalTemplateFragment
//        lateinit var shareJournalOutputFragment: ShareJournalOutputFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        val userEmail = intent.getStringExtra("userEmail")!!
        val folderName = intent.getStringExtra("folderName")!!
        val planName = intent.getStringExtra("planName")!!

        // init plan reference to pass in
        planRef = Firebase.firestore
            .collection("users")
            .document(userEmail)
            .collection(folderName)
            .document(planName)

        // init fragments
        shareOptionsFragment = ShareOptionsFragment(planRef)

        shareIgOptionsFragment = ShareIgOptionsFragment(planRef)
        shareIgTemplateFragment = ShareIgTemplateFragment(planRef)
//        shareIgOutputFragment = ShareIgOutputFragment(planRef)

        shareJournalOptionsFragment = ShareJournalOptionsFragment(planRef)
        shareJournalTemplateFragment = ShareJournalTemplateFragment(planRef)
//        shareJournalOutputFragment = ShareJournalOutputFragment(planRef)

        initFragment()
    }

    private fun initFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .add(R.id.share_fragment_container, shareOptionsFragment, "SHARE_OPTIONS")
            .add(R.id.share_fragment_container, shareIgOptionsFragment, "SHARE_IG_OPTIONS")
            .add(R.id.share_fragment_container, shareIgTemplateFragment, "SHARE_IG_TEPLATE")
//            .add(R.id.share_fragment_container, shareIgOutputFragment, "SHARE_IG_OUTPUT")
            .add(R.id.share_fragment_container, shareJournalOptionsFragment, "SHARE_JOURNAL_OPTIONS")
            .add(R.id.share_fragment_container, shareJournalTemplateFragment, "SHARE_JOURNAL_TEPLATE")
//            .add(R.id.share_fragment_container, shareJournalOutputFragment, "SHARE_JOURNAL_OUTPUT")
            .hide(shareIgOptionsFragment)
            .hide(shareIgTemplateFragment)
//            .hide(shareIgOutputFragment)
            .hide(shareJournalOptionsFragment)
            .hide(shareJournalTemplateFragment)
//            .hide(shareJournalOutputFragment)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}

