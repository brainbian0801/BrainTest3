package com.example.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Trace
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import com.example.ListNode
import com.example.R
import com.example.brainaidlserver.IPersonManager
import com.example.brainaidlserver.Person
import com.example.databinding.ActivityHomeBinding
import com.example.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        //val navController = findNavController(R.id.nav_host_fragment_content_main)
        //appBarConfiguration = AppBarConfiguration(navController.graph)
        //setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        binding.btnAddPerson.setOnClickListener {
            val result = remoteServer?.addPerson(Person("Client_Jacky"))
            Log.i("Brain", "addPerson in result = $result")
        }

        binding.btnAddPersonOut.setOnClickListener {
            val result = remoteServer?.addPersonOut(Person("Client_Jacky_out"))
            Log.i("Brain", "add Person out result = $result")
        }

        binding.btnGetPerson.setOnClickListener {
            val result = remoteServer?.persionList
            Log.i("Brain", "getPerson list = $result")
        }

        binding.btnBindService.setOnClickListener {
            Trace.beginSection("Test")
            bindPersonManagerService()
            Trace.endSection()
        }
    }


    private fun interViewProj() {
        //hashMapTest()
        var head = testListNode()
//            while(head != null) {
//                Log.i("Brain", " ${head.x}")
//                if(head.next == null) break;
//                head = head.next!!
//            }
        var revertHead = revertLinkedList(head)
        while(revertHead != null) {
            Log.i("Brain", " ${revertHead.x}")
            if(revertHead.next == null) break;
            revertHead = revertHead.next!!
        }
    }

    private fun revertLinkedList(head: ListNode?): ListNode? {
        if(head?.next == null) return head
        val newNode = revertLinkedList(head.next)
        head.next?.next = head
        head.next = null
        return newNode
    }

    private fun testListNode(): ListNode {
        val head = ListNode(1, null)
        head.next = ListNode(2, null)
        head.next?.next = ListNode(3, null)
        head.next?.next?.next = ListNode(4, null)
        return head
    }

    private var remoteServer: IPersonManager? = null

    private val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i("Brain", "onServiceConnected.......")
            remoteServer = IPersonManager.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i("Brain", "onServiceDisconnected.......")

        }
    }

    private fun bindPersonManagerService() {
        //Android 10 有一个包可见性问题需要处理，在AndroidManifest.xml中添加<queries>
        val intent = Intent()
        intent.action = "com.example.brainaidlserver.PERSON_ACTION"
        intent.`package` = "com.example.brainaidlserver"
        val bindResult = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        Log.i("Brain", "bindPersonManagerService result = $bindResult")
    }

    private fun hashMapTest() {
        val hashMap = mutableMapOf<String,String>()

        with(hashMap) {
            put("one", "Jacky")
            put("two", "Tommy")
            put("three", "Brain")
            put("four", "Kobe")
        }
        /*val hashMap2 = buildMap<Int, String> {
            this[1] = "Jacky"
            this[2] = "Tommy"
            this[3] = "Brain"
            1 to "Jacky"
            2 to "Tommy"
            3 to "Brain"
        }*/
        val hashMap3 = HashMap<Int, String>()
        with(hashMap3) {
            put(4, "Jacky")
            put(3, "Tommy")
            put(1, "Brain")
            put(2, "Kobe")
        }
        val treeMap = TreeMap<Int, String>()
        /*hashMap.forEach { entry ->
            Log.i("Brain", "hashMap key = ${entry.key}  value = ${entry.value}")
        }
        Log.i("Brain", "hashMap2 = $hashMap2 size = ${hashMap2.size}")
        hashMap2.forEach { (key, value) ->
            Log.i("Brain", "hashMap2 key = $key value = $value")
        }*/
        hashMap3.entries.forEach {
            Log.i("Brain", "hashMap3 entries key = ${it.key} value = ${it.value}")
        }
        hashMap3.forEach { (key, value) ->
            Log.i("Brain", "hashMap3 key = $key value = $value")
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}